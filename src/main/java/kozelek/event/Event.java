package kozelek.event;

import kozelek.simulation.SimulationCore;

public abstract class Event implements Comparable<Event> {
    protected final SimulationCore core;
    protected final Double time;

    public Event(SimulationCore simulationCore, double time) {
        this.time = time;
        this.core = simulationCore;

    }

    public abstract void execute();


    @Override
    public int compareTo(Event other) {
        if (this.time.equals(other.time)) {
            return 0;
        }

        if (other.time.equals(0.0)) {
            return 1;
        }

        return Double.compare(this.time, other.time);
    }

    public SimulationCore getSimulationCore() {
        return core;
    }

    public double getTime() {
        return time;
    }

    // 100_000 / pracovny den
    public int getDays() {
        return (int) (time / (60 * 60 * 8));
    }

    // 100_000 % 8 hodin / hodina
    public int getHours() {
        return (int) ((time % (60 * 60 * 8)) / (60 * 60)) + 6;
    }

    // 100_000 % hodina / minuta
    public int getMinutes() {
        return (int) ((time % (60 * 60)) / 60);
    }

    public int getSeconds() {
        return (int) (time % 60);
    }

    public String timeToString() {
        return String.format("%d, %d:%d:%d", getDays(), getHours(), getMinutes(), getSeconds());
    }

}
