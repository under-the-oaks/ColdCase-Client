package tech.underoaks.coldcase.state.tileContent;

import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.game.Interaction;
import tech.underoaks.coldcase.game.TextureController;
import tech.underoaks.coldcase.state.InteractionChain;
import tech.underoaks.coldcase.state.Map;
import tech.underoaks.coldcase.state.tiles.Tile;
import tech.underoaks.coldcase.state.updates.GameStateUpdateException;
import tech.underoaks.coldcase.state.updates.MoveUpdate;

/**
 * Test Block for transcendent interactions
 * @implNote Only for testing purposes
 */
public class TranscendentTestBlock extends TileContent{

    /**
     * Default-Constructor
     */
    public TranscendentTestBlock() {

        super(TextureController.getInstance().getTranscendentTestBlockTexture(), false, false);
        this.visibilityState = VisibilityStates.TRANSCENDENT;
    }

    @Override
    public boolean action(InteractionChain chain, Interaction interaction) throws GameStateUpdateException {

        System.out.println("TranscendentTestBlock.action() - flower patch");

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
        chain.addAction(new Interaction(new Vector2(3, 3), interaction.getActionDirection(), this.getClass()));
        return true;
    }

    @Override
    public boolean update(InteractionChain chain, Vector2 tilePosition, Interaction interaction, TileContent handler) throws GameStateUpdateException {
        return false;
    }
}
