package tech.underoaks.coldcase.game;
/**
 * Enum representing different game levels.
 * Each level has a corresponding map path that is used to load the level's map.
 *
 * @author Jean-Luc Wenserski
 */
public enum Levels {
    /** The first part of the tutorial. */
    LEVEL_05("maps/Map_Tutorial_normalBlock"),
    /** The second part of the tutorial. */
    LEVEL_06("maps/Map_Tutorial_transcendentBlock"),
    /** The third part of the tutorial. */
    LEVEL_07("maps/Map_Tutorial_spikes"),
    /** Easy main level */
    LEVEL_02("maps/Map_Mvp"),
    /** Medium main level */
    LEVEL_03("maps/New_Level_Test_Medium"),
    /** Hard main level */
    LEVEL_04("maps/New_Level_Test_Hard");

    private final String mapPath;

    Levels(String mapPath) {
        this.mapPath = mapPath;
    }

    /**
     * Gets the filepath of the selected map
     * @return The path as a String
     */
    public String getMapPath() {
        return mapPath;
    }
}
