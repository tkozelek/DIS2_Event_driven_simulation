package kozelek.event.order;

import kozelek.config.Constants;
import kozelek.entity.order.Order;
import kozelek.entity.order.OrderType;
import kozelek.event.Event;
import kozelek.simulation.Simulation;
import kozelek.simulation.SimulationCore;

public class OrderArrivalEvent extends Event {

    public OrderArrivalEvent(SimulationCore simulationCore, double time) {
        super(simulationCore, time);
    }

    @Override
    public void execute() {
        Simulation simCore = (Simulation) this.getSimulationCore();

        if (this.getTime() > Constants.SIMULATION_TIME) {
            throw new IllegalStateException("[ORDER ARRIVAL] Order arrived after simulation time.");
        }

        // vytvorenie objednávky
        Order order = new Order(simCore.getOrderId(), (OrderType) simCore.getOrderTypeGenerator().sample());
        order.setArrivalTime(this.getTime());

        // vytvorenie eventu začiatku práce na objednávke za predpokladu že je dostupný zo skupiny A


        // ak nie je dostupny pridaj do queue objednávok

        // naplanovanie novej objednavky
        double offset = simCore.getOrderArrivalGenerator().sample();
        double nextOrderTime = this.getTime() + offset;

        if (nextOrderTime < Constants.SIMULATION_TIME) {
            OrderArrivalEvent newEvent = new OrderArrivalEvent(simCore, nextOrderTime);
            simCore.addEvent(newEvent);
        }



    }
}
