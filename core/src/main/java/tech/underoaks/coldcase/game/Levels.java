package tech.underoaks.coldcase.game;

public enum Levels {
    LEVEL_01("maps/Map_GoalDemo"),
    LEVEL_02("maps/Map_Mvp");

    private String mapPath;
    Levels(String mapPath) {
        this.mapPath = mapPath;
    }

    public String getMapPath() {
        return mapPath;
    }

}
