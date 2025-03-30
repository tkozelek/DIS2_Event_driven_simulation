package kozelek.gui.model;

import kozelek.gui.interfaces.Observer;
import kozelek.gui.model.SimulationData;
import kozelek.simulation.Simulation;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

public class SimulationManager {
    private Simulation simulation;
    private final Observer observer;

    public SimulationManager(Observer observer) {
        this.observer = observer;
    }

    public void startSimulation(int replicationCount, int[] groups) {
        this.simulation = new Simulation(replicationCount, null, groups);
        this.simulation.addObserver(observer);

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                simulation.simuluj();
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    System.out.println("Simulation finished!");
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        worker.execute();
    }

    public void pauseSimulation() {
        if (simulation != null) {
            simulation.togglePauseSimulation();
        }
    }

    public void stopSimulation() {
        if (simulation != null) {
            simulation.stopSimulation();
        }
    }

    public void setSpeed(int speed) {
        if (simulation != null) {
            simulation.setSpeed(speed);
        }
    }
}
