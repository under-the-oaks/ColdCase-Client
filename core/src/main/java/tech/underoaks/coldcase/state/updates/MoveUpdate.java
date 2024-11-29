package tech.underoaks.coldcase.state.updates;

import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.state.Map;
import tech.underoaks.coldcase.state.tileContent.TileContent;
import tech.underoaks.coldcase.state.tiles.Tile;

import java.util.Stack;

public class MoveUpdate extends GameStateUpdate {

    private final int sourceIndex;
    private final Vector2 sourcePosition;
    private final Vector2 targetPosition;

    public MoveUpdate(Vector2 sourcePosition, int sourceIndex, Vector2 targetPosition) {
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
