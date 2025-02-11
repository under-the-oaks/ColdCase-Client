package tech.underoaks.coldcase.state.tileContent;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.game.Interaction;
import tech.underoaks.coldcase.game.TextureController;
import tech.underoaks.coldcase.state.InteractionChain;
import tech.underoaks.coldcase.state.updates.GameStateUpdateException;
import tech.underoaks.coldcase.state.updates.RemoveTileContentUpdate;
/**
 * HoleTest Class
 * Represents a hole on the tile grid that can interact with movable blocks.
 * This class handles the rendering of the hole texture and updates based on
 * interactions with movable blocks. If a block is moved into the hole's
 * position, it will remove both the hole and the block from the tile.
 * Author: jean874
 */
public class Hole extends TileContent{

    private static final Texture texture = TextureController.getInstance().holeTexture();
    private static final Sprite sprite = new Sprite(texture);

    /**
     * Default-Constructor
     */
    public Hole() {
        super(texture,false,true);
    }

    @Override
    public void render(Batch batch, float x, float y) {

        if (sprite != null) {
            sprite.setPosition(x, y);
            batch.draw(sprite, x, y , sprite.getWidth(), sprite.getHeight());
        }

        if (tileContent != null) {
            tileContent.render(batch, x, y);
        }
    }

    @Override
    public boolean action(InteractionChain chain, Interaction interaction) throws GameStateUpdateException {
        return false;
    }

    @Override
    public boolean update(InteractionChain chain, Vector2 tilePosition, Interaction interaction, TileContent handler) throws GameStateUpdateException, UpdateTileContentException {
        Vector2 pos = interaction.getTargetPos();
        pos = pos.cpy().add(interaction.getActionDirection().getVector());

        if(pos.equals(tilePosition) && handler instanceof MovableBlock){

            chain.addGameStateUpdate(new RemoveTileContentUpdate(tilePosition,chain.getSnapshot().getSnapshotMap().getChildIndex(tilePosition,this)));
            chain.addGameStateUpdate(new RemoveTileContentUpdate(tilePosition,chain.getSnapshot().getSnapshotMap().getChildIndex(tilePosition,handler)));
            return true;
        }
        return false;
    }
}
