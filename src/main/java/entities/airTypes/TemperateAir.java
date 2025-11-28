package entities.airTypes;

import com.fasterxml.jackson.databind.node.ObjectNode;

import fileio.AirInput;
import fileio.CommandInput;

import entities.Air;

public class TemperateAir extends Air {
    private final double pollenLevel;

    public TemperateAir(AirInput airInput) {
        super(airInput);
        pollenLevel = airInput.getPollenLevel();
    }

    public double getPollenLevel() {
        return this.roundScore(pollenLevel);
    }

    public double calculateAirQuality() {
        double qualityScore = (this.getOxygenLevel() * 2) +
                (this.getHumidity() * 0.7) -
                (pollenLevel * 0.1);
        return this.roundScore(this.normalizeScore(qualityScore));
    }

    public void addSpecificTrait(ObjectNode objNode) {
        objNode.put("pollenLevel", this.getPollenLevel());
    }

    public double checkWeather(CommandInput cmdInput) {
        if (!cmdInput.getType().equals("season"))
            return Double.MAX_VALUE;
        String season = cmdInput.getSeason();
        double seasonPenalty = season.equalsIgnoreCase("Spring") ? 15 : 0;
        return (calculateAirQuality() - seasonPenalty);
    }
}
