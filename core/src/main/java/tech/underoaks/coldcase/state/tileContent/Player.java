package tech.underoaks.coldcase.state.tileContent;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.game.Interaction;
import tech.underoaks.coldcase.game.PlayerController;
import tech.underoaks.coldcase.state.updates.GameStateUpdateException;
import tech.underoaks.coldcase.state.InteractionChain;
import tech.underoaks.coldcase.state.updates.MoveUpdate;
import tech.underoaks.coldcase.state.Map;
import tech.underoaks.coldcase.state.tiles.Tile;
import tech.underoaks.coldcase.game.Direction;

public class Player extends TileContent {

    public static void queueMovement(Vector2 tilePosition, Direction actionDirection) {
        Vector2 newPosition = tilePosition.cpy().add(actionDirection.getVector());
        PlayerController.getInstance().setPlayerPosition(newPosition);
    }

    public static void queueRotation(Direction actionDirection) {
        PlayerController.getInstance().setPlayerDirection(actionDirection);
    }

    private static final Texture texture = new Texture("./sprites/player_detective_right.png");
    // Textures for the player facing different directions
    private static final Texture textureNorth = new Texture("./sprites/player_detective_up.png");
    private static final Texture textureSouth = new Texture("./sprites/player_detective_down.png");
    private static final Texture textureEast = new Texture("./sprites/player_detective_right.png");
    private static final Texture textureWest = new Texture("./sprites/player_detective_left.png");
    public static Texture currentTexture;

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public int playerIndex = 2;

    public Player() {
        super(texture, true, false);
        currentTexture = texture;
    }

    @Override
    public boolean action(InteractionChain chain, Interaction interaction) throws GameStateUpdateException {

        int childIndex = chain.getSnapshot().getSnapshotMap().getChildIndex(interaction.getTargetPos(), this);
        Vector2 targetPosition = switch (interaction.getActionDirection()) {
            case NORTH -> interaction.getTargetPos().cpy().add(Direction.NORTH.getVector());
            case SOUTH -> interaction.getTargetPos().cpy().add(Direction.SOUTH.getVector());
            case EAST -> interaction.getTargetPos().cpy().add(Direction.EAST.getVector());
            case WEST -> interaction.getTargetPos().cpy().add(Direction.WEST.getVector());
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
        chain.addGameStateUpdate(new MoveUpdate(interaction.getTargetPos(), childIndex, targetPosition));
        return true;
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
            batch.draw(currentTexture, x, y + 540);
        }else {
            super.render(batch, x, y);
        }

        if (tileContent != null) {
            tileContent.render(batch, x, y);
        }
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
    public static void updateTexture(Direction lookDirection) {
        Texture newTexture = switch (lookDirection) {
            case NORTH -> textureNorth;
            case SOUTH -> textureSouth;
            case EAST -> textureEast;
            case WEST -> textureWest;
        };

        if (newTexture != currentTexture) {
            currentTexture = newTexture;
        }
    }
}
