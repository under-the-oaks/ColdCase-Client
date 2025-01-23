package tech.underoaks.coldcase.state.tileContent;

import tech.underoaks.coldcase.game.TextureController;

public class TestItem extends ItemObject {
    public TestItem() {
        super(TextureController.getInstance().getTestItemTexture());
    }
}
