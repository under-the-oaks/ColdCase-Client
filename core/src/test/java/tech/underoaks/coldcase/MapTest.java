package tech.underoaks.coldcase;

import com.badlogic.gdx.ApplicationListener;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.underoaks.coldcase.game.*;
import tech.underoaks.coldcase.state.InteractionChain;
import tech.underoaks.coldcase.state.Snapshot;
import tech.underoaks.coldcase.state.tileContent.TileContent;
import tech.underoaks.coldcase.state.tileContent.Wall;
import tech.underoaks.coldcase.state.tiles.Tile;
import tech.underoaks.coldcase.state.Map;
import tech.underoaks.coldcase.state.updates.UpdateTypes;
import tech.underoaks.coldcase.state.updates.GameStateUpdate;
import tech.underoaks.coldcase.state.updates.GameStateUpdateException;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MapTest{

    static ApplicationListener game;
    static Texture mockTexture;
    static Map mockMap;
    static Tile mockTile;
    static TileContent mockTileContent;

    // "Empty" Update

    /**
     * A GamesStateUpdate constructed to work with {@code TestTileContent}
     */
    static class EmptyUpdate extends GameStateUpdate {

        protected EmptyUpdate() {
            super(UpdateTypes.MAP_MODIFICATION);
        }

        @Override
        public void apply(Map map) {

            map.getTile(0,0).pushTileContent( new BrokenTileContent() );

        }
    }

    // Fake Tile Klasse erstellen

    static class TestTile extends Tile {

        public TestTile() {
            super(mockTexture);
        }

    }

    // Fake TileContent Klasse erstellen

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
        public boolean action(InteractionChain chain, Interaction interaction) {
            return false;
        }

        @Override
        public boolean update(InteractionChain chain, Vector2 tilePosition, Interaction interaction, TileContent handler) throws GameStateUpdateException {

            if (chain.getGSUQueue().peek() == null) {
                GameStateUpdate update = new EmptyUpdate();

                chain.addGameStateUpdate(update);

                return true;
            }

            return false;
        }
    }

    /**
     * A TileContent used for testing purposes mainly for {@code UpdateMapUntilStable}.
     * The action method always returns true making it run the method run into it's maximum
     * amount of iteration.
     */
    static class BrokenTileContent extends TileContent {

        public BrokenTileContent() {
            super(mockTexture, false, false);
        }

        @Override
        public boolean action(InteractionChain chain, Interaction interaction) throws GameStateUpdateException {
            //System.out.println("brokenTile.action()");
            chain.addGameStateUpdate( new EmptyUpdate() );
            return true;
        }

        @Override
        public boolean update(InteractionChain chain, Vector2 tilePosition, Interaction interaction, TileContent handler) {
            return true;
        }
    }

    @BeforeEach
    public void setUp(){
        game = new HeadlessApplicationListener();

        // Mock Variablen befüllen

        mockTexture = mock(Texture.class);
        when(mockTexture.getWidth()).thenReturn(32);
        when(mockTexture.getHeight()).thenReturn(32);

        mockTile = new TestTile();

        mockTileContent = new TestTileContent();

        mockTile.setTileContent(mockTileContent);

        Tile[][] testTileArray = new Tile[1][1];

        testTileArray[0][0] = mockTile;

        mockMap = new Map(testTileArray);
    }

    @Test
    public void GetTileContentByTypeTest(){

        // Finden
        Assertions.assertEquals( new Vector2(0,0), mockMap.getTileContentByType( TestTileContent.class ));

        // Nicht finden
        Assertions.assertNull( mockMap.getTileContentByType( Wall.class ));
    }

    @Test
    public void GetMatrixWidthAndHeightTest(){

    }

    @Test
    public void GetTileContentByIndexTest(){

        // Finden
        Assertions.assertEquals(
            mockTileContent,
            mockMap.getTileContentByIndex( new Vector2(0, 0), 0)
        );

        // Map - Out of Bounds
        Assertions.assertThrows( ArrayIndexOutOfBoundsException.class, () -> mockMap.getTileContentByIndex( new Vector2(0,1), 0 ) );

        // TileContent - Out of Bounds
        Assertions.assertThrows( IllegalArgumentException.class, () -> mockMap.getTileContentByIndex( new Vector2(0,0), 1 ) );

    }

    @Test
    public void IsOutOfBoundsTest() {

        Vector2 position;

        // Position innerhalb der Map
        position = new Vector2(0, 0);
        Assertions.assertFalse(mockMap.isOutOfBounds(position));

        // Negative Position - x
        position = new Vector2(-1, 1);
        Assertions.assertTrue(mockMap.isOutOfBounds(position));

        // Position out of bounds - x
        position = new Vector2(5, 1);
        Assertions.assertTrue(mockMap.isOutOfBounds(position));

        // Negative Position - y
        position = new Vector2(1, -1);
        Assertions.assertTrue(mockMap.isOutOfBounds(position));

        // Position out of bounds - y
        position = new Vector2(1, 5);
        Assertions.assertTrue(mockMap.isOutOfBounds(position));

        // Grenzfälle
        position = new Vector2(0, 1);
        Assertions.assertTrue(mockMap.isOutOfBounds(position));

        position = new Vector2(1, 0);
        Assertions.assertTrue(mockMap.isOutOfBounds(position));

    }

    @Test
    public void isoTo2DTest() {

        // Hängt immer stark vom Rendering/Fine-Tuning ab

        Vector2 position;
        Vector2 result;

        // Most Basic Output
        position = new Vector2(0, 0);
        result = mockMap.isoTo2D(position);
        Assertions.assertEquals(new Vector2(0, 0), result);

        position = new Vector2(1, 0);
        result = mockMap.isoTo2D(position);
        Assertions.assertEquals(new Vector2(0.5f, -0.5f), result);

        position = new Vector2(0, 1);
        result = mockMap.isoTo2D(position);
        Assertions.assertEquals(new Vector2(1.0f, 1.0f), result);

        // 1,1 Projection
        position = new Vector2(1, 1);
        result = mockMap.isoTo2D(position);
        Assertions.assertEquals(new Vector2(1.5f, 0.5f), result);

        // -1,-1 Projection
        position = new Vector2(-1, -1);
        result = mockMap.isoTo2D(position);
        Assertions.assertEquals(new Vector2(-1.5f, -0.5f), result);

    }

    @Test
    public void twoDToIsoTest() {

        // Hängt immer stark vom Rendering/Fine-Tuning ab

        Vector2 position;
        Vector2 result;

        // Most Basic Output
        position = new Vector2(0, 0);
        result = mockMap.twoDToIso(position);
        Assertions.assertEquals(new Vector2(0, 0), result);

        position = new Vector2(0.5f, -0.5f);
        result = mockMap.twoDToIso(position);
        Assertions.assertEquals(new Vector2(1, 0), result);

        position = new Vector2(1.0f, 1.0f);
        result = mockMap.twoDToIso(position);
        Assertions.assertEquals(new Vector2(0, 1), result);

        // 1,1 Projection
        position = new Vector2(1.5f, 0.5f);
        result = mockMap.twoDToIso(position);
        Assertions.assertEquals(new Vector2(1,1), result);

        // -1,-1 Projection
        position = new Vector2(-1.5f, -0.5f);
        result = mockMap.twoDToIso(position);
        Assertions.assertEquals(new Vector2(-1, -1), result);

    }

    @Test
    public void DeepCloneTest(){

        // Nicht ausreichend

        Map deepClone = mockMap.deepClone();

        Vector2 position = new Vector2(0,0);
        int index = 0;

        Assertions.assertNotNull(deepClone);

        Assertions.assertEquals(mockMap.getTileArrayHeight(), deepClone.getTileArrayHeight());
        Assertions.assertEquals(mockMap.getTileArrayWidth(), deepClone.getTileArrayWidth());

        Assertions.assertSame(mockMap.getTile(position).getClass(), deepClone.getTile(position).getClass());

        Assertions.assertNotSame(mockMap.getTileContentByIndex(position,index),
            deepClone.getTileContentByIndex(position,index));

    }

    @Test
    public void UpdateMapTest() {

        Snapshot snapshot = new Snapshot(mockMap);

        InteractionChain chain = new InteractionChain(snapshot);

        GameStateUpdate before = chain.getGSUQueue().peek();

        Interaction interaction = mock(Interaction.class);
        TileContent handler = mock(TileContent.class);

        boolean updated = false;

        try {
            List<TileContent> updatedTiles = mockMap.updateMap(chain, interaction, handler);

            for (TileContent updatedTile : updatedTiles) {
                updated = updated || ( updatedTile != null ) ;
            }
        }
        catch (Exception ignored) {

        }

        GameStateUpdate after = chain.getGSUQueue().peek();

        // Check whether Map was updated

        Assertions.assertTrue(updated);

        // Check if the GameStateUpdate was queued, like the update() of TestTileContent does

        Assertions.assertNotEquals(before, after);
    }

    @Test
    public void UpdateMapUntilStableTest() {

        Snapshot snapshot = new Snapshot(mockMap);

        InteractionChain chain = new InteractionChain(snapshot);

        Interaction interaction = mock(Interaction.class);
        TileContent handler = mock(TileContent.class);

        // Läuft durch

        try{
            mockMap.updateUntilStable(chain, interaction, handler);
        }
        catch (Exception ignored) {

        }

        mockMap.getTile(0,0).pushTileContent( new BrokenTileContent() );

        snapshot = new Snapshot(mockMap);

        InteractionChain brokenChain = new InteractionChain(snapshot);

        // Endless Loop wird abgebrochen

        Assertions.assertThrows( IllegalStateException.class, () -> mockMap.updateUntilStable( brokenChain, interaction, handler ));

    }

    @Test
    public void IsPlayerNextToTileTest() throws URISyntaxException {
        // ARRANGE
        TextureFactory mockTextureFactory =  mock(TextureFactory.class);
        when(mockTextureFactory.create(anyString())).thenReturn(mock(Texture.class));
        TextureController.create(mockTextureFactory);

        Map map = MapGenerator.serializeContentToMap(Path.of(
            Objects.requireNonNull(getClass().getClassLoader().getResource("Map_Test_GloveItem")).toURI()
        ), true);
        GameController.getInstance().setCurrentMap(map);

        Vector2 playerPosition = new Vector2(3, 1);
        PlayerController.getInstance().setPlayerPosition(playerPosition);

        int[] indices = {1,0,-1};

        // ACT, ASSERT
        for(int index_a : indices) {
            for(int index_b : indices) {
                if(index_a == 0 && index_b == 0) continue;

                Vector2 testPosition = new Vector2(
                    playerPosition.x + index_a,
                    playerPosition.y + index_b
                );
                Assertions.assertTrue(Map.isPlayerNextToTile(testPosition));
            }
        }
        Assertions.assertFalse(Map.isPlayerNextToTile(playerPosition));
        Assertions.assertFalse(Map.isPlayerNextToTile(new Vector2(-10, -10)));

        GameController.destroy();
        PlayerController.destroy();
        TextureController.destroy();
    }

    /**
     * Berechnung:
     * x = 450*(0 - 0) = 0
     * y = -320*(0 + 0) = 0
     */
    @Test
    public void TwoDToIso45Test_Zero() {
        Vector2 result = Map.twoDToIso45(0, 0);
        Assertions.assertEquals(0.0f, result.x, 0.001f);
        Assertions.assertEquals(0.0f, result.y, 0.001f);
    }

    /**
     * Berechnung:
     * x = 450*(1 - 0) = 450
     * y = -320*(1 + 0) = -320
     */
    @Test
    public void TwoDToIso45Test_PositiveInput() {
        Vector2 result = Map.twoDToIso45(1, 0);
        Assertions.assertEquals(450.0f, result.x, 0.001f);
        Assertions.assertEquals(-320.0f, result.y, 0.001f);
    }

    /**
     * Berechnung:
     * x = 450*(2 - 1) = 450
     * y = -320*(2 + 1) = -960
     */
    @Test
    public void TwoDToIso45Test_MixedInput() {
        Vector2 result = Map.twoDToIso45(2, 1);
        Assertions.assertEquals(450.0f, result.x, 0.001f);
        Assertions.assertEquals(-960.0f, result.y, 0.001f);
    }

    /**
     * Berechnung:
     * x = 450*(-1 - (-2)) = 450*(1) = 450
     * y = -320*(-1 + (-2)) = -320*(-3) = 960
     */
    @Test
    public void TwoDToIso45Test_NegativeInput() {
        Vector2 result = Map.twoDToIso45(-1, -2);
        Assertions.assertEquals(450.0f, result.x, 0.001f);
        Assertions.assertEquals(960.0f, result.y, 0.001f);
    }
}
