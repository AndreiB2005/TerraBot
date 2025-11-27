package entities;

public class Animal extends Entity {
    private final AnimalType type;

    private enum AnimalType {
        Herbivores(85),
        Carnivores(30),
        Omnivores(60),
        Detritivores(90),
        Parasites(10);

        private final double attackProbability;

        AnimalType(double attackProbability) {
            this.attackProbability = attackProbability;
        }

        public double getAttackProbability() {
            return attackProbability;
        }
    }

    public Animal(String name, double mass, String type) {
        super(name, mass);
        this.type = AnimalType.valueOf(type);
    }

    public String getType() {
        return type.name();
    }

    public double getAttack() {
        return (100 - type.getAttackProbability()) / 10;
    }
}
