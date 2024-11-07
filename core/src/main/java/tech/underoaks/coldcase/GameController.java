package tech.underoaks.coldcase;

import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.data.Map;
import tech.underoaks.coldcase.data.tileContent.TileContent;
import tech.underoaks.coldcase.data.tiles.Tile;

/**
 * Central manager responsible for handling interactions within the game.
 */
public class GameController {
    /** Singleton instance of GameController */
    private static GameController instance;

    /** Active game map */
    private Map currentMap;

    public static GameController getInstance() {
        if (instance == null) {
            instance = new GameController();
        }
        return instance;
    }

    public void triggerAction(Vector2 position, Vector2 targetPos) {
        InteractionChain chain = createInteractionChain();

        // Requesting an action handler to respond to the triggered action
        Tile targetTile = chain.getSnapshot().getSnapshotMap().getTile(
            (int) targetPos.x,
            (int) targetPos.y
        );
        if(targetTile == null) {
            return;
        }

        TileContent targetTileContent = targetTile.getTileContent();
        if(targetTileContent == null) {
            return;
        }

        targetTileContent.handleAction(chain, position);

        // TODO if false -> Keine Interaktion -> Player Movement?
    }

    public Map getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(Map map) {
        this.currentMap = map;
    }

    public InteractionChain createInteractionChain() {
        Snapshot snapshot = new Snapshot(currentMap);
        return new InteractionChain(snapshot);
    }
}
