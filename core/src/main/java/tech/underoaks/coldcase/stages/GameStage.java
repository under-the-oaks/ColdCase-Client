package tech.underoaks.coldcase.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.game.GameController;
import tech.underoaks.coldcase.game.PlayerController;
import tech.underoaks.coldcase.stages.actors.MapActor;
import tech.underoaks.coldcase.state.tileContent.ItemObject;

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

        inputMultiplexer.addProcessor(PlayerController.getInstance());
    }

    @Override
    public void render(float delta){
        super.render(delta);

        fixedUpdate(delta);

        getBatch().begin();
        renderFPS(getBatch());
        renderInventory(1000, new Vector2(0,0) );
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

    void renderInventory( float inventoryDimension, Vector2 inventoryOffset ) {

        getBatch().draw( new Texture("./isometric tileset/separated images/TEST_INVENTORY.png") , -5000 + inventoryOffset.x + (inventoryDimension / 2), -5000 + inventoryOffset.y + (inventoryDimension / 2), inventoryDimension, inventoryDimension);

        if ( PlayerController.getInstance().getInventory() != null ) {

            ItemObject item = (ItemObject) PlayerController.getInstance().getInventory();

            Texture uiTexture = item.getInventoryTexture();

            if ( uiTexture != null ) {

                getBatch().draw( uiTexture , -5000 + inventoryOffset.x + (inventoryDimension / 2), -5000 + inventoryOffset.y + (inventoryDimension / 2), inventoryDimension, inventoryDimension);

            }
        }
    }

}
