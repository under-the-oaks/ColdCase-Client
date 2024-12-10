package tech.underoaks.coldcase.state.tileContent;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.game.Interaction;
import tech.underoaks.coldcase.game.PlayerController;
import tech.underoaks.coldcase.state.InteractionChain;
import tech.underoaks.coldcase.state.updates.AddTileContentUpdate;
import tech.underoaks.coldcase.state.updates.GameStateUpdateException;

/**
 * The {@code PortalObject} class represents a portal within the game world that allows the player
 * to send an item to the other player through the portal.
 *
 * <p>The player can send an item through the portal by interacting with it whilst holding an item.
 * In this case the item gets transferred to the other portal. He can also pick up an item laying on
 * the portal.
 *
 */
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

        // Item empfangen
        if ( interaction.getParameters().length > 0) {

            // Item erstellen
            TileContent newItem = TileContents.getNewTileClassByIndex(interaction.getParameters()[0]);
            chain.addGameStateUpdate( new AddTileContentUpdate( interaction.getTargetPos(), newItem ));

            return true;
        }

        // Item versuchen zu senden

        if ( interaction.getParameters().length == 0 ) {

            // Wenn etwas auf dem Portal liegt
            if (tileContent != null) return false;

            // Wenn nichts auf dem Portal liegt
            else { // tileContent == null

                // Spieler hat kein Item
                if (PlayerController.getInstance().getInventory() == null) return false;

                //Spieler hat Item
                else { // PlayerController.getInstance().getInventory() != null

                    // Inventar auslesen
                    TileContent inventory = PlayerController.getInstance().getInventory();

                    // Index der Klasse auslesen
                    int index = TileContents.getIndexByClass(inventory.getClass());

                    // Inventar leeren
                    PlayerController.getInstance().setInventory(null);

                    // Item auf Remote-Portal erschaffen
                    if (inventory instanceof ItemObject) {
                        chain.getPendingRemoteActions().add(new Interaction(interaction.getTargetPos(), interaction.getActionDirection(), this.getClass(), index));
                    }

                    return true;

                }
            }
        }

        return false;
    }

    @Override
    public boolean update(InteractionChain chain, Vector2 tilePosition, Interaction interaction, TileContent handler) throws GameStateUpdateException, UpdateTileContentException {
        return false;
    }
}
