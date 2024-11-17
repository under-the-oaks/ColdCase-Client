package tech.underoaks.coldcase;

import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.data.Map;
import tech.underoaks.coldcase.data.tileContent.TileContent;
import tech.underoaks.coldcase.data.tiles.Tile;
import tech.underoaks.coldcase.enums.UpdateTypes;

/**
 * The AddTileContentUpdate class represents a game state update that adds
 * specific tile content to a map.
 */

public class AddTileContentUpdate extends GameStateUpdate{
    private final Vector2 targetPosition;
    private final TileContent targetTileContent;

    public AddTileContentUpdate(Vector2 targetPosition, TileContent targetTileContent) {
        super(UpdateTypes.MAP_MODIFICATION);
        this.targetPosition = targetPosition;
        this.targetTileContent = targetTileContent;
    }

    @Override
    public void apply(Map map) {
        Tile targetTile = map.getTile(targetPosition);
        targetTile.pushTileContent(targetTileContent);
    }
}
