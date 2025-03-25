package kozelek.gui.view.talbemodel;

import kozelek.entity.order.Order;
import kozelek.entity.worker.Worker;
import kozelek.event.Event;

import java.util.List;
import java.util.function.Function;

public class OrderTable extends Table<Order> {

    public OrderTable() {
        super(new String[]{"ID", "Type", "Wid", "Stat", "Arr", "S Cut", "E Cut", "S Paint", "E Paint", "S Ass", "E Ass", "S Fit", "E Fit", "Fin"},
                List.of(
                Order::getId,
                Order::getOrderType,
                Order::getCurrentWorker,
                Order::getOrderActivity,
                w -> String.format("%s", Event.timeToString(w.getArrivalTime())),
                w -> String.format("%s", Event.timeToString(w.getStartCuttingTime())),
                w -> String.format("%s", Event.timeToString(w.getFinishCuttingTime())),
                w -> String.format("%s", Event.timeToString(w.getStartPaintingTime())),
                w -> String.format("%s", Event.timeToString(w.getFinishPaintingTime())),
                w -> String.format("%s", Event.timeToString(w.getStartAssemblyTime())),
                w -> String.format("%s", Event.timeToString(w.getFinishAssemblyTime())),
                w -> String.format("%s", Event.timeToString(w.getStartFittingAssemblyTime())),
                w -> String.format("%s", Event.timeToString(w.getFinishFittingAssemblyTime())),
                w -> String.format("%s", Event.timeToString(w.getFinishTime()))
        ));
    }
}
