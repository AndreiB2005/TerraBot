package mission.commandCenter;

import com.fasterxml.jackson.databind.node.ObjectNode;

import direction.Direction;
import mission.World;
import worldMap.MapMatrix;
import worldMap.MapCell;
import mission.TerraBot;

public class MoveRobot implements Command {
    private final MapMatrix worldMap;
    private final TerraBot robot;
    private final boolean simulationStarted;

    public MoveRobot(World world) {
        worldMap = world.getWorldMap();
        robot = world.getMyRobot();
        simulationStarted = world.getSimulationStarted();
    }

    public void execute(ObjectNode objNode) throws
            NotStartedException, StillChargingException {
        if (!simulationStarted)
            throw new NotStartedException();
        if (robot.getRechargeTime() > 0)
            throw new StillChargingException();
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
            if (robot.getBattery() < qualityScore)
                throw new NotEnoughBatteryException();
            robot.changeCoordinates(bestX, bestY);
            robot.setBattery(robot.getBattery() - qualityScore);
            message = "The robot has successfully moved to position (" + bestX +
                    ", " + bestY + ").";
        } catch (NotEnoughBatteryException e) {
            message = e.getMessage();
        }
        objNode.put("message", message);
    }

    private int getCellQuality(int posX, int posY) {
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
        return (int)(Math.round(Math.abs(cellQuality / cnt)));
    }
}
