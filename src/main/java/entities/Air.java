package entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fileio.AirInput;
import fileio.CommandInput;

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
    public double currQuality;
    private int weatherTimestamp = 0;

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

    public Air(AirInput airInput) {
        super(airInput.getName(), airInput.getMass());
        type = AirType.valueOf(airInput.getType());
        humidity = airInput.getHumidity();
        temperature = airInput.getTemperature();
        oxygenLevel = airInput.getOxygenLevel();
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

    public double getCurrQuality() {
        return currQuality;
    }

    public int getWeatherTimestamp() {
        return weatherTimestamp;
    }

    public void setCurrQuality(double currQuality) {
        this.currQuality = currQuality;
    }

    public void setWeatherTimestamp(int weatherTimestamp) {
        this.weatherTimestamp = Math.max(0, weatherTimestamp);
    }

    public double normalizeScore(double score) {
        return Math.max(0, Math.min(score, 100));
    }

    public double roundScore(double score) {
        return (Math.round(score * 100) / 100d);
    }

    public static Air createAir(AirInput airInput) {
        return switch (airInput.getType()) {
            case "TropicalAir" -> new TropicalAir(airInput);
            case "PolarAir" -> new PolarAir(airInput);
            case "TemperateAir" -> new TemperateAir(airInput);
            case "DesertAir" -> new DesertAir(airInput);
            case "MountainAir" -> new MountainAir(airInput);
            default -> throw new IllegalArgumentException();
        };
    }

    public abstract double calculateAirQuality();

    public double getToxicity() {
        double toxicityAQ = 100 * (1 - this.calculateAirQuality() / type.getMaxScore());
        return (Math.round(toxicityAQ * 100) / 100d);
    }

    public ObjectNode printAir(ObjectMapper mapper) {
        ObjectNode airNode = this.printEntity(mapper);
        airNode.put("humidity", getHumidity());
        airNode.put("temperature", getTemperature());
        airNode.put("oxygenLevel", getOxygenLevel());
        airNode.put("airQuality", currQuality);
        this.addSpecificTrait(airNode);
        return airNode;
    }

    public abstract void addSpecificTrait(ObjectNode objNode);

    public abstract double checkWeather(CommandInput cmdInput);
}
