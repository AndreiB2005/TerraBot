package mission;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import fileio.SimulationInput;

import worldMap.MapMatrix;

public class World {
    private final ObjectMapper mapper;
    private final ArrayNode output;
    private final Iterator<Simulation> simulationIt;
    private Simulation currSimulation;
    private MapMatrix worldMap;
    private TerraBot myRobot;
    private boolean simulationStarted;

    public World(List<SimulationInput> simulationsInput, ObjectMapper mapper,
                 ArrayNode output) {
        this.mapper = mapper;
        this.output = output;
        List<Simulation> simulationList = new ArrayList<>();
        for (SimulationInput sim : simulationsInput)
            simulationList.add(new Simulation(sim));
        simulationIt = simulationList.iterator();
        currSimulation = null;
        worldMap = null;
        myRobot = null;
        simulationStarted = false;
    }

    public void buildSimulation() {
        currSimulation = simulationIt.next();
        worldMap = currSimulation.getMap();
        myRobot = currSimulation.getRobot();
    }

    public ObjectMapper getMapper() {
        return mapper;
    }

    public ArrayNode getOutput() {
        return output;
    }

    public Simulation getCurrSimulation() {
        return currSimulation;
    }

    public MapMatrix getWorldMap() {
        return worldMap;
    }

    public TerraBot getMyRobot() {
        return myRobot;
    }

    public boolean getSimulationStarted() {
        return simulationStarted;
    }

    public void setSimulationStarted(boolean simulationStarted) {
        this.simulationStarted = simulationStarted;
    }
}
