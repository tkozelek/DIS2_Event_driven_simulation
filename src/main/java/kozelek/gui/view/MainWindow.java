package kozelek.gui.view;

import kozelek.entity.Workstation;
import kozelek.entity.worker.Worker;
import kozelek.gui.model.SimulationData;
import kozelek.gui.view.talbemodel.OrderTable;
import kozelek.gui.view.talbemodel.WorkerTable;
import kozelek.gui.view.talbemodel.WorkstationTable;
import kozelek.event.Event;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
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
    private JTable table1;
    private JTable table2;
    private JTable table3;
    private JLabel labelTime;
    private JLabel labelReplication;
    private JTable table4;
    private JTable table5;

    private WorkerTable workerTable1, workerTable2, workerTable3;
    private OrderTable orderTable;
    private WorkstationTable workstationTable;

    public MainWindow() {
        setTitle("Diskretna simulacia");
        setMinimumSize(new Dimension(1600, 900));
        setSize(1800, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.add(panel1);

        this.initTables();
    }

    private void initTables() {
        workerTable1 = new WorkerTable();
        workerTable2 = new WorkerTable();
        workerTable3 = new WorkerTable();
        orderTable = new OrderTable();
        workstationTable = new WorkstationTable();

        table1.setModel(workerTable1);
        table2.setModel(workerTable2);
        table3.setModel(workerTable3);
        table4.setModel(orderTable);
        table5.setModel(workstationTable);
    }

    public void updateData(SimulationData simData) {
        Worker[][] workers = simData.workers();
        for (int i = 0; i < workers.length; i++) {
            switch (i) {
                case 0 -> workerTable1.addRows(List.of(workers[i]));
                case 1 -> workerTable2.addRows(List.of(workers[i]));
                case 2 -> workerTable3.addRows(List.of(workers[i]));
            }
        }
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

    public JTable getTable1() {
        return table1;
    }

    public JTable getTable2() {
        return table2;
    }

    public JTable getTable3() {
        return table3;
    }

    public JLabel getLabelTime() {
        return labelTime;
    }

    public JLabel getLabelReplication() {
        return labelReplication;
    }
}
