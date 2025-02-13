package tech.underoaks.coldcase.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.state.tileContent.Player;
import tech.underoaks.coldcase.state.tileContent.TileContent;

/**
 * Handles player input and movement
 */
public class PlayerController implements InputProcessor {

    private static PlayerController instance;
    private Vector2 playerPosition;
    private Direction lookDirection = Direction.EAST;

    private float keyHoldTime = 0f;
    private boolean isMoving = false;
    private int moveKeycode = -1;
    private static final float AUTO_MOVE_TIME = 0.04f;

    /**
     * Returns the player's inventory.
     *
     * @return the {@link TileContent} representing the player's inventory, or {@code null} if not set
     */
    public TileContent getInventory() {
        return inventory;
    }

    /**
     * Sets the player's inventory.
     * Additionally, prints the inventory to the console for debugging purposes if the inventory is not {@code null}.
     *
     * @param inventory the {@link TileContent} representing the new inventory
     */
    public void setInventory(TileContent inventory) {
        this.inventory = inventory;

        //debug
        if (inventory != null) {
            System.out.println("Inventory:" + this.inventory);
        }
    }

    private TileContent inventory;

    /**
     * Returns the singleton instance of {@code PlayerController}.
     * If the instance does not exist yet, it is created.
     *
     * @return the singleton {@code PlayerController} instance
     */
    public static PlayerController getInstance() {
        if (instance == null) {
            instance = new PlayerController();
        }
        return instance;
    }

    /**
     * Destroys the singleton instance of {@code PlayerController} by setting it to {@code null}.
     * This is typically used to reset the controller for a new game session.
     */
    public static void destroy() {
        instance = null;
    }

    /**
     * Sets the player's current position.
     *
     * @param playerPosition a {@link Vector2} representing the new position of the player on the map
     */
    public void setPlayerPosition(Vector2 playerPosition) {
        this.playerPosition = playerPosition;
    }

    /**
     * Retrieves the current position of the player.
     *
     * @return a {@link Vector2} representing the player's position on the map
     */
    public Vector2 getPlayerPosition() {
        return playerPosition;
    }

    /**
     * Sets the direction the player is looking towards.
     *
     * @param lookDirection the new {@code Direction} for the player
     */
    public void setPlayerDirection(Direction lookDirection) {
        this.lookDirection = lookDirection;
    }

    /**
     * Returns the current direction the player is looking towards.
     *
     * @return the {@code Direction} the player is currently facing
     */
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

    /**
     * Updates the player's movement.
     * <p>
     * If the player is moving, this method increments the key hold time based on the frame's delta time and triggers an auto-move action
     * if the hold time exceeds the defined auto-move threshold.
     * </p>
     */
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
        if (keycode == moveKeycode) {
            resetMovement();
        }
        return false;
    }

    /**
     * Resets the player's movement state, stopping any auto-move and resetting the key hold time.
     */
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
