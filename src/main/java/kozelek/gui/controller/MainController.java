package kozelek.gui.controller;

import kozelek.gui.interfaces.Observable;
import kozelek.gui.interfaces.Observer;
import kozelek.gui.model.SimulationData;
import kozelek.gui.view.MainWindow;
import kozelek.simulation.Simulation;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

public class MainController implements Observer {
    private MainWindow view;
    private Simulation simulation;
    private int replicationCount;
    private int[] groups;

    public MainController(MainWindow view) {
        this.view = view;
        this.view.setVisible(true);

        this.view.getStartButton().addActionListener(_ ->  this.startSimulation());
        this.view.getPauseButton().addActionListener(_ ->  this.pauseSimulation());
        this.view.getStopButton().addActionListener(_ ->  this.stopSimulation());
    }

    private void stopSimulation() {
        this.simulation.stopSimulation();
    }

    private void pauseSimulation() {
        this.simulation.togglePauseSimulation();
    }

    public void startSimulation() {
        if (!this.validateInput())
            return;
        this.simulation = new Simulation(replicationCount, 5L, groups);
        this.simulation.setSpeed(200000);
        this.simulation.addObserver(this);
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
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

    private boolean validateInput() {
        if (!this.view.getFieldReplicationCount().getText().isBlank()
        && !this.view.getFieldWorkerA().getText().isBlank()
        && !this.view.getFieldWorkerB().getText().isBlank()
        && !this.view.getFieldWorkerC().getText().isBlank()) {
            this.replicationCount = Integer.parseInt(this.view.getFieldReplicationCount().getText());
            groups = new int[3];
            groups[0] = Integer.parseInt(this.view.getFieldWorkerA().getText());
            groups[1] = Integer.parseInt(this.view.getFieldWorkerB().getText());
            groups[2] = Integer.parseInt(this.view.getFieldWorkerC().getText());
            return true;
        } else {
            this.showError("Field replication count cannot be empty");
            return false;
        }
    }

    @Override
    public void update(SimulationData data) {
        this.view.updateData(data);
    }

    @Override
    public void updateTime(double time) {
        this.view.updateTime(time);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this.view, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
