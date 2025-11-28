package entities;

import fileio.PlantInput;

public class Plant extends Entity {
    private final PlantType type;
    private Maturity age = Maturity.Young;
    private double growthLevel = 0d;

    private enum PlantType {
        FloweringPlants(6d, 90),
        GymnospermsPlants(0d, 60),
        Ferns(0d, 30),
        Mosses(0.8, 40),
        Algae(0.5, 20);

        private final double oxygenPlant;
        private final double stuckProbability;

        PlantType(double oxygenPlant, double stuckProbability) {
            this.oxygenPlant = oxygenPlant;
            this.stuckProbability = stuckProbability;
        }

        public double getOxygenPlant() {
            return oxygenPlant;
        }

        public double getStuckProbability() {
            return stuckProbability;
        }
    }

    private enum Maturity {
        Young(0.2),
        Mature(0.7),
        Old(0.4);

        private final double oxygenBonus;

        Maturity(double oxygenBonus) {
            this.oxygenBonus = oxygenBonus;
        }

        public double getOxygenBonus() {
            return oxygenBonus;
        }

        public Maturity nextLevel() {
            return switch (this) {
                case Young -> Mature;
                case Mature -> Old;
                case Old -> null;
            };
        }
    }

    public Plant(PlantInput plantInput) {
        super(plantInput.getName(), plantInput.getMass());
        type = PlantType.valueOf(plantInput.getType());
    }

    public String getType() {
        return type.name();
    }

    public double generateOxygen() {
        return type.getOxygenPlant() + age.getOxygenBonus();
    }

    public void growPlant() {
        growthLevel += 0.2;
        if (growthLevel > 1d) {
            growthLevel -= 1d;
            age = age.nextLevel();
        }
    }

    public double getStuck() {
        return type.getStuckProbability() / 100d;
    }

    public boolean isDead() {
        return age == null;
    }
}
