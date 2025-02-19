package tech.underoaks.coldcase.state.tileContent;

import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.game.GameController;
import tech.underoaks.coldcase.game.Interaction;
import tech.underoaks.coldcase.game.PlayerController;
import tech.underoaks.coldcase.game.TextureController;
import tech.underoaks.coldcase.state.updates.GameStateUpdateException;
import tech.underoaks.coldcase.state.InteractionChain;
import tech.underoaks.coldcase.state.updates.MoveUpdate;
import tech.underoaks.coldcase.state.Map;
import tech.underoaks.coldcase.state.tiles.Tile;

/**
 * A {@link TileContent} that is able to be moved around the play field. Moving requires a {@link GloveItem}
 */
public class MovableBlock extends TileContent {
    /**
     * Default-Constructor
     */
    public MovableBlock() {
        super(TextureController.getInstance().getMovableBlockTexture(), false, false);
    }

    @Override
    public boolean action(InteractionChain chain, Interaction interaction) throws GameStateUpdateException {

        // No Glove in Inventory
        if(interaction.getUuid().equals(GameController.getInstance().uuid.toString()))
        {
            if(PlayerController.getInstance().getInventory() == null || PlayerController.getInstance().getInventory().getClass() != GloveItem.class){
                System.out.println( "Insufficient inventory - Glove needed" );
                return false;
            }
        }

        // Glove in Inventory
        int childIndex = chain.getSnapshot().getSnapshotMap().getChildIndex(interaction.getTargetPos(), this);

        Vector2 targetPosition = interaction.getTargetPos().cpy().add(interaction.getActionDirection().getVector());

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

        chain.addGameStateUpdate(new MoveUpdate(interaction.getTargetPos(), childIndex, targetPosition));
        return true;
    }

    @Override
    public boolean update(InteractionChain chain, Vector2 tilePosition, Interaction interaction, TileContent handler) {
        return false;
    }

}
