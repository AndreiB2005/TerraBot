package mission;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.List;
import java.util.ArrayList;

import entities.Entity;

public class TerraBot {
    private int battery;
    private int rechargeTime;
    private final List<Entity> scanList = new ArrayList<>();
    private final List<InventoryItem> inventory;
    private int posX;
    private int posY;

    private class InventoryItem {
        private final String topic;
        private final List<String> factsList;

        public InventoryItem(String topic, String fact) {
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

    public TerraBot(int energyPoints) {
        battery = energyPoints;
        rechargeTime = 0;
        inventory = new ArrayList<>();
        posX = 0;
        posY = 0;
    }

    public int getBattery() {
        return battery;
    }

    public int getRechargeTime() {
        return rechargeTime;
    }

    public List<Entity> getScanList() {
        return scanList;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public void setRechargeTime(int rechargeTime) {
        this.rechargeTime = Math.max(0, rechargeTime);
    }

    public void changeCoordinates(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public void saveData(String topic, String fact) {
        boolean searchTopic = false;
        for (InventoryItem item : inventory) {
            if (item.getTopic().equals(topic)) {
                item.getFactsList().add(fact);
                searchTopic = true;
                break;
            }
        }
        if (!searchTopic)
            inventory.add(new InventoryItem(topic, fact));
    }

    public boolean findTopic(String topic) {
        for (Entity currEntity : scanList) {
            if (currEntity.getName().equals(topic))
                return true;
        }
        return false;
    }

    public List<String> getTopicFacts(String topic) {
        for (InventoryItem item : inventory)
            if (item.getTopic().equals(topic))
                return item.getFactsList();
        return null;
    }

    public void printData(ObjectMapper mapper, ObjectNode objNode) {
        ArrayNode output = mapper.createArrayNode();
        for (InventoryItem item : inventory) {
            ObjectNode itemNode = mapper.createObjectNode();
            itemNode.put("topic", item.getTopic());
            ArrayNode factsNode = mapper.createArrayNode();
            for (String fact : item.getFactsList())
                factsNode.add(fact);
            itemNode.set("facts", factsNode);
            output.add(itemNode);
        }
        objNode.set("output", output);
    }
}
