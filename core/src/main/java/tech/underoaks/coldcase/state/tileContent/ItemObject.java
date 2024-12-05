package tech.underoaks.coldcase.state.tileContent;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.game.Interaction;
import tech.underoaks.coldcase.game.PlayerController;
import tech.underoaks.coldcase.state.InteractionChain;
import tech.underoaks.coldcase.state.updates.AddTileContentUpdate;
import tech.underoaks.coldcase.state.updates.GameStateUpdateException;
import tech.underoaks.coldcase.state.updates.RemoveTileContentUpdate;

public class ItemObject extends TileContent{

    private Texture inventoryTexture;

    public ItemObject(Texture texture) {
        super(texture, true, true);
        inventoryTexture = texture;
    }

    public Texture getInventoryTexture() {
        return inventoryTexture;
    }

    public void setInventoryTexture(Texture inventoryTexture) {
        this.inventoryTexture = inventoryTexture;
    }

    @Override
    public boolean action(InteractionChain chain, Interaction interaction) throws GameStateUpdateException {
        int childIndex = chain.getSnapshot().getSnapshotMap().getChildIndex(interaction.getTargetPos(), this);

        //no item in inventory
        if (PlayerController.getInstance().getInventory() == null) {
            PlayerController.getInstance().setInventory(this);
            chain.addGameStateUpdate(new RemoveTileContentUpdate(interaction.getTargetPos(), childIndex));
            return true;
        } else {//item switching if item in inventory is not null
            TileContent playerInventoryItem = PlayerController.getInstance().getInventory();
            PlayerController.getInstance().setInventory(this);

            chain.addGameStateUpdate(new RemoveTileContentUpdate(interaction.getTargetPos(), childIndex));
            chain.addGameStateUpdate(new AddTileContentUpdate(interaction.getTargetPos(), playerInventoryItem));
            return true;
        }

        //return false;
    }

    @Override
    public boolean update(InteractionChain chain, Vector2 tilePosition) throws GameStateUpdateException {
        return false;
    }
}
