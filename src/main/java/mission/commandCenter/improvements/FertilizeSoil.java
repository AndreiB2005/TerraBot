package mission.commandCenter.improvements;

import java.util.List;

import fileio.CommandInput;

import mission.World;
import mission.commandCenter.ImproveEnvironment;

/**
 * FertilizeSoil represents a specific environment improvement that
 * increases the organic matter of the soil in the current map cell.
 * <p>
 * The improvement requires the TerraBot to have knowledge about the
 * target entity (from which the fertilization method is derived) stored
 * in its database. If no appropriate method is found, the improvement
 * cannot be applied.
 * <p>
 * This class extends {@link ImproveEnvironment} and implements the
 * {@link #applyImprovement()} method to apply the fertilization.
 */
public class FertilizeSoil extends ImproveEnvironment {

    private static final double ORGANIC_MATTER_INCREASE = 0.3;

    /**
     * Constructs a FertilizeSoil improvement command.
     *
     * @param world    the simulation world
     * @param cmdInput the command input containing the target entity name
     */
    public FertilizeSoil(final World world, final CommandInput cmdInput) {
        super(world, cmdInput);
    }

    /**
     * Applies the fertilization improvement.
     * <p>
     * Checks if the TerraBot has a fact describing the fertilization method
     * for the target entity. If found, increases the soil's organic matter.
     * If no method is available, returns the corresponding error message.
     *
     * @return a message describing whether the fertilization was successful or failed
     */
    @Override
    public String applyImprovement() {
        try {
            String entityName = getEntityName();
            StringBuilder method = new StringBuilder("Method to fertilize soil with ");
            method.append(entityName);
            List<String> factsList = getMyRobot().getTopicFacts(entityName);
            if (factsList == null) {
                throw new NoMethodException();
            }
            boolean foundMethod = false;
            for (String fact : factsList) {
                if (method.toString().equals(fact)) {
                    foundMethod = true;
                    break;
                }
            }
            if (!foundMethod) {
                throw new NoMethodException();
            }
            getCurrSoil().setOrganicMatter(
                    getCurrSoil().getOrganicMatter() + ORGANIC_MATTER_INCREASE
            );
            return "The soil was successfully fertilized using " + entityName;
        } catch (NoMethodException e) {
            return e.getMessage();
        }
    }
}
