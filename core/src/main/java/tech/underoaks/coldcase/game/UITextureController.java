package tech.underoaks.coldcase.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;

/**
 * This class is responsible for providing the skin for the UI elements.
 *
 * @author mabe.edu
 */
public class UITextureController {

    private static UITextureController instance;

    private BitmapFont font;
    private Skin skin;
    private Texture buttonUp;
    private Texture buttonDown;
    private Texture reloadButton;
    private Texture menuBackground;
    private final int buttonSizeMultiplyer = 4;

    private UITextureController(TextureFactory factory) {
        instantiateUITextures(factory);
        instantiateFont();
        instantiateSkin();
    }

    private void instantiateFont() {
        this.font = new BitmapFont(Gdx.files.internal("font/TypoFont.fnt"));
        this.font.getData().setScale(10);
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
        this.skin.add("default", inputTextFieldStyle);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.BLACK;
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGB565);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        labelStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        this.skin.add("default", labelStyle);

    }

    private void instantiateUITextures(TextureFactory factory) {
        this.buttonUp = factory.create("./sprites/ui/button_new_ghost.png");
        this.buttonDown = factory.create("./sprites/ui/button_new_detective.png");
        this.menuBackground = factory.create("./sprites/ui/hintergrund_06.png");
        this.reloadButton = factory.create("./sprites/ui/Placeholder_reloadButton.png");
    }

    /**
     * Creates a new {@code UITextureController} instance using the provided {@code TextureFactory}.
     * <p>
     * If an instance is already initialized, this method throws an {@code IllegalStateException}.
     * </p>
     *
     * @param factory the {@code TextureFactory} used to create textures for UI elements
     * @return the newly created {@code UITextureController} instance
     * @throws IllegalStateException if the UITextureController has already been initialized
     */
    public static UITextureController create(TextureFactory factory) {
        if (instance != null) {
            throw new IllegalStateException("TextureController already initialized");
        }
        instance = new UITextureController(factory);
        return instance;
    }

    /**
     * Returns the singleton instance of {@code UITextureController}.
     * <p>
     * If the instance has not been created yet, this method throws an {@code IllegalStateException}.
     * </p>
     *
     * @return the UITextureController instance
     * @throws IllegalStateException if the UITextureController is not initialized
     */
    public static UITextureController getInstance() {
        if (instance == null) {
            throw new IllegalStateException("TextureController not initialized");
        }
        return instance;
    }

    /**
     * Checks whether a {@code UITextureController} instance has been initialized.
     *
     * @return {@code true} if a UITextureController instance exists; {@code false} otherwise
     */
    public static boolean exists() {
        return instance != null;
    }

    /**
     * Returns the {@code Skin} used for styling UI elements.
     *
     * @return the {@code Skin} instance
     */
    public Skin getSkin() {
        return skin;
    }

    /**
     * Calculates and returns the size of a button based on the dimensions of the buttonUp texture
     * scaled by the button size multiplier.
     *
     * @return a {@code Vector2} containing the width and height of a button
     */
    public Vector2 getButtonSize() {
        //button size
        float buttonWidth = this.buttonUp.getWidth() * buttonSizeMultiplyer;
        float buttonHeight = this.buttonUp.getHeight() * buttonSizeMultiplyer;

        return new Vector2(buttonWidth, buttonHeight);
    }

    /**
     * Returns a {@code Drawable} representing the "button up" texture.
     *
     * @return a {@code Drawable} for the button up state
     */
    public Drawable getButtonUp() {
        return new TextureRegionDrawable(new TextureRegion(buttonUp));
    }

    /**
     * Returns a {@code Drawable} representing the "button down" texture.
     *
     * @return a {@code Drawable} for the button down state
     */
    public Drawable getButtonDown() {
        return new TextureRegionDrawable(new TextureRegion(buttonDown));
    }

    /**
     * Returns a {@code Drawable} representing the reload button texture.
     *
     * @return a {@code Drawable} for the reload button
     */
    public Drawable getReloadButton() {
        return new TextureRegionDrawable(new TextureRegion(reloadButton));
    }

    /**
     * Returns an {@code Image} configured as the menu background.
     * <p>
     * The returned {@code Image} is set to fill its parent and uses a fill scaling mode.
     * </p>
     *
     * @return the menu background {@code Image}
     */
    public Image getMenuBackground() {

        Image backgroundImage = new Image(new TextureRegionDrawable(new TextureRegion(menuBackground)));
        backgroundImage.setFillParent(true);
        backgroundImage.setScaling(Scaling.fill);
        return backgroundImage;
    }

    /**
     * Returns the {@code BitmapFont} used by the UI.
     *
     * @return the {@code BitmapFont} instance
     */
    public BitmapFont getFont() {
        return font;
    }

    /**
     * Maps an integer to a letter.
     *
     * @param number      The number to map to a letter. Must be between 1 and 26. 1 corresponds to 'a' or 'A', 2 to 'b' or 'B', etc.
     * @param isUpperCase Whether the letter should be uppercase.
     * @return The letter corresponding to the number.
     * @throws IllegalArgumentException If the number is not between 1 and 26.
     */
    public static char mapIntToLetter(int number, boolean isUpperCase) {
        if (number < 1 || number > 26) {
            throw new IllegalArgumentException("Number must be between 1 and 26");
        }
        if (isUpperCase) {
            return (char) ('A' + number - 1);
        } else {
            return (char) ('a' + number - 1);
        }
    }
}
