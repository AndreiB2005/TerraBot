package mission.commandCenter;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import entities.Entity;
import mission.World;
import mission.TerraBot;
import map.MapCell;

/**
 * Command that allows the TerraBot to scan an entity in its current map cell.
 * <p>
 * The robot can scan Plants, Animals, or Water entities depending on the
 * sensory inputs provided (color, smell, sound). Scanned entities are added
 * to the robot's knowledge base, and the robot's battery is decreased by 7 energy points.
 * If the entity cannot be found or the robot has insufficient energy, an exception is thrown.
 */
public class ScanObject implements Command {
    /** Energy cost for scanning an entity. */
    private static final int ENERGY_COST = 7;

    private final TerraBot myRobot;
    private final MapCell mapCell;
    private final String color;
    private final String smell;
    private final String sound;
    private final boolean simulationStarted;

    /**
     * Constructs a ScanObject command for a given simulation world and command input.
     *
     * @param world    the World instance containing the simulation
     * @param cmdInput the command input specifying sensory parameters (color, smell, sound)
     */
    public ScanObject(final World world, final CommandInput cmdInput) {
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

    /**
     * Executes the scan command.
     * <p>
     * Throws a NotStartedException if the simulation has not started,
     * or a StillChargingException if the robot is currently recharging.
     * If the scan is successful, the scanned entity is marked and added
     * to the robot's scan list, and the robot's battery is reduced.
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

        String message;
        try {
            Entity cellEntity = getCellEntity();
            myRobot.setBattery(myRobot.getBattery() - ENERGY_COST);
            cellEntity.setScanned(true);
            myRobot.getScanList().add(cellEntity);
            message = cellEntity.getScanResult();
        } catch (Exception e) {
            message = e.getMessage();
        }
        objNode.put("message", message);
    }

    /**
     * Determines which entity in the current cell should be scanned
     * based on the sensory inputs.
     *
     * @return the entity to be scanned
     * @throws NotEnoughEnergyException if the robot has less than 7 energy points
     * @throws NotFoundException        if no entity matches the sensory criteria
     */
    private Entity getCellEntity() throws
            NotEnoughEnergyException, NotFoundException {
        Entity cellEntity = null;
        if (!color.equals("none") && !smell.equals("none") && sound.equals("none")) {
            cellEntity = mapCell.getPlant();
        }
        if (color.equals("none") && smell.equals("none") && sound.equals("none")) {
            cellEntity = mapCell.getWater();
        }
        if (!color.equals("none") && !smell.equals("none") && !sound.equals("none")) {
            cellEntity = mapCell.getAnimal();
        }

        if (myRobot.getBattery() < ENERGY_COST) {
            throw new NotEnoughEnergyException();
        }
        if (cellEntity == null) {
            throw new NotFoundException();
        }

        return cellEntity;
    }
}
