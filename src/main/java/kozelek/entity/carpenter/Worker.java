package kozelek.entity.carpenter;

import kozelek.entity.Workstation;
import kozelek.entity.order.Order;

public class Worker {
    private int id;
    private final WorkerGroup group;
    private WorkerWork currentWork;
    private WorkerPosition currentPosition;
    private Order currentOrder;
    private Workstation currentWorkstation;

    public Worker(WorkerGroup group, int id) {
        this.id = id;
        this.group = group;
        this.currentPosition = WorkerPosition.STORAGE;
        this.currentWork = WorkerWork.IDLE;
        this.currentOrder = null;
        this.currentWorkstation = null;
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }

    public Workstation getCurrentWorkstation() {
        return currentWorkstation;
    }

    public void setCurrentWorkstation(Workstation currentWorkstation) {
        this.currentWorkstation = currentWorkstation;
    }

    public WorkerGroup getGroup() {
        return group;
    }

    public WorkerWork getCurrentWork() {
        return currentWork;
    }

    public void setCurrentWork(WorkerWork currentWork) {
        this.currentWork = currentWork;
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
}
