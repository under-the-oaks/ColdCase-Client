package tech.underoaks.coldcase;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GroundTile extends Tile {

    private static final Texture texture = new Texture("./isometric tileset/separated images/tile_063.png");

    public GroundTile(float x, float y) {
        super(texture, x, y);
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
    }


}
