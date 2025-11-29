package worldMap;

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

public class MapCell {
    private Plant plant;
    private Animal animal;
    private Water water;
    private Air air;
    private Soil soil;

    public Plant getPlant() {
        return plant;
    }

    public Animal getAnimal() {
        return animal;
    }

    public Water getWater() {
        return water;
    }

    public Soil getSoil() {
        return soil;
    }

    public Air getAir() {
        return air;
    }

    public void setPlant(PlantInput plant) {
        this.plant = (plant != null) ? new Plant(plant) : null;
    }

    public void setAnimal(AnimalInput animal) {
        this.animal = (animal != null) ? new Animal(animal) : null;
    }

    public void setWater(WaterInput water) {
        this.water = (water != null) ? new Water(water) : null;
    }

    public void setSoil(SoilInput soil) {
        this.soil = Soil.createSoil(soil);
    }

    public void setAir(AirInput air) {
        this.air = Air.createAir(air);
    }

    public void moveAnimal(Animal animal) {
        this.animal = animal;
    }
}
