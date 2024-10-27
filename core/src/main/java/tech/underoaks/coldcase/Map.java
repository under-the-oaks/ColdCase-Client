package tech.underoaks.coldcase;

public record Map(
    Tile[][] tileArray
) {
    /**
     * Generates a new map without any tiles placed
     *
     * @param height Height of the map
     * @param width Width of the map
     * @return Empty map
     */
    public Map getEmptyMap(int height, int width) {
        Tile[][] tileArray = new Tile[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tileArray[y][x] = new EmptyTile();
            }
        }
        return new Map(tileArray);
    }
}
