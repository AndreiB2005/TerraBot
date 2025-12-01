package mission.commandCenter;

/**
 * Exception thrown when an action is attempted but the robot is still charging.
 */
public class StillChargingException extends Exception {

    /**
     * Constructs a new StillChargingException with a default message.
     */
    public StillChargingException() {
        super("ERROR: Robot still charging. Cannot perform action");
    }
}
