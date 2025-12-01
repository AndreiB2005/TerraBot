package mission.commandCenter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import fileio.CommandInput;
import mission.World;
import mission.TerraBot;
import map.MapMatrix;
import map.MapCell;
import entities.Entity;

/**
 * Command that allows the TerraBot to learn a fact about a scanned entity.
 * <p>
 * The robot can save a fact (subject) about a component (entity) in its knowledge base.
 * The component must be either present in the current cell and scanned,
 * or previously scanned and saved. Executing this command consumes 2 energy points.
 * If the component is not available or there is insufficient battery, an exception is thrown.
 */
public class LearnFact implements Command {
    private final ObjectMapper mapper;
    private final TerraBot myRobot;
    private final MapMatrix worldMap;
    private final String subject;
    private final String component;
    private final boolean simulationStarted;

    /**
     * Constructs a LearnFact command for a given simulation world and command input.
     *
     * @param world    the World instance containing the simulation
     * @param cmdInput the command input specifying the fact's subject and target component
     */
    public LearnFact(final World world, final CommandInput cmdInput) {
        mapper = world.getMapper();
        myRobot = world.getMyRobot();
        worldMap = world.getWorldMap();
        subject = cmdInput.getSubject();
        component = cmdInput.getComponents();
        simulationStarted = world.getSimulationStarted();
    }

    /**
     * Executes the LearnFact command.
     * <p>
     * Throws a NotStartedException if the simulation has not started,
     * or a StillChargingException if the robot is currently recharging.
     * The command searches for the target in the current cell and in the robot's database.
     * If the component is available and scanned, the fact is saved in the robot's knowledge base,
     * and the battery is reduced by 2 energy points. Otherwise, an exception is thrown.
     *
     * @param objNode the JSON object node to store the result message
     * @throws NotStartedException    if the simulation has not started
     * @throws StillChargingException if the robot is recharging
     */
    public void execute(final ObjectNode objNode) throws
            NotStartedException, StillChargingException {
        if (!simulationStarted) {
            throw new NotStartedException();
        }
        if (myRobot.getRechargeTime() > 0) {
            throw new StillChargingException();
        }

        MapCell currCell = worldMap.getCell(myRobot.getPosX(), myRobot.getPosY());
        ArrayList<Entity> cellEntities = new ArrayList<>();
        cellEntities.add(currCell.getPlant());
        cellEntities.add(currCell.getAnimal());
        cellEntities.add(currCell.getWater());

        String message;
        try {
            if (myRobot.getBattery() < 2) {
                throw new NotEnoughBatteryException();
            }

            Entity entityItem = null;
            for (Entity entity : cellEntities) {
                if (entity != null && entity.getName().equals(component)
                        && entity.isScanned()) {
                    entityItem = entity;
                    break;
                }
            }

            if (entityItem == null && !myRobot.findTopic(component)) {
                throw new NotSavedException();
            }

            myRobot.setBattery(myRobot.getBattery() - 2);
            myRobot.saveData(component, subject);
            message = "The fact has been successfully saved in the database.";
        } catch (Exception e) {
            message = e.getMessage();
        }

        objNode.put("message", message);
    }
}
