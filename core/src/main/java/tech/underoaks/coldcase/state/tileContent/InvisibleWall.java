package tech.underoaks.coldcase.data.tileContent;

import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.InteractionChain;
import tech.underoaks.coldcase.loader.enums.Direction;

public class InvisibleWall extends TileContent {

    public InvisibleWall() {
        super(null, false, false);
    }

    @Override
    public boolean action(InteractionChain chain, Vector2 tilePosition, Direction actionDirection) {
        return false;
    }

    @Override
    public boolean update(InteractionChain chain, Vector2 tilePosition) {
        return false;
    }
}
