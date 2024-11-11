package tech.underoaks.coldcase;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.data.tileContent.TileContent;
import tech.underoaks.coldcase.loader.enums.Direction;

public class PlayerController {

    private static PlayerController instance;

    private Vector2 playerPosition;

    private Direction playerHeading ;

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
                playerHeading = Direction.NORTH;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            if (GameController.getInstance().triggerAction(playerPosition, Direction.SOUTH)) {
                playerPosition.y += 1;
                playerHeading = Direction.SOUTH;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            if (GameController.getInstance().triggerAction(playerPosition, Direction.WEST)) {
                playerPosition.x -= 1;
                playerHeading = Direction.WEST;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            if (GameController.getInstance().triggerAction(playerPosition, Direction.EAST)) {
                playerPosition.x += 1;
                playerHeading = Direction.EAST;
            }
        }
        // Interact with Item
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            switch (playerHeading) {
                case NORTH:{
                    GameController.getInstance().triggerAction(playerPosition.cpy().add(0,-1), Direction.NORTH);
                    break;
                }
                case SOUTH:{
                    GameController.getInstance().triggerAction(playerPosition.cpy().add(0, 1),Direction.SOUTH);
                    break;
                }
                case EAST:{
                    GameController.getInstance().triggerAction(playerPosition.cpy().add(+1,0), Direction.EAST);
                    break;
                }
                case WEST:{
                    GameController.getInstance().triggerAction(playerPosition.cpy().add(-1, 0), Direction.WEST);
                    break;
                }

            }
        }
    }

    public void setPlayerPosition(Vector2 playerPosition) {
        this.playerPosition = playerPosition;
    }
}
