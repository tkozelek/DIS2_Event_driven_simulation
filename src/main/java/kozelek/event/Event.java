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
    public static int getDays(double time) {
        return (int) (time / (60 * 60 * 8));
    }

    // 100_000 % 8 hodin / hodina
    public static int getHours(double time) {
        return (int) ((time % (60 * 60 * 8)) / (60 * 60)) + 6;
    }

    // 100_000 % hodina / minuta
    public static int getMinutes(double time) {
        return (int) ((time % (60 * 60)) / 60);
    }

    public static int getSeconds(double time) {
        return (int) (time % 60);
    }

    public static String timeToString(double time) {
        if (time == 0.0) {
            return "-";
        }
        return String.format("%d, %02d:%02d:%02d", Event.getDays(time), Event.getHours(time), Event.getMinutes(time), Event.getSeconds(time));
    }

}
