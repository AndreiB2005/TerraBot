package mission.commandCenter;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface Command {
    void execute(ObjectNode objNode) throws NotStartedException;
}
