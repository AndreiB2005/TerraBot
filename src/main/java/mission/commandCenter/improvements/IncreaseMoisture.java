package mission.commandCenter.improvements;

import java.util.List;

import fileio.CommandInput;

import mission.World;
import mission.commandCenter.ImproveEnvironment;

/**
 * IncreaseMoisture represents a specific environment improvement that
 * increases the moisture level (water retention) of the soil in the current map cell.
 */
public class IncreaseMoisture extends ImproveEnvironment {

    private static final double WATER_RETENTION_INCREASE = 0.2;
    private static final String METHOD_STRING = "Method to increase moisture.";

    public IncreaseMoisture(final World world, final CommandInput cmdInput) {
        super(world, cmdInput);
    }

    /**
     * Applies the moisture improvement.
     * <p>
     * Checks if the TerraBot has a fact describing the method to increase
     * moisture for the target entity. If found, increases the soil's water retention by 0.2.
     * If no method is available, returns the corresponding error message.
     *
     * @return a message describing whether the moisture increase was successful or failed
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

            getCurrSoil().setWaterRetention(
                    getCurrSoil().getWaterRetention() + WATER_RETENTION_INCREASE
            );

            return "The moisture was successfully increased using " + entityName;
        } catch (NoMethodException e) {
            return e.getMessage();
        }
    }
}
