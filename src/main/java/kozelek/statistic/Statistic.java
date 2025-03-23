package kozelek.statistic;

import java.util.ArrayList;

abstract class Statistic {
    private final String name;

    public Statistic(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract double getMean();
    public abstract double getVariance();
}
