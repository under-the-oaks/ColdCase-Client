package tech.underoaks.coldcase.game;

import com.badlogic.gdx.math.Vector2;
import org.glassfish.grizzly.utils.Pair;
import tech.underoaks.coldcase.state.InteractionChain;
import tech.underoaks.coldcase.state.Map;
import tech.underoaks.coldcase.state.RemoteGameController;
import tech.underoaks.coldcase.state.Snapshot;
import tech.underoaks.coldcase.state.tileContent.TileContent;
import tech.underoaks.coldcase.state.tileContent.UpdateTileContentException;
import tech.underoaks.coldcase.state.tileContent.VisibilityStates;
import tech.underoaks.coldcase.state.tiles.Tile;
import tech.underoaks.coldcase.state.updates.GameStateUpdate;
import tech.underoaks.coldcase.state.updates.GameStateUpdateException;

import java.util.Stack;
import java.util.Queue;
import java.util.LinkedList;

/**
 * Central manager responsible for handling interactions within the game.
 */
public class GameController {
    /**
     * Singleton instance of GameController
     */
    private static GameController instance;

    /**
     * Active game map
     */
    private Map currentMap;

    /**
     * TODO JavaDoc
     */
    private final Stack<InteractionChain> interactions = new Stack<>();

    /**
     * Retrieves the singleton instance of the GameController.
     *
     * @return The singleton GameController instance.
     */
    public static GameController getInstance() {
        if (instance == null) {
            instance = new GameController();
        }
        return instance;
    }

    /**
     * TODO
     * @param targetPos
     * @param actionDirection
     * @return
     */
    public boolean triggerAction(Vector2 targetPos, Direction actionDirection) {
        InteractionChain chain = createInteractionChain();

        // Trigger local action
        try {
            InteractionChain testChain = createInteractionChain(chain);
            interactions.push(testChain);
            boolean result = triggerAction(testChain, targetPos, actionDirection);
            if(!result) {
                return false;
            }

            triggerQueuedLocalActions(chain);
            triggerQueuedRemoteActions(chain);

            // Apply local changes
            chain.getGSUQueue().addAll(testChain.getGSUQueue());
        }
        finally {
            interactions.pop();
        }

        applyGSUQueue(currentMap, chain.getGSUQueue());
        return true;
    }

    /**
     * FIXME JavaDoc
     * @param targetPos
     * @param actionDirection
     * @return
     */
    public boolean triggerAction(InteractionChain testChain, Vector2 targetPos, Direction actionDirection) {
        RemoteGameController remote = null;
        try {
            TileContent handler = triggerLocalAction(testChain, targetPos, actionDirection);
            if(handler == null) {
                return false;
            }

            // If local action was transcendent, trigger remote action
            if(handler.getVisibilityState().equals(VisibilityStates.TRANSCENDENT)) {
                remote = new RemoteGameController();
                Queue<Pair<Vector2, Direction>> newRemoteActions = remote.triggerAction(targetPos, actionDirection);
                testChain.getPendingRemoteActions().addAll(newRemoteActions);
            }

            return true;
        }
        finally {
            if(remote != null) {
                remote.close();
            }
        }
    }

    /**
     * FIXME JavaDoc
     * Initiates an action at a specified position targeting another position.
     *
     * @param actionDirection The direction in which the action get triggered.
     * @param targetPos       The target position where the action is applied.
     * @return True if the action was successfully triggered, false otherwise.
     */
    public TileContent triggerLocalAction(InteractionChain chain, Vector2 targetPos, Direction actionDirection) {
        Map snapshotMap = chain.getSnapshot().getSnapshotMap();

        // Requesting an action handler to respond to the triggered action
        if (snapshotMap.isOutOfBounds(targetPos)) {
            return null;
        }
        Tile targetTile = snapshotMap.getTile(targetPos);
        if (targetTile == null) {
            return null;
        }

        TileContent targetTileContent = targetTile.getTileContent();
        if (targetTileContent == null) {
            return null;
        }

        // Actions will be handled inside a secured body to ensure only valid actions will be
        // applied to the running instance
        TileContent handler;
        try {
            // Trigger initial action
            handler = targetTileContent.handleAction(chain, targetPos, actionDirection);
            if (handler == null) {
                return null;
            }

            // Update due to potential changes
            snapshotMap.updateUntilStable(chain);
        }
        catch (UpdateTileContentException e) {
            System.err.println("Update tile content failed");
            System.err.println(e.getMessage());
            return null;
        }
        catch (GameStateUpdateException e) {
            throw new RuntimeException("Couldn't handle action", e);
        }

        return handler;
    }

