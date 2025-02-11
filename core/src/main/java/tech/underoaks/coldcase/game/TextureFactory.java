package tech.underoaks.coldcase.game;

import com.badlogic.gdx.graphics.Texture;

/**
 * Klasse übernimmt die Erstellung von Texture-Objekten
 * @implNote Hilfreich fürs Testen
 */
public class TextureFactory {
    /**
     * Creates a new {@link Texture}
     * @param path ressource identifier for the texture
     * @return Initialized {@link Texture}
     */
    public Texture create(String path) {
        return new Texture(path);
    }
}
