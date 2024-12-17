package tech.underoaks.coldcase.stages;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class StageManager {

    private static StageManager instance;
    public static StageManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("TextureController not initialized");
        }
        return instance;
    }

    private Game game;

    public static StageManager create(Game game) {
        if (instance != null) {
            throw new IllegalStateException("TextureController already initialized");
        }
        instance = new StageManager();
        instance.game = game;
        return instance;
    }

    // Show in the game the screen which enum type is received
    public void showScreen(Stages stage, Object... params) {

        // Get current screen to dispose it
        Screen currentStage = game.getScreen();

        // Show new screen
        AbstractStage newStage = stage.getScreen(params);
        newStage.buildStage();
        game.setScreen(newStage);

        // Dispose previous stage
        if (currentStage != null) {
            currentStage.dispose();
        }
    }

}
