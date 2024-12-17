package tech.underoaks.coldcase.stages;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import tech.underoaks.coldcase.game.TextureController;

public class MainMenu extends AbstractStage{

    @Override
    public void buildStage() {
        Button button = new Button();
        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = TextureController.getInstance().getButtonUp();
        style.down = TextureController.getInstance().getButtonDown();
        Skin skin = new Skin();
        skin.add("default", style);

        button.setStyle(style);
        button.setPosition(getHeight()/2, getWidth()/2);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                StageManager.getInstance().showScreen(Stages.GAME);
            }
        });

        addActor(button);
    }
}
