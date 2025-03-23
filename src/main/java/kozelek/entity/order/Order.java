package kozelek.entity.order;

import kozelek.entity.Workstation;
import kozelek.entity.carpenter.WorkerGroup;

public class Order {
    private final long id;
    private final OrderType orderType;
    private OrderActivity orderActivity;
    private Workstation workstation;
    private double arrivalTime;

    public Order(long id, OrderType orderType) {
        this.id = id;
        this.orderType = orderType;
        this.orderActivity = OrderActivity.Empty;
    }

    public WorkerGroup getNextNeededGroup() {
        return switch (orderActivity) {
            case Empty -> WorkerGroup.GROUP_A;
            case Cut, Assembled -> WorkerGroup.GROUP_C;
            case Painted -> WorkerGroup.GROUP_B;
            default -> throw new IllegalStateException("Unexpected value: " + orderActivity);
        };
    }

    public long getId() {
        return id;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public Workstation getWorkstation() {
        return workstation;
    }

    public void setWorkstation(Workstation workstation) {
        this.workstation = workstation;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(double arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public OrderActivity getOrderActivity() {
        return orderActivity;
    }

    public void setOrderActivity(OrderActivity orderActivity) {
        this.orderActivity = orderActivity;
    }
}
