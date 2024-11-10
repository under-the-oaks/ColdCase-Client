package tech.underoaks.coldcase;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.loader.enums.Direction;

public class PlayerController {

    private static PlayerController instance;

    private Vector2 playerPosition;

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
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            if (GameController.getInstance().triggerAction(playerPosition, Direction.SOUTH)) {
                playerPosition.y += 1;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            if (GameController.getInstance().triggerAction(playerPosition, Direction.WEST)) {
                playerPosition.x -= 1;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            if (GameController.getInstance().triggerAction(playerPosition, Direction.EAST)) {
                playerPosition.x += 1;
            }
        }
    }

    public void setPlayerPosition(Vector2 playerPosition) {
        this.playerPosition = playerPosition;
    }
}