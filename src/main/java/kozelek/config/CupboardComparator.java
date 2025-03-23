package kozelek.config;

import kozelek.entity.order.Order;
import kozelek.entity.order.OrderType;

import java.util.Comparator;

public class CupboardComparator implements Comparator<Order> {
    @Override
    public int compare(Order o1, Order o2) {
        if (o1.getOrderType() == OrderType.CUPBOARD && o2.getOrderType() != OrderType.CUPBOARD) {
            return -1;
        }
        if (o2.getOrderType() == OrderType.CUPBOARD && o1.getOrderType() != OrderType.CUPBOARD) {
            return 1;
        }
        return Integer.compare(o1.getId(), o2.getId());
    }
}
