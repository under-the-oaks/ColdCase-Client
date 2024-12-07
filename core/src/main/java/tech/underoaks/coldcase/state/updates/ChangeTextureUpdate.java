package tech.underoaks.coldcase.state.updates;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.state.Map;

public class ChangeTextureUpdate extends GameStateUpdate {

    private final Texture newTexture;
    private final Vector2 targetPosition;
    private final int sourceIndex;

    public ChangeTextureUpdate(Texture newTexture, Vector2 targetPosition, int sourceIndex) {
        super(UpdateTypes.VISUAL_EFFECT);
        this.newTexture = newTexture;
        this.targetPosition = targetPosition;
        this.sourceIndex = sourceIndex;
    }

    @Override
    public void apply(Map map) {
        map.getTileContentByIndex(targetPosition, sourceIndex).setTexture(newTexture);
    }
}
