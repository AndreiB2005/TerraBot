package mission.commandCenter;

public class NotStartedException extends Exception {
    private final String message;

    public NotStartedException() {
        message = "ERROR: Simulation not started. Cannot perform action";
    }

    public String getMessage() {
        return message;
    }
}
