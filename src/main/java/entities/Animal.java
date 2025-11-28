package entities;

import fileio.AnimalInput;

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

    public Animal(AnimalInput animalInput) {
        super(animalInput.getName(), animalInput.getMass());
        type = AnimalType.valueOf(animalInput.getType());
    }

    public String getType() {
        return type.name();
    }

    public double getAttack() {
        return (100 - type.getAttackProbability()) / 10d;
    }
}
