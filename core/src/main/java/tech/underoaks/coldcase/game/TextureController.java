package tech.underoaks.coldcase.game;

import com.badlogic.gdx.graphics.Texture;

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

    Texture gloveTexture = new Texture("./sprites/item_glove_detective_6.png");

    // Textures for the detective player
    Texture detectiveTexture = new Texture("./sprites/player_detective_right.png");
    Texture detectiveTextureNorth = new Texture("./sprites/player_detective_up.png");
    Texture detectiveTextureSouth = new Texture("./sprites/player_detective_down.png");
    Texture detectiveTextureEast = new Texture("./sprites/player_detective_right.png");
    Texture detectiveTextureWest = new Texture("./sprites/player_detective_left.png");

    // Textures for the ghost player
    Texture ghostTexture = new Texture("./sprites/Sprite_Ghost_Placeholder.png");

    public Texture getGloveTexture() {
        return gloveTexture;
    }

    public Texture getPlayerTexture() {
        return _isDetective ? detectiveTexture : ghostTexture;
    }

    public Texture getPlayerTextureNorth() {
        return _isDetective ? detectiveTextureNorth : ghostTexture;
    }

    public Texture getPlayerTextureSouth() {
        return _isDetective ? detectiveTextureSouth : ghostTexture;
    }

    public Texture getPlayerTextureEast() {
        return _isDetective ? detectiveTextureEast : ghostTexture;
    }

    public Texture getPlayerTextureWest() {
        return _isDetective ? detectiveTextureWest : ghostTexture;
    }

}
