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
        this.plant = new Plant(plant);
    }

    public void setAnimal(AnimalInput animal) {
        this.animal = new Animal(animal);
    }

    public void setWater(WaterInput water) {
        this.water = new Water(water);
    }

    public void setSoil(SoilInput soil) {
        this.soil = Soil.createSoil(soil);
    }

    public void setAir(AirInput air) {
        this.air = Air.createAir(air);
    }
}
