package tech.underoaks.coldcase.state.tiles;

import tech.underoaks.coldcase.game.TextureController;

/**
 * Represents a basic ground tile.
 */
public class GroundTile extends Tile {
    /**
     * Default-Constructor
     */
    public GroundTile() {
        super(TextureController.getInstance().getGroundTileTexture());
    }
}
