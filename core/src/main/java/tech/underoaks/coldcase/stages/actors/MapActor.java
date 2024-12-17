package tech.underoaks.coldcase.stages.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import tech.underoaks.coldcase.Main;
import tech.underoaks.coldcase.MapGenerator;
import tech.underoaks.coldcase.game.GameController;
import tech.underoaks.coldcase.game.PlayerController;
import tech.underoaks.coldcase.state.Map;
import tech.underoaks.coldcase.state.tileContent.Player;

import java.nio.file.Path;
import java.util.Properties;

public class MapActor extends Actor {

    private Map map;
    private final Properties properties = Main.properties;

    public MapActor() {
        map = setupMap();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        map.render(batch, getOriginX(), getOriginY());
    }

    private Map setupMap() {

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

        return map;
    }
}
