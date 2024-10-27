package tech.underoaks.coldcase;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Tile {
    private TileContent tileContent;
    private Texture texture;

    public Tile(Texture texture) {
        this.texture = texture;
    }

    public void render(SpriteBatch batch, float x, float y) {
        if (texture == null) {
            return;
        }
        batch.draw(texture, x, y);
    }

    public TileContent getTileContent() {
        return tileContent;
    }

    public void setTileContent(TileContent tileContent) {
        this.tileContent = tileContent;
    }

}
