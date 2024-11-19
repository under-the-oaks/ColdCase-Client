package tech.underoaks.coldcase;

import tech.underoaks.coldcase.game.GameController;
import tech.underoaks.coldcase.game.PlayerController;
import tech.underoaks.coldcase.state.Map;
import tech.underoaks.coldcase.state.tileContent.Player;

import java.nio.file.Path;

/**
 * {@code HeadlessApplicationListener} ist eine Implementierung der Hauptanwendung
 * für eine kopflose Umgebung (ohne grafische Ausgabe).
 * <p>
 * Diese Klasse erweitert {@link Main} und wird verwendet, um das Spiel im
 * "Headless"-Modus zu starten, was nützlich für Tests oder Server-Betrieb sein kann.
 * Sie lädt die Karte, initialisiert den {@link GameController} und setzt die
 * Position des Spielers.
 * </p>
 *
 * @see Main
 * @see GameController
 * @see Map
 */
public class HeadlessApplicationListener extends Main {

    public GameController gameController;

    @Override
    public void create() {
        Map map = Map.getMap(Path.of("maps/test_plain"));
        gameController = GameController.getInstance();
        gameController.setCurrentMap(map);

        PlayerController.getInstance().setPlayerPosition(gameController.getCurrentMap().getTileContentByType(Player.class));
    }

    @Override
    public void render() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void dispose() {
        gameController.getCurrentMap().dispose();
    }
}
