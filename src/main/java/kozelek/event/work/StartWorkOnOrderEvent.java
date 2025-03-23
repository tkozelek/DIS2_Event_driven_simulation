package kozelek.event.work;

import kozelek.entity.order.Order;
import kozelek.event.Event;
import kozelek.simulation.Simulation;
import kozelek.simulation.SimulationCore;

public class StartWorkOnOrderEvent extends Event {
    public StartWorkOnOrderEvent(SimulationCore simulationCore, double time, Order order) {
        super(simulationCore, time);
    }

    @Override
    public void execute() {

    }
}
