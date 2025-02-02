package tech.underoaks.coldcase.stages;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import tech.underoaks.coldcase.game.TextureController;

public class StageManager {

    private Stages nextStage;
    private Object[] nextStageParams;
    private static StageManager instance;

    private AbstractStage currentStage;

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
        currentStage = (AbstractStage) game.getScreen();

        InputMultiplexer inputMultiplexer = new InputMultiplexer();

        // Show new screen
        AbstractStage newStage = stage.getScreen(params);
        newStage.buildStage(inputMultiplexer);
        game.setScreen(newStage);

        inputMultiplexer.addProcessor(newStage);
        Gdx.input.setInputProcessor(inputMultiplexer);

        // Dispose previous stage
        if (currentStage != null) {
            currentStage.dispose();
        }
        currentStage = newStage;
    }

    public void setNextStage(Stages nextStage, Object... params) {
        this.nextStage = nextStage;
        this.nextStageParams = params;
    }

    public void update() {
        if (nextStage != null) {
            showScreen(nextStage,nextStageParams);
            nextStage = null;
            nextStageParams = null;
        }
    }

    public AbstractStage getCurrentStage() {
        return currentStage;
    }
}
