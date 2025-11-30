package mission.commandCenter;

public class NotEnoughEnergyException extends Exception {
    private final String message;

    public NotEnoughEnergyException() {
        message = "ERROR: Not enough energy to perform action";
    }

    public String getMessage() {
        return message;
    }
}
