package tech.underoaks.coldcase;

import com.badlogic.gdx.Game;
import tech.underoaks.coldcase.game.GameController;
import tech.underoaks.coldcase.game.TextureController;
import tech.underoaks.coldcase.game.TextureFactory;
import tech.underoaks.coldcase.remote.WebSocketClient;
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
    private final Properties properties = new Properties();

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

        // Stage Management
        StageManager.create(this);
        StageManager.getInstance().showScreen(Stages.GAME);


        // Multiplayer setup
        setupWebSocketConnection();
    }

    @Override
    public void dispose() {
        super.dispose();
        GameController.getInstance().getCurrentMap().dispose();
    }

    void setupWebSocketConnection() {
        if (!properties.containsKey("websocket_url") || !properties.containsKey("session_id")) {
            throw new RuntimeException("Missing websocket url or map_override property");
        }
        WebSocketClient.create(properties.getProperty("websocket_url"), properties.getProperty("session_id"));
    }
}
