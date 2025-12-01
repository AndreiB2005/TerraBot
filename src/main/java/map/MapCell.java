package map;

import fileio.PlantInput;
import fileio.AnimalInput;
import fileio.WaterInput;
import fileio.SoilInput;
import fileio.AirInput;

import entities.Plant;
import entities.Animal;
import entities.Water;
import entities.Soil;
import entities.Air;

/**
 * Represents a single cell in the world map, containing various environmental
 * and biological entities such as plants, animals, water, air, and soil.
 * Provides getters and setters for each entity type, and allows moving animals.
 */
public class MapCell {

    private Plant plant;
    private Animal animal;
    private Water water;
    private Air air;
    private Soil soil;

    /**
     * @return the plant present in this cell, or null if none
     */
    public Plant getPlant() {
        return plant;
    }

    /**
     * @return the animal present in this cell, or null if none
     */
    public Animal getAnimal() {
        return animal;
    }

    /**
     * @return the water present in this cell, or null if none
     */
    public Water getWater() {
        return water;
    }

    /**
     * @return the soil present in this cell, or null if none
     */
    public Soil getSoil() {
        return soil;
    }

    /**
     * @return the air present in this cell, or null if none
     */
    public Air getAir() {
        return air;
    }

    /**
     * Sets the plant for this cell.
     *
     * @param plant input data to create a Plant object, or null to remove it
     */
    public void setPlant(final PlantInput plant) {
        this.plant = (plant != null) ? new Plant(plant) : null;
    }

    /**
     * Sets the animal for this cell.
     *
     * @param animal input data to create an Animal object, or null to remove it
     */
    public void setAnimal(final AnimalInput animal) {
        this.animal = (animal != null) ? new Animal(animal) : null;
    }

    /**
     * Sets the water for this cell.
     *
     * @param water input data to create a Water object, or null to remove it
     */
    public void setWater(final WaterInput water) {
        this.water = (water != null) ? new Water(water) : null;
    }

    /**
     * Sets the soil for this cell using the appropriate soil type factory.
     *
     * @param soil input data to create a Soil object
     */
    public void setSoil(final SoilInput soil) {
        this.soil = Soil.createSoil(soil);
    }

    /**
     * Sets the air for this cell using the appropriate air type factory.
     *
     * @param air input data to create an Air object
     */
    public void setAir(final AirInput air) {
        this.air = Air.createAir(air);
    }

    /**
     * Moves an animal into this cell, replacing any existing animal.
     *
     * @param newAnimal the Animal object to move into this cell
     */
    public void moveAnimal(final Animal newAnimal) {
        animal = newAnimal;
    }
}
