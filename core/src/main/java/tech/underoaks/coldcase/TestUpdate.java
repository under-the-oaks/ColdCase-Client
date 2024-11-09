package tech.underoaks.coldcase;

import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.data.Map;
import tech.underoaks.coldcase.data.tileContent.TestContent;
import tech.underoaks.coldcase.enums.UpdateTypes;

/**
 * A test implementation of GameStateUpdate for adding TestContent to a tile.
 */
public class TestUpdate extends GameStateUpdate {
    private final Vector2 pos;

    /**
     * Constructs a new TestUpdate.
     *
     * @param pos The position where the TestContent will be added.
     */
    public TestUpdate(Vector2 pos) {
        super(UpdateTypes.MAP_MODIFICATION);
        this.pos = pos;
    }

    @Override
    public void apply(Map map) {
        TestContent testContent = new TestContent();
        map.getTile(pos).pushTileContent(testContent);
    }
}
