package tech.underoaks.coldcase.game;

import com.badlogic.gdx.graphics.Texture;

/**
 * Klasse übernimmt die Erstellung von Texture-Objekten
 * @implNote Hilfreich fürs Testen
 */
public class TextureFactory {
    public Texture create(String path) {
        return new Texture(path);
    }
}
