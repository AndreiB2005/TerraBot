package entities.airTypes;

import com.fasterxml.jackson.databind.node.ObjectNode;

import fileio.AirInput;
import fileio.CommandInput;

import entities.Air;

/**
 * Represents a desert-type air entity with specific traits
 * such as dust particles and the possibility of a desert storm.
 * Desert air quality is influenced by oxygen level, dust, temperature,
 * and weather events.
 */
public class DesertAir extends Air {

    /** Magic number constants */
    private static final double DUST_FACTOR = 0.2d;
    private static final double TEMPERATURE_FACTOR = 0.3d;
    private static final double DESERT_STORM_PENALTY = 30d;

    /** Amount of dust particles present in the air. */
    private final double dustParticles;

    /** Indicates whether a desert storm is currently occurring. */
    private boolean desertStorm;

    /**
     * Constructs a DesertAir instance based on input data.
     *
     * @param airInput input data containing air properties
     */
    public DesertAir(final AirInput airInput) {
        super(airInput);
        dustParticles = airInput.getDustParticles();
    }

    /**
     * @return the amount of dust particles, rounded to two decimals
     */
    public double getDustParticles() {
        return this.roundScore(dustParticles);
    }

    /**
     * Calculates the air quality for desert air based on oxygen level,
     * dust particles, and temperature.
     *
     * @return the normalized and rounded air quality score
     */
    @Override
    public double calculateAirQuality() {
        double qualityScore = (this.getOxygenLevel() * 2)
                - (dustParticles * DUST_FACTOR)
                - (this.getTemperature() * TEMPERATURE_FACTOR);
        return this.roundScore(this.normalizeScore(qualityScore));
    }

    /**
     * Adds desert-specific traits to the JSON representation.
     *
     * @param objNode the JSON node to augment
     */
    @Override
    public void addSpecificTrait(final ObjectNode objNode) {
        objNode.put("desertStorm", desertStorm);
    }

    /**
     * Checks the effect of a weather command on desert air.
     * Specifically handles desert storms, which decrease air quality.
     *
     * @param cmdInput the weather command input
     * @return adjusted air quality after applying desert storm effect,
     *         or Double.MAX_VALUE if the command is not relevant
     */
    @Override
    public double checkWeather(final CommandInput cmdInput) {
        if (!cmdInput.getType().equals("desertStorm")) {
            return Double.MAX_VALUE;
        }

        desertStorm = cmdInput.isDesertStorm();
        double desertStormPenalty = desertStorm ? DESERT_STORM_PENALTY : 0d;
        return (calculateAirQuality() - desertStormPenalty);
    }
}
