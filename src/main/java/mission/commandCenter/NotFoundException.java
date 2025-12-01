package mission.commandCenter;

/**
 * Exception thrown when a requested object is not found.
 */
public class NotFoundException extends Exception {

    /**
     * Constructs a new NotFoundException with a default message.
     */
    public NotFoundException() {
        super("ERROR: Object not found. Cannot perform action");
    }
}
