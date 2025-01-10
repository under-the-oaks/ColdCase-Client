package tech.underoaks.coldcase.stages.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import tech.underoaks.coldcase.Main;
import tech.underoaks.coldcase.MapGenerator;
import tech.underoaks.coldcase.game.GameController;
import tech.underoaks.coldcase.game.PlayerController;
import tech.underoaks.coldcase.game.TextureController;
import tech.underoaks.coldcase.state.Map;
import tech.underoaks.coldcase.state.tileContent.Player;

import java.nio.file.Path;
import java.util.Properties;

public class MapActor extends Actor {

    private Map map;
    private final Properties properties = Main .getProperties();

    public MapActor() {
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
        map = setupMap(path);
    }

    public MapActor(String path){
        setupMap(path);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        map.render(batch, getOriginX(), getOriginY());
    }

    private Map setupMap(String path) {

        map = MapGenerator.serializeContentToMap(Path.of(path), TextureController.getIsDetective());

        GameController gameController = GameController.getInstance();
        gameController.setCurrentMap(map);

        PlayerController.getInstance().setPlayerPosition(gameController.getCurrentMap().getTileContentByType(Player.class));

        return map;
    }
}
