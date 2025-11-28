package entities.airTypes;

import com.fasterxml.jackson.databind.node.ObjectNode;

import fileio.AirInput;

import entities.Air;
import fileio.CommandInput;

public class TropicalAir extends Air {
    private final double co2Level;

    public TropicalAir(AirInput airInput) {
        super(airInput);
        co2Level = airInput.getCo2Level();
    }

    public double getCo2Level() {
        return this.roundScore(co2Level);
    }

    public double calculateAirQuality() {
        double qualityScore = (this.getOxygenLevel() * 2) +
                (this.getHumidity() * 0.5) -
                (co2Level * 0.01);
        return this.roundScore(this.normalizeScore(qualityScore));
    }

    public void addSpecificTrait(ObjectNode objNode) {
        objNode.put("co2Level", this.getCo2Level());
    }

    public double checkWeather(CommandInput cmdInput) {
        if (!cmdInput.getType().equals("rainfall"))
            return Double.MAX_VALUE;
        double rainfall = cmdInput.getRainfall();
        return (calculateAirQuality() + (rainfall * 0.3));
    }
}
