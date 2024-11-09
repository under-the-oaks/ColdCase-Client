package tech.underoaks.coldcase;

import tech.underoaks.coldcase.data.Map;

/**
 * Represents a snapshot of the game state at a specific point in time.
 */
public class Snapshot {
    /** The copied state of the Map */
    private final Map snapshotMap;

    /**
     * Creates a snapshot of the provided Map.
     *
     * @param originalMap The map to snapshot.
     */
    public Snapshot(Map originalMap) {
        this.snapshotMap = deepCopyMap(originalMap);
    }

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
