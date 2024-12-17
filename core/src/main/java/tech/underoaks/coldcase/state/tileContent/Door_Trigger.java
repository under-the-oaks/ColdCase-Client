package tech.underoaks.coldcase.state.tileContent;

import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.game.Interaction;
import tech.underoaks.coldcase.game.TextureController;
import tech.underoaks.coldcase.state.InteractionChain;
import tech.underoaks.coldcase.state.updates.GameStateUpdateException;

import java.util.Objects;

public class Door_Trigger extends TileContent{

    public Door_Trigger() {
        super(TextureController.getInstance().getDoorTriggerTexture(), true, false);
    }

    @Override
    public boolean action(InteractionChain chain, Interaction interaction) throws GameStateUpdateException {

        if (Objects.equals(interaction.getCaller(), Player.class.getName())){
            chain.addRemoteAction(new Interaction(interaction.getTargetPos(), interaction.getActionDirection(), this.getClass()));
        }

        return true;
    }

    @Override
    public boolean update(InteractionChain chain, Vector2 tilePosition, Interaction interaction, TileContent handler) throws GameStateUpdateException, UpdateTileContentException {
        return false;
    }

}
