package tech.underoaks.coldcase.state.tiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
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

    /**
     * Constructs a new {@code Tile} with the specified texture.
     * <p>
     * This constructor initializes the tile's texture and creates a corresponding {@link Sprite},
     * setting its origin to the center. The texture is used for rendering the tile.
     * </p>
     *
     * @param texture the {@link Texture} to be used for this tile's appearance
     */
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
    public void render(Batch batch, float x, float y) {

        if (texture != null) {
            batch.draw(sprite, x, y);
        }

        if (tileContent != null) {
            tileContent.render(batch, x, y);
        }
    }

    /**
     * Retrieves the {@link TileContent} currently placed on this tile.
     *
     * @return the current {@code TileContent} on the tile, or {@code null} if none is set
     */
    public TileContent getTileContent() {
        return tileContent;
    }

    /**
     * Sets the {@link TileContent} for this tile.
     * <p>
     * This method replaces any existing content on the tile with the specified {@code TileContent}.
     * </p>
     *
     * @param tileContent the new {@code TileContent} to set on the tile
     */
    public void setTileContent(TileContent tileContent) {
        this.tileContent = tileContent;
    }

    /**
     * Adds the specified {@link TileContent} to this tile.
     * <p>
     * If the tile currently has no content, the provided {@code TileContent} is set directly.
     * Otherwise, the new content is pushed onto the existing content stack.
     * </p>
     *
     * @param tileContent the {@code TileContent} to add to the tile
     */
    public void pushTileContent(TileContent tileContent) {
        if (this.tileContent == null) {
            this.tileContent = tileContent;
        } else {
            this.tileContent.pushContent(tileContent);
        }
    }

    /**
     * Removes and returns the top {@link TileContent} from this tile.
     * <p>
     * If the tile's content is a stack (i.e., contains nested {@code TileContent}),
     * this method removes and returns the top element of that stack.
     * If there is only one content element, it is removed and the tile's content is set to {@code null}.
     * If the tile has no content, {@code null} is returned.
     * </p>
     *
     * @return the removed {@code TileContent}, or {@code null} if the tile was empty
     */
    public TileContent popTileContent() {
        if (this.tileContent == null) {
            return null;
        } else if (this.tileContent.getNextContent() == null) {
            TileContent content = this.tileContent;
            this.tileContent = null;
            return content;
        } else {
            return this.tileContent.popContent();
        }
    }

    /**
     * Retrieves the top {@link TileContent} from this tile without removing it.
     * <p>
     * If the tile's content is a stack of {@code TileContent} objects, this method returns
     * the top element of that stack. If the tile has no content, it returns {@code null}.
     * </p>
     *
     * @return the top {@code TileContent} on the tile, or {@code null} if the tile is empty
     */
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

    /**
     * Releases the resources used by this tile.
     * <p>
     * Disposes of the tile's texture if it is not {@code null}, and also calls {@code dispose()}
     * on its {@link TileContent} if present.
     * </p>
     */
    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
        if (tileContent != null) {
            tileContent.dispose();
        }
    }

}
