package tech.underoaks.coldcase.loader.enums;

import tech.underoaks.coldcase.data.tileContent.TestContent;
import tech.underoaks.coldcase.data.tileContent.TileContent;
import tech.underoaks.coldcase.data.tileContent.Wall;

import java.lang.reflect.InvocationTargetException;

/** TODO @YASSINE JAVADOC */
public enum TileContents {
    WALL(1, Wall.class), TEST(2, TestContent.class);

    private final int index;
    private final Class<? extends TileContent> tileClass;

    TileContents(int index, Class<? extends TileContent> tileClass) {
        this.index = index;
        this.tileClass = tileClass;
    }

    public int getIndex() {
        return index;
    }

    public TileContent getNewTileContent() {
        try {
            return tileClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static TileContent getNewTileClassByIndex(int index) {
        for (TileContents tileContent : TileContents.values()) {
            if (tileContent.getIndex() == index) {
                return tileContent.getNewTileContent();
            }
        }
        throw new IllegalArgumentException("No tile class found for index: " + index);
    }
}
