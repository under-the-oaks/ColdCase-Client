package tech.underoaks.coldcase.state.tiles;

import java.lang.reflect.InvocationTargetException;

/**
 * Enum representing different types of tiles.
 * Used for mapping indices to tile classes.
 */
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

    /**
     * Creates a new instance of the tile.
     *
     * @return A new Tile instance.
     */
    public Tile getNewTile() {
        try {
            return tileClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves a new Tile instance based on the given index.
     *
     * @param index The index of the Tile.
     * @return A new Tile instance.
     * @throws IllegalArgumentException If no Tile corresponds to the given index.
     */
    public static Tile getNewTileClassByIndex(int index) {
        for (Tiles tile : Tiles.values()) {
            if (tile.getIndex() == index) {
                return tile.getNewTile();
            }
        }
        throw new IllegalArgumentException("No tile class found for index: " + index);
    }
}
