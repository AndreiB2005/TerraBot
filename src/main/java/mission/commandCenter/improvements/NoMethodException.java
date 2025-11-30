package mission.commandCenter.improvements;

public class NoMethodException extends Exception {
    private final String message;

    public NoMethodException() {
        message = "ERROR: Fact not yet saved. Cannot perform action";
    }

    public String getMessage() {
        return message;
    }
}
