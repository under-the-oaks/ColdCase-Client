package tech.underoaks.coldcase.data.tiles;

import com.badlogic.gdx.graphics.Texture;

/**
 * Represents an empty tile with no special properties.
 */
public class EmptyTile extends Tile {

    private static final Texture texture = new Texture("./isometric tileset/separated images/tile_101.png");

    public EmptyTile() {
        super(texture);
    }
}
