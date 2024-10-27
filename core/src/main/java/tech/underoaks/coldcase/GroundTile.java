package tech.underoaks.coldcase;

import com.badlogic.gdx.graphics.Texture;

public class GroundTile extends Tile {

    private static final Texture texture = new Texture("./isometric tileset/separated images/tile_063.png");

    public GroundTile() {
        super(texture);
    }

}
