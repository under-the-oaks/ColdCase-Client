package tech.underoaks.coldcase.state.updates;

import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.state.Map;

/**
 * A {@link GameStateUpdate} that changes the player-passability-parameter of a TileContent
 */
public class PlayerPassebilityUpdate extends GameStateUpdate{

    private final boolean passible;
    private final Vector2 targetPosition;
    private final int sourceIndex;

    /**
     * Default-Constructor
     * @param passable True if the Content should be Player-Passable; False otherwise
     * @param targetPosition The {@link Vector2}-Position of the Content that will be updated
     * @param sourceIndex The index (z-Axis) that specifies which TileContent (in a stack) should be affected.
     */
    public PlayerPassebilityUpdate(boolean passable, Vector2 targetPosition, int sourceIndex) {
        super(UpdateTypes.MAP_MODIFICATION);
        this.passible = passable;
        this.targetPosition = targetPosition;
        this.sourceIndex = sourceIndex;
    }

    @Override
    public void apply(Map map) {
        map.getTileContentByIndex(targetPosition, sourceIndex).setPlayerPassable(passible);
    }
}
