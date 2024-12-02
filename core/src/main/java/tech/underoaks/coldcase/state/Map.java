package tech.underoaks.coldcase.state;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.state.tileContent.UpdateTileContentException;
import tech.underoaks.coldcase.state.updates.GameStateUpdateException;
import tech.underoaks.coldcase.state.tileContent.TileContent;
import tech.underoaks.coldcase.state.tiles.Tiles;
import tech.underoaks.coldcase.state.tiles.EmptyTile;
import tech.underoaks.coldcase.state.tiles.Tile;
import tech.underoaks.coldcase.state.tileContent.TileContents;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the game map, which is a 2D array of {@link Tile} objects.
 * Provides methods for accessing and modifying the map, rendering, and updating the map state.
 */
public class Map {

    public Tile[][] tileArray;

    /**
     * The size of each tile in pixels
     */
    static float tileSize = 960;

    /**
     * Default constructor for Map needed for deserialization in {@link MapGenerator}
     */
    public Map() {
    }

    public Map(Tile[][] tileArray) {
        this.tileArray = tileArray;
    }

    public Tile getTile(int x, int y) {
        return tileArray[y][x];
    }

    public Tile getTile(Vector2 position) {
        return this.getTile((int) position.y, (int) position.x);
    }

    public Vector2 getTileContentByType(Class<? extends TileContent> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }
        if (tileArray == null || tileArray.length == 0) {
            return null;
        }
        for (int i = 0; i < tileArray.length; i++) {
            for (int j = 0; j < tileArray[i].length; j++) {
                if (type.isInstance(tileArray[i][j].getTileContent())) {
                    return new Vector2(i, j);
                }
            }
        }
        return null;
    }

    public void setTile(int x, int y, Tile tile) {
        tileArray[y][x] = tile;
    }

    public int getWidth() {
        return tileArray[0].length;
    }

    public int getHeight() {
        return tileArray.length;
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
     * @return 2D-List of integers representing the map
     */
    private static List<List<Integer>> readMapFile(Path path) {
        List<List<Integer>> rawTiles = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(path);
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

    /**
     * Determines the maximum width of the matrix (the longest row).
     *
     * @param matrix A List of Tiles representing the matrix.
     * @return The maximum width (length of the longest row).
     */
    private static int getMatrixWidth(List<List<Tile>> matrix) {
        int max = Integer.MIN_VALUE;
        for (List<Tile> row : matrix) {
            int length = row.size();
            max = Math.max(length, max);
        }
        return max;
    }

    public int getChildIndex(Vector2 tile, TileContent tileContent) {
        return tileArray[(int) tile.x][(int) tile.y].getTileContent().getChildIndex(tileContent);
    }

    public TileContent getTileContentByIndex(Vector2 position, int index) {
        if (index == -1) {
            throw new IllegalArgumentException("Index out of bounds");
        }

        TileContent tileContent = this.getTile(position).getTileContent();

        if (tileContent == null) {
            throw new IllegalArgumentException("TileContent not found");
        }

        return tileContent.getTileContentByIndex(index);
    }

    public boolean isOutOfBounds(Vector2 position) {
        return position.x < 0 || position.y < 0 || position.x >= getWidth() || position.y >= getHeight();
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
                float tempY = x * tileSize / 2 * -1;
                float tempX = y * tileSize / 2 * -1;
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

    /**
     * FIXME JavaDoc
     * Continuously updates the map until no further updates are possible.
     *
     * <p>Keeps trying to update the map with the given {@code InteractionChain} until no more changes occur.</p>
     *
     * @param chain the {@code InteractionChain} used to manage interactions and snapshots during updates
     * @throws IllegalStateException if the maximum iteration limit is exceeded, suggesting a potential cyclic dependency in {@code TileContent}.
     * @implNote This method has a limit on the number of iterations to prevent endless loops. If one {@code TileContent}
     * triggers another in a cyclic manner, the loop may otherwise never terminate.
     */
    public void updateUntilStable(InteractionChain chain) throws GameStateUpdateException, UpdateTileContentException {
        int maxIteration = 25;
        int iteration = 0;
        while (!this.updateMap(chain).isEmpty()) {
            // Keep updating until no further updates occur
            iteration++;
            if (iteration > maxIteration) {
                throw new IllegalStateException("Loop terminated due to excessive iterations; check for cyclic dependencies in TileContent.");
            }
        }
    }

    /**
     * FIXME JavaDoc
     * Updates the map by attempting to perform an update on each {@code Tile} in {@code tileArray}.
     *
     * <p>For each Tile with non-null {@code TileContent}, the {@code handleUpdate} method
     * is invoked with the given {@code InteractionChain}.
     *
     * @param chain the {@code InteractionChain} managing interactions and snapshots for updates
     * @return true if at least one {@code TileContent} performs an update; false otherwise
     * @see TileContent#handleUpdate(InteractionChain, Vector2)
     */
    public List<TileContent> updateMap(InteractionChain chain) throws GameStateUpdateException, UpdateTileContentException {
        List<TileContent> updated = new ArrayList<>();
        for (int i = 0; i < tileArray.length; i++) {
            for (int j = 0; j < tileArray[i].length; j++) {
                if (tileArray[i][j].getTileContent() == null) {
                    continue;
                }
                updated.addAll(tileArray[i][j].getTileContent().handleUpdate(
                    chain,
                    new Vector2(i, j)
                ));
            }
        }
        return updated;
    }

    /**
     * Creates a deep clone of this {@code Map} instance.
     *
     * @return A new {@code Map} instance with a deep-cloned {@code Tile} array.
     */
    public Map deepClone() {
        Tile[][] clonedTileArray = new Tile[tileArray.length][];

        for (int i = 0; i < tileArray.length; i++) {
            clonedTileArray[i] = new Tile[tileArray[i].length];
            for (int j = 0; j < tileArray[i].length; j++) {
                clonedTileArray[i][j] = tileArray[i][j].clone();
            }
        }

        return new Map(clonedTileArray);
    }

    public void dispose() {
        for (Tile[] tiles : tileArray) {
            for (Tile tile : tiles) {
                tile.dispose();
            }
        }
    }
}
