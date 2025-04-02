package kozelek.config;

import kozelek.entity.order.Order;

import java.util.Comparator;

public class OrderActivityComparator implements Comparator<Order> {
    @Override
    public int compare(Order o1, Order o2) {
        return Integer.compare(o1.getId(), o2.getId());
    }
}
