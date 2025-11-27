package mission;

import java.util.List;

import fileio.SimulationInput;
import fileio.TerritorySectionParamsInput;
import fileio.PlantInput;
import fileio.AnimalInput;
import fileio.WaterInput;
import fileio.SoilInput;
import fileio.AirInput;
import fileio.PairInput;

import worldMap.MapMatrix;

public class Simulation {
    private final MapMatrix map;
    private final TerraBot robot;

    public Simulation(SimulationInput simulation) {
        map = new MapMatrix(simulation.getTerritoryDim());
        robot = new TerraBot(simulation.getEnergyPoints());
        TerritorySectionParamsInput params = simulation.getTerritorySectionParams();
        placePlant(params.getPlants());
        placeAnimal(params.getAnimals());
        placeWater(params.getWater());
        placeSoil(params.getSoil());
        placeAir(params.getAir());
    }

    public MapMatrix getMap() {
        return map;
    }

    public TerraBot getRobot() {
        return robot;
    }

    private void placePlant(List<PlantInput> plantList) {
        for (PlantInput currPlant : plantList) {
            List<PairInput> pairList = currPlant.getSections();
            for (PairInput pair : pairList)
                map.getCell(pair.getX(), pair.getY()).setPlant(currPlant);
        }
    }

    private void placeAnimal(List<AnimalInput> animalList) {
        for (AnimalInput currAnimal : animalList) {
            List<PairInput> pairList = currAnimal.getSections();
            for (PairInput pair : pairList)
                map.getCell(pair.getX(), pair.getY()).setAnimal(currAnimal);
        }
    }

    private void placeWater(List<WaterInput> waterList) {
        for (WaterInput currWater : waterList) {
            List<PairInput> pairList = currWater.getSections();
            for (PairInput pair : pairList)
                map.getCell(pair.getX(), pair.getY()).setWater(currWater);
        }
    }

    private void placeSoil(List<SoilInput> soilList) {
        for (SoilInput currSoil : soilList) {
            List<PairInput> pairList = currSoil.getSections();
            for (PairInput pair : pairList)
                map.getCell(pair.getX(), pair.getY()).setSoil(currSoil);
        }
    }

    private void placeAir(List<AirInput> airList) {
        for (AirInput currAir : airList) {
            List<PairInput> pairList = currAir.getSections();
            for (PairInput pair : pairList)
                map.getCell(pair.getX(), pair.getY()).setAir(currAir);
        }
    }
}
