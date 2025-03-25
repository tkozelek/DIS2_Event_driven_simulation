package kozelek.event.order;

import kozelek.config.Constants;
import kozelek.entity.worker.Worker;
import kozelek.entity.worker.WorkerGroup;
import kozelek.entity.order.Order;
import kozelek.entity.order.OrderType;
import kozelek.event.Event;
import kozelek.event.groups.StartWorkOnOrderEvent;
import kozelek.simulation.Simulation;
import kozelek.simulation.SimulationCore;

public class OrderArrivalEvent extends Event {

    public OrderArrivalEvent(SimulationCore simulationCore, double time) {
        super(simulationCore, time);
    }

    @Override
    public void execute() {
        Simulation simulation = (Simulation) this.getSimulationCore();
        if (Constants.DEBUG)
            System.out.format("S: [%.2f] Order arrived %d in queue \n",
                    this.getTime(), simulation.getGroupAQueueSize());

        if (this.getTime() > Constants.SIMULATION_TIME) {
            throw new IllegalStateException("[ORDER ARRIVAL] Order arrived after simulation time.");
        }

        // vytvorenie objednávky
        Order order = new Order(simulation.getOrderId(), (OrderType) simulation.getOrderTypeGenerator().sample());
        order.setArrivalTime(this.getTime());
        simulation.addOrder(order);

        // vlozime do queue
        simulation.addToQueueA(order, time);

        // vytvorenie eventu začiatku práce na objednávke za predpokladu že je dostupný zo skupiny A
        Worker worker = simulation.getFreeWorkerFromGroup(WorkerGroup.GROUP_A);
        if (worker != null) {
            StartWorkOnOrderEvent startWorkEvent = new StartWorkOnOrderEvent(
                    getSimulationCore(),
                    getTime(),
                    WorkerGroup.GROUP_A);
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
