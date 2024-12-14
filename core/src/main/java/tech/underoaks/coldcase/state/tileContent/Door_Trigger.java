package tech.underoaks.coldcase.state.tileContent;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.game.Interaction;
import tech.underoaks.coldcase.state.InteractionChain;
import tech.underoaks.coldcase.state.updates.GameStateUpdateException;

import java.util.Objects;

public class Door_Trigger extends TileContent{

    private static final Texture texture = new Texture("./sprites/block_transcendent.png");

    public Door_Trigger() {
        super(texture, true, false);
    }

    @Override
    public boolean action(InteractionChain chain, Interaction interaction) throws GameStateUpdateException {

        if (Objects.equals(interaction.getCaller(), Player.class.getName())){
            chain.getPendingRemoteActions().add(new Interaction(interaction.getTargetPos(), interaction.getActionDirection(), this.getClass()));
        }

        return true;
    }

    @Override
    public boolean update(InteractionChain chain, Vector2 tilePosition, Interaction interaction, TileContent handler) throws GameStateUpdateException, UpdateTileContentException {
        return false;
    }

}
