package tech.underoaks.coldcase.game;
/**
 * Enum representing different game levels.
 * Each level has a corresponding map path that is used to load the level's map.
 *
 * @author Jean-Luc Wenserski
 */
public enum Levels {
    LEVEL_01("maps/Map_GoalDemo"),
    LEVEL_02("maps/Map_Mvp");

    private final String mapPath;
    Levels(String mapPath) {
        this.mapPath = mapPath;
    }

    public String getMapPath() {
        return mapPath;
    }

}
