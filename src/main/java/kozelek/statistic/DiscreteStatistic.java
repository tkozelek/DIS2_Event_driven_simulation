package kozelek.statistic;

import java.util.ArrayList;

class DiscreteStatistic extends Statistic {

    private double sum;
    private double sumOfSquares;
    private int count;

    public DiscreteStatistic(String name) {
        super(name);
        this.sum = 0.0;
        this.sumOfSquares = 0.0;
        this.count = 0;
    }

    public void addValue(double value) {
        sum += value;
        sumOfSquares += Math.pow(value, 2);
        count++;
    }

    @Override
    public double getMean() {
        return count == 0 ? 0.0 : sum / count;
    }

    @Override
    public double getVariance() {
        if (count == 0) return 0.0;

        return (sumOfSquares / count) - Math.pow(getMean(), 2);
    }

}
