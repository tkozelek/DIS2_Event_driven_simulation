package kozelek.statistic;

public class DiscreteStatistic extends Statistic {
    private double sum;
    private double sumSquared;

    public DiscreteStatistic(String name) {
        super(name);
    }

    public void addValue(double value) {
        sum += value;
        sumSquared += value * value;
        addCount();
        minMax(value);
    }

    @Override
    public double getMean() {
        return getCount() == 0 ? 0.0 : sum / getCount();
    }

    public double getVariance() {
        long count = getCount();
        if (count <= 1) return 0.0;

        double variance = (sumSquared - (sum * sum) / count) / (count - 1);

        return Math.max(variance, 0.0);
    }

    public double getStandardDeviation() {
        return Math.sqrt(getVariance());
    }

    @Override
    public double[] getConfidenceInterval() {
        long count = getCount();
        if (count <= 30) {
            return new double[]{0.0, 0.0};
        }

        double mean = getMean();
        double zScore = 1.96;

        double standardError = getStandardDeviation() * zScore / Math.sqrt(count);

        return new double[]{mean - standardError, mean + standardError};
    }

    public void clear() {
        sum = 0.0;
        sumSquared = 0.0;
        reset();
    }
}
