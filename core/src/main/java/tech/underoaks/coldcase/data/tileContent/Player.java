package tech.underoaks.coldcase.data.tileContent;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.GameController;
import tech.underoaks.coldcase.GameStateUpdateException;
import tech.underoaks.coldcase.InteractionChain;
import tech.underoaks.coldcase.MoveUpdate;
import tech.underoaks.coldcase.data.Map;
import tech.underoaks.coldcase.data.tiles.Tile;
import tech.underoaks.coldcase.loader.enums.Direction;

public class Player extends TileContent {

    private static final Texture texture = new Texture("./isometric tileset/placholder_player.png");

    // Textures for the player facing different directions
    private static final Texture textureNorth = new Texture("./isometric tileset/placholder_player_north.PNG");
    private static final Texture textureSouth = new Texture("./isometric tileset/placholder_player_south.PNG");
    private static final Texture textureEast = new Texture("./isometric tileset/placholder_player_east.PNG");
    private static final Texture textureWest = new Texture("./isometric tileset/placholder_player_west.PNG");
    private Texture currentTexture;

    public Player() {
        super(texture, true, false);
        currentTexture = texture;
    }

    @Override
    public boolean action(InteractionChain chain, Vector2 tilePosition, Direction actionDirection) throws GameStateUpdateException {

        int childIndex = chain.getSnapshot().getSnapshotMap().getChildIndex(tilePosition, this);
        Vector2 targetPosition = switch (actionDirection) {
            case NORTH -> tilePosition.cpy().add(0, -1);
            case SOUTH -> tilePosition.cpy().add(0, 1);
            case EAST -> tilePosition.cpy().add(1, 0);
            case WEST -> tilePosition.cpy().add(-1, 0);
        };

        Map snapshotMap = chain.getSnapshot().getSnapshotMap();

        //validation checks: if the target tile is out of bounds
        if (snapshotMap.isOutOfBounds(targetPosition)) {
            return false;
        }

        //validation checks: if the target tile is not passable
        Tile targetTile = snapshotMap.getTile(targetPosition);
        TileContent topTargetContent = targetTile.topTileContent();
        if (topTargetContent != null && !topTargetContent.isPlayerPassable()) {
            return false;
        }

        // adding the validated movement to the chain
        chain.addGameStateUpdate(new MoveUpdate(tilePosition, childIndex, targetPosition));
        return true;
    }

    @Override
    public boolean update(InteractionChain chain, Vector2 tilePosition) throws GameStateUpdateException {
        return false;
    }

    /**
     * Updates the texture of an object based on the specified direction the object is facing.
     *
     * @param lookDirection The direction that the object is looking toward. Valid values are {@code NORTH},
     *                      {@code SOUTH}, {@code EAST}, and {@code WEST}. The method sets the texture
     *                      according to this direction.
     */
    public void updateTexture(Direction lookDirection) {
        Texture newTexture = switch (lookDirection) {
            case NORTH -> textureNorth;
            case SOUTH -> textureSouth;
            case EAST -> textureEast;
            case WEST -> textureWest;
        };

        if (newTexture != currentTexture) {
            this.currentTexture = newTexture;
            System.out.println("Player texture updated: " + newTexture);
        }
    }

    /**
     * Renders the object at the specified coordinates using the {@link SpriteBatch}.
     * If a texture is currently set, it draws the object with the texture; otherwise,
     * it invokes the superclass's rendering method.
     *
     * @param batch The {@link SpriteBatch} used to draw the texture.
     * @param x     The x-coordinate where the texture should be drawn.
     * @param y     The y-coordinate where the texture should be drawn. The y-coordinate is
     *              adjusted by adding 8 units for positioning purposes.
     */
    @Override
    public void render(SpriteBatch batch, float x, float y) {
        if (currentTexture != null) {
            batch.draw(currentTexture, x, y + 8);
        } else {
            super.render(batch, x, y);
        }
    }
}
