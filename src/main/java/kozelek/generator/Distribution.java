package kozelek.generator;

public class Distribution {
    private final double min;
    private final double max;
    private final double probability;

    public Distribution(double min, double max, double probability) {
        this.min = min;
        this.max = max;
        this.probability = probability;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getProbability() {
        return probability;
    }
}
