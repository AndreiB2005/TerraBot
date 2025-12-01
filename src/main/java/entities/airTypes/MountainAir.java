package entities.airTypes;

import com.fasterxml.jackson.databind.node.ObjectNode;

import fileio.AirInput;
import fileio.CommandInput;

import entities.Air;

/**
 * Represents a mountain-type air entity with specific traits such as altitude.
 * Air quality is influenced by oxygen level, altitude, humidity,
 * and activities like people hiking.
 */
public class MountainAir extends Air {

    /** Constants replacing magic numbers */
    private static final double ALTITUDE_DIVISOR = 1000d;
    private static final double ALTITUDE_OXYGEN_FACTOR = 0.5d;
    private static final double HUMIDITY_FACTOR = 0.6d;
    private static final double HIKER_PENALTY = 0.1d;

    /** Altitude of the mountain air in meters. */
    private final double altitude;

    /**
     * Constructs a MountainAir instance based on input data.
     *
     * @param airInput input data containing air properties
     */
    public MountainAir(final AirInput airInput) {
        super(airInput);
        altitude = airInput.getAltitude();
    }

    /**
     * @return the altitude, rounded to two decimals
     */
    public double getAltitude() {
        return this.roundScore(altitude);
    }

    /**
     * Calculates the air quality for mountain air based on oxygen level,
     * altitude, and humidity.
     *
     * @return the normalized and rounded air quality score
     */
    @Override
    public double calculateAirQuality() {
        double oxygenFactor = this.getOxygenLevel()
                - (altitude / ALTITUDE_DIVISOR * ALTITUDE_OXYGEN_FACTOR);

        double qualityScore = (oxygenFactor * 2)
                + (this.getHumidity() * HUMIDITY_FACTOR);

        return this.roundScore(this.normalizeScore(qualityScore));
    }

    /**
     * Adds mountain-specific traits to the JSON representation.
     *
     * @param objNode the JSON node to augment
     */
    @Override
    public void addSpecificTrait(final ObjectNode objNode) {
        objNode.put("altitude", this.getAltitude());
    }

    /**
     * Checks the effect of a weather or activity command on mountain air.
     * Specifically accounts for the number of people hiking, which slightly
     * reduces air quality.
     *
     * @param cmdInput the command input affecting air
     * @return adjusted air quality after hiking effect,
     *         or Double.MAX_VALUE if the command is not relevant
     */
    @Override
    public double checkWeather(final CommandInput cmdInput) {
        if (!cmdInput.getType().equals("peopleHiking")) {
            return Double.MAX_VALUE;
        }

        int numberOfHikers = cmdInput.getNumberOfHikers();
        return calculateAirQuality() - (numberOfHikers * HIKER_PENALTY);
    }
}
