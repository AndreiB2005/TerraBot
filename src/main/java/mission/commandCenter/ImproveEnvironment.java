package mission.commandCenter;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;

import mission.World;
import mission.TerraBot;
import mission.commandCenter.improvements.PlantVegetation;
import mission.commandCenter.improvements.FertilizeSoil;
import mission.commandCenter.improvements.IncreaseHumidity;
import mission.commandCenter.improvements.IncreaseMoisture;
import map.MapCell;
import entities.Soil;
import entities.Air;

/**
 * Abstract command for improving the environment in the current map cell.
 * <p>
 * This command provides the basic structure for environment improvements
 * such as planting vegetation, fertilizing soil, increasing humidity, or
 * increasing soil moisture. Each specific improvement extends this class
 * and implements the {@code applyImprovement} method.
 * <p>
 * The command consumes 10 energy points from the TerraBot and requires
 * the targeted entity to exist in the robot's knowledge base.
 * Throws a NotStartedException if the simulation has not started, and
 * a StillChargingException if the robot is recharging.
 */
public abstract class ImproveEnvironment implements Command {
    /** Energy cost for performing an improvement. */
    private static final int ENERGY_COST = 10;

    private final TerraBot myRobot;
    private final Soil currSoil;
    private final Air currAir;
    private final String entityName;
    private final boolean simulationStarted;

    /**
     * Constructs an ImproveEnvironment command.
     *
     * @param world    the simulation world
     * @param cmdInput the command input containing the target entity name and improvement type
     */
    public ImproveEnvironment(final World world, final CommandInput cmdInput) {
        myRobot = world.getMyRobot();
        int posX = myRobot.getPosX();
        int posY = myRobot.getPosY();
        MapCell currCell = world.getWorldMap().getCell(posX, posY);
        currSoil = currCell.getSoil();
        currAir = currCell.getAir();
        entityName = cmdInput.getName();
        simulationStarted = world.getSimulationStarted();
    }

    /**
     * Returns the TerraBot executing this command.
     */
    public TerraBot getMyRobot() {
        return myRobot;
    }

    /**
     * Returns the Soil object of the current cell.
     */
    public Soil getCurrSoil() {
        return currSoil;
    }

    /**
     * Returns the Air object of the current cell.
     */
    public Air getCurrAir() {
        return currAir;
    }

    /**
     * Returns the name of the entity targeted by the improvement.
     */
    public String getEntityName() {
        return entityName;
    }

    /**
     * Applies the specific environment improvement.
     *
     * @return a message describing the result of the improvement
     */
    public abstract String applyImprovement();

    /**
     * Executes the environment improvement command.
     * <p>
     * Throws NotStartedException if the simulation is not started,
     * StillChargingException if the robot is recharging,
     * NotEnoughBatteryException if the robot has insufficient energy,
     * or NotSavedException if the targeted entity is not in the robot's knowledge base.
     *
     * @param objNode the JSON object node to store the output message
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
            if (myRobot.getBattery() < ENERGY_COST) {
                throw new NotEnoughBatteryException();
            }
            if (!myRobot.findTopic(entityName)) {
                throw new NotSavedException();
            }

            myRobot.setBattery(myRobot.getBattery() - ENERGY_COST);

            message = applyImprovement();
        } catch (Exception e) {
            message = e.getMessage();
        }
        objNode.put("message", message);
    }

    /**
     * Factory method to generate a specific environment improvement
     * based on the command input.
     *
     * @param world    the simulation world
     * @param cmdInput the command input containing the improvement type
     * @return the corresponding ImproveEnvironment instance
     */
    public static ImproveEnvironment generateImprovement(final World world,
                                                         final CommandInput cmdInput) {
        return switch (cmdInput.getImprovementType()) {
            case "plantVegetation" -> new PlantVegetation(world, cmdInput);
            case "fertilizeSoil" -> new FertilizeSoil(world, cmdInput);
            case "increaseHumidity" -> new IncreaseHumidity(world, cmdInput);
            case "increaseMoisture" -> new IncreaseMoisture(world, cmdInput);
            default -> throw new IllegalArgumentException();
        };
    }
}
