package kozelek.event.groups.groupc.fitting;

import kozelek.config.Constants;
import kozelek.entity.order.OrderActivity;
import kozelek.entity.order.OrderType;
import kozelek.entity.worker.Worker;
import kozelek.entity.worker.WorkerWork;
import kozelek.event.Event;
import kozelek.simulation.Simulation;
import kozelek.simulation.SimulationCore;

public class StartFittingAssemblyEvent extends Event {
    private final Worker worker;
    public StartFittingAssemblyEvent(SimulationCore simulationCore, double time, Worker worker) {
        super(simulationCore, time);
        this.worker = worker;
    }

    @Override
    public void execute() {
        Simulation simulation = (Simulation) getSimulationCore();

        if (Constants.DEBUG)
            System.out.format("S: [%.2f] %s fitting order #%d, %s\n",
                    this.getTime(), worker, worker.getCurrentOrder().getId(), worker.getCurrentOrder().getOrderType());

        worker.setCurrentWork(WorkerWork.FITTING, time);
        worker.getCurrentOrder().setStartFittingAssemblyTime(this.getTime());

        if (worker.getCurrentOrder().getOrderType() != OrderType.CUPBOARD)
            throw new IllegalStateException("Order should be cupboard");

        if (worker.getCurrentOrder().getOrderActivity() != OrderActivity.Assembled)
            throw new IllegalStateException("Order activity should be Assembled");

        worker.getCurrentOrder().setOrderActivity(OrderActivity.Fitting);

        double offset = simulation.getFittingAssemblyGenerator().sample();
        double endTime = this.getTime() + offset;

        if (endTime < Constants.SIMULATION_TIME)
            simulation.addEvent(new EndFittingAssemblyEvent(getSimulationCore(), endTime, worker));

    }
}
