package tech.underoaks.coldcase.state.tileContent;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.game.Direction;
import tech.underoaks.coldcase.game.GameController;
import tech.underoaks.coldcase.state.InteractionChain;
import tech.underoaks.coldcase.state.Map;
import tech.underoaks.coldcase.state.tiles.Tile;
import tech.underoaks.coldcase.state.updates.GameStateUpdateException;
import tech.underoaks.coldcase.state.updates.MoveUpdate;

public class TranscendentTestBlock extends TileContent{
    private static final Texture texture = new Texture("./sprites/block_transcendent.png");

    public TranscendentTestBlock() {

        super(texture, false, false);
        this.visibilityState = VisibilityStates.TRANSCENDENT;
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
        chain.addAction(new Vector2(3,3), Direction.EAST);
        return true;
    }

    @Override
    public boolean update(InteractionChain chain, Vector2 tilePosition) throws GameStateUpdateException {
        return false;
    }
}
