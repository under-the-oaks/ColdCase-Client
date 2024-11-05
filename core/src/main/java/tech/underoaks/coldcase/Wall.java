package tech.underoaks.coldcase;

import com.badlogic.gdx.graphics.Texture;

public class Wall extends TileContent {

    private static final Texture texture = new Texture("./isometric tileset/separated images/tile_065.png");

    public Wall() {
        super(texture, false, false);
    }
}
