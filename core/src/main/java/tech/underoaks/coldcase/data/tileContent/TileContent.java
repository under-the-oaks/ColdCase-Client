package tech.underoaks.coldcase.data.tileContent;

import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.GameStateUpdateException;
import tech.underoaks.coldcase.InteractionChain;
import tech.underoaks.coldcase.remote.WebSocketClient;
import tech.underoaks.coldcase.data.tiles.Tile;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import tech.underoaks.coldcase.enums.VisibilityStates;
import tech.underoaks.coldcase.loader.enums.Direction;

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

    protected boolean isTranscendent = false;
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
     * Tries to perform the action associated with this TileContent when interacted with.
     *
     * <p>{@code handleAction(...)} is a recursive function that traverses a stack of {@code TileContent}s in post order.
     * This means that the top most content gets the first chance to handle a triggered action.
     * If a content handles the action no more contents will be able to accept it.
     *
     * @param chain           InteractionChain managing the snapshot.
     * @param actionDirection The direction in wich the action get triggered.
     * @param tilePosition    The position of the currently selected tile.
     * @return True if the action has been taken care of; False otherwise
     * @throws GameStateUpdateException If a GameStateUpdate has failed
     */
    public boolean handleAction(InteractionChain chain, Vector2 tilePosition, Direction actionDirection) throws GameStateUpdateException {
        if (tileContent != null && tileContent.handleAction(chain, tilePosition, actionDirection)) {
            return true;
        }
        if(isTranscendent){
            return remoteAction(chain, tilePosition, actionDirection);
        }
        return action(chain, tilePosition, actionDirection);
    }

    /**
     * Performs the action associated with this TileContent when interacted with.
     *
     * @param chain           InteractionChain managing the snapshot.
     * @param actionDirection The direction in wich the action get triggered.
     * @param tilePosition    The position of the currently selected tile.
     * @return True if the action has been taken care of; False otherwise
     * @throws GameStateUpdateException If a GameStateUpdate has failed
     */
    public abstract boolean action(InteractionChain chain, Vector2 tilePosition, Direction actionDirection) throws GameStateUpdateException;


    public boolean remoteAction(InteractionChain chain, Vector2 tilePosition, Direction actionDirection) throws GameStateUpdateException{
        //check local action
        if(!action(chain, tilePosition,actionDirection)){
            return false;
        }

        WebSocketClient client = WebSocketClient.getInstance();

        client.createRemoteInteractionChain();

        if (client.appendRemoteInteraction()){
            client.applyRemoteGSUs();
            return true;
        }
        client.abortRemoteInteractionChain();
        return false;
    }


    /**
     * Tries to perform an update associated with this TileContent when triggered.
     *
     * <p>{@code handleUpdate(...)} is a recursive function that traverses a stack of {@code TileContent}s in post order.
     * This means that the top most content gets the first chance to handle a triggered update.
     *
     * @param chain        InteractionChain managing the snapshot.
     * @param tilePosition The position of the currently selected tile.
     * @return True if an update as been performed; False otherwise
     * @throws GameStateUpdateException If a GameStateUpdate has failed
     * @see TileContent#update(InteractionChain, Vector2)
     */
    public boolean handleUpdate(InteractionChain chain, Vector2 tilePosition) throws GameStateUpdateException {
        boolean result_child = false;
        if (tileContent != null) {
            result_child = tileContent.handleUpdate(chain, tilePosition);
        }
        boolean result_self = update(chain, tilePosition);
        return result_child || result_self;
    }

    /**
     * Updates the state of this TileContent based on interactions.
     *
     * @param chain        InteractionChain managing the snapshot.
     * @param tilePosition The position of the currently selected tile.
     * @return True if an update as been performed; False otherwise
     * @throws GameStateUpdateException If a GameStateUpdate has failed
     * @implNote Ensure this method returns {@code true} only for meaningful changes to avoid unnecessary processing.
     * It should not always return {@code true} to prevent infinite loops in calling methods like
     * {@code updateUntilStable}. Avoid cyclic updates that could trigger endless interactions.
     */
    public abstract boolean update(InteractionChain chain, Vector2 tilePosition) throws GameStateUpdateException;

    public void setNextContent(TileContent tileContent) {
        this.tileContent = tileContent;
    }

    public TileContent getNextContent() {
        return tileContent;
    }

    /**
     * Adds a new content layer on top of the current stack of contents.
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

    public TileContent topContent() {
        if (this.tileContent == null) {
            return this;
        }
        return this.tileContent.topContent();
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

    public void dispose() {
        texture.dispose();
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

    public TileContent getTileContentByIndex(int i) {
        if (i == 0) {
            return this;
        }
        if (tileContent == null) {
            throw new IllegalArgumentException("TileContent not found");
        }
        return tileContent.getTileContentByIndex(i - 1);
    }
}
