package tech.underoaks.coldcase;

import tech.underoaks.coldcase.data.Map;
import tech.underoaks.coldcase.data.tileContent.TestContent;
import tech.underoaks.coldcase.data.tileContent.TileContent;

public class TestUpdate extends GameStateUpdate {
    @Override
    public void apply(Map map) {
        System.out.println("apply");
        // TestContent testContent = new TestContent();
        // map.getTile(4,4).pushTileContent(testContent);
    }
}
