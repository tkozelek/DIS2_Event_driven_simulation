package kozelek.gui.view;

import kozelek.config.Constants;
import kozelek.entity.worker.Worker;
import kozelek.gui.model.SimulationData;
import kozelek.gui.view.talbemodel.OrderTable;
import kozelek.gui.view.talbemodel.WorkerTable;
import kozelek.gui.view.talbemodel.WorkerTotalTable;
import kozelek.gui.view.talbemodel.WorkstationTable;
import kozelek.event.Event;
import kozelek.statistic.DiscreteStatistic;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainWindow extends JFrame {
    private JPanel panel1;
    private JTextField fieldReplicationCount;
    private JTextField fieldWorkerA;
    private JTextField fieldWorkerB;
    private JTextField fieldWorkerC;
    private JButton startButton;
    private JButton pauseButton;
    private JButton stopButton;
    private JTable tableARep;
    private JTable tableBRep;
    private JTable tableCRep;
    private JLabel labelTime;
    private JLabel labelReplication;
    private JTable tableOrder;
    private JTable tableWorkstation;
    private JSlider sliderSpeed;
    private JLabel labelSpeed;
    private JLabel labelA;
    private JLabel labelB;
    private JLabel labelC;
    private JLabel labelAverageTimeInSystem;
    private JLabel labelAverageTimeInSystemTotal;
    private JTabbedPane tabbedPanel1;
    private JTable tableATotal;
    private JTabbedPane tabbedPanel2;
    private JTabbedPane tabbedPanel3;
    private JTable tableBTotal;
    private JTable tableCTotal;

    private WorkerTable workerTableARep, workerTableBRep, workerTableCRep;
    private WorkerTotalTable workerTableATotal, workerTableBTotal, workerTableCTotal;
    private OrderTable orderTable;
    private WorkstationTable workstationTable;

    public MainWindow() {
        setTitle("Diskretna simulacia");
        setMinimumSize(new Dimension(1600, 900));
        setSize(1800, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.add(panel1);

        this.sliderSpeed.setMaximum(Constants.MAX_SPEED);
        this.sliderSpeed.setValue(Constants.DEFAULT_SPEED);

        this.initTables();
    }

    private void initTables() {
        workerTableARep = new WorkerTable();
        workerTableBRep = new WorkerTable();
        workerTableCRep = new WorkerTable();

        workerTableATotal = new WorkerTotalTable();
        workerTableBTotal = new WorkerTotalTable();
        workerTableCTotal = new WorkerTotalTable();

        orderTable = new OrderTable();
        workstationTable = new WorkstationTable();

        tableARep.setModel(workerTableARep);
        tableBRep.setModel(workerTableBRep);
        tableCRep.setModel(workerTableCRep);
        tableATotal.setModel(workerTableATotal);
        tableBTotal.setModel(workerTableBTotal);
        tableCTotal.setModel(workerTableCTotal);

        tableOrder.setModel(orderTable);
        tableWorkstation.setModel(workstationTable);
    }

    public void updateData(SimulationData simData) {
        updateWorkers(simData);
        updateRest(simData);
        updateQueueSize(simData);
        updateAverageTimeInSystem(simData);
    }

    private void updateAverageTimeInSystem(SimulationData simData) {
        if (simData.orderTimeInSystem() != null && simData.orderTimeInSystem()[0] != null) {
            this.labelAverageTimeInSystem.setText(Event.timeToString(simData.orderTimeInSystem()[0].getMean()));
        }
        if (simData.orderTimeInSystem() != null && simData.orderTimeInSystem()[1] != null) {
            this.labelAverageTimeInSystemTotal.setText(Event.timeToString(simData.orderTimeInSystem()[1].getMean()));
        }
    }

    private void updateQueueSize(SimulationData simData) {
        if (simData.queues() != null) {
            this.labelA.setText(String.format("Group A - %d", simData.queues()[0]));
            this.labelB.setText(String.format("Group B - %d", simData.queues()[1]));
            this.labelC.setText(String.format("Group C - %d", simData.queues()[2]));
        }
    }

    public void updateWorkers(SimulationData simData) {
        if (simData.workerWorkloadTotal() != null) {
            DiscreteStatistic[][] stats = simData.workerWorkloadTotal();
            for (int i = 0; i < stats.length; i++) {
                switch (i) {
                    case 0 -> workerTableATotal.addRows(List.of(stats[i]));
                    case 1 -> workerTableBTotal.addRows(List.of(stats[i]));
                    case 2 -> workerTableCTotal.addRows(List.of(stats[i]));
                }
            }
        }

        if (simData.workers() == null)
            return;

        Worker[][] workers = simData.workers();
        for (int i = 0; i < workers.length; i++) {
            switch (i) {
                case 0 -> workerTableARep.addRows(List.of(workers[i]));
                case 1 -> workerTableBRep.addRows(List.of(workers[i]));
                case 2 -> workerTableCRep.addRows(List.of(workers[i]));
            }
        }
    }

    public void updateRest(SimulationData simData) {
        if (simData.workstations() != null)
            workstationTable.addRows(simData.workstations());
        if (simData.orders() != null)
            orderTable.addRows(simData.orders());
        labelReplication.setText(simData.currentReplication() + "");
    }

    public void updateTime(double time) {
        this.labelTime.setText(Event.timeToString(time));
    }

    public JPanel getPanel1() {
        return panel1;
    }

    public JTextField getFieldReplicationCount() {
        return fieldReplicationCount;
    }

    public JTextField getFieldWorkerA() {
        return fieldWorkerA;
    }

    public JTextField getFieldWorkerB() {
        return fieldWorkerB;
    }

    public JTextField getFieldWorkerC() {
        return fieldWorkerC;
    }

    public JButton getStartButton() {
        return startButton;
    }

    public JButton getPauseButton() {
        return pauseButton;
    }

    public JButton getStopButton() {
        return stopButton;
    }

    public JTable getTable4() {
        return tableOrder;
    }

    public JTable getTable5() {
        return tableWorkstation;
    }

    public JLabel getLabelSpeed() {
        return labelSpeed;
    }

    public WorkerTable getWorkerTable1() {
        return workerTableARep;
    }

    public WorkerTable getWorkerTable2() {
        return workerTableBRep;
    }

    public WorkerTable getWorkerTable3() {
        return workerTableCRep;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public WorkstationTable getWorkstationTable() {
        return workstationTable;
    }

    public JTable getTable1() {
        return tableARep;
    }

    public JTable getTable2() {
        return tableBRep;
    }

    public JTable getTable3() {
        return tableCRep;
    }

    public JLabel getLabelTime() {
        return labelTime;
    }

    public JLabel getLabelReplication() {
        return labelReplication;
    }

    public JSlider getSliderSpeed() {
        return sliderSpeed;
    }
}
