package tech.underoaks.coldcase;

import java.lang.reflect.InvocationTargetException;

public record Map(
    Tile[][] tileArray
) {
    /**
     * Generates a new map without any tiles placed
     *
     * @param height Height of the map
     * @param width  Width of the map
     * @return Empty map
     */
    public Map getEmptyMap(int height, int width) {
        return getMap(EmptyTile.class, height, width);
    }

    public Map getPlainMap(int height, int width) {
        return getMap(GroundTile.class, height, width);
    }

    /**
     * Generates a new map that is filled with a specified type of tile
     *
     * @param tile Class that will fill the map
     * @param height Height of the map
     * @param width Width of the map
     * @return New Map
     */
    private Map getMap(Class<? extends  Tile> tile, int height, int width) {
        Tile[][] tileArray = new Tile[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                try {
                    tileArray[y][x] = tile.getDeclaredConstructor().newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return new Map(tileArray);
    }
}
