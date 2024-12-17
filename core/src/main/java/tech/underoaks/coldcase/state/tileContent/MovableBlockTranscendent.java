package tech.underoaks.coldcase.state.tileContent;

import com.badlogic.gdx.graphics.Texture;

/**
 * Transcendent Version of {@link MovableBlock} that gets synced between both clients
 */
public class MovableBlockTranscendent extends MovableBlock {
    public MovableBlockTranscendent() {
        super();

        this.visibilityState = VisibilityStates.TRANSCENDENT;
        setTexture(new Texture("sprites/block_transcendent_2.png"));
    }
}
