package mission.commandCenter;

public class NotEnoughBatteryException extends Exception {
    private final String message;

    public NotEnoughBatteryException() {
        message = "ERROR: Not enough battery left. Cannot perform action";
    }

    public String getMessage() {
        return message;
    }
}
