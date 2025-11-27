package entities.airTypes;

import com.fasterxml.jackson.databind.node.ObjectNode;

import entities.Air;

public class DesertAir extends Air {
    private final double dustParticles;

    public DesertAir(String name, double mass, String type, double humidity,
                     double temperature, double oxygenLevel,
                     double dustParticles) {
        super(name, mass, type, humidity, temperature, oxygenLevel);
        this.dustParticles = dustParticles;
    }

    public double getDustParticles() {
        return this.roundScore(dustParticles);
    }

    public double getAirQuality() {
        double qualityScore = (this.getOxygenLevel() * 2) -
                (dustParticles * 0.2) -
                (this.getTemperature() * 0.3);
        return this.roundScore(this.normalizeScore(qualityScore));
    }

    public void addSpecificTrait(ObjectNode objNode) {
        objNode.put("dustParticles", this.getDustParticles());
    }
}
