package entities;

import fileio.WaterInput;

public class Water extends Entity {
    private final String type;
    private final double salinity;
    private final double pH;
    private final double purity;
    private final double turbidity;
    private final double contaminantIndex;
    private final boolean isFrozen;
    private int scanTimestamp = 1;

    public Water(WaterInput waterInput) {
        super(waterInput.getName(), waterInput.getMass());
        type = waterInput.getType();
        salinity = waterInput.getSalinity();
        pH = waterInput.getPH();
        purity = waterInput.getPurity();
        turbidity = waterInput.getTurbidity();
        contaminantIndex = waterInput.getContaminantIndex();
        isFrozen = waterInput.isFrozen();
    }

    public String getType() {
        return type;
    }

    public int getScanTimestamp() {
        return scanTimestamp;
    }

    public void incScanTimestamp() {
        scanTimestamp++;
    }

    public double getWaterQuality() {
        double purityScore = purity / 100;
        double phScore = 1 - (Math.abs(pH - 7.5) / 7.5);
        double salinityScore = 1 - (salinity / 350);
        double turbidityScore = 1 - (turbidity / 100);
        double contaminantScore = 1 - (contaminantIndex / 100);
        double frozenScore = (isFrozen) ? 0d : 1d;
        return (0.3 * purityScore +
                0.2 * phScore +
                0.15 * salinityScore +
                0.1 * turbidityScore +
                0.15 * contaminantScore +
                0.2 * frozenScore) * 100;
    }

    public String getScanResult() {
        return "The scanned object is water.";
    }
}
