package tech.underoaks.coldcase.data.tileContent;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.GameStateUpdateException;
import tech.underoaks.coldcase.InteractionChain;
import tech.underoaks.coldcase.MoveUpdate;
import tech.underoaks.coldcase.data.Map;
import tech.underoaks.coldcase.data.tiles.Tile;
import tech.underoaks.coldcase.loader.enums.Direction;

public class movableBlock extends TileContent {

    private static final Texture texture = new Texture("./isometric tileset/separated images/tile_067.png");

    public movableBlock() {
        super(texture, false, false);
    }

    @Override
    public boolean action(InteractionChain chain, Vector2 tilePosition, Direction actionDirection) throws GameStateUpdateException {

        int childIndex = chain.getSnapshot().getSnapshotMap().getChildIndex(tilePosition, this);

        Vector2 targetPosition = tilePosition.cpy().add(actionDirection.getVector());

        //validation checks: if the target tile is out of bounds
        Map snapshotMap = chain.getSnapshot().getSnapshotMap();
        if (snapshotMap.isOutOfBounds(targetPosition)) {
            return false;
        }

        //validation checks: if the target tile is not passable
        Tile targetTile = snapshotMap.getTile(targetPosition);
        TileContent topTargetContent = targetTile.topTileContent();
        if (topTargetContent != null && !topTargetContent.isObjectPassable()) {
            return false;
        }

        chain.addGameStateUpdate(new MoveUpdate(tilePosition, childIndex, targetPosition));
        return true;
    }

    @Override
    public boolean update(InteractionChain chain, Vector2 tilePosition) {
        return false;
    }

}
