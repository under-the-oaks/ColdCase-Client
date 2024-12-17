package tech.underoaks.coldcase.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.MapGenerator;
import tech.underoaks.coldcase.game.GameController;
import tech.underoaks.coldcase.game.PlayerController;
import tech.underoaks.coldcase.state.Map;
import tech.underoaks.coldcase.state.tileContent.ItemObject;
import tech.underoaks.coldcase.state.tileContent.Player;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

public class GameStage extends AbstractStage {
    private Map map;
    private static final String propertiesPath = ".properties";
    private final Properties properties = new Properties();
    private float timeSinceLastGSUCheck = 0f;

    GameStage() {
        super();
    }

    @Override
    public void buildStage() {
        Gdx.input.setInputProcessor(PlayerController.getInstance());

        setupMap();
    }

    @Override
    public void render(float delta){
        super.render(delta);

        timeSinceLastGSUCheck += delta;
        if (timeSinceLastGSUCheck >= 0.1f) {
            GameController.getInstance().applyNextPendingGSU();
            timeSinceLastGSUCheck = 0f;
        }

        getBatch().begin();

        renderFPS(getBatch());
        map.render(getBatch());
        renderInventory(1000, new Vector2(0,0) );

        getBatch().end();

    }

    private void renderFPS(Batch batch) {
        BitmapFont font = new BitmapFont();
        int fps = Gdx.graphics.getFramesPerSecond();
        font.draw(batch, "FPS: " + fps, 10, 20);
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

    void setupMap() {
        try {
            properties.load(new FileInputStream(propertiesPath));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(!properties.containsKey("map_path")) {
            throw new RuntimeException("Missing required property: map_path");
        }
        if(!properties.containsKey("role")) {
            throw new RuntimeException("Missing required property role");
        }
        String path = properties.getProperty("map_path");
        boolean detective = switch (properties.getProperty("role")) {
            case "detective" -> true;
            case "ghost" -> false;
            default -> throw new RuntimeException("Unknown role: " + properties.getProperty("role"));
        };

        map = MapGenerator.serializeContentToMap(Path.of(path), detective);

        GameController gameController = GameController.getInstance();
        gameController.setCurrentMap(map);

        PlayerController.getInstance().setPlayerPosition(gameController.getCurrentMap().getTileContentByType(Player.class));
    }

}
