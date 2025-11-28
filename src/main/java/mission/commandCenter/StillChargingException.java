package mission.commandCenter;

public class StillChargingException extends Exception {
    private final String message;

    public StillChargingException() {
        message = "ERROR: Robot still charging. Cannot perform action";
    }

    public String getMessage() {
        return message;
    }
}
