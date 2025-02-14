package tech.underoaks.coldcase.state.tileContent;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.*;

import tech.underoaks.coldcase.HeadlessApplicationListener;
import tech.underoaks.coldcase.MapGenerator;
import tech.underoaks.coldcase.game.*;
import tech.underoaks.coldcase.state.Map;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Objects;

import static org.mockito.Mockito.*;

public class MovableBlockTest {

    private HeadlessApplicationListener game;

    @BeforeAll
    public static void beforeAll() {
        TextureFactory mockTextureFactory = mock(TextureFactory.class);
        when(mockTextureFactory.create(anyString())).thenReturn(mock(Texture.class));
        TextureController.create(mockTextureFactory);
    }

    @AfterAll
    public static void afterAll() {
        TextureController.destroy();
    }

    @BeforeEach
    public void beforeEach() throws URISyntaxException {
        game = new HeadlessApplicationListener();
        game.gameController = GameController.getInstance();

        Map map = MapGenerator.serializeContentToMap(Path.of(
            Objects.requireNonNull(getClass().getClassLoader().getResource("Map_Test_Hole")).toURI()
        ), true);
        game.gameController.setCurrentMap(map);

        PlayerController.getInstance().setPlayerPosition(new Vector2(3,1));
    }

    @AfterEach
    public void afterEach() {
        GameController.destroy();
        PlayerController.destroy();
    }

    @Test
    public void testMove() {
        // ARRANGE
        Interaction interaction_arrange = new Interaction(
            new Vector2(8, 2), // Startposition
            Direction.WEST, // Richtung
            Player.class
        );
        TileContent expectedBlock = game.gameController.getCurrentMap().getTile(2, 8).topTileContent();
        PlayerController.getInstance().setInventory(new GloveItem());

        // ACT
        boolean res = game.gameController.triggerAction(interaction_arrange);
        game.gameController.applyNextPendingGSU();

        // ASSERT
        Assertions.assertTrue(res, "Action needs to be successful");
        Assertions.assertNull(game.gameController.getCurrentMap().getTile(2, 8).topTileContent());
        Assertions.assertEquals(expectedBlock, game.gameController.getCurrentMap().getTile(1, 8).topTileContent());
    }

    @Test
    public void testMoveNOK_GloveNeeded() {
        // ARRANGE
        Interaction interaction_arrange = new Interaction(
            new Vector2(8, 2), // Startposition
            Direction.WEST, // Richtung
            Player.class
        );
        TileContent expectedBlock = game.gameController.getCurrentMap().getTile(2, 8).topTileContent();

        // ACT
        boolean res = game.gameController.triggerAction(interaction_arrange);
        game.gameController.applyNextPendingGSU();

        // ASSERT
        Assertions.assertFalse(res, "Action needs to be not successful");
        Assertions.assertEquals(expectedBlock, game.gameController.getCurrentMap().getTile(2, 8).topTileContent());
        Assertions.assertNull(game.gameController.getCurrentMap().getTile(1, 8).topTileContent());
    }

    @Test
    public void testMoveNOK_GloveNeeded_WrongItem() {
        // ARRANGE
        Interaction interaction_arrange = new Interaction(
            new Vector2(8, 2), // Startposition
            Direction.WEST, // Richtung
            Player.class
        );
        TileContent expectedBlock = game.gameController.getCurrentMap().getTile(2, 8).topTileContent();
        PlayerController.getInstance().setInventory(new TestItem());

        // ACT
        boolean res = game.gameController.triggerAction(interaction_arrange);
        game.gameController.applyNextPendingGSU();

        // ASSERT
        Assertions.assertFalse(res, "Action needs to be not successful");
        Assertions.assertEquals(expectedBlock, game.gameController.getCurrentMap().getTile(2, 8).topTileContent());
        Assertions.assertNull(game.gameController.getCurrentMap().getTile(1, 8).topTileContent());
    }

