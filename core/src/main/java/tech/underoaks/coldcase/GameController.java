package tech.underoaks.coldcase;

import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.data.Map;
import tech.underoaks.coldcase.data.tileContent.TileContent;

/**
 * Central manager responsible for handling interactions within the game.
 */
public class GameController {
    /** Active game map */
    private Map currentMap;

    /** Snapshot of the Map used for simulating interactions. */
    private Snapshot currentSnapshot;

    public GameController(Map currentMap) {
        this.currentMap = currentMap;
    }

    public void triggerAction(Vector2 position, Vector2 targetPos) {
        if(currentSnapshot != null) {
            throw new IllegalStateException("A snapshot is already initialized");
        }
        InteractionChain chain = createInteractionChain();

        // TODO CHAIN OF COMMAND

        discardSnapshot();
    }

    private void createSnapshot() {
        currentSnapshot = new Snapshot(currentMap);
    }

    public void discardSnapshot() {
        currentSnapshot = null;
    }

    public Map getCurrentMap() {
        return currentMap;
    }

    public InteractionChain createInteractionChain() {
        createSnapshot();
        return new InteractionChain(currentSnapshot);
    }
}
