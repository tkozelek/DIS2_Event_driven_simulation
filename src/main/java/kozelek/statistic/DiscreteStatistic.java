package kozelek.statistic;

public class DiscreteStatistic extends Statistic {
    private double sum;
    private double sumSquared;

    public DiscreteStatistic(String name) {
        super(name);
    }

    public void addValue(double value) {
        sum += value;
        sumSquared += value * value; // More efficient than Math.pow(value, 2)
        addCount();
    }

    @Override
    public double getMean() {
        return getCount() == 0 ? 0.0 : sum / getCount();
    }

    public double getVariance() {
        if (getCount() <= 1) return 0.0;
        double mean = getMean();
        return (sumSquared / getCount()) - (mean * mean);
    }

    public double getStandardDeviation() {
        return Math.sqrt(getVariance());
    }

    public void clear() {
        sum = 0.0;
        sumSquared = 0.0;
        reset();
    }
}
