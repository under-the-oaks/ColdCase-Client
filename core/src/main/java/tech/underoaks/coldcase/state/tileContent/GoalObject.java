package tech.underoaks.coldcase.state.tileContent;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.game.*;
import tech.underoaks.coldcase.state.InteractionChain;

import tech.underoaks.coldcase.state.updates.EndLevelUpdate;
import tech.underoaks.coldcase.state.updates.GameStateUpdateException;


public class GoalObject extends TileContent {

    private static final Texture texture = TextureController.getInstance().getGoalObjectTexture();

    private static final Sprite sprite = new Sprite(texture);

    //public GoalObject(Texture texture, boolean isPlayerPassable, boolean isObjectPassable) {
    //    super(texture, isPlayerPassable, isObjectPassable);
    //}

    public GoalObject() {
        super(texture, true, true);
        this.visibilityState = VisibilityStates.TRANSCENDENT;
    }

    @Override
    public void render(SpriteBatch batch, float x, float y) {

        if (sprite != null) {
            sprite.setPosition(x, y);
            batch.draw(sprite, x, y + 480, sprite.getWidth(), sprite.getHeight());
        }

        if (tileContent != null) {
            tileContent.render(batch, x, y);
        }
    }

    @Override
    public boolean action(InteractionChain chain, Interaction interaction) throws GameStateUpdateException {
        //System.out.println("TEST")test;
        if (isPlayerOnTile(interaction.getTargetPos()) || isPlayerNextToTile(interaction.getTargetPos())){

            chain.addGameStateUpdate(new EndLevelUpdate());
            return true; //Action was successfully
        }
        return false;
    }

    @Override
    public boolean update(InteractionChain chain, Vector2 tilePosition, Interaction interaction, TileContent handler) throws GameStateUpdateException {

        if (isPlayerOnTile(tilePosition)) { //Check if the player is on the same tile
            //GameController.getInstance().triggerAction(new Interaction(tilePosition, Direction.NORTH, GoalObject.class));
            //chain.addGameStateUpdate(new EndLevelUpdate());
            //return true;
        }
        return false; //No Action performed
    }

    /**
     * Checks if the player is on the goal tile.
     *
     * @param tilePosition The position of the current tile.
     * @return true if the player is on the same tile.
     */

    private boolean isPlayerOnTile(Vector2 tilePosition) {
        Vector2 playerPosition = PlayerController.getInstance().getPlayerPosition();
        return playerPosition.equals(tilePosition);

    }

    private boolean isPlayerNextToTile(Vector2 tilePosition) {
        Vector2 playerPosition = PlayerController.getInstance().getPlayerPosition();
        return playerPosition.equals(tilePosition.cpy().add(1, 0)) ||
            playerPosition.equals(tilePosition.cpy().add(0, 1)) ||
            playerPosition.equals(tilePosition.cpy().sub(1, 0)) ||
            playerPosition.equals(tilePosition.cpy().sub(0, 1));
    }

}
