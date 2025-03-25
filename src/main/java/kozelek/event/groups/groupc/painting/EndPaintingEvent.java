package kozelek.event.groups.groupc.painting;

import kozelek.config.Constants;
import kozelek.entity.order.Order;
import kozelek.entity.order.OrderActivity;
import kozelek.entity.worker.Worker;
import kozelek.entity.worker.WorkerGroup;
import kozelek.entity.worker.WorkerWork;
import kozelek.event.Event;
import kozelek.event.groups.StartWorkOnOrderEvent;
import kozelek.simulation.Simulation;
import kozelek.simulation.SimulationCore;

public class EndPaintingEvent extends Event {
    private final Worker worker;

    public EndPaintingEvent(SimulationCore simulationCore, double time, Worker worker) {
        super(simulationCore, time);
        this.worker = worker;
    }

    @Override
    public void execute() {
        Simulation simulation = (Simulation) getSimulationCore();

        if (Constants.DEBUG)
            System.out.format("E: [%.2f] %s painting order %d\n",
                    this.getTime(), worker, worker.getCurrentOrder().getId());

        Order order = worker.getCurrentOrder();
        order.setCurrentWorker(null);
        order.setFinishPaintingTime(this.getTime());
        order.setOrderActivity(OrderActivity.Painted);

        worker.setCurrentWork(WorkerWork.IDLE, time);
        worker.setCurrentOrder(null);

        simulation.addToQueueB(order, time);

        worker.setCurrentOrder(null);

        if (simulation.getGroupCQueueSize() > 0)
            simulation.addEvent(new StartWorkOnOrderEvent(simulation, this.getTime(), worker.getGroup()));

        if (simulation.getGroupBQueueSize() > 0)
            simulation.addEvent(new StartWorkOnOrderEvent(simulation, this.getTime(), WorkerGroup.GROUP_B));
    }
}
