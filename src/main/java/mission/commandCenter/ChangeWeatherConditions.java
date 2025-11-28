package mission.commandCenter;

import com.fasterxml.jackson.databind.node.ObjectNode;

import fileio.CommandInput;

import entities.Air;
import worldMap.MapMatrix;
import mission.World;

public class ChangeWeatherConditions implements Command {
    private final MapMatrix worldMap;
    private final boolean simulationStarted;
    private final CommandInput currCommand;

    public ChangeWeatherConditions(World world, CommandInput currCommand) {
        worldMap = world.getWorldMap();
        simulationStarted = world.getSimulationStarted();
        this.currCommand = currCommand;
    }

    public void execute(ObjectNode objNode) throws NotStartedException {
        if (!simulationStarted)
            throw new NotStartedException();
        boolean canChange = false;
        for (int y = 0; y < worldMap.getRows(); y++) {
            for (int x = 0; x < worldMap.getCols(); x++) {
                Air currAir = worldMap.getCell(x, y).getAir();
                if (currAir == null)
                    continue;
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
        String message;
        if (canChange)
            message = "The weather has changed.";
        else
            message = "ERROR: The weather change does not affect the environment. Cannot perform action";
        objNode.put("message", message);
    }

    private double checkCellWeather(int posX, int posY) {
        Air currAir = worldMap.getCell(posX, posY).getAir();
        return currAir.checkWeather(currCommand);
    }
}
