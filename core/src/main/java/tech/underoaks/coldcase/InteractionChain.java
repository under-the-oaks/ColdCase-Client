package tech.underoaks.coldcase;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Represents a sequence of interactions resulting from a player's action.
 */
public class InteractionChain {
    /** Queue of GameStateUpdates associated with this interaction chain. */
    private Queue<GameStateUpdate> gsuQueue;

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
     */
    public void addGameStateUpdate(GameStateUpdate gsu) {
        // FIXME
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Validates the interaction chain within the snapshot.
     *
     * @return True if valid; False otherwise
     */
    public Queue<GameStateUpdate> getGSUQueue() {
        // FIXME
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
