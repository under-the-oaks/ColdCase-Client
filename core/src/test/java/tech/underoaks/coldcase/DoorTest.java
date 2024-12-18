package tech.underoaks.coldcase;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.underoaks.coldcase.game.Direction;
import tech.underoaks.coldcase.game.Interaction;
import tech.underoaks.coldcase.game.TextureController;
import tech.underoaks.coldcase.state.InteractionChain;
import tech.underoaks.coldcase.state.Map;
import tech.underoaks.coldcase.state.Snapshot;
import tech.underoaks.coldcase.state.tileContent.Door;
import tech.underoaks.coldcase.state.tileContent.ItemObject;
import tech.underoaks.coldcase.state.tileContent.TileContent;
import tech.underoaks.coldcase.state.tiles.Tile;
import tech.underoaks.coldcase.state.updates.GameStateUpdateException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DoorTest {
    static HeadlessApplicationListener game;
    Door testDoor;
    Texture openTexture;
    Texture closeTexture;
    TextureController textureController;

    @BeforeEach
    public void setUp() {
        Texture texture = mock(Texture.class);
        game = new HeadlessApplicationListener();
        openTexture = mock(Texture.class);
        closeTexture = mock(Texture.class);

        textureController = mock(TextureController.class);
        when(TextureController.getInstance()).thenReturn(textureController);
        when(textureController.getDoorClosedTexture()).thenReturn(openTexture);
        when(textureController.getDoorOpenTexture()).thenReturn(closeTexture);


    }

    @Test
    public void ActionTest(){



        InteractionChain chain = mock(InteractionChain.class);
        Interaction interaction = mock(Interaction.class);




        testDoor = new Door();


        try {
            testDoor.action(chain,interaction);
            Assertions.assertEquals(testDoor.getTexture(),openTexture);

        } catch (GameStateUpdateException e) {
            throw new RuntimeException(e);
        }


    }
}
