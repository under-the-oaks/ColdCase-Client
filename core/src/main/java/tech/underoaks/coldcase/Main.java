package tech.underoaks.coldcase;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import tech.underoaks.coldcase.game.GameController;
import tech.underoaks.coldcase.game.PlayerController;
import tech.underoaks.coldcase.remote.WebSocketClient;
import tech.underoaks.coldcase.state.Map;
import tech.underoaks.coldcase.state.tileContent.ItemObject;
import tech.underoaks.coldcase.state.tileContent.Player;

import java.nio.file.Path;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private GameController gameController;
    private ExtendViewport viewport;
    private float timeSinceLastLog = 0f;
    private float timeSinceLastGSUCheck = 0f;

    @Override
    public void create() {
        batch = new SpriteBatch();
        viewport = new ExtendViewport(10000, 10000);

        Map map = MapGenerator.serializeContentToMap(Path.of("maps/Map_HoleDemo"), false);
        Gdx.input.setInputProcessor(PlayerController.getInstance());

        gameController = GameController.getInstance();
        gameController.setCurrentMap(map);

        PlayerController.getInstance().setPlayerPosition(gameController.getCurrentMap().getTileContentByType(Player.class));
        WebSocketClient.getInstance();
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        float deltaTime = Gdx.graphics.getDeltaTime();
        int fps = Gdx.graphics.getFramesPerSecond();

        timeSinceLastLog += deltaTime;

        if (timeSinceLastLog >= 0.5f) {
            //System.out.println("FPS: " + fps);
            timeSinceLastLog = 0f;
        }

        timeSinceLastGSUCheck += deltaTime;

        if (timeSinceLastGSUCheck >= 0.1f) {
            GameController.getInstance().applyNextPendingGSU();
            timeSinceLastGSUCheck = 0f;
        }

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        gameController.getCurrentMap().render(batch);

        renderInventory(1000, new Vector2(0,0) );

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        gameController.getCurrentMap().dispose();
        batch.dispose();
    }

    void renderInventory( float inventoryDimension, Vector2 inventoryOffset ) {

        batch.draw( new Texture("./isometric tileset/separated images/TEST_INVENTORY.png") , -5000 + inventoryOffset.x + (inventoryDimension / 2), -5000 + inventoryOffset.y + (inventoryDimension / 2), inventoryDimension, inventoryDimension);

        if ( PlayerController.getInstance().getInventory() != null ) {

            ItemObject item = (ItemObject) PlayerController.getInstance().getInventory();

            Texture uiTexture = item.getInventoryTexture();

            if ( uiTexture != null ) {

                batch.draw( uiTexture , -400 + inventoryOffset.x + (inventoryDimension / 2), -400 + inventoryOffset.y + (inventoryDimension / 2), inventoryDimension, inventoryDimension);

            }
        }
    }
}
