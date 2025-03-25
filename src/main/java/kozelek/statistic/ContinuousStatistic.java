package kozelek.statistic;

import java.util.LinkedList;

public class ContinuousStatistic extends Statistic {
    private double weightedSum = 0.0;  // Stores sum of (value * timeDelta)
    private double lastTime = 0.0;     // Stores last added time
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
        return weightedSum / (times.getLast() - times.getFirst());
    }

    public void addValue(double time, double value) {
        if (!times.isEmpty()) {
            double timeDelta = time - lastTime;
            weightedSum += values.getLast() * timeDelta;
        }

        times.add(time);
        values.add(value);
        lastTime = time;
    }

    public void clear() {
        values.clear();
        times.clear();
        weightedSum = 0.0;
        lastTime = 0.0;
    }
}
