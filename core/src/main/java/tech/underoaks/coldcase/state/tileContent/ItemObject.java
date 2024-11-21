package tech.underoaks.coldcase.state.tileContent;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.game.Direction;
import tech.underoaks.coldcase.game.PlayerController;
import tech.underoaks.coldcase.state.InteractionChain;
import tech.underoaks.coldcase.state.updates.AddTileContentUpdate;
import tech.underoaks.coldcase.state.updates.GameStateUpdateException;
import tech.underoaks.coldcase.state.updates.RemoveTileContentUpdate;

public class ItemObject extends TileContent{
    public ItemObject(Texture texture) {
        super(texture, true, true);
    }

    @Override
    public boolean action(InteractionChain chain, Vector2 tilePosition, Direction actionDirection) throws GameStateUpdateException {
        int childIndex = chain.getSnapshot().getSnapshotMap().getChildIndex(tilePosition, this);

        //no item in inventory
        if(PlayerController.getInstance().getInventory() == null){
            PlayerController.getInstance().setInventory(this);
            chain.addGameStateUpdate(new RemoveTileContentUpdate(tilePosition, childIndex));
            return true;
        }else{//item switching if item in inventory is not null
            TileContent playerInventoryItem = PlayerController.getInstance().getInventory();
            PlayerController.getInstance().setInventory(this);

            chain.addGameStateUpdate(new RemoveTileContentUpdate(tilePosition, childIndex));
            chain.addGameStateUpdate(new AddTileContentUpdate(tilePosition, playerInventoryItem));
            return true;
        }

        //return false;
    }

    @Override
    public boolean update(InteractionChain chain, Vector2 tilePosition) throws GameStateUpdateException {
        return false;
    }
}
