package tech.underoaks.coldcase.data.tileContent;

import com.badlogic.gdx.graphics.Texture;
import tech.underoaks.coldcase.GameController;

public class TestContent extends TileContent {
    private static final Texture texture = new Texture("./isometric tileset/separated images/tile_050.png");

    public TestContent() {
        super(texture, false, false);
    }

    @Override
    public void action(GameController controller) {
        System.out.println(this.getClass().getSimpleName() + " action");
    }

    @Override
    public void update(GameController controller) {
        System.out.println(this.getClass().getSimpleName() + " update");
    }
}
