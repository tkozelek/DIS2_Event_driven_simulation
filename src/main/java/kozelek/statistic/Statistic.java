package kozelek.statistic;

import java.util.ArrayList;
import java.util.List;

abstract class Statistic {
    private final String name;
    protected long count;

    public Statistic(String name) {
        this.name = name;
        this.count = 0;
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

}
