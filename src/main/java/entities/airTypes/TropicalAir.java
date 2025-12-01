package entities.airTypes;

import com.fasterxml.jackson.databind.node.ObjectNode;

import fileio.AirInput;
import fileio.CommandInput;

import entities.Air;

/**
 * Represents a tropical-type air entity with specific traits such as CO2 level.
 * Air quality is influenced by oxygen level, humidity, CO2 concentration, and rainfall.
 */
public class TropicalAir extends Air {

    /** Constants replacing magic numbers */
    private static final double HUMIDITY_FACTOR = 0.5d;
    private static final double CO2_FACTOR = 0.01d;
    private static final double RAINFALL_BONUS = 0.3d;

    /** CO2 concentration level in the air. */
    private final double co2Level;

    /**
     * Constructs a TropicalAir instance based on input data.
     *
     * @param airInput input data containing air properties
     */
    public TropicalAir(final AirInput airInput) {
        super(airInput);
        co2Level = airInput.getCo2Level();
    }

    /**
     * @return CO2 level, rounded to two decimals
     */
    public double getCo2Level() {
        return this.roundScore(co2Level);
    }

    /**
     * Calculates the air quality for tropical air based on oxygen level,
     * humidity, and CO2 concentration.
     *
     * @return the normalized and rounded air quality score
     */
    @Override
    public double calculateAirQuality() {
        double qualityScore = (this.getOxygenLevel() * 2)
                + (this.getHumidity() * HUMIDITY_FACTOR)
                - (co2Level * CO2_FACTOR);

        return this.roundScore(this.normalizeScore(qualityScore));
    }

    /**
     * Adds tropical-specific traits to the JSON representation.
     *
     * @param objNode the JSON node to augment
     */
    @Override
    public void addSpecificTrait(final ObjectNode objNode) {
        objNode.put("co2Level", this.getCo2Level());
    }

    /**
     * Checks the effect of a weather command on tropical air.
     * Specifically accounts for rainfall, which increases air quality.
     *
     * @param cmdInput the command input affecting air
     * @return adjusted air quality after rainfall effect,
     *         or Double.MAX_VALUE if the command is not relevant
     */
    @Override
    public double checkWeather(final CommandInput cmdInput) {
        if (!cmdInput.getType().equals("rainfall")) {
            return Double.MAX_VALUE;
        }

        double rainfall = cmdInput.getRainfall();
        return calculateAirQuality() + (rainfall * RAINFALL_BONUS);
    }
}
