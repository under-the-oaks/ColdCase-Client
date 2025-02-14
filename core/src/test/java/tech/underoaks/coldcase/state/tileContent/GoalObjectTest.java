package tech.underoaks.coldcase.state.tileContent;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.*;
import tech.underoaks.coldcase.MapGenerator;
import tech.underoaks.coldcase.game.*;
import tech.underoaks.coldcase.state.InteractionChain;
import tech.underoaks.coldcase.state.Map;
import tech.underoaks.coldcase.state.Snapshot;
import tech.underoaks.coldcase.state.tiles.GroundTile;
import tech.underoaks.coldcase.state.tiles.Tile;
import tech.underoaks.coldcase.state.updates.EndLevelUpdate;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class GoalObjectTest {
    private TileContent goalObj;
    private Map map;

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
        map = MapGenerator.serializeContentToMap(Path.of(
            Objects.requireNonNull(getClass().getClassLoader().getResource("Map_TestEmpty")).toURI()
        ), true);
        goalObj = new GoalObject();
        Tile tileWithGoal = new GroundTile();
        tileWithGoal.setTileContent(goalObj);
        map.setTile(0, 0, tileWithGoal);

    }

    @AfterEach
    public void afterEach() {
        GameController.destroy();
        PlayerController.destroy();
    }

    @Test
    public void testGoalObjectAction_PlayerOnTile() throws Exception {
        PlayerController.getInstance().setPlayerPosition(new Vector2(0,0));
        Tile tileWithPlayer = new GroundTile();
        tileWithPlayer.setTileContent(new Player());
        map.setTile(0,0, tileWithPlayer);

        InteractionChain mockChain = mock(InteractionChain.class);
        doNothing().when(mockChain).addGameStateUpdate(any());
        when(mockChain.getSnapshot()).thenReturn(new Snapshot(map));

        boolean result = goalObj.action(mockChain, new Interaction(new Vector2(0, 0), Direction.NORTH, Player.class));

        Assertions.assertTrue(result, "Action should succeed when player is on the goal tile");
        verify(mockChain).addGameStateUpdate(any(EndLevelUpdate.class));
    }

    @Test
    public void testGoalObjectAction_PlayerNextToTile() throws Exception {
        PlayerController.getInstance().setPlayerPosition(new Vector2(1,0));
        Tile tileWithPlayer = new GroundTile();
        tileWithPlayer.setTileContent(new Player());
        map.setTile(1,0, tileWithPlayer);

        InteractionChain mockChain = mock(InteractionChain.class);
        doNothing().when(mockChain).addGameStateUpdate(any());
        when(mockChain.getSnapshot()).thenReturn(new Snapshot(map));

        boolean result = goalObj.action(mockChain, new Interaction(new Vector2(0, 1), Direction.NORTH, Player.class));

        Assertions.assertTrue(result, "Action should succeed when player is next to the goal tile");
        verify(mockChain).addGameStateUpdate(any(EndLevelUpdate.class));
    }

    @Test
    public void testGoalObjectAction_PlayerFarAway() throws Exception {
        PlayerController.getInstance().setPlayerPosition(new Vector2(1,0));
        Tile tileWithPlayer = new GroundTile();
        tileWithPlayer.setTileContent(new Player());
        map.setTile(3,0, tileWithPlayer);

        InteractionChain mockChain = mock(InteractionChain.class);
        doNothing().when(mockChain).addGameStateUpdate(any());
        when(mockChain.getSnapshot()).thenReturn(new Snapshot(map));

        boolean result = goalObj.action(mockChain, new Interaction(new Vector2(5, 5), Direction.NORTH, Player.class));

        Assertions.assertFalse(result, "Action should fail when player is far from the goal tile");
        verify(mockChain, never()).addGameStateUpdate(any());
    }
}


