package mission.commandCenter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.List;

import fileio.CommandInput;

import mission.World;
import mission.TerraBot;
import worldMap.MapMatrix;
import worldMap.MapCell;
import entities.Air;

public class CommandHandler {
    private final World myWorld;
    private final List<CommandInput> commandList;
    private int prevTimestamp;

    public CommandHandler(World myWorld, List<CommandInput> commandList) {
        this.myWorld = myWorld;
        this.commandList = commandList;
        prevTimestamp = 0;
    }

    public void executeCommands() {
        ObjectMapper mapper = myWorld.getMapper();
        ArrayNode output = myWorld.getOutput();
        for (CommandInput cmdInput : commandList) {
            Command currCommand = CommandHandler.generateCommand(cmdInput, myWorld);
            ObjectNode objNode = mapper.createObjectNode();
            int timePassed = cmdInput.getTimestamp() - prevTimestamp;
            if (myWorld.getSimulationStarted()) {
                int rechargeTime = myWorld.getMyRobot().getRechargeTime();
                myWorld.getMyRobot().setRechargeTime(rechargeTime - timePassed);
                checkChangeWeather(timePassed);
            }
            objNode.put("command", cmdInput.getCommand());
            try {
                currCommand.execute(objNode);
            } catch (Exception e) {
                objNode.put("message", e.getMessage());
            }
            objNode.put("timestamp", cmdInput.getTimestamp());
            output.add(objNode);
            prevTimestamp = cmdInput.getTimestamp();
        }
    }

    private static Command generateCommand(CommandInput cmdInput, World myWorld) {
        MapMatrix mapMatrix = myWorld.getWorldMap();
        TerraBot terraBot = myWorld.getMyRobot();
        MapCell mapCell = null;
        if (myWorld.getSimulationStarted()) {
            mapCell = mapMatrix.getCell(terraBot.getPosX(), terraBot.getPosY());
        }
        return switch (cmdInput.getCommand()) {
            case "startSimulation" -> new StartSimulation(myWorld);
            case "endSimulation" -> new EndSimulation(myWorld);
            case "printEnvConditions" -> new PrintEnvConditions(myWorld, mapCell);
            case "printMap" -> new PrintMap(myWorld);
            case "moveRobot" -> new MoveRobot(myWorld);
            case "getEnergyStatus" -> new GetEnergyStatus(myWorld);
            case "rechargeBattery" -> new RechargeBattery(myWorld, cmdInput.getTimeToCharge());
            case "changeWeatherConditions" -> new ChangeWeatherConditions(myWorld, cmdInput);
            default -> throw new IllegalArgumentException();
        };
    }

    private void checkChangeWeather(int time) {
        for (int y = 0; y < myWorld.getWorldMap().getRows(); y++) {
            for (int x = 0; x < myWorld.getWorldMap().getCols(); x++) {
                Air currAir = myWorld.getWorldMap().getCell(x, y).getAir();
                int weatherTime = currAir.getWeatherTimestamp();
                currAir.setWeatherTimestamp(weatherTime - time);
                if (currAir.getWeatherTimestamp() == 0)
                    currAir.setCurrQuality(currAir.calculateAirQuality());
            }
        }
    }
}
