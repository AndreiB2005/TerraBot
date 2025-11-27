package entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import entities.soilTypes.ForestSoil;
import entities.soilTypes.SwampSoil;
import entities.soilTypes.DesertSoil;
import entities.soilTypes.GrasslandSoil;
import entities.soilTypes.TundraSoil;

public abstract class Soil extends Entity {
    private final SoilType type;
    private final double nitrogen;
    private double waterRetention;
    private final double soilpH;
    private double organicMatter;

    private enum SoilType{
        ForestSoil,
        SwampSoil,
        DesertSoil,
        GrasslandSoil,
        TundraSoil;
    }

    public Soil(String name, double mass, String type, double nitrogen,
                double waterRetention, double soilpH, double organicMatter) {
        super(name, mass);
        this.type = SoilType.valueOf(type);
        this.nitrogen = nitrogen;
        this.waterRetention = waterRetention;
        this.soilpH = soilpH;
        this.organicMatter = organicMatter;
    }

    public String getType() {
        return type.name();
    }

    public double getNitrogen() {
        return nitrogen;
    }

    public double getWaterRetention() {
        return waterRetention;
    }

    public double getSoilpH() {
        return soilpH;
    }

    public double getOrganicMatter() {
        return organicMatter;
    }

    public double normalizeScore(double score) {
        return Math.max(0, Math.min(score, 100));
    }

    public double roundScore(double score) {
        return (Math.round(score * 100) / 100d);
    }

    public static Soil createSoil(String name, double mass, String type, double nitrogen,
                           double waterRetention, double soilpH, double organicMatter,
                           double specificTrait) {
        return switch (type) {
            case "ForestSoil" -> new ForestSoil(name, mass, type, nitrogen, waterRetention,
                    soilpH, organicMatter, specificTrait);
            case "SwampSoil" -> new SwampSoil(name, mass, type, nitrogen, waterRetention,
                    soilpH, organicMatter, specificTrait);
            case "DesertSoil" -> new DesertSoil(name, mass, type, nitrogen, waterRetention,
                    soilpH, organicMatter, specificTrait);
            case "GrasslandSoil" -> new GrasslandSoil(name, mass, type, nitrogen, waterRetention,
                    soilpH, organicMatter, specificTrait);
            case "TundraSoil" -> new TundraSoil(name, mass, type, nitrogen, waterRetention,
                    soilpH, organicMatter, specificTrait);
            default -> throw new IllegalArgumentException();
        };
    }

    public abstract double getTrap();

    public abstract double getSoilQuality();

    public ObjectNode printSoil(ObjectMapper mapper) {
        ObjectNode soilNode = this.printEntity(mapper);
        soilNode.put("nitrogen", this.getNitrogen());
        soilNode.put("waterRetention", this.getWaterRetention());
        soilNode.put("soilpH", this.getSoilpH());
        soilNode.put("organicMatter", this.getOrganicMatter());
        soilNode.put("soilQuality", this.getSoilQuality());
        this.addSpecificTrait(soilNode);
        return soilNode;
    }

    public abstract void addSpecificTrait(ObjectNode objNode);
}
