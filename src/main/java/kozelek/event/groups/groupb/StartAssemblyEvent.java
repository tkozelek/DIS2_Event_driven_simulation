package kozelek.event.groups.groupb;

import kozelek.config.Constants;
import kozelek.entity.order.OrderActivity;
import kozelek.entity.order.OrderType;
import kozelek.entity.worker.Worker;
import kozelek.entity.worker.WorkerWork;
import kozelek.event.Event;
import kozelek.simulation.Simulation;
import kozelek.simulation.SimulationCore;

public class StartAssemblyEvent extends Event {
    private Worker worker;
    public StartAssemblyEvent(SimulationCore simulationCore, double time, Worker worker) {
        super(simulationCore, time);
        this.worker = worker;
    }

    @Override
    public void execute() {
        Simulation simulation = (Simulation) this.getSimulationCore();

        if (Constants.DEBUG)
            System.out.format("S: [%.2f] %s assembly on order %d\n",
                    this.getTime(), worker, worker.getCurrentOrder().getId());

        if (worker.getCurrentOrder().getWorkstation() != worker.getCurrentWorkstation())
            throw new IllegalStateException("Order workstation is not the same as the current workstation");

        worker.setCurrentWork(WorkerWork.ASSEMBLING, time);
        worker.getCurrentOrder().setStartAssemblyTime(this.getTime());

        if (worker.getCurrentOrder().getOrderActivity() != OrderActivity.Painted)
            throw new IllegalStateException("Order activity should be Painted");

        this.worker.getCurrentOrder().setOrderActivity(OrderActivity.Assembling);

        double offset = this.getAssemblyTimeBasedOnOrderType(simulation);
        double endTime = this.getTime() + offset;

        if (endTime < Constants.SIMULATION_TIME)
            simulation.addEvent(new EndAssemblyEvent(getSimulationCore(), endTime, worker));

    }

    private double getAssemblyTimeBasedOnOrderType(Simulation simulation) {
        return switch (this.worker.getCurrentOrder().getOrderType()) {
            case OrderType.CHAIR -> simulation.getAssemblyChairGenerator().sample();
            case OrderType.TABLE -> simulation.getAssemblyTableGenerator().sample();
            case OrderType.CUPBOARD -> simulation.getAssemblycupboardGenerator().sample();
        };
    }
}
