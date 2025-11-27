package mission.commandCenter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import worldMap.MapMatrix;
import worldMap.MapCell;

public class PrintMap implements Command {
    private final String commandName = "printMap";
    private final int timestamp;
    private final ObjectMapper mapper;
    private final MapMatrix mapMatrix;
    private final boolean simulationStarted;

    public PrintMap(int timestamp, ObjectMapper mapper, MapMatrix mapMatrix,
                    boolean simulationStarted) {
        this.timestamp = timestamp;
        this.mapper = mapper;
        this.mapMatrix = mapMatrix;
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
        ArrayNode cellArray = mapper.createArrayNode();
        for (int y = 0; y < mapMatrix.getRows(); y++) {
            for (int x = 0; x < mapMatrix.getCols(); x++) {
                ObjectNode cellContent = printCell(x, y);
                cellArray.add(cellContent);
            }
        }
        objNode.set("output", cellArray);
    }

    private ObjectNode printCell(int posX, int posY) {
        MapCell cell = mapMatrix.getCell(posX, posY);
        ObjectNode cellNode = mapper.createObjectNode();
        cellNode.set("section", printSection(posX, posY));
        cellNode.put("totalNrOfObjects", cntEntity(cell));
        cellNode.put("airQuality", printAirQuality(cell));
        cellNode.put("soilQuality", printSoilQuality(cell));
        return cellNode;
    }

    private ArrayNode printSection(int posX, int posY) {
        ArrayNode section = mapper.createArrayNode();
        section.add(posX);
        section.add(posY);
        return section;
    }

    private int cntEntity(MapCell cell) {
        int cnt = (cell.getPlant() != null) ? 1 : 0;
        cnt += (cell.getAnimal() != null) ? 1 : 0;
        cnt += (cell.getWater() != null) ? 1 : 0;
        return cnt;
    }

    private String printAirQuality(MapCell cell) {
        if (cell.getAir().getAirQuality() > 70)
            return "good";
        if (cell.getAir().getAirQuality() > 40)
            return "moderate";
        return "poor";
    }

    private String printSoilQuality(MapCell cell) {
        if (cell.getSoil().getSoilQuality() > 70)
            return "good";
        if (cell.getSoil().getSoilQuality() > 40)
            return "moderate";
        return "poor";
    }
}
