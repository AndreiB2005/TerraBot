package mission.commandCenter;

import com.fasterxml.jackson.databind.node.ObjectNode;

import mission.World;

public class EndSimulation implements Command {
    private final World myWorld;

    public EndSimulation(World myWorld) {
        this.myWorld = myWorld;
    }

    public void execute(ObjectNode objNode) throws NotStartedException {
        if (!myWorld.getSimulationStarted())
            throw new NotStartedException();
        myWorld.setSimulationStarted(false);
        objNode.put("message", "Simulation has ended.");
    }
}
