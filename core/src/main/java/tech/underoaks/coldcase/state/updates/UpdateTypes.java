package tech.underoaks.coldcase.state.updates;

/**
 * Enum representing the types of GameStateUpdates.
 */
public enum UpdateTypes {
    /**
     * Update involving changes to the map structure (e.g. moving an object)
     */
    MAP_MODIFICATION(true),
    /**
     * Update involving visual effects (e.g. particle effects, animations)
     */
    VISUAL_EFFECT,
    /**
     * Update involving audio effects (e.g. sound effects)
     */
    AUDIO_EFFECT;

    /**
     * Update can can trigger other updates
     */
    private final boolean hasConsequences;

    UpdateTypes(boolean hasConsequences) {
        this.hasConsequences = hasConsequences;
    }

    UpdateTypes() {
        this.hasConsequences = false;
    }

    /**
     * Checks whether this {@link UpdateTypes} has consequences or not
     * @return true if it requires consecutive update calls; false otherwise
     */
    public boolean hasConsequences() {
        return hasConsequences;
    }
}
