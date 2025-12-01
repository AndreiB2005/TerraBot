package entities.airTypes;

import com.fasterxml.jackson.databind.node.ObjectNode;

import fileio.AirInput;
import fileio.CommandInput;

import entities.Air;

/**
 * Represents a polar-type air entity with specific traits such as ice crystal concentration.
 * Air quality is influenced by oxygen level, temperature, ice crystals, and wind speed.
 */
public class PolarAir extends Air {

    /** Magic number constants */
    private static final double TEMPERATURE_BASE = 100d;
    private static final double ICE_FACTOR = 0.05d;
    private static final double WIND_SPEED_FACTOR = 0.2d;

    /** Concentration of ice crystals in the air. */
    private final double iceCrystalConcentration;

    /**
     * Constructs a PolarAir instance based on input data.
     *
     * @param airInput input data containing air properties
     */
    public PolarAir(final AirInput airInput) {
        super(airInput);
        iceCrystalConcentration = airInput.getIceCrystalConcentration();
    }

    /**
     * @return ice crystal concentration, rounded to two decimals
     */
    public double getIceCrystalConcentration() {
        return this.roundScore(iceCrystalConcentration);
    }

    /**
     * Calculates the air quality for polar air based on oxygen level,
     * temperature, and ice crystal concentration.
     *
     * @return the normalized and rounded air quality score
     */
    @Override
    public double calculateAirQuality() {
        double qualityScore = (this.getOxygenLevel() * 2)
                + (TEMPERATURE_BASE - Math.abs(this.getTemperature()))
                - (iceCrystalConcentration * ICE_FACTOR);
        return this.roundScore(this.normalizeScore(qualityScore));
    }

    /**
     * Adds polar-specific traits to the JSON representation.
     *
     * @param objNode the JSON node to augment
     */
    @Override
    public void addSpecificTrait(final ObjectNode objNode) {
        objNode.put("iceCrystalConcentration", this.getIceCrystalConcentration());
    }

    /**
     * Checks the effect of a weather command on polar air.
     * Specifically accounts for wind speed, which reduces air quality.
     *
     * @param cmdInput the command input affecting air
     * @return adjusted air quality after wind effect,
     *         or Double.MAX_VALUE if the command is not relevant
     */
    @Override
    public double checkWeather(final CommandInput cmdInput) {
        if (!cmdInput.getType().equals("windSpeed")) {
            return Double.MAX_VALUE;
        }

        double windSpeed = cmdInput.getWindSpeed();
        return (calculateAirQuality() - (windSpeed * WIND_SPEED_FACTOR));
    }
}
