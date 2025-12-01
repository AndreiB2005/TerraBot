package entities;

import fileio.WaterInput;

/**
 * Represents a body of water with several physical and chemical attributes
 * such as salinity, pH, purity, turbidity, and contamination levels.
 * Water quality is computed based on these properties.
 */
public class Water extends Entity {

    /** Constants replacing magic numbers */
    private static final double ONE_HUNDRED = 100d;
    private static final double IDEAL_PH = 7.5d;
    private static final double MAX_SALINITY = 350d;

    private static final double PURITY_WEIGHT = 0.3d;
    private static final double PH_WEIGHT = 0.2d;
    private static final double SALINITY_WEIGHT = 0.15d;
    private static final double TURBIDITY_WEIGHT = 0.1d;
    private static final double CONTAMINANT_WEIGHT = 0.15d;
    private static final double FROZEN_WEIGHT = 0.2d;

    /** The water type (e.g., freshwater, saltwater). */
    private final String type;

    /** Salinity level expressed in ppt (parts per thousand). */
    private final double salinity;

    /** The pH value of the water. */
    private final double pH;

    /** Purity level (percentage). */
    private final double purity;

    /** Turbidity level (percentage). */
    private final double turbidity;

    /** Contaminant index (percentage). */
    private final double contaminantIndex;

    /** Indicates whether the water is frozen. */
    private final boolean isFrozen;

    /** Timestamp used for tracking scanning operations. */
    private int scanTimestamp = 1;

    /**
     * Constructs a water entity based on input data.
     *
     * @param waterInput the input object containing the water properties
     */
    public Water(final WaterInput waterInput) {
        super(waterInput.getName(), waterInput.getMass());
        type = waterInput.getType();
        salinity = waterInput.getSalinity();
        pH = waterInput.getPH();
        purity = waterInput.getPurity();
        turbidity = waterInput.getTurbidity();
        contaminantIndex = waterInput.getContaminantIndex();
        isFrozen = waterInput.isFrozen();
    }

    /**
     * Returns the water type.
     *
     * @return the water type as a string
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * Returns the scan timestamp value.
     *
     * @return scan timestamp
     */
    public int getScanTimestamp() {
        return scanTimestamp;
    }

    /** Increments the scan timestamp counter. */
    public void incScanTimestamp() {
        scanTimestamp++;
    }

    /**
     * Computes the overall water quality score (0–100),
     * based on purity, pH, salinity, turbidity, contaminants, and freeze state.
     *
     * @return computed water quality score
     */
    public double getWaterQuality() {

        double purityScore = purity / ONE_HUNDRED;
        double phScore = 1 - (Math.abs(pH - IDEAL_PH) / IDEAL_PH);
        double salinityScore = 1 - (salinity / MAX_SALINITY);
        double turbidityScore = 1 - (turbidity / ONE_HUNDRED);
        double contaminantScore = 1 - (contaminantIndex / ONE_HUNDRED);
        double frozenScore = (isFrozen) ? 0d : 1d;

        return (PURITY_WEIGHT * purityScore
                + PH_WEIGHT * phScore
                + SALINITY_WEIGHT * salinityScore
                + TURBIDITY_WEIGHT * turbidityScore
                + CONTAMINANT_WEIGHT * contaminantScore
                + FROZEN_WEIGHT * frozenScore) * ONE_HUNDRED;
    }

    /**
     * Returns a description used when scanning this water entity.
     *
     * @return a water-specific scan message
     */
    @Override
    public String getScanResult() {
        return "The scanned object is water.";
    }
}
