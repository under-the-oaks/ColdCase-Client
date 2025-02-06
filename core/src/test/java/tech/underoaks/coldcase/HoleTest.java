package tech.underoaks.coldcase;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.*;
import tech.underoaks.coldcase.game.*;
import tech.underoaks.coldcase.state.*;
import tech.underoaks.coldcase.state.tileContent.*;
import tech.underoaks.coldcase.state.tiles.GroundTile;
import tech.underoaks.coldcase.state.tiles.Tile;
import tech.underoaks.coldcase.state.updates.RemoveTileContentUpdate;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
/**
 * HoleTest Class
 * The tests cover the following scenarios:
 * - Checking if a MovableBlock is accepted by the hole.
 * - Ensuring that the hole removes the block upon interaction.
 * Author: jean874
 */
public class HoleTest {
    private TileContent holeObj;
    private MovableBlock block;
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
                Objects.requireNonNull(getClass().getClassLoader().getResource("Map_Test")).toURI()
        ), true);
        holeObj = new Hole();
        block = new MovableBlock();
        Tile tileWithHole = new GroundTile();
        tileWithHole.setTileContent(holeObj);
        Tile tileWithBlock = new GroundTile();
        tileWithBlock.setTileContent(block);
        map.setTile(0,0,tileWithHole);
        map.setTile(0,1,tileWithBlock);
    }

    @AfterEach
    public void afterEach() {
        GameController.destroy();
        PlayerController.destroy();
    }

    @Test
    public void acceptMovableBlock() throws Exception {
        InteractionChain mockChain = mock(InteractionChain.class);
        doNothing().when(mockChain).addGameStateUpdate(any());
        when(mockChain.getSnapshot()).thenReturn(new Snapshot(map));

        boolean result = holeObj.update(mockChain, new Vector2(0, 0),
                new Interaction(new Vector2(1, 0), Direction.NORTH, Player.class), block);

        Assertions.assertTrue(result);

        result = holeObj.update(mockChain, new Vector2(0, 0),
                new Interaction(new Vector2(1, 0), Direction.NORTH, Player.class), new MovableBlockTranscendent());

        verify(mockChain, times(4)).addGameStateUpdate(any(RemoveTileContentUpdate.class));
        Assertions.assertTrue(result);
    }
}
