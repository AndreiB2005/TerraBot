package mission.commandCenter;

public class AlreadyStartedException extends Exception {
    private final String errorMessage;

    public AlreadyStartedException() {
        this.errorMessage = "ERROR: Simulation already started. Cannot perform action";
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
