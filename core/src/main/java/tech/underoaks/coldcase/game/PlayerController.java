package tech.underoaks.coldcase.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.state.tileContent.TileContent;

public class PlayerController implements InputProcessor {

    private static PlayerController instance;
    private Vector2 playerPosition;
    private Direction lookDirection = Direction.NORTH;

    public TileContent getInventory() {
        return inventory;
    }

    public void setInventory(TileContent inventory) {
        this.inventory = inventory;

        //debug
        if (inventory != null) {
            System.out.println("Inventory:"+this.inventory.toString());
        }
    }

    private TileContent inventory;

    public static PlayerController getInstance() {
        if (instance == null) {
            instance = new PlayerController();
        }
        return instance;
    }

    public void setPlayerPosition(Vector2 playerPosition) {
        this.playerPosition = playerPosition;
    }

    /**
     * Called when a key was pressed
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed
     */
    @Override
    public boolean keyDown(int keycode) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            lookDirection = Direction.NORTH;
            if (GameController.getInstance().triggerAction(playerPosition, Direction.NORTH)) {
                playerPosition.add(Direction.NORTH.getVector());
                return true;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            lookDirection = Direction.SOUTH;
            if (GameController.getInstance().triggerAction(playerPosition, Direction.SOUTH)) {
                playerPosition.add(Direction.SOUTH.getVector());
                return true;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            lookDirection = Direction.WEST;
            if (GameController.getInstance().triggerAction(playerPosition, Direction.WEST)) {
                playerPosition.add(Direction.WEST.getVector());
                return true;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            lookDirection = Direction.EAST;
            if (GameController.getInstance().triggerAction(playerPosition, Direction.EAST)) {
                playerPosition.add(Direction.EAST.getVector());
                return true;
            }
        }
        // Interact
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            GameController.getInstance().triggerAction(playerPosition.cpy().add(lookDirection.getVector()), lookDirection);
            return true;
        }
        return false;
    }

    /**
     * Called when a key was released
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed
     */
    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    /**
     * Called when a key was typed
     *
     * @param character The character
     * @return whether the input was processed
     */
    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    /**
     * Called when the screen was touched or a mouse button was pressed. The button parameter will be {@link Input.Buttons#LEFT} on iOS.
     *
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @param button  the button
     * @return whether the input was processed
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    /**
     * Called when a finger was lifted or a mouse button was released. The button parameter will be {@link Input.Buttons#LEFT} on iOS.
     *
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event.
     * @param button  the button
     * @return whether the input was processed
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    /**
     * Called when the touch gesture is cancelled. Reason may be from OS interruption to touch becoming a large surface such as
     * the user cheek). Relevant on Android and iOS only. The button parameter will be {@link Input.Buttons#LEFT} on iOS.
     *
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event.
     * @param button  the button
     * @return whether the input was processed
     */
    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    /**
     * Called when a finger or the mouse was dragged.
     *
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event.
     * @return whether the input was processed
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    /**
     * Called when the mouse was moved without any buttons being pressed. Will not be called on iOS.
     *
     * @param screenX
     * @param screenY
     * @return whether the input was processed
     */
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    /**
     * Called when the mouse wheel was scrolled. Will not be called on iOS.
     *
     * @param amountX the horizontal scroll amount, negative or positive depending on the direction the wheel was scrolled.
     * @param amountY the vertical scroll amount, negative or positive depending on the direction the wheel was scrolled.
     * @return whether the input was processed.
     */
    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
