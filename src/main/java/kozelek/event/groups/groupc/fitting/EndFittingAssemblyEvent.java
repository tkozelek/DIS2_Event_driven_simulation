package kozelek.event.groups.groupc.fitting;

import kozelek.config.Constants;
import kozelek.entity.Workstation;
import kozelek.entity.order.Order;
import kozelek.entity.order.OrderActivity;
import kozelek.entity.worker.Worker;
import kozelek.entity.worker.WorkerGroup;
import kozelek.entity.worker.WorkerWork;
import kozelek.event.Event;
import kozelek.event.groups.StartWorkOnOrderEvent;
import kozelek.simulation.Simulation;
import kozelek.simulation.SimulationCore;

public class EndFittingAssemblyEvent extends Event {
    private Worker worker;
    public EndFittingAssemblyEvent(SimulationCore simulationCore, double time, Worker worker) {
        super(simulationCore, time);
        this.worker = worker;
    }

    @Override
    public void execute() {
        Simulation simulation = (Simulation) getSimulationCore();

        if (Constants.DEBUG)
            System.out.format("E: [%.2f] %s fitting order #%d, %s\n",
                    this.getTime(), worker, worker.getCurrentOrder().getId(), worker.getCurrentOrder().getOrderType());

        Order order = worker.getCurrentOrder();
        Workstation workstation = order.getWorkstation();

        worker.setCurrentWork(WorkerWork.IDLE, time);

        workstation.setCurrentOrder(null);

        order.setFinishFittingAssemblyTime(this.getTime());
        order.setFinishTime(this.getTime());
        order.setOrderActivity(OrderActivity.Finished);
        order.setCurrentWorker(null);
        simulation.addToFinished(order);

        if (simulation.getGroupCQueueSize() > 0)
            simulation.addEvent(new StartWorkOnOrderEvent(simulation, this.getTime(), WorkerGroup.GROUP_C));
    }
}
