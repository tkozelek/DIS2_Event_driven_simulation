package kozelek.event;

import kozelek.simulation.SimulationCore;

public abstract class Event implements Comparable<Event> {
    public final SimulationCore core;
    public final Double time;

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
}
