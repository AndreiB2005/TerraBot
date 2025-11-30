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
import entities.Plant;
import entities.Animal;
import entities.Water;
import entities.Soil;
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
                for (int idx = 0; idx < timePassed; idx++) {
                    updateWater();
                    evolvePlant();
                    feedAnimal();
                    finishIteration();
                }
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
            case "scanObject" -> new ScanObject(myWorld, cmdInput);
            case "learnFact" -> new LearnFact(myWorld, cmdInput);
            case "printKnowledgeBase" -> new PrintKnowledgeBase(myWorld);
            case "improveEnvironment" -> ImproveEnvironment.generateImprovement(myWorld, cmdInput);
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

    private void evolvePlant() {
        for (int y = 0; y < myWorld.getWorldMap().getRows(); y++) {
            for (int x = 0; x < myWorld.getWorldMap().getCols(); x++) {
                MapCell currCell = myWorld.getWorldMap().getCell(x, y);
                Plant currPlant = currCell.getPlant();
                Soil currSoil = currCell.getSoil();
                Air currAir = currCell.getAir();
                Water currWater = currCell.getWater();
                if (currPlant == null || !currPlant.isScanned())
                    continue;
                if (currSoil != null)
                    currPlant.growPlant();
                if (currWater != null && currWater.isScanned())
                    currPlant.growPlant();
                if (currPlant.isDead()) {
                    currCell.setPlant(null);
                    continue;
                }
                if (currAir != null)
                    currAir.setOxygenLevel(currAir.getOxygenLevel() + currPlant.generateOxygen());
            }
        }
    }

    private void updateWater() {
        for (int y = 0; y < myWorld.getWorldMap().getRows(); y++) {
            for (int x = 0; x < myWorld.getWorldMap().getCols(); x++) {
                MapCell currCell = myWorld.getWorldMap().getCell(x, y);
                Soil currSoil = currCell.getSoil();
                Air currAir = currCell.getAir();
                Water currWater = currCell.getWater();
                if (currWater == null || !currWater.isScanned())
                    continue;
                if (currAir != null && currWater.getScanTimestamp() % 2 == 0)
                    currAir.setHumidity(currAir.getHumidity() + 0.1);
                if (currSoil != null && currWater.getScanTimestamp() % 2 == 0)
                    currSoil.setWaterRetention(currSoil.getWaterRetention() + 0.1);
                currWater.incScanTimestamp();
            }
        }
    }

    private void feedAnimal() {
        for (int y = 0; y < myWorld.getWorldMap().getRows(); y++) {
            for (int x = 0; x < myWorld.getWorldMap().getCols(); x++) {
                MapCell currCell = myWorld.getWorldMap().getCell(x, y);
                Animal currAnimal = currCell.getAnimal();
                Soil currSoil = currCell.getSoil();
                Air currAir = currCell.getAir();
                if (currAnimal == null || !currAnimal.isScanned())
                    continue;
                if (currAnimal.getDoneIteration())
                    continue;
                if (currAir != null && currAir.isToxic())
                    currAnimal.setSoilMatter(0d);
                if (currSoil != null)
                    currSoil.setOrganicMatter(currSoil.getOrganicMatter() +
                            currAnimal.getSoilMatter());
                MapCell nextCell = currAnimal.findNextCell(myWorld.getWorldMap(),
                        x, y);
                if (currAnimal.getScanTimestamp() % 2 == 0) {
                    currAnimal.eatEntities(nextCell);
                    nextCell.moveAnimal(currAnimal);
                    currCell.setAnimal(null);
                } else if (currCell.getWater() != null) {
                    currAnimal.drinkWater(currCell);
                }
                currAnimal.incScanTimestamp();
                currAnimal.setDoneIteration(true);
            }
        }
    }

    private void finishIteration() {
        for (int y = 0; y < myWorld.getWorldMap().getRows(); y++) {
            for (int x = 0; x < myWorld.getWorldMap().getCols(); x++) {
                Animal currAnimal = myWorld.getWorldMap().getCell(x, y).getAnimal();
                if (currAnimal != null)
                    currAnimal.setDoneIteration(false);
            }
        }
    }
}
