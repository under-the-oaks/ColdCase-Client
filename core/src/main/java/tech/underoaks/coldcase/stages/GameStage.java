package tech.underoaks.coldcase.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import tech.underoaks.coldcase.game.GameController;
import tech.underoaks.coldcase.game.Levels;
import tech.underoaks.coldcase.game.PlayerController;
import tech.underoaks.coldcase.stages.actors.InventoryActor;
import tech.underoaks.coldcase.stages.actors.MapActor;

/**
 * GameStage class -> initializes alle game relevant actors and handles fixed updates
 */
public class GameStage extends AbstractStage {
    private final String path;
    private float fixedUpdateClock = 0f;
    MapActor mapActor;

    GameStage(Levels level) {
        super();
        if(level == null){
            this.path = "maps/Map_GoalDemo";
        }else {
            this.path = level.getMapPath();
        }
    }

    @Override
    public void buildStage(InputMultiplexer inputMultiplexer) {
        Gdx.input.setInputProcessor(PlayerController.getInstance());

        mapActor = new MapActor(path);

        mapActor.setOrigin(getWidth() / 2, getHeight() / 2);
        addActor(mapActor);

        InventoryActor inventoryActor = new InventoryActor();
        inventoryActor.setPosition(100, 100);
        inventoryActor.setSize(2000, 2000);
        addActor(inventoryActor);

        inputMultiplexer.addProcessor(PlayerController.getInstance());
    }


    @Override
    public void render(float delta) {
        super.render(delta);

        fixedUpdate(delta);

        getBatch().begin();
        //renderFPS(getBatch());
        getBatch().end();

    }

    /**
     * Fixed update method to trigger Methods every 0.1 seconds
     *
     * @param delta time since last frame
     */
    private void fixedUpdate(float delta) {
        fixedUpdateClock += delta;
        if (fixedUpdateClock >= 0.1f) {
            fixedUpdateClock = 0f;

            // all fixed update methods below
            GameController.getInstance().applyNextPendingGSU();
        }
    }

    /**
     * Renders the current FPS in the top left corner
     *
     * @param batch the batch to render the FPS
     */
    private void renderFPS(Batch batch) {
        BitmapFont font = new BitmapFont();
        font.getData().setScale(20);
        int fps = Gdx.graphics.getFramesPerSecond();
        font.draw(batch, "FPS: " + fps, 100, getHeight() - 100);
    }

}
