package tech.underoaks.coldcase.stages;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import tech.underoaks.coldcase.Main;
import tech.underoaks.coldcase.game.TextureController;
import tech.underoaks.coldcase.game.UITextureController;

import java.util.Properties;

public class HostStage extends AbstractStage {

    private final Skin skin = UITextureController.getInstance().getSkin();
    private TextField sessionIDField;
    private TextButton startButton;
    private Label hostLabel;
    private Image hostImage;
    private Label teammateLabel;
    private Image teammateImage;
    private Button switchButton;
    private TextButton backButton;

    @Override
    public void buildStage(InputMultiplexer inputMultiplexer) {

        //TODO: start connection to Server here

        String sessionID = getSessionID();

        Table table = new Table();
        table.setFillParent(true); // Fill the entire stage
        this.addActor(table);

        //button size
        Vector2 buttonSize = UITextureController.getInstance().getButtonSize(getWidth(), getHeight());
        float buttonWidth = buttonSize.x;
        float buttonHeight = buttonSize.y;

        // TextField for Session ID (non-editable)
        // TODO@jean-luc: Replace with actual session ID
        sessionIDField = new TextField("SessionID: " + sessionID, skin);
        sessionIDField.setDisabled(true); // Make it non-editable
        sessionIDField.setAlignment(1); // Center the text

        // 2. Image and Caption
        hostImage = new Image(TextureController.getInstance().getPlayerTexture()); // Replace with your image path
        hostLabel = new Label( TextureController.getIsDetective()? "You are the detective" : "You are the ghost", skin);
        hostLabel.setAlignment(1); // Center the text

        teammateImage = new Image(TextureController.getInstance().getGhostTexture());
        teammateLabel = new Label("Your teammate is the ghost", skin);
        teammateLabel.setAlignment(1); // Center the text

        switchButton = new Button(skin.get("reload", Button.ButtonStyle.class));
        switchButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //TODO: switch roles on both clients
            }
        });

        // 3. Start Button
        startButton = new TextButton("Start", skin);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                StageManager.getInstance().showScreen(Stages.GAME);
                //TODO: start game on both clients
            }
        });

        // 4. Back Button
        backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                StageManager.getInstance().showScreen(Stages.MAIN_MENU);
            }
        });

        Table playerSelectionTable = new Table();
        playerSelectionTable.add(hostImage).width(1080*4).height(1080*4);
        playerSelectionTable.add(switchButton).width(1080).height(1080);
        playerSelectionTable.add(teammateImage).width(1080*4).height(1080*4);
        playerSelectionTable.row();
        playerSelectionTable.add(hostLabel);
        playerSelectionTable.add();
        playerSelectionTable.add(teammateLabel);

        // Arrange components in the table
        table.add(sessionIDField).width(buttonWidth).height(buttonHeight).padBottom(20);
        table.row();
        table.add(playerSelectionTable).padBottom(20);
        table.row();
        table.add(startButton).width(buttonWidth).height(buttonHeight).fillX().uniformX();
        table.row();
        table.add(backButton).width(buttonWidth).height(buttonHeight).fillX().uniformX();

        Pixmap bgPixmap = new Pixmap(1,1, Pixmap.Format.RGB565);
        bgPixmap.setColor(Color.GRAY);
        bgPixmap.fill();
        TextureRegionDrawable textureRegionDrawableBg = new TextureRegionDrawable(new TextureRegion(new Texture(bgPixmap)));
        table.setBackground(textureRegionDrawableBg);

    }

    private String getSessionID() {
        Properties properties = Main.properties;
        if(!properties.containsKey("session_id")) {
            throw new RuntimeException("Missing required property role");
        }
        return properties.getProperty("session_id");
    }
}
