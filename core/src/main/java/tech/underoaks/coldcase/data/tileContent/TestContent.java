package tech.underoaks.coldcase.data.tileContent;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.GameController;

public class TestContent extends TileContent {
    private static final Texture texture = new Texture("./isometric tileset/separated images/tile_050.png");

    public TestContent() {
        super(texture, false, false);
    }

    @Override
    public boolean action(GameController controller, Vector2 position) {
        System.out.println(this.getClass().getSimpleName() + " action");
        return true;
    }

    @Override
    public void update(GameController controller) {
        System.out.println(this.getClass().getSimpleName() + " update");
    }
}
