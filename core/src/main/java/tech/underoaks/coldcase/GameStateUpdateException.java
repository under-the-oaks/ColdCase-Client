package tech.underoaks.coldcase;

public class GameStateUpdateException extends Exception {
    public GameStateUpdateException(String message) {
        super(message);
    }

    public GameStateUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
