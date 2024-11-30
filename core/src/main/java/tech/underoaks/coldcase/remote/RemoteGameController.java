package tech.underoaks.coldcase.remote;

import com.badlogic.gdx.math.Vector2;
import org.glassfish.grizzly.utils.Pair;
import tech.underoaks.coldcase.game.Direction;

import java.util.Arrays;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * Represents a remote game controller responsible for managing remote interactions
 * and game state updates through WebSocket communication.
 * <p>
 * This class handles the creation and management of interaction chains and provides
 * functionality to trigger actions remotely and apply or abort updates to the game state.
 * </p>
 */
public class RemoteGameController implements AutoCloseable {

    /** Unique identifier for the remote game controller instance. needed for callbacks*/
    private static String remoteGameControllerInstanceId = null;

    /** Unique identifier for the remote interaction chain associated with this controller. not used ATM*/
    private final String remoteInteractionChainId;

    /**
     * Initializes a new remote game controller, establishing a remote interaction chain
     * and retrieving its unique identifier.
     * <p>
     *     blocks until timeout or responseMessage completes the futureObj.
     * </p>
     */
    public RemoteGameController() {

        CompletableFuture<Object> future = new CompletableFuture<>();
        remoteGameControllerInstanceId = UUID.randomUUID().toString(); // Automatically generate a unique ID
        WebSocketMessagesManager.getInstance().createRemoteInteractionChain(remoteGameControllerInstanceId,future);
        String tmpRemoteInteractionChainId = null;

        try {
            Object returnObj = future.get(5, TimeUnit.SECONDS);// Blocks until the response is provided
            if(returnObj instanceof Messages.CreateRemoteInteractionChainResponseMessage messageObj){
                tmpRemoteInteractionChainId = messageObj.getRemoteInteractionChainId();
            }
        } catch (ExecutionException | InterruptedException e) {
            System.err.println(Arrays.toString(e.getStackTrace()));
            //TODO should this trigger an abort gsu?
        } catch (TimeoutException e) {
            System.err.println("TIMEOUT in createRemoteInteractionChain");
            //TODO retry?
        }


        this.remoteInteractionChainId = tmpRemoteInteractionChainId;

        //System.out.println("GOT remote Interaction Chain Id:" + remoteInteractionChainId);
    }

    /**
     * Triggers a remote action at the specified position and direction.
     *
     * @param targetPos       the target position of the action.
     * @param actionDirection the direction of the action.
     * @return a queue of interactions representing the result of the triggered action,
     * or {@code null} if the action times out, an error occurs or no Action took place.
     */
    public Queue<Pair<Vector2, Direction>> triggerAction(Vector2 targetPos, Direction actionDirection) {
        return triggerAction(targetPos, actionDirection, false);
    }

    /**
     * Triggers a remote action at the specified position and direction with an option to suppress follow-up actions.
     *
     * <p>
     *     needed to not create an endless loop of creating remote interaction chains.
     * </p>
     *
     * @param targetPos                  the target position of the action.
     * @param actionDirection            the direction of the action.
     * @param suppressTranscendentFollowUp whether to suppress follow-up actions.
     * @return a queue of interactions representing the result of the triggered action,
     * or {@code null} if the action times out or an error occurs.
     */
    public Queue<Pair<Vector2, Direction>> triggerAction(
        Vector2 targetPos, Direction actionDirection, boolean suppressTranscendentFollowUp) {

        CompletableFuture<Object> future = new CompletableFuture<>(); //used for synchronisation

        WebSocketMessagesManager.getInstance().appendRemoteInteraction(
            remoteGameControllerInstanceId, future, targetPos, actionDirection, suppressTranscendentFollowUp);
        String callId = UUID.randomUUID().toString(); // Automatically generate a unique ID

        try {
            Object returnObj = future.get(60, TimeUnit.SECONDS);// Block until the response is provided
            if (returnObj instanceof Messages.AppendRemoteInteractionResponseMessage messageObj) {
                if (!messageObj.getInteractions().isEmpty()
                    && messageObj.getInteractions().peek() instanceof Pair<?, ?>) {
                    return messageObj.getInteractions();
                }
            }
        } catch (ExecutionException | InterruptedException e) {
            System.err.println(Arrays.toString(e.getStackTrace()));
            //TODO
        } catch (TimeoutException e) {
            System.err.println("TIMEOUT in triggerAction");
            return null; // return in case of timeout
        } finally {
            //TODO may needs to remove future?
        }

        return null;//return in case server times out or responded with wrong data
    }

    /**
     * Applies all pending updates in the game state update (GSU) queue for the remote interaction chain on the other
     * client.
     */
    public void applyGSUQueue() {
        WebSocketMessagesManager.getInstance().applyRemoteGSUs(remoteInteractionChainId);
    }

    /**
     * Closes the remote game controller and aborts any pending updates in the GSU queue.
     */
    @Override
    public void close() {
        WebSocketMessagesManager.getInstance().abortRemoteGSU(remoteInteractionChainId);
    }
}
