package tech.underoaks.coldcase.state.tileContent;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.game.Interaction;
import tech.underoaks.coldcase.state.updates.GameStateUpdateException;
import tech.underoaks.coldcase.state.InteractionChain;
import tech.underoaks.coldcase.state.updates.MoveUpdate;
import tech.underoaks.coldcase.state.Map;
import tech.underoaks.coldcase.state.tiles.Tile;
import tech.underoaks.coldcase.game.Direction;

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
    public boolean action(InteractionChain chain, Interaction interaction) throws GameStateUpdateException {

        int childIndex = chain.getSnapshot().getSnapshotMap().getChildIndex(interaction.getTargetPos(), this);
        Vector2 targetPosition = switch (interaction.getActionDirection()) {
            case NORTH -> interaction.getTargetPos().cpy().add(Direction.NORTH.getVector());
            case SOUTH -> interaction.getTargetPos().cpy().add(Direction.SOUTH.getVector());
            case EAST -> interaction.getTargetPos().cpy().add(Direction.EAST.getVector());
            case WEST -> interaction.getTargetPos().cpy().add(Direction.WEST.getVector());
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
        chain.addGameStateUpdate(new MoveUpdate(interaction.getTargetPos(), childIndex, targetPosition));
        return true;
    }

    @Override
    public boolean update(InteractionChain chain, Vector2 tilePosition) throws GameStateUpdateException {
        return false;
    }
}
