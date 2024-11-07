package tech.underoaks.coldcase.data.tileContent;

import tech.underoaks.coldcase.GameController;
import tech.underoaks.coldcase.data.tiles.Tile;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import tech.underoaks.coldcase.enums.VisibilityStates;

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
    /** Reference to the next TileContent in the stack */
    protected TileContent tileContent;

    /** The visibility state of this TileContent */
    protected VisibilityStates visibilityState;

    private Texture texture;

    private boolean isPlayerPassable;
    private boolean isObjectPassable;

    public TileContent(Texture texture, boolean isPlayerPassable, boolean isObjectPassable) {
        this.texture = texture;
        this.isPlayerPassable = isPlayerPassable;
        this.isObjectPassable = isObjectPassable;
    }


    /**
     * Renders the tileContent at the specified coordinates using the given {@code SpriteBatch}.
     * If the tile has no texture, this method renders nothing.
     *
     * <p>If the tileContent has a {@code TileContent}, the render method of the content is called.
     *
     * @param batch the {@code SpriteBatch} used to render the tileContent
     * @param x     the x-coordinate for rendering the tile
     * @param y     the y-coordinate for rendering the tile
     */
    public void render(SpriteBatch batch, float x, float y) {
        if (texture != null) {
            batch.draw(texture, x, y + 8);
        }

        if (tileContent != null) {
            tileContent.render(batch, x, y);
        }
    }

    /**
     * Performs the action associated with this TileContent when interacted with.
     *
     * @param controller The GameController managing the interaction.
     */
    public abstract void action(GameController controller);

    /**
     * Updates the state of this TileContent based on interactions.
     *
     * @param controller The GameController managing the interaction.
     */
    public abstract void update(GameController controller);

    public void setNextContent(TileContent tileContent) {
        this.tileContent = tileContent;
    }

    public TileContent getNextContent() {
        return tileContent;
    }

    public void pushContent(TileContent tileContent) {
        if(this.tileContent != null) {
            this.tileContent.pushContent(tileContent);
        }
        else {
            this.tileContent = tileContent;
        }
    }

    public TileContent popContent() {
        if(this.tileContent == null) {
            return null;
        }
        if(this.tileContent.tileContent == null) {
            return this.tileContent;
        }
        return this.tileContent.popContent();
    }

    public VisibilityStates getVisibilityState() {
        return visibilityState;
    }

    public void setVisibilityState(VisibilityStates visibilityState) {
        this.visibilityState = visibilityState;
    }


    public boolean isObjectPassable() {
        return isObjectPassable;
    }

    public void setObjectPassable(boolean objectPassable) {
        isObjectPassable = objectPassable;
    }

    public boolean isPlayerPassable() {
        return isPlayerPassable;
    }

    public void setPlayerPassable(boolean playerPassable) {
        isPlayerPassable = playerPassable;
    }
}
