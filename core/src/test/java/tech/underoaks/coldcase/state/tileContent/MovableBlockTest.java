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
            Objects.requireNonNull(getClass().getClassLoader().getResource("Map_Test")).toURI()
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
    public void testInteractionWithoutGlove() {
        PlayerController.getInstance().setInventory(null);

        Interaction interaction = new Interaction(
            new Vector2(9, 3),
            Direction.EAST,
            MovableBlock.class
        );

        boolean result = game.gameController.triggerAction(interaction);
        game.gameController.applyNextPendingGSU();

        // ASSERT
        Assertions.assertFalse(result, "Block sollte ohne Handschuh nicht bewegbar sein.");
    }

    @Test
    public void testInteractionWithGlove() {
        PlayerController.getInstance().setInventory(new GloveItem());

        Interaction interaction = new Interaction(
            new Vector2(3, 1),
            Direction.EAST,
            MovableBlock.class
        );

        boolean result = game.gameController.triggerAction(interaction);
        game.gameController.applyNextPendingGSU();

        // ASSERT
        Assertions.assertTrue(result, "Block sollte mit Handschuh bewegbar sein.");
        Assertions.assertNull(game.gameController.getCurrentMap().getTile(1, 3).topTileContent());
        Assertions.assertNotNull(game.gameController.getCurrentMap().getTile(2, 3).topTileContent());
    }

    @Test
    public void testInteractionEast() {
        Interaction interaction = new Interaction(
            new Vector2(3, 1),
            Direction.EAST,
            MovableBlock.class
        );

        TileContent expectedBlock = game.gameController.getCurrentMap().getTile(1, 3).topTileContent();

        game.gameController.triggerAction(interaction);
        game.gameController.applyNextPendingGSU();

        Assertions.assertNull(game.gameController.getCurrentMap().getTile(1,3).topTileContent());

        Assertions.assertSame(expectedBlock,game.gameController.getCurrentMap().getTile(2,3).topTileContent());

    }

    @Test
    public void testInteractionWest() {
        Interaction interaction = new Interaction(
            new Vector2(3, 1),
            Direction.WEST,
            MovableBlock.class
        );

        TileContent expectedBlock = game.gameController.getCurrentMap().getTile(1, 3).topTileContent();

        game.gameController.triggerAction(interaction);
        game.gameController.applyNextPendingGSU();

        Assertions.assertNull(game.gameController.getCurrentMap().getTile(1,3).topTileContent());

        Assertions.assertSame(expectedBlock,game.gameController.getCurrentMap().getTile(0,3).topTileContent());

    }

    @Test
    public void testInteractionNorth() {
        Interaction interaction = new Interaction(
            new Vector2(3, 1),
            Direction.NORTH,
            MovableBlock.class
        );

        TileContent expectedBlock = game.gameController.getCurrentMap().getTile(2, 3).topTileContent();

        game.gameController.triggerAction(interaction);
        game.gameController.applyNextPendingGSU();

        Assertions.assertNull(game.gameController.getCurrentMap().getTile(3,2).topTileContent());

        Assertions.assertSame(expectedBlock,game.gameController.getCurrentMap().getTile(3,1).topTileContent());

    }

    @Test
    public void testInteractionSouth() {
        Interaction interaction = new Interaction(
            new Vector2(3, 1),
            Direction.SOUTH,
            MovableBlock.class
        );

        TileContent expectedBlock = game.gameController.getCurrentMap().getTile(1, 3).topTileContent();

        game.gameController.triggerAction(interaction);
        game.gameController.applyNextPendingGSU();

        Assertions.assertNull(game.gameController.getCurrentMap().getTile(1,3).topTileContent());

        Assertions.assertSame(expectedBlock,game.gameController.getCurrentMap().getTile(1,4).topTileContent());

    }

    @Test
    public void testInvalidMovement_OutOfBounds() {
        // ARRANGE
        Interaction interaction_arrange = new Interaction(
            new Vector2(3, 1),
            Direction.WEST,
            MovableBlock.class
        );
        TileContent expectedPlayer = game.gameController.getCurrentMap().getTile(1, 3).topTileContent();

        game.gameController.triggerAction(interaction_arrange);
        game.gameController.applyNextPendingGSU();

        // ACT
        Interaction interaction_act = new Interaction(
            new Vector2(3, 0),
            Direction.WEST,
            MovableBlock.class
        );
        boolean result = game.gameController.triggerAction(interaction_act);
        game.gameController.applyNextPendingGSU();

        // ASSERT
        Assertions.assertTrue(result);
        Assertions.assertSame(expectedPlayer, game.gameController.getCurrentMap().getTile(0, 3).topTileContent());
    }

    @Test
    public void testInvalidMovement_NotPassable() {
        // ARRANGE
        Interaction interaction_arrangeA = new Interaction(
            new Vector2(3, 1),
            Direction.EAST,
            MovableBlock.class
        );
        TileContent expectedPlayer = game.gameController.getCurrentMap().getTile(1, 3).topTileContent();

        game.gameController.triggerAction(interaction_arrangeA);
        game.gameController.applyNextPendingGSU();

        Interaction interaction_arrangeB = new Interaction(
            new Vector2(3, 2),
            Direction.EAST,
            MovableBlock.class
        );
        game.gameController.triggerAction(interaction_arrangeB);
        game.gameController.applyNextPendingGSU();

        //ACT
        Interaction interaction_arrangeC = new Interaction(
            new Vector2(3, 3),
            Direction.EAST,
            MovableBlock.class
        );
        boolean result = game.gameController.triggerAction(interaction_arrangeC);
        game.gameController.applyNextPendingGSU();

        // ASSERT
        Assertions.assertTrue(result);
        Assertions.assertSame(expectedPlayer, game.gameController.getCurrentMap().getTile(3, 3).topTileContent());
    }
}
