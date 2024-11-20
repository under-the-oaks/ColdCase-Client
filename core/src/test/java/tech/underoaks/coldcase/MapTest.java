package tech.underoaks.coldcase;

import com.badlogic.gdx.ApplicationListener;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.underoaks.coldcase.game.Direction;
import tech.underoaks.coldcase.state.InteractionChain;
import tech.underoaks.coldcase.state.tileContent.TileContent;
import tech.underoaks.coldcase.state.tileContent.Wall;
import tech.underoaks.coldcase.state.tiles.Tile;
import tech.underoaks.coldcase.state.Map;
import tech.underoaks.coldcase.state.updates.UpdateTypes;
import tech.underoaks.coldcase.state.updates.GameStateUpdate;
import tech.underoaks.coldcase.state.updates.GameStateUpdateException;

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

            System.out.println("Applied");
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
     *
     * The action method adds a BrokenTileContent on top of one tile to change the map
     * for testing. It did not work as intended though.
     */
    static class TestTileContent extends TileContent {

        public TestTileContent() {
            super(mockTexture, false, false);
        }

        @Override
        public boolean action(InteractionChain chain, Vector2 tilePosition, Direction actionDirection) throws GameStateUpdateException {

            if (chain.getSnapshot().getSnapshotMap().getTile(tilePosition).topTileContent().getClass() == BrokenTileContent.class) {
                return false;
            }

            //System.out.println("TestTile.action()");
            chain.addGameStateUpdate( new EmptyUpdate() );
            return true;
        }

        @Override
        public boolean update(InteractionChain chain, Vector2 tilePosition) throws GameStateUpdateException {
            //System.out.println("TestTile.update()");

            if (chain.getSnapshot().getSnapshotMap().getTile(tilePosition).topTileContent().getClass() == BrokenTileContent.class) {
                return true;
            }

            return false;
        }
    }

    /**
     * A TileContent used for testing purposes mainly for {@code UpdateMapUntilStable}.
     *
     * The action method always returns true making it run the method run into it's maximum
     * amount of iteration.
     */
    static class BrokenTileContent extends TileContent {

        public BrokenTileContent() {
            super(mockTexture, false, false);
        }

        @Override
        public boolean action(InteractionChain chain, Vector2 tilePosition, Direction actionDirection) throws GameStateUpdateException {
            //System.out.println("brokenTile.action()");
            chain.addGameStateUpdate( new EmptyUpdate() );
            return true;
        }

        @Override
        public boolean update(InteractionChain chain, Vector2 tilePosition) throws GameStateUpdateException {
            //System.out.println("brokenTile.update()");
            return false;
        }
    }

    @BeforeEach
    public void setUp(){
        //das kann alles mit mockito gemacht werden, der headless application listener ist nicht notwendig
        //game = new HeadlessApplicationListener();

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

        /* TODO - Soll das so sein?
         Map emptyMap = new Map(null); //Leere Map wird nicht gehandled
         System.out.println( emptyMap.getTileContentByType( TestTileContent.class ) );
         Assertions.assertNull( emptyMap.getTileContentByType( TestTileContent.class ) );
         */

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

        Assertions.assertEquals(mockMap.getHeight(), deepClone.getHeight());
        Assertions.assertEquals(mockMap.getWidth(), deepClone.getWidth());

        Assertions.assertSame(mockMap.getTile(position).getClass(), deepClone.getTile(position).getClass());

        Assertions.assertNotSame(mockMap.getTileContentByIndex(position,index),
            deepClone.getTileContentByIndex(position,index));

    }

    @Test
    public void UpdateMapTest() {

        /* Versuch

        GameController gameController = new GameController();

        gameController.setCurrentMap(mockMap);

        Vector2 past = mockMap.getTileContentByType( BrokenTileContent.class );

        gameController.triggerAction( new Vector2(0,0), Direction.NORTH );

        Vector2 current = mockMap.getTileContentByType( BrokenTileContent.class );

        // Soll beweisen, das die Map verändert wird
        Assertions.assertNotNull(current);


         */
    }

    @Test
    public void UpdateMapUntilStableTest() {

        /*

        Tile brokenTile = new TestTile();
        brokenTile.setTileContent(new BrokenTileContent());

        mockMap = new Map(
            new Tile[][] {
                { mockTile, brokenTile }
            }
        );

        GameController gameController = new GameController();

        gameController.setCurrentMap(mockMap);

        Vector2 position = new Vector2(0, 0);
        Vector2 brokenTilePosition = new Vector2(1, 0);

        // Soll nach 100 Iterationen fehlschlagen
        Assertions.assertThrows( IllegalStateException.class, () -> gameController.triggerAction( brokenTilePosition, Direction.NORTH));

        mockMap.setTile(1,0,mockTile);

        // Soll alle Updates ausführen
        Assertions.assertDoesNotThrow(() -> gameController.triggerAction( position, Direction.NORTH));


         */
    }
}
