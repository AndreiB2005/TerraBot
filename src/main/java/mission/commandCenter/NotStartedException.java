package mission.commandCenter;

public class NotStartedException extends Exception {
    private final String errorMessage;

    public NotStartedException() {
        this.errorMessage = "ERROR: Simulation not started. Cannot perform action";
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
