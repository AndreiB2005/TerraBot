package mission;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import fileio.SimulationInput;

import map.MapMatrix;

/**
 * Represents the overall simulation world which manages multiple Simulation instances.
 * It keeps track of the current simulation, the world map, and the TerraBot operating in it.
 */
public class World {

    /** ObjectMapper for JSON output. */
    private final ObjectMapper mapper;

    /** JSON array storing the output of simulations. */
    private final ArrayNode output;

    /** Iterator over the list of simulations to process them sequentially. */
    private final Iterator<Simulation> simulationIt;

    /** Currently active simulation. */
    private Simulation currSimulation;

    /** Map of the current simulation. */
    private MapMatrix worldMap;

    /** TerraBot operating in the current simulation. */
    private TerraBot myRobot;

    /** Flag indicating whether the current simulation has started. */
    private boolean simulationStarted;

    /**
     * Constructs the world with a list of simulation inputs, JSON mapper, and output array.
     *
     * @param simulationsInput list of simulations to be run in the world
     * @param mapper JSON ObjectMapper used for creating output
     * @param output JSON array to store simulation results
     */
    public World(final List<SimulationInput> simulationsInput, final ObjectMapper mapper,
                 final ArrayNode output) {
        this.mapper = mapper;
        this.output = output;
        List<Simulation> simulationList = new ArrayList<>();
        for (SimulationInput sim : simulationsInput) {
            simulationList.add(new Simulation(sim));
        }
        simulationIt = simulationList.iterator();
        currSimulation = null;
        worldMap = null;
        myRobot = null;
        simulationStarted = false;
    }

    /**
     * Initializes the next simulation from the iterator and sets up
     * the current world map and TerraBot.
     * <p>
     * After calling this method, the fields {@code currSimulation},
     * {@code worldMap}, and {@code myRobot} will reference the new simulation's resources.
     * </p>
     *
     * @throws java.util.NoSuchElementException if no more simulations are available
     */
    public void buildSimulation() {
        currSimulation = simulationIt.next();
        worldMap = currSimulation.getMap();
        myRobot = currSimulation.getRobot();
    }

    /**
     * Returns the JSON ObjectMapper used by the world.
     *
     * @return the ObjectMapper instance
     */
    public ObjectMapper getMapper() {
        return mapper;
    }

    /**
     * Returns the JSON array where simulation results are stored.
     *
     * @return output ArrayNode
     */
    public ArrayNode getOutput() {
        return output;
    }

    /**
     * Returns the currently active simulation.
     *
     * @return the current Simulation, or {@code null} if none is active
     */
    public Simulation getCurrSimulation() {
        return currSimulation;
    }

    /**
     * Returns the world map associated with the current simulation.
     *
     * @return the MapMatrix of the current simulation, or {@code null} if not set
     */
    public MapMatrix getWorldMap() {
        return worldMap;
    }

    /**
     * Returns the TerraBot operating in the current simulation.
     *
     * @return the TerraBot instance, or {@code null} if not initialized
     */
    public TerraBot getMyRobot() {
        return myRobot;
    }

    /**
     * Indicates whether the current simulation has started.
     *
     * @return true if the simulation has started, false otherwise
     */
    public boolean getSimulationStarted() {
        return simulationStarted;
    }

    /**
     * Sets whether the current simulation has started.
     *
     * @param simulationStarted true if the simulation is active, false otherwise
     */
    public void setSimulationStarted(final boolean simulationStarted) {
        this.simulationStarted = simulationStarted;
    }
}
