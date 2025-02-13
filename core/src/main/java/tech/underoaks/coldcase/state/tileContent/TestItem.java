package tech.underoaks.coldcase.state.tileContent;

import tech.underoaks.coldcase.game.TextureController;

/**
 * Test-Item
 * @implNote For testing only
 */
public class TestItem extends ItemObject {
    /**
     * Default-Constructor
     */
    public TestItem() {
        super(TextureController.getInstance().getTestItemTexture());
    }
}
