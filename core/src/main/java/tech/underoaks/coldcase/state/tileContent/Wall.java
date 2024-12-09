package tech.underoaks.coldcase.state.tileContent;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.game.Interaction;
import tech.underoaks.coldcase.state.InteractionChain;

/**
 * Represents a wall that blocks movement on a tile.
 */
public class Wall extends TileContent {

    private static final Texture texture = new Texture("./sprites/block_detective.png");

    public Wall() {
        super(texture, false, false);
    }

    @Override
    public boolean action(InteractionChain chain, Interaction interaction) {
        return false;
    }

    @Override
    public boolean update(InteractionChain chain, Vector2 tilePosition, Interaction interaction, TileContent handler) {
        return false;
    }
}
