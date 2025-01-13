package tech.underoaks.coldcase.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.Screen;

/**
 * Abstract class representing a stage in the game. A stage is a container for actors
 * that handles input, rendering, and updates. This class implements the {@link Screen} interface
 * and provides a foundation for custom stages in the game.
 * <p>
 * Subclasses must implement the {@link #buildStage(InputMultiplexer)} method to add actors
 * and configure input processors. The `render` method handles screen rendering and updates
 * the stage.
 * </p>
 * @author mabe.edu
 * @coauthor jean874
 */
public abstract class AbstractStage extends Stage implements Screen {

    protected AbstractStage() {
        super( new ExtendViewport(19200, 10800));
    }

    // Subclasses must load actors in this method
    public abstract void buildStage(InputMultiplexer inputMultiplexer);


    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        // Calling to Stage methods
        super.draw();
        super.act(delta);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void resize(int width, int height) {
        getViewport().update(width,height,true);
    }

    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}

    @Override
    public boolean keyDown(int keyCode) {
        return super.keyDown(keyCode);
    }

    /**
     * This method is called when the stage successfully establishes a connection.
     * It can be overridden in subclasses to handle specific actions upon connection.
     */
    public void onConnected(){}

    /**
     * This method is called when the stage is disconnected.
     * It can be overridden in subclasses to handle specific actions upon disconnection.
     */
    public void onDisconnected(){}
}
