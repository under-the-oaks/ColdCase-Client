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

    private float keyHoldTime = 0f;
    private static final float AUTO_MOVE_TIME = 0.06f;
    private boolean onlyW = false;
    private boolean onlyS = false;
    private boolean onlyA = false;
    private boolean onlyD = false;

    public TileContent getInventory() {
        return inventory;
    }

    public void setInventory(TileContent inventory) {
        this.inventory = inventory;

        //debug
        if (inventory != null) {
            System.out.println("Inventory:"+this.inventory);
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
        if (!GameController.getInstance().isPendingGSUQueueEmpty() || onlyD || onlyA || onlyS || onlyW) {
            return false;
        }

        System.out.println(lookDirection);

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

    public void update() {
        if (keyHoldTime < AUTO_MOVE_TIME) {
            keyHoldTime += Gdx.graphics.getDeltaTime();
        }

        if (keyHoldTime >= AUTO_MOVE_TIME) {

            if (Gdx.input.isKeyPressed(Input.Keys.W) && !onlyD && !onlyA && !onlyS) {
                movePlayer(Direction.NORTH);
                onlyW = true;
            } else if (Gdx.input.isKeyPressed(Input.Keys.S) && !onlyD && !onlyA && !onlyW) {
                movePlayer(Direction.SOUTH);
                onlyS = true;
            } else if (Gdx.input.isKeyPressed(Input.Keys.A) && !onlyD && !onlyW && !onlyS) {
                movePlayer(Direction.WEST);
                onlyA = true;
            } else if (Gdx.input.isKeyPressed(Input.Keys.D) && !onlyW && !onlyA && !onlyS) {
                movePlayer(Direction.EAST);
                onlyD = true;
            }
            keyHoldTime = 0f;
        }
    }

    private void movePlayer(Direction direction) {
        GameController.getInstance().triggerAction(new Interaction(playerPosition, direction, Player.class));
    }

    /**
     * Called when a key was released
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed
     */
    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.W || keycode == Input.Keys.S ||
            keycode == Input.Keys.A || keycode == Input.Keys.D) {
            keyHoldTime = 0f;

            onlyD = false;
            onlyA = false;
            onlyS = false;
            onlyW = false;
        }
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
