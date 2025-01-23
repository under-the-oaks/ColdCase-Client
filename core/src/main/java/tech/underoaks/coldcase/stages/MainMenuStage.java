package tech.underoaks.coldcase.stages;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import tech.underoaks.coldcase.game.UITextureController;
import tech.underoaks.coldcase.remote.WebSocketClient;

public class MainMenuStage extends AbstractStage {
    private Image backgroundImage = UITextureController.getInstance().getMenuBackground();
    private Table table;
    @Override
    public void buildStage(InputMultiplexer inputMultiplexer) {

        Skin skin = UITextureController.getInstance().getSkin();
        // Create buttons
        TextButton hostButton = new TextButton("Host", skin);
        TextButton joinButton = new TextButton("Join", skin);
        TextButton settingsButton = new TextButton("Settings", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        // Add listeners for button actions
        hostButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                StageManager.getInstance().showScreen(Stages.HOST);
            }
        });

        joinButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                StageManager.getInstance().showScreen(Stages.JOIN);
            }
        });

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                StageManager.getInstance().showScreen(Stages.SETTINGS);
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                WebSocketClient.getInstance().closeSession();
                System.exit(0);
            }
        });

        //add Bg
        addActor(backgroundImage);

        // Use a table to organize buttons neatly
        table = new Table();
        table.setFillParent(true); // Center the table on the stage
        addActor(table);
//        table.setDebug(true);

        //button size
        Vector2 buttonSize = UITextureController.getInstance().getButtonSize();
        float buttonWidth = buttonSize.x;
        float buttonHeight = buttonSize.y;

        // Add buttons to the table
        table.add(hostButton).width(buttonWidth).height(buttonHeight).padBottom(160);
        table.row();
        table.add(joinButton).width(buttonWidth).height(buttonHeight).padBottom(160);
        table.row();
        table.add(exitButton).width(buttonWidth).height(buttonHeight).padBottom(160);

        Pixmap bgPixmap = new Pixmap(1,1, Pixmap.Format.RGB565);
        bgPixmap.setColor(Color.GRAY);
        bgPixmap.fill();
    }
}
