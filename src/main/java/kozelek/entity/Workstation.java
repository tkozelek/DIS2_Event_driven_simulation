package kozelek.entity;

import kozelek.entity.carpenter.Worker;
import kozelek.entity.order.Order;

import java.util.ArrayList;
import java.util.List;

public class Workstation {
    private Order currentOrder;
    private final List<Worker> workersAtStation;

    public Workstation() {
        this.workersAtStation = new ArrayList<>();
    }

    public void addWorker(Worker worker) {
        workersAtStation.add(worker);
    }

    public void removeWorker(Worker worker) {
        workersAtStation.remove(worker);
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }
}
