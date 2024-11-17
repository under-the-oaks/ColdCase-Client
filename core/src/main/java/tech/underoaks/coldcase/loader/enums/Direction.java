package tech.underoaks.coldcase.loader.enums;

import com.badlogic.gdx.math.Vector2;

/**
 * Represents the cardinal directions used for movement and orientation in the game.
 */
public enum Direction {
    WEST, EAST, NORTH, SOUTH;

    public Vector2 getVector() {
        return switch (this) {
            case NORTH -> new Vector2(-1, 0);
            case SOUTH -> new Vector2(1, 0);
            case EAST -> new Vector2(0, 1);
            case WEST -> new Vector2(-0, -1);
        };
    }
}
