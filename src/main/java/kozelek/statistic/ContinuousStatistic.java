package kozelek.statistic;

import java.util.ArrayList;

public class ContinuousStatistic extends Statistic {
    private final ArrayList<Double> values = new ArrayList<>();
    private final ArrayList<Double> times = new ArrayList<>();

    public ContinuousStatistic(String name) {
        super(name);
    }

    @Override
    public double getMean() {
        if (times.size() <= 1) {
            return 0.0;
        }

        double numerator = 0.0;
        for (int i = 0; i < times.size() - 1; i++) {
            double curValue = values.get(i);
            double timesDiff = times.get(i + 1) - times.get(i);
            numerator += timesDiff * curValue;
        }

        return numerator / times.getLast();
    }

    public void addValue(double time, double value) {
        times.add(time);
        values.add(value);
    }

    public void clear() {
        values.clear();
        times.clear();
    }

    // https://www.geeksforgeeks.org/standard-deviation-formula/
    public double getStandardDeviation(double mean) {
        int n = values.size();
        if (n < 2) {
            return 0.0;
        }

        double sumSquaredDiffs = 0.0;

        for (double value : values) {
            sumSquaredDiffs += Math.pow(value - mean, 2);
        }

        return Math.sqrt(sumSquaredDiffs / (n - 1));
    }

    @Override
    public double[] getConfidenceInterval() {
        int n = values.size();
        if (n < 30) {
            return new double[]{0.0, 0.0};
        }

        double mean = getMean();
        double stdDev = getStandardDeviation(mean);
        double zValue = 1.96;

        double marginOfError = zValue * (stdDev / Math.sqrt(n));

        return new double[]{mean - marginOfError, mean + marginOfError};
    }
}
