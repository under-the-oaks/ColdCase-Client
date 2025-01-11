package tech.underoaks.coldcase;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import tech.underoaks.coldcase.game.PlayerController;
import tech.underoaks.coldcase.game.TextureController;
import tech.underoaks.coldcase.game.TextureFactory;
import tech.underoaks.coldcase.game.UITextureController;
import tech.underoaks.coldcase.stages.StageManager;
import tech.underoaks.coldcase.stages.Stages;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * {@link com.badlogic.gdx.Game} implementation shared by all platforms.
 */
public class Main extends Game {
    private static final String propertiesPath = ".properties";
    private static final Properties properties = new Properties();
    private float fixedUpdateClock = 0f;

    @Override
    public void create() {
        //load properties from file
        try {
            properties.load(new FileInputStream(propertiesPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Texture Management
        if(!TextureController.exists()) {
            TextureController.create(true, new TextureFactory());
        }
        if(!UITextureController.exists()) {
            UITextureController.create(new TextureFactory());
        }

        // Stage Management
        StageManager.create(this);
        StageManager.getInstance().showScreen(Stages.MAIN_MENU);
    }

    public void render() {
        super.render();
        float delta = Gdx.graphics.getDeltaTime();
        fixedUpdate(delta);
    }


    /**
     * Fixed update method to trigger Methods every 0.1 seconds
     * This is supposed to be used for Loading and Menu Logic.
     * The Game Stage has its own fixed update method.
     *
     * @param delta time since last frame
     */
    private void fixedUpdate(float delta) {
        fixedUpdateClock += delta;
        if (fixedUpdateClock >= 0.075f) {
            fixedUpdateClock = 0f;

            // all fixed update methods below
            StageManager.getInstance().update();
            PlayerController.getInstance().update();

        }
    }

    public static Properties getProperties() {
        return properties;
    }
}
