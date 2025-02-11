package tech.underoaks.coldcase.state.tiles;

import tech.underoaks.coldcase.game.TextureController;

/**
 * Represents an empty tile with no special properties.
 */
public class EmptyTile extends Tile {
    /**
     * Default-Constructor
     */
    public EmptyTile() {
        super(TextureController.getInstance().getEmptyTileTexture());
    }
}
