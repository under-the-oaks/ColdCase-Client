package tech.underoaks.coldcase.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.state.tileContent.Player;
import tech.underoaks.coldcase.state.tileContent.TileContent;

public class PlayerController implements InputProcessor {

    private static PlayerController instance;
    private Vector2 playerPosition;
    private Direction lookDirection = Direction.EAST;

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

        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            if (lookDirection == Direction.NORTH) {
                if (GameController.getInstance().triggerAction(new Interaction(playerPosition, Direction.NORTH, Player.class))) {
                    return true;
                }
            } else {
                lookDirection = Direction.NORTH;
                Player.updateTexture(lookDirection);
                return true;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            if (lookDirection == Direction.SOUTH) {
                if (GameController.getInstance().triggerAction(new Interaction(playerPosition, Direction.SOUTH, Player.class))) {
                    return true;
                }
            } else {
                lookDirection = Direction.SOUTH;
                Player.updateTexture(lookDirection);
                return true;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            if (lookDirection == Direction.WEST) {
                if (GameController.getInstance().triggerAction(new Interaction(playerPosition, Direction.WEST, Player.class))) {
                    return true;
                }
            } else {
                lookDirection = Direction.WEST;
                Player.updateTexture(lookDirection);
                return true;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            if (lookDirection == Direction.EAST) {
                if (GameController.getInstance().triggerAction(new Interaction(playerPosition, Direction.EAST, Player.class))) {
                    return true;
                }
            } else {
                lookDirection = Direction.EAST;
                Player.updateTexture(lookDirection);
                return true;
            }
        }

        // Interact
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            GameController.getInstance().triggerAction(new Interaction(playerPosition.cpy().add(lookDirection.getVector()), lookDirection, Player.class));
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
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
