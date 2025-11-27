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

public class CommandHandler {
    private final World myWorld;
    private final List<CommandInput> commandList;

    public CommandHandler(World myWorld, List<CommandInput> commandList) {
        this.myWorld = myWorld;
        this.commandList = commandList;
    }

    public void executeCommands() {
        ObjectMapper mapper = myWorld.getMapper();
        ArrayNode output = myWorld.getOutput();
        for (CommandInput cmdInput : commandList) {
            Command currCommand = CommandHandler.generateCommand(cmdInput, myWorld);
            ObjectNode objNode = mapper.createObjectNode();
            objNode.put("command", cmdInput.getCommand());
            try {
                currCommand.execute(objNode);
            } catch (NotStartedException e) {
                objNode.put("message", e.getErrorMessage());
            }
            objNode.put("timestamp", cmdInput.getTimestamp());
            output.add(objNode);
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
            case "startSimulation" -> new StartSimulation(cmdInput.getTimestamp(), myWorld);
            case "endSimulation" -> new EndSimulation(cmdInput.getTimestamp(), myWorld);
            case "printEnvConditions" -> new PrintEnvConditions(cmdInput.getTimestamp(),
                    myWorld.getMapper(), mapCell, myWorld.getSimulationStarted());
            case "printMap" -> new PrintMap(cmdInput.getTimestamp(), myWorld.getMapper(),
                    mapMatrix, myWorld.getSimulationStarted());
            default -> throw new IllegalArgumentException();
        };
    }
}
