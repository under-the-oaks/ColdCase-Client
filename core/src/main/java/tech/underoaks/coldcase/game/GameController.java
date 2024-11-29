package tech.underoaks.coldcase.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
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
            interactions.push(chain);
            boolean result = GameController.triggerAction(chain, targetPos, actionDirection);
            if(!result) {
                return false;
            }

            GameController.triggerQueuedLocalActions(interactions, chain);
            GameController.triggerQueuedRemoteActions(chain);
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
    public static boolean triggerAction(InteractionChain testChain, Vector2 targetPos, Direction actionDirection) {
        RemoteGameController remote = null;
        try {
            TileContent handler = GameController.triggerLocalAction(testChain, targetPos, actionDirection);
            if(handler == null) {
                return false;
            }

            // If local action was transcendent, trigger remote action
            if(handler.getVisibilityState().equals(VisibilityStates.TRANSCENDENT)) {
                remote = new RemoteGameController();
                Queue<Pair<Vector2, Direction>> newRemoteActions = remote.triggerAction(targetPos, actionDirection);
                testChain.getPendingRemoteActions().addAll(newRemoteActions);   //TODO handling getting null back or is that an error
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
    public static TileContent triggerLocalAction(InteractionChain chain, Vector2 targetPos, Direction actionDirection) {
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
    public static void triggerRemoteAction(InteractionChain chain, Vector2 targetPos, Direction actionDirection) {
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

    public void endLevel(){
        System.out.println("Ending the level...");
        Gdx.app.exit();
        System.exit(0);

    }

    private void loadNextLevel() {
        System.out.println("Loading next level...");
        //TODO: Load new Level

    }


    /**
     * FIXME JavaDoc
     * @param chain
     * @return
     */
    private static InteractionChain createInteractionChain(InteractionChain chain) {
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
//        if(interactions.isEmpty()) {
//            InteractionChain chain = createInteractionChain();
//            try {
//                interactions.push(chain);
//                boolean result = GameController.triggerAction(chain, targetPos, actionDirection);
//                if(!result) {
//                    return new LinkedList<>();
//                }
//
//                // FIXME hier wird die chain zu früh auf die current map ausgeführt
//                // applyGSUQueue(currentMap, chain.getGSUQueue());
//                return chain.getPendingActions();
//            }
//            finally {
//                interactions.pop();
//            }
//        }
        // Local called initial action -> This is a response
        //else {
            InteractionChain currentChain = interactions.peek();
            InteractionChain chain = createInteractionChain(currentChain);
            try {
                interactions.push(chain);
                boolean result = triggerAction(chain, targetPos, actionDirection);
                if(!result) {
                    return new LinkedList<>();
                }

            }
            finally {
                interactions.pop();
            }

            currentChain.getGSUQueue().addAll(chain.getGSUQueue());
            return chain.getPendingActions();
        //}
    }

    public void handleCreateRemoteInteractionChain()
    {
        if(!interactions.isEmpty()) {
            throw new RuntimeException("Another Interaction is currently running");
        }
        interactions.push(createInteractionChain());
    }

    public void handleApplyRemoteGSUsMessage()
    {
        // Vorbedingung: Es befindet sich genau eine Chain in dem Interaction-Stack
        if(interactions.size() != 1) {
            throw new RuntimeException("Only one Chain is allowed in the Stack");
        }
        applyGSUQueue(currentMap, interactions.pop().getGSUQueue());
    }

    public void handleAbortRemoteGSUsMessage()
    {
        interactions.clear();
    }

    /**
     * FIXME JavaDoc
     * @param chain
     * @return
     */
    private static void triggerQueuedLocalActions(Stack<InteractionChain> interactions, InteractionChain chain) {
        // Trigger locally queued actions
        Pair<Vector2, Direction> action;
        while((action = chain.getPendingActions().poll()) != null) {
            try {
                InteractionChain testChain = GameController.createInteractionChain(chain);
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
    private static void triggerQueuedRemoteActions(InteractionChain chain) {
        // Trigger locally queued remote actions
        Pair<Vector2, Direction> action;
        while((action = chain.getPendingRemoteActions().poll()) != null) {
            GameController.triggerRemoteAction(chain, action.getFirst(), action.getSecond());
        }
    }
}
