package kozelek.event.groups.groupa;

import kozelek.config.Constants;
import kozelek.entity.worker.Worker;
import kozelek.entity.worker.WorkerWork;
import kozelek.entity.order.Order;
import kozelek.entity.order.OrderActivity;
import kozelek.event.Event;
import kozelek.simulation.Simulation;
import kozelek.simulation.SimulationCore;

public class EndCuttingEvent extends Event {
    private final Worker worker;

    public EndCuttingEvent(SimulationCore simulationCore, double time, Worker worker) {
        super(simulationCore, time);
        this.worker = worker;
    }

    @Override
    public void execute() {
        if (Constants.DEBUG)
            System.out.format("E: [%.2f] %s cutting %s\n",
                    this.getTime(), worker, worker.getCurrentOrder());

        Simulation simulation = (Simulation) getSimulationCore();

        // group a finished
        Order order = this.worker.getCurrentOrder();
        simulation.addToQueueC(order);

        order.setCurrentWorker(null);
        order.setOrderActivity(OrderActivity.Cut);
        order.setFinishCuttingTime(this.time);

        this.worker.setCurrentWork(WorkerWork.IDLE);
        this.worker.setCurrentOrder(null);

        // dokončil pracu, pozri ci je v queue, presun ho

    }
}
