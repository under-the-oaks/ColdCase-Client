package tech.underoaks.coldcase.stages;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import tech.underoaks.coldcase.Main;
import tech.underoaks.coldcase.game.TextureController;
import tech.underoaks.coldcase.game.UITextureController;
import tech.underoaks.coldcase.remote.WebSocketClient;

public class JoinStage extends AbstractStage {
    private final Skin skin = UITextureController.getInstance().getSkin();
    private TextField sessionIDField;
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

        // Connect Button next to the TextField
        connectButton = new TextButton("Connect", skin);
        connectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Connect button clicked");
                System.out.println("WebSocketClient.exists(): " + WebSocketClient.exists());
                if (WebSocketClient.exists()) {
                    System.out.println("WebSocketClient.getInstance().isConnectionOpen(): " + WebSocketClient.getInstance().isConnectionOpen());
                }
                if (!WebSocketClient.exists() || !WebSocketClient.getInstance().isConnectionOpen()) {
                    String sessionID = sessionIDField.getText();
                    TextureController.setIsDetective(false);
                    Main.getProperties().setProperty("role", "ghost");
                    System.out.println("Connecting to session: " + sessionID);
                    WebSocketClient.create(Main.getProperties().getProperty("websocket_url"), sessionID);
                }
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
        table.add(playerSelectionTable).padBottom(160);
        table.row();
        table.add(backButton).width(buttonWidth).height(buttonHeight).fillX().uniformX();

    }
}
