package tech.underoaks.coldcase.state.tileContent;

import java.lang.reflect.InvocationTargetException;

/**
 * Enum representing different types of tile contents.
 * Used for mapping indices to tile content classes.
 */
public enum TileContents {
    /** {@link Wall} */
    WALL(1, Wall.class),
    /** {@link TestContent} */
    TEST(2, TestContent.class),
    /** {@link Player} */
    PLAYER(3, Player.class),
    /** {@link MovableBlock} */
    MOVABLE_BLOCK(4, MovableBlock.class),
    /** {@link TestItem} */
    TEST_ITEM(5, TestItem.class),
    /** {@link TestItem02} */
    TEST_ITEM02(6, TestItem02.class),
    /** {@link InvisibleWall} */
    INVISIBLE_WALL(7, InvisibleWall.class),
    /** {@link GloveItem} */
    GLOVE_ITEM(8, GloveItem.class),
    /** {@link GoalObject} */
    GOAL_OBJECT(9,GoalObject.class),
    /** {@link MovableBlockTranscendent} */
    MOVABLE_BLOCK_TRANSCENDENT(10, MovableBlockTranscendent.class),
    /** {@link TranscendentTestBlock} */
    TRANSCENDENT_TEST(11, TranscendentTestBlock.class),
    /** {@link Door} */
    DOOR(12, Door.class),
    /** {@link Door_Trigger} */
    DOOR_TRIGGER(13, Door_Trigger.class),
    /** {@link Hole} */
    HOLE(14,Hole.class),
    /** {@link PortalObject} */
    PORTAL(15,PortalObject.class);

    private final int index;
    private final Class<? extends TileContent> tileClass;

    TileContents(int index, Class<? extends TileContent> tileClass) {
        this.index = index;
        this.tileClass = tileClass;
    }

    /**
     * Gets the unique identifier of this TileContent
     * @return unique identifier of this TileContent
     */
    public int getIndex() {
        return index;
    }

    /**
     * Gets a {@link TileContent} by searching for its identifier
     * @param tileClass A {@link Class} extending {@link TileContent} that will be the search target
     * @return Index of the Class if found; -1 otherwise
     */
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
