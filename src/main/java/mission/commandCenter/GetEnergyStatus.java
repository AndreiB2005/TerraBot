package mission.commandCenter;

import com.fasterxml.jackson.databind.node.ObjectNode;
import mission.World;
import mission.TerraBot;

/**
 * Command that prints the current energy status of the TerraBot.
 * <p>
 * When executed, this command retrieves the remaining battery points
 * of the TerraBot and returns them as a message in the JSON output.
 * <p>
 * The command can only be executed if the simulation has started and
 * the TerraBot is not currently recharging.
 */
public class GetEnergyStatus implements Command {
    private final TerraBot robot;
    private final boolean simulationStarted;

    /**
     * Constructs a GetEnergyStatus command for a given world.
     *
     * @param world the World instance containing the simulation and TerraBot
     */
    public GetEnergyStatus(final World world) {
        robot = world.getMyRobot();
        simulationStarted = world.getSimulationStarted();
    }

    /**
     * Executes the command to print the remaining energy of the TerraBot.
     * <p>
     * Throws a NotStartedException if the simulation is not started, or
     * a StillChargingException if the TerraBot is recharging.
     * On success, the JSON object node is populated with a message indicating
     * the current energy points of the TerraBot.
     *
     * @param objNode the JSON object node to store the energy status message
     * @throws NotStartedException    if the simulation has not started
     * @throws StillChargingException if the TerraBot is currently recharging
     */
    public void execute(final ObjectNode objNode) throws
            NotStartedException, StillChargingException {
        if (!simulationStarted) {
            throw new NotStartedException();
        }
        if (robot.getRechargeTime() > 0) {
            throw new StillChargingException();
        }
        objNode.put("message", "TerraBot has " + robot.getBattery()
                + " energy points left.");
    }
}
