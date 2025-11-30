package mission.commandCenter.improvements;

import java.util.List;

import fileio.CommandInput;

import mission.World;
import mission.commandCenter.ImproveEnvironment;

public class IncreaseHumidity extends ImproveEnvironment {
    public IncreaseHumidity(World world, CommandInput cmdInput) {
        super(world, cmdInput);
    }

    public String applyImprovement() {
        try {
            String entityName = getEntityName();
            String method = "Method to increase humidity.";
            List<String> factsList = getMyRobot().getTopicFacts(entityName);
            if (factsList == null)
                throw new NoMethodException();
            boolean foundMethod = false;
            for (String fact : factsList) {
                if (method.equals(fact)) {
                    foundMethod = true;
                    break;
                }
            }
            if (!foundMethod)
                throw new NoMethodException();
            getCurrAir().setHumidity(getCurrAir().getHumidity() + 0.2);
            return "The humidity was successfully increased using " + entityName;
        } catch (NoMethodException e) {
            return e.getMessage();
        }
    }
}
