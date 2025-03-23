package kozelek.config;

import kozelek.entity.order.Order;
import kozelek.entity.order.OrderActivity;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class OrderActivityComparator implements Comparator<Order> {

    private static final List<OrderActivity> ACTIVITY_PRIORITY = Arrays.asList(
            OrderActivity.Empty,
            OrderActivity.Cut,
            OrderActivity.Painted,
            OrderActivity.Assembled,
            OrderActivity.Fitting,
            OrderActivity.Finished
    );

    @Override
    public int compare(Order o1, Order o2) {
        int index1 = ACTIVITY_PRIORITY.indexOf(o1.getOrderActivity());
        int index2 = ACTIVITY_PRIORITY.indexOf(o2.getOrderActivity());

        return Integer.compare(index1, index2);
    }
}
