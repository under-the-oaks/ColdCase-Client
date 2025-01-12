package tech.underoaks.coldcase;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.*;
import tech.underoaks.coldcase.game.*;
import tech.underoaks.coldcase.state.Map;
import tech.underoaks.coldcase.state.tileContent.Player;
import tech.underoaks.coldcase.state.tileContent.TileContent;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayerTest {


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

        PlayerController.getInstance().setPlayerPosition(new Vector2(3, 1));
    }

    @AfterEach
    public void afterEach() {
        GameController.destroy();
        PlayerController.destroy();
    }

    @Test
    public void testInteractionEast() {
        Interaction interaction = new Interaction(
                new Vector2(3, 1),
                Direction.EAST,
                Player.class
        );

        TileContent expectedPlayer = game.gameController.getCurrentMap().getTile(1, 3).topTileContent();

        game.gameController.triggerAction(interaction);
        game.gameController.applyNextPendingGSU();

        // Ist der Player nicht mehr an seiner alten Position?
        Assertions.assertNull(game.gameController.getCurrentMap().getTile(1, 3).topTileContent());

        // Ist der Player an seiner neuen Position?
        Assertions.assertSame(expectedPlayer, game.gameController.getCurrentMap().getTile(2, 3).topTileContent());

    }


    @Test
    public void testInteractionWest() {
        Interaction interaction = new Interaction(
                new Vector2(3, 1), // Startposition
                Direction.WEST,    // Richtung
                Player.class
        );

        TileContent expectedPlayer = game.gameController.getCurrentMap().getTile(1, 3).topTileContent();

        game.gameController.triggerAction(interaction);
        game.gameController.applyNextPendingGSU();

        // Ist der Player nicht mehr an seiner alten Position?
        Assertions.assertNull(game.gameController.getCurrentMap().getTile(1, 3).topTileContent());

        // Ist der Player an seiner neuen Position?
        Assertions.assertSame(expectedPlayer, game.gameController.getCurrentMap().getTile(0, 3).topTileContent());
    }


    @Test
    public void testInteractionNorth() {
        Interaction interaction = new Interaction(
                new Vector2(3, 2), // Startposition
                Direction.NORTH,   // Richtung
                Player.class
        );

        TileContent expectedPlayer = game.gameController.getCurrentMap().getTile(2, 3).topTileContent();

        game.gameController.triggerAction(interaction);
        game.gameController.applyNextPendingGSU();

        // Ist der Player nicht mehr an seiner alten Position?
        Assertions.assertNull(game.gameController.getCurrentMap().getTile(3, 2).topTileContent());

        // Ist der Player an seiner neuen Position?
        Assertions.assertSame(expectedPlayer, game.gameController.getCurrentMap().getTile(3, 1).topTileContent());
    }

    @Test
    public void testInteractionSouth() {
        Interaction interaction = new Interaction(
                new Vector2(3, 1), // Startposition
                Direction.SOUTH,   // Richtung
                Player.class
        );

        TileContent expectedPlayer = game.gameController.getCurrentMap().getTile(1, 3).topTileContent();

        game.gameController.triggerAction(interaction);
        game.gameController.applyNextPendingGSU();

        // Ist der Player nicht mehr an seiner alten Position?
        Assertions.assertNull(game.gameController.getCurrentMap().getTile(1, 3).topTileContent());

        // Ist der Player an seiner neuen Position?
        Assertions.assertSame(expectedPlayer, game.gameController.getCurrentMap().getTile(1, 4).topTileContent());
    }

    @Test
    public void testInvalidMovement_OutOfBounds() {
        // ARRANGE
        Interaction interaction_arrange = new Interaction(
                new Vector2(3, 1), // Startposition
                Direction.WEST, // Richtung
                Player.class
        );
        TileContent expectedPlayer = game.gameController.getCurrentMap().getTile(1, 3).topTileContent();

        game.gameController.triggerAction(interaction_arrange);
        game.gameController.applyNextPendingGSU();

        // ACT
        Interaction interaction_act = new Interaction(
                new Vector2(3, 0), // Startposition
                Direction.WEST,   // Richtung
                Player.class
        );
        boolean result = game.gameController.triggerAction(interaction_act);
        game.gameController.applyNextPendingGSU();

        // ASSERT
        Assertions.assertTrue(result); // Auch wenn eine Aktion fehlschlägt, gilt die Aktion als 'behandelt'
        Assertions.assertSame(expectedPlayer, game.gameController.getCurrentMap().getTile(0, 3).topTileContent());
    }

    @Test
    public void testInvalidMovement_NotPassable() {
        // ARRANGE
        Interaction interaction_arrangeA = new Interaction(
                new Vector2(3, 1), // Startposition
                Direction.EAST, // Richtung
                Player.class
        );
        TileContent expectedPlayer = game.gameController.getCurrentMap().getTile(1, 3).topTileContent();

        game.gameController.triggerAction(interaction_arrangeA);
        game.gameController.applyNextPendingGSU();

        Interaction interaction_arrangeB = new Interaction(
                new Vector2(3, 2), // Startposition
                Direction.EAST, // Richtung
                Player.class
        );
        game.gameController.triggerAction(interaction_arrangeB);
        game.gameController.applyNextPendingGSU();

        //ACT
        Interaction interaction_arrangeC = new Interaction(
                new Vector2(3, 3), // Startposition
                Direction.EAST, // Richtung
                Player.class
        );
        boolean result = game.gameController.triggerAction(interaction_arrangeC);
        game.gameController.applyNextPendingGSU();

        // ASSERT
        Assertions.assertTrue(result); // Auch wenn eine Aktion fehlschlägt, gilt die Aktion als 'behandelt'
        Assertions.assertSame(expectedPlayer, game.gameController.getCurrentMap().getTile(3, 3).topTileContent());
    }
}
