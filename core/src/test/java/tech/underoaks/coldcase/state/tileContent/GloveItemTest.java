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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GloveItemTest {
    private HeadlessApplicationListener game;

    @BeforeAll
    public static void beforeAll() {
        TextureFactory mockTextureFactory =  mock(TextureFactory.class);
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
            Objects.requireNonNull(getClass().getClassLoader().getResource("Map_Test_GloveItem")).toURI()
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
    public void testGloveItem_Pickup() {
        // ARRANGE
        Interaction interaction = new Interaction(
            new Vector2(4, 1),
            Direction.SOUTH,
            Player.class
        );
        TileContent glove = game.gameController.getCurrentMap().getTile(1, 4).topTileContent();

        // ACT
        boolean action = game.gameController.triggerAction(interaction);
        game.gameController.applyNextPendingGSU();

        // ASSERT
        Assertions.assertTrue(action);
        Assertions.assertEquals(glove, PlayerController.getInstance().getInventory());
        Assertions.assertNull(game.gameController.getCurrentMap().getTile(1, 4).topTileContent());
    }

    @Test
    public void testGloveItem_Swap() {
        // ARRANGE
        Interaction interaction = new Interaction(
            new Vector2(4, 1),
            Direction.SOUTH,
            Player.class
        );
        TileContent glove = game.gameController.getCurrentMap().getTile(1, 4).topTileContent();
        TestItem testItem = new TestItem();
        PlayerController.getInstance().setInventory(testItem);

        // ACT
        boolean action = game.gameController.triggerAction(interaction);
        game.gameController.applyNextPendingGSU();
        game.gameController.applyNextPendingGSU();

        // ASSERT
        Assertions.assertTrue(action);
        Assertions.assertEquals(glove, PlayerController.getInstance().getInventory());
        Assertions.assertEquals(testItem, game.gameController.getCurrentMap().getTile(1, 4).topTileContent());
    }
}
