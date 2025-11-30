package mission.commandCenter.improvements;

import java.util.List;

import fileio.CommandInput;

import mission.World;
import mission.commandCenter.ImproveEnvironment;

public class FertilizeSoil extends ImproveEnvironment {
    public FertilizeSoil(World world, CommandInput cmdInput) {
        super(world, cmdInput);
    }

    public String applyImprovement() {
        try {
            String entityName = getEntityName();
            StringBuilder method = new StringBuilder("Method to fertilize soil with ");
            method.append(entityName);
            List<String> factsList = getMyRobot().getTopicFacts(entityName);
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
            getCurrSoil().setOrganicMatter(getCurrSoil().getOrganicMatter() + 0.3);
            return "The soil was successfully fertilized using " + entityName;
        } catch (NoMethodException e) {
            return e.getMessage();
        }
    }
}
