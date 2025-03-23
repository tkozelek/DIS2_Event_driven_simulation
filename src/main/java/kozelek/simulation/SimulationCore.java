package kozelek.simulation;

import kozelek.event.Event;

import java.util.PriorityQueue;

public abstract class SimulationCore {
    private final PriorityQueue<Event> eventCalendar;
    protected final int numberOfReps;
    private int speed = 10;
    private double currentTime;
    private volatile boolean stopped = false;
    private volatile boolean paused = false;
    private int currentRep = 0;


    public SimulationCore(int numberOfReps) {
        this.numberOfReps = numberOfReps;
        this.eventCalendar = new PriorityQueue<>();


    }

    public int getNumberOfReps() {
        return numberOfReps;
    }

    public void simuluj() {
        this.beforeReplications();
        for (int i = 0; i < this.numberOfReps; i++) {
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
        if (event == null) {
            throw new IllegalStateException("Event is null!");
        }

        double time = event.getTime();

        if (time < this.currentTime) {
            throw new IllegalStateException("Time is less than current time!");
        }
        currentTime = time;

        event.execute();
    }

    public void addEvent(Event event) {
        this.eventCalendar.add(event);
    }

    public int getSpeed() {
        return speed;
    }

    public void pauseSimulation() {
        paused = true;
    }

    public void resumeSimulation() {
        paused = false;
    }

    public void stopSimulation() {
        stopped = true;
    }

    public abstract void replication();
    public abstract void beforeReplications();
    public abstract void beforeReplication();
    public abstract void afterReplication();
    public abstract void afterReplications();
}
