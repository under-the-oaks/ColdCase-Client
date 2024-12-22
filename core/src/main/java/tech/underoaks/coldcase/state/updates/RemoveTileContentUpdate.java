package tech.underoaks.coldcase.state.updates;

import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.state.Map;
import tech.underoaks.coldcase.state.tileContent.TileContent;
import tech.underoaks.coldcase.state.tiles.Tile;

import java.util.Stack;

/**
 * The RemoveTileContentUpdate class represents a game state update that removes
 * specific tile content from a map. This class extends GameStateUpdate.
 */

public class RemoveTileContentUpdate extends GameStateUpdate {

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


        while (poppedContent != null ) {
            if(poppedContent != sourceContent){
                tileContents.push(poppedContent);
            }
            poppedContent = sourceTile.popTileContent();
        }

        while (!tileContents.isEmpty()) {
            sourceTile.pushTileContent(tileContents.pop());
        }
    }
}
