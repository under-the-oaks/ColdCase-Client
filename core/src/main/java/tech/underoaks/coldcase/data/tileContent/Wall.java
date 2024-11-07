package tech.underoaks.coldcase.data.tileContent;

import com.badlogic.gdx.graphics.Texture;
import tech.underoaks.coldcase.GameController;

public class Wall extends TileContent {

    private static final Texture texture = new Texture("./isometric tileset/separated images/tile_065.png");

    public Wall() {
        super(texture, false, false);
    }


    @Override
    public void action(GameController controller) {
        // TODO
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(GameController controller) {
        // TODO
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
