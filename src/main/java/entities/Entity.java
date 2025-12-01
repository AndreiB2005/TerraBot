package entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Represents a generic entity characterized by a name and a mass.
 * This abstract class serves as a base for all specific entity types
 * that can be scanned and represented in JSON format.
 */
public abstract class Entity {

    /** The name of the entity. */
    private final String name;

    /** The mass of the entity. */
    private double mass;

    /** Indicates whether this entity has been scanned. */
    private boolean scanned = false;

    /**
     * Constructs a new entity with the specified name and mass.
     *
     * @param name the name of the entity
     * @param mass the mass of the entity
     */
    public Entity(final String name, final double mass) {
        this.name = name;
        this.mass = mass;
    }

    /**
     * Returns the specific type of this entity.
     * Implemented by subclasses to indicate their concrete type.
     *
     * @return the type of the entity
     */
    public abstract String getType();

    /**
     * Returns the name of the entity.
     *
     * @return the entity name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the mass of the entity.
     *
     * @return the entity mass
     */
    public double getMass() {
        return mass;
    }

    /**
     * Indicates whether the entity has been scanned.
     *
     * @return {@code true} if scanned, otherwise {@code false}
     */
    public boolean isScanned() {
        return scanned;
    }

    /**
     * Updates the mass of the entity.
     *
     * @param mass the new mass value
     */
    public void setMass(final double mass) {
        this.mass = mass;
    }

    /**
     * Sets the scanned state of the entity.
     *
     * @param scanned whether the entity has been scanned
     */
    public void setScanned(final boolean scanned) {
        this.scanned = scanned;
    }

    /**
     * Generates a JSON representation of the entity,
     * containing its type, name, and mass.
     *
     * @param mapper the JSON object mapper used to create the node
     * @return an {@link ObjectNode} describing the entity
     */
    public ObjectNode printEntity(final ObjectMapper mapper) {
        ObjectNode entityNode = mapper.createObjectNode();
        entityNode.put("type", this.getType());
        entityNode.put("name", this.getName());
        entityNode.put("mass", this.getMass());
        return entityNode;
    }

    /**
     * Returns a generic message describing the result of scanning this entity.
     * Subclasses may override this to provide custom scan information.
     *
     * @return a textual scan result
     */
    public String getScanResult() {
        return "The scanned object is an entity.";
    }
}
