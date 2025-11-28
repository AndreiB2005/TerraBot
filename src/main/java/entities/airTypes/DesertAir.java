package entities.airTypes;

import com.fasterxml.jackson.databind.node.ObjectNode;

import fileio.AirInput;
import fileio.CommandInput;

import entities.Air;

public class DesertAir extends Air {
    private final double dustParticles;
    private boolean desertStorm;

    public DesertAir(AirInput airInput) {
        super(airInput);
        dustParticles = airInput.getDustParticles();
    }

    public double getDustParticles() {
        return this.roundScore(dustParticles);
    }

    public double calculateAirQuality() {
        double qualityScore = (this.getOxygenLevel() * 2) -
                (dustParticles * 0.2) -
                (this.getTemperature() * 0.3);
        return this.roundScore(this.normalizeScore(qualityScore));
    }

    public void addSpecificTrait(ObjectNode objNode) {
        objNode.put("desertStorm", desertStorm);
    }

    public double checkWeather(CommandInput cmdInput) {
        if (!cmdInput.getType().equals("desertStorm"))
            return Double.MAX_VALUE;
        desertStorm = cmdInput.isDesertStorm();
        double desertStormPenalty = desertStorm ? 30d : 0d;
        return (calculateAirQuality() - desertStormPenalty);
    }
}
