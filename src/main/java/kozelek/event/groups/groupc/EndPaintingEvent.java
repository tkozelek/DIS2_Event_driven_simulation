package kozelek.event.groups.groupc;

import kozelek.entity.order.Order;
import kozelek.entity.order.OrderActivity;
import kozelek.entity.worker.Worker;
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

        System.out.format("S: [%.2f] %s painting order #%d\n",
                this.getTime(), worker, worker.getCurrentOrder().getId());

        Order order = worker.getCurrentOrder();
        order.setCurrentWorker(null);
        order.setFinishPaintingTime(this.getTime());
        order.setOrderActivity(OrderActivity.Painted);

        worker.setCurrentWork(WorkerWork.IDLE);
        worker.setCurrentOrder(null);

        simulation.addToQueueB(worker.getCurrentOrder());

        worker.setCurrentOrder(null);

        if (simulation.getGroupCQueueSize() > 0)
            simulation.addEvent(new StartWorkOnOrderEvent(simulation, this.getTime(), worker.getGroup()));
    }
}
