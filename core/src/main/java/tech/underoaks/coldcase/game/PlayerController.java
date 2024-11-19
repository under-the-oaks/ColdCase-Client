package tech.underoaks.coldcase.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.state.tileContent.TileContent;

public class PlayerController {

    private static PlayerController instance;
    private Vector2 playerPosition;
    private Direction lookDirection;



    public TileContent getInventory() {
        return inventory;
    }

    public void setInventory(TileContent inventory) {
        this.inventory = inventory;
        //debug
        System.out.println("Inventory:"+this.inventory.toString());
    }

    private TileContent inventory;

    public static PlayerController getInstance() {
        if (instance == null) {
            instance = new PlayerController();
        }
        return instance;
    }

    public void inputUpdate() {
        // Update position based on user input
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            if (GameController.getInstance().triggerAction(playerPosition, Direction.NORTH)) {
                playerPosition.y -= 1;
            }
            lookDirection = Direction.NORTH;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            if (GameController.getInstance().triggerAction(playerPosition, Direction.SOUTH)) {
                playerPosition.y += 1;
            }
            lookDirection = Direction.SOUTH;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            if (GameController.getInstance().triggerAction(playerPosition, Direction.WEST)) {
                playerPosition.x -= 1;
            }
            lookDirection = Direction.WEST;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            if (GameController.getInstance().triggerAction(playerPosition, Direction.EAST)) {
                playerPosition.x += 1;
            }
            lookDirection = Direction.EAST;
        }
        // Interact
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            GameController.getInstance().triggerAction(playerPosition.cpy().add(lookDirection.getVector()), lookDirection);
        }

    }

    public void setPlayerPosition(Vector2 playerPosition) {
        this.playerPosition = playerPosition;
    }
}
