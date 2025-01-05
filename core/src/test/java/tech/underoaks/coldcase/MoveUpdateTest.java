package tech.underoaks.coldcase;


import static org.mockito.Mockito.*;



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


public class MoveUpdateTest {


    private HeadlessApplicationListener game;

    @BeforeAll
    public static void beforeAll() {
        TextureFactory mockTextureFactory = mock(TextureFactory.class);
        when(mockTextureFactory.create(anyString())).thenReturn(mock(Texture.class));
        TextureController.create(false, mockTextureFactory);
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
        Assertions.assertNull(game.gameController.getCurrentMap().getTile(1,3).topTileContent());

        // Ist der Player an seiner neuen Position?
        Assertions.assertSame(expectedPlayer,game.gameController.getCurrentMap().getTile(2,3).topTileContent());

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


}



