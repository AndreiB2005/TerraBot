package entities.soilTypes;

import com.fasterxml.jackson.databind.node.ObjectNode;

import fileio.SoilInput;

import entities.Soil;

/**
 * Represents a forest-type soil entity with specific traits such as leaf litter.
 * Soil quality is influenced by nitrogen content, organic matter, water retention,
 * and leaf litter.
 */
public class ForestSoil extends Soil {

    /* Constants for coefficients */

    private static final double NITROGEN_COEFF = 1.2;
    private static final double ORGANIC_MATTER_COEFF = 2.0;
    private static final double WATER_RETENTION_COEFF = 1.5;
    private static final double LEAF_LITTER_COEFF = 0.3;

    private static final double TRAP_WATER_RETENTION_COEFF = 0.6;
    private static final double TRAP_LEAF_LITTER_COEFF = 0.4;
    private static final double TRAP_MAX_VALUE = 80d;
    private static final double PERCENT = 100d;

    /** Amount of leaf litter present in the soil. */
    private final double leafLitter;

    /**
     * Constructs a ForestSoil instance based on input data.
     *
     * @param soilInput input data containing soil properties
     */
    public ForestSoil(final SoilInput soilInput) {
        super(soilInput);
        leafLitter = soilInput.getLeafLitter();
    }

    /**
     * Calculates the quality of the forest soil based on nitrogen, organic matter,
     * water retention, and leaf litter.
     *
     * @return normalized and rounded soil quality score
     */
    @Override
    public double getSoilQuality() {
        double qualityScore = (this.getNitrogen() * NITROGEN_COEFF)
                + (this.getOrganicMatter() * ORGANIC_MATTER_COEFF)
                + (this.getWaterRetention() * WATER_RETENTION_COEFF)
                + (leafLitter * LEAF_LITTER_COEFF);
        return this.roundScore(this.normalizeScore(qualityScore));
    }

    /**
     * Computes the trap value of this forest soil.
     *
     * @return trap value as a percentage
     */
    @Override
    public double getTrap() {
        return ((this.getWaterRetention() * TRAP_WATER_RETENTION_COEFF)
                + (leafLitter * TRAP_LEAF_LITTER_COEFF))
                / TRAP_MAX_VALUE * PERCENT;
    }

    /**
     * @return the leaf litter amount, rounded to two decimals
     */
    public double getLeafLitter() {
        return this.roundScore(leafLitter);
    }

    /**
     * Adds forest-specific traits to the JSON representation.
     *
     * @param objNode the JSON node to augment
     */
    @Override
    public void addSpecificTrait(final ObjectNode objNode) {
        objNode.put("leafLitter", this.getLeafLitter());
    }
}
