package entities.soilTypes;

import com.fasterxml.jackson.databind.node.ObjectNode;

import fileio.SoilInput;

import entities.Soil;

public class DesertSoil extends Soil {
    private final double salinity;

    public DesertSoil(SoilInput soilInput) {
        super(soilInput);
        salinity = soilInput.getSalinity();
    }

    public double getSoilQuality() {
        double qualityScore = (this.getNitrogen() * 0.5) +
                (this.getWaterRetention() * 0.3) +
                (salinity * 2);
        return this.roundScore(this.normalizeScore(qualityScore));
    }

    public double getTrap() {
        return (100 - this.getWaterRetention() + salinity) / 100d * 100;
    }

    public double getSalinity() {
        return this.roundScore(salinity);
    }

    public void addSpecificTrait(ObjectNode objNode) {
        objNode.put("salinity", this.getSalinity());
    }
}
