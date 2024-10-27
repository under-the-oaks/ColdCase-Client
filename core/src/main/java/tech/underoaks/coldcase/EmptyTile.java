package tech.underoaks.coldcase;

import com.badlogic.gdx.graphics.Texture;

public class EmptyTile extends Tile {

    private static final Texture texture = new Texture("./isometric tileset/separated images/tile_101.png");

    public EmptyTile() {
        super(texture);
    }
}
