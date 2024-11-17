package tech.underoaks.coldcase.data.tileContent;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.GameStateUpdateException;
import tech.underoaks.coldcase.InteractionChain;
import tech.underoaks.coldcase.MoveUpdate;
import tech.underoaks.coldcase.data.Map;
import tech.underoaks.coldcase.data.tiles.Tile;
import tech.underoaks.coldcase.loader.enums.Direction;

public class Player extends TileContent {

    private static final Texture texture = new Texture("./isometric tileset/placholder_player.png");

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public int playerIndex = 2;

    public Player() {
        super(texture, true, false);
    }

    @Override
    public boolean action(InteractionChain chain, Vector2 tilePosition, Direction actionDirection) throws GameStateUpdateException {

        int childIndex = chain.getSnapshot().getSnapshotMap().getChildIndex(tilePosition, this);
        Vector2 targetPosition = switch (actionDirection) {
            case NORTH -> tilePosition.cpy().add(Direction.NORTH.getVector());
            case SOUTH -> tilePosition.cpy().add(Direction.SOUTH.getVector());
            case EAST -> tilePosition.cpy().add(Direction.EAST.getVector());
            case WEST -> tilePosition.cpy().add(Direction.WEST.getVector());
        };

        Map snapshotMap = chain.getSnapshot().getSnapshotMap();

        //validation checks: if the target tile is out of bounds
        if (snapshotMap.isOutOfBounds(targetPosition)) {
            return false;
        }

        //validation checks: if the target tile is not passable
        Tile targetTile = snapshotMap.getTile(targetPosition);
        TileContent topTargetContent = targetTile.topTileContent();
        if (topTargetContent != null && !topTargetContent.isPlayerPassable()) {
            return false;
        }

        // adding the validated movement to the chain
        chain.addGameStateUpdate(new MoveUpdate(tilePosition, childIndex, targetPosition));
        return true;
    }

    @Override
    public boolean update(InteractionChain chain, Vector2 tilePosition) throws GameStateUpdateException {
        return false;
    }
}
