package tech.underoaks.coldcase.state.tileContent;

import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.game.Interaction;
import tech.underoaks.coldcase.game.TextureController;
import tech.underoaks.coldcase.state.InteractionChain;

/**
 * Represents a wall that blocks movement on a tile.
 */
public class Wall extends TileContent {

    /**
     * Default-Constructor
     */
    public Wall() {
        super(TextureController.getInstance().getWallTexture(), false, false);
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
