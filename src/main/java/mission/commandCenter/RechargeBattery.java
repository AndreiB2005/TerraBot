package mission.commandCenter;

import com.fasterxml.jackson.databind.node.ObjectNode;
import mission.World;
import mission.TerraBot;

/**
 * Command that recharges the TerraBot's battery.
 * <p>
 * When executed, this command increases the battery of the TerraBot by a specified
 * charge time and sets the recharge countdown. The command can only be executed
 * if the simulation has started and the TerraBot is not already recharging.
 */
public class RechargeBattery implements Command {
    private final TerraBot myRobot;
    private final int chargeTime;
    private final boolean simulationStarted;

    /**
     * Constructs a RechargeBattery command for a given world and charge time.
     *
     * @param world      the World instance containing the simulation and TerraBot
     * @param chargeTime the number of energy points to recharge the battery
     */
    public RechargeBattery(final World world, final int chargeTime) {
        myRobot = world.getMyRobot();
        this.chargeTime = chargeTime;
        simulationStarted = world.getSimulationStarted();
    }

    /**
     * Executes the command to recharge the TerraBot's battery.
     * <p>
     * Throws a NotStartedException if the simulation has not started, or a
     * StillChargingException if the TerraBot is currently recharging.
     * On success, the battery is incremented by the charge time, the recharge timer
     * is set, and a message is added to the JSON object node.
     *
     * @param objNode the JSON object node to store the charging message
     * @throws NotStartedException    if the simulation has not started
     * @throws StillChargingException if the TerraBot is currently recharging
     */
    public void execute(final ObjectNode objNode) throws
            NotStartedException, StillChargingException {
        if (!simulationStarted) {
            throw new NotStartedException();
        }
        if (myRobot.getRechargeTime() > 0) {
            throw new StillChargingException();
        }
        myRobot.setRechargeTime(chargeTime);
        myRobot.setBattery(myRobot.getBattery() + chargeTime);
        objNode.put("message", "Robot battery is charging.");
    }
}
