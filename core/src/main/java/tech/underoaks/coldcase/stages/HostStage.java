package tech.underoaks.coldcase.stages;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import tech.underoaks.coldcase.Main;
import tech.underoaks.coldcase.game.LevelManager;
import tech.underoaks.coldcase.game.TextureController;
import tech.underoaks.coldcase.game.UITextureController;
import tech.underoaks.coldcase.remote.WebSocketClient;
import tech.underoaks.coldcase.remote.WebSocketMessagesManager;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.Objects;

import static java.lang.Thread.sleep;

/**
 * The HostStage is a screen in the game where the host player can start a session,
 * view the session ID, and manage the connection. The host will have the option to start the game
 * and manage the session ID.
 * This screen extends {@link AbstractStage}.
 *
 * @author mabe.edu
 * @coauthor jean874
 */
public class HostStage extends AbstractStage {

    private final Skin skin = UITextureController.getInstance().getSkin();
    private TextField sessionIDField;
    private TextButton startButton;
    private Label hostLabel;
    private Label teammateLabel;
    private TextButton backButton;

    @Override
    public void buildStage(InputMultiplexer inputMultiplexer) {

        TextureController.setIsDetective(true);

        Table table = new Table();
        table.setFillParent(true); // Fill the entire stage

        //add Bg
        addActor(UITextureController.getInstance().getMenuBackground());

        this.addActor(table);

        //button size
        Vector2 buttonSize = UITextureController.getInstance().getButtonSize();
        float buttonWidth = buttonSize.x;
        float buttonHeight = buttonSize.y;

        // TextField for Session ID (non-editable)
        sessionIDField = new TextField("SessionID: Loading...", skin);
        sessionIDField.setDisabled(true); // Make it non-editable
        sessionIDField.setAlignment(1); // Center the text

        // button: copy to clipboard
        TextButton copyButton = new TextButton("Copy", skin);
        copyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(sessionIDField.getText().substring(11)), null);
            }
        });


        hostLabel = new Label(TextureController.getIsDetective() ? "You are the detective" : "You are the ghost", skin);
        hostLabel.setAlignment(1); // Center the text


        teammateLabel = new Label("Your teammate is the ghost", skin);
        teammateLabel.setAlignment(1); // Center the text

        // 3. Start Button
        startButton = new TextButton("Start", skin);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                WebSocketMessagesManager.startGame(0);
                LevelManager.getInstance().loadNextLevel();
            }
        });

        // 4. Back Button
        backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (WebSocketClient.getInstance().closeSession()) {
                    StageManager.getInstance().showScreen(Stages.MAIN_MENU);
                }
            }
        });

        Table playerSelectionTable = new Table();
        playerSelectionTable.row();
        playerSelectionTable.add(hostLabel);
        playerSelectionTable.add(teammateLabel);

        Table sessionIDTable = new Table();
        sessionIDTable.add(sessionIDField).width(8000).height(buttonHeight).padRight(10);
        sessionIDTable.add(copyButton).width(buttonWidth).height(buttonHeight);

        // Arrange components in the table
        table.add(sessionIDTable).padBottom(160);
        table.row();
        table.add(playerSelectionTable).padBottom(160);
        table.row();
        table.add(startButton).width(buttonWidth).height(buttonHeight).padBottom(160);
        table.row();
        table.add(backButton).width(buttonWidth).height(buttonHeight).padBottom(160);


        // setup lobby
        WebSocketClient.getInstance().connect(Main.getProperties().getProperty("websocket_url"));

        // execute this on a new Thread to load in the Session ID asynchronously to loading in the stage
        new Thread(() -> {
            String updatedSessionID = WebSocketClient.getLobbyID();
            while (Objects.equals(updatedSessionID, null)) {
                try {
                    sleep(100);
                    updatedSessionID = WebSocketClient.getLobbyID();
                } catch (InterruptedException v) {
                    throw new RuntimeException("Interrupted while waiting for Session ID: " + v);
                }
                setSessionIDField(updatedSessionID);
            }
        }).start();


    }

    public void setSessionIDField(String sessionID) {
        sessionIDField.setText("SessionID: " + sessionID);
    }
}
