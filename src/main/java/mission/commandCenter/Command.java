package mission.commandCenter;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Represents a generic command that can be executed by the CommandHandler
 * in a simulation environment. Commands encapsulate a specific action
 * such as starting the simulation, moving the robot, scanning an object,
 * or changing environmental conditions.
 */
public interface Command {

    /**
     * Executes the command and populates the provided JSON object node
     * with the command's result or status.
     *
     * @param objNode the JSON object node to store output information
     * @throws NotStartedException if the command requires a simulation
     *         to be started, but it hasn't been started yet
     * @throws StillChargingException if the command requires the robot to
     *         act but the TerraBot is still charging
     */
    void execute(ObjectNode objNode) throws NotStartedException, StillChargingException;
}
