package mission.commandCenter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import worldMap.MapCell;

public class PrintEnvConditions implements Command {
    private final String commandName = "printEnvConditions";
    private final int timestamp;
    private final ObjectMapper mapper;
    private final MapCell cell;
    private final boolean simulationStarted;

    public PrintEnvConditions(int timestamp, ObjectMapper mapper, MapCell cell,
            boolean simulationStarted) {
        this.timestamp = timestamp;
        this.mapper = mapper;
        this.cell = cell;
        this.simulationStarted = simulationStarted;
    }

    public String getCommandName() {
        return commandName;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void execute(ObjectNode objNode) throws NotStartedException {
        if (!simulationStarted)
            throw new NotStartedException();
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
