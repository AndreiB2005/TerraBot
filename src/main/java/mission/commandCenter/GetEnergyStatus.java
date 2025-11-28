package mission.commandCenter;

import com.fasterxml.jackson.databind.node.ObjectNode;

import mission.World;
import mission.TerraBot;

public class GetEnergyStatus implements Command {
    private final TerraBot robot;
    private final boolean simulationStarted;

    public GetEnergyStatus(World world) {
        robot = world.getMyRobot();
        simulationStarted = world.getSimulationStarted();
    }

    public void execute(ObjectNode objNode) throws
            NotStartedException, StillChargingException {
        if (!simulationStarted)
            throw new NotStartedException();
        if (robot.getRechargeTime() > 0)
            throw new StillChargingException();
        objNode.put("message", "TerraBot has " + robot.getBattery() +
                " energy points left.");
    }
}
