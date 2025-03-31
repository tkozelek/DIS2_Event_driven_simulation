package kozelek.statistic;

public abstract class Statistic {
    private final String name;
    protected long count;
    private double min, max;

    public Statistic(String name) {
        this.name = name;
        this.count = 0;
        this.min = Double.MAX_VALUE;
        this.max = Double.MIN_VALUE;
    }

    public void minMax(double value) {
        if (value < min) {
            min = value;
        }
        if (value > max) {
            max = value;
        }
    }

    public String getName() {
        return name;
    }

    public long getCount() {
        return count;
    }

    public void addCount() {
        count++;
    }

    public void reset() {
        count = 0;
    }

    public abstract double getMean();

    public abstract double[] getConfidenceInterval();

    @Override
    public String toString() {
        double[] is = getConfidenceInterval();
        return String.format("%s: %.4f <%.4f, %.4f>, min: %.2f, max: %.2f", name, getMean(), is[0], is[1], min, max);
    }
}
