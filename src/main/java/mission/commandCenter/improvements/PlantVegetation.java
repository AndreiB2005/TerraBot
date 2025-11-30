package mission.commandCenter.improvements;

import java.util.List;

import fileio.CommandInput;

import mission.World;
import mission.commandCenter.ImproveEnvironment;

public class PlantVegetation extends ImproveEnvironment {
    public PlantVegetation(World world, CommandInput cmdInput) {
        super(world, cmdInput);
    }

    public String applyImprovement() {
        try {
            String entityName = this.getEntityName();
            StringBuilder method = new StringBuilder("Method to plant ");
            method.append(entityName);
            List<String> factsList = this.getMyRobot().getTopicFacts(entityName);
            if (factsList == null)
                throw new NoMethodException();
            boolean foundMethod = false;
            for (String fact : factsList) {
                if (method.toString().equals(fact)) {
                    foundMethod = true;
                    break;
                }
            }
            if (!foundMethod)
                throw new NoMethodException();
            getCurrAir().setOxygenLevel(getCurrAir().getOxygenLevel() + 0.3);
            return "The " + entityName + " was planted successfully.";
        } catch (NoMethodException e) {
            return e.getMessage();
        }
    }
}
