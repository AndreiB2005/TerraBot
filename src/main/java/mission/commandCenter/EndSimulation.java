package mission.commandCenter;

import com.fasterxml.jackson.databind.node.ObjectNode;

import mission.World;

public class EndSimulation implements Command {
    private final String commandName = "endSimulation";
    private final int timestamp;
    private final World myWorld;

    public EndSimulation(int timestamp, World myWorld) {
        this.timestamp = timestamp;
        this.myWorld = myWorld;
    }

    public void execute(ObjectNode objNode) throws NotStartedException {
        if (!myWorld.getSimulationStarted())
            throw new NotStartedException();
        myWorld.setSimulationStarted(false);
        objNode.put("message", "Simulation has ended.");
    }
}
