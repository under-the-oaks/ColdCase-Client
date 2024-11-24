package tech.underoaks.coldcase.state.tileContent;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.game.Direction;
import tech.underoaks.coldcase.game.GameController;
import tech.underoaks.coldcase.game.PlayerController;
import tech.underoaks.coldcase.state.InteractionChain;

import tech.underoaks.coldcase.state.updates.GameStateUpdateException;


public class GoalObject extends TileContent {

    private static final Texture texture = new Texture("./isometric tileset/separated images/tile_010.png");

    public GoalObject(Texture texture, boolean isPlayerPassable, boolean isObjectPassable) {
        super(texture, isPlayerPassable, isObjectPassable);
    }

    public GoalObject() {
        super(texture, true, true); // Beispielwerte
    }


    @Override
    public boolean action(InteractionChain chain, Vector2 tilePosition, Direction actionDirection) throws GameStateUpdateException {

        return false; //No Action performed
    }

    @Override
    public boolean update(InteractionChain chain, Vector2 tilePosition) throws GameStateUpdateException {
        if (isPlayerOnTile(tilePosition)) { //Check if the player is on the same tile

            endLevel(); //Triggeer the end of the level

            return true; //Action was successfully

        }
        return false; //No Action performedd
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

    /**
     * Ends the level when the player reaches the goal.
     */

    private void endLevel() {

        System.out.println("Level completed!");
        GameController.getInstance().endLevel();

    }
}
