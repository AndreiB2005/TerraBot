package entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fileio.SoilInput;

import entities.soilTypes.ForestSoil;
import entities.soilTypes.SwampSoil;
import entities.soilTypes.DesertSoil;
import entities.soilTypes.GrasslandSoil;
import entities.soilTypes.TundraSoil;

/**
 * Represents a soil entity with chemical and physical properties.
 * Each soil has a type, nitrogen content, water retention capacity,
 * pH value, and organic matter. Specific soil types extend this class
 * to provide additional traits and behavior.
 */
public abstract class Soil extends Entity {

    /** Magic number constants */
    private static final double MAX_SCORE = 100d;
    private static final double MULTIPLIER_TWO_DECIMALS = 100d;
    private static final double DIVIDER_TWO_DECIMALS = 100d;

    /** The type of soil (e.g., Forest, Swamp, Desert). */
    private final SoilType type;

    /** Nitrogen content of the soil. */
    private final double nitrogen;

    /** Water retention capacity of the soil. */
    private double waterRetention;

    /** Soil pH value. */
    private final double soilpH;

    /** Organic matter content of the soil. */
    private double organicMatter;

    /** Enum representing the available soil types. */
    private enum SoilType {
        ForestSoil,
        SwampSoil,
        DesertSoil,
        GrasslandSoil,
        TundraSoil;
    }

    /**
     * Constructs a soil entity based on the input data.
     *
     * @param soilInput the input data containing soil properties
     */
    public Soil(final SoilInput soilInput) {
        super(soilInput.getName(), soilInput.getMass());
        type = SoilType.valueOf(soilInput.getType());
        nitrogen = soilInput.getNitrogen();
        waterRetention = soilInput.getWaterRetention();
        soilpH = soilInput.getSoilpH();
        organicMatter = soilInput.getOrganicMatter();
    }

    /** @return the string name of this soil's type */
    @Override
    public String getType() {
        return type.name();
    }

    /** @return nitrogen content of the soil */
    public double getNitrogen() {
        return nitrogen;
    }

    /** @return water retention capacity of the soil */
    public double getWaterRetention() {
        return waterRetention;
    }

    /** @return pH value of the soil */
    public double getSoilpH() {
        return soilpH;
    }

    /** @return organic matter content of the soil */
    public double getOrganicMatter() {
        return organicMatter;
    }

    /** Sets the water retention capacity of the soil. */
    public void setWaterRetention(final double waterRetention) {
        this.waterRetention = waterRetention;
    }

    /** Sets the organic matter content of the soil. */
    public void setOrganicMatter(final double organicMatter) {
        this.organicMatter = organicMatter;
    }

    /**
     * Normalizes a score to the range 0–100.
     *
     * @param score the value to normalize
     * @return the normalized score
     */
    public double normalizeScore(final double score) {
        return Math.max(0, Math.min(score, MAX_SCORE));
    }

    /**
     * Rounds a score to two decimal places.
     *
     * @param score the value to round
     * @return the rounded score
     */
    public double roundScore(final double score) {
        return (Math.round(score * MULTIPLIER_TWO_DECIMALS) / DIVIDER_TWO_DECIMALS);
    }

    /**
     * Factory method to create a specific soil type based on input.
     *
     * @param soilInput the input data defining the soil
     * @return a concrete Soil instance
     */
    public static Soil createSoil(final SoilInput soilInput) {
        return switch (soilInput.getType()) {
            case "ForestSoil" -> new ForestSoil(soilInput);
            case "SwampSoil" -> new SwampSoil(soilInput);
            case "DesertSoil" -> new DesertSoil(soilInput);
            case "GrasslandSoil" -> new GrasslandSoil(soilInput);
            case "TundraSoil" -> new TundraSoil(soilInput);
            default -> throw new IllegalArgumentException();
        };
    }

    /**
     * Returns the trap value of this soil, specific to soil type.
     *
     * @return trap value
     */
    public abstract double getTrap();

    /**
     * Computes the soil quality score for this soil.
     *
     * @return soil quality value
     */
    public abstract double getSoilQuality();

    /**
     * Returns a JSON representation of the soil entity, including
     * type, nitrogen, water retention, pH, organic matter, soil quality,
     * and any soil-specific traits.
     *
     * @param mapper the ObjectMapper used to create JSON nodes
     * @return ObjectNode representing this soil
     */
    public ObjectNode printSoil(final ObjectMapper mapper) {
        ObjectNode soilNode = printEntity(mapper);
        soilNode.put("nitrogen", nitrogen);
        soilNode.put("waterRetention",
                Math.round(waterRetention * MULTIPLIER_TWO_DECIMALS) / DIVIDER_TWO_DECIMALS);
        soilNode.put("soilpH", soilpH);
        soilNode.put("organicMatter", organicMatter);
        soilNode.put("soilQuality", getSoilQuality());
        addSpecificTrait(soilNode);
        return soilNode;
    }

    /**
     * Adds soil-type-specific traits to the JSON representation.
     *
     * @param objNode the JSON node to augment
     */
    public abstract void addSpecificTrait(ObjectNode objNode);
}