    /**
     * FIXME JavaDoc
     * @param targetPos
     * @param actionDirection
     */
    public void triggerRemoteAction(InteractionChain chain, Vector2 targetPos, Direction actionDirection) {
        try(RemoteGameController remote = new RemoteGameController()) {
            Queue<Pair<Vector2, Direction>> newRemoteActions = remote.triggerAction(targetPos, actionDirection);
            chain.getPendingRemoteActions().addAll(newRemoteActions);
        }
    }

    /**
     * Applies a queue of GameStateUpdates to the current map.
     *
     * @param queue The queue of GameStateUpdates to apply.
     */
    private void applyGSUQueue(Map map, Queue<GameStateUpdate> queue) {
        for (GameStateUpdate gsu : queue) {
            gsu.apply(map);
        }
    }

    public Map getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(Map map) {
        this.currentMap = map;
    }

    /**
     * Creates a new InteractionChain with a snapshot of the current map.
     *
     * @return A new InteractionChain instance.
     */
    private InteractionChain createInteractionChain() {
        Snapshot snapshot = new Snapshot(currentMap);
        return new InteractionChain(snapshot);
    }

    /**
     * FIXME JavaDoc
     * @param chain
     * @return
     */
    private InteractionChain createInteractionChain(InteractionChain chain) {
        Snapshot snapshot = new Snapshot(chain.getSnapshot().getSnapshotMap());
        return new InteractionChain(snapshot);
    }

    // REMOTE HANDLING

    /**
     * FIXME JavaDoc
     * @param targetPos
     * @param actionDirection
     * @return
     */
    public Queue<Pair<Vector2, Direction>> handleTriggerRemoteInteraction(Vector2 targetPos, Direction actionDirection) {
        System.out.println("handleAppendRemoteInteraction Called");
        // Remote called initial action
        if(interactions.isEmpty()) {
            InteractionChain chain = createInteractionChain();
            boolean result = triggerAction(chain, targetPos, actionDirection);
            if(!result) {
                return new LinkedList<>();
            }

            // FIXME hier wird die chain zu früh auf die current map ausgeführt
            applyGSUQueue(currentMap, chain.getGSUQueue());
            return chain.getPendingActions();
        }
        // Local called initial action -> This is a response
        else {
            InteractionChain currentChain = interactions.peek();
            InteractionChain chain = createInteractionChain(currentChain);

            boolean result = triggerAction(chain, targetPos, actionDirection);
            if(!result) {
                return new LinkedList<>();
            }

            currentChain.getGSUQueue().addAll(chain.getGSUQueue());
            return chain.getPendingActions();
        }
    }

    /**
     * FIXME JavaDoc
     * @param chain
     * @return
     */
    private void triggerQueuedLocalActions(InteractionChain chain) {
        // Trigger locally queued actions
        Pair<Vector2, Direction> action;
        while((action = chain.getPendingActions().poll()) != null) {
            try {
                InteractionChain testChain = createInteractionChain(chain);
                interactions.add(testChain);
                triggerAction(chain, action.getFirst(), action.getSecond());
            }
            finally {
                interactions.pop();
            }
        }
    }

    /**
     * FIXME JavaDoc
     * @param chain
     */
    private void triggerQueuedRemoteActions(InteractionChain chain) {
        // Trigger locally queued remote actions
        Pair<Vector2, Direction> action;
        while((action = chain.getPendingRemoteActions().poll()) != null) {
            triggerRemoteAction(chain, action.getFirst(), action.getSecond());
        }
    }
}