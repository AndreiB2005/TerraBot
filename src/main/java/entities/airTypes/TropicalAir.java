package entities.airTypes;

import com.fasterxml.jackson.databind.node.ObjectNode;

import entities.Air;

public class TropicalAir extends Air {
    private final double co2Level;

    public TropicalAir(String name, double mass, String type, double humidity,
                       double temperature, double oxygenLevel,
                       double co2Level) {
        super(name, mass, type, humidity, temperature, oxygenLevel);
        this.co2Level = co2Level;
    }

    public double getCo2Level() {
        return this.roundScore(co2Level);
    }

    public double getAirQuality() {
        double qualityScore = (this.getOxygenLevel() * 2) +
                (this.getHumidity() * 0.5) -
                (co2Level * 0.01);
        return this.roundScore(this.normalizeScore(qualityScore));
    }

    public void addSpecificTrait(ObjectNode objNode) {
        objNode.put("co2Level", this.getCo2Level());
    }
}
