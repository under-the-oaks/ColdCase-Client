package tech.underoaks.coldcase.data.tileContent;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.GameStateUpdateException;
import tech.underoaks.coldcase.InteractionChain;
import tech.underoaks.coldcase.MovementGSU;
import tech.underoaks.coldcase.loader.enums.Direction;

public class Player extends TileContent {

    private static final Texture texture = new Texture("./isometric tileset/placholder_player.png");

    public Player() {
        super(texture, true, false);
    }

    @Override
    public boolean action(InteractionChain chain, Vector2 tilePosition, Direction actionDirection) throws GameStateUpdateException {

        int childIndex = chain.getSnapshot().getSnapshotMap().getChildIndex(tilePosition, this);
        Vector2 targetPosition = switch (actionDirection) {
            case NORTH -> tilePosition.cpy().add(0, -1);
            case SOUTH -> tilePosition.cpy().add(0, 1);
            case EAST -> tilePosition.cpy().add(1, 0);
            case WEST -> tilePosition.cpy().add(-1, 0);
        };

        if (chain.getSnapshot().getSnapshotMap().isOutOfBounds(targetPosition)) {
            return false;
        }
        
        chain.addGameStateUpdate(new MovementGSU(tilePosition, childIndex, targetPosition));
        return true;
    }

    @Override
    public boolean update(InteractionChain chain, Vector2 tilePosition) throws GameStateUpdateException {
        return false;
    }
}
