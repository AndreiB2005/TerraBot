package entities.airTypes;

import com.fasterxml.jackson.databind.node.ObjectNode;

import entities.Air;

public class MountainAir extends Air {
    private final double altitude;

    public MountainAir(String name, double mass, String type, double humidity,
                       double temperature, double oxygenLevel,
                       double altitude) {
        super(name, mass, type, humidity, temperature, oxygenLevel);
        this.altitude = altitude;
    }

    public double getAltitude() {
        return this.roundScore(altitude);
    }

    public double getAirQuality() {
        double oxygenFactor = this.getOxygenLevel() - (altitude / 1000 * 0.5);
        double qualityScore = (oxygenFactor * 2) +
                (this.getHumidity() * 0.6);
        return this.roundScore(this.normalizeScore(qualityScore));
    }

    public void addSpecificTrait(ObjectNode objNode) {
        objNode.put("altitude", this.getAltitude());
    }
}
