package kozelek.config;

import kozelek.entity.order.Order;
import kozelek.entity.order.OrderActivity;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class OrderActivityComparator implements Comparator<Order> {
    @Override
    public int compare(Order o1, Order o2) {
        return Integer.compare(o1.getId(), o2.getId());
    }
}
