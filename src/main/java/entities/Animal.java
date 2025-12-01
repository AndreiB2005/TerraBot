package entities;

import fileio.AnimalInput;
import direction.Direction;
import map.MapCell;
import map.MapMatrix;

/**
 * Represents an animal entity capable of movement, feeding, drinking,
 * and interacting with other entities on the map. Animals have type-specific
 * behaviors such as carnivory, attack probability, and soil matter contribution.
 */
public class Animal extends Entity {

    /** Magic number constants */
    private static final double MAX_ATTACK_VALUE = 100d;
    private static final double ATTACK_NORMALIZATION_FACTOR = 10d;
    private static final double WATER_INTAKE_RATE = 0.08d;
    private static final double SOIL_MATTER_BASIC = 0.5d;
    private static final double SOIL_MATTER_COMBINED = 0.8d;

    /** The specific animal category determining its behavior. */
    private final AnimalType type;

    /** Timestamp used for tracking consecutive scans. */
    private int scanTimestamp = 1;

    /** Amount of organic matter returned to the soil after feeding. */
    private double soilMatter = 0;

    /** Flag indicating whether the animal has completed an iteration step. */
    private boolean doneIteration = false;

    /**
     * Defines the types of animals, each specifying attack capability
     * and whether the animal feeds on other animals.
     */
    private enum AnimalType {
        Herbivores(85, false),
        Carnivores(30, true),
        Omnivores(60, false),
        Detritivores(90, false),
        Parasites(10, true);

        private final double attackProbability;
        private final boolean eatsAnimal;

        AnimalType(final double attackProbability, final boolean eatsAnimal) {
            this.attackProbability = attackProbability;
            this.eatsAnimal = eatsAnimal;
        }

        /**
         * Returns the probability that this animal successfully attacks.
         *
         * @return attack probability (0–100)
         */
        public double getAttackProbability() {
            return attackProbability;
        }

        /**
         * Indicates whether this type of animal feeds on other animals.
         *
         * @return {@code true} if carnivorous, otherwise {@code false}
         */
        public boolean getEatsAnimal() {
            return eatsAnimal;
        }
    }

    /**
     * Constructs a new animal based on input data.
     *
     * @param animalInput the source input containing initialization values
     */
    public Animal(final AnimalInput animalInput) {
        super(animalInput.getName(), animalInput.getMass());
        type = AnimalType.valueOf(animalInput.getType());
    }

    /**
     * Returns the string name of this animal's type.
     *
     * @return animal type name
     */
    @Override
    public String getType() {
        return type.name();
    }

    /** @return the scan timestamp of this animal */
    public int getScanTimestamp() {
        return scanTimestamp;
    }

    /** @return the stored soil matter value */
    public double getSoilMatter() {
        return soilMatter;
    }

    /** @return whether this animal has completed its iteration */
    public boolean getDoneIteration() {
        return doneIteration;
    }

    /** @return {@code true} if this animal feeds on other animals */
    public boolean eatsAnimals() {
        return type.getEatsAnimal();
    }

    /** Updates the soil matter stored after feeding. */
    public void setSoilMatter(final double soilMatter) {
        this.soilMatter = soilMatter;
    }

    /** Sets whether the animal has completed its iteration step. */
    public void setDoneIteration(final boolean doneIteration) {
        this.doneIteration = doneIteration;
    }

    /** Increments the scan timestamp counter. */
    public void incScanTimestamp() {
        scanTimestamp++;
    }

    /**
     * Computes this animal's attack value based on type probability.
     *
     * @return a normalized attack value
     */
    public double getAttack() {
        return (MAX_ATTACK_VALUE - type.getAttackProbability()) / ATTACK_NORMALIZATION_FACTOR;
    }

    /**
     * Returns a description used when scanning this animal.
     *
     * @return an animal-specific scan message
     */
    @Override
    public String getScanResult() {
        return "The scanned object is an animal.";
    }

