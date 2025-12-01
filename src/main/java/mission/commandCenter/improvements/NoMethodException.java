package mission.commandCenter.improvements;

/**
 * Exception thrown when a fact/method is not yet saved in the database.
 */
public class NoMethodException extends Exception {

    /**
     * Constructs a new NoMethodException with a default message.
     */
    public NoMethodException() {
        super("ERROR: Fact not yet saved. Cannot perform action");
    }
}
