package tech.underoaks.coldcase;

import static org.mockito.Mockito.*;


import com.badlogic.gdx.math.Vector2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.underoaks.coldcase.state.Map;
import tech.underoaks.coldcase.state.tileContent.TileContent;
import tech.underoaks.coldcase.state.tiles.Tile;
import tech.underoaks.coldcase.state.updates.MoveUpdate;

import java.util.Stack;

public class MoveUpdateTest {

    private Map mockMap;
    private Tile mockSourceTile;
    private Tile mockTargetTile;
    private TileContent mockSourceContent;
    private TileContent mockPoppedContent;
    private Vector2 sourcePosition;
    private Vector2 targetPosition;
    private int sourceIndex;

    @BeforeEach
    public void setUp() {
        // Mocking Map and Tiles
        mockMap = mock(Map.class);
        mockSourceTile = mock(Tile.class);
        mockTargetTile = mock(Tile.class);
        mockSourceContent = mock(TileContent.class);
        mockPoppedContent = mock(TileContent.class);

        sourcePosition = new Vector2(1, 1);
        targetPosition = new Vector2(2, 2);
        sourceIndex = 0;

        // Mocking the Map behavior
        when(mockMap.getTile(sourcePosition)).thenReturn(mockSourceTile);
        when(mockMap.getTile(targetPosition)).thenReturn(mockTargetTile);
        when(mockMap.getTileContentByIndex(sourcePosition, sourceIndex)).thenReturn(mockSourceContent);

        // Simulate popping content from the source tile
        when(mockSourceTile.popTileContent()).thenReturn(mockPoppedContent).thenReturn(null);
    }

    @Test
    public void testApplyMoveUpdate() {
        // Create MoveUpdate instance
        MoveUpdate moveUpdate = new MoveUpdate(sourcePosition, sourceIndex, targetPosition);

        // Apply the move update to the mocked map
        moveUpdate.apply(mockMap);

        // Verify that the correct methods are called
        verify(mockSourceTile, times(1)).popTileContent(); // pop content from source tile
        verify(mockTargetTile, times(1)).pushTileContent(mockSourceContent); // push content to target tile
        verify(mockSourceTile, times(1)).pushTileContent(mockPoppedContent); // push popped content back to source tile
    }

    @Test
    public void testMoveUpdateWithEmptySourceTile() {
        // Simulate that the source tile is empty (no content to pop)
        when(mockSourceTile.popTileContent()).thenReturn(null);

        // Create MoveUpdate instance
        MoveUpdate moveUpdate = new MoveUpdate(sourcePosition, sourceIndex, targetPosition);

        // Apply the move update to the mocked map
        moveUpdate.apply(mockMap);

        // Verify that no content is moved if the source tile is empty
        verify(mockTargetTile, times(0)).pushTileContent(mockSourceContent);
    }

    @Test
    public void testMoveUpdateWithMultiplePoppedContents() {
        // Simulate multiple contents being popped from the source tile
        TileContent mockSecondContent = mock(TileContent.class);
        when(mockSourceTile.popTileContent()).thenReturn(mockPoppedContent).thenReturn(mockSecondContent).thenReturn(null);

        // Create MoveUpdate instance
        MoveUpdate moveUpdate = new MoveUpdate(sourcePosition, sourceIndex, targetPosition);

        // Apply the move update to the mocked map
        moveUpdate.apply(mockMap);

        // Verify that the popped content is pushed back in the correct order
        verify(mockSourceTile, times(2)).pushTileContent(any(TileContent.class));
        verify(mockTargetTile, times(1)).pushTileContent(mockSourceContent);
    }
}
