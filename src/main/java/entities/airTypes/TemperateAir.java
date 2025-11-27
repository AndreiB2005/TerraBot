package entities.airTypes;

import com.fasterxml.jackson.databind.node.ObjectNode;

import entities.Air;

public class TemperateAir extends Air {
    private final double pollenLevel;

    public TemperateAir(String name, double mass, String type, double humidity,
                        double temperature, double oxygenLevel,
                        double pollenLevel) {
        super(name, mass, type, humidity, temperature, oxygenLevel);
        this.pollenLevel = pollenLevel;
    }

    public double getPollenLevel() {
        return this.roundScore(pollenLevel);
    }

    public double getAirQuality() {
        double qualityScore = (this.getOxygenLevel() * 2) +
                (this.getHumidity() * 0.7) -
                (pollenLevel * 0.1);
        return this.roundScore(this.normalizeScore(qualityScore));
    }

    public void addSpecificTrait(ObjectNode objNode) {
        objNode.put("pollenLevel", this.getPollenLevel());
    }
}
