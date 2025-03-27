package kozelek.statistic;

import java.util.LinkedList;

public class ContinuousStatistic extends Statistic {
    private double weightedSum = 0.0;
    private Double lastTime = null;
    private final LinkedList<Double> values = new LinkedList<>();
    private final LinkedList<Double> times = new LinkedList<>();

    public ContinuousStatistic(String name) {
        super(name);
    }

    @Override
    public double getMean() {
        if (times.size() <= 1) {
            return 0.0;
        }
        double totalTime = times.getLast() - times.getFirst();
        return totalTime > 0 ? weightedSum / totalTime : 0.0;
    }

    public void addValue(double time, double value) {
        if (!times.isEmpty() && lastTime != null) {
            double timeDelta = time - lastTime;
            if (timeDelta > 0) {
                weightedSum += values.getLast() * timeDelta;
            }
        }

        times.add(time);
        values.add(value);
        lastTime = time;
    }

    public void clear() {
        values.clear();
        times.clear();
        weightedSum = 0.0;
        lastTime = null;
    }
}
