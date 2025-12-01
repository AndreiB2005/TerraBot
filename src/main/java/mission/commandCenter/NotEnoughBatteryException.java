package mission.commandCenter;

/**
 * Exception thrown when the robot does not have enough battery
 * to perform an action.
 */
public class NotEnoughBatteryException extends Exception {

    /**
     * Constructs a new NotEnoughBatteryException with a default message.
     */
    public NotEnoughBatteryException() {
        super("ERROR: Not enough battery left. Cannot perform action");
    }
}
