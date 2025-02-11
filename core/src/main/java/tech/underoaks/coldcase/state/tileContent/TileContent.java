package tech.underoaks.coldcase.state.tileContent;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import tech.underoaks.coldcase.game.Interaction;
import tech.underoaks.coldcase.state.updates.GameStateUpdateException;
import tech.underoaks.coldcase.state.InteractionChain;
import tech.underoaks.coldcase.state.tiles.Tile;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
public abstract class TileContent implements Cloneable {
    /**
     * Reference to the next TileContent in the stack
     */
    public TileContent tileContent;

    /**
     * The visibility state of this TileContent
     */
    protected VisibilityStates visibilityState;

    private Texture texture;
    private Sprite sprite;

    private boolean isPlayerPassable;
    private boolean isObjectPassable;

    /**
     * Constructs a new {@code TileContent} with the specified texture and passability properties.
     * <p>
     * If a texture is provided, a new {@link Sprite} is created and its origin is set to the center.
     * The content's visibility is set to {@link VisibilityStates#PLAYER_ONE_ONLY} by default.
     * </p>
     *
     * @param texture          the {@link Texture} used for rendering the TileContent; may be {@code null}
     * @param isPlayerPassable {@code true} if the content can be passed by the player, {@code false} otherwise
     * @param isObjectPassable {@code true} if the content can be passed by objects, {@code false} otherwise
     */
    public TileContent(Texture texture, boolean isPlayerPassable, boolean isObjectPassable) {
        this.texture = texture;

        if (texture != null) {
            this.sprite = new Sprite(texture);
            this.sprite.setOriginCenter();
        }

        this.isPlayerPassable = isPlayerPassable;
        this.isObjectPassable = isObjectPassable;
        this.visibilityState = VisibilityStates.PLAYER_ONE_ONLY; // FIXME als Parameter callen
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
    public void render(Batch batch, float x, float y) {
        if (sprite != null) {
            batch.draw(sprite, x, y + 410);
        }

        if (tileContent != null) {
            tileContent.render(batch, x, y);
        }
    }

    /**
     * FIXME JavaDoc
     * Tries to perform the action associated with this TileContent when interacted with.
     *
     * <p>{@code handleAction(...)} is a recursive function that traverses a stack of {@code TileContent}s in post order.
     * This means that the top most content gets the first chance to handle a triggered action.
     * If a content handles the action no more contents will be able to accept it.
     *
     * @param chain       InteractionChain managing the snapshot.
     * @param interaction The interaction to trigger.
     * @return True if the action has been taken care of; False otherwise
     * @throws GameStateUpdateException If a GameStateUpdate has failed
     */
    public TileContent handleAction(InteractionChain chain, Interaction interaction) throws GameStateUpdateException {
        TileContent handler;
        if (tileContent != null) {
            handler = tileContent.handleAction(chain, interaction);
            if (handler != null) {
                return handler;
            }
        }

        // Wenn auf dem Player eine Interaktion durchführt wird, wird unabhängig von dessen Ergebnis der Command als 'behandelt' betrachtet.
        if (this.getClass() == Player.class) {
            action(chain, interaction);
            return this;
        }

        return action(chain, interaction) ? this : null;
    }

    /**
     * Performs the action associated with this TileContent when interacted with.
     *
     * @param chain       InteractionChain managing the snapshot.
     * @param interaction The interaction to trigger.
     * @return True if the action has been taken care of; False otherwise
     * @throws GameStateUpdateException If a GameStateUpdate has failed
     */
    public abstract boolean action(InteractionChain chain, Interaction interaction) throws GameStateUpdateException;

    /**
     * FIXME JavaDoc
     * Tries to perform an update associated with this TileContent when triggered.
     *
     * <p>{@code handleUpdate(...)} is a recursive function that traverses a stack of {@code TileContent}s in post order.
     * This means that the top most content gets the first chance to handle a triggered update.
     *
     * @param chain        InteractionChain managing the snapshot.
     * @param tilePosition The position of the currently selected tile.
     * @param interaction  The interaction that needs to be handled.
     * @param handler      The TileContent that has been handling the interaction so far.
     * @return True if an update as been performed; False otherwise
     * @throws GameStateUpdateException   If a GameStateUpdate has failed
     * @throws UpdateTileContentException If a TileContent couldn't be updated (due to a failing validation)
     * @see TileContent#update(InteractionChain, Vector2, Interaction, TileContent)
     */
    public List<TileContent> handleUpdate(InteractionChain chain, Vector2 tilePosition, Interaction interaction, TileContent handler) throws GameStateUpdateException, UpdateTileContentException {
        List<TileContent> handlers = tileContent != null ? tileContent.handleUpdate(chain, tilePosition, interaction, handler) : new ArrayList<>();

        if (update(chain, tilePosition, interaction, handler)) {
            handlers.add(this);
        }

        return handlers;
    }

    /**
     * Updates the state of this TileContent based on interactions.
     *
     * @param chain        InteractionChain managing the snapshot.
     * @param tilePosition The position of the currently selected tile.
     * @param interaction  The interaction that is currently being handled.
     * @param handler      The TileContent that has been handling the interaction so far.
     * @return True if an update has been performed; False otherwise
     * @throws GameStateUpdateException   If a GameStateUpdate has failed
     * @throws UpdateTileContentException If the TileContent couldn't be updated (due to a failing validation)
     * @implNote Ensure this method returns {@code true} only for meaningful changes to avoid unnecessary processing.
     * It should not always return {@code true} to prevent infinite loops in calling methods like
     * {@code updateUntilStable}. Avoid cyclic updates that could trigger endless interactions.
     */
    public abstract boolean update(InteractionChain chain, Vector2 tilePosition, Interaction interaction, TileContent handler) throws GameStateUpdateException, UpdateTileContentException;

    /**
     * Sets the next {@code TileContent} in the stack.
     *
     * @param tileContent the {@code TileContent} to set as the next content in the stack
     */
    public void setNextContent(TileContent tileContent) {
        this.tileContent = tileContent;
    }

    /**
     * Returns the next {@code TileContent} in the stack.
     *
     * @return the next {@code TileContent}, or {@code null} if there is none
     */
    public TileContent getNextContent() {
        return tileContent;
    }

    /**
     * Adds a new content layer on top of the current stack of contents.
     *
     * @param tileContent The TileContent that is being injected into the Map
     */
    public void pushContent(TileContent tileContent) {
        if (this.tileContent != null) {
            this.tileContent.pushContent(tileContent);
        } else {
            this.tileContent = tileContent;
        }
    }

    /**
     * Removes the topmost content layer from the stack and returns it.
     *
     * @return The topmost {@link TileContent}
     */
    public TileContent popContent() {
        if (this.tileContent == null) {
            return null;
        }
        if (this.tileContent.tileContent == null) {
            TileContent content = this.tileContent;
            this.tileContent = null;
            return content;
        }
        return this.tileContent.popContent();
    }

    /**
     * Retrieves the topmost {@code TileContent} in the stack.
     * <p>
     * If there are nested contents, this method returns the topmost element. Otherwise, it returns this.
     * </p>
     *
     * @return the topmost {@code TileContent} in the stack
     */
    public TileContent topContent() {
        if (this.tileContent == null) {
            return this;
        }
        return this.tileContent.topContent();
    }

    /**
     * Returns the current visibility state of this {@code TileContent}.
     *
     * @return the {@link VisibilityStates} representing the visibility of this content
     */
    public VisibilityStates getVisibilityState() {
        return visibilityState;
    }

    /**
     * Sets the visibility state for this {@code TileContent}.
     *
     * @param visibilityState the new {@link VisibilityStates} to apply
     */
    public void setVisibilityState(VisibilityStates visibilityState) {
        this.visibilityState = visibilityState;
    }

    /**
     * Indicates whether this {@code TileContent} is passable by objects.
     *
     * @return {@code true} if objects can pass through this content, {@code false} otherwise
     */
    public boolean isObjectPassable() {
        return isObjectPassable;
    }

    /**
     * Sets whether this {@code TileContent} is passable by objects.
     *
     * @param objectPassable {@code true} if objects should be able to pass through this content, {@code false} otherwise
     */
    public void setObjectPassable(boolean objectPassable) {
        isObjectPassable = objectPassable;
    }

    /**
     * Indicates whether this {@code TileContent} is passable by the player.
     *
     * @return {@code true} if the player can pass through this content, {@code false} otherwise
     */
    public boolean isPlayerPassable() {
        return isPlayerPassable;
    }

    /**
     * Sets whether this {@code TileContent} is passable by the player.
     *
     * @param playerPassable {@code true} if the player should be able to pass through this content, {@code false} otherwise
     */
    public void setPlayerPassable(boolean playerPassable) {
        isPlayerPassable = playerPassable;
    }

    /**
     * Disposes of the resources used by this TileContent.
     * <p>
     * Releases the underlying texture and disposes any nested {@code TileContent} if present.
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

    @Override
    public TileContent clone() throws CloneNotSupportedException {
        try {
            TileContent cloned = (TileContent) super.clone();
            if (this.tileContent != null) {
                cloned.tileContent = this.tileContent.clone();
            }
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Returns the index of the specified {@code TileContent} in the content stack.
     * <p>
     * The current {@code TileContent} is considered to have index 0. If the specified content is not found,
     * {@code -1} is returned.
     * </p>
     *
     * @param tileContent the {@code TileContent} whose index is to be determined
     * @return the index of the specified {@code TileContent}, or {@code -1} if not found
     */
    public int getChildIndex(TileContent tileContent) {
        if (this == tileContent) {
            return 0;
        }
        if (this.tileContent == null) {
            return -1;
        }
        int index = this.tileContent.getChildIndex(tileContent);
        if (index == -1) {
            return -1;
        }
        return index + 1;
    }

    /**
     * Retrieves a {@code TileContent} from the content stack by its index.
     * <p>
     * An index of 0 returns this {@code TileContent}. For higher indices, the method traverses the nested content.
     * If the specified index is not found, an {@code IllegalArgumentException} is thrown.
     * </p>
     *
     * @param i the index of the desired {@code TileContent} (0 for this, 1 for the next, etc.)
     * @return the {@code TileContent} at the specified index
     * @throws IllegalArgumentException if no {@code TileContent} exists at the given index
     */
    public TileContent getTileContentByIndex(int i) {
        if (i == 0) {
            return this;
        }
        if (tileContent == null) {
            throw new IllegalArgumentException("TileContent not found");
        }
        return tileContent.getTileContentByIndex(i - 1);
    }

    /**
     * Returns the {@link Texture} associated with this {@code TileContent}.
     *
     * @return the texture used for rendering, or {@code null} if none is set
     */
    public Texture getTexture() {
        return texture;
    }

    /**
     * Sets the texture for this {@code TileContent} and updates its sprite.
     *
     * @param texture the new {@link Texture} to be used for rendering
     */
    public void setTexture(Texture texture) {
        this.texture = texture;
        this.sprite = new Sprite(texture);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj == null || getClass() != obj.getClass()) return false;

        TileContent other = (TileContent) obj;

        if (isPlayerPassable != other.isPlayerPassable) return false;
        if (isObjectPassable != other.isObjectPassable) return false;
        if (visibilityState != other.visibilityState) return false;

        if (!Objects.equals(texture, other.texture)) return false;
        return Objects.equals(sprite, other.sprite);
    }


}
