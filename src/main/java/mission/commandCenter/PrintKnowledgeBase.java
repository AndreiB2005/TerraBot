package mission.commandCenter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mission.World;
import mission.TerraBot;

public class PrintKnowledgeBase implements Command {
    private final ObjectMapper mapper;
    private final TerraBot myRobot;
    private final boolean simulationStarted;

    public PrintKnowledgeBase(World world) {
        mapper = world.getMapper();
        myRobot = world.getMyRobot();
        simulationStarted = world.getSimulationStarted();
    }

    public void execute(ObjectNode objNode) throws
            NotStartedException, StillChargingException {
        if (!simulationStarted)
            throw new NotStartedException();
        if (myRobot.getRechargeTime() > 0)
            throw new StillChargingException();
        myRobot.printData(mapper, objNode);
    }
}
