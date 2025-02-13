package tech.underoaks.coldcase.game;

import com.badlogic.gdx.math.Vector2;

/**
 * Represents the cardinal directions used for movement and orientation in the game.
 */
public enum Direction {
    /** West-Direction */
    WEST,
    /** East-Direction */
    EAST,
    /** North-Direction */
    NORTH,
    /** South-Direction */
    SOUTH;

    /**
     * Gets a normalized Vector representing the specified Direction
     * @return Vector of the given Direction
     */
    public Vector2 getVector() {
        return switch (this) {
            case NORTH -> new Vector2(-1, 0);
            case SOUTH -> new Vector2(1, 0);
            case EAST -> new Vector2(0, 1);
            case WEST -> new Vector2(-0, -1);
        };
    }
}
