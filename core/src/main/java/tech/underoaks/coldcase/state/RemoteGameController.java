package tech.underoaks.coldcase.state;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Vector2;
import org.glassfish.grizzly.utils.Pair;
import tech.underoaks.coldcase.game.Direction;
import tech.underoaks.coldcase.remote.Messages;
import tech.underoaks.coldcase.remote.WebSocketClient;
import tech.underoaks.coldcase.remote.WebSocketMessagesManager;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * TODO JavaDoc
 */
public class RemoteGameController implements AutoCloseable {
    private static String remoteGameControllerInstanceId = null;
    private final String remoteInteractionChainId;

    /**
     * TODO JavaDoc
     */
    public RemoteGameController() {

        CompletableFuture<Object> future = new CompletableFuture<>();
        this.remoteGameControllerInstanceId = UUID.randomUUID().toString(); // Automatically generate a unique ID
        WebSocketMessagesManager.getInstace().createRemoteInteractionChain(remoteGameControllerInstanceId,future); // TODO @Jean-Luc need identifier for remote interaction chain
        String tmpRemoteInteractionChainId = null;

        try {
            Object returnObj = future.get(60, TimeUnit.MINUTES);// Block until the response is provided
            if(returnObj instanceof Messages.CreateRemoteInteractionChainResponseMessage messageObj){
                tmpRemoteInteractionChainId = messageObj.getRemoteInteractionChainId();
            }
        } catch (ExecutionException | InterruptedException e) {
            System.err.println(Arrays.toString(e.getStackTrace()));
            //TODO
        } catch (TimeoutException e) {
            System.err.println("TIMEOUT in create remote interaktionchain");
        }


        this.remoteInteractionChainId = tmpRemoteInteractionChainId;

        System.out.println("GOT remote Interaction Chain Id:" + remoteInteractionChainId);
    }

    /**
     * TODO JavaDoc
     * @param targetPos
     * @param actionDirection
     * @return
     */
    public Queue<Pair<Vector2, Direction>> triggerAction(Vector2 targetPos, Direction actionDirection) {
        return triggerAction(targetPos, actionDirection, false);
    }

    /**
     * TODO JavaDoc
     *
     * @param targetPos
     * @param actionDirection
     * @return
     */
    public Queue<Pair<Vector2, Direction>> triggerAction(Vector2 targetPos, Direction actionDirection, boolean suppressTranscendentFollowUp) {
        CompletableFuture<Object> future = new CompletableFuture<>(); //used for synchronisation

        WebSocketMessagesManager.getInstace().appendRemoteInteraction(remoteGameControllerInstanceId, future, targetPos, actionDirection, suppressTranscendentFollowUp);
        String callId = UUID.randomUUID().toString(); // Automatically generate a unique ID

        try {
            Object returnObj = future.get(60, TimeUnit.SECONDS);// Block until the response is provided
            if (returnObj instanceof Messages.AppendRemoteInteractionResponseMessage messageObj) {
                if (!messageObj.getInteractions().isEmpty() && messageObj.getInteractions().peek() instanceof Pair<?, ?>) { //TODO chasten ok like this?
                    return (Queue<Pair<Vector2, Direction>>) returnObj;
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

        //return WebSocketClient.getInstance().appendRemoteInteraction(targetPos,actionDirection); // TODO ...(Vector2 targetPos, Direction actionDirection)
        return null;//return in case server times out or responded with wrong data
    }

    /**
     * TODO JavaDoc
     *
     * @return
     */
    public void applyGSUQueue() {
        WebSocketMessagesManager.getInstace().applyRemoteGSUs(remoteInteractionChainId);
    }

    @Override
    public void close() {
        WebSocketMessagesManager.getInstace().abortRemoteGSU(remoteInteractionChainId);
    }
}
