package tech.underoaks.coldcase.stages.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import tech.underoaks.coldcase.MapGenerator;
import tech.underoaks.coldcase.game.GameController;
import tech.underoaks.coldcase.game.PlayerController;
import tech.underoaks.coldcase.game.TextureController;
import tech.underoaks.coldcase.state.Map;
import tech.underoaks.coldcase.state.tileContent.Player;
import java.nio.file.Path;

/**
 * Actor representing the game map in the stage.
 * The MapActor is responsible for loading and rendering the map and player onto the screen.
 * It also handles setting up the map based on a provided path.
 */
public class MapActor extends Actor {

    private Map map;

    /**
     * Default-Constructor
     * @param path The path that points to the map-file
     */
    public MapActor(String path){
        setupMap(path);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        map.render(batch, getOriginX(), getOriginY());
    }

    /**
     * Sets up the map by deserializing the map data from the specified file path.
     * It also updates the game controller with the current map and sets the player's initial position.
     *
     * @param path The file path to the map data that will be loaded.
     */
    private void setupMap(String path) {

        map = MapGenerator.serializeContentToMap(Path.of(path), TextureController.getIsDetective());

        GameController gameController = GameController.getInstance();
        gameController.setCurrentMap(map);

        PlayerController.getInstance().setPlayerPosition(gameController.getCurrentMap().getTileContentByType(Player.class));

    }
}