    @Test
    public void testMoveOK_GloveNeeded_SkipForRemote() throws URISyntaxException {
        // ARRANGE
        Interaction interaction_arrange = new Interaction(
            new Vector2(8, 2), // Startposition
            Direction.WEST, // Richtung
            Player.class
        );

        // Destroy current GameController to Change ID
        afterEach();
        beforeEach();

        TileContent expectedBlock = game.gameController.getCurrentMap().getTile(2, 8).topTileContent();

        // ACT
        boolean res = game.gameController.triggerAction(interaction_arrange);
        game.gameController.applyNextPendingGSU();

        // ASSERT
        Assertions.assertTrue(res, "Action needs to be successful");
        Assertions.assertNull(game.gameController.getCurrentMap().getTile(2, 8).topTileContent());
        Assertions.assertEquals(expectedBlock, game.gameController.getCurrentMap().getTile(1, 8).topTileContent());
    }

    @Test
    public void testMoveNOK_OutOfBounds() {
        // ARRANGE
        Interaction interaction_arrangeA = new Interaction(
            new Vector2(8, 2), // Startposition
            Direction.WEST, // Richtung
            Player.class
        );
        Interaction interaction_arrangeB = new Interaction(
            new Vector2(8, 1), // Startposition
            Direction.WEST, // Richtung
            Player.class
        );
        Interaction interaction_arrangeC = new Interaction(
            new Vector2(8, 0), // Startposition
            Direction.WEST, // Richtung
            Player.class
        );
        TileContent expectedBlock = game.gameController.getCurrentMap().getTile(2, 8).topTileContent();
        PlayerController.getInstance().setInventory(new GloveItem());

        // ACT
        boolean resA = game.gameController.triggerAction(interaction_arrangeA);
        game.gameController.applyNextPendingGSU();
        boolean resB = game.gameController.triggerAction(interaction_arrangeB);
        game.gameController.applyNextPendingGSU();
        boolean resC = game.gameController.triggerAction(interaction_arrangeC);
        game.gameController.applyNextPendingGSU();

        // ASSERT
        Assertions.assertTrue(resA, "Action needs to be successful");
        Assertions.assertTrue(resB, "Action needs to be successful");
        Assertions.assertFalse(resC, "Action needs to be not successful");
        Assertions.assertNull(game.gameController.getCurrentMap().getTile(1, 8).topTileContent());
        Assertions.assertNull(game.gameController.getCurrentMap().getTile(2, 8).topTileContent());
        Assertions.assertEquals(expectedBlock, game.gameController.getCurrentMap().getTile(0, 8).topTileContent());
    }

    @Test
    public void testMoveNOK_NotPassable() {
        // ARRANGE
        Interaction interaction_arrange = new Interaction(
            new Vector2(8, 2), // Startposition
            Direction.SOUTH, // Richtung
            Player.class
        );
        TileContent expectedBlock = game.gameController.getCurrentMap().getTile(2, 8).topTileContent();
        PlayerController.getInstance().setInventory(new GloveItem());

        // ACT
        boolean res = game.gameController.triggerAction(interaction_arrange);
        game.gameController.applyNextPendingGSU();

        // ASSERT
        Assertions.assertFalse(res, "Action needs to be not successful");
        Assertions.assertEquals(expectedBlock, game.gameController.getCurrentMap().getTile(2, 8).topTileContent());
        Assertions.assertNull(game.gameController.getCurrentMap().getTile(1, 8).topTileContent());
    }

    @Test
    public void testMoveOK_Passable() {
        // ARRANGE
        Interaction interaction_arrange = new Interaction(
            new Vector2(8, 2), // Startposition
            Direction.EAST, // Richtung
            Player.class
        );
        TileContent expectedBlock = game.gameController.getCurrentMap().getTile(2, 8).topTileContent();
        PlayerController.getInstance().setInventory(new GloveItem());

        // ACT
        boolean res = game.gameController.triggerAction(interaction_arrange);
        game.gameController.applyNextPendingGSU();

        // ASSERT
        Assertions.assertTrue(res, "Action needs to be successful");
        Assertions.assertNull(game.gameController.getCurrentMap().getTile(2, 8).topTileContent());
        Assertions.assertEquals(expectedBlock, game.gameController.getCurrentMap().getTile(3, 8).topTileContent());
    }
}
