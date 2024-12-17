package tech.underoaks.coldcase.state.tileContent;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import tech.underoaks.coldcase.game.Interaction;
import tech.underoaks.coldcase.game.PlayerController;
import tech.underoaks.coldcase.game.TextureController;
import tech.underoaks.coldcase.state.InteractionChain;
import tech.underoaks.coldcase.state.updates.AddTileContentUpdate;
import tech.underoaks.coldcase.state.updates.GameStateUpdateException;
import tech.underoaks.coldcase.state.updates.RemoveTileContentUpdate;

public class GloveItem extends ItemObject {

    private Sprite sprite;

    public GloveItem() {
        super(TextureController.getInstance().getGloveTexture());
        sprite = new Sprite(TextureController.getInstance().getGloveTexture());
    }

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
    public boolean action(InteractionChain chain, Interaction interaction) throws GameStateUpdateException {

        int childIndex = chain.getSnapshot().getSnapshotMap().getChildIndex(interaction.getTargetPos(), this);

        //no item in inventory

        if(PlayerController.getInstance().getInventory() == null){
            PlayerController.getInstance().setInventory(this);
            chain.addGameStateUpdate(new RemoveTileContentUpdate(interaction.getTargetPos(), childIndex));
            return true;
        }

        //item switching if item in inventory is not null
        else {
            TileContent previousItem = PlayerController.getInstance().getInventory();
            PlayerController.getInstance().setInventory(this);

            chain.addGameStateUpdate(new RemoveTileContentUpdate(interaction.getTargetPos(), childIndex));
            chain.addGameStateUpdate(new AddTileContentUpdate(interaction.getTargetPos(), previousItem));
            return true;
        }

        //return false;
    }
}
