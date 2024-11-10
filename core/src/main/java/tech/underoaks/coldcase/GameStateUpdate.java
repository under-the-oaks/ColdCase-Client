package tech.underoaks.coldcase;

import tech.underoaks.coldcase.data.Map;
import tech.underoaks.coldcase.enums.UpdateTypes;

/**
 * Abstract class representing an atomic change to the game state.
 */
public abstract class GameStateUpdate {
    /**
     * The type of update to be performed.
     */
    public final UpdateTypes UPDATE_TYPE;

    protected GameStateUpdate(UpdateTypes updateType) {
        this.UPDATE_TYPE = updateType;
    }

    /**
     * Applies the GameStateUpdate to the game state.
     *
     * @param map the map that will receive changes.
     */
    public abstract void apply(Map map);
}
