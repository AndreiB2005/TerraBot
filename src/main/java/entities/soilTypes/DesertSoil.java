package entities.soilTypes;

import com.fasterxml.jackson.databind.node.ObjectNode;

import fileio.SoilInput;

import entities.Soil;

/**
 * Represents a desert-type soil entity with specific traits such as salinity.
 * Soil quality is influenced by nitrogen content, water retention, and salinity.
 */
public class DesertSoil extends Soil {

    /** Coefficients used in soil quality calculation. */
    private static final double NITROGEN_COEFFICIENT = 0.5;
    private static final double WATER_RETENTION_COEFFICIENT = 0.3;
    private static final double SALINITY_COEFFICIENT = 2.0;

    /** Trap-related constants. */
    private static final double MAX_PERCENT = 100d;

    /** Salinity level of the soil. */
    private final double salinity;

    /**
     * Constructs a DesertSoil instance based on input data.
     *
     * @param soilInput input data containing soil properties
     */
    public DesertSoil(final SoilInput soilInput) {
        super(soilInput);
        salinity = soilInput.getSalinity();
    }

    /**
     * Calculates the quality of the desert soil based on nitrogen, water retention,
     * and salinity.
     *
     * @return normalized and rounded soil quality score
     */
    @Override
    public double getSoilQuality() {
        double qualityScore = (this.getNitrogen() * NITROGEN_COEFFICIENT)
                + (this.getWaterRetention() * WATER_RETENTION_COEFFICIENT)
                + (salinity * SALINITY_COEFFICIENT);
        return this.roundScore(this.normalizeScore(qualityScore));
    }

    /**
     * Computes the trap value of this desert soil.
     *
     * @return trap value as a percentage
     */
    @Override
    public double getTrap() {
        return (MAX_PERCENT - this.getWaterRetention() + salinity) / MAX_PERCENT * MAX_PERCENT;
    }

    /**
     * @return the salinity level, rounded to two decimals
     */
    public double getSalinity() {
        return this.roundScore(salinity);
    }

    /**
     * Adds desert-specific traits to the JSON representation.
     *
     * @param objNode the JSON node to augment
     */
    @Override
    public void addSpecificTrait(final ObjectNode objNode) {
        objNode.put("salinity", this.getSalinity());
    }
}
