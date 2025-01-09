package tech.underoaks.coldcase.state;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.MapGenerator;
import tech.underoaks.coldcase.game.Interaction;
import tech.underoaks.coldcase.game.PlayerController;
import tech.underoaks.coldcase.state.tileContent.UpdateTileContentException;
import tech.underoaks.coldcase.state.updates.GameStateUpdateException;
import tech.underoaks.coldcase.state.tileContent.TileContent;
import tech.underoaks.coldcase.state.tiles.Tile;

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
    static float tileSize = 1080;


    private boolean isSnapshotMap = false;

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

    public int getTileArrayWidth() {
        return tileArray[0].length;
    }

    public int getTileArrayHeight() {
        return tileArray.length;
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
        return position.x < 0 || position.y < 0 || position.x >= getTileArrayWidth() || position.y >= getTileArrayHeight();
    }

    /**
     * Calls the render method of each tile of the map in the correct order and with the correct position in isometric coordinates.
     * <br><br>
     * This calls twoDToIso() to convert the 2D coordinates to isometric coordinates
     *
     * @param batch SpriteBatch to render the map
     */
    public void render(Batch batch, float originX, float originY) {
        for (int y = 0; y < tileArray.length; y++) {
            for (int x = 0; x < tileArray[y].length; x++) {
                Vector2 position = twoDToIso45(x, y);

                // calculate offset to origin for rendering -> middle of the map should be in the origin
                // Pythagoras to calculate the diagonal of the map
                float offsetY = (float) Math.sqrt(Math.pow((double) getTileArrayHeight() / 2, 2) + Math.pow((double) getTileArrayWidth() / 2, 2));
                // to account for size of the tile sprites
                offsetY = offsetY * 320;
                // to account for the height of the tiles -> the middle of the top side of the tile should be in the origin
                offsetY = offsetY - 660;
                // to account for the width of the tiles -> the middle of the tile should be in the origin, not the bottom left corner
                float halfTileSpriteWidth = 540;

                tileArray[y][x].render(batch, originX + position.x - halfTileSpriteWidth, originY + position.y + offsetY);

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

    public Vector2 twoDToIso45(int ex, int why) {

        Vector2 rotatedPt = new Vector2(0, 0);

        rotatedPt.y = ((((float) ex - (float) why) * 320) + ((float) why * 320 * 2)) * -1;
        rotatedPt.x = ((((float) why - (float) ex) * 450)) * -1;

        return rotatedPt;
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
    public void updateUntilStable(InteractionChain chain, Interaction interaction, TileContent handler) throws GameStateUpdateException, UpdateTileContentException {
        int maxIteration = 25;
        int iteration = 0;
        while (!this.updateMap(chain, interaction, handler).isEmpty()) {
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
     * @return List of {@code TileContents} that handled the update
     * @see TileContent#handleUpdate(InteractionChain, Vector2, Interaction, TileContent)
     */
    public List<TileContent> updateMap(InteractionChain chain, Interaction interaction, TileContent handler) throws GameStateUpdateException, UpdateTileContentException {
        List<TileContent> updated = new ArrayList<>();
        for (int i = 0; i < tileArray.length; i++) {
            for (int j = 0; j < tileArray[i].length; j++) {
                if (tileArray[i][j].getTileContent() == null) {
                    continue;
                }
                updated.addAll(tileArray[i][j].getTileContent().handleUpdate(chain, new Vector2(i, j), interaction, handler));
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

    /**
     * Returns whether the map is a snapshot map.
     *
     * @return true if the map is a snapshot map, false otherwise.
     */
    public boolean isSnapshotMap() {
        return isSnapshotMap;
    }

    /**
     * Sets the snapshot map status.
     *
     * @param snapshotMap true if the map is a snapshot map, false otherwise.
     */
    public void setIsSnapshotMap(boolean snapshotMap) {
        isSnapshotMap = snapshotMap;
    }

    /**
     * Checks if the player is on the specified tile position.
     *
     * @param tilePosition The position of the tile to check.
     * @return true if the player's position is the same as the tile's position, false otherwise.
     */
    public static boolean isPlayerOnTile(Vector2 tilePosition) {
        Vector2 playerPosition = PlayerController.getInstance().getPlayerPosition();
        return playerPosition.equals(tilePosition);
    }

    /**
     * Checks if the player is next to the specified tile position.
     * The player is considered next to the tile if they are adjacent in any of the four cardinal directions.
     *
     * @param tilePosition The position of the tile to check.
     * @return true if the player is adjacent to the tile (either to the left, right, above, or below), false otherwise.
     */
    public static boolean isPlayerNextToTile(Vector2 tilePosition) {
        Vector2 playerPosition = PlayerController.getInstance().getPlayerPosition();
        return playerPosition.equals(tilePosition.cpy().add(1, 0)) ||
            playerPosition.equals(tilePosition.cpy().add(0, 1)) ||
            playerPosition.equals(tilePosition.cpy().sub(1, 0)) ||
            playerPosition.equals(tilePosition.cpy().sub(0, 1));
    }

}
