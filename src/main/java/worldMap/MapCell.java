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
        this.plant = new Plant(plant.getName(), plant.getMass(), plant.getType());
    }

    public void setAnimal(AnimalInput animal) {
        this.animal = new Animal(animal.getName(), animal.getMass(), animal.getType());
    }

    public void setWater(WaterInput water) {
        this.water = new Water(water.getName(), water.getMass(), water.getType(),
                water.getSalinity(), water.getPH(), water.getPurity(),
                water.getTurbidity(), water.getContaminantIndex(), water.isFrozen());
    }

    public void setSoil(SoilInput soil) {
        double specificTrait;
        switch (soil.getType()) {
            case "ForestSoil" -> specificTrait = soil.getLeafLitter();
            case "SwampSoil" -> specificTrait = soil.getWaterLogging();
            case "DesertSoil" -> specificTrait = soil.getSalinity();
            case "GrasslandSoil" -> specificTrait = soil.getRootDensity();
            case "TundraSoil" -> specificTrait = soil.getPermafrostDepth();
            default -> throw new IllegalArgumentException();
        }
        this.soil = Soil.createSoil(soil.getName(), soil.getMass(), soil.getType(),
                soil.getNitrogen(), soil.getWaterRetention(), soil.getSoilpH(),
                soil.getOrganicMatter(), specificTrait);
    }

    public void setAir(AirInput air) {
        double specificTrait;
        switch (air.getType()) {
            case "TropicalAir" -> specificTrait = air.getCo2Level();
            case "PolarAir" -> specificTrait = air.getIceCrystalConcentration();
            case "TemperateAir" -> specificTrait = air.getPollenLevel();
            case "DesertAir" -> specificTrait = air.getDustParticles();
            case "MountainAir" -> specificTrait = air.getAltitude();
            default -> throw new IllegalArgumentException();
        }
        this.air = Air.createAir(air.getName(), air.getMass(), air.getType(),
                air.getHumidity(), air.getTemperature(), air.getOxygenLevel(),
                specificTrait);
    }
}
