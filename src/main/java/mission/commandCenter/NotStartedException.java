package mission.commandCenter;

/**
 * Exception thrown when an action is attempted but the simulation has not started.
 */
public class NotStartedException extends Exception {

    /**
     * Constructs a new NotStartedException with a default message.
     */
    public NotStartedException() {
        super("ERROR: Simulation not started. Cannot perform action");
    }
}
