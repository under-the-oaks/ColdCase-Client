package tech.underoaks.coldcase.data.tileContent.trigger;

import com.badlogic.gdx.graphics.Texture;
import tech.underoaks.coldcase.data.tileContent.TileContent;

public abstract class Trigger extends TileContent {
    public Trigger(Texture texture, boolean isPlayerPassable, boolean isObjectPassable) {
        super(texture, isPlayerPassable, isObjectPassable);
    }

    public abstract void onTrigger();
}
