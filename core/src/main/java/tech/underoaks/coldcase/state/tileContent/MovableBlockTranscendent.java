package tech.underoaks.coldcase.state.tileContent;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import tech.underoaks.coldcase.game.Interaction;
import tech.underoaks.coldcase.state.InteractionChain;
import tech.underoaks.coldcase.state.Map;
import tech.underoaks.coldcase.state.tiles.Tile;
import tech.underoaks.coldcase.state.updates.GameStateUpdateException;
import tech.underoaks.coldcase.state.updates.MoveUpdate;

public class MovableBlockTranscendent extends MovableBlock {
    public MovableBlockTranscendent() {
        super();

        this.visibilityState = VisibilityStates.TRANSCENDENT;
        setTexture(new Texture("sprites/block_transcendent_2.png"));
    }
}
