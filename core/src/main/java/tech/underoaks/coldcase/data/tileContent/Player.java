package tech.underoaks.coldcase.data.tileContent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;

public class Player extends TileContent {

    private static final Texture texture = new Texture("./isometric tileset/placholder_player.png");

    public Player() {
        super(texture, true, false);
    }

    public void update(float deltaTime) {
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            // not implemented - waiting on game Controller
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            // not implemented - waiting on game Controller
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            // not implemented - waiting on game Controller
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            // not implemented - waiting on game Controller
        }
    }

}
