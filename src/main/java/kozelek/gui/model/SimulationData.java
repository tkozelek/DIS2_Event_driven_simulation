package kozelek.gui.model;

import kozelek.entity.Workstation;
import kozelek.entity.order.Order;
import kozelek.entity.worker.Worker;

import java.util.List;

public record SimulationData(
        Worker[][] workers,
        List<Workstation> workstations,
        List<Order> orders,
        int currentReplication
        ) { }
