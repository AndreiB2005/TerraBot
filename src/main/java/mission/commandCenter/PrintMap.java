package mission.commandCenter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import mission.TerraBot;
import mission.World;
import map.MapMatrix;
import map.MapCell;

/**
 * Command that prints the entire map with summarized information for each cell.
 * <p>
 * When executed, this command iterates through all cells of the map and collects
 * data about the number of entities, air quality, and soil quality in each cell.
 * The result is returned as a structured JSON array where each element corresponds
 * to a cell in the map.
 * <p>
 * The command can only be executed if the simulation has started and the
 * TerraBot is not currently recharging.
 */
public class PrintMap implements Command {
    private final ObjectMapper mapper;
    private final MapMatrix mapMatrix;
    private final TerraBot robot;
    private final boolean simulationStarted;

    /** Threshold above which air/soil quality is considered good. */
    private static final int QUALITY_GOOD_THRESHOLD = 70;

    /** Threshold above which air/soil quality is considered moderate. */
    private static final int QUALITY_MODERATE_THRESHOLD = 40;

    /**
     * Constructs a PrintMap command for a given world.
     *
     * @param world the World instance containing the simulation and TerraBot
     */
    public PrintMap(final World world) {
        mapper = world.getMapper();
        mapMatrix = world.getWorldMap();
        robot = world.getMyRobot();
        simulationStarted = world.getSimulationStarted();
    }

    /**
     * Executes the command to print the map with environmental summaries.
     * <p>
     * Throws a NotStartedException if the simulation is not started, or
     * a StillChargingException if the TerraBot is recharging.
     * On success, the JSON object node is populated with an array of all cells,
     * including their coordinates, total number of entities, air quality,
     * and soil quality.
     *
     * @param objNode the JSON object node to store the map summary
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
        ArrayNode cellArray = mapper.createArrayNode();
        for (int y = 0; y < mapMatrix.getRows(); y++) {
            for (int x = 0; x < mapMatrix.getCols(); x++) {
                ObjectNode cellContent = printCell(x, y);
                cellArray.add(cellContent);
            }
        }
        objNode.set("output", cellArray);
    }

    private ObjectNode printCell(final int posX, final int posY) {
        MapCell cell = mapMatrix.getCell(posX, posY);
        ObjectNode cellNode = mapper.createObjectNode();
        cellNode.set("section", printSection(posX, posY));
        cellNode.put("totalNrOfObjects", cntEntity(cell));
        cellNode.put("airQuality", printAirQuality(cell));
        cellNode.put("soilQuality", printSoilQuality(cell));
        return cellNode;
    }

    private ArrayNode printSection(final int posX, final int posY) {
        ArrayNode section = mapper.createArrayNode();
        section.add(posX);
        section.add(posY);
        return section;
    }

    private int cntEntity(final MapCell cell) {
        int cnt = (cell.getPlant() != null) ? 1 : 0;
        cnt += (cell.getAnimal() != null) ? 1 : 0;
        cnt += (cell.getWater() != null) ? 1 : 0;
        return cnt;
    }

    private String printAirQuality(final MapCell cell) {
        double quality = cell.getAir().calculateAirQuality();
        if (quality > QUALITY_GOOD_THRESHOLD) {
            return "good";
        }
        if (quality > QUALITY_MODERATE_THRESHOLD) {
            return "moderate";
        }
        return "poor";
    }

    private String printSoilQuality(final MapCell cell) {
        double quality = cell.getSoil().getSoilQuality();
        if (quality > QUALITY_GOOD_THRESHOLD) {
            return "good";
        }
        if (quality > QUALITY_MODERATE_THRESHOLD) {
            return "moderate";
        }
        return "poor";
    }
}
