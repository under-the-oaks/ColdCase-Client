package tech.underoaks.coldcase.data.tileContent.trigger;

import tech.underoaks.coldcase.data.tileContent.TileContent;

public abstract class Trigger extends TileContent {
    public Trigger() {
        super();
    }

    public abstract void onTrigger();
}
