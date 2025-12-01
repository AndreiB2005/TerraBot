package mission.commandCenter;

import com.fasterxml.jackson.databind.node.ObjectNode;
import direction.Direction;
import mission.World;
import map.MapMatrix;
import map.MapCell;
import mission.TerraBot;

/**
 * Command that moves the TerraBot to the adjacent map cell with the lowest environmental hazard.
 * <p>
 * The robot evaluates all neighboring cells (up, down, left, right, and diagonals) and selects the
 * one with the lowest cumulative "hazard score," which is calculated based on plants, animals,
 * soil, and air conditions. The robot's battery decreases by the hazard score amount when moving.
 * The command can only be executed if the simulation has started and the robot is not recharging.
 */
public class MoveRobot implements Command {
    private final MapMatrix worldMap;
    private final TerraBot robot;
    private final boolean simulationStarted;

    /**
     * Constructs a MoveRobot command for a given world.
     *
     * @param world the World instance containing the simulation and TerraBot
     */
    public MoveRobot(final World world) {
        worldMap = world.getWorldMap();
        robot = world.getMyRobot();
        simulationStarted = world.getSimulationStarted();
    }

    /**
     * Executes the command to move the TerraBot to the safest adjacent cell.
     * <p>
     * Throws a NotStartedException if the simulation has not started, a StillChargingException
     * if the TerraBot is currently recharging, or a NotEnoughBatteryException if the robot
     * does not have enough battery to move to the selected cell.
     * On success, the robot's position and battery are updated, and a message is added to the
     * JSON object node describing the move.
     *
     * @param objNode the JSON object node to store the movement message
     * @throws NotStartedException if the simulation has not started
     * @throws StillChargingException if the TerraBot is currently recharging
     */
    public void execute(final ObjectNode objNode) throws
            NotStartedException, StillChargingException {
        if (!simulationStarted) {
            throw new NotStartedException();
        }
        if (robot.getRechargeTime() > 0) {
            throw new StillChargingException();
        }

        int posX = robot.getPosX();
        int posY = robot.getPosY();
        int qualityScore = Integer.MAX_VALUE;
        int bestX = posX;
        int bestY = posY;

        for (Direction dir : Direction.values()) {
            int currX = posX + dir.getDirX();
            int currY = posY + dir.getDirY();
            if (worldMap.isInsideMap(currX, currY)) {
                int cellQuality = getCellQuality(currX, currY);
                if (cellQuality < qualityScore) {
                    qualityScore = cellQuality;
                    bestX = currX;
                    bestY = currY;
                }
            }
        }

        String message;
        try {
            if (robot.getBattery() < qualityScore) {
                throw new NotEnoughBatteryException();
            }
            robot.changeCoordinates(bestX, bestY);
            robot.setBattery(robot.getBattery() - qualityScore);
            message = "The robot has successfully moved to position (" + bestX
                    + ", " + bestY + ").";
        } catch (NotEnoughBatteryException e) {
            message = e.getMessage();
        }
        objNode.put("message", message);
    }

    /**
     * Computes a cell's hazard score based on the entities it contains.
     * <p>
     * The score is the average of:
     * <ul>
     *     <li>Plant stuck probability</li>
     *     <li>Animal attack power</li>
     *     <li>Soil trap value</li>
     *     <li>Air toxicity</li>
     * </ul>
     *
     * @param posX the X-coordinate of the cell
     * @param posY the Y-coordinate of the cell
     * @return the rounded absolute value of the average hazard score
     */
    private int getCellQuality(final int posX, final int posY) {
        MapCell cell = worldMap.getCell(posX, posY);
        double cellQuality = 0d;
        int cnt = 0;

        if (cell.getPlant() != null) {
            cellQuality += cell.getPlant().getStuck();
            cnt++;
        }
        if (cell.getAnimal() != null) {
            cellQuality += cell.getAnimal().getAttack();
            cnt++;
        }
        if (cell.getSoil() != null) {
            cellQuality += cell.getSoil().getTrap();
            cnt++;
        }
        if (cell.getAir() != null) {
            cellQuality += cell.getAir().getToxicity();
            cnt++;
        }

        return (int) (Math.round(Math.abs(cellQuality / cnt)));
    }
}
