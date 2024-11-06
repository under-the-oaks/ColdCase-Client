package tech.underoaks.coldcase.data.tileContent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player extends TileContent{

    private Texture texture;

    public Player(String texturePath) {
        texture = new Texture(texturePath);
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

    public void render(SpriteBatch batch, float x, float y) {
        batch.draw(texture, x, y);
    }

    public void dispose() {
        texture.dispose();
    }

}
