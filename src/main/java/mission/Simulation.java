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

import map.MapMatrix;

/**
 * Represents a simulation environment that contains a map and a TerraBot.
 * Initializes the map and places entities (plants, animals, water, soil, air)
 * in the correct cells according to the simulation input.
 */
public class Simulation {

    /** The map matrix representing the territory. */
    private final MapMatrix map;

    /** The TerraBot operating in the simulation. */
    private final TerraBot robot;

    /**
     * Constructs a Simulation based on the provided simulation input.
     * Initializes the map dimensions, creates the TerraBot, and places
     * all entities in their respective locations.
     *
     * @param simulation the input containing map dimensions, energy points, and entities
     */
    public Simulation(final SimulationInput simulation) {
        map = new MapMatrix(simulation.getTerritoryDim());
        robot = new TerraBot(simulation.getEnergyPoints());
        TerritorySectionParamsInput params = simulation.getTerritorySectionParams();
        placePlant(params.getPlants());
        placeAnimal(params.getAnimals());
        placeWater(params.getWater());
        placeSoil(params.getSoil());
        placeAir(params.getAir());
    }

    /**
     * @return the map matrix of the simulation
     */
    public MapMatrix getMap() {
        return map;
    }

    /**
     * @return the TerraBot operating in the simulation
     */
    public TerraBot getRobot() {
        return robot;
    }

    /**
     * Places all plants in the map based on their designated sections.
     *
     * @param plantList list of plants to place
     */
    private void placePlant(final List<PlantInput> plantList) {
        for (PlantInput currPlant : plantList) {
            List<PairInput> pairList = currPlant.getSections();
            for (PairInput pair : pairList) {
                map.getCell(pair.getX(), pair.getY()).setPlant(currPlant);
            }
        }
    }

    /**
     * Places all animals in the map based on their designated sections.
     *
     * @param animalList list of animals to place
     */
    private void placeAnimal(final List<AnimalInput> animalList) {
        for (AnimalInput currAnimal : animalList) {
            List<PairInput> pairList = currAnimal.getSections();
            for (PairInput pair : pairList) {
                map.getCell(pair.getX(), pair.getY()).setAnimal(currAnimal);
            }
        }
    }

    /**
     * Places all water entities in the map based on their designated sections.
     *
     * @param waterList list of water entities to place
     */
    private void placeWater(final List<WaterInput> waterList) {
        for (WaterInput currWater : waterList) {
            List<PairInput> pairList = currWater.getSections();
            for (PairInput pair : pairList) {
                map.getCell(pair.getX(), pair.getY()).setWater(currWater);
            }
        }
    }

    /**
     * Places all soil entities in the map based on their designated sections.
     *
     * @param soilList list of soil entities to place
     */
    private void placeSoil(final List<SoilInput> soilList) {
        for (SoilInput currSoil : soilList) {
            List<PairInput> pairList = currSoil.getSections();
            for (PairInput pair : pairList) {
                map.getCell(pair.getX(), pair.getY()).setSoil(currSoil);
            }
        }
    }

    /**
     * Places all air entities in the map based on their designated sections.
     *
     * @param airList list of air entities to place
     */
    private void placeAir(final List<AirInput> airList) {
        for (AirInput currAir : airList) {
            List<PairInput> pairList = currAir.getSections();
            for (PairInput pair : pairList) {
                map.getCell(pair.getX(), pair.getY()).setAir(currAir);
            }
        }
    }
}
