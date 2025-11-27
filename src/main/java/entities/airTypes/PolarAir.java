package entities.airTypes;

import com.fasterxml.jackson.databind.node.ObjectNode;

import entities.Air;

public class PolarAir extends Air {
    private final double iceCrystalConcentration;

    public PolarAir(String name, double mass, String type, double humidity,
                    double temperature, double oxygenLevel,
                    double iceCrystalConcentration) {
        super (name, mass, type, humidity, temperature, oxygenLevel);
        this.iceCrystalConcentration = iceCrystalConcentration;
    }

    public double getIceCrystalConcentration() {
        return this.roundScore(iceCrystalConcentration);
    }

    public double getAirQuality() {
        double qualityScore = (this.getOxygenLevel() * 2) +
                (100 - Math.abs(this.getTemperature())) -
                (iceCrystalConcentration * 0.05);
        return this.roundScore(this.normalizeScore(qualityScore));
    }

    public void addSpecificTrait(ObjectNode objNode) {
        objNode.put("iceCrystalConcentration", this.getIceCrystalConcentration());
    }
}
