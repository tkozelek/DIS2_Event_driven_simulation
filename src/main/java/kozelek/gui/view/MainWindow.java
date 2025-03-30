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
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import javax.swing.plaf.multi.MultiLabelUI;
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
    private JLabel labelOrderNotWorkedOn;
    private JTabbedPane mainTabbedPanel;
    private ChartPanel chartPanel1;
    private JLabel currentRepLabel;
    private JFreeChart chart1;
    private Chart chart;

    private WorkerTable workerTableARep, workerTableBRep, workerTableCRep;
    private WorkerTotalTable workerTableATotal, workerTableBTotal, workerTableCTotal;
    private OrderTable orderTable;
    private WorkstationTable workstationTable;

    public MainWindow() {
        setTitle("Diskretna simulacia");
        setMinimumSize(new Dimension(1800, 1000));
        setSize(1800, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.add(mainTabbedPanel);

        this.sliderSpeed.setMaximum(Constants.MAX_SPEED);
        this.sliderSpeed.setValue(Constants.DEFAULT_SPEED);

        Icon home = new ImageIcon("home.png");
        Icon graph = new ImageIcon("diagram.png");
        mainTabbedPanel.setIconAt(0, home);
        mainTabbedPanel.setIconAt(1, graph);

        this.initTables();
    }

    public void createUIComponents() {
        chart = new Chart("Average time", "Average time in system");
        chart1 = chart.getChart();

        chartPanel1 = new ChartPanel(chart1);
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
        if (getSpeed() < Constants.MAX_SPEED) {
            updateTime(simData);
            updateWorkersReplication(simData);
            updateWorkstationOrderTable(simData);
            updateAverageTimeInSystemReplication(simData);
        }
        updateQueueSize(simData);
        updateWorkersTotal(simData);
        updateAverageTimeInSystemTotal(simData);
        updateAverageCountOfNotWorkedOnOrder(simData);

        labelReplication.setText(simData.currentReplication() + "");
        currentRepLabel.setText(String.format("Replication: %d", simData.currentReplication()));
    }

    public void updateChart(SimulationData simData, int replicationCount) {
        SwingUtilities.invokeLater(() -> {
            if (simData.updateChart() && simData.currentReplication() >= (replicationCount * Constants.PERCENTAGE_CUT_DATA) &&
                    simData.currentReplication() % (replicationCount * Constants.PERCENTAGE_UPDATE_DATA) == 0) {
                XYSeriesCollection dataset = (XYSeriesCollection) chart1.getXYPlot().getDataset();

                XYSeries seriesMain = dataset.getSeries(0);
                XYSeries seriesBottom = dataset.getSeries(1);
                XYSeries seriesTop = dataset.getSeries(2);

                int rep = simData.currentReplication();
                DiscreteStatistic ds = simData.orderTimeInSystem()[1];
                double[] is = ds.getConfidenceInterval();

                seriesMain.add(rep, ds.getMean());
                seriesBottom.add(rep, is[0]);
                seriesTop.add(rep, is[1]);

                chart.updateRange(Constants.OFFSET_FACTOR);
                chart1.fireChartChanged();
            }
        });
    }

    private void updateAverageCountOfNotWorkedOnOrder(SimulationData simData) {
        if (simData.orderNotWorkedOnTotal() != null) {
            labelOrderNotWorkedOn.setText(String.format("%.2f", simData.orderNotWorkedOnTotal().getMean()));
        }
    }

    private void updateAverageTimeInSystemReplication(SimulationData simData) {
        if (simData.orderTimeInSystem() != null && simData.orderTimeInSystem()[0] != null) {
            this.labelAverageTimeInSystem.setText(String.format("%.2f (%.2f)",
                    Event.getWorkDay(simData.orderTimeInSystem()[0].getMean()),
                    simData.orderTimeInSystem()[0].getMean()));
        }
    }

    private void updateAverageTimeInSystemTotal(SimulationData simData) {
        if (simData.orderTimeInSystem() != null && simData.orderTimeInSystem()[1] != null) {
            double[] is = simData.orderTimeInSystem()[1].getConfidenceInterval();
            this.labelAverageTimeInSystemTotal.setText("<html>" + String.format("%.2f (%.2f)<br>[%.2f | %.2f]" + "</html>",
                    Event.getWorkDay(simData.orderTimeInSystem()[1].getMean()),
                    (simData.orderTimeInSystem()[1].getMean()),
                    is[0],
                    is[1]));
        }
    }

    private void updateQueueSize(SimulationData simData) {
        JLabel[] labels = new JLabel[]{labelA, labelB, labelC};

        for (int i = 0; i < labels.length; i++) {
            labels[i].setText(String.format("Group %c (%.2f%% | %.2f%%) - %d | %.2f",
                    (i + 'A'),
                    getSpeed() < Constants.MAX_SPEED ? calculateWorkloadForGroupReplication(simData, i) * 100 : 0.0,
                    simData.workloadForGroupTotal() != null ? simData.workloadForGroupTotal()[i].getMean() * 100 : 0.0,
                    simData.queues() != null && getSpeed() < Constants.MAX_SPEED ? simData.queues()[i] : 0,
                    simData.queueLengthTotal() != null ? simData.queueLengthTotal()[i].getMean() : 0.0));
        }
    }

    private double calculateWorkloadForGroupReplication(SimulationData simData, int i) {
        return Arrays.stream(simData.workers()[i])
                .mapToDouble(w -> w.getStatisticWorkload().getMean())
                .average().getAsDouble();
    }

    public void updateWorkersTotal(SimulationData simData) {
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
    }

    public void updateWorkersReplication(SimulationData simData) {
        if (simData.workers() == null && getSpeed() >= Constants.MAX_SPEED)
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

    public void updateWorkstationOrderTable(SimulationData simData) {
        if (simData.workstations() != null)
            workstationTable.addRows(simData.workstations());
        if (simData.orders() != null)
            orderTable.addRows(simData.orders());
    }

    public void updateTime(SimulationData simData) {
        this.labelTime.setText(Event.timeToDateString(simData.time(), 6));
    }

    public int getSpeed() {
        return getSliderSpeed().getValue();
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

    public Chart getChart() {
        return chart;
    }

    public JFreeChart getChart1() {
        return chart1;
    }

    public ChartPanel getChartPanel1() {
        return chartPanel1;
    }

    public JLabel getCurrentRepLabel() {
        return currentRepLabel;
    }
}
