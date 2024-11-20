package tech.underoaks.coldcase.state;

import com.badlogic.gdx.math.Vector2;
import org.glassfish.grizzly.utils.Pair;
import tech.underoaks.coldcase.game.Direction;

import java.util.LinkedList;
import java.util.Queue;

/**
 * TODO JavaDoc
 */
public class RemoteGameController implements AutoCloseable {
    /**
     * TODO JavaDoc
     */
    public RemoteGameController() {
        // FIXME Überflüssig?
        // WebSocketClient.getInstance().createRemoteInteractionChain(); // TODO @Jean-Luc need identifier for remote interaction chain
    }

    /**
     * TODO JavaDoc
     * @param targetPos
     * @param actionDirection
     * @return
     */
    public Queue<Pair<Vector2, Direction>> triggerAction(Vector2 targetPos, Direction actionDirection) {
        return new LinkedList<>(); // FIXME
        //return WebSocketClient.getInstance().appendRemoteInteraction(); // TODO ...(Vector2 targetPos, Direction actionDirection)
    }

    /**
     * TODO JavaDoc
     * @return
     */
    public void applyGSUQueue() {
        // FIXME Überflüssig?
        // WebSocketClient.getInstance().applyRemoteGSUs();
    }

    @Override
    public void close() {
        // FIXME Überflüssig?
        // WebSocketClient.getInstance().abortRemoteInteractionChain();
    }
}
