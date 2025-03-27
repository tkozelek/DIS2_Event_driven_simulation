package kozelek.event.groups.groupb;

import kozelek.config.Constants;
import kozelek.entity.Workstation;
import kozelek.entity.order.Order;
import kozelek.entity.order.OrderActivity;
import kozelek.entity.order.OrderType;
import kozelek.entity.worker.Worker;
import kozelek.entity.worker.WorkerGroup;
import kozelek.entity.worker.WorkerWork;
import kozelek.event.Event;
import kozelek.event.groups.StartWorkOnOrderEvent;
import kozelek.simulation.Simulation;
import kozelek.simulation.SimulationCore;

public class EndAssemblyEvent extends Event {
    private final Worker worker;
    public EndAssemblyEvent(SimulationCore simulationCore, double time, Worker worker) {
        super(simulationCore, time);
        this.worker = worker;
    }

    @Override
    public void execute() {
        Simulation simulation = (Simulation) this.getSimulationCore();

        if (Constants.DEBUG)
            System.out.format("E: [%.2f] %s assembly on order %d\n",
                    this.getTime(), worker, worker.getCurrentOrder().getId());

        Order order = this.worker.getCurrentOrder();
        Workstation workstation = this.worker.getCurrentWorkstation();
        order.setFinishAssemblyTime(this.getTime());
        order.setCurrentWorker(null);

        worker.setCurrentWork(WorkerWork.IDLE, time);
        worker.setCurrentOrder(null);
        order.setOrderActivity(OrderActivity.Assembled);


        if (order.getOrderType() == OrderType.CUPBOARD) {
            simulation.addToQueueC(order, time);
        } else {
            order.setFinishTime(this.getTime());
            workstation.setCurrentOrder(null);
            order.setOrderActivity(OrderActivity.Finished);
            simulation.addToFinished(order);
            order.setCurrentWorker(null);
        }

        if (simulation.getGroupBQueueSize() > 0)
            simulation.addEvent(new StartWorkOnOrderEvent(getSimulationCore(), time, WorkerGroup.GROUP_B));
    }
}
