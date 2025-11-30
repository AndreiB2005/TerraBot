package mission.commandCenter;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;

import mission.World;
import mission.TerraBot;
import mission.commandCenter.improvements.PlantVegetation;
import mission.commandCenter.improvements.FertilizeSoil;
import mission.commandCenter.improvements.IncreaseHumidity;
import mission.commandCenter.improvements.IncreaseMoisture;
import worldMap.MapCell;
import entities.Soil;
import entities.Air;

public abstract class ImproveEnvironment implements Command {
    private final TerraBot myRobot;
    private final Soil currSoil;
    private final Air currAir;
    private final String entityName;
    private final boolean simulationStarted;

    public ImproveEnvironment(World world, CommandInput cmdInput) {
        myRobot = world.getMyRobot();
        int posX = myRobot.getPosX();
        int posY = myRobot.getPosY();
        MapCell currCell = world.getWorldMap().getCell(posX, posY);
        currSoil = currCell.getSoil();
        currAir = currCell.getAir();
        entityName = cmdInput.getName();
        simulationStarted = world.getSimulationStarted();
    }

    public TerraBot getMyRobot() {
        return myRobot;
    }

    public Soil getCurrSoil() {
        return currSoil;
    }

    public Air getCurrAir() {
        return currAir;
    }

    public String getEntityName() {
        return entityName;
    }

    public abstract String applyImprovement();

    public void execute(ObjectNode objNode) throws
            NotStartedException, StillChargingException {
        if (!simulationStarted)
            throw new NotStartedException();
        if (myRobot.getRechargeTime() > 0)
            throw new StillChargingException();
        String message;
        try {
            if (myRobot.getBattery() < 10)
                throw new NotEnoughBatteryException();
            if (!myRobot.findTopic(entityName))
                throw new NotSavedException();
            myRobot.setBattery(myRobot.getBattery() - 10);
            message = applyImprovement();
        } catch (Exception e) {
            message = e.getMessage();
        }
        objNode.put("message", message);
    }

    public static ImproveEnvironment generateImprovement(World world, CommandInput cmdInput) {
        return switch (cmdInput.getImprovementType()) {
            case "plantVegetation" -> new PlantVegetation(world, cmdInput);
            case "fertilizeSoil" -> new FertilizeSoil(world, cmdInput);
            case "increaseHumidity" -> new IncreaseHumidity(world, cmdInput);
            case "increaseMoisture" -> new IncreaseMoisture(world, cmdInput);
            default -> throw new IllegalArgumentException();
        };
    }
}
