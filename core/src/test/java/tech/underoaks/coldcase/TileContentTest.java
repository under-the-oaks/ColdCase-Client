package tech.underoaks.coldcase;

import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.Test;
import tech.underoaks.coldcase.game.Direction;
import tech.underoaks.coldcase.state.InteractionChain;
import tech.underoaks.coldcase.state.tileContent.TileContent;
import tech.underoaks.coldcase.state.updates.GameStateUpdateException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TileContentTest {

    @Test
    public void testHandleAction() throws GameStateUpdateException {
        TileContent content = mock(TileContent.class);
        InteractionChain chain = mock(InteractionChain.class);
        Vector2 tilePosition = new Vector2(0, 0);
        Direction actionDirection = Direction.NORTH;

        when(content.handleAction(chain, tilePosition, actionDirection)).thenReturn(true);

        boolean result = content.handleAction(chain, tilePosition, actionDirection);

        assertTrue(result);
    }

    @Test
    public void testHandleUpdate() throws GameStateUpdateException {
        TileContent content = mock(TileContent.class);
        InteractionChain chain = mock(InteractionChain.class);
        Vector2 tilePosition = new Vector2(0, 0);

        when(content.handleUpdate(chain, tilePosition)).thenReturn(true);

        boolean result = content.handleUpdate(chain, tilePosition);

        assertTrue(result);
    }

    @Test
    public void testPushContent() {
        TileContent baseContent = mock(TileContent.class);
        TileContent newContent = mock(TileContent.class);

        when(baseContent.getNextContent()).thenReturn(newContent);

        baseContent.pushContent(newContent);

        assertEquals(newContent, baseContent.getNextContent());
    }

    @Test
    public void testPopContent() {
        TileContent baseContent = mock(TileContent.class);
        TileContent newContent = mock(TileContent.class);

        when(baseContent.getNextContent()).thenReturn(newContent);

        when(baseContent.popContent()).thenReturn(newContent);

        when(baseContent.getNextContent()).thenReturn(null);

        TileContent poppedContent = baseContent.popContent();

        assertEquals(newContent, poppedContent);
        assertNull(baseContent.getNextContent());
    }

    @Test
    public void testTopContent() {
        TileContent baseContent = mock(TileContent.class);
        TileContent newContent = mock(TileContent.class);

        when(baseContent.topContent()).thenReturn(newContent);

        TileContent topContent = baseContent.topContent();

        assertEquals(newContent, topContent);
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        TileContent content = mock(TileContent.class);

        TileContent clonedContent = mock(TileContent.class);
        when(content.clone()).thenReturn(clonedContent);

        TileContent result = content.clone();

        assertNotNull(result);
        assertEquals(clonedContent, result);
    }

    @Test
    public void testGetChildIndex() {
        TileContent baseContent = mock(TileContent.class);
        TileContent childContent = mock(TileContent.class);

        when(baseContent.getChildIndex(childContent)).thenReturn(1);

        int index = baseContent.getChildIndex(childContent);

        assertEquals(1, index);
    }

    @Test
    public void testGetTileContentByIndex() {
        TileContent baseContent = mock(TileContent.class);
        TileContent childContent = mock(TileContent.class);

        when(baseContent.getTileContentByIndex(1)).thenReturn(childContent);

        TileContent retrievedContent = baseContent.getTileContentByIndex(1);

        assertEquals(childContent, retrievedContent);
    }

}
