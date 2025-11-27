package entities.soilTypes;

import com.fasterxml.jackson.databind.node.ObjectNode;

import entities.Soil;

public class ForestSoil extends Soil {
    private final double leafLitter;

    public ForestSoil(String name, double mass, String type, double nitrogen,
                      double waterRetention, double soilpH, double organicMatter,
                      double leafLitter) {
        super(name, mass, type, nitrogen, waterRetention, soilpH, organicMatter);
        this.leafLitter = leafLitter;
    }

    public double getSoilQuality() {
        double qualityScore = (this.getNitrogen() * 1.2) +
                (this.getOrganicMatter() * 2) +
                (this.getWaterRetention() * 1.5) +
                (leafLitter * 0.3);
        return this.roundScore(this.normalizeScore(qualityScore));
    }

    public double getTrap() {
        return ((this.getWaterRetention() * 0.6) + (leafLitter * 0.4)) / 80 * 100;
    }

    public double getLeafLitter() {
        return this.roundScore(leafLitter);
    }

    public void addSpecificTrait(ObjectNode objNode) {
        objNode.put("leafLitter", this.getLeafLitter());
    }
}
