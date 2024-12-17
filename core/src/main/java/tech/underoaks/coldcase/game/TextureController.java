package tech.underoaks.coldcase.game;

import com.badlogic.gdx.graphics.Texture;

public class TextureController {
    private static TextureController instance;

    private static boolean isDetective = true;

    // Texturen
    private Texture gloveTexture;
    private Texture detectiveTexture;
    private Texture detectiveTextureNorth;
    private Texture detectiveTextureSouth;
    private Texture detectiveTextureEast;
    private Texture detectiveTextureWest;
    private Texture ghostTexture;

    private TextureController(TextureFactory factory) {
        this.gloveTexture = factory.create("./sprites/item_glove_detective_6.png");

        this.detectiveTexture = factory.create("./sprites/player_detective_right.png");
        this.detectiveTextureNorth = factory.create("./sprites/player_detective_up.png");
        this.detectiveTextureSouth = factory.create("./sprites/player_detective_down.png");
        this.detectiveTextureEast = factory.create("./sprites/player_detective_right.png");
        this.detectiveTextureWest = factory.create("./sprites/player_detective_left.png");

        this.ghostTexture = factory.create("./sprites/Sprite_Ghost_Placeholder.png");
    }

    public static TextureController create(boolean isDetective, TextureFactory factory) {
        if (instance != null) {
            throw new IllegalStateException("TextureController already initialized");
        }
        TextureController.isDetective = isDetective;
        instance = new TextureController(factory);
        return instance;
    }

    public static TextureController getInstance() {
        if (instance == null) {
            throw new IllegalStateException("TextureController not initialized");
        }
        return instance;
    }

    public Texture getGloveTexture() {
        return gloveTexture;
    }

    public Texture getPlayerTexture() {
        return isDetective ? detectiveTexture : ghostTexture;
    }

    public Texture getPlayerTextureNorth() {
        return isDetective ? detectiveTextureNorth : ghostTexture;
    }

    public Texture getPlayerTextureSouth() {
        return isDetective ? detectiveTextureSouth : ghostTexture;
    }

    public Texture getPlayerTextureEast() {
        return isDetective ? detectiveTextureEast : ghostTexture;
    }

    public Texture getPlayerTextureWest() {
        return isDetective ? detectiveTextureWest : ghostTexture;
    }

}
