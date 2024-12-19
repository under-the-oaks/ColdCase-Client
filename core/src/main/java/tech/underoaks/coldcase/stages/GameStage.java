package tech.underoaks.coldcase.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import tech.underoaks.coldcase.game.GameController;
import tech.underoaks.coldcase.game.PlayerController;
import tech.underoaks.coldcase.stages.actors.InventoryActor;
import tech.underoaks.coldcase.stages.actors.MapActor;

public class GameStage extends AbstractStage {
    private float timeSinceLastGSUCheck = 0f;

    GameStage() {
        super();
    }

    @Override
    public void buildStage(InputMultiplexer inputMultiplexer) {
        Gdx.input.setInputProcessor(PlayerController.getInstance());
        MapActor mapActor = new MapActor();
        mapActor.setOrigin(getWidth()/2, getHeight()/2);
        addActor(mapActor);

        InventoryActor inventoryActor = new InventoryActor();
        inventoryActor.setPosition(100, 100);
        inventoryActor.setSize(2000, 2000);
        addActor(inventoryActor);

        inputMultiplexer.addProcessor(PlayerController.getInstance());
    }

    @Override
    public void render(float delta){
        super.render(delta);

        fixedUpdate(delta);

        getBatch().begin();
        renderFPS(getBatch());
        getBatch().end();

    }

    /**
     * Fixed update method to trigger Methods every 0.1 seconds
     * @param delta time since last frame
     */
    private void fixedUpdate(float delta) {
        timeSinceLastGSUCheck += delta;
        if (timeSinceLastGSUCheck >= 0.1f) {
            GameController.getInstance().applyNextPendingGSU();
            timeSinceLastGSUCheck = 0f;
        }
    }

    private void renderFPS(Batch batch) {
        BitmapFont font = new BitmapFont();
        int fps = Gdx.graphics.getFramesPerSecond();
        font.draw(batch, "FPS: " + fps, 10, getHeight()-10);
    }

    @Override
    public void dispose() {
        super.dispose();
        GameController.getInstance().getCurrentMap().dispose();
    }

}
