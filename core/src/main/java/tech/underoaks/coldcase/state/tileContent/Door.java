package tech.underoaks.coldcase.state.tileContent;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.game.Interaction;
import tech.underoaks.coldcase.state.InteractionChain;
import tech.underoaks.coldcase.state.updates.GameStateUpdateException;

public class Door extends TileContent {

    private static final Texture texture_closed = new Texture("./isometric tileset/separated images/tile_067.png");
    private static final Texture texture_open = new Texture("./isometric tileset/separated images/tile_068.png");

    public Door() {
        super(texture_closed, false, false);
    }

    public boolean action(InteractionChain chain, Interaction interaction) throws GameStateUpdateException {

        // if action is called not by the player but by another object it opens the door, aka changes sprites and sets playerPassable to true
        if (interaction.getCaller() != Player.class){
            this.setTexture(texture_open);
            this.setPlayerPassable(!this.isPlayerPassable());
        }

        return true;
    }

    public boolean update(InteractionChain chain, Vector2 tilePosition) {
        return false;
    }
}
