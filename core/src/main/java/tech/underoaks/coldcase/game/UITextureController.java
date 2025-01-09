package tech.underoaks.coldcase.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * This class is responsible for providing the skin for the UI elements.
 *
 * @implNote Helpful for testing i think
 */
public class UITextureController {

    private static UITextureController instance;

    private BitmapFont font;
    private Skin skin;
    private Texture buttonUp;
    private Texture buttonDown;
    private Texture reloadButton;
    private Texture menuBackground;

    private UITextureController(TextureFactory factory) {
        instantiateUITextures(factory);
        instantiateFont();
        instantiateSkin();
    }

    private void instantiateFont() {
        this.font = new BitmapFont();
        this.font.getData().setScale(20);
    }

    private void instantiateSkin() {
        this.skin = new Skin();
        TextButton.TextButtonStyle defaultTextButtonStyle = new TextButton.TextButtonStyle();
        defaultTextButtonStyle.font = font;
        defaultTextButtonStyle.fontColor = Color.BLACK;
        defaultTextButtonStyle.up = getButtonUp();
        defaultTextButtonStyle.down = getButtonDown();
        this.skin.add("default", defaultTextButtonStyle);

        Button.ButtonStyle relaodButtonStyle = new Button.ButtonStyle();
        relaodButtonStyle.up = getReloadButton();
        relaodButtonStyle.down = getReloadButton();
        this.skin.add("reload", relaodButtonStyle);

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = font;
        textFieldStyle.fontColor = Color.BLACK;
        this.skin.add("default", textFieldStyle);

        TextField.TextFieldStyle inputTextFieldStyle = new TextField.TextFieldStyle();
        inputTextFieldStyle.font = font;
        inputTextFieldStyle.fontColor = Color.BLACK;
        Pixmap bgPixmap = new Pixmap(1, 1, Pixmap.Format.RGB565);
        bgPixmap.setColor(Color.WHITE);
        bgPixmap.fill();
        inputTextFieldStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(bgPixmap)));
        this.skin.add("input", inputTextFieldStyle);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.BLACK;
        this.skin.add("default", labelStyle);

    }

    private void instantiateUITextures(TextureFactory factory) {
        this.buttonUp = factory.create("./sprites/ui/button_up.png");
        this.buttonDown = factory.create("./sprites/ui/button_down.png");
        this.menuBackground = factory.create("./sprites/ui/PlaceholderBackground.png");
        this.reloadButton = factory.create("./sprites/ui/Placeholder_reloadButton.png");
    }

    public static UITextureController create(TextureFactory factory) {
        if (instance != null) {
            throw new IllegalStateException("TextureController already initialized");
        }
        instance = new UITextureController(factory);
        return instance;
    }

    public static UITextureController getInstance() {
        if (instance == null) {
            throw new IllegalStateException("TextureController not initialized");
        }
        return instance;
    }

    public static boolean exists() {
        return instance != null;
    }

    public Skin getSkin() {
        return skin;
    }

    public Vector2 getButtonSize(float width, float height) {
        //button size
        float buttonWidth = width / 2;
        float buttonHeight = height / 10;

        return new Vector2(buttonWidth, buttonHeight);
    }

    public Drawable getButtonUp() {
        return new TextureRegionDrawable(new TextureRegion(buttonUp));
    }

    public Drawable getButtonDown() {
        return new TextureRegionDrawable(new TextureRegion(buttonDown));
    }

    public Drawable getReloadButton() {
        return new TextureRegionDrawable(new TextureRegion(reloadButton));
    }

    public Texture getMenuBackground() {
        return menuBackground;
    }
}
