package tech.underoaks.coldcase.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import org.glassfish.grizzly.utils.Pair;
import tech.underoaks.coldcase.remote.RemoteGameController;
import tech.underoaks.coldcase.state.InteractionChain;
import tech.underoaks.coldcase.state.Map;
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
     * Stack of interaction chains representing nested interactions.
     */
    private final Stack<InteractionChain> interactions = new Stack<>();

    /**
     * Queue of pending game state updates to be applied.
     */
    private final Queue<GameStateUpdate> pendingUpdates = new LinkedList<>();

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
     * Triggers an action at the specified position in the given direction.
     *
     * @param targetPos       The target position where the action is applied.
     * @param actionDirection The direction in which the action is triggered.
     * @return True if the action was successfully triggered, false otherwise.
     */
    public boolean triggerAction(Vector2 targetPos, Direction actionDirection) {
        InteractionChain chain = createInteractionChain();

        // Trigger local action
        try {
            interactions.push(chain);
            boolean result = GameController.triggerAction(chain, targetPos, actionDirection);
            if (!result) {
                return false;
            }

            GameController.triggerQueuedLocalActions(interactions, chain);
            GameController.triggerQueuedRemoteActions(chain);
        } finally {
            interactions.pop();
        }

        //applyGSUQueue(currentMap, chain.getGSUQueue());
        pendingUpdates.addAll(chain.getGSUQueue());
        return true;
    }

    /**
     * Triggers an action using the provided interaction chain, target position, and direction.
     *
     * @param testChain       The interaction chain to use.
     * @param targetPos       The target position where the action is applied.
     * @param actionDirection The direction in which the action is triggered.
     * @return True if the action was successfully triggered, false otherwise.
     */
    public static boolean triggerAction(InteractionChain testChain, Vector2 targetPos, Direction actionDirection) {
        return triggerAction(testChain, targetPos, actionDirection, false);
    }

    /**
     * Triggers an action using the provided interaction chain, target position, and direction,
     * with an option to suppress transcendent follow-up actions.
     *
     * @param testChain                    The interaction chain to use.
     * @param targetPos                    The target position where the action is applied.
     * @param actionDirection              The direction in which the action is triggered.
     * @param suppressTranscendentFollowUp If true, suppresses triggering of transcendent follow-up actions.
     * @return True if the action was successfully triggered, false otherwise.
     */
    public static boolean triggerAction(InteractionChain testChain, Vector2 targetPos, Direction actionDirection, boolean suppressTranscendentFollowUp) {
        RemoteGameController remote = null;
        try {
            TileContent handler = GameController.triggerLocalAction(testChain, targetPos, actionDirection);
            if (handler == null) {
                return false;
            }

            // If local action was transcendent, trigger remote action
            if (!suppressTranscendentFollowUp && handler.getVisibilityState().equals(VisibilityStates.TRANSCENDENT)) {
                remote = new RemoteGameController();
                Queue<Pair<Vector2, Direction>> newRemoteActions = remote.triggerAction(targetPos, actionDirection, true); // Suppress Transcended Trigger
                if (newRemoteActions != null) {
                    testChain.getPendingRemoteActions().addAll(newRemoteActions);
                } else {
                    return false;
                }
            }

            return true;
        } finally {
            if (remote != null) {
                remote.close();
            }
        }
    }

    /**
     * Initiates a local action at a specified position and direction.
     *
     * @param chain           The interaction chain to use.
     * @param targetPos       The target position where the action is applied.
     * @param actionDirection The direction in which the action is triggered.
     * @return The TileContent handler if the action was handled, null otherwise.
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
        } catch (UpdateTileContentException e) {
            System.err.println("Update tile content failed");
            System.err.println(e.getMessage());
            return null;
        } catch (GameStateUpdateException e) {
            throw new RuntimeException("Couldn't handle action", e);
        }

        return handler;
    }


    /**
     * Triggers a remote action at the specified position and direction.
     *
     * @param chain           The interaction chain to use.
     * @param targetPos       The target position where the action is applied.
     * @param actionDirection The direction in which the action is triggered.
     */
    public static void triggerRemoteAction(InteractionChain chain, Vector2 targetPos, Direction actionDirection) {
        try (RemoteGameController remote = new RemoteGameController()) {
            Queue<Pair<Vector2, Direction>> newRemoteActions = remote.triggerAction(targetPos, actionDirection);
            if (newRemoteActions == null) {
                return;
            }
            chain.getPendingRemoteActions().addAll(newRemoteActions);
        }
    }

    /**
     * Applies a queue of GameStateUpdates to the specified map.
     *
     * @param map   The map to which the updates will be applied.
     * @param queue The queue of GameStateUpdates to apply.
     */
    private void applyGSUQueue(Map map, Queue<GameStateUpdate> queue) {
        if (map != currentMap) {
            GameStateUpdate gsu = queue.remove();
            gsu.apply(map);
            return;
        }
        pendingUpdates.addAll(queue);
    }

    /**
     * Applies the next pending GameStateUpdate to the current map.
     */
    public void applyNextPendingGSU() {
        if (pendingUpdates.isEmpty()) {
            return;
        }
        GameStateUpdate gsu = pendingUpdates.remove();
        gsu.apply(currentMap);
    }

    /**
     * Checks if there are any pending GameStateUpdates to be applied.
     *
     * @return True if there are no pending updates, false otherwise.
     */
    public boolean isPendingGSUQueueEmpty() {
        return pendingUpdates.isEmpty();
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
     * Ends the current level and exits the game.
     */
    public void endLevel() {
        System.out.println("Ending the level...");
        Gdx.app.exit();
        System.exit(0);

    }

    /**
     * Loads the next level (to be implemented).
     */
    private void loadNextLevel() {
        System.out.println("Loading next level...");
        //TODO: Load new Level

    }


    /**
     * Creates a new InteractionChain based on an existing chain.
     *
     * @param chain The existing InteractionChain.
     * @return A new InteractionChain instance.
     */
    private static InteractionChain createInteractionChain(InteractionChain chain) {
        Snapshot snapshot = new Snapshot(chain.getSnapshot().getSnapshotMap());
        return new InteractionChain(snapshot);
    }

    // REMOTE HANDLING

    /**
     * Handles triggering of a remote interaction.
     *
     * @param targetPos                    The target position where the action is applied.
     * @param actionDirection              The direction in which the action is triggered.
     * @param suppressTranscendentFollowUp If true, suppresses triggering of transcendent follow-up actions.
     * @return A queue of pending actions, or null if the action was unsuccessful.
     */
    public Queue<Pair<Vector2, Direction>> handleTriggerRemoteInteraction(Vector2 targetPos, Direction actionDirection, boolean suppressTranscendentFollowUp) {
        System.out.println("handleAppendRemoteInteraction Called");

        InteractionChain currentChain = interactions.peek();
        InteractionChain chain = createInteractionChain(currentChain);
        try {
            interactions.push(chain);
            boolean result = triggerAction(chain, targetPos, actionDirection, suppressTranscendentFollowUp);
            if (!result) {
                return null;
            }

        } finally {
            interactions.pop();
        }

        currentChain.getGSUQueue().addAll(chain.getGSUQueue());
        return chain.getPendingActions();
    }

    /**
     * Handles the creation of a remote interaction chain.
     *
     * @throws RuntimeException if another interaction is currently running.
     */
    public void handleCreateRemoteInteractionChain() {
        if (!interactions.isEmpty()) {
            throw new RuntimeException("Another Interaction is currently running");
        }
        interactions.push(createInteractionChain());
    }

    /**
     * Applies the remote GameStateUpdates.
     *
     * @throws RuntimeException if there is not exactly one chain in the interaction stack.
     */
    public void handleApplyRemoteGSUsMessage() {
        // Vorbedingung: Es befindet sich genau eine Chain in dem Interaction-Stack
        if (interactions.size() != 1) {
            throw new RuntimeException("Only one Chain is allowed in the Stack");
        }
        //dapplyGSUQueue(currentMap, interactions.pop().getGSUQueue());
        pendingUpdates.addAll(interactions.pop().getGSUQueue());
    }

    /**
     * Aborts the remote GameStateUpdates.
     */
    public void handleAbortRemoteGSUsMessage() {
        interactions.clear();
    }

    /**
     * Triggers queued local actions from the interaction chain.
     *
     * @param interactions The stack of interaction chains.
     * @param chain        The current interaction chain.
     */
    private static void triggerQueuedLocalActions(Stack<InteractionChain> interactions, InteractionChain chain) {
        // Trigger locally queued actions
        Pair<Vector2, Direction> action;
        while ((action = chain.getPendingActions().poll()) != null) {
            try {
                InteractionChain testChain = GameController.createInteractionChain(chain);
                interactions.add(testChain);
                triggerAction(chain, action.getFirst(), action.getSecond());
            } finally {
                interactions.pop();
            }
        }
    }

    /**
     * Triggers queued remote actions from the interaction chain.
     *
     * @param chain The current interaction chain.
     */
    private static void triggerQueuedRemoteActions(InteractionChain chain) {
        // Trigger locally queued remote actions
        Pair<Vector2, Direction> action;
        while ((action = chain.getPendingRemoteActions().poll()) != null) {
            GameController.triggerRemoteAction(chain, action.getFirst(), action.getSecond());
        }
    }
}
