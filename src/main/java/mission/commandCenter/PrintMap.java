package mission.commandCenter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import mission.TerraBot;
import mission.World;
import worldMap.MapMatrix;
import worldMap.MapCell;

public class PrintMap implements Command {
    private final ObjectMapper mapper;
    private final MapMatrix mapMatrix;
    private final TerraBot robot;
    private final boolean simulationStarted;

    public PrintMap(World world) {
        mapper = world.getMapper();
        mapMatrix = world.getWorldMap();
        robot = world.getMyRobot();
        simulationStarted = world.getSimulationStarted();
    }

    public void execute(ObjectNode objNode) throws
            NotStartedException, StillChargingException {
        if (!simulationStarted)
            throw new NotStartedException();
        if (robot.getRechargeTime() > 0)
            throw new StillChargingException();
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
        if (cell.getAir().calculateAirQuality() > 70)
            return "good";
        if (cell.getAir().calculateAirQuality() > 40)
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
