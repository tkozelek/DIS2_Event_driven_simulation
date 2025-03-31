package kozelek.gui.model;

import kozelek.gui.interfaces.Observer;
import kozelek.simulation.Simulation;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class SimulationManager {
    private Simulation simulation;
    private final Observer observer;
    private SwingWorker<Void, Void> worker;

    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    public SimulationManager(Observer observer) {
        this.observer = observer;
    }

    public void startSimulation(int replicationCount, int[] groups) {
        if (isRunning.get()) return;
        isRunning.set(true);

        this.simulation = new Simulation(replicationCount, null, groups);
        this.simulation.addObserver(observer);

        worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                simulation.simuluj();
                return null;
            }

            @Override
            protected void done() {
                isRunning.set(false);
                System.out.println("Simulation finished!");
            }
        };
        worker.execute();
    }

    public void stopSimulation() {
        if (simulation != null) {
            simulation.stopSimulation();
        }
        if (worker != null) {
            worker.cancel(true);
        }
    }

    public void pauseSimulation() {
        if (simulation != null) {
            simulation.togglePauseSimulation();
        }
    }

    public void setSpeed(int speed) {
        if (simulation != null) {
            simulation.setSpeed(speed);
        }
    }
}
