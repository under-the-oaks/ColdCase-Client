package tech.underoaks.coldcase;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import tech.underoaks.coldcase.game.TextureController;
import tech.underoaks.coldcase.game.TextureFactory;
import tech.underoaks.coldcase.state.Map;
import tech.underoaks.coldcase.state.tileContent.InvisibleWall;
import tech.underoaks.coldcase.state.tileContent.TileContent;
import tech.underoaks.coldcase.state.tileContent.TileContents;
import tech.underoaks.coldcase.state.tiles.EmptyTile;
import tech.underoaks.coldcase.state.tiles.Tile;
import tech.underoaks.coldcase.state.tiles.Tiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for generating maps from text files.
 * <p>
 * It provides methods for serializing and deserializing maps to and from JSON.
 * The JSON format used is the one provided by the {@link Json} class provided by libGDX.
 */
public final class MapGenerator {

    static Json json = new Json();

    /**
     * Serializes the map from text files to JSON format.
     * <p>
     * The map is read from the text files located at the given path.
     *
     * @param path        The path to the directory containing the map text files.
     *                    The map files should be named "map.detective" and "map.ghost".
     * @param isDetective Whether the map is for the detective or the ghost.
     *                    If true, the detective map is serialized. Otherwise, the ghost map is serialized.
     * @return A {@link Map} Object.
     */
    public static Map serializeContentToMap(Path path, boolean isDetective) {
        if(!TextureController.exists()) {
            TextureController.create(isDetective, new TextureFactory());
        }

        Path tilePath = Path.of(path + (isDetective ? "/map.detective" : "/map.ghost"));
        List<String> lines = null;
        // Read the file into a list of strings
        try {
            lines = Files.readAllLines(tilePath);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        assert lines != null;

        // Split the lines into layers
        // Each layer is separated by a line containing only "---"
        // layer 0 is the metadata layer containing the x and y size of the map,
        // layer 1 is the tile layer,
        // layer 2 and all following layers are the tile content layers
        List<List<String>> mapLayers = new ArrayList<>();

        // The size of the current partition, used to count the number of lines in each partition
        int partitionSize = 0;

        // Split the lines into layers using the "---" separator and partitionSize variable
        for (int i = 0; i < lines.size(); i++) {
            if (!lines.get(i).contains("---")) {
                assert lines.get(i).matches("^(\\d+ ?)*$") : "Invalid map file format";
                partitionSize++;
            } else {
                mapLayers.add(new ArrayList<>(lines.subList(i - partitionSize, i)));
                partitionSize = 0;
            }
        }

        // extract the metadata layer
        String[] rawMapSize = mapLayers.getFirst().getFirst().split(" ");
        Vector2 mapSize = new Vector2(Integer.parseInt(rawMapSize[0]), Integer.parseInt(rawMapSize[1]));

        // extract the tile layer
        List<String> tiles = mapLayers.get(1);
        Tile[][] tileArray = new Tile[(int) mapSize.x][(int) mapSize.y];

        // create the tile Objects and insert into tileArray
        for (int i = 0; i < mapSize.x; i++) {
            // if the map size x is larger than the actual number of lines,
            // reduce the map size - no need to create a whole row of empty tiles
            try {
                if (tiles.get(i) == null) {
                    mapSize.x--;
                    continue;
                }
            } catch (IndexOutOfBoundsException e) {
                tiles.add("0");
            }
            String[] tileRow = tiles.get(i).split(" ");
            for (int j = 0; j < mapSize.y; j++) {
                // if the map size y is larger than the actual number of tiles in the row,
                // fill the rest of the row with empty tiles
                try {
                    tileArray[i][j] = Tiles.getNewTileClassByIndex(Integer.parseInt(tileRow[j]));
                } catch (IndexOutOfBoundsException e) {
                    tileArray[i][j] = Tiles.getNewTileClassByIndex(0);
                }
            }
        }

        // extract the tile content layers
        for (int i = 2; i < mapLayers.size(); i++) {
            List<String> tileContents = mapLayers.get(i);
            for (int j = 0; j < mapSize.x; j++) {
                String[] tileContentRow = new String[0];
                try {
                    tileContentRow = tileContents.get(j).split(" ");
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Index out of bounds: " + e.getMessage());
                }
                for (int k = 0; k < mapSize.y; k++) {
                    // if the Tile is an instance of EmptyTile, push an Invisible Wall to it so the player cant walk on it
                    if (tileArray[j][k] instanceof EmptyTile) {
                        tileArray[j][k].pushTileContent(new InvisibleWall());
                        continue;
                    }

                    // if the tileContentRow[k] is empty, skip it
                    // -> gets caused by the metadata size being larger than the actual map size
                    // this interaction between the metadata and the actual map size is intended
                    try {
                        TileContent newTileContent = TileContents.getNewTileClassByIndex(Integer.parseInt(tileContentRow[k]));
                        tileArray[j][k].pushTileContent(newTileContent);
                    } catch (IndexOutOfBoundsException ignored) {
                    }

                }
            }
        }

        return new Map(tileArray);
    }

    /**
     * Serializes a {@link Map} object to JSON format. This is planed to be used for saving the Map to a file.
     * Note that this does not include the PlayerController or the GameController which are not part of the Map.
     *
     * @param map The {@link Map} to serialize.
     */
    public static String serializeMapToJson(Map map) {
        return json.toJson(map);
    }

    /**
     * Deserializes a {@link Map} object from JSON format.
     *
     * @param jsonMap The JSON string to deserialize.
     * @return The deserialized {@link Map} object.
     */
    public static Map deserializeMap(String jsonMap) {
        return json.fromJson(Map.class, jsonMap);
    }

}
