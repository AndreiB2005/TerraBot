package mission.commandCenter;

import com.fasterxml.jackson.databind.node.ObjectNode;

import mission.World;
import mission.TerraBot;

public class RechargeBattery implements Command {
    private final TerraBot myRobot;
    private final int chargeTime;
    private final boolean simulationStarted;

    public RechargeBattery(World world, int chargeTime) {
        myRobot = world.getMyRobot();
        this.chargeTime = chargeTime;
        simulationStarted = world.getSimulationStarted();
    }

    public void execute(ObjectNode objNode) throws
            NotStartedException, StillChargingException {
        if (!simulationStarted)
            throw new NotStartedException();
        if (myRobot.getRechargeTime() > 0)
            throw new StillChargingException();
        myRobot.setRechargeTime(chargeTime);
        myRobot.setBattery(myRobot.getBattery() + chargeTime);
        objNode.put("message", "Robot battery is charging.");
    }
}
