package tech.underoaks.coldcase.state.tileContent;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.state.InteractionChain;
import tech.underoaks.coldcase.game.Direction;

/**
 * Represents a wall that blocks movement on a tile.
 */
public class Wall extends TileContent {

    private static final Texture texture = new Texture("./isometric tileset/separated images/tile_065.png");

    public Wall() {
        super(texture, false, false);
    }

    @Override
    public boolean action(InteractionChain chain, Vector2 tilePosition, Direction actionDirection) {
        // TODO
        return false;
    }

    @Override
    public boolean update(InteractionChain chain, Vector2 tilePosition) {
        // TODO
        return false;
    }
}
