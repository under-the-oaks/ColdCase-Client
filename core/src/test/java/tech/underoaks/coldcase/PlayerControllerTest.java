package tech.underoaks.coldcase;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.*;
import tech.underoaks.coldcase.game.*;
import tech.underoaks.coldcase.state.Map;
import tech.underoaks.coldcase.state.tileContent.GloveItem;
import tech.underoaks.coldcase.state.tileContent.Player;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayerControllerTest {

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
        HeadlessApplicationListener game = new HeadlessApplicationListener();
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
    public void testMoveNorth() {
        // ARRANGE
        Vector2 previousPos = PlayerController.getInstance().getPlayerPosition();
        Vector2 expectedPos = previousPos.add(Direction.NORTH.getVector());

        // ACT
        PlayerController.getInstance().keyDown(Input.Keys.W);
        PlayerController.getInstance().keyUp(Input.Keys.W);
        PlayerController.getInstance().keyDown(Input.Keys.W);
        PlayerController.getInstance().keyUp(Input.Keys.W);
        GameController.getInstance().applyNextPendingGSU();
        GameController.getInstance().applyNextPendingGSU();

        // ASSERT
        Assertions.assertEquals(expectedPos, PlayerController.getInstance().getPlayerPosition());
    }

    @Test
    public void testMoveEast() {
        // ARRANGE
        Vector2 previousPos = PlayerController.getInstance().getPlayerPosition();
        Vector2 expectedPos = previousPos.add(Direction.EAST.getVector());

        // ACT
        PlayerController.getInstance().keyDown(Input.Keys.D);
        PlayerController.getInstance().keyUp(Input.Keys.D);
        PlayerController.getInstance().keyDown(Input.Keys.D);
        PlayerController.getInstance().keyUp(Input.Keys.D);
        GameController.getInstance().applyNextPendingGSU();
        GameController.getInstance().applyNextPendingGSU();

        // ASSERT
        Assertions.assertEquals(expectedPos, PlayerController.getInstance().getPlayerPosition());
    }

    @Test
    public void testMoveWest() {
        // ARRANGE
        Vector2 previousPos = PlayerController.getInstance().getPlayerPosition();
        Vector2 expectedPos = previousPos.add(Direction.WEST.getVector());

        // ACT
        PlayerController.getInstance().keyDown(Input.Keys.A);
        PlayerController.getInstance().keyUp(Input.Keys.A);
        PlayerController.getInstance().keyDown(Input.Keys.A);
        PlayerController.getInstance().keyUp(Input.Keys.A);
        GameController.getInstance().applyNextPendingGSU();
        GameController.getInstance().applyNextPendingGSU();

        // ASSERT
        Assertions.assertEquals(expectedPos, PlayerController.getInstance().getPlayerPosition());
    }

    @Test
    public void testMoveSouth() {
        // ARRANGE
        Vector2 previousPos = PlayerController.getInstance().getPlayerPosition();
        Vector2 expectedPos = previousPos.add(Direction.SOUTH.getVector());

        // ACT
        PlayerController.getInstance().keyDown(Input.Keys.S);
        PlayerController.getInstance().keyUp(Input.Keys.S);
        PlayerController.getInstance().keyDown(Input.Keys.S);
        PlayerController.getInstance().keyUp(Input.Keys.S);
        GameController.getInstance().applyNextPendingGSU();
        GameController.getInstance().applyNextPendingGSU();

        // ASSERT
        Assertions.assertEquals(expectedPos, PlayerController.getInstance().getPlayerPosition());
    }

    @Test
    public void testInteract() {
        // ARRANGE
        // ACT
        for (int i = 0; i < 4; i++) {
            PlayerController.getInstance().keyDown(Input.Keys.S);
            PlayerController.getInstance().keyUp(Input.Keys.S);
            GameController.getInstance().applyNextPendingGSU();
        }

        PlayerController.getInstance().keyDown(Input.Keys.E);
        PlayerController.getInstance().keyUp(Input.Keys.E);
        GameController.getInstance().applyNextPendingGSU();
        GameController.getInstance().applyNextPendingGSU();

        // ASSERT
        Assertions.assertEquals(new Vector2(6, 1), PlayerController.getInstance().getPlayerPosition());
        Assertions.assertEquals(Player.class, GameController.getInstance().getCurrentMap().getTile(1, 6).topTileContent().getClass());

        Assertions.assertEquals(GloveItem.class, PlayerController.getInstance().getInventory().getClass());
        Assertions.assertNull(GameController.getInstance().getCurrentMap().getTile(1, 7).topTileContent());
    }

    @Test
    public void testMove_NOK_PendingGSU() {
        // ARRANGE
        PlayerController.getInstance().setInventory(new GloveItem());
        GameController.getInstance().triggerAction(
            new Interaction(
                new Vector2(8,2),
                Direction.EAST,
                PlayerController.class
            )
        );
        Vector2 previousPos = PlayerController.getInstance().getPlayerPosition();

        // ACT
        for (int i = 0; i < 2; i++) {
            PlayerController.getInstance().keyDown(Input.Keys.S);
            PlayerController.getInstance().keyUp(Input.Keys.S);
        }
        for (int i = 0; i < 10; i++) GameController.getInstance().applyNextPendingGSU();

        // ASSERT
        Assertions.assertEquals(previousPos, PlayerController.getInstance().getPlayerPosition());
    }

    @Test
    public void testMove_NOK_InvalidKey() {
        // ARRANGE
        Vector2 previousPos = PlayerController.getInstance().getPlayerPosition();
        Vector2 expectedPos = previousPos.add(Direction.NORTH.getVector());

        // ACT
        PlayerController.getInstance().keyDown(Input.Keys.X);
        PlayerController.getInstance().keyUp(Input.Keys.X);
        PlayerController.getInstance().keyDown(Input.Keys.X);
        PlayerController.getInstance().keyUp(Input.Keys.X);
        GameController.getInstance().applyNextPendingGSU();
        GameController.getInstance().applyNextPendingGSU();

        // ASSERT
        Assertions.assertEquals(expectedPos, PlayerController.getInstance().getPlayerPosition());
    }
}
