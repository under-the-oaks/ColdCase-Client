package tech.underoaks.coldcase.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.state.tileContent.Player;
import tech.underoaks.coldcase.state.tileContent.TileContent;

/**
 * Handles player input and movement
 *
 * @author MaxBecker, Toni Bingenheimer
 * @Contributor: Jean-Luc Wenserski, Jonathan Christe
 */
public class PlayerController implements InputProcessor {

    private static PlayerController instance;
    private Vector2 playerPosition;
    private Direction lookDirection = Direction.EAST;

    private float keyHoldTime = 0f;
    private boolean isMoving = false;
    private int moveKeycode = -1;
    private static final float AUTO_MOVE_TIME = 0.04f;

    public TileContent getInventory() {
        return inventory;
    }

    public void setInventory(TileContent inventory) {
        this.inventory = inventory;

        //debug
        if (inventory != null) {
            System.out.println("Inventory:" + this.inventory);
        }
    }

    private TileContent inventory;

    public static PlayerController getInstance() {
        if (instance == null) {
            instance = new PlayerController();
        }
        return instance;
    }

    public static void destroy() {
        instance = null;
    }

    public void setPlayerPosition(Vector2 playerPosition) {
        this.playerPosition = playerPosition;
    }

    public Vector2 getPlayerPosition() {
        return playerPosition;
    }

    public void setPlayerDirection(Direction lookDirection) {
        this.lookDirection = lookDirection;
    }

    public Direction getPlayerDirection() {
        return lookDirection;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (!GameController.getInstance().isPendingGSUQueueEmpty()) {
            return false;
        }

        if (keycode == Input.Keys.W || keycode == Input.Keys.S || keycode == Input.Keys.A || keycode == Input.Keys.D) {
            switch (keycode) {
                case Input.Keys.W -> movePlayer(Direction.NORTH);
                case Input.Keys.S -> movePlayer(Direction.SOUTH);
                case Input.Keys.A -> movePlayer(Direction.WEST);
                case Input.Keys.D -> movePlayer(Direction.EAST);
            }
            System.out.println(lookDirection);
            isMoving = true;
            moveKeycode = keycode;
            return true;
        }

        // Interact
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            GameController.getInstance().triggerAction(new Interaction(playerPosition.cpy().add(lookDirection.getVector()), lookDirection, Player.class));
            return true;
        }
        return false;
    }

    public void update() {
        if (!isMoving) return;

        if (keyHoldTime < AUTO_MOVE_TIME) {
            keyHoldTime += Gdx.graphics.getDeltaTime();
        }

        if (keyHoldTime >= AUTO_MOVE_TIME) {
            movePlayer(lookDirection);
            keyHoldTime = 0f;
        }
    }

    private void movePlayer(Direction direction) {
        if (lookDirection == direction) {
            GameController.getInstance().triggerAction(new Interaction(playerPosition, direction, Player.class));
        } else {
            lookDirection = direction;
            Player.updateTexture(lookDirection);
        }
    }

    /**
     * Called when a key was released
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed
     */
    @Override
    public boolean keyUp(int keycode) {
        if ( keycode == moveKeycode) {
            resetMovement();
        }
        return false;
    }

    public void resetMovement() {
        isMoving = false;
        keyHoldTime = 0f;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
