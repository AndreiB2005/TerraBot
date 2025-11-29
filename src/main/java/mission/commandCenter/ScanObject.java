package mission.commandCenter;

import com.fasterxml.jackson.databind.node.ObjectNode;

import fileio.CommandInput;

import entities.Entity;
import mission.World;
import mission.TerraBot;
import worldMap.MapCell;

public class ScanObject implements Command {
    private final TerraBot myRobot;
    private final MapCell mapCell;
    private final String color;
    private final String smell;
    private final String sound;
    private final boolean simulationStarted;

    public ScanObject(World world, CommandInput cmdInput) {
        simulationStarted = world.getSimulationStarted();
        if (simulationStarted) {
            myRobot = world.getMyRobot();
            int posX = myRobot.getPosX();
            int posY = myRobot.getPosY();
            mapCell = world.getWorldMap().getCell(posX, posY);
        } else {
            myRobot = null;
            mapCell = null;
        }
        color = cmdInput.getColor();
        smell = cmdInput.getSmell();
        sound = cmdInput.getSound();
    }

    public void execute(ObjectNode objNode) throws
            NotStartedException, StillChargingException {
        if (!simulationStarted)
            throw new NotStartedException();
        if (myRobot.getRechargeTime() > 0)
            throw new StillChargingException();
        Entity cellEntity = null;
        String message;
        if (!color.equals("none") && !smell.equals("none") && sound.equals("none"))
            cellEntity = mapCell.getPlant();
        if (color.equals("none") && smell.equals("none") && sound.equals("none"))
            cellEntity = mapCell.getWater();
        if (!color.equals("none") && !smell.equals("none") && !sound.equals("none"))
            cellEntity = mapCell.getAnimal();
        try {
            if (myRobot.getBattery() < 7)
                throw new NotEnoughBatteryException();
            if (cellEntity == null)
                throw new NotFoundException();
            myRobot.setBattery(myRobot.getBattery() - 7);
            cellEntity.setScanned(true);
            message = cellEntity.getScanResult();
        } catch (Exception e) {
            message = e.getMessage();
        }
        objNode.put("message", message);
    }
}
