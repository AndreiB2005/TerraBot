package mission.commandCenter;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import entities.Air;
import map.MapMatrix;
import mission.World;

/**
 * Command that changes the weather conditions of all air cells in the simulation.
 * <p>
 * Each air cell evaluates whether the current command affects its environmental conditions
 * (e.g., rainfall, desert storm, wind speed). If a change is possible, the air cell's
 * quality and weather timestamp are updated. If no cell is affected, a corresponding
 * error message is returned.
 */
public class ChangeWeatherConditions implements Command {
    private final MapMatrix worldMap;
    private final boolean simulationStarted;
    private final CommandInput currCommand;

    /**
     * Constructs a ChangeWeatherConditions command.
     *
     * @param world       the World instance containing the simulation
     * @param currCommand the command input containing weather parameters
     */
    public ChangeWeatherConditions(final World world, final CommandInput currCommand) {
        worldMap = world.getWorldMap();
        simulationStarted = world.getSimulationStarted();
        this.currCommand = currCommand;
    }

    /**
     * Executes the weather change command across all air cells.
     * <p>
     * Throws a NotStartedException if the simulation has not started. Each air cell evaluates
     * the effect of the command using its checkWeather method. Cells that can be affected
     * will update their current quality and weather timestamp.
     *
     * @param objNode the JSON object node to store the result message
     * @throws NotStartedException if the simulation has not started
     */
    public void execute(final ObjectNode objNode) throws NotStartedException {
        if (!simulationStarted) {
            throw new NotStartedException();
        }

        boolean canChange = false;
        for (int y = 0; y < worldMap.getRows(); y++) {
            for (int x = 0; x < worldMap.getCols(); x++) {
                Air currAir = worldMap.getCell(x, y).getAir();
                if (currAir == null) {
                    continue;
                }

                double currWeather = checkCellWeather(x, y);
                if (currWeather == Double.MAX_VALUE) {
                    currAir.setCurrQuality(currAir.calculateAirQuality());
                } else {
                    currAir.setWeatherTimestamp(2);
                    currAir.setCurrQuality(currWeather);
                    canChange = true;
                }
            }
        }

        String message = canChange
                ? "The weather has changed."
                : "ERROR: The weather change does not affect the environment."
                + " Cannot perform action";
        objNode.put("message", message);
    }

    /**
     * Checks whether the current weather command affects a given cell.
     *
     * @param posX the X-coordinate of the cell
     * @param posY the Y-coordinate of the cell
     * @return the new air quality if affected, or Double.MAX_VALUE if unaffected
     */
    private double checkCellWeather(final int posX, final int posY) {
        Air currAir = worldMap.getCell(posX, posY).getAir();
        return currAir.checkWeather(currCommand);
    }
}
