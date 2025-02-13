package tech.underoaks.coldcase.state.tileContent;

import tech.underoaks.coldcase.game.TextureController;

/**
 * Transcendent Version of {@link MovableBlock} that gets synced between both clients
 */
public class MovableBlockTranscendent extends MovableBlock {
    /**
     * Default-Constructor
     */
    public MovableBlockTranscendent() {
        super();

        this.visibilityState = VisibilityStates.TRANSCENDENT;
        setTexture(TextureController.getInstance().getMovableBlockTranscendantTexture());
    }
}
