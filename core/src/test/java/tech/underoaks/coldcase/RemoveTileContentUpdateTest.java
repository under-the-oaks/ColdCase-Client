package tech.underoaks.coldcase;

import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.underoaks.coldcase.state.Map;
import tech.underoaks.coldcase.state.tileContent.TileContent;
import tech.underoaks.coldcase.state.tiles.Tile;
import tech.underoaks.coldcase.state.updates.RemoveTileContentUpdate;

import static org.mockito.Mockito.*;

public class RemoveTileContentUpdateTest {
    private Map mockMap;
    private Tile mockTile;
    private TileContent mockTargetContent;
    private TileContent mockOtherContent;
    private Vector2 mockPosition;
    private RemoveTileContentUpdate removeTileContentUpdate;

    @BeforeEach
    void setUp() {
        // Mock dependencies
        mockMap = mock(Map.class);
        mockTile = mock(Tile.class);
        mockTargetContent = mock(TileContent.class);
        mockOtherContent = mock(TileContent.class);
        mockPosition = mock(Vector2.class);
        removeTileContentUpdate = new RemoveTileContentUpdate(mockPosition, 0);

        // Mock behaviors
        when(mockMap.getTile(mockPosition)).thenReturn(mockTile);
        when(mockMap.getTileContentByIndex(mockPosition, 0)).thenReturn(mockTargetContent);


    }

    @Test
    void apply_shouldRemoveTileContentFromTargetTile() {
        removeTileContentUpdate.apply(mockMap);

        verify(mockMap).getTileContentByIndex(mockPosition, 0);
        verify(mockMap).getTile(mockPosition);
        verify(mockTile, atLeastOnce()).popTileContent();
    }

    @Test
    void apply_shouldNotDeleteOtherTileContent() {
        // Mock the structure of TileContent (a chain of parent -> child)
        TileContent otherContent1 = mock(TileContent.class);
        TileContent otherContent2 = mock(TileContent.class);

        // Simulate the stack-like behavior for popTileContent
        when(mockTile.popTileContent())
            .thenReturn(otherContent1)  // First pop returns otherContent1
            .thenReturn(mockTargetContent)  // Second pop returns the target
            .thenReturn(null);  // Third pop indicates the stack is empty

        // Simulate pushing content back onto the tile
        doNothing().when(mockTile).pushTileContent(any(TileContent.class));

        // Act
        removeTileContentUpdate.apply(mockMap);

        // Assert
        // Verify the popTileContent behavior
        verify(mockTile, times(3)).popTileContent();

        // Ensure the correct order of pushTileContent
        verify(mockTile).pushTileContent(otherContent1);
        verify(mockTile, never()).pushTileContent(mockTargetContent);  // Target should not be re-added
        verify(mockTile, never()).pushTileContent(otherContent2);  // Content not popped should not be re-added
    }

}
