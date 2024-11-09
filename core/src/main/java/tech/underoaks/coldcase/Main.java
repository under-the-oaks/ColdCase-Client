package tech.underoaks.coldcase;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import tech.underoaks.coldcase.data.Map;

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

        Map map = Map.getMap(Path.of("maps/test_plain"));
        gameController = GameController.getInstance();
        gameController.setCurrentMap(map);

        gameController.triggerAction(
            new Vector2(0, 0),
            new Vector2(1,2)
        );
    }



    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        float deltaTime = Gdx.graphics.getDeltaTime();
        int fps = Gdx.graphics.getFramesPerSecond();

        timeSinceLastLog += deltaTime;

        if (timeSinceLastLog >= 2f) {
            System.out.println("FPS: " + fps);
            timeSinceLastLog = 0f;
        }

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
        batch.dispose();
    }
}
