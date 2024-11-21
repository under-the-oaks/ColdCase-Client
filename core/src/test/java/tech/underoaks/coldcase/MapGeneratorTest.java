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

    // wenn jemand diesen test hinbekommt hut ab, ich habs nicht geschafft
//    @Test
//    void testEmptyTilePushesInvisibleWall() {
//        Path mockPath = Path.of("mock/directory");
//        List<String> mockLines = Arrays.asList(
//                "1 1", // Metadata layer (map size)
//                "---",
//                "0", // Tile layer (0 = EmptyTile)
//                "---",
//                "0",  // Tile content layer
//                "---"
//        );
//
//        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class);
//             MockedStatic<Tiles> mockedTiles = mockStatic(Tiles.class);
//             MockedStatic<TileContents> mockedTileContents = mockStatic(TileContents.class)) {
//
//            // Mock Gdx.files
//            Gdx.files = mock(com.badlogic.gdx.Files.class);
//            FileHandle mockFileHandle = mock(FileHandle.class);
//
//            // Mock the behavior of Gdx.files.internal
//            when(Gdx.files.internal(Mockito.anyString())).thenReturn(mockFileHandle);
//
//            // Mock FileHandle path and Texture loading
//            when(mockFileHandle.path()).thenReturn("mock_texture_path");
//
//            mockedFiles.when(() -> Files.readAllLines(any())).thenReturn(mockLines);
//
//            InvisibleWall mockedInvisibleWall = mock(InvisibleWall.class);
//
//            EmptyTile mockedEmptyTile = mock(EmptyTile.class);
//            doNothing().when(mockedEmptyTile).pushTileContent(any());
//            doNothing().when(mockedEmptyTile).setTileContent(mockedInvisibleWall);
//
//            mockedTiles.when(() -> Tiles.getNewTileClassByIndex(0)).thenReturn(mockedEmptyTile);
//            mockedTileContents.when(() -> TileContents.getNewTileClassByIndex(7)).thenReturn(mockedInvisibleWall);
//
//            Map result = mapGenerator.serializeContentToMap(mockPath, true);
//
//            assertNotNull(result, "Map should not be null.");
//            System.out.println(result.tileArray[0][0]);
//            verify(mockedEmptyTile, times(1)).pushTileContent(any());
//        }
//    }

    @Test
    public void testSerializeContentToMap_null() {
        assertThrows(AssertionError.class, () -> MapGenerator.serializeContentToMap(null, true));
    }
}
