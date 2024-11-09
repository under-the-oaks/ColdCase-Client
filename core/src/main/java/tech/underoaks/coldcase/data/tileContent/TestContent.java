package tech.underoaks.coldcase.data.tileContent;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.GameStateUpdateException;
import tech.underoaks.coldcase.InteractionChain;
import tech.underoaks.coldcase.TestUpdate;

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
    public boolean action(InteractionChain chain, Vector2 position) throws GameStateUpdateException {
        chain.addGameStateUpdate(new TestUpdate(new Vector2(3, 4)));
        return true;
    }

    @Override
    public boolean update(InteractionChain chain) {
        return false;
    }
}
