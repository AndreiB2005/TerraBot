package entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fileio.SoilInput;

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

    public Soil(SoilInput soilInput) {
        super(soilInput.getName(), soilInput.getMass());
        type = SoilType.valueOf(soilInput.getType());
        nitrogen = soilInput.getNitrogen();
        waterRetention = soilInput.getWaterRetention();
        soilpH = soilInput.getSoilpH();
        organicMatter = soilInput.getOrganicMatter();
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

    public void setWaterRetention(double waterRetention) {
        this.waterRetention = waterRetention;
    }

    public void setOrganicMatter(double organicMatter) {
        this.organicMatter = organicMatter;
    }

    public double normalizeScore(double score) {
        return Math.max(0, Math.min(score, 100));
    }

    public double roundScore(double score) {
        return (Math.round(score * 100) / 100d);
    }

    public static Soil createSoil(SoilInput soilInput) {
        return switch (soilInput.getType()) {
            case "ForestSoil" -> new ForestSoil(soilInput);
            case "SwampSoil" -> new SwampSoil(soilInput);
            case "DesertSoil" -> new DesertSoil(soilInput);
            case "GrasslandSoil" -> new GrasslandSoil(soilInput);
            case "TundraSoil" -> new TundraSoil(soilInput);
            default -> throw new IllegalArgumentException();
        };
    }

    public abstract double getTrap();

    public abstract double getSoilQuality();

    public ObjectNode printSoil(ObjectMapper mapper) {
        ObjectNode soilNode = printEntity(mapper);
        soilNode.put("nitrogen", nitrogen);
        soilNode.put("waterRetention", Math.round(waterRetention * 100) / 100d);
        soilNode.put("soilpH", soilpH);
        soilNode.put("organicMatter", organicMatter);
        soilNode.put("soilQuality", getSoilQuality());
        addSpecificTrait(soilNode);
        return soilNode;
    }

    public abstract void addSpecificTrait(ObjectNode objNode);
}
