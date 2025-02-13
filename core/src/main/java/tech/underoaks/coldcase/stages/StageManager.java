package tech.underoaks.coldcase.stages;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;

/**
 * Manages the different screens (stages) of the game.
 * <p>
 * This class is responsible for handling transitions between various stages, such as the main menu, gameplay,
 * settings, etc. It employs a singleton pattern to ensure only one instance exists throughout the game's lifecycle.
 * The {@code StageManager} must be initialized via {@link #create(Game)} before it can be accessed using {@link #getInstance()}.
 * </p>
 */
public class StageManager {

    private Stages nextStage;
    private Object[] nextStageParams;
    private static StageManager instance;

    private AbstractStage currentStage;

    /**
     * Returns the singleton instance of the {@code StageManager}.
     * <p>
     * This method throws an {@code IllegalStateException} if the {@code StageManager} has not been created yet.
     * </p>
     *
     * @return the singleton {@code StageManager} instance
     * @throws IllegalStateException if the {@code StageManager} is not initialized
     */
    public static StageManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("TextureController not initialized");
        }
        return instance;
    }

    private Game game;

    /**
     * Creates a new {@code StageManager} instance with the specified {@link Game} instance.
     * <p>
     * This method must be called before any other calls to {@code StageManager.getInstance()}.
     * If a {@code StageManager} instance already exists, an {@code IllegalStateException} is thrown.
     * </p>
     *
     * @param game the {@link Game} instance to associate with this {@code StageManager}
     * @return the newly created {@code StageManager} instance
     * @throws IllegalStateException if a {@code StageManager} is already initialized
     */
    public static StageManager create(Game game) {
        if (instance != null) {
            throw new IllegalStateException("TextureController already initialized");
        }
        instance = new StageManager();
        instance.game = game;
        return instance;
    }

    /**
     * Displays the screen corresponding to the given stage.
     * <p>
     * This method retrieves the new stage from the {@link Stages} enum using the provided parameters,
     * builds the stage with an {@link InputMultiplexer}, sets it as the current screen in the {@link Game},
     * and disposes the previous stage if it exists.
     * </p>
     *
     * @param stage  the {@link Stages} enum value representing the stage to be shown
     * @param params optional parameters required to build the stage
     */
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

    /**
     * Sets the next stage to be displayed along with any parameters required to build it.
     * <p>
     * The specified stage and parameters will be applied on the next call to {@code update()}.
     * </p>
     *
     * @param nextStage the {@link Stages} enum value representing the next stage to display
     * @param params    optional parameters required by the stage's screen builder
     */
    public void setNextStage(Stages nextStage, Object... params) {
        this.nextStage = nextStage;
        this.nextStageParams = params;
    }

    /**
     * Checks if a next stage has been set and, if so, transitions to that stage.
     * <p>
     * If a next stage is defined, this method calls {@code showScreen} with the provided stage and parameters,
     * and then resets the next stage variables.<br>
     * This is done because the context in which {@code showScreen} can be called doesn't always have the required libGDX context to draw in the application window.
     * This means that update should only be called from within the scope of the Game class.
     * </p>
     */
    public void update() {
        if (nextStage != null) {
            showScreen(nextStage, nextStageParams);
            nextStage = null;
            nextStageParams = null;
        }
    }

    /**
     * Returns the current stage (screen) being displayed by the game.
     *
     * @return the current {@link AbstractStage} or {@code null} if no stage is currently set
     */
    public AbstractStage getCurrentStage() {
        return currentStage;
    }
}
