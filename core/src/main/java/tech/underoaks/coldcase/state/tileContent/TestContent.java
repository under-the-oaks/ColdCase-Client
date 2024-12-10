package tech.underoaks.coldcase.state.tileContent;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.game.Interaction;
import tech.underoaks.coldcase.state.updates.GameStateUpdateException;
import tech.underoaks.coldcase.state.InteractionChain;
import tech.underoaks.coldcase.state.updates.TestUpdate;

/**
 * Represents a test content that can be placed on a tile.
 * Used primarily for testing purposes.
 */
public class TestContent extends TileContent {
    private static final Texture texture = new Texture("./isometric tileset/separated images/tile_050.png");

    public TestContent() {
        super(texture, false, false);
    }

    @Override
    public boolean action(InteractionChain chain, Interaction interaction) throws GameStateUpdateException {
        chain.addGameStateUpdate(new TestUpdate(new Vector2(3, 4)));
        return true;
    }

    @Override
    public boolean update(InteractionChain chain, Vector2 tilePosition, Interaction interaction, TileContent handler) {
        return false;
    }
}
