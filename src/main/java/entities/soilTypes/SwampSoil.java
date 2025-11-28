package entities.soilTypes;

import com.fasterxml.jackson.databind.node.ObjectNode;

import fileio.SoilInput;

import entities.Soil;

public class SwampSoil extends Soil {
    private final double waterLogging;

    public SwampSoil(SoilInput soilInput) {
        super(soilInput);
        waterLogging = soilInput.getWaterLogging();
    }

    public double getSoilQuality() {
        double qualityScore = (this.getNitrogen() * 1.1) +
                (this.getOrganicMatter() * 2.2) -
                (waterLogging * 5);
        return this.roundScore(this.normalizeScore(qualityScore));
    }

    public double getTrap() {
        return waterLogging * 10;
    }

    public double getWaterLogging() {
        return this.roundScore(waterLogging);
    }

    public void addSpecificTrait(ObjectNode objNode) {
        objNode.put("waterLogging", this.getWaterLogging());
    }
}
