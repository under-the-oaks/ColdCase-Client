package tech.underoaks.coldcase.state.tileContent;

import com.badlogic.gdx.graphics.Texture;

public enum UITextures {

    TEST_ITEM(0, new Texture("./isometric tileset/separated images/TEST_ITEM.png")),
    TEST_ITEM_02(1, new Texture("./isometric tileset/separated images/TEST_ITEM02.png"));

    int index;
    Texture texture;

    UITextures(int index, Texture texture) {
        this.index = index;
        this.texture = texture;
    }

    public static UITextures getUITexture(TileContent item) {
        return switch (item) {
            case TestItem testItem -> UITextures.TEST_ITEM;
            case TestItem02 testItem02 -> UITextures.TEST_ITEM_02;
            default -> null;
        };
    }



    public Texture getTexture() {
        return texture;
    }
}
