package tech.underoaks.coldcase.data.tileContent;

import com.badlogic.gdx.graphics.Texture;
import tech.underoaks.coldcase.data.tiles.Tile;

/**
 * The {@code TileContent} class represents the content that can be placed on a {@code Tile}.
 * This content can be an item, a static object like a wall, or any other entity that occupies
 * a tile on the game board.
 *
 * <p>Each instance of {@code TileContent} can represent a unique object or type of content.
 * Subclasses or specific instances may be used to specify different types of content.
 *
 * <p>This class provides a way to abstractly define what exists on a tile without needing
 * to specify the specific type directly within the {@code Tile} class.
 *
 * @see Tile
 */
public abstract class TileContent {

    private Texture texture;

    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }
}
