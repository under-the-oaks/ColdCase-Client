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

    public boolean hasConsequences() {
        return hasConsequences;
    }
}
