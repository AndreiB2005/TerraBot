package mission.commandCenter;

import com.fasterxml.jackson.databind.node.ObjectNode;
import mission.World;

/**
 * Command that ends a running simulation within the World.
 * <p>
 * When executed, this command stops the current simulation.
 * If the simulation has not been started yet, a NotStartedException is thrown.
 */
public class EndSimulation implements Command {
    private final World myWorld;

    /**
     * Constructs an EndSimulation command for the given world.
     *
     * @param myWorld the World instance in which the simulation will be ended
     */
    public EndSimulation(final World myWorld) {
        this.myWorld = myWorld;
    }

    /**
     * Executes the end simulation command.
     * <p>
     * If the simulation is running, it is stopped and a success message
     * is added to the provided JSON object node. If the simulation has
     * not been started, a NotStartedException is thrown.
     *
     * @param objNode the JSON object node to store the result message
     * @throws NotStartedException if the simulation has not been started
     */
    public void execute(final ObjectNode objNode) throws NotStartedException {
        if (!myWorld.getSimulationStarted()) {
            throw new NotStartedException();
        }
        myWorld.setSimulationStarted(false);
        objNode.put("message", "Simulation has ended.");
    }
}
