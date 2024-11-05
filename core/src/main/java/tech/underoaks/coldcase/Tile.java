package tech.underoaks.coldcase;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * The {@code Tile} class represents a single tile on the game board.
 * Each tile can hold a specific {@code TileContent}, which may be an item, a static object
 * like a wall, or any other entity that occupies a tile.
 *
 * <p>This class provides rendering functionality, allowing each tile to display its texture
 * on the screen. The texture and content of each tile can be configured and updated, enabling
 * dynamic changes on the game board.
 *
 * <p>As an abstract class, {@code Tile} can be extended to create specific types of tiles
 * with unique behaviors or additional properties.
 *
 * @see TileContent
 */
public abstract class Tile {
    private TileContent tileContent;
    private Texture texture;

    public Tile(Texture texture) {
        this.texture = texture;
    }

    /**
     * Renders the tile at the specified coordinates using the given {@code SpriteBatch}.
     * If the tile has no texture, this method does nothing.
     *
     * @param batch the {@code SpriteBatch} used to render the tile
     * @param x the x-coordinate for rendering the tile
     * @param y the y-coordinate for rendering the tile
     */
    public void render(SpriteBatch batch, float x, float y) {
        if (texture != null) {
            batch.draw(texture, x, y);
        }

        batch.draw(texture, x, y);
        if (tileContent != null) {
            tileContent.render(batch, x, y);
        }
    }

    public TileContent getTileContent() {
        return tileContent;
    }

    public void setTileContent(TileContent tileContent) {
        this.tileContent = tileContent;
    }

}
