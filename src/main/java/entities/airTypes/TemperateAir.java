package entities.airTypes;

import com.fasterxml.jackson.databind.node.ObjectNode;

import fileio.AirInput;
import fileio.CommandInput;

import entities.Air;

/**
 * Represents a temperate-type air entity with specific traits such as pollen level.
 * Air quality is influenced by oxygen level, humidity, pollen concentration, and seasonal effects.
 */
public class TemperateAir extends Air {

    /** Magic number constants */
    private static final double HUMIDITY_FACTOR = 0.7d;
    private static final double POLLEN_FACTOR = 0.1d;
    private static final double SPRING_SEASON_PENALTY = 15d;

    /** Level of pollen in the air. */
    private final double pollenLevel;

    /**
     * Constructs a TemperateAir instance based on input data.
     *
     * @param airInput input data containing air properties
     */
    public TemperateAir(final AirInput airInput) {
        super(airInput);
        pollenLevel = airInput.getPollenLevel();
    }

    /**
     * @return pollen level, rounded to two decimals
     */
    public double getPollenLevel() {
        return this.roundScore(pollenLevel);
    }

    /**
     * Calculates the air quality for temperate air based on oxygen level,
     * humidity, and pollen concentration.
     *
     * @return the normalized and rounded air quality score
     */
    @Override
    public double calculateAirQuality() {
        double qualityScore = (this.getOxygenLevel() * 2)
                + (this.getHumidity() * HUMIDITY_FACTOR)
                - (pollenLevel * POLLEN_FACTOR);
        return this.roundScore(this.normalizeScore(qualityScore));
    }

    /**
     * Adds temperate-specific traits to the JSON representation.
     *
     * @param objNode the JSON node to augment
     */
    @Override
    public void addSpecificTrait(final ObjectNode objNode) {
        objNode.put("pollenLevel", this.getPollenLevel());
    }

    /**
     * Checks the effect of a weather command on temperate air.
     * Specifically accounts for the season, which may reduce air quality.
     *
     * @param cmdInput the command input affecting air
     * @return adjusted air quality after seasonal effect,
     *         or Double.MAX_VALUE if the command is not relevant
     */
    @Override
    public double checkWeather(final CommandInput cmdInput) {
        if (!cmdInput.getType().equals("newSeason")) {
            return Double.MAX_VALUE;
        }

        String season = cmdInput.getSeason();
        double seasonPenalty = season.equalsIgnoreCase("Spring") ? SPRING_SEASON_PENALTY : 0d;
        return (calculateAirQuality() - seasonPenalty);
    }
}
