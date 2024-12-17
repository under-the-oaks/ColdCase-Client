package tech.underoaks.coldcase.state;

import tech.underoaks.coldcase.game.Interaction;
import tech.underoaks.coldcase.state.updates.GameStateUpdate;
import tech.underoaks.coldcase.state.updates.GameStateUpdateException;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Represents a sequence of interactions resulting from a player's action.
 */
public class InteractionChain {
    /**
     * Queue of GameStateUpdates associated with this interaction chain.
     */
    private final Queue<GameStateUpdate> gsuQueue;

    /**
     * {@link Queue} that stores actions that need to be executed by the {@link tech.underoaks.coldcase.game.GameController}
     */
    private final Queue<Interaction> pendingActions;

    /**
     * {@link Queue} that stores remote actions that need to be executed by the {@link tech.underoaks.coldcase.game.GameController}
     */
    private final Queue<Interaction> pendingRemoteActions;

    /**
     * Snapshot that will act as the testing environment
     */
    private final Snapshot snapshot;

    public InteractionChain(Snapshot snapshot) {
        this.snapshot = snapshot;
        this.gsuQueue = new LinkedList<>();
        this.pendingActions = new LinkedList<>();
        this.pendingRemoteActions = new LinkedList<>();
    }

    public Snapshot getSnapshot() {
        return snapshot;
    }

    /**
     * Adds an interaction to be executed by the {@link tech.underoaks.coldcase.game.GameController}
     * @param interaction The interaction to add.
     */
    public void addAction(Interaction interaction) {
        pendingActions.add(interaction);
    }


    /**
     * Adds a remote interaction to be executed by the {@link tech.underoaks.coldcase.game.GameController}
     * remotely
     * @param interaction The interaction to add.
     */
    public void addRemoteAction(Interaction interaction) {
        pendingRemoteActions.add(interaction);
    }

    /**
     * Adds a {@link GameStateUpdate} to the interactions chain.
     *
     * @param gsu The GameStateUpdate to add.
     * @throws GameStateUpdateException If the update has failed.
     */
    public void addGameStateUpdate(GameStateUpdate gsu) throws GameStateUpdateException {
        try {
            if (gsu.UPDATE_TYPE.hasConsequences()) {
                gsu.apply(snapshot.getSnapshotMap());
            }
        } catch (Exception e) {
            throw new GameStateUpdateException("Error updating the game state", e);
        }
        gsuQueue.add(gsu);
    }

    public Queue<GameStateUpdate> getGSUQueue() {
        return gsuQueue;
    }

    public Queue<Interaction> getPendingActions() {
        return pendingActions;
    }

    public Queue<Interaction> getPendingRemoteActions() {
        return pendingRemoteActions;
    }
}
