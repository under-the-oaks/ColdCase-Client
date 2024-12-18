package tech.underoaks.coldcase.state.tileContent;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import tech.underoaks.coldcase.game.Interaction;
import tech.underoaks.coldcase.state.InteractionChain;
import tech.underoaks.coldcase.state.Map;
import tech.underoaks.coldcase.game.PlayerController;
import tech.underoaks.coldcase.state.tiles.Tile;
import tech.underoaks.coldcase.state.updates.GameStateUpdateException;
import tech.underoaks.coldcase.state.updates.MoveUpdate;

public class MovableBlockTranscendent extends TileContent{
    private static final Texture texture = new Texture("./sprites/block_transcendent_2.png");

    public MovableBlockTranscendent() {

        super(texture, false, false);
        this.visibilityState = VisibilityStates.TRANSCENDENT;
    }

    @Override
    public boolean action(InteractionChain chain, Interaction interaction) throws GameStateUpdateException {

        // No Glove in Inventory

        if(PlayerController.getInstance().getInventory() == null || PlayerController.getInstance().getInventory().getClass() != GloveItem.class){
            System.out.println( "Insufficient inventory - Glove needed" );
            return false;
        }
        
        System.out.println("MovableBlockTranscendent.action() - red rock");

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
    public boolean update(InteractionChain chain, Vector2 tilePosition, Interaction interaction, TileContent handler) throws GameStateUpdateException {
        return false;
    }
}
