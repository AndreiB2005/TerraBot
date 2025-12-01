package mission.commandCenter.improvements;

import java.util.List;

import fileio.CommandInput;

import mission.World;
import mission.commandCenter.ImproveEnvironment;

/**
 * PlantVegetation represents a specific environment improvement that
 * plants a given vegetation (entity) in the current map cell.
 * <p>
 * The improvement requires the TerraBot to have knowledge about the
 * target entity (from which the method to plant it is derived)
 * stored in its database. If no appropriate method is found, the improvement
 * cannot be applied.
 * <p>
 * This class extends {@link ImproveEnvironment} and implements the
 * {@link #applyImprovement()} method to plant the vegetation and increase
 * the oxygen level in the current air cell.
 */
public class PlantVegetation extends ImproveEnvironment {

    private static final double OXYGEN_INCREASE = 0.3;

    public PlantVegetation(final World world, final CommandInput cmdInput) {
        super(world, cmdInput);
    }

    /**
     * Applies the vegetation planting improvement.
     * <p>
     * Checks if the TerraBot has a fact describing the method to plant
     * the target entity. If found, increases the air's oxygen level by 0.3.
     * If no method is available, returns the corresponding error message.
     *
     * @return a message describing whether the planting was successful or failed
     */
    @Override
    public String applyImprovement() {
        try {
            String entityName = this.getEntityName();
            String method = "Method to plant " + entityName;

            List<String> factsList = this.getMyRobot().getTopicFacts(entityName);
            if (factsList == null) {
                throw new NoMethodException();
            }

            boolean foundMethod = false;
            for (String fact : factsList) {
                if (method.equals(fact)) {
                    foundMethod = true;
                    break;
                }
            }

            if (!foundMethod) {
                throw new NoMethodException();
            }

            getCurrAir().setOxygenLevel(
                    getCurrAir().getOxygenLevel() + OXYGEN_INCREASE
            );

            return "The " + entityName + " was planted successfully.";
        } catch (NoMethodException e) {
            return e.getMessage();
        }
    }
}
