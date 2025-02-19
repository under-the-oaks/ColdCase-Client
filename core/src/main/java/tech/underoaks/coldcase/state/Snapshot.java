package tech.underoaks.coldcase.state;

/**
 * Represents a snapshot of the game state at a specific point in time.
 */
public class Snapshot {
    /**
     * The copied state of the Map
     */
    private final Map snapshotMap;

    /**
     * Creates a snapshot of the provided Map.
     *
     * @param originalMap The map to snapshot.
     */
    public Snapshot(Map originalMap) {
        this.snapshotMap = deepCopyMap(originalMap);
        this.snapshotMap.setIsSnapshotMap(true);
    }

    /**
     * Gets the {@link Map} that is being stored in this Snapshot
     * @return The {@link Map}
     */
    public Map getSnapshotMap() {
        return snapshotMap;
    }

    /**
     * Creates a deep copy of the given Map.
     *
     * @param originalMap The original Map to copy.
     * @return A deep copy of the original Map.
     */
    private static Map deepCopyMap(Map originalMap) {
        return originalMap.deepClone();
    }
}
