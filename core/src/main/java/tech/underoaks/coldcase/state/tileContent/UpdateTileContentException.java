package tech.underoaks.coldcase.state.tileContent;

/**
 * Exception thrown when a GameStateUpdate fails.
 */
public class UpdateTileContentException extends Exception {
    /**
     * Constructs a new GameStateUpdateException with the specified detail message.
     *
     * @param message The detail message.
     */
    public UpdateTileContentException(String message) {
        super(message);
    }

    /**
     * Constructs a new GameStateUpdateException with the specified detail message and cause.
     *
     * @param message The detail message.
     * @param cause   The cause of the exception.
     */
    public UpdateTileContentException(String message, Throwable cause) {
        super(message, cause);
    }
}
