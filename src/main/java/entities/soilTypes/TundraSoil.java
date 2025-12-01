package entities.soilTypes;

import com.fasterxml.jackson.databind.node.ObjectNode;

import fileio.SoilInput;

import entities.Soil;

/**
 * Represents a tundra-type soil entity with specific traits such as permafrost depth.
 * Soil quality is influenced by nitrogen content, organic matter, and permafrost depth.
 */
public class TundraSoil extends Soil {

    /** Coefficients used in soil quality calculation. */
    private static final double NITROGEN_COEFFICIENT = 0.7;
    private static final double ORGANIC_MATTER_COEFFICIENT = 0.5;
    private static final double PERMAFROST_PENALTY = 1.5;

    /** Trap-related constants. */
    private static final double MAX_TRAP_DEPTH = 50d;
    private static final double PERCENT_FACTOR = 100d;

    /** Depth of permafrost in the soil. */
    private final double permafrostDepth;

    /**
     * Constructs a TundraSoil instance based on input data.
     *
     * @param soilInput input data containing soil properties
     */
    public TundraSoil(final SoilInput soilInput) {
        super(soilInput);
        permafrostDepth = soilInput.getPermafrostDepth();
    }

    /**
     * Calculates the quality of the tundra soil based on nitrogen, organic matter,
     * and permafrost depth.
     *
     * @return normalized and rounded soil quality score
     */
    @Override
    public double getSoilQuality() {
        double qualityScore = (this.getNitrogen() * NITROGEN_COEFFICIENT)
                + (this.getOrganicMatter() * ORGANIC_MATTER_COEFFICIENT)
                - (permafrostDepth * PERMAFROST_PENALTY);
        return this.roundScore(this.normalizeScore(qualityScore));
    }

    /**
     * Computes the trap value of this tundra soil.
     *
     * @return trap value as a percentage
     */
    @Override
    public double getTrap() {
        return (MAX_TRAP_DEPTH - permafrostDepth) / MAX_TRAP_DEPTH * PERCENT_FACTOR;
    }

    /**
     * @return the permafrost depth, rounded to two decimals
     */
    public double getPermafrostDepth() {
        return this.roundScore(permafrostDepth);
    }

    /**
     * Adds tundra-specific traits to the JSON representation.
     *
     * @param objNode the JSON node to augment
     */
    @Override
    public void addSpecificTrait(final ObjectNode objNode) {
        objNode.put("permafrostDepth", this.getPermafrostDepth());
    }
}
