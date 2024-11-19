package tech.underoaks.coldcase.state.updates;

/**
 * Exception thrown when a GameStateUpdate fails.
 */
public class GameStateUpdateException extends Exception {
    /**
     * Constructs a new GameStateUpdateException with the specified detail message.
     *
     * @param message The detail message.
     */
    public GameStateUpdateException(String message) {
        super(message);
    }

    /**
     * Constructs a new GameStateUpdateException with the specified detail message and cause.
     *
     * @param message The detail message.
     * @param cause   The cause of the exception.
     */
    public GameStateUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
