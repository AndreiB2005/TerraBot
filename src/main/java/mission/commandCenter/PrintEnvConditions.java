package mission.commandCenter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mission.TerraBot;
import mission.World;
import worldMap.MapCell;
import entities.Air;

public class PrintEnvConditions implements Command {
    private final ObjectMapper mapper;
    private final MapCell cell;
    private final TerraBot robot;
    private final boolean simulationStarted;

    public PrintEnvConditions(World world, MapCell cell) {
        mapper = world.getMapper();
        this.cell = cell;
        robot = world.getMyRobot();
        simulationStarted = world.getSimulationStarted();
    }

    public void execute(ObjectNode objNode) throws
            NotStartedException, StillChargingException {
        if (!simulationStarted)
            throw new NotStartedException();
        if (robot.getRechargeTime() > 0)
            throw new StillChargingException();
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
