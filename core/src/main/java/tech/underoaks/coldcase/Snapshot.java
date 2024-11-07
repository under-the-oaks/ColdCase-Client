package tech.underoaks.coldcase;

import tech.underoaks.coldcase.data.Map;

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

    private static Map deepCopyMap(Map originalMap) {
        // FIXME
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
