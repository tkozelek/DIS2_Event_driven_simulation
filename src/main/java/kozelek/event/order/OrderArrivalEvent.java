package kozelek.event.order;

import kozelek.config.Constants;
import kozelek.entity.Workstation;
import kozelek.entity.carpenter.Worker;
import kozelek.entity.carpenter.WorkerGroup;
import kozelek.entity.order.Order;
import kozelek.entity.order.OrderType;
import kozelek.event.Event;
import kozelek.event.groups.groupa.StartWorkOnOrderGroupAEvent;
import kozelek.simulation.Simulation;
import kozelek.simulation.SimulationCore;

public class OrderArrivalEvent extends Event {

    public OrderArrivalEvent(SimulationCore simulationCore, double time) {
        super(simulationCore, time);
    }

    @Override
    public void execute() {
        System.out.format("OrderArrivalEvent, time: %.2f\n", time);

        Simulation simulation = (Simulation) this.getSimulationCore();

        if (this.getTime() > Constants.SIMULATION_TIME) {
            throw new IllegalStateException("[ORDER ARRIVAL] Order arrived after simulation time.");
        }

        // vytvorenie objednávky
        Order order = new Order(simulation.getOrderId(), (OrderType) simulation.getOrderTypeGenerator().sample());
        order.setArrivalTime(this.getTime());

        // vlozime do queue
        simulation.addToQueueA(order);

        // vytvorenie eventu začiatku práce na objednávke za predpokladu že je dostupný zo skupiny A
        Worker worker = simulation.getFreeWorkerFromGroup(WorkerGroup.GROUP_A);
        if (worker != null) {
            Workstation workstation = simulation.getFreeWorkstation();
            StartWorkOnOrderGroupAEvent startWorkEvent = new StartWorkOnOrderGroupAEvent(
                    getSimulationCore(),
                    getTime(),
                    worker,
                    workstation);
            simulation.addEvent(startWorkEvent);
        }

        // naplanovanie novej objednavky
        double offset = simulation.getOrderArrivalGenerator().sample();
        double nextOrderTime = this.getTime() + offset;

        if (nextOrderTime < Constants.SIMULATION_TIME) {
            OrderArrivalEvent newEvent = new OrderArrivalEvent(simulation, nextOrderTime);
            simulation.addEvent(newEvent);
        }
    }
}
