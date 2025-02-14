package tech.underoaks.coldcase.game;

import com.badlogic.gdx.math.Vector2;

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
    private String caller;
    /**
     * Additional parameters for the interaction. Each TileContent can use these parameters in its action method.
     */
    private int[] parameters;

    /**
     * UUID of the executing GameController
     */
    private String uuid;

    /**
     * Default constructor. For serialization purposes.
     */
    public Interaction() {
    }

    /**
     * Constructs an Interaction with the specified target position, action direction, caller, and additional parameters.
     * <p>
     * The target position is copied to prevent external modifications. The caller is stored as its fully-qualified class name.
     * The UUID is set based on the current GameController instance.
     * </p>
     *
     * @param targetPos       the target position for the interaction
     * @param actionDirection the direction in which the action should be performed
     * @param caller          the class representing the object that initiated the interaction
     * @param parameters      additional parameters for the interaction
     */
    public Interaction(Vector2 targetPos, Direction actionDirection, Class<?> caller, int... parameters) {
        this.targetPos = targetPos.cpy(); // this is so that the targetPos can't be changed from outside do to the Vector2 being mutable and our game handling interactions asynchronously
        this.actionDirection = actionDirection;
        this.caller = caller.getName();
        this.parameters = parameters;
        this.uuid = GameController.getInstance().uuid.toString();
    }

    /**
     * Returns a copy of the target position for this interaction.
     * <p>
     * A copy is returned to maintain immutability since {@code Vector2} is mutable.
     * </p>
     *
     * @return a {@link Vector2} representing the target position
     */
    public Vector2 getTargetPos() {
        return targetPos.cpy();
    }

    /**
     * Sets the target position for this interaction.
     * <p>
     * A copy of the provided vector is used to prevent external modifications.
     * </p>
     *
     * @param targetPos the {@link Vector2} representing the new target position
     */
    public void setTargetPos(Vector2 targetPos) {
        this.targetPos = targetPos.cpy();
    }

    /**
     * Returns the direction of the action for this interaction.
     *
     * @return the action direction as a {@code Direction} enum value
     */
    public Direction getActionDirection() {
        return actionDirection;
    }


    /**
     * Sets the direction for the action of this interaction.
     *
     * @param actionDirection the {@code Direction} to set for the action
     */
    public void setActionDirection(Direction actionDirection) {
        this.actionDirection = actionDirection;
    }

    /**
     * Returns the fully-qualified name of the caller that initiated this interaction.
     *
     * @return the caller's class name as a {@code String}
     */
    public String getCaller() {
        return caller;
    }

    /**
     * Sets the caller for this interaction.
     * <p>
     * The caller is recorded as the fully-qualified name of the provided class.
     * </p>
     *
     * @param caller the class representing the caller initiating the interaction
     */
    public void setCaller(Class<?> caller) {
        this.caller = caller.getName();
    }

    /**
     * Returns the additional parameters associated with this interaction.
     *
     * @return an array of integers representing the parameters
     */
    public int[] getParameters() {
        return parameters;
    }

    /**
     * Sets the additional parameters for this interaction.
     *
     * @param parameters an array of integers representing the parameters
     */
    public void setParameters(int[] parameters) {
        this.parameters = parameters;
    }

    /**
     * Returns the UUID of the executing GameController associated with this interaction.
     *
     * @return the UUID as a {@code String}
     */
    public String getUuid() {
        return uuid;
    }
}
