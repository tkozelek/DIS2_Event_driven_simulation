package kozelek.entity;

import kozelek.entity.order.Order;

public class Workstation {
    private Order currentOrder;
    private final int id;

    public Workstation(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }
}
