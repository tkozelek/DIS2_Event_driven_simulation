package kozelek.event.groups.groupa;

import kozelek.entity.carpenter.Worker;
import kozelek.entity.carpenter.WorkerWork;
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
        System.out.format("EndCuttingEvent, worker: %d, time: %.2f\n", worker.getId(), time);

        Simulation simulation = (Simulation) getSimulationCore();

        // group a finished
        Order order = this.worker.getCurrentOrder();
        simulation.addToQueueC(order);

        order.setCurrentWorker(null);
        order.setOrderActivity(OrderActivity.Cut);
        order.setFinishCuttingTime(this.time);

        this.worker.setCurrentWork(WorkerWork.IDLE);
        this.worker.setCurrentOrder(null);


    }
}
