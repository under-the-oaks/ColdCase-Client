package tech.underoaks.coldcase.data.tiles;

import com.badlogic.gdx.graphics.Texture;

public class GroundTile extends Tile {

    private static final Texture texture = new Texture("./isometric tileset/separated images/tile_069.png");

    public GroundTile() {
        super(texture);
    }

}
