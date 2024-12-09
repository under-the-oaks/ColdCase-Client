package tech.underoaks.coldcase.state.tileContent;

import java.lang.reflect.InvocationTargetException;

/**
 * Enum representing different types of tile contents.
 * Used for mapping indices to tile content classes.
 */
public enum TileContents {
    WALL(1, Wall.class),
    TEST(2, TestContent.class),
    PLAYER(3, Player.class),
    MOVABLE_BLOCK(4, MovableBlock.class),
    TEST_ITEM(5, TestItem.class),
    TEST_ITEM02(6, TestItem02.class),
    INVISIBLE_WALL(7, InvisibleWall.class),
    GLOVE_ITEM(8, GloveItem.class),
    GOAL_OBJECT(9,GoalObject.class),
    MOVABLE_BLOCK_TRANSCENDENT(10, MovableBlockTranscendent.class),
    TRANSCENDENT_TEST(11, TranscendentTestBlock.class),
    DOOR(12, Door.class),
    DOOR_TRIGGER(13, Door_Trigger.class),
    PORTAL(14, PortalObject.class);

    private final int index;
    private final Class<? extends TileContent> tileClass;

    TileContents(int index, Class<? extends TileContent> tileClass) {
        this.index = index;
        this.tileClass = tileClass;
    }

    public int getIndex() {
        return index;
    }

    public static int getIndexByClass(Class<? extends TileContent> tileClass) {
        for (TileContents contents : TileContents.values()) {

            if (contents.tileClass.equals(tileClass)) {
                return contents.getIndex();
            }

        }
        return -1;
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
