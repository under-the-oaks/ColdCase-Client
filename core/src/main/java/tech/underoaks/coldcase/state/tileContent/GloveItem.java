package tech.underoaks.coldcase.state.tileContent;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.game.Direction;
import tech.underoaks.coldcase.game.PlayerController;
import tech.underoaks.coldcase.state.InteractionChain;
import tech.underoaks.coldcase.state.updates.AddTileContentUpdate;
import tech.underoaks.coldcase.state.updates.GameStateUpdateException;
import tech.underoaks.coldcase.state.updates.RemoveTileContentUpdate;

public class GloveItem extends ItemObject {

    private static final Texture texture = new Texture("./sprites/item_glove_detective_6.png");

    private static final Sprite sprite = new Sprite(texture);

    public GloveItem() { super(texture); }

    @Override
    public void render(SpriteBatch batch, float x, float y) {

        if (sprite != null) {
            sprite.setPosition(x, y);
            batch.draw(sprite, x, y + 480, sprite.getWidth(), sprite.getHeight());
        }

        if (tileContent != null) {
            tileContent.render(batch, x, y);
        }
    }

    @Override
    public boolean action(InteractionChain chain, Vector2 tilePosition, Direction actionDirection) throws GameStateUpdateException {

        int childIndex = chain.getSnapshot().getSnapshotMap().getChildIndex(tilePosition, this);

        //no item in inventory

        if(PlayerController.getInstance().getInventory() == null){
            PlayerController.getInstance().setInventory(this);
            chain.addGameStateUpdate(new RemoveTileContentUpdate(tilePosition, childIndex));
            return true;
        }

        //item switching if item in inventory is not null
        else {
            TileContent previousItem = PlayerController.getInstance().getInventory();
            PlayerController.getInstance().setInventory(this);

            chain.addGameStateUpdate(new RemoveTileContentUpdate(tilePosition, childIndex));
            chain.addGameStateUpdate(new AddTileContentUpdate(tilePosition, previousItem));
            return true;
        }

        //return false;
    }
}
