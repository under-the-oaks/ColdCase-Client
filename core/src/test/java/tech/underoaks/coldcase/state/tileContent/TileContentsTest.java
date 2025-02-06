package tech.underoaks.coldcase.state.tileContent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

public class TileContentsTest {
    /**
     * @implNote This does not test the Enum against any 'correct' values.
     */
    @Test
    public void testGetIndexByClass_OK() {
        // ARRANGE
        TileContents content = TileContents.GLOVE_ITEM;
        GloveItem glove = mock(GloveItem.class);
        int expected = content.getIndex();

        // ACT
        int actual = TileContents.getIndexByClass(glove.getClass());

        // ASSERT
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetIndexByClass_NotFound() {
        // ARRANGE
        int expected = -1;

        // ACT
        int actual = TileContents.getIndexByClass(null);

        // ASSERT
        Assertions.assertEquals(expected, actual);
    }
}
