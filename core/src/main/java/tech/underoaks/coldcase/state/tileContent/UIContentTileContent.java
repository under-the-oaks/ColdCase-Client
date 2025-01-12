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

    public UIContentTileContent() {
        super(null, false, false);
        this.displayText = "";
        this.transformShift = UIContentTileContentShift.SHIFT_BLOCKSIDE_LEFT;
    }

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
    public enum UIContentTileContentShift {
        SHIFT_BLOCKSIDE_LEFT(690, 810, 0, 0, 0),
        SHIFT_BLOCKSIDE_RIGHT(210, 810, 0, 0, 0),
        SHIFT_BLOCKTOP(400, 800, 0,0,45);

        public final int shiftX;
        public final int shiftY;
        public final int tiltX;
        public final int tiltY;
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
