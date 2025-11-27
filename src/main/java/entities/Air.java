package entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import entities.airTypes.TropicalAir;
import entities.airTypes.PolarAir;
import entities.airTypes.TemperateAir;
import entities.airTypes.DesertAir;
import entities.airTypes.MountainAir;

public abstract class Air extends Entity {
    private final AirType type;
    private double humidity;
    private final double temperature;
    private double oxygenLevel;

    private enum AirType {
        TropicalAir(82),
        PolarAir(142),
        TemperateAir(84),
        DesertAir(65),
        MountainAir(78);

        private final double maxScore;

        AirType(double maxScore) {
            this.maxScore = maxScore;
        }

        public double getMaxScore() {
            return maxScore;
        }
    }

    public Air(String name, double mass, String type, double humidity,
               double temperature, double oxygenLevel) {
        super(name, mass);
        this.type = AirType.valueOf(type);
        this.humidity = humidity;
        this.temperature = temperature;
        this.oxygenLevel = oxygenLevel;
    }

    public String getType() {
        return type.name();
    }

    public double getHumidity() {
        return humidity;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getOxygenLevel() {
        return oxygenLevel;
    }

    public double normalizeScore(double score) {
        return Math.max(0, Math.min(score, 100));
    }

    public double roundScore(double score) {
        return (Math.round(score * 100) / 100d);
    }

    public static Air createAir(String name, double mass, String type, double humidity,
                         double temperature, double oxygenLevel,
                         double specificVariable) {
        return switch (type) {
            case "TropicalAir" -> new TropicalAir(name, mass, type, humidity, temperature,
                    oxygenLevel, specificVariable);
            case "PolarAir" -> new PolarAir(name, mass, type, humidity, temperature,
                    oxygenLevel, specificVariable);
            case "TemperateAir" -> new TemperateAir(name, mass, type, humidity, temperature,
                    oxygenLevel, specificVariable);
            case "DesertAir" -> new DesertAir(name, mass, type, humidity, temperature,
                    oxygenLevel, specificVariable);
            case "MountainAir" -> new MountainAir(name, mass, type, humidity, temperature,
                    oxygenLevel, specificVariable);
            default -> throw new IllegalArgumentException();
        };
    }

    public abstract double getAirQuality();

    public double getToxicity() {
        double toxicityAQ = 100 * (1 - this.getAirQuality() / type.getMaxScore());
        return (Math.round(toxicityAQ * 100) / 100d);
    }

    public ObjectNode printAir(ObjectMapper mapper) {
        ObjectNode airNode = this.printEntity(mapper);
        airNode.put("humidity", this.getHumidity());
        airNode.put("temperature", this.getTemperature());
        airNode.put("oxygenLevel", this.getOxygenLevel());
        airNode.put("airQuality", this.getAirQuality());
        this.addSpecificTrait(airNode);
        return airNode;
    }

    public abstract void addSpecificTrait(ObjectNode objNode);
}
