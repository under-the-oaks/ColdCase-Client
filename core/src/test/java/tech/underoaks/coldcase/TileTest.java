package tech.underoaks.coldcase;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.headless.HeadlessApplication;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.underoaks.coldcase.data.tileContent.TileContent;
import tech.underoaks.coldcase.data.tiles.GroundTile;
import tech.underoaks.coldcase.data.tiles.Tile;
import tech.underoaks.coldcase.loader.enums.Direction;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TileTest{

    ApplicationListener game;

    @BeforeEach
    public void setUp(){
        game = new HeadlessApplicationListener();
    }

    static Texture mockTexture;

    @Test
    public void GetSetTileContentTest(){

        mockTexture = mock(Texture.class);
        when(mockTexture.getWidth()).thenReturn(32);
        when(mockTexture.getHeight()).thenReturn(32);

        class TestTile extends Tile {

            public TestTile() {
                super(mockTexture);
            }

        }

        class TestTileContent extends TileContent {

            public TestTileContent() {
                super(mockTexture, false, false);
            }

            @Override
            public boolean action(InteractionChain chain, Vector2 tilePosition, Direction actionDirection) throws GameStateUpdateException {
                return false;
            }

            @Override
            public boolean update(InteractionChain chain, Vector2 tilePosition) throws GameStateUpdateException {
                return false;
            }
        }

        Tile tile = new TestTile();

        TileContent tileContent = new TestTileContent();

        tile.setTileContent(tileContent);

        Assertions.assertEquals(tileContent, tile.getTileContent());
    }

}
