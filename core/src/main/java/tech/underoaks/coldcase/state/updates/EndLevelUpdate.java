package tech.underoaks.coldcase.state.updates;

import tech.underoaks.coldcase.game.LevelManager;
import tech.underoaks.coldcase.state.Map;

/**
 * A {@link GameStateUpdate} that triggers the next level to be loaded.
 */
public class EndLevelUpdate extends GameStateUpdate {
    /**
     * Default-Constructor
     */
    public EndLevelUpdate() {
        super(UpdateTypes.MAP_MODIFICATION); //is this important?
    }

    @Override
    public void apply(Map map) {
        if(!map.isSnapshotMap()){
            System.out.println("Level completed");
            LevelManager.getInstance().loadNextLevel();
        }
    }
}
