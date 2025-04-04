package kozelek.event.extrawork;

import kozelek.config.Constants;
import kozelek.entity.order.Order;
import kozelek.entity.order.OrderActivity;
import kozelek.entity.worker.WorkerGroup;
import kozelek.event.Event;
import kozelek.event.groups.StartWorkOnOrderEvent;
import kozelek.simulation.Simulation;
import kozelek.simulation.SimulationCore;

public class EndSusenieEvent extends Event {
    private final Order order;

    public EndSusenieEvent(SimulationCore simulationCore, double time, Order order) {
        super(simulationCore, time);
        this.order = order;
    }

    @Override
    public void execute() {
        Simulation simulation = (Simulation) getSimulationCore();

        if (Constants.DEBUG)
            System.out.format("E: [%.2f] stop drying order %d\n",
                    this.getTime(), order.getId());

        this.order.setFinishDryingTime(this.getTime());
        this.order.setOrderActivity(OrderActivity.Dried);
        simulation.addToQueueB(order, time);

        if (simulation.getGroupCQueueSize() > 0)
            simulation.addEvent(new StartWorkOnOrderEvent(simulation, this.getTime(), WorkerGroup.GROUP_C));

        if (simulation.getGroupBQueueSize() > 0)
            simulation.addEvent(new StartWorkOnOrderEvent(simulation, this.getTime(), WorkerGroup.GROUP_B));
    }
}
