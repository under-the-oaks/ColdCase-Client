package tech.underoaks.coldcase.data.tileContent;

import com.badlogic.gdx.graphics.Texture;

public abstract class ItemObject extends TileContent{
    public ItemObject(Texture texture) {
        super(texture, true, true);
    }
}
