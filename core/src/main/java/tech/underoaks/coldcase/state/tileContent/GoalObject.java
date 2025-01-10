package tech.underoaks.coldcase.state.tileContent;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.game.*;
import tech.underoaks.coldcase.state.InteractionChain;

import tech.underoaks.coldcase.state.Map;
import tech.underoaks.coldcase.state.updates.EndLevelUpdate;
import tech.underoaks.coldcase.state.updates.GameStateUpdateException;


public class GoalObject extends TileContent {

    private final Sprite sprite = new Sprite( TextureController.getInstance().getGoalObjectTexture());

    public GoalObject() {
        super( TextureController.getInstance().getGoalObjectTexture(), true, false);
        this.visibilityState = VisibilityStates.TRANSCENDENT;
    }

    @Override
    public void render(Batch batch, float x, float y) {

        if (sprite != null) {
            sprite.setPosition(x, y);
            batch.draw(sprite, x, y + 480, sprite.getWidth(), sprite.getHeight());
        }

        if (tileContent != null) {
            tileContent.render(batch, x, y);
        }
    }

    @Override
    public boolean action(InteractionChain chain, Interaction interaction) throws GameStateUpdateException {

        if (Map.isPlayerOnTile(interaction.getTargetPos()) || Map.isPlayerNextToTile(interaction.getTargetPos())){

            chain.addGameStateUpdate(new EndLevelUpdate());
            return true; //Action was successfully
        }
        return false;
    }

    @Override
    public boolean update(InteractionChain chain, Vector2 tilePosition, Interaction interaction, TileContent handler) throws GameStateUpdateException {

        return false; //No Action performed
    }


}
