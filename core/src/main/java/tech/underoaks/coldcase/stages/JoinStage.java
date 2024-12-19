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
import tech.underoaks.coldcase.game.TextureController;
import tech.underoaks.coldcase.game.UITextureController;

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
                String sessionID = sessionIDField.getText();
                System.out.println("Connecting to session: " + sessionID);
                // TODO: Connect to the server
            }
        });

        //TODO: sync character selection
        //Image and Caption
        hostImage = new Image(TextureController.getInstance().getPlayerTexture());
        hostLabel = new Label( TextureController.getIsDetective()? "You are the detective" : "You are the ghost", skin);
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
        playerSelectionTable.add(hostImage).width(1080*4).height(1080*4);
        playerSelectionTable.add().width(1080).height(1080);
        playerSelectionTable.add(teammateImage).width(1080*4).height(1080*4);
        playerSelectionTable.row();
        playerSelectionTable.add(hostLabel);
        playerSelectionTable.add();
        playerSelectionTable.add(teammateLabel);

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
