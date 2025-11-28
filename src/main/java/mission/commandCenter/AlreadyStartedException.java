package mission.commandCenter;

public class AlreadyStartedException extends Exception {
    private final String message;

    public AlreadyStartedException() {
        message = "ERROR: Simulation already started. Cannot perform action";
    }

    public String getMessage() {
        return message;
    }
}
