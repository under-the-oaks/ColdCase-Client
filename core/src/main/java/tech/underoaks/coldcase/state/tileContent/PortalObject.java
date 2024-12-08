package tech.underoaks.coldcase.state.tileContent;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.game.Interaction;
import tech.underoaks.coldcase.game.PlayerController;
import tech.underoaks.coldcase.remote.RemoteGameController;
import tech.underoaks.coldcase.state.InteractionChain;
import tech.underoaks.coldcase.state.updates.AddTileContentUpdate;
import tech.underoaks.coldcase.state.updates.GameStateUpdateException;
import tech.underoaks.coldcase.state.updates.RemoveTileContentUpdate;

public class PortalObject extends TileContent{

    static final Texture texture = new Texture("./sprites/block_ghost.png");

    static final Sprite sprite = new Sprite(texture);

    public PortalObject() {

        super(texture, false, false);

    }

    @Override
    public void render(SpriteBatch batch, float x, float y) {

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

        System.out.println("Portal Action\n");

        // Item empfangen

        if ( interaction.getParameters().length > 0) {

            System.out.println("Erhalte Item!");

            // Erhalte Glove Item

            if (interaction.getParameters() [0] == 1) {

                chain.addGameStateUpdate(
                    new AddTileContentUpdate( interaction.getTargetPos(), new GloveItem() )
                );

                System.out.println("    Glove erthalten!");

                System.out.println(tileContent);

                return true;
            }

            System.out.println( "   Item nicht bekannt!" );

            return false;
        }

        // Item versuchen zu senden

        else if ( interaction.getParameters().length == 0 ) {

            System.out.println( "Versuche Item zu senden!" );

            // Wenn etwas auf dem Portal liegt

            if (tileContent != null) {

                System.out.println("    Portal belegt!");

                return false;
            }

            // Wenn nichts auf dem Portal liegt

            else { // tileContent == null

                System.out.println("    Portal frei!");

                // Spieler hat kein Item

                if (PlayerController.getInstance().getInventory() == null) {

                    System.out.println("        Kein Item zum uebertragen");

                    return false;
                }

                //Spieler hat Item

                else { // PlayerController.getInstance().getInventory() != null

                    System.out.println("        Starte Item-Uebertragung!");

                    // Inventar auslesen

                    TileContent inventory = PlayerController.getInstance().getInventory();

                    // Inventar leeren

                    PlayerController.getInstance().setInventory(null);

                    // Item auf Remote-Portal erschaffen

                    chain.getPendingRemoteActions().add(new Interaction(interaction.getTargetPos(), interaction.getActionDirection(), this.getClass(),1));

                    System.out.println("            Item versendet!");

                    return true;

                }

            }

        }

        return false;
    }

    @Override
    public boolean update(InteractionChain chain, Vector2 tilePosition) throws GameStateUpdateException, UpdateTileContentException {
        return false;
    }
}
