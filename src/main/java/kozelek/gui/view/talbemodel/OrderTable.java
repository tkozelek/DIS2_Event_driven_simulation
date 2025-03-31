package kozelek.gui.view.talbemodel;

import kozelek.entity.order.Order;
import kozelek.event.Event;

import java.util.List;

public class OrderTable extends Table<Order> {

    public OrderTable() {
        super(new String[]{"ID", "Type", "Wid", "Stat", "Arr", "S Cut", "E Cut", "S Paint", "E Paint", "S Ass", "E Ass", "S Fit", "E Fit", "Fin"},
                List.of(
                        Order::getId,
                        Order::getOrderType,
                        Order::getCurrentWorker,
                        Order::getOrderActivity,
                        w -> String.format("%s", Event.timeToDateString(w.getArrivalTime(), 6)),
                        w -> String.format("%s", Event.timeToDateString(w.getStartCuttingTime(), 6)),
                        w -> String.format("%s", Event.timeToDateString(w.getFinishCuttingTime(), 6)),
                        w -> String.format("%s", Event.timeToDateString(w.getStartPaintingTime(), 6)),
                        w -> String.format("%s", Event.timeToDateString(w.getFinishPaintingTime(), 6)),
                        w -> String.format("%s", Event.timeToDateString(w.getStartAssemblyTime(), 6)),
                        w -> String.format("%s", Event.timeToDateString(w.getFinishAssemblyTime(), 6)),
                        w -> String.format("%s", Event.timeToDateString(w.getStartFittingAssemblyTime(), 6)),
                        w -> String.format("%s", Event.timeToDateString(w.getFinishFittingAssemblyTime(), 6)),
                        w -> String.format("%s", Event.timeToDateString(w.getFinishTime(), 6))
                ));
    }
}
