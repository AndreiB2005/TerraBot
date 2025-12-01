package mission.commandCenter;

import com.fasterxml.jackson.databind.node.ObjectNode;
import mission.World;

/**
 * Command that starts a simulation within the World.
 * <p>
 * When executed, this command initializes the current simulation,
 * sets up the world map, and activates the TerraBot.
 * If the simulation has already started, it returns an appropriate message.
 */
public class StartSimulation implements Command {
    private final World myWorld;

    /**
     * Constructs a StartSimulation command for the given world.
     *
     * @param myWorld the World instance in which the simulation will be started
     */
    public StartSimulation(final World myWorld) {
        this.myWorld = myWorld;
    }

    /**
     * Executes the start simulation command.
     * <p>
     * If the simulation has not yet started, it initializes the simulation
     * and marks it as started. If the simulation is already running,
     * an AlreadyStartedException is caught and a message is returned.
     *
     * @param objNode the JSON object node to store the result message
     */
    public void execute(final ObjectNode objNode) {
        String message;
        try {
            if (myWorld.getSimulationStarted()) {
                throw new AlreadyStartedException();
            }
            myWorld.setSimulationStarted(true);
            myWorld.buildSimulation();
            message = "Simulation has started.";
        } catch (AlreadyStartedException e) {
            message = e.getMessage();
        }
        objNode.put("message", message);
    }
}
