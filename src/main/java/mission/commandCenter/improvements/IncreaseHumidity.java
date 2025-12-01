package mission.commandCenter.improvements;

import java.util.List;

import fileio.CommandInput;

import mission.World;
import mission.commandCenter.ImproveEnvironment;

/**
 * IncreaseHumidity represents a specific environment improvement that
 * increases the humidity of the air in the current map cell.
 */
public class IncreaseHumidity extends ImproveEnvironment {

    private static final double HUMIDITY_INCREASE = 0.2;
    private static final String METHOD_STRING = "Method to increase humidity.";

    public IncreaseHumidity(final World world, final CommandInput cmdInput) {
        super(world, cmdInput);
    }

    /**
     * Applies the humidity improvement.
     * <p>
     * Checks if the TerraBot has a fact describing the method to increase
     * humidity for the target entity. If found, increases the air's humidity by 0.2.
     * If no method is available, returns the corresponding error message.
     *
     * @return a message describing whether the humidity increase was successful or failed
     */
    @Override
    public String applyImprovement() {
        try {
            String entityName = getEntityName();
            List<String> factsList = getMyRobot().getTopicFacts(entityName);

            if (factsList == null) {
                throw new NoMethodException();
            }

            boolean foundMethod = false;
            for (String fact : factsList) {
                if (METHOD_STRING.equals(fact)) {
                    foundMethod = true;
                    break;
                }
            }

            if (!foundMethod) {
                throw new NoMethodException();
            }

            getCurrAir().setHumidity(
                    getCurrAir().getHumidity() + HUMIDITY_INCREASE
            );

            return "The humidity was successfully increased using " + entityName;
        } catch (NoMethodException e) {
            return e.getMessage();
        }
    }
}
