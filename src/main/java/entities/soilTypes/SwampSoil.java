package entities.soilTypes;

import com.fasterxml.jackson.databind.node.ObjectNode;

import fileio.SoilInput;

import entities.Soil;

/**
 * Represents a swamp-type soil entity with specific traits such as water logging.
 * Soil quality is influenced by nitrogen content, organic matter, and water logging.
 */
public class SwampSoil extends Soil {

    private static final double NITROGEN_COEFFICIENT = 1.1;
    private static final double ORGANIC_MATTER_COEFFICIENT = 2.2;
    private static final double WATER_LOGGING_PENALTY = 5;
    private static final double TRAP_COEFFICIENT = 10;

    /** Level of water logging in the soil. */
    private final double waterLogging;

    /**
     * Constructs a SwampSoil instance based on input data.
     *
     * @param soilInput input data containing soil properties
     */
    public SwampSoil(final SoilInput soilInput) {
        super(soilInput);
        waterLogging = soilInput.getWaterLogging();
    }

    /**
     * Calculates the quality of the swamp soil based on nitrogen, organic matter,
     * and water logging.
     *
     * @return normalized and rounded soil quality score
     */
    @Override
    public double getSoilQuality() {
        double qualityScore = (this.getNitrogen() * NITROGEN_COEFFICIENT)
                + (this.getOrganicMatter() * ORGANIC_MATTER_COEFFICIENT)
                - (waterLogging * WATER_LOGGING_PENALTY);
        return this.roundScore(this.normalizeScore(qualityScore));
    }

    /**
     * Computes the trap value of this swamp soil.
     *
     * @return trap value as a percentage
     */
    @Override
    public double getTrap() {
        return waterLogging * TRAP_COEFFICIENT;
    }

    /**
     * @return the water logging level, rounded to two decimals
     */
    public double getWaterLogging() {
        return this.roundScore(waterLogging);
    }

    /**
     * Adds swamp-specific traits to the JSON representation.
     *
     * @param objNode the JSON node to augment
     */
    @Override
    public void addSpecificTrait(final ObjectNode objNode) {
        objNode.put("waterLogging", this.getWaterLogging());
    }
}
