package mission.commandCenter;

/**
 * Exception thrown when an attempt is made to start a simulation
 * that has already been started.
 */
public class AlreadyStartedException extends Exception {

    /**
     * Constructs a new AlreadyStartedException with a default message.
     */
    public AlreadyStartedException() {
        super("ERROR: Simulation already started. Cannot perform action");
    }
}
