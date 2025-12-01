package mission.commandCenter;

/**
 * Exception thrown when the robot does not have enough energy
 * to perform an action.
 */
public class NotEnoughEnergyException extends Exception {

    /**
     * Constructs a new NotEnoughEnergyException with a default message.
     */
    public NotEnoughEnergyException() {
        super("ERROR: Not enough energy to perform action");
    }
}
