package tech.underoaks.coldcase.state.tiles;

import com.badlogic.gdx.graphics.Texture;

/**
 * Represents an empty tile with no special properties.
 */
public class EmptyTile extends Tile {

    private static Texture texture;

    public EmptyTile() {
        super(getTexture());
    }

    private static Texture getTexture() {
        if (texture == null) {
            texture = new Texture("./isometric tileset/separated images/tile_101.png");
        }
        return texture;
    }
}
