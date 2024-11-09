package tech.underoaks.coldcase.data.tileContent;

import com.badlogic.gdx.graphics.Texture;

public class TestItem extends ItemObject{
    private static final Texture texture = new Texture("./isometric tileset/separated images/TEST_ITEM.png");


    public TestItem() {
        super(texture);
    }
}
