package mission.commandCenter;

public class NotFoundException extends Exception {
    private final String message;

    public NotFoundException() {
        message = "ERROR: Object not found. Cannot perform action";
    }

    public String getMessage() {
        return message;
    }
}
