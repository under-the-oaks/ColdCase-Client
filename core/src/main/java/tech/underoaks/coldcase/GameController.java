package tech.underoaks.coldcase;

import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.data.Map;
import tech.underoaks.coldcase.data.tileContent.TileContent;
import tech.underoaks.coldcase.data.tiles.Tile;
import tech.underoaks.coldcase.loader.enums.Direction;

import java.util.Queue;

/**
 * Central manager responsible for handling interactions within the game.
 */
public class GameController {
    /**
     * Singleton instance of GameController
     */
    private static GameController instance;

    /**
     * Active game map
     */
    private Map currentMap;

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
     * Initiates an action at a specified position targeting another position.
     *
     * @param actionDirection The direction in which the action get triggered.
     * @param targetPos       The target position where the action is applied.
     * @return True if the action was successfully triggered, false otherwise.
     */
    public boolean triggerAction(Vector2 targetPos, Direction actionDirection) {
        InteractionChain chain = createInteractionChain();
        Map snapshotMap = chain.getSnapshot().getSnapshotMap();

        // Requesting an action handler to respond to the triggered action
        Tile targetTile = snapshotMap.getTile(targetPos);
        if (targetTile == null) {
            return false;
        }

        TileContent targetTileContent = targetTile.getTileContent();
        if (targetTileContent == null) {
            return false;
        }

        // Actions will be handled inside a secured body to ensure only valid actions will be
        // applied to the running instance
        try {
            // Trigger initial action
            boolean action = targetTileContent.handleAction(chain, targetPos, actionDirection);
            if (!action) {
                return false;
            }

            // Update due to potential changes
            snapshotMap.updateUntilStable(chain);
        } catch (GameStateUpdateException e) {
            System.err.println("Couldn't handle action");
            System.err.println(e.getMessage());
            return false;
        }

        applyGSUQueue(chain.getGSUQueue());

        return true;
    }

    /**
     * Applies a queue of GameStateUpdates to the current map.
     *
     * @param queue The queue of GameStateUpdates to apply.
     */
    private void applyGSUQueue(Queue<GameStateUpdate> queue) {
        for (GameStateUpdate gsu : queue) {
            gsu.apply(currentMap);
        }
    }

    public Map getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(Map map) {
        this.currentMap = map;
    }

    /**
     * Creates a new InteractionChain with a snapshot of the current map.
     *
     * @return A new InteractionChain instance.
     */
    public InteractionChain createInteractionChain() {
        Snapshot snapshot = new Snapshot(currentMap);
        return new InteractionChain(snapshot);
    }
}
