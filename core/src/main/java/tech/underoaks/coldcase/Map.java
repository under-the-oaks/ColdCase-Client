package tech.underoaks.coldcase;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.enums.TileContents;
import tech.underoaks.coldcase.enums.Tiles;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public record Map(
        Tile[][] tileArray
) {

    static float tileSize = 16;

    /**
     * Generates a new map without any tiles placed
     *
     * @param height Height of the map
     * @param width  Width of the map
     * @return Empty map
     */
    public static Map getEmptyMap(int height, int width) {
        return getGenericMap(EmptyTile.class, height, width);
    }

    /**
     * Generates a new map without any special tiles
     *
     * @param height Height of the map
     * @param width  Width of the map
     * @return Empty map
     */
    public static Map getPlainMap(int height, int width) {
        return getGenericMap(GroundTile.class, height, width);
    }

    /**
     * Generates a new map from a template file
     * Each template files should be a text file with the following format:
     * Each line represents a row of the map
     * Each number represents a tile or tile content
     * The numbers should be separated by a space
     * The numbers should be the index of the tile/tileContent in the Tiles/TileContents enum
     *
     * @param path Path to the template folder
     *             The folder should contain two files:
     *             map.tiles: The file containing the tile layout
     *             map.content: The file containing the tile content layout
     *             The files should be in the format described above
     * @return New Map
     */
    public static Map getMap(Path path) {
        List<List<Tile>> tiles = new ArrayList<>();
        Path tilePath = Path.of(path + "/map.tiles");
        Path contentPath = Path.of(path + "/map.content");
        try {
            List<List<Integer>> rawTiles = readMapFile(tilePath);

            for (int i = 0; i < rawTiles.size(); i++) {
                tiles.add(new ArrayList<>());
                for (int j = 0; j < rawTiles.get(i).size(); j++) {
                    tiles.get(i).add(Tiles.getNewTileClassByIndex(rawTiles.get(i).get(j)));
                }
            }
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }

        try {
            List<List<Integer>> rawTiles = readMapFile(contentPath);

            for (int i = 0; i < rawTiles.size(); i++) {
                for (int j = 0; j < rawTiles.get(i).size(); j++) {
                    int temp = rawTiles.get(i).get(j);
                    if (temp != 0) {
                        tiles.get(i).get(j).setTileContent(TileContents.getNewTileClassByIndex(rawTiles.get(i).get(j)));
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }


        // Convert 2D-List to 2D-Array
        Tile[][] tileArray = new Tile[tiles.size()][getMatrixWidth(tiles)];
        for (int i = 0; i < tileArray.length; i++) {
            for (int j = 0; j < tileArray[i].length; j++) {
                Tile tile;
                try {
                    tile = tiles.get(i).get(j);
                } catch (IndexOutOfBoundsException e) {
                    // If the template is not uniform,
                    // the remaining tiles will be filled up using EmptyTile
                    tile = new EmptyTile();
                }
                tileArray[i][j] = tile;
            }
        }

        return new Map(tileArray);
    }

    /**
     * Reads a map file and returns a 2D List of integers representing the map
     *
     * @param path Path to the file
     * @return 2D List of integers representing the map
     */
    private static List<List<Integer>> readMapFile(Path path) {
        List<List<Integer>> rawTiles = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(path);
            System.out.println(lines.size());
            for (int i = 0; i < lines.size(); i++) {
                String[] split = lines.get(i).split(" ");
                rawTiles.add(new ArrayList<>());
                for (String s : split) {
                    rawTiles.get(i).add(Integer.parseInt(s));
                }
            }


        } catch (IOException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }

        return rawTiles;
    }

    private static int getMatrixWidth(List<List<Tile>> matrix) {
        int max = Integer.MIN_VALUE;
        for (List<Tile> row : matrix) {
            int length = row.size();
            max = Math.max(length, max);
        }
        return max;
    }

    /**
     * Generates a new map that is filled with a specified type of tile
     *
     * @param tile   Class that will fill the map
     * @param height Height of the map
     * @param width  Width of the map
     * @return New Map
     */
    private static Map getGenericMap(Class<? extends Tile> tile, int height, int width) {
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

    /**
     * Calls the render method of each tile of the map in the correct order and with the correct position in isometric coordinates.
     * <br><br>
     * This calls twoDToIso() to convert the 2D coordinates to isometric coordinates
     *
     * @param batch SpriteBatch to render the map
     */
    public void render(SpriteBatch batch) {
        for (int y = 0; y < tileArray.length; y++) {
            for (int x = 0; x < tileArray[y].length; x++) {
                float tempX = x * tileSize * -1;
                float tempY = y * tileSize * -1;
                Vector2 tempPt = twoDToIso(new Vector2(tempX, tempY));
                tileArray[y][x].render(batch, tempPt.x, tempPt.y);
            }
        }
    }

    /**
     * Converts a point in isometric coordinates to normal 2D coordinates
     *
     * @param pt Vector2 Point in isometric Coordinates to convert
     * @return Converted point as a Vector2
     */
    public Vector2 isoTo2D(Vector2 pt) {
        Vector2 tempPt = new Vector2(0, 0);
        tempPt.x = (2 * pt.y + pt.x) / 2;
        tempPt.y = (2 * pt.y - pt.x) / 2;
        return (tempPt);
    }

    /**
     * Converts a point in normal 2D coordinates to isometric coordinates
     *
     * @param pt Vector2 Point in normal 2D coordinates to convert
     * @return Converted point as a Vector2
     */
    public Vector2 twoDToIso(Vector2 pt) {
        Vector2 tempPt = new Vector2(0, 0);
        tempPt.x = pt.x - pt.y;
        tempPt.y = (pt.x + pt.y) / 2;
        return (tempPt);
    }


}
