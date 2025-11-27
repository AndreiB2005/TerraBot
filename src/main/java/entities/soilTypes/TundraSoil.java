package entities.soilTypes;

import com.fasterxml.jackson.databind.node.ObjectNode;

import entities.Soil;

public class TundraSoil extends Soil {
    private final double permafrostDepth;

    public TundraSoil(String name, double mass, String type, double nitrogen,
                      double waterRetention, double soilpH, double organicMatter,
                      double permafrostDepth) {
        super(name, mass, type, nitrogen, waterRetention, soilpH, organicMatter);
        this.permafrostDepth = permafrostDepth;
    }

    public double getSoilQuality() {
        double qualityScore = (this.getNitrogen() * 0.7) +
                (this.getOrganicMatter() * 0.5) -
                (permafrostDepth * 1.5);
        return this.roundScore(this.normalizeScore(qualityScore));
    }

    public double getTrap() {
        return (50 - permafrostDepth) * 50 / 100;
    }

    public double getPermafrostDepth() {
        return this.roundScore(permafrostDepth);
    }

    public void addSpecificTrait(ObjectNode objNode) {
        objNode.put("permafrostDepth", this.getPermafrostDepth());
    }
}
