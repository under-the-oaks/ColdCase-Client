package tech.underoaks.coldcase;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Represents a sequence of interactions resulting from a player's action.
 */
public class InteractionChain {
    /** Queue of GameStateUpdates associated with this interaction chain. */
    private final Queue<GameStateUpdate> gsuQueue;

    /** Snapshot that will act as the testing environment */
    private final Snapshot snapshot;

    public InteractionChain(Snapshot snapshot) {
        this.snapshot = snapshot;
        this.gsuQueue = new LinkedList<>();
    }

    public Snapshot getSnapshot() {
        return snapshot;
    }

    /**
     * Adds a {@link GameStateUpdate} to the interactions chain.
     *
     * @param gsu The GameStateUpdate to add.
     * @throws GameStateUpdateException If the update has failed.
     */
    public void addGameStateUpdate(GameStateUpdate gsu) throws GameStateUpdateException {
        // TODO Igler fragen ob das Pattern OK ist
        try {
            if(gsu.UPDATE_TYPE.hasConsequences()) {
                gsu.apply(snapshot.getSnapshotMap());
            }
        }
        catch (Exception e) {
            throw new GameStateUpdateException("Error updating the game state", e);
        }
        gsuQueue.add(gsu);
    }

    /**
     * Validates the interaction chain within the snapshot.
     *
     * @return True if valid; False otherwise
     */
    public Queue<GameStateUpdate> getGSUQueue() {
        return gsuQueue;
    }
}
