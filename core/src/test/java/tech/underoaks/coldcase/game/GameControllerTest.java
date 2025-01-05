package tech.underoaks.coldcase.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.*;
import org.mockito.MockedConstruction;
import tech.underoaks.coldcase.HeadlessApplicationListener;
import tech.underoaks.coldcase.MapGenerator;
import tech.underoaks.coldcase.remote.RemoteGameController;
import tech.underoaks.coldcase.state.InteractionChain;
import tech.underoaks.coldcase.state.Map;
import tech.underoaks.coldcase.state.Snapshot;
import tech.underoaks.coldcase.state.tileContent.GloveItem;
import tech.underoaks.coldcase.state.tileContent.Player;
import tech.underoaks.coldcase.state.tileContent.TileContent;
import tech.underoaks.coldcase.state.tiles.Tile;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class GameControllerTest {
    private HeadlessApplicationListener game;

    @BeforeAll
    public static void beforeAll() {
        TextureFactory mockTextureFactory = mock(TextureFactory.class);
        when(mockTextureFactory.create(anyString())).thenReturn(mock(Texture.class));
        TextureController.create(false, mockTextureFactory);
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
    public void getGameControllerTest() {
        Assertions.assertNotNull(game);
        Assertions.assertNotNull(GameController.getInstance());
        Assertions.assertSame(GameController.getInstance(), game.gameController);
    }

    @Test
    public void triggerActionTest_local_noHandler() {
        Interaction interaction = new Interaction(
            new Vector2(0, 0),
            Direction.EAST,
            Player.class
        );

        Map expectedMap = game.gameController.getCurrentMap();
        Assertions.assertFalse(game.gameController.triggerAction(interaction));
        Assertions.assertEquals(expectedMap, game.gameController.getCurrentMap());
    }

    @Test
    public void triggerActionTest_local_outOfBounds() {
        Interaction interaction = new Interaction(
            new Vector2(-1, -1),
            Direction.EAST,
            Player.class
        );

        Map expectedMap = game.gameController.getCurrentMap();
        Assertions.assertFalse(game.gameController.triggerAction(interaction));
        Assertions.assertEquals(expectedMap, game.gameController.getCurrentMap());
    }

    @Test
    public void triggerActionTest_local_noTileContent() {
        Interaction interaction = new Interaction(
            new Vector2(0, 1),
            Direction.EAST,
            Player.class
        );

        Map expectedMap = game.gameController.getCurrentMap();
        Assertions.assertFalse(game.gameController.triggerAction(interaction));
        Assertions.assertEquals(expectedMap, game.gameController.getCurrentMap());
    }

    @Test
    public void triggerActionTest_local_changedState() {
        Interaction interaction = new Interaction(
            new Vector2(3, 1),
            Direction.EAST,
            Player.class
        );

        Map expectedMap = game.gameController.getCurrentMap();
        Snapshot expectedSnapshot = new Snapshot(expectedMap);
        InteractionChain expectedChain = new InteractionChain(expectedSnapshot);


        TileContent expectedHandler = expectedMap.getTile(1,3).topTileContent();
        TileContent handler = GameController.triggerLocalAction(expectedChain, interaction);
        Assertions.assertNotNull(handler);
        Assertions.assertEquals(expectedHandler.getClass(), handler.getClass());
    }

    @Test
    public void triggerActionTest_local_tileContentDidNotHandle() {
        Interaction interaction = new Interaction(
            new Vector2(0, 4),
            Direction.EAST,
            Player.class
        );

        Map expectedMap = game.gameController.getCurrentMap();
        Snapshot expectedSnapshot = new Snapshot(expectedMap);
        InteractionChain expectedChain = new InteractionChain(expectedSnapshot);

        TileContent handler = GameController.triggerLocalAction(expectedChain, interaction);
        Assertions.assertNull(handler);
    }

    @Test
    public void triggerActionTest_remote_noTranscendent() {
        PlayerController.getInstance().setInventory(new GloveItem());
        Interaction interaction = new Interaction(
            new Vector2(8, 2),
            Direction.EAST,
            Player.class
        );

        Map expectedMap = game.gameController.getCurrentMap();
        Assertions.assertTrue(GameController.getInstance().triggerAction(interaction));
        Assertions.assertEquals(expectedMap, game.gameController.getCurrentMap());
    }

    @Test
    public void triggerActionTest_remote_noHandler() {
        PlayerController.getInstance().setInventory(new GloveItem());
        Interaction interaction = new Interaction(
            new Vector2(6, 2),
            Direction.EAST,
            Player.class
        );

        try(MockedConstruction<RemoteGameController> ignored =
            mockConstruction(RemoteGameController.class, (mock, context) -> when(mock.triggerAction(any(Interaction.class), anyBoolean())).thenReturn(null))) {
            Map expectedMap = game.gameController.getCurrentMap();
            Assertions.assertFalse(GameController.getInstance().triggerAction(interaction));
            Assertions.assertEquals(expectedMap, game.gameController.getCurrentMap());
        }
    }

    @Test
    public void triggerActionTest_remote_noFollowUp() {
        PlayerController.getInstance().setInventory(new GloveItem());
        Interaction interaction = new Interaction(
            new Vector2(6, 2),
            Direction.EAST,
            Player.class
        );

        try(MockedConstruction<RemoteGameController> ignored =
                mockConstruction(RemoteGameController.class, (mock, context) -> when(mock.triggerAction(any(Interaction.class), anyBoolean())).thenReturn(new LinkedList<>()))) {
            Map expectedMap = game.gameController.getCurrentMap();
            Assertions.assertTrue(GameController.getInstance().triggerAction(interaction));
            Assertions.assertEquals(expectedMap, game.gameController.getCurrentMap());
        }
    }

    @Test
    public void triggerActionTest_remote_suppressFollowUp() {
        PlayerController.getInstance().setInventory(new GloveItem());
        Interaction interaction = new Interaction(
            new Vector2(6, 2),
            Direction.EAST,
            Player.class
        );

        Map expectedMap = game.gameController.getCurrentMap();
        Snapshot expectedSnapshot = new Snapshot(expectedMap);
        InteractionChain expectedChain = new InteractionChain(expectedSnapshot);

        try(MockedConstruction<RemoteGameController> ignored =
                mockConstruction(RemoteGameController.class, (mock, context) -> when(mock.triggerAction(any(Interaction.class), anyBoolean())).thenReturn(null))) {
            Assertions.assertTrue(GameController.triggerAction(expectedChain, interaction, true));
        }
    }

    @Test
    public void triggerRemoteActionTest_followUp() {
        Map expectedMap = game.gameController.getCurrentMap();
        Snapshot expectedSnapshot = new Snapshot(expectedMap);
        InteractionChain expectedChain = new InteractionChain(expectedSnapshot);

        Interaction interactionA = new Interaction();
        Interaction interactionB = new Interaction(
            new Vector2(-1, -1),
            Direction.NORTH,
            Player.class
        );

        try(MockedConstruction<RemoteGameController> ignored =
                mockConstruction(RemoteGameController.class, (mock, context) -> {
                    Queue<Interaction> queue = new LinkedList<>();
                    queue.add(interactionB);
                    when(mock.triggerAction(any(Interaction.class))).thenReturn(queue);
                })) {
            GameController.triggerRemoteAction(expectedChain, interactionA);
            Assertions.assertNotNull(expectedChain.getPendingRemoteActions());
            Assertions.assertEquals(1, expectedChain.getPendingRemoteActions().size());
            Assertions.assertEquals(interactionB, expectedChain.getPendingRemoteActions().poll());
        }
    }

    @Test
    public void triggerRemoteActionTest_noFollowUp() {
        Map expectedMap = game.gameController.getCurrentMap();
        Snapshot expectedSnapshot = new Snapshot(expectedMap);
        InteractionChain expectedChain = new InteractionChain(expectedSnapshot);

        Interaction interactionA = new Interaction();

        try(MockedConstruction<RemoteGameController> ignored =
                mockConstruction(RemoteGameController.class, (mock, context) -> when(mock.triggerAction(any(Interaction.class))).thenReturn(null))) {
            GameController.triggerRemoteAction(expectedChain, interactionA);
            Assertions.assertNotNull(expectedChain.getPendingRemoteActions());
            Assertions.assertEquals(0, expectedChain.getPendingRemoteActions().size());
        }
    }

    @Test
    public void isPendingGSUQueueEmptyTest_Empty() {
        Assertions.assertTrue(GameController.getInstance().isPendingGSUQueueEmpty());
    }

    @Test
    public void isPendingGSUQueueEmptyTest_NotEmpty() {
        PlayerController.getInstance().setInventory(new GloveItem());
        Interaction interaction = new Interaction(
            new Vector2(8, 2),
            Direction.EAST,
            Player.class
        );

        Assertions.assertTrue(GameController.getInstance().triggerAction(interaction));
        Assertions.assertFalse(GameController.getInstance().isPendingGSUQueueEmpty());
    }

    @Test
    public void handleCreateRemoteInteractionTest() {
        Assertions.assertThrows(RuntimeException.class, () -> GameController.getInstance().handleApplyRemoteGSUsMessage());
        GameController.getInstance().handleCreateRemoteInteractionChain();
        Assertions.assertDoesNotThrow(() -> GameController.getInstance().handleApplyRemoteGSUsMessage());
    }

    @Test
    public void handleApplyRemoteGSUsMessageTest_AlreadyRunning() {
        Assertions.assertDoesNotThrow(() -> GameController.getInstance().handleCreateRemoteInteractionChain());
        Assertions.assertThrows(RuntimeException.class, () -> GameController.getInstance().handleCreateRemoteInteractionChain());
    }

    @Test
    public void handleAbortRemoteGSUsMessageTest() {
        Assertions.assertDoesNotThrow(() -> GameController.getInstance().handleCreateRemoteInteractionChain());
        GameController.getInstance().handleAbortRemoteGSUsMessage();
        Assertions.assertDoesNotThrow(() -> GameController.getInstance().handleCreateRemoteInteractionChain());
    }

    @Test
    public void handleTriggerRemoteInteractionTest_NoResult() {
        Interaction interaction = new Interaction(
            new Vector2(-1, -1),
            Direction.EAST,
            Player.class
        );

        GameController.getInstance().handleCreateRemoteInteractionChain();
        Assertions.assertNull(
            GameController.getInstance().handleTriggerRemoteInteraction(interaction, false)
        );
    }

    @Test
    public void handleTriggerRemoteInteractionTest_NoFollowUp() {
        PlayerController.getInstance().setInventory(new GloveItem());
        Interaction interaction = new Interaction(
            new Vector2(8, 2),
            Direction.EAST,
            Player.class
        );

        GameController.getInstance().handleCreateRemoteInteractionChain();
        Assertions.assertNotNull(
            GameController.getInstance().handleTriggerRemoteInteraction(interaction, false)
        );

        GameController.getInstance().handleApplyRemoteGSUsMessage();
        Assertions.assertFalse(GameController.getInstance().isPendingGSUQueueEmpty());
    }

    @Test
    public void applyNextPendingGSUTest_Empty() {
        Map expectedMap = game.gameController.getCurrentMap();
        GameController.getInstance().applyNextPendingGSU();
        Assertions.assertEquals(expectedMap, game.gameController.getCurrentMap());
    }

    @Test
    public void applyNextPendingGSUTest_NotEmpty() {
        Map expectedMap = game.gameController.getCurrentMap().deepClone();

        PlayerController.getInstance().setInventory(new GloveItem());
        Interaction interaction = new Interaction(
            new Vector2(8, 2),
            Direction.EAST,
            Player.class
        );

        Assertions.assertTrue(GameController.getInstance().triggerAction(interaction));
        Assertions.assertFalse(GameController.getInstance().isPendingGSUQueueEmpty());

        GameController.getInstance().applyNextPendingGSU();
        Assertions.assertTrue(GameController.getInstance().isPendingGSUQueueEmpty());

        Tile[][] expectedTiles = expectedMap.tileArray;
        Tile[][] actualTiles = game.gameController.getCurrentMap().tileArray;

        for (int i = 0; i < expectedTiles.length; i++) {
            for (int j = 0; j < expectedTiles[i].length; j++) {
                Class<? extends TileContent> expected = null;
                if(expectedTiles[i][j].getTileContent() != null){
                    expected = expectedTiles[i][j].getTileContent().getClass();
                }

                Class<? extends TileContent> actual = null;
                if(actualTiles[i][j].getTileContent() != null){
                    actual = actualTiles[i][j].getTileContent().getClass();
                }

                if(i == 8 && j == 2 || i == 8 && j == 3){
                    Assertions.assertNotEquals(expected, actual);
                }
                else {
                    Assertions.assertEquals(expected, actual, "Error at index [" + i + "][" + j + "]");
                }
            }
        }
    }
}
