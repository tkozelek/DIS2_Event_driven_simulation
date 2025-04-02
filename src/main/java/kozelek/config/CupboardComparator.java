package kozelek.config;

import kozelek.entity.order.Order;
import kozelek.entity.order.OrderActivity;
import kozelek.entity.order.OrderType;

import java.util.Comparator;

public class CupboardComparator implements Comparator<Order> {
    @Override
    public int compare(Order o1, Order o2) {
        boolean o1IsAssembledCupboard = o1.getOrderActivity() == OrderActivity.Assembled && o1.getOrderType() == OrderType.CUPBOARD;
        boolean o2IsAssembledCupboard = o2.getOrderActivity() == OrderActivity.Assembled && o2.getOrderType() == OrderType.CUPBOARD;

        if (o1IsAssembledCupboard && !o2IsAssembledCupboard) {
            return -1;
        }
        if (o2IsAssembledCupboard && !o1IsAssembledCupboard) {
            return 1;
        }

        return Double.compare(o1.getFinishCuttingTime(), o2.getFinishCuttingTime());
    }
}
