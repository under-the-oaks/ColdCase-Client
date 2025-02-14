package tech.underoaks.coldcase.game;

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
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * Central manager responsible for handling interactions within the game.
 */
public class GameController {
    /**
     * UUID of this GameController instance
     */
    public final UUID uuid;

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

    private GameController() {
        this.uuid = UUID.randomUUID();
    }

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
     * Removes the currently running instance
     */
    public static void destroy() {
        instance = null;
    }

    /**
     * Triggers an action at the specified position in the given direction.
     *
     * @param interaction The interaction to trigger.
     * @return True if the action was successfully triggered, false otherwise.
     */
    public boolean triggerAction(Interaction interaction) {
        InteractionChain chain = createInteractionChain();

        // Trigger local action
        try {
            interactions.push(chain);
            boolean result = GameController.triggerAction(chain, interaction);
            if (!result) {
                return false;
            }

            GameController.triggerQueuedLocalActions(interactions, chain);
            GameController.triggerQueuedRemoteActions(chain);
        } catch (TimeoutException e) {
            System.err.println("TIMEOUT in triggerAction");
            return false;
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
     * @param interaction     The interaction to trigger.
     * @return True if the action was successfully triggered, false otherwise.
     */
    public static boolean triggerAction(InteractionChain testChain, Interaction interaction) {
        return triggerAction(testChain, interaction, false);
    }

    /**
     * Triggers an action using the provided interaction chain, target position, and direction,
     * with an option to suppress transcendent follow-up actions.
     *
     * @param testChain                    The interaction chain to use.
     * @param interaction     The interaction to trigger.
     * @param suppressTranscendentFollowUp If true, suppresses triggering of transcendent follow-up actions.
     * @return True if the action was successfully triggered, false otherwise.
     */
    public static boolean triggerAction(InteractionChain testChain, Interaction interaction, boolean suppressTranscendentFollowUp) {
        RemoteGameController remote = null;
        try {
            TileContent handler = GameController.triggerLocalAction(testChain, interaction);
            if (handler == null) {
                return false;
            }

            // If local action was transcendent, trigger the same action on the remote client
            if (!suppressTranscendentFollowUp && handler.getVisibilityState().equals(VisibilityStates.TRANSCENDENT)) {
                remote = new RemoteGameController();
                Queue<Interaction> newRemoteActions = remote.triggerAction(interaction, true); // Suppress Transcended Trigger
                if (newRemoteActions != null) {
                    testChain.getPendingRemoteActions().addAll(newRemoteActions);
                } else {
                    return false;
                }
            }

            return true;
        } catch (TimeoutException e) {
            System.err.println("TIMEOUT");
            return false;
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
     * @param interaction     The interaction to trigger.
     * @return The TileContent handler if the action was handled, null otherwise.
     */
    public static TileContent triggerLocalAction(InteractionChain chain, Interaction interaction) {
        Map snapshotMap = chain.getSnapshot().getSnapshotMap();

        // Requesting an action handler to respond to the triggered action
        if (snapshotMap.isOutOfBounds(interaction.getTargetPos())) {
            return null;
        }
        Tile targetTile = snapshotMap.getTile(interaction.getTargetPos());
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
            handler = targetTileContent.handleAction(chain, interaction);
            if (handler == null) {
                return null;
            }

            // Update due to potential changes
            snapshotMap.updateUntilStable(chain, interaction, handler);
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
     * @param interaction     The interaction to trigger.
     * @throws TimeoutException When the remote client is not responding
     */
    public static void triggerRemoteAction(InteractionChain chain, Interaction interaction) throws TimeoutException {
        try (RemoteGameController remote = new RemoteGameController()) {
            Queue<Interaction> newRemoteActions = remote.triggerAction(interaction);
            if (newRemoteActions == null) {
                return;
            }
            chain.getPendingRemoteActions().addAll(newRemoteActions);
        }
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

    /**
     * Gets the current 'live'-Map
     * @return {@link Map}
     */
    public Map getCurrentMap() {
        return currentMap;
    }

    /**
     * Sets the current 'live'-Map
     * @param map {@link Map}
     */
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
     * @param interaction     The interaction to trigger.
     * @param suppressTranscendentFollowUp If true, suppresses triggering of transcendent follow-up actions.
     * @return A queue of pending actions, or null if the action was unsuccessful.
     */
    public Queue<Interaction> handleTriggerRemoteInteraction(Interaction interaction, boolean suppressTranscendentFollowUp) {

        InteractionChain currentChain = interactions.peek();
        InteractionChain chain = createInteractionChain(currentChain);
        try {
            interactions.push(chain);
            boolean result = triggerAction(chain, interaction, suppressTranscendentFollowUp);
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
        Interaction action;
        while ((action = chain.getPendingActions().poll()) != null) {
            try {
                InteractionChain testChain = GameController.createInteractionChain(chain);
                interactions.add(testChain);
                triggerAction(chain, action);
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
    private static void triggerQueuedRemoteActions(InteractionChain chain) throws TimeoutException {
        // Trigger locally queued remote actions
        Interaction action;
        while ((action = chain.getPendingRemoteActions().poll()) != null) {
            System.out.println("Triggering remote action: " + action.getTargetPos() + " " + action.getActionDirection());
            GameController.triggerRemoteAction(chain, action);
        }
    }
}
