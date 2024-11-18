package tech.underoaks.coldcase.loader.enums;

import tech.underoaks.coldcase.data.tileContent.*;

import java.lang.reflect.InvocationTargetException;

/**
 * Enum representing different types of tile contents.
 * Used for mapping indices to tile content classes.
 */
public enum TileContents {
    WALL(1, Wall.class),
    TEST(2, TestContent.class),
    PLAYER(3, Player.class),
    MOVABLE_BLOCK(4, movableBlock.class),
    TEST_ITEM(5,TestItem.class),
    TEST_ITEM02(6,TestItem02.class),
    INVISIBLE_WALL(7, InvisibleWall.class);

    private final int index;
    private final Class<? extends TileContent> tileClass;

    TileContents(int index, Class<? extends TileContent> tileClass) {
        this.index = index;
        this.tileClass = tileClass;
    }

    public int getIndex() {
        return index;
    }

    /**
     * Creates a new instance of the tile content.
     *
     * @return A new TileContent instance.
     */
    public TileContent getNewTileContent() {
        try {
            return tileClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves a new TileContent instance based on the given index.
     *
     * @param index The index of the TileContent.
     * @return A new TileContent instance.
     * @throws IllegalArgumentException If no TileContent corresponds to the given index.
     */
    public static TileContent getNewTileClassByIndex(int index) {
        if (index == 0) {
            return null;
        }

        for (TileContents tileContent : TileContents.values()) {
            if (tileContent.getIndex() == index) {
                return tileContent.getNewTileContent();
            }
        }
        throw new IllegalArgumentException("No tile class found for index: " + index);
    }
}
