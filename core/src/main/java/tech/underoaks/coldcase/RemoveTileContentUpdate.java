package tech.underoaks.coldcase;

import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.data.Map;
import tech.underoaks.coldcase.data.tileContent.TileContent;
import tech.underoaks.coldcase.data.tiles.Tile;
import tech.underoaks.coldcase.enums.UpdateTypes;

import java.util.Stack;

public class RemoveTileContentUpdate extends GameStateUpdate{

    private final int sourceIndex;
    private final Vector2 sourcePosition;

    public RemoveTileContentUpdate(Vector2 sourcePosition, int sourceIndex) {
        super(UpdateTypes.MAP_MODIFICATION);
        this.sourceIndex = sourceIndex;
        this.sourcePosition = sourcePosition;
    }

    @Override
    public void apply(Map map) {
        TileContent sourceContent = map.getTileContentByIndex(sourcePosition, sourceIndex);

        Stack<TileContent> tileContents = new Stack<>();

        Tile sourceTile = map.getTile(sourcePosition);

        TileContent poppedContent = sourceTile.popTileContent();

        while (poppedContent != null && poppedContent != sourceContent) {
            tileContents.push(poppedContent);
            poppedContent = sourceTile.popTileContent();
        }

        while (!tileContents.isEmpty()) {
            sourceTile.pushTileContent(tileContents.pop());
        }
    }
}
