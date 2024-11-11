package tech.underoaks.coldcase;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import tech.underoaks.coldcase.data.Map;
import tech.underoaks.coldcase.data.tileContent.Player;

import java.nio.file.Path;

public class HeadlessApplicationListener extends Main {

    public GameController gameController;

    @Override
    public void create() {
        Map map = Map.getMap(Path.of("maps/test_plain"));
        gameController = GameController.getInstance();
        gameController.setCurrentMap(map);

        PlayerController.getInstance().setPlayerPosition(gameController.getCurrentMap().getTileContentByType(Player.class));
    }

    @Override
    public void render() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void dispose() {
        gameController.getCurrentMap().dispose();
    }
}
