package tech.underoaks.coldcase;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class Tile {

    private Texture texture;
    private Vector2 position;

    public Tile(Texture texture, float x, float y) {
        this.texture = texture;
        this.position = new Vector2(x, y);
    }

    public void render(SpriteBatch batch) {
        if(texture == null) {
            return;
        }
        batch.draw(texture, position.x, position.y);
    }
}
