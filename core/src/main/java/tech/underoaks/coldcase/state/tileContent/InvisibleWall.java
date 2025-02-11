package tech.underoaks.coldcase.state.tileContent;

import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.game.Interaction;
import tech.underoaks.coldcase.state.InteractionChain;
import tech.underoaks.coldcase.state.updates.GameStateUpdateException;

/**
 * A tile content that represents an invisible wall. This is added to empty tiles automatically when the map is generated,
 * so that the player cannot become jesus and walk on water/emptyTiles.
 */
public class InvisibleWall extends TileContent {
    /**
     * Default-Constructor
     */
    public InvisibleWall() {
        super(null, false, false);
    }

    @Override
    public boolean action(InteractionChain chain, Interaction interaction) throws GameStateUpdateException {
        return false;
    }

    @Override
    public boolean update(InteractionChain chain, Vector2 tilePosition, Interaction interaction, TileContent handler) {
        return false;
    }
}
