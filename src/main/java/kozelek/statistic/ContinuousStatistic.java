package kozelek.statistic;

import java.util.LinkedList;

public class ContinuousStatistic extends Statistic {

    private final LinkedList<Double> values = new LinkedList<>();
    private final LinkedList<Double> times = new LinkedList<>();

    private double weightedSum = 0.0;
    private double totalTime = 0.0;
    private double weightedSumOfSquares = 0.0;

    public ContinuousStatistic(String name) {
        super(name);
    }

    @Override
    public double getMean() {
        if (times.size() <= 1) {
            return 0.0;
        }

        return weightedSum / totalTime;
    }

    @Override
    public double getVariance() {
        if (times.size() <= 1) {
            return 0.0;
        }

        double mean = getMean();  // Priemer je potrebný na výpočet odchýlok

        double weightedSumOfSquaredDeviations = 0.0;
        for (int i = 0; i < times.size() - 1; i++) {
            double timeInterval = times.get(i + 1) - times.get(i);
            double deviation = values.get(i) - mean;
            weightedSumOfSquaredDeviations += timeInterval * Math.pow(deviation, 2);
        }

        return weightedSumOfSquaredDeviations / totalTime;
    }

    public void addValue(double time, double value) {
        if (!times.isEmpty()) {
            double prevTime = times.getLast();
            double timeInterval = time - prevTime;

            weightedSum += timeInterval * value;
            weightedSumOfSquares += timeInterval * Math.pow(value, 2);
            totalTime += timeInterval;
        }

        times.add(time);
        values.add(value);
    }

    public void clear() {
        values.clear();
        times.clear();
        weightedSum = 0.0;
        weightedSumOfSquares = 0.0;
        totalTime = 0.0;
    }
}
