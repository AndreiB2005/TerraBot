package entities.airTypes;

import com.fasterxml.jackson.databind.node.ObjectNode;

import fileio.AirInput;
import fileio.CommandInput;

import entities.Air;

public class MountainAir extends Air {
    private final double altitude;

    public MountainAir(AirInput airInput) {
        super(airInput);
        altitude = airInput.getAltitude();
    }

    public double getAltitude() {
        return this.roundScore(altitude);
    }

    public double calculateAirQuality() {
        double oxygenFactor = this.getOxygenLevel() - (altitude / 1000 * 0.5);
        double qualityScore = (oxygenFactor * 2) +
                (this.getHumidity() * 0.6);
        return this.roundScore(this.normalizeScore(qualityScore));
    }

    public void addSpecificTrait(ObjectNode objNode) {
        objNode.put("altitude", this.getAltitude());
    }

    public double checkWeather(CommandInput cmdInput) {
        if (!cmdInput.getType().equals("peopleHiking"))
            return Double.MAX_VALUE;
        int numberOfHikers = cmdInput.getNumberOfHikers();
        return (calculateAirQuality() - numberOfHikers * 0.1);
    }
}
