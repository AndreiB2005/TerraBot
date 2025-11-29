package entities;

import fileio.AnimalInput;

import direction.Direction;
import worldMap.MapCell;
import worldMap.MapMatrix;

public class Animal extends Entity {
    private final AnimalType type;
    private int scanTimestamp = 1;
    private double soilMatter = 0;
    private boolean doneIteration = false;

    private enum AnimalType {
        Herbivores(85, false),
        Carnivores(30, true),
        Omnivores(60, false),
        Detritivores(90, false),
        Parasites(10, true);

        private final double attackProbability;
        private final boolean eatsAnimal;

        AnimalType(double attackProbability, boolean eatsAnimal) {
            this.attackProbability = attackProbability;
            this.eatsAnimal = eatsAnimal;
        }

        public double getAttackProbability() {
            return attackProbability;
        }

        public boolean getEatsAnimal() {
            return eatsAnimal;
        }
    }

    public Animal(AnimalInput animalInput) {
        super(animalInput.getName(), animalInput.getMass());
        type = AnimalType.valueOf(animalInput.getType());
    }

    public String getType() {
        return type.name();
    }

    public int getScanTimestamp() {
        return scanTimestamp;
    }

    public double getSoilMatter() {
        return soilMatter;
    }

    public boolean getDoneIteration() {
        return doneIteration;
    }

    public void setSoilMatter(double soilMatter) {
        this.soilMatter = soilMatter;
    }

    public void setDoneIteration(boolean doneIteration) {
        this.doneIteration = doneIteration;
    }

    public void incScanTimestamp() {
        scanTimestamp++;
    }

    public double getAttack() {
        return (100 - type.getAttackProbability()) / 10d;
    }

    public String getScanResult() {
        return "The scanned object is an animal.";
    }

    public void drinkWater(MapCell mapCell) {
        Water water = mapCell.getWater();
        double intakeRate = 0.08;
        double waterToDrink = Math.min(this.getMass() * intakeRate, water.getMass());
        this.setMass(this.getMass() + waterToDrink);
        water.setMass(water.getMass() - waterToDrink);
        if (water.getMass() == 0)
            mapCell.setWater(null);
    }

    public void eatEntities(MapCell mapCell) {
        Animal prey = mapCell.getAnimal();
        Plant plant = mapCell.getPlant();
        Water water = mapCell.getWater();
        boolean isPlant = false;
        boolean isWater = false;
        if (prey != null && type.getEatsAnimal()) {
            this.setMass(this.getMass() + prey.getMass());
            mapCell.setAnimal(null);
            soilMatter = 0.5;
        }
        if (plant != null && plant.isScanned() && (prey == null || !type.getEatsAnimal())) {
            isPlant = true;
            this.setMass(this.getMass() + plant.getMass());
            mapCell.setPlant(null);
            soilMatter = 0.5;
        }
        if (water != null && water.isScanned() && (prey == null || !type.getEatsAnimal())) {
            isWater = true;
            drinkWater(mapCell);
            soilMatter = 0.5;
        }

        if (isPlant && isWater)
            soilMatter = 0.8;
    }

    public MapCell findNextCell(MapMatrix mapWorld, int posX, int posY) {
        double bestWaterQuality = 0;
        Direction bestDirection = null;
        for (Direction dir : Direction.values()) {
            int currX = posX + dir.getDirX();
            int currY = posY + dir.getDirY();
            if (mapWorld.isInsideMap(currX, currY)) {
                MapCell currCell = mapWorld.getCell(currX, currY);
                Plant currPlant = currCell.getPlant();
                Water currWater = currCell.getWater();
                if (currPlant != null && currWater != null &&
                        currPlant.isScanned() && currWater.isScanned()) {
                    if (bestWaterQuality < currWater.getWaterQuality()) {
                        bestWaterQuality = currWater.getWaterQuality();
                        bestDirection = dir;
                    }
                } else if (currPlant != null && bestDirection == null && currPlant.isScanned()) {
                    bestDirection = dir;
                }
            }
        }
        if (bestDirection == null) {
            double waterQuality = 0;
            for (Direction dir : Direction.values()) {
                int currX = posX + dir.getDirX();
                int currY = posY + dir.getDirY();
                if (mapWorld.isInsideMap(currX, currY)) {
                    MapCell currCell = mapWorld.getCell(currX, currY);
                    Water currWater = currCell.getWater();
                    if (currWater != null && currWater.isScanned()) {
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
        int bestX = posX + bestDirection.getDirX();
        int bestY = posY + bestDirection.getDirY();
        return mapWorld.getCell(bestX, bestY);
    }
}
