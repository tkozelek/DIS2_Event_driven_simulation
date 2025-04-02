package kozelek.entity;

import kozelek.entity.order.Order;

public class Workstation {
    private final int id;
    private Order currentOrder;

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
