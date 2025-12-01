package entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fileio.AirInput;
import fileio.CommandInput;

import entities.airTypes.TropicalAir;
import entities.airTypes.PolarAir;
import entities.airTypes.TemperateAir;
import entities.airTypes.DesertAir;
import entities.airTypes.MountainAir;

/**
 * Represents an air entity with properties such as humidity, temperature,
 * oxygen level, and air quality. Each air type has a maximum air quality
 * score and may have specific traits and behaviors.
 */
public abstract class Air extends Entity {

    /** Magic number constants */
    private static final double MAX_SCORE = 100d;
    private static final double MULTIPLIER_TWO_DECIMALS = 100d;
    private static final double DIVIDER_TWO_DECIMALS = 100d;
    private static final double TOXICITY_THRESHOLD = 0.8d;

    /** The type of air (Tropical, Polar, Temperate, Desert, Mountain). */
    private final AirType type;

    /** Current humidity level (percentage). */
    private double humidity;

    /** Temperature of the air (degrees). */
    private final double temperature;

    /** Oxygen level in the air (percentage). */
    private double oxygenLevel;

    /** Current computed air quality. */
    private double currQuality;

    /** Timestamp tracking weather changes. */
    private int weatherTimestamp = 0;

    /** Enum representing the available air types with their maximum air quality. */
    private enum AirType {
        TropicalAir(82),
        PolarAir(142),
        TemperateAir(84),
        DesertAir(65),
        MountainAir(78);

        private final double maxScore;

        AirType(final double maxScore) {
            this.maxScore = maxScore;
        }

        /** @return maximum possible air quality for this type */
        public double getMaxScore() {
            return maxScore;
        }
    }

    /**
     * Constructs an air entity based on the input data.
     *
     * @param airInput the input data containing air properties
     */
    public Air(final AirInput airInput) {
        super(airInput.getName(), airInput.getMass());
        type = AirType.valueOf(airInput.getType());
        humidity = airInput.getHumidity();
        temperature = airInput.getTemperature();
        oxygenLevel = airInput.getOxygenLevel();
    }

    /** @return the string name of this air type */
    @Override
    public String getType() {
        return type.name();
    }

    /** @return the current humidity */
    public double getHumidity() {
        return humidity;
    }

    /** @return the temperature */
    public double getTemperature() {
        return temperature;
    }

    /** @return the current oxygen level */
    public double getOxygenLevel() {
        return oxygenLevel;
    }

    /** @return the current air quality */
    public double getCurrQuality() {
        return currQuality;
    }

    /** @return the weather timestamp */
    public int getWeatherTimestamp() {
        return weatherTimestamp;
    }

    /** Sets the humidity level. */
    public void setHumidity(final double humidity) {
        this.humidity = humidity;
    }

    /** Sets the oxygen level. */
    public void setOxygenLevel(final double oxygenLevel) {
        this.oxygenLevel = oxygenLevel;
    }

    /** Sets the current air quality. */
    public void setCurrQuality(final double currQuality) {
        this.currQuality = currQuality;
    }

    /** Sets the weather timestamp, ensuring it is non-negative. */
    public void setWeatherTimestamp(final int weatherTimestamp) {
        this.weatherTimestamp = Math.max(0, weatherTimestamp);
    }

    /**
     * Normalizes a score to the range 0–100.
     *
     * @param score the value to normalize
     * @return normalized score
     */
    public double normalizeScore(final double score) {
        return Math.max(0, Math.min(score, MAX_SCORE));
    }

    /**
     * Rounds a score to two decimal places.
     *
     * @param score the value to round
     * @return rounded score
     */
    public double roundScore(final double score) {
        return Math.round(score * MULTIPLIER_TWO_DECIMALS) / DIVIDER_TWO_DECIMALS;
    }

    /**
     * Factory method to create a specific air type based on input.
     *
     * @param airInput the input data defining the air
     * @return a concrete Air instance
     */
    public static Air createAir(final AirInput airInput) {
        return switch (airInput.getType()) {
            case "TropicalAir" -> new TropicalAir(airInput);
            case "PolarAir" -> new PolarAir(airInput);
            case "TemperateAir" -> new TemperateAir(airInput);
            case "DesertAir" -> new DesertAir(airInput);
            case "MountainAir" -> new MountainAir(airInput);
            default -> throw new IllegalArgumentException();
        };
    }

    /** Calculates the air quality for this specific air type. */
    public abstract double calculateAirQuality();

    /**
     * Computes the toxicity of the air based on the maximum air quality score.
     *
     * @return toxicity value (0–100)
     */
    public double getToxicity() {
        double toxicityAQ = MAX_SCORE * (1 - this.calculateAirQuality() / type.getMaxScore());
        return Math.max(Math.round(toxicityAQ * MULTIPLIER_TWO_DECIMALS) / DIVIDER_TWO_DECIMALS, 0);
    }

    /** @return true if the air is considered toxic (toxicity > 80% of max score) */
    public boolean isToxic() {
        return getToxicity() > type.getMaxScore() * TOXICITY_THRESHOLD;
    }

    /**
     * Returns a JSON representation of the air entity, including humidity,
     * temperature, oxygen level, and current air quality.
     *
     * @param mapper the ObjectMapper used to create JSON nodes
     * @return ObjectNode representing this air
     */
    public ObjectNode printAir(final ObjectMapper mapper) {
        ObjectNode airNode = this.printEntity(mapper);
        airNode.put("humidity", Math.round(humidity
                * MULTIPLIER_TWO_DECIMALS) / DIVIDER_TWO_DECIMALS);
        airNode.put("temperature", temperature);
        airNode.put("oxygenLevel", Math.round(oxygenLevel
                * MULTIPLIER_TWO_DECIMALS) / DIVIDER_TWO_DECIMALS);
        airNode.put("airQuality", currQuality);
        this.addSpecificTrait(airNode);
        return airNode;
    }

    /** Adds air-type-specific traits to the JSON representation. */
    public abstract void addSpecificTrait(ObjectNode objNode);

    /**
     * Evaluates the effect of weather commands on this air entity.
     *
     * @param cmdInput the input command affecting weather
     * @return resulting value or impact from the weather check
     */
    public abstract double checkWeather(CommandInput cmdInput);
}
