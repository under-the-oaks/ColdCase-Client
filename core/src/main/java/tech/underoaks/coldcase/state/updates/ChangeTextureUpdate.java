package tech.underoaks.coldcase.state.updates;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.state.Map;

/**
 * A {@link GameStateUpdate} that changes the Texture of a specified {@link tech.underoaks.coldcase.state.tileContent.TileContent}
 */
public class ChangeTextureUpdate extends GameStateUpdate {

    private final Texture newTexture;
    private final Vector2 targetPosition;
    private final int sourceIndex;

    /**
     * Default-Constructor
     * @param newTexture The new Texture that will be replacing the previous one.
     * @param targetPosition The {@link Vector2} that is pointing to the desired Location.
     * @param sourceIndex The vertical (z) position of the TileContent that is being requested.s
     */
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
