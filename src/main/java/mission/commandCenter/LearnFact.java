package mission.commandCenter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

import fileio.CommandInput;

import mission.World;
import mission.TerraBot;
import worldMap.MapMatrix;
import worldMap.MapCell;
import entities.Entity;

public class LearnFact implements Command {
    private final ObjectMapper mapper;
    private final TerraBot myRobot;
    private final MapMatrix worldMap;
    private final String subject;
    private final String component;
    private final boolean simulationStarted;

    public LearnFact(World world, CommandInput cmdInput) {
        mapper = world.getMapper();
        myRobot = world.getMyRobot();
        worldMap = world.getWorldMap();
        subject = cmdInput.getSubject();
        component = cmdInput.getComponents();
        simulationStarted = world.getSimulationStarted();
    }

    public void execute(ObjectNode objNode) throws
            NotStartedException, StillChargingException {
        if (!simulationStarted)
            throw new NotStartedException();
        if (myRobot.getRechargeTime() > 0)
            throw new StillChargingException();
        MapCell currCell = worldMap.getCell(myRobot.getPosX(), myRobot.getPosY());
        ArrayList<Entity> cellEntities = new ArrayList<>();
        cellEntities.add(currCell.getPlant());
        cellEntities.add(currCell.getAnimal());
        cellEntities.add(currCell.getWater());
        String message;
        try {
            if (myRobot.getBattery() < 2)
                throw new NotEnoughBatteryException();
            Entity entityItem = null;
            for (Entity entity : cellEntities) {
                if (entity != null && entity.getName().equals(component) &&
                        entity.isScanned()) {
                    entityItem = entity;
                    break;
                }
            }
            if (entityItem == null && !myRobot.findTopic(component))
                throw new NotSavedException();
            myRobot.setBattery(myRobot.getBattery() - 2);
            myRobot.saveData(component, subject);
            message = "The fact has been successfully saved in the database.";
        } catch (Exception e) {
            message = e.getMessage();
        }
        objNode.put("message", message);
    }
}
