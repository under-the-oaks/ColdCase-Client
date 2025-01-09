package tech.underoaks.coldcase.state.tileContent;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.game.Interaction;
import tech.underoaks.coldcase.game.PlayerController;
import tech.underoaks.coldcase.game.TextureController;
import tech.underoaks.coldcase.state.updates.GameStateUpdateException;
import tech.underoaks.coldcase.state.InteractionChain;
import tech.underoaks.coldcase.state.updates.MoveUpdate;
import tech.underoaks.coldcase.state.Map;
import tech.underoaks.coldcase.state.tiles.Tile;
import tech.underoaks.coldcase.game.Direction;

public class Player extends TileContent {

    public static Texture currentTexture;

    public Player() {
        super(TextureController.getInstance().getPlayerTexture(), true, false);
        currentTexture = TextureController.getInstance().getPlayerTexture();
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
        updateMovement( PlayerController.getInstance().getPlayerPosition(), PlayerController.getInstance().getPlayerDirection() );

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
    public void render(Batch batch, float x, float y) {

        if (currentTexture != null) {
            batch.draw(currentTexture, x, y + 540);
        }else {
            super.render(batch, x, y);
        }

        if (tileContent != null) {
            tileContent.render(batch, x, y);
        }

        System.out.println("Player position: " + x + ", " + y);

    }

    @Override
    public boolean update(InteractionChain chain, Vector2 tilePosition, Interaction interaction, TileContent handler) throws GameStateUpdateException {
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
            case NORTH -> TextureController.getInstance().getPlayerTextureNorth();
            case SOUTH -> TextureController.getInstance().getPlayerTextureSouth();
            case EAST -> TextureController.getInstance().getPlayerTextureEast();
            case WEST -> TextureController.getInstance().getPlayerTextureWest();
        };

        if (newTexture != currentTexture) {

            currentTexture = newTexture;

        }
    }


    public void updateMovement(Vector2 tilePosition, Direction actionDirection) {

        Vector2 newPosition = tilePosition.cpy().add(actionDirection.getVector());
        PlayerController.getInstance().setPlayerPosition(newPosition);
        PlayerController.getInstance().setPlayerDirection(actionDirection);

    }

}
