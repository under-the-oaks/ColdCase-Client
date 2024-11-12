package tech.underoaks.coldcase.data.tileContent;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.*;
import tech.underoaks.coldcase.loader.enums.Direction;

public class ItemObject extends TileContent{
    public ItemObject(Texture texture) {
        super(texture, true, true);
    }

    @Override
    public boolean action(InteractionChain chain, Vector2 tilePosition, Direction actionDirection) throws GameStateUpdateException {
        int childIndex = chain.getSnapshot().getSnapshotMap().getChildIndex(tilePosition, this);


        if(PlayerController.getInstance().getInventory() == null){
            PlayerController.getInstance().setInventory(this);
            chain.addGameStateUpdate(new RemoveTileContentUpdate(tilePosition, childIndex));
            return true;
        }else{
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
