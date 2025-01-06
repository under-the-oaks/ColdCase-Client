package tech.underoaks.coldcase.state.updates;

import tech.underoaks.coldcase.game.GameController;
import tech.underoaks.coldcase.state.Map;

public class EndLevelUpdate extends GameStateUpdate{
    public EndLevelUpdate() {
        super(UpdateTypes.MAP_MODIFICATION); //is this important?

    }

    @Override
    public void apply(Map map) {
        if(!map.isSnapshotMap()){
            GameController.getInstance().endLevel();
        }
    }
}
