package kozelek.gui.controller;

import kozelek.gui.interfaces.Observer;
import kozelek.gui.model.SimulationData;
import kozelek.gui.view.MainWindow;
import kozelek.gui.model.SimulationManager;

import javax.swing.*;

public class MainController implements Observer {
    private MainWindow view;
    private SimulationManager simulationManager;
    private int replicationCount;
    private int[] groups;

    public MainController(MainWindow view) {
        this.view = view;
        this.simulationManager = new SimulationManager(this);
        this.view.setVisible(true);

        this.view.getStartButton().addActionListener(_ -> startSimulation());
        this.view.getPauseButton().addActionListener(_ -> pauseSimulation());
        this.view.getStopButton().addActionListener(_ -> stopSimulation());

        this.view.getSliderSpeed().addChangeListener(_ -> this.changeSpeed());
    }

    private void changeSpeed() {
        int speed = view.getSliderSpeed().getValue();
        this.view.getLabelSpeed().setText(String.valueOf(speed));
        this.simulationManager.setSpeed(speed);
    }

    private void stopSimulation() {
        this.simulationManager.stopSimulation();
    }

    private void pauseSimulation() {
        this.simulationManager.pauseSimulation();
    }

    public void startSimulation() {
        if (!validateInput())
            return;
        simulationManager.startSimulation(replicationCount, groups);
        this.changeSpeed();
    }

    private boolean validateInput() {
        if (!view.getFieldReplicationCount().getText().isBlank()
                && !view.getFieldWorkerA().getText().isBlank()
                && !view.getFieldWorkerB().getText().isBlank()
                && !view.getFieldWorkerC().getText().isBlank()) {
            replicationCount = Integer.parseInt(view.getFieldReplicationCount().getText());
            groups = new int[]{
                    Integer.parseInt(view.getFieldWorkerA().getText()),
                    Integer.parseInt(view.getFieldWorkerB().getText()),
                    Integer.parseInt(view.getFieldWorkerC().getText())
            };
            return true;
        } else {
            showError("Fields cannot be empty");
            return false;
        }
    }

    @Override
    public void update(SimulationData data) {
        view.updateData(data);
    }

    @Override
    public void updateTime(double time) {
        view.updateTime(time);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(view, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
