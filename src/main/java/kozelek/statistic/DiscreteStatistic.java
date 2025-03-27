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

    public double[] getConfidenceInterval() {
        long count = getCount();
        if (count <= 30) {
            return new double[]{getMean(), getMean()};
        }

        double mean = getMean();
        double standardError = getStandardDeviation() / Math.sqrt(count);

        double zScore = 1.960;

        double marginOfError = zScore * standardError;
        return new double[]{mean - marginOfError, mean + marginOfError};
    }

    public void clear() {
        sum = 0.0;
        sumSquared = 0.0;
        reset();
    }
}
