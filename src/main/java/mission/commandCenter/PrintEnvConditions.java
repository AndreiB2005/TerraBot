package mission.commandCenter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import mission.TerraBot;
import mission.World;
import map.MapCell;

/**
 * Command that prints the environmental conditions of a specific map cell.
 * <p>
 * When executed, this command collects information about the soil, plants,
 * animals, water, and air present in the cell where the TerraBot is located,
 * and returns it as a structured JSON object.
 * <p>
 * The command can only be executed if the simulation has started and the
 * TerraBot is not currently recharging.
 */
public class PrintEnvConditions implements Command {
    private final ObjectMapper mapper;
    private final MapCell cell;
    private final TerraBot robot;
    private final boolean simulationStarted;

    /**
     * Constructs a PrintEnvConditions command for a given world and map cell.
     *
     * @param world the World instance containing the simulation and TerraBot
     * @param cell  the MapCell whose environmental conditions will be printed
     */
    public PrintEnvConditions(final World world, final MapCell cell) {
        mapper = world.getMapper();
        this.cell = cell;
        robot = world.getMyRobot();
        simulationStarted = world.getSimulationStarted();
    }

    /**
     * Executes the command to print environmental conditions.
     * <p>
     * Throws a NotStartedException if the simulation is not started, or
     * a StillChargingException if the TerraBot is recharging.
     * On success, the environmental data of soil, plants, animals, water,
     * and air are added to the provided JSON object node.
     *
     * @param objNode the JSON object node to store the environment data
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
        ObjectNode output = mapper.createObjectNode();
        ObjectNode plantNode;
        ObjectNode animalNode;
        ObjectNode waterNode;
        ObjectNode soilNode;
        ObjectNode airNode;
        soilNode = cell.getSoil().printSoil(mapper);
        output.set("soil", soilNode);
        if (cell.getPlant() != null) {
            plantNode = cell.getPlant().printEntity(mapper);
            output.set("plants", plantNode);
        }
        if (cell.getAnimal() != null) {
            animalNode = cell.getAnimal().printEntity(mapper);
            output.set("animals", animalNode);
        }
        if (cell.getWater() != null) {
            waterNode = cell.getWater().printEntity(mapper);
            output.set("water", waterNode);
        }
        airNode = cell.getAir().printAir(mapper);
        output.set("air", airNode);
        objNode.set("output", output);
    }
}
