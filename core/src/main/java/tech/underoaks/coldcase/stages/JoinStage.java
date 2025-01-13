package tech.underoaks.coldcase.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import tech.underoaks.coldcase.Main;
import tech.underoaks.coldcase.game.TextureController;
import tech.underoaks.coldcase.game.UITextureController;
import tech.underoaks.coldcase.remote.WebSocketClient;

/**
 * The JoinStage is a screen in the game where players can connect to a session using a session ID.
 * It allows players to input a session ID, view connection status, and navigate back to the main menu.
 * This screen is part of the game's user interface and extends the {@link AbstractStage}.
 *
 * @author mabe.edu
 * @coauthor jean874
 */
public class JoinStage extends AbstractStage {
    private final Skin skin = UITextureController.getInstance().getSkin();
    private TextField sessionIDField;
    private Label connectionStatusLabel;
    private TextButton connectButton;
    private Label hostLabel;
    private Label teammateLabel;
    private TextButton backButton;

    @Override
    public void buildStage(InputMultiplexer inputMultiplexer) {

        //add Bg
        addActor(UITextureController.getInstance().getMenuBackground());

        Table table = new Table();
        table.setFillParent(true); // Fill the entire stage
        this.addActor(table);

        //button size
        Vector2 buttonSize = UITextureController.getInstance().getButtonSize();
        float buttonWidth = buttonSize.x;
        float buttonHeight = buttonSize.y;

        // TextField for entering Session ID
        sessionIDField = new TextField("", skin);
        sessionIDField.setMessageText("Enter Session ID"); // Placeholder text
        sessionIDField.setAlignment(1);

        // Label for connection status
        connectionStatusLabel = new Label("Not Connected", skin);
        connectionStatusLabel.setColor(Color.RED);
        connectionStatusLabel.setAlignment(1); // Center the text

        // Connect Button next to the TextField
        connectButton = new TextButton("Connect", skin);
        connectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                String sessionID = sessionIDField.getText();
                System.out.println("Connecting to session: " + sessionID);
                WebSocketClient.getInstance().connect(Main.getProperties().getProperty("websocket_url"),sessionID);
            }
        });

        //Image and Caption
        hostLabel = new Label("You are the detective", skin);
        hostLabel.setAlignment(1); // Center the text

        teammateLabel = new Label("Your teammate is the ghost", skin);
        teammateLabel.setAlignment(1); // Center the text

        backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                WebSocketClient.getInstance().closeSession();
                StageManager.getInstance().showScreen(Stages.MAIN_MENU);
            }
        });

        Table topRow = new Table();

        topRow.add(sessionIDField).width(8000).height(buttonHeight).padRight(100);
        topRow.add(connectButton).width(buttonWidth).height(buttonHeight);

        Table playerSelectionTable = new Table();
        playerSelectionTable.add();
        playerSelectionTable.row();
        playerSelectionTable.add(teammateLabel);
        playerSelectionTable.add();
        playerSelectionTable.add(hostLabel);

        // Arrange components in the table
        table.add(topRow).padBottom(160);
        table.row();
        table.add(connectionStatusLabel).padBottom(160);
        table.row();
        table.add(playerSelectionTable).padBottom(160);
        table.row();
        table.add(backButton).width(buttonWidth).height(buttonHeight).fillX().uniformX();

    }

    /**
     * This method is called when the connection is successfully established.
     * It updates the UI to show that the user is connected and disables the connect button.
     */
    @Override
    public void onConnected() {
        Gdx.app.postRunnable(() -> {
            connectionStatusLabel.setText("Connected");
            connectionStatusLabel.setColor(Color.GREEN);
            connectButton.setDisabled(true);
            connectButton.setTouchable(Touchable.disabled);
            TextureController.setIsDetective(false);
        });
    }

    /**
     * This method is called when the connection is disconnected.
     * It updates the UI to show that the user is not connected and enables the connect button.
     */
    @Override
    public void onDisconnected() {
        connectionStatusLabel.setText("Not Connected");
        connectionStatusLabel.setColor(Color.RED);
        connectButton.setDisabled(false);
        connectButton.setTouchable(Touchable.enabled);
    }
}
