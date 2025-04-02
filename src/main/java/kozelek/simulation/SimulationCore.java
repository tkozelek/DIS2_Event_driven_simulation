package kozelek.simulation;

import kozelek.config.Constants;
import kozelek.event.Event;

import java.util.PriorityQueue;


public abstract class SimulationCore {
    protected final int numberOfReps;
    private final PriorityQueue<Event> eventCalendar;
    protected volatile boolean stopped = false;
    private int speed = 10;
    private double currentTime;
    private volatile boolean paused = false;
    private int currentRep = 1;


    public SimulationCore(int numberOfReps) {
        this.numberOfReps = numberOfReps;
        this.eventCalendar = new PriorityQueue<>(20);
    }

    public int getNumberOfReps() {
        return numberOfReps;
    }

    public void simuluj() {
        this.beforeReplications();
        for (currentRep = 1; currentRep <= this.numberOfReps; currentRep++) {
            if (stopped)
                break;
            this.beforeReplication();
            this.replication();
            this.afterReplication();
        }
        this.afterReplications();
    }

    protected void executeEvent() {
        while (paused && !stopped) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        if (stopped) return;

        Event event = this.eventCalendar.poll();
        if (event == null)
            throw new IllegalStateException("[Sim.Core] Event is null!");


        double time = event.getTime();

        if (time < this.currentTime)
            throw new IllegalStateException("[Sim.Core] Event time is less than current time!");

        if (time > Constants.SIMULATION_TIME)
            throw new IllegalStateException("[Sim.Core] Event time is greater than total simulation time!");

        currentTime = time;

        event.execute();
    }

    public int getCurrentRep() {
        return this.currentRep;
    }

    public void addEvent(Event event) {
        this.eventCalendar.add(event);
    }

    public boolean isEventCalendarEmpty() {
        return this.eventCalendar.isEmpty();
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void togglePauseSimulation() {
        paused = !paused;
    }

    public void stopSimulation() {
        stopped = true;
    }

    public void resetTime() {
        this.currentTime = 0.0;
    }

    public double getCurrentTime() {
        return currentTime;
    }

    public void updateTime() {

    }

    public abstract void replication();

    public abstract void beforeReplications();

    public abstract void beforeReplication();

    public abstract void afterReplication();

    public abstract void afterReplications();
}
