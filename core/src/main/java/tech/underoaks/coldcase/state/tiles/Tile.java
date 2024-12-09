package tech.underoaks.coldcase.state.tiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import tech.underoaks.coldcase.state.tileContent.TileContent;

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
public abstract class Tile implements Cloneable {
    /**
     * The content placed on this Tile
     */
    private TileContent tileContent;

    private Texture texture;

    private Sprite sprite;

    public Tile(Texture texture) {
        this.texture = texture;
        this.sprite = new Sprite(texture);
        this.sprite.setOriginCenter();
    }

    /**
     * Renders the tile at the specified coordinates using the given {@code SpriteBatch}.
     * If the tile has no texture, this method renders nothing.
     *
     * <p>If the tile has a {@code TileContent}, the render method of the content is called.
     *
     * @param batch the {@code SpriteBatch} used to render the tile
     * @param x     the x-coordinate for rendering the tile
     * @param y     the y-coordinate for rendering the tile
     */
    public void render(SpriteBatch batch, float x, float y) {

        if (texture != null) {
            batch.draw(sprite, x, y);
        }

        batch.draw(sprite, x, y);
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

    public void pushTileContent(TileContent tileContent) {
        if (this.tileContent == null) {
            this.tileContent = tileContent;
        } else {
            this.tileContent.pushContent(tileContent);
        }
    }

    public TileContent popTileContent() {
        if (this.tileContent == null) {
            return null;
        } else if (this.tileContent.tileContent == null) {
            TileContent content = this.tileContent;
            this.tileContent = null;
            return content;
        } else {
            return this.tileContent.popContent();
        }
    }

    public TileContent topTileContent() {
        if (this.tileContent == null) {
            return null;
        } else {
            return this.tileContent.topContent();
        }
    }

    @Override
    public Tile clone() {
        try {
            Tile cloned = (Tile) super.clone();
            if (this.tileContent != null) {
                cloned.tileContent = this.tileContent.clone();
            }
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Tile other = (Tile) obj;

        // Compare tileContent: both should be either equal or both should be null
        if (tileContent != null ? !tileContent.equals(other.tileContent) : other.tileContent != null) {
            return false;  // tileContent is not equal or one is null, return false
        }

        return true;
    }


    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
        if (tileContent != null) {
            tileContent.dispose();
        }
    }

}
