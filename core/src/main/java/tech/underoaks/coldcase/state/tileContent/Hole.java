package tech.underoaks.coldcase.state.tileContent;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.game.Interaction;
import tech.underoaks.coldcase.state.InteractionChain;
import tech.underoaks.coldcase.state.updates.GameStateUpdateException;
import tech.underoaks.coldcase.state.updates.RemoveTileContentUpdate;

public class Hole extends TileContent{

    private static final Texture texture = new Texture("./sprites/tileContent_hole.png");
    private static final Sprite sprite = new Sprite(texture);

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

        if(pos.equals(tilePosition) && handler instanceof MovableBlockTranscendent){
            System.out.println("test");
            chain.addGameStateUpdate(new RemoveTileContentUpdate(tilePosition,chain.getSnapshot().getSnapshotMap().getChildIndex(tilePosition,this)));
            chain.addGameStateUpdate(new RemoveTileContentUpdate(tilePosition,chain.getSnapshot().getSnapshotMap().getChildIndex(tilePosition,handler)));
        }
        return false;
    }
}
