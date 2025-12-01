package entities.soilTypes;

import com.fasterxml.jackson.databind.node.ObjectNode;

import fileio.SoilInput;

import entities.Soil;

/**
 * Represents a grassland-type soil entity with specific traits such as root density.
 * Soil quality is influenced by nitrogen content, organic matter, and root density.
 */
public class GrasslandSoil extends Soil {

    private static final double NITROGEN_COEFFICIENT = 1.3;
    private static final double ORGANIC_MATTER_COEFFICIENT = 1.5;
    private static final double ROOT_DENSITY_COEFFICIENT = 0.8;

    private static final double ROOT_TRAP_OFFSET = 50d;
    private static final double WATER_RETENTION_COEFFICIENT = 0.5;
    private static final double TRAP_MAX = 75d;
    private static final double PERCENT_MAX = 100d;

    /** Density of roots present in the soil. */
    private final double rootDensity;

    /**
     * Constructs a GrasslandSoil instance based on input data.
     *
     * @param soilInput input data containing soil properties
     */
    public GrasslandSoil(final SoilInput soilInput) {
        super(soilInput);
        rootDensity = soilInput.getRootDensity();
    }

    /**
     * Calculates the quality of the grassland soil based on nitrogen, organic matter,
     * and root density.
     *
     * @return normalized and rounded soil quality score
     */
    @Override
    public double getSoilQuality() {
        double qualityScore = (this.getNitrogen() * NITROGEN_COEFFICIENT)
                + (this.getOrganicMatter() * ORGANIC_MATTER_COEFFICIENT)
                + (rootDensity * ROOT_DENSITY_COEFFICIENT);
        return this.roundScore(this.normalizeScore(qualityScore));
    }

    /**
     * Computes the trap value of this grassland soil.
     *
     * @return trap value as a percentage
     */
    @Override
    public double getTrap() {
        return ((ROOT_TRAP_OFFSET - rootDensity)
                + this.getWaterRetention() * WATER_RETENTION_COEFFICIENT)
                / TRAP_MAX * PERCENT_MAX;
    }

    /**
     * @return the root density, rounded to two decimals
     */
    public double getRootDensity() {
        return this.roundScore(rootDensity);
    }

    /**
     * Adds grassland-specific traits to the JSON representation.
     *
     * @param objNode the JSON node to augment
     */
    @Override
    public void addSpecificTrait(final ObjectNode objNode) {
        objNode.put("rootDensity", this.getRootDensity());
    }
}
