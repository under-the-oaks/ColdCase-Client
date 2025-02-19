package tech.underoaks.coldcase.state.tileContent;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.game.GameController;
import tech.underoaks.coldcase.game.Interaction;
import tech.underoaks.coldcase.game.PlayerController;
import tech.underoaks.coldcase.game.TextureController;
import tech.underoaks.coldcase.state.InteractionChain;
import tech.underoaks.coldcase.state.updates.ChangeTextureUpdate;
import tech.underoaks.coldcase.state.updates.GameStateUpdateException;

import java.util.Objects;

/**
 * A {@link TileContent} that is able to unlock a door on the different client.
 * A {@link GloveItem} is needed to interact with this Content.
 */
public class Door_Trigger extends TileContent{

    private final Texture trigger_closed = TextureController.getInstance().getTrigger_closed();
    private final Texture trigger_opened = TextureController.getInstance().getTrigger_opened();
    private static boolean isOpen = false;

    /**
     * Default-Constructor
     */
    public Door_Trigger() {
        super(TextureController.getInstance().getTrigger_closed(), true, false);
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

        if (Objects.equals(interaction.getCaller(), Player.class.getName())) {

            int childIndex = chain.getSnapshot().getSnapshotMap().getChildIndex(interaction.getTargetPos(), this);

            if (!isOpen) {
                chain.addGameStateUpdate(new ChangeTextureUpdate(trigger_opened, interaction.getTargetPos(), childIndex));
                isOpen = true;
            } else {
                chain.addGameStateUpdate(new ChangeTextureUpdate(trigger_closed, interaction.getTargetPos(), childIndex));
                isOpen = false;
            }

            chain.addRemoteAction(new Interaction(interaction.getTargetPos(), interaction.getActionDirection(), this.getClass()));
        }

        return true;
    }

    @Override
    public boolean update(InteractionChain chain, Vector2 tilePosition, Interaction interaction, TileContent handler) throws GameStateUpdateException, UpdateTileContentException {
        return false;
    }

}
