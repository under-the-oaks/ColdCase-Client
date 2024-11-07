package tech.underoaks.coldcase;

import tech.underoaks.coldcase.enums.UpdateTypes;

/**
 * Abstract class representing an atomic change to the game state.
 */
public abstract class GameStateUpdate {
    /**
     * The type of update to be performed.
     */
    protected UpdateTypes updateType;

    /**
     * Data required to perform the update.
     */
    protected Object updateData;

    /**
     * Applies the GameStateUpdate to the game state.
     *
     * @param controller The GameController managing the game state.
     */
    public abstract void apply(GameController controller);

    public UpdateTypes getUpdateType() {
        return updateType;
    }

    public Object getUpdateData() {
        return updateData;
    }
}
