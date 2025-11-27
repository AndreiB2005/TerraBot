package entities.soilTypes;

import com.fasterxml.jackson.databind.node.ObjectNode;

import entities.Soil;

public class GrasslandSoil extends Soil {
    private final double rootDensity;

    public GrasslandSoil(String name, double mass, String type, double nitrogen,
                         double waterRetention, double soilpH, double organicMatter,
                         double rootDensity) {
        super(name, mass, type, nitrogen, waterRetention, soilpH, organicMatter);
        this.rootDensity = rootDensity;
    }

    public double getSoilQuality() {
        double qualityScore = (this.getNitrogen() * 1.3) +
                (this.getOrganicMatter() * 1.5) +
                (rootDensity * 0.8);
        return this.roundScore(this.normalizeScore(qualityScore));
    }

    public double getTrap() {
        return ((50 - rootDensity) + this.getWaterRetention() * 0.5) / 75 * 100;
    }

    public double getRootDensity() {
        return this.roundScore(rootDensity);
    }

    public void addSpecificTrait(ObjectNode objNode) {
        objNode.put("rootDensity", this.getRootDensity());
    }
}
