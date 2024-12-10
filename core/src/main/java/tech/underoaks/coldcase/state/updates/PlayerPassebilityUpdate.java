package tech.underoaks.coldcase.state.updates;

import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.state.Map;

public class PlayerPassebilityUpdate extends GameStateUpdate{

    private final boolean passible;
    private final Vector2 targetPosition;
    private final int sourceIndex;

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