    /**
     * Allows the animal to drink water from the current map cell.
     * The consumed water increases the animal's mass and reduces the cell's water mass.
     *
     * @param mapCell the cell containing the water source
     */
    public void drinkWater(final MapCell mapCell) {
        Water water = mapCell.getWater();
        double intakeRate = WATER_INTAKE_RATE;
        double waterToDrink = Math.min(this.getMass() * intakeRate, water.getMass());

        this.setMass(this.getMass() + waterToDrink);
        water.setMass(water.getMass() - waterToDrink);

        if (water.getMass() == 0) {
            mapCell.setWater(null);
        }
    }

    /**
     * Allows the animal to eat any available entities in the cell:
     * prey animals (if carnivorous), plants, or water if scanned.
     * Consuming resources contributes to soil matter.
     *
     * @param mapCell the cell containing potential food sources
     */
    public void eatEntities(final MapCell mapCell) {
        Animal prey = mapCell.getAnimal();
        Plant plant = mapCell.getPlant();
        Water water = mapCell.getWater();

        boolean atePlant = false;
        boolean drankWater = false;

        if (prey != null && type.getEatsAnimal()) {
            this.setMass(this.getMass() + prey.getMass());
            mapCell.setAnimal(null);
            soilMatter = SOIL_MATTER_BASIC;
        }

        if (plant != null && plant.isScanned() && (prey == null || !type.getEatsAnimal())) {
            atePlant = true;
            this.setMass(this.getMass() + plant.getMass());
            mapCell.setPlant(null);
            soilMatter = SOIL_MATTER_BASIC;
        }

        if (water != null && water.isScanned() && (prey == null || !type.getEatsAnimal())) {
            drankWater = true;
            drinkWater(mapCell);
            soilMatter = SOIL_MATTER_BASIC;
        }

        if (atePlant && drankWater) {
            soilMatter = SOIL_MATTER_COMBINED;
        }
    }

    /**
     * Determines the next cell the animal should move to,
     * prioritizing water quality and scanned entities in adjacent cells.
     *
     * @param mapWorld the map matrix representing the world
     * @param posX     current X position
     * @param posY     current Y position
     * @return the chosen next map cell
     */
    public MapCell findNextCell(final MapMatrix mapWorld, final int posX, final int posY) {
        double bestWaterQuality = 0;
        Direction bestDirection = null;
        Animal animal = mapWorld.getCell(posX, posY).getAnimal();

        // First pass: prioritize water + plant combinations
        for (Direction dir : Direction.values()) {
            int currX = posX + dir.getDirX();
            int currY = posY + dir.getDirY();

            if (mapWorld.isInsideMap(currX, currY)) {
                MapCell currCell = mapWorld.getCell(currX, currY);
                Plant currPlant = currCell.getPlant();
                Water currWater = currCell.getWater();

                if (currCell.getAnimal() != null && !animal.eatsAnimals()) {
                    continue;
                }

                if (currPlant != null && currWater != null
                        && currPlant.isScanned() && currWater.isScanned()) {
                    if (bestWaterQuality < currWater.getWaterQuality()) {
                        bestWaterQuality = currWater.getWaterQuality();
                        bestDirection = dir;
                    }
                } else if (currPlant != null && bestDirection == null && currPlant.isScanned()) {
                    bestDirection = dir;
                }
            }
        }

        // Second pass: fallback to water-only prioritization
        if (bestDirection == null) {
            double waterQuality = 0;

            for (Direction dir : Direction.values()) {
                int currX = posX + dir.getDirX();
                int currY = posY + dir.getDirY();

                if (mapWorld.isInsideMap(currX, currY)) {
                    MapCell currCell = mapWorld.getCell(currX, currY);
                    Water currWater = currCell.getWater();

                    if (currWater != null && currWater.isScanned()) {
                        if (currCell.getAnimal() != null && !animal.eatsAnimals()) {
                            continue;
                        }

                        if (waterQuality < currWater.getWaterQuality()) {
                            waterQuality = currWater.getWaterQuality();
                            bestDirection = dir;
                        }
                    } else if (bestDirection == null) {
                        bestDirection = dir;
                    }
                }
            }
        }

        if (bestDirection == null) {
            return mapWorld.getCell(posX, posY);
        }

        int bestX = posX + bestDirection.getDirX();
        int bestY = posY + bestDirection.getDirY();
        return mapWorld.getCell(bestX, bestY);
    }
}
