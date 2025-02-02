package tech.underoaks.coldcase.game;
/**
 * Enum representing different game levels.
 * Each level has a corresponding map path that is used to load the level's map.
 *
 * @author Jean-Luc Wenserski
 */
public enum Levels {
    //LEVEL_TEST("maps/test_plain"),
//    LEVEL_01("maps/Map_GoalDemo"),
    LEVEL_05("maps/Map_Tutorial_normalBlock"),
    LEVEL_06("maps/Map_Tutorial_transcendentBlock"),
    LEVEL_07("maps/Map_Tutorial_spikes"),
    LEVEL_02("maps/Map_Mvp"),
    LEVEL_03("maps/New_Level_Test_Medium"),
    LEVEL_04("maps/New_Level_Test_Hard");

    private final String mapPath;
    Levels(String mapPath) {
        this.mapPath = mapPath;
    }

    public String getMapPath() {
        return mapPath;
    }

}
