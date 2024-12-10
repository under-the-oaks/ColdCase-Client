package tech.underoaks.coldcase.state.tileContent;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.game.Interaction;
import tech.underoaks.coldcase.state.InteractionChain;
import tech.underoaks.coldcase.state.updates.ChangeTextureUpdate;
import tech.underoaks.coldcase.state.updates.GameStateUpdateException;
import tech.underoaks.coldcase.state.updates.PlayerPassebilityUpdate;

import java.util.Objects;

public class Door extends TileContent {

    private static final Texture texture_closed = new Texture("./sprites/object_spike_detective_2.png");
    private static final Texture texture_open = new Texture("./sprites/object_spike_retracted_detective_1.png");

    public Door() {
        super(texture_closed, false, false);
    }

    public boolean action(InteractionChain chain, Interaction interaction) throws GameStateUpdateException {

        // if action is called not by the player but by another object it opens the door, aka changes sprites and sets playerPassable to true
        if (!Objects.equals(interaction.getCaller(), Player.class.getName())) {

            int childIndex = chain.getSnapshot().getSnapshotMap().getChildIndex(interaction.getTargetPos(), this);

            if (this.isPlayerPassable()) {
                chain.addGameStateUpdate(new ChangeTextureUpdate(texture_closed, interaction.getTargetPos(), childIndex));
                chain.addGameStateUpdate(new PlayerPassebilityUpdate(false, interaction.getTargetPos(), childIndex));
            } else {
                chain.addGameStateUpdate(new ChangeTextureUpdate(texture_open, interaction.getTargetPos(), childIndex));
                chain.addGameStateUpdate(new PlayerPassebilityUpdate(true, interaction.getTargetPos(), childIndex));
            }
            return true;
        }
        else {
            return false;
        }

    }

    public boolean update(InteractionChain chain, Vector2 tilePosition, Interaction interaction, TileContent handler) {
        return false;
    }
}
