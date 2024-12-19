package tech.underoaks.coldcase.stages.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import tech.underoaks.coldcase.game.PlayerController;
import tech.underoaks.coldcase.state.tileContent.ItemObject;

public class InventoryActor extends Actor {
    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(new Texture("./isometric tileset/separated images/TEST_INVENTORY.png"), getX(), getY(), getWidth(), getHeight());

        if (PlayerController.getInstance().getInventory() != null) {
            ItemObject item = (ItemObject) PlayerController.getInstance().getInventory();
            Texture uiTexture = item.getInventoryTexture();
            if (uiTexture != null) {
                batch.draw(uiTexture, getX(), getY(), getWidth(), getHeight());
            }
        }
    }

//    void renderInventory(float inventoryDimension, Vector2 inventoryOffset) {
//
//        getBatch().draw(new Texture("./isometric tileset/separated images/TEST_INVENTORY.png"), -5000 + inventoryOffset.x + (inventoryDimension / 2), -5000 + inventoryOffset.y + (inventoryDimension / 2), inventoryDimension, inventoryDimension);
//
//        if (PlayerController.getInstance().getInventory() != null) {
//
//            ItemObject item = (ItemObject) PlayerController.getInstance().getInventory();
//
//            Texture uiTexture = item.getInventoryTexture();
//
//            if (uiTexture != null) {
//
//                getBatch().draw(uiTexture, -5000 + inventoryOffset.x + (inventoryDimension / 2), -5000 + inventoryOffset.y + (inventoryDimension / 2), inventoryDimension, inventoryDimension);
//
//            }
//        }
//    }

}
