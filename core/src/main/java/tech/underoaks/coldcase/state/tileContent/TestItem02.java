package tech.underoaks.coldcase.state.tileContent;

import tech.underoaks.coldcase.game.TextureController;

/**
 * Test-Item
 * @implNote Only for testing
 */
public class TestItem02 extends ItemObject {
    /**
     * Default-Constructor
     */
    public TestItem02() {
        super(TextureController.getInstance().getTestItem02Texture());
    }
}
