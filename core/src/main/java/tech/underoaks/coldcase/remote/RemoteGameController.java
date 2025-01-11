package tech.underoaks.coldcase.remote;

import tech.underoaks.coldcase.game.Interaction;

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

    /**
     * Unique identifier for the remote game controller instance. needed for callbacks
     */
    private static String remoteGameControllerInstanceId = null;

    /**
     * Unique identifier for the remote interaction chain associated with this controller. not used ATM
     */
    private final String remoteInteractionChainId;

    /**
     * Initializes a new remote game controller, establishing a remote interaction chain
     * and retrieving its unique identifier.
     * <p>
     * blocks until timeout or responseMessage completes the futureObj.
     * </p>
     */
    public RemoteGameController() throws TimeoutException {

        CompletableFuture<Object> future = new CompletableFuture<>();
        remoteGameControllerInstanceId = UUID.randomUUID().toString(); // Automatically generate a unique ID
        WebSocketMessagesManager.getInstance().createRemoteInteractionChain(remoteGameControllerInstanceId, future);
        String tmpRemoteInteractionChainId = null;

        try {
            Object returnObj = future.get(5, TimeUnit.SECONDS);// Blocks until the response is provided
            if (returnObj instanceof Messages.CreateRemoteInteractionChainResponseMessage messageObj) {
                tmpRemoteInteractionChainId = messageObj.getRemoteInteractionChainId();
            }
        } catch (ExecutionException | InterruptedException e) {
            System.err.println(Arrays.toString(e.getStackTrace()));
        }


        this.remoteInteractionChainId = tmpRemoteInteractionChainId;

        //System.out.println("GOT remote Interaction Chain Id:" + remoteInteractionChainId);
    }

    /**
     * Triggers a remote action at the specified position and direction.
     *
     * @param interaction The interaction to trigger.
     * @return a queue of interactions representing the result of the triggered action,
     * or {@code null} if the action times out, an error occurs or no Action took place.
     */
    public Queue<Interaction> triggerAction(Interaction interaction) {
        return triggerAction(interaction, false);
    }

    /**
     * Triggers a remote action at the specified position and direction with an option to suppress follow-up actions.
     *
     * <p>
     * needed to not create an endless loop of creating remote interaction chains.
     * </p>
     *
     * @param interaction                  The interaction to trigger.
     * @param suppressTranscendentFollowUp whether to suppress follow-up actions.
     * @return a queue of interactions representing the result of the triggered action,
     * or {@code null} if the action times out or an error occurs.
     */
    public Queue<Interaction> triggerAction(Interaction interaction, boolean suppressTranscendentFollowUp) {

        CompletableFuture<Object> future = new CompletableFuture<>(); //used for synchronisation

        WebSocketMessagesManager.getInstance().appendRemoteInteraction(remoteGameControllerInstanceId, future, interaction, suppressTranscendentFollowUp);


        try {
            Object returnObj = future.get(3, TimeUnit.SECONDS);// Block until the response is provided
            if (returnObj instanceof Messages.AppendRemoteInteractionResponseMessage messageObj) {
                return messageObj.getInteractions();
            }
        } catch (ExecutionException | InterruptedException e) {
            System.err.println(Arrays.toString(e.getStackTrace()));
        } catch (TimeoutException e) {
            System.err.println("TIMEOUT in triggerAction");
            return null; // return in case of timeout
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
