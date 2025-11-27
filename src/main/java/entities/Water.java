package entities;

public class Water extends Entity {
    private final String type;
    private final double salinity;
    private final double pH;
    private final double purity;
    private final double turbidity;
    private final double contaminantIndex;
    private final boolean isFrozen;

    public Water(String name, double mass, String type, double salinity,
                 double pH, double purity, double turbidity,
                 double contaminantIndex, boolean isFrozen) {
        super(name, mass);
        this.type = type;
        this.salinity = salinity;
        this.pH = pH;
        this.purity = purity;
        this.turbidity = turbidity;
        this.contaminantIndex = contaminantIndex;
        this.isFrozen = isFrozen;
    }

    public String getType() {
        return type;
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

}
