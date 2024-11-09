package tech.underoaks.coldcase;

import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.data.Map;
import tech.underoaks.coldcase.data.tileContent.TileContent;
import tech.underoaks.coldcase.data.tiles.Tile;

import java.util.Queue;

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
        Map snapshotMap = chain.getSnapshot().getSnapshotMap();

        // Requesting an action handler to respond to the triggered action
        Tile targetTile = snapshotMap.getTile(targetPos);
        if(targetTile == null) {
            return;
        }

        TileContent targetTileContent = targetTile.getTileContent();
        if(targetTileContent == null) {
            return;
        }

        // Actions will be handled inside a secured body to ensure only valid actions will be
        // applied to the running instance
        try {
            // Trigger initial action
            boolean action = targetTileContent.handleAction(chain, position);

            if(!action) {
                System.out.println("Couldn't handle action");
                // TODO @MAX Hier Movement?
                // Wenn ja dann nach dem Movement nochmal updateUntilStable und action auf true setzen
            }

            if(!action) {
                return;
            }

            // Update due to potential changes
            snapshotMap.updateUntilStable(chain);
        } catch (GameStateUpdateException e) {
            System.err.println("Couldn't handle action");
            System.err.println(e.getMessage());
            return;
        }

        applyGSUQueue(chain.getGSUQueue());
    }

    private void applyGSUQueue(Queue<GameStateUpdate> queue) {
        for(GameStateUpdate gsu : queue) {
            gsu.apply(currentMap);
        }
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
