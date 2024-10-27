package tech.underoaks.coldcase;

import com.badlogic.gdx.graphics.Texture;

public class EmptyTile extends Tile {

    private static final Texture texture = null;

    public EmptyTile(float x, float y) {
        super(texture, x, y);
    }
}
