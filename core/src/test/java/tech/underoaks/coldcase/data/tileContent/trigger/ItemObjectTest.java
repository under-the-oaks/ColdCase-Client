package tech.underoaks.coldcase.data.tileContent.trigger;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.underoaks.coldcase.HeadlessApplicationListener;
import tech.underoaks.coldcase.game.Direction;
import tech.underoaks.coldcase.game.PlayerController;
import tech.underoaks.coldcase.state.InteractionChain;
import tech.underoaks.coldcase.state.Map;
import tech.underoaks.coldcase.state.Snapshot;
import tech.underoaks.coldcase.state.tileContent.ItemObject;
import tech.underoaks.coldcase.state.tileContent.TileContent;
import tech.underoaks.coldcase.state.tiles.Tile;
import tech.underoaks.coldcase.state.updates.GameStateUpdateException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemObjectTest {

    static HeadlessApplicationListener game;

    @BeforeEach
    public void setUp() {
        game = new HeadlessApplicationListener();
    }

    @Test
    public void ActionTest() {

        Tile tile = mock(Tile.class);
        TileContent tileContent = mock(TileContent.class);
        Texture texture = mock(Texture.class);
        InteractionChain chain = mock(InteractionChain.class);
        Vector2 tilePosition = new Vector2(0, 0);
        Direction direction = Direction.NORTH;

        Snapshot snapshot = mock(Snapshot.class);

        Map map = new Map(new Tile[1][1]);
        map.setTile(0,0, tile);

        ItemObject itemObject = new ItemObject(texture);

        when ( chain.getSnapshot() ).thenReturn(snapshot);
        when ( snapshot.getSnapshotMap() ).thenReturn(map);
        when ( tile.getTileContent() ).thenReturn(itemObject);

        try {

            // Pick Up Item

            Assertions.assertNull( PlayerController.getInstance().getInventory() );
            Assertions.assertTrue( itemObject.action( chain, tilePosition, direction ) );
            Assertions.assertNotNull( PlayerController.getInstance().getInventory() );

            // Swap Item

            itemObject = new ItemObject(texture);

            TileContent before = PlayerController.getInstance().getInventory();

            Assertions.assertTrue( itemObject.action( chain, tilePosition, direction ) );

            TileContent after = PlayerController.getInstance().getInventory();

            Assertions.assertNotEquals( before, after );

        }
        catch (GameStateUpdateException e) {

            e.printStackTrace();

        }
    }

}
