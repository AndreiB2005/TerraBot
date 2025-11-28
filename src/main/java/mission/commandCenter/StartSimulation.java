package mission.commandCenter;

import com.fasterxml.jackson.databind.node.ObjectNode;

import mission.World;

public class StartSimulation implements Command {
    private final World myWorld;

    public StartSimulation(World myWorld) {
        this.myWorld = myWorld;
    }

    public void execute(ObjectNode objNode) {
        String message;
        try {
            if (myWorld.getSimulationStarted())
                throw new AlreadyStartedException();
            myWorld.setSimulationStarted(true);
            myWorld.buildSimulation();
            message = "Simulation has started.";
        } catch (AlreadyStartedException e) {
            message = e.getMessage();
        }
        objNode.put("message", message);
    }
}
