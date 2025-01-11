package tech.underoaks.coldcase;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.underoaks.coldcase.game.Interaction;
import tech.underoaks.coldcase.state.InteractionChain;
import tech.underoaks.coldcase.state.tileContent.TileContent;
import tech.underoaks.coldcase.state.tileContent.UpdateTileContentException;
import tech.underoaks.coldcase.state.tiles.Tile;
import tech.underoaks.coldcase.state.updates.GameStateUpdate;
import tech.underoaks.coldcase.state.updates.GameStateUpdateException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TileTest {

    static ApplicationListener game;
    static Texture mockTexture;
    static Tile mockTile;
    static TileContent mockTileContent;
    static TileContent mockTileContentZwo;

    static class TestTile extends Tile {

        public TestTile() {
            super(mockTexture);
        }

    }

    /**
     * A TileContent used for testing purposes using a mockTexture instead of an actual.
     * <p>
     * The action method adds a BrokenTileContent on top of one tile to change the map
     * for testing. It did not work as intended though.
     */
    static class TestTileContent extends TileContent {

        public TestTileContent() {
            super(mockTexture, false, false);
        }

        @Override
        public boolean action(InteractionChain chain, Interaction interaction) throws GameStateUpdateException {
            return false;
        }

        @Override
        public boolean update(InteractionChain chain, Vector2 tilePosition, Interaction interaction, TileContent handler) throws GameStateUpdateException {

            if (chain.getGSUQueue().peek() == null) {
                GameStateUpdate update = new MapTest.EmptyUpdate();

                chain.addGameStateUpdate(update);

                return true;
            }

            return false;
        }
    }

    @BeforeEach
    public void setUp(){
        game = new HeadlessApplicationListener();

        // Mock Variablen befÃ¼llen

        mockTexture = mock(Texture.class);
        when(mockTexture.getWidth()).thenReturn(32);
        when(mockTexture.getHeight()).thenReturn(32);

        mockTile = new TestTile();

        mockTileContent = new TestTileContent();
        mockTileContentZwo = new TestTileContent();
        mockTile.setTileContent(null);
    }

    //Tests for pushTileContent
    @Test
    void pushTileContent_ShouldSetContent_WhenTileIsEmpty(){
        mockTile.pushTileContent(mockTileContent);

        assertEquals(mockTileContent, mockTile.getTileContent());
    }

    @Test
    void pushTileContent_ShouldDelegateToExistingContent_WhenTileIsNotEmpty(){
        mockTile.setTileContent(mockTileContent);

        mockTile.pushTileContent(mockTileContentZwo);
        assertEquals(mockTileContentZwo, mockTileContent.getNextContent());
    }

    //Tests for popTileContent
    @Test
    void popTileContent_ShouldReturnNull_WhenTileContentIsNull(){
        assertNull(mockTile.popTileContent());
    }

    @Test
    void popTileContent_ShouldReturnOwnTileContent_WhenNoChildContentExists(){
        mockTile.setTileContent(mockTileContent);

        assertEquals(mockTileContent,mockTile.popTileContent());
    }

    @Test
    void popTileContent_ShouldDelegateToChildTileContent_WhenChainContainsMultipleContents() {
        mockTile.setTileContent(mockTileContent);
        mockTileContent.setNextContent(mockTileContentZwo);

        assertEquals(mockTileContentZwo,mockTile.popTileContent());
    }

    //Tests for topTileContent
    @Test
    void topTileContent_ShouldReturnNull_WhenTileContentIsNull(){
        assertNull(mockTile.topTileContent());
    }

    @Test
    void topTileContent_ShouldReturnTheTilesTileContent_WhenTileContentIsNotNull(){
        mockTile.setTileContent(mockTileContent);

        TileContent res = mockTile.topTileContent();

        assertEquals(mockTileContent,res);
    }

    //Tests for clone

    @Test
    void clone_ShouldReturnDifferentInstance() {
        mockTile.setTileContent(mockTileContent);

        Tile clonedTile = mockTile.clone();

        assertNotSame(mockTile, clonedTile);
    }

    @Test
    void clone_ShouldCopyTileContent_WhenTileContentIsNotNull() {
        mockTile.setTileContent(mockTileContent);

        Tile clonedTile = mockTile.clone();

        assertNotSame(mockTile, clonedTile);
        assertEquals(mockTile, clonedTile);
    }

}