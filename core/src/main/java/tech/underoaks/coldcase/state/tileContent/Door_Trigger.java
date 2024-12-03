package tech.underoaks.coldcase.state.tileContent;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.game.Interaction;
import tech.underoaks.coldcase.state.InteractionChain;
import tech.underoaks.coldcase.state.updates.GameStateUpdateException;

public class Door_Trigger extends TileContent{

    private static final Texture texture = new Texture("./isometric tileset/separated images/tile_069.png");

    public Door_Trigger() {
        super(texture, false, false);
    }

    @Override
    public boolean action(InteractionChain chain, Interaction interaction) throws GameStateUpdateException {

        if (interaction.getCaller() instanceof Player){
            chain.getPendingRemoteActions().add(new Interaction(interaction.getTargetPos(), interaction.getActionDirection(), this));
        }

        return true;
    }

    @Override
    public boolean update(InteractionChain chain, Vector2 tilePosition) throws GameStateUpdateException, UpdateTileContentException {
        return false;
    }

}
