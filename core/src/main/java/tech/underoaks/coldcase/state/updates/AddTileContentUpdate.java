package tech.underoaks.coldcase.state.updates;

import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.state.Map;
import tech.underoaks.coldcase.state.tileContent.TileContent;
import tech.underoaks.coldcase.state.tiles.Tile;

/**
 * The AddTileContentUpdate class represents a game state update that adds
 * specific tile content to a map.
 */

public class AddTileContentUpdate extends GameStateUpdate {
    private final Vector2 targetPosition;
    private final TileContent targetTileContent;

    /**
     * Default-Constructor
     * @param targetPosition The {@link Vector2} that is pointing to the desired Location.
     * @param targetTileContent The {@link TileContent} that needs to be added.
     */
    public AddTileContentUpdate(Vector2 targetPosition, TileContent targetTileContent) {
        super(UpdateTypes.MAP_MODIFICATION);
        this.targetPosition = targetPosition;
        this.targetTileContent = targetTileContent;
    }

    @Override
    public void apply(Map map) {
        Tile targetTile = map.getTile(targetPosition);
        targetTile.pushTileContent(targetTileContent);

        System.out.println(targetTile.topTileContent());
    }
}
