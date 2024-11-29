package tech.underoaks.coldcase;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import tech.underoaks.coldcase.game.GameController;
import tech.underoaks.coldcase.game.PlayerController;
import tech.underoaks.coldcase.remote.WebSocketClient;
import tech.underoaks.coldcase.state.Map;
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

    @Override
    public void create() {
        batch = new SpriteBatch();
        viewport = new ExtendViewport(800, 800);

        Map map = MapGenerator.serializeContentToMap(Path.of("maps/Map_MovableBlockDemo"), true);
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

        gameController.applyNextPendingGSU();

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        gameController.getCurrentMap().render(batch);

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
}
