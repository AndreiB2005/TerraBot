package mission.commandCenter;

public class NotSavedException extends Exception {
    private final String message;

    public NotSavedException() {
        message = "ERROR: Subject not yet saved. Cannot perform action";
    }

    public String getMessage() {
        return message;
    }
}
