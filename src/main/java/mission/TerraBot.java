package mission;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.List;
import java.util.ArrayList;

import entities.Entity;

/**
 * Represents a TerraBot agent that can scan entities, store knowledge, and manage
 * energy/battery levels. Maintains an inventory of scanned facts organized by topics.
 */
public class TerraBot {

    /** Current battery level of the TerraBot. */
    private int battery;

    /** Number of steps required to fully recharge. */
    private int rechargeTime;

    /** List of scanned entities. */
    private final List<Entity> scanList = new ArrayList<>();

    /** Inventory storing knowledge in topics and facts. */
    private final List<InventoryItem> inventory;

    /** Current X-coordinate on the map. */
    private int posX;

    /** Current Y-coordinate on the map. */
    private int posY;

    /**
     * Represents an inventory item storing a topic and its associated facts.
     */
    private class InventoryItem {
        private final String topic;
        private final List<String> factsList;

        InventoryItem(final String topic, final String fact) {
            this.topic = topic;
            factsList = new ArrayList<>();
            factsList.add(fact);
        }

        public String getTopic() {
            return topic;
        }

        public List<String> getFactsList() {
            return factsList;
        }
    }

    /**
     * Constructs a TerraBot with initial energy points.
     *
     * @param energyPoints initial battery level
     */
    public TerraBot(final int energyPoints) {
        battery = energyPoints;
        rechargeTime = 0;
        inventory = new ArrayList<>();
        posX = 0;
        posY = 0;
    }

    /**
     * Returns the current battery level of the TerraBot.
     *
     * @return current battery value
     */
    public int getBattery() {
        return battery;
    }

    /**
     * Returns the number of remaining steps required for the TerraBot to fully recharge.
     *
     * @return recharge time steps remaining
     */
    public int getRechargeTime() {
        return rechargeTime;
    }

    /**
     * Returns the list of scanned entities.
     *
     * @return list of entities scanned by the TerraBot
     */
    public List<Entity> getScanList() {
        return scanList;
    }

    /**
     * Gets the current X-coordinate of the TerraBot.
     *
     * @return X-coordinate
     */
    public int getPosX() {
        return posX;
    }

    /**
     * Gets the current Y-coordinate of the TerraBot.
     *
     * @return Y-coordinate
     */
    public int getPosY() {
        return posY;
    }

    /**
     * Sets the TerraBot's battery level.
     *
     * @param battery new battery value
     */
    public void setBattery(final int battery) {
        this.battery = battery;
    }

    /**
     * Sets the number of steps required to recharge the TerraBot.
     * Ensures the value is never negative.
     *
     * @param rechargeTime number of steps to recharge
     */
    public void setRechargeTime(final int rechargeTime) {
        this.rechargeTime = Math.max(0, rechargeTime);
    }

    /**
     * Updates the TerraBot's coordinates on the map.
     *
     * @param newPosX new X-coordinate
     * @param newPosY new Y-coordinate
     */
    public void changeCoordinates(final int newPosX, final int newPosY) {
        posX = newPosX;
        posY = newPosY;
    }

    /**
     * Saves a fact under a specific topic in the inventory.
     * Creates a new topic if it does not exist.
     *
     * @param topic the topic of knowledge
     * @param fact the fact to store
     */
    public void saveData(final String topic, final String fact) {
        boolean searchTopic = false;
        for (InventoryItem item : inventory) {
            if (item.getTopic().equals(topic)) {
                item.getFactsList().add(fact);
                searchTopic = true;
                break;
            }
        }
        if (!searchTopic) {
            inventory.add(new InventoryItem(topic, fact));
        }
    }

    /**
     * Checks if an entity with the given name/topic has been scanned.
     *
     * @param topic the name of the entity
     * @return true if the topic exists in scanList, false otherwise
     */
    public boolean findTopic(final String topic) {
        for (Entity currEntity : scanList) {
            if (currEntity.getName().equals(topic)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves all facts associated with a topic.
     *
     * @param topic the topic to search for
     * @return list of facts under the topic, or null if topic does not exist
     */
    public List<String> getTopicFacts(final String topic) {
        for (InventoryItem item : inventory) {
            if (item.getTopic().equals(topic)) {
                return item.getFactsList();
            }
        }
        return null;
    }

    /**
     * Adds the TerraBot's inventory data to a JSON object for output.
     *
     * @param mapper the ObjectMapper used to create JSON nodes
     * @param objNode the parent JSON object to populate with inventory data
     */
    public void printData(final ObjectMapper mapper, final ObjectNode objNode) {
        ArrayNode output = mapper.createArrayNode();
        for (InventoryItem item : inventory) {
            ObjectNode itemNode = mapper.createObjectNode();
            itemNode.put("topic", item.getTopic());
            ArrayNode factsNode = mapper.createArrayNode();
            for (String fact : item.getFactsList()) {
                factsNode.add(fact);
            }
            itemNode.set("facts", factsNode);
            output.add(itemNode);
        }
        objNode.set("output", output);
    }
}
