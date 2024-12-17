package tech.underoaks.coldcase;

import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import tech.underoaks.coldcase.state.Map;
import tech.underoaks.coldcase.state.tileContent.TileContent;
import tech.underoaks.coldcase.state.tiles.Tile;
import tech.underoaks.coldcase.state.updates.AddTileContentUpdate;

import static org.mockito.Mockito.*;

class AddTileContentUpdateTest {
    private Map mockMap;
    private Tile mockTile;
    private TileContent mockTileContent;
    private Vector2 mockPosition;
    private AddTileContentUpdate addTileContentUpdate;

    @BeforeEach
    void setUp() {
        // Mock dependencies
        mockMap = mock(Map.class);
        mockTile = mock(Tile.class);
        mockTileContent = mock(TileContent.class);
        mockPosition = mock(Vector2.class);

        // Setup behavior
        when(mockMap.getTile(mockPosition)).thenReturn(mockTile);

        // Create instance of AddTileContentUpdate with mocked values
        addTileContentUpdate = new AddTileContentUpdate(mockPosition, mockTileContent);
    }

    @Test
    void apply_shouldAddTileContentToTargetTile() {
        // Act
        addTileContentUpdate.apply(mockMap);

        // Assert
        verify(mockMap).getTile(mockPosition); // Verify the tile is retrieved from the map
        verify(mockTile).pushTileContent(mockTileContent); // Verify the tile content is pushed to the tile
    }
}
