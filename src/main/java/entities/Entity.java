package entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class Entity {
    private final String name;
    private double mass;

    public Entity(String name, double mass) {
        this.name = name;
        this.mass = mass;
    }

    public abstract String getType();

    public String getName() {
        return name;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public ObjectNode printEntity(ObjectMapper mapper) {
        ObjectNode entityNode = mapper.createObjectNode();
        entityNode.put("type", this.getType());
        entityNode.put("name", this.getName());
        entityNode.put("mass", this.getMass());
        return entityNode;
    }
}
