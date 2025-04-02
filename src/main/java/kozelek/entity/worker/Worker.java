package kozelek.entity.worker;

import kozelek.entity.Workstation;
import kozelek.entity.order.Order;
import kozelek.statistic.ContinuousStatistic;

public class Worker {
    private final int id;
    private final WorkerGroup group;
    // STATISTIKY
    private final ContinuousStatistic statisticWorkload;
    private WorkerWork currentWork;
    private WorkerPosition currentPosition;
    private Order currentOrder;
    private Workstation currentWorkstation;
    private int finishedTasks = 0;

    public Worker(WorkerGroup group, int id) {
        this.id = id;
        this.group = group;
        this.currentPosition = WorkerPosition.STORAGE;
        this.currentWork = WorkerWork.IDLE;
        this.currentOrder = null;
        this.currentWorkstation = null;

        this.statisticWorkload = new ContinuousStatistic("Workload");
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
        if (currentOrder != null) finishedTasks++;
    }

    public Workstation getCurrentWorkstation() {
        return currentWorkstation;
    }

    public void setCurrentWorkstation(Workstation currentWorkstation) {
        this.currentWorkstation = currentWorkstation;
    }

    public int getFinishedTasks() {
        return finishedTasks;
    }

    public WorkerGroup getGroup() {
        return group;
    }

    public WorkerWork getCurrentWork() {
        return currentWork;
    }

    public void setCurrentWork(WorkerWork currentWork, double time) {
        this.currentWork = currentWork;
        this.statisticWorkload.addValue(time, (currentWork == WorkerWork.IDLE ? 0 : 1));
    }

    public ContinuousStatistic getStatisticWorkload() {
        return statisticWorkload;
    }

    public WorkerPosition getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(WorkerPosition currentPosition) {
        this.currentPosition = currentPosition;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        String gr = group.toString();
        return String.format("W(%s) #%d", gr.charAt(gr.length() - 1), this.id);
    }
}
