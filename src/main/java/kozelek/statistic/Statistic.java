package kozelek.statistic;

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

    public abstract double[] getConfidenceInterval();

    @Override
    public String toString() {
        double[] is = getConfidenceInterval();
        return String.format("%s: %.4f <%.4f, %.4f>", name, getMean(), is[0], is[1]);
    }
}
