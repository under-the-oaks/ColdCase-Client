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
    private Image hostImage;
    private Label teammateLabel;
    private Image teammateImage;
    private TextButton backButton;

    @Override
    public void buildStage(InputMultiplexer inputMultiplexer) {

        Table table = new Table();
        table.setFillParent(true); // Fill the entire stage
        this.addActor(table);

        //button size
        Vector2 buttonSize = UITextureController.getInstance().getButtonSize(getWidth(), getHeight());
        float buttonWidth = buttonSize.x;
        float buttonHeight = buttonSize.y;

        // TextField for entering Session ID
        sessionIDField = new TextField("", skin.get("input", TextField.TextFieldStyle.class));
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
        hostImage = new Image(TextureController.getInstance().getDetectiveTexture());
        hostLabel = new Label("You are the detective", skin);
        hostLabel.setAlignment(1); // Center the text

        teammateImage = new Image(TextureController.getInstance().getGhostTexture());
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
        topRow.add(sessionIDField).width(buttonWidth).height(buttonHeight).padRight(10);
        topRow.add(connectButton).width(buttonWidth/2).height(buttonHeight);

        Table playerSelectionTable = new Table();
        playerSelectionTable.add(teammateImage).width(1080*4).height(1080*4);
        playerSelectionTable.add().width(1080).height(1080);
        playerSelectionTable.add(hostImage).width(1080*4).height(1080*4);
        playerSelectionTable.row();
        playerSelectionTable.add(teammateLabel);
        playerSelectionTable.add();
        playerSelectionTable.add(hostLabel);

        // Arrange components in the table
        table.add(topRow).padBottom(20);
        table.row();
        table.add(playerSelectionTable).padBottom(20);
        table.row();
        table.add(backButton).width(buttonWidth).height(buttonHeight).fillX().uniformX();

        Pixmap bgPixmap = new Pixmap(1,1, Pixmap.Format.RGB565);
        bgPixmap.setColor(Color.GRAY);
        bgPixmap.fill();
        TextureRegionDrawable textureRegionDrawableBg = new TextureRegionDrawable(new TextureRegion(new Texture(bgPixmap)));
        table.setBackground(textureRegionDrawableBg);
    }
}
