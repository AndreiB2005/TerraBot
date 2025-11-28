package entities.airTypes;

import com.fasterxml.jackson.databind.node.ObjectNode;

import fileio.AirInput;
import fileio.CommandInput;

import entities.Air;

public class PolarAir extends Air {
    private final double iceCrystalConcentration;

    public PolarAir(AirInput airInput) {
        super (airInput);
        iceCrystalConcentration = airInput.getIceCrystalConcentration();
    }

    public double getIceCrystalConcentration() {
        return this.roundScore(iceCrystalConcentration);
    }

    public double calculateAirQuality() {
        double qualityScore = (this.getOxygenLevel() * 2) +
                (100 - Math.abs(this.getTemperature())) -
                (iceCrystalConcentration * 0.05);
        return this.roundScore(this.normalizeScore(qualityScore));
    }

    public void addSpecificTrait(ObjectNode objNode) {
        objNode.put("iceCrystalConcentration", this.getIceCrystalConcentration());
    }

    public double checkWeather(CommandInput cmdInput) {
        if (!cmdInput.getType().equals("windSpeed"))
            return Double.MAX_VALUE;
        double windSpeed = cmdInput.getWindSpeed();
        return (calculateAirQuality() - (windSpeed * 0.2));
    }
}
