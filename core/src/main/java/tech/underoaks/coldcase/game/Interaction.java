package tech.underoaks.coldcase.game;

import com.badlogic.gdx.math.Vector2;
import tech.underoaks.coldcase.state.tileContent.TileContent;

/**
 * Represents an interaction between two objects in the game world.
 */
public class Interaction {
    /**
     * The position of the target TileContent.
     */
    private Vector2 targetPos;
    /**
     * The direction the action is supposed to be going in.
     */
    private Direction actionDirection;
    /**
     * The object that initiated the interaction.
     */
    private TileContent caller;
    /**
     * Additional parameters for the interaction. Each TileContent can use these parameters in its action method.
     */
    private int[] parameters;

    /**
     * Default constructor. For serialization purposes.
     */
    public Interaction() {
    }

    public Interaction(Vector2 targetPos, Direction actionDirection, TileContent caller, int... parameters) {
        this.targetPos = targetPos.cpy(); // this is so that the targetPos can't be changed from outside do to the Vector2 being mutable and our game handling interactions asynchronously
        this.actionDirection = actionDirection;
        this.caller = caller;
        this.parameters = parameters;
    }

    public Vector2 getTargetPos() {
        return targetPos;
    }

    public void setTargetPos(Vector2 targetPos) {
        this.targetPos = targetPos;
    }

    public Direction getActionDirection() {
        return actionDirection;
    }

    public void setActionDirection(Direction actionDirection) {
        this.actionDirection = actionDirection;
    }

    public TileContent getCaller() {
        return caller;
    }

    public void setCaller(TileContent caller) {
        this.caller = caller;
    }

    public int[] getParameters() {
        return parameters;
    }

    public void setParameters(int[] parameters) {
        this.parameters = parameters;
    }
}
