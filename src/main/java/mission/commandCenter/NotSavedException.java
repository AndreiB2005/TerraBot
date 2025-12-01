package mission.commandCenter;

/**
 * Exception thrown when a subject has not yet been saved in the database.
 */
public class NotSavedException extends Exception {

    /**
     * Constructs a new NotSavedException with a default message.
     */
    public NotSavedException() {
        super("ERROR: Subject not yet saved. Cannot perform action");
    }
}
