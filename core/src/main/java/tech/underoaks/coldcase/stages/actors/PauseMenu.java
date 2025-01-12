package tech.underoaks.coldcase.stages.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import tech.underoaks.coldcase.game.LevelManager;
import tech.underoaks.coldcase.game.Levels;
import tech.underoaks.coldcase.game.UITextureController;
import tech.underoaks.coldcase.remote.WebSocketMessagesManager;
import tech.underoaks.coldcase.stages.StageManager;
import tech.underoaks.coldcase.stages.Stages;

public class PauseMenu extends Group {

    public PauseMenu() {
        super();

        Skin skin = UITextureController.getInstance().getSkin();

        Button resetButton = new TextButton("Reset", skin);
        resetButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                LevelManager levelManager = LevelManager.getInstance();
                int currentLevelIndex = levelManager.currentLevelIndex;

                WebSocketMessagesManager.startGame(currentLevelIndex);
                levelManager.loadLevel(Levels.values()[currentLevelIndex]);
            }
        });


        Button exitButton = new TextButton("Exit", skin);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                WebSocketMessagesManager.exitToMainMenuMessage();
                StageManager.getInstance().showScreen(Stages.MAIN_MENU);

                LevelManager.getInstance().currentLevelIndex = 0;
            }
        });

        // Use a table to organize buttons neatly
        Table table = new Table();
        table.setFillParent(true); // Center the table on the stage
        addActor(table);

        //button size
        Vector2 buttonSize = UITextureController.getInstance().getButtonSize();
        float buttonWidth = buttonSize.x;
        float buttonHeight = buttonSize.y;

        System.out.println("buttonWidth: " + buttonWidth);
        System.out.println("buttonHeight: " + buttonHeight);

        resetButton.setSize(buttonWidth, buttonHeight);
        exitButton.setSize(buttonWidth, buttonHeight);

        // Add buttons to the table
        table.add(resetButton).width(buttonWidth).height(buttonHeight).padBottom(160);
        table.row();
        table.add(exitButton).width(buttonWidth).height(buttonHeight);

        // Set background color
        Pixmap bgPixmap = new Pixmap(20, 20, Pixmap.Format.RGBA8888);
        bgPixmap.setColor(new Color(0, 0, 0, 0.7f));
        bgPixmap.fill();
        TextureRegionDrawable textureRegionDrawableBg = new TextureRegionDrawable(new TextureRegion(new Texture(bgPixmap)));
        table.setBackground(textureRegionDrawableBg);
    }

}
