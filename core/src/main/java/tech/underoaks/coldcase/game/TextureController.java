package tech.underoaks.coldcase.game;

import com.badlogic.gdx.graphics.Texture;
import tech.underoaks.coldcase.state.tileContent.TileContent;
import tech.underoaks.coldcase.state.tileContent.TileContents;

public class TextureController {
    private static TextureController instance;
    public static TextureController getInstance() {
        if (instance == null) {
            throw new IllegalStateException("TextureController not initialized");
        }
        return instance;
    }

    public static TextureController create(boolean isDetective) {
        if (instance != null) {
            throw new IllegalStateException("TextureController already initialized");
        }
        _isDetective = isDetective;
        instance = new TextureController();
        return instance;
    }

    private static boolean _isDetective = true;


    public Texture getGloveTexture() {
        return new Texture("./sprites/item_glove_detective_6.png");
    }

    public Texture getPlayerTexture() {
        return new Texture(_isDetective ? "./sprites/player_detective.png" : "./sprites/player_sherlock.png");
    }

}
