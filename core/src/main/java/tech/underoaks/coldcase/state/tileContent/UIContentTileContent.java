package tech.underoaks.coldcase.state.tileContent;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.game.Interaction;
import tech.underoaks.coldcase.game.UITextureController;
import tech.underoaks.coldcase.state.InteractionChain;
import tech.underoaks.coldcase.state.updates.GameStateUpdateException;

/**
 * A {@code TileContent} that displays text on the Map.
 *
 * @author mabe.edu
 */
public class UIContentTileContent extends TileContent {
    private final String displayText;
    private final UIContentTileContentShift transformShift;

    /**
     * Constructs a new {@code UIContentTileContent} with an empty display text and the default transform shift.
     * <p>
     * The default transform shift used is {@link UIContentTileContentShift#SHIFT_BLOCKSIDE_LEFT}.
     * </p>
     */
    public UIContentTileContent() {
        super(null, false, false);
        this.displayText = "";
        this.transformShift = UIContentTileContentShift.SHIFT_BLOCKSIDE_LEFT;
    }

    /**
     * Constructs a new {@code UIContentTileContent} with the specified display text and transform shift.
     *
     * @param text  the text to display on the map
     * @param shift the transform shift configuration to apply when rendering the text
     */
    public UIContentTileContent(String text, UIContentTileContentShift shift) {
        super(null, false, false);
        this.displayText = text;
        this.transformShift = shift;
    }

    @Override
    public void render(Batch batch, float x, float y) {
        BitmapFont font = UITextureController.getInstance().getFont();

        batch.end();
        batch.getTransformMatrix().idt(); // Reset to identity matrix
        batch.getTransformMatrix().translate(x + transformShift.shiftX, y + transformShift.shiftY, 0); // Translate to text position
        batch.getTransformMatrix().rotate(1, 0, 0, transformShift.tiltX); // Rotate around the x-axis
        batch.getTransformMatrix().rotate(0, 1, 0, transformShift.tiltY); // Rotate around the y-axis
        batch.getTransformMatrix().rotate(0, 0, 1, transformShift.tiltZ); // Rotate around the z-axis
        batch.begin(); // Begin the batch again after modifying the matrix

        font.draw(batch, displayText, 0, 0);

        // Reset the matrix and continue with the super.render
        batch.end();
        batch.getTransformMatrix().idt(); // Reset the matrix to avoid affecting other rendering
        batch.begin();

        super.render(batch, x, y);
    }

    @Override
    public boolean action(InteractionChain chain, Interaction interaction) throws GameStateUpdateException {
        return false;
    }

    @Override
    public boolean update(InteractionChain chain, Vector2 tilePosition, Interaction interaction, TileContent handler) throws GameStateUpdateException, UpdateTileContentException {
        return false;
    }

    /**
     * The different ways the text can be shifted and tilted.
     * <p>
     * The shift values are the x and y coordinates of the text relative to the tile.
     * The tilt values are the angles of rotation around the x, y, and z axes.
     * The text is rendered at the tile's position, then shifted and tilted according to these values.
     */
    public enum UIContentTileContentShift {
        /**
         * Shift configuration for rendering text on the left side of a block.
         * <p>
         * Applies a horizontal shift of 690 and a vertical shift of 810 with no rotation.
         * </p>
         */
        SHIFT_BLOCKSIDE_LEFT(690, 810, 0, 0, 0),
        /**
         * Shift configuration for rendering text on the right side of a block.
         * <p>
         * Applies a horizontal shift of 210 and a vertical shift of 810 with no rotation.
         * </p>
         */
        SHIFT_BLOCKSIDE_RIGHT(210, 810, 0, 0, 0),
        /**
         * Shift configuration for rendering text at the top of a block.
         * <p>
         * Applies a horizontal shift of 400, a vertical shift of 800, and rotates the text 45 degrees around the z-axis.
         * </p>
         */
        SHIFT_BLOCKTOP(400, 800, 0, 0, 45);

        /**
         * The horizontal shift applied to the text relative to the tile's position.
         */
        public final int shiftX;
        /**
         * The vertical shift applied to the text relative to the tile's position.
         */
        public final int shiftY;
        /**
         * The rotation angle applied around the x-axis.
         */
        public final int tiltX;
        /**
         * The rotation angle applied around the y-axis.
         */
        public final int tiltY;
        /**
         * The rotation angle applied around the z-axis.
         */
        public final int tiltZ;


        UIContentTileContentShift(int shiftX, int shiftY, int tiltX, int tiltY, int tiltZ) {
            this.shiftX = shiftX;
            this.shiftY = shiftY;
            this.tiltX = tiltX;
            this.tiltY = tiltY;
            this.tiltZ = tiltZ;
        }
    }

}
