package tech.underoaks.coldcase.loader.enums;

import tech.underoaks.coldcase.data.tiles.EmptyTile;
import tech.underoaks.coldcase.data.tiles.GroundTile;
import tech.underoaks.coldcase.data.tiles.Tile;

import java.lang.reflect.InvocationTargetException;

public enum Tiles {
    EMPTY(0, EmptyTile.class), GROUND(1, GroundTile.class);

    private final int index;
    private final Class<? extends Tile> tileClass;

    Tiles(int index, Class<? extends Tile> tileClass) {
        this.index = index;
        this.tileClass = tileClass;
    }

    public int getIndex() {
        return index;
    }

    public Tile getNewTile() {
        try {
            return tileClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static Tile getNewTileClassByIndex(int index) {
        for (Tiles tile : Tiles.values()) {
            if (tile.getIndex() == index) {
                return tile.getNewTile();
            }
        }
        throw new IllegalArgumentException("No tile class found for index: " + index);
    }
}
