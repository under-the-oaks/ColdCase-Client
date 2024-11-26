package tech.underoaks.coldcase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import tech.underoaks.coldcase.state.Map;
import tech.underoaks.coldcase.state.tileContent.TileContent;
import tech.underoaks.coldcase.state.tileContent.TileContents;
import tech.underoaks.coldcase.state.tiles.Tile;
import tech.underoaks.coldcase.state.tiles.Tiles;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MapGeneratorTest {
    Tile mockedTile = mock(Tile.class);
    TileContent mockedTileContent = mock(TileContent.class);

    @BeforeEach
    void setUp() {
        when(mockedTile.topTileContent()).thenReturn(mockedTileContent);
    }

    @Test
    void testSerializeContentToMapFromFile() {
        Path mockPath = Path.of("mock/directory");
        List<String> mockLines = Arrays.asList("1 1", // Metadata layer (map size)
                "---", "1", // Tile layer
                "---", "1",  // Tile content layer
                "---");

        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class); MockedStatic<Tiles> tilesMockedStatic = Mockito.mockStatic(Tiles.class); MockedStatic<TileContents> tileContentsMockedStatic = Mockito.mockStatic(TileContents.class)) {

            mockedFiles.when(() -> java.nio.file.Files.readAllLines(any())).thenReturn(mockLines);

            tilesMockedStatic.when(() -> Tiles.getNewTileClassByIndex(anyInt())).thenReturn(mockedTile);

            tileContentsMockedStatic.when(() -> TileContents.getNewTileClassByIndex(anyInt())).thenReturn(mockedTileContent);

            Map result = MapGenerator.serializeContentToMap(mockPath, true);

            assertNotNull(result, "Map should not be null.");
            Tile[][] tileArray = result.tileArray;
            assertEquals(1, tileArray.length, "Map width should match metadata.");
            assertEquals(1, tileArray[0].length, "Map height should match metadata.");
        }
    }

    @Test
    public void testSerializeContentToMap_null() {
        assertThrows(AssertionError.class, () -> MapGenerator.serializeContentToMap(null, true));
    }
}
