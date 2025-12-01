package mission.commandCenter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mission.World;
import mission.TerraBot;

/**
 * Command that prints the TerraBot's knowledge base.
 * <p>
 * This command outputs all facts the robot has learned and stored about
 * various scanned entities. The facts are organized by topic (entity name)
 * and listed in the order they were saved.
 * <p>
 * Throws a NotStartedException if the simulation has not started,
 * or a StillChargingException if the robot is currently recharging.
 */
public class PrintKnowledgeBase implements Command {
    private final ObjectMapper mapper;
    private final TerraBot myRobot;
    private final boolean simulationStarted;

    /**
     * Constructs a PrintKnowledgeBase command for a given simulation world.
     *
     * @param world the World instance containing the simulation
     */
    public PrintKnowledgeBase(final World world) {
        mapper = world.getMapper();
        myRobot = world.getMyRobot();
        simulationStarted = world.getSimulationStarted();
    }

    /**
     * Executes the PrintKnowledgeBase command.
     * <p>
     * Throws a NotStartedException if the simulation has not started,
     * or a StillChargingException if the robot is recharging.
     * The command uses the TerraBot's {@code printData} method to populate
     * the provided JSON object node with all learned facts.
     *
     * @param objNode the JSON object node to store the knowledge base
     * @throws NotStartedException    if the simulation has not started
     * @throws StillChargingException if the robot is recharging
     */
    public void execute(final ObjectNode objNode) throws
            NotStartedException, StillChargingException {
        if (!simulationStarted) {
            throw new NotStartedException();
        }
        if (myRobot.getRechargeTime() > 0) {
            throw new StillChargingException();
        }

        myRobot.printData(mapper, objNode);
    }
}
