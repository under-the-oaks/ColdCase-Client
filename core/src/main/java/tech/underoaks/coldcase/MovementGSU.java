package tech.underoaks.coldcase;

import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.data.Map;
import tech.underoaks.coldcase.data.tileContent.TileContent;
import tech.underoaks.coldcase.data.tiles.Tile;
import tech.underoaks.coldcase.enums.UpdateTypes;

import java.util.Stack;

public class MovementGSU extends GameStateUpdate {

    private final int sourceIndex;
    private final Vector2 sourcePosition;
    private final Vector2 targetPosition;


    public MovementGSU(Vector2 sourcePosition, int sourceIndex, Vector2 targetPosition) {
        super(UpdateTypes.MAP_MODIFICATION);
        this.sourcePosition = sourcePosition;
        this.sourceIndex = sourceIndex;
        this.targetPosition = targetPosition;
    }

    @Override
    public void apply(Map map) {
        TileContent sourceContent = map.getTileContentByIndex(sourcePosition, sourceIndex);

        Stack<TileContent> tileContents = new Stack<>();

        Tile sourceTile = map.getTile(sourcePosition);
        Tile targetTile = map.getTile(targetPosition);

        TileContent poppedContent = sourceTile.popTileContent();

        while (poppedContent != null && poppedContent != sourceContent) {
            tileContents.push(poppedContent);
            poppedContent = sourceTile.popTileContent();
        }

        targetTile.pushTileContent(sourceContent);

        while (!tileContents.isEmpty()) {
            sourceTile.pushTileContent(tileContents.pop());
        }
    }
}
