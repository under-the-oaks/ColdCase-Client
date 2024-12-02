package tech.underoaks.coldcase.state.tiles;

import com.badlogic.gdx.graphics.Texture;

/**
 * Represents a basic ground tile.
 */
public class GroundTile extends Tile {

    private static final Texture texture = new Texture("./sprites/Sprite_StandardBlock_G_Cropped.png");

    public GroundTile() {
        super(texture);
    }

}
