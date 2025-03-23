package kozelek.event.groups.groupc;

import kozelek.config.Constants;
import kozelek.entity.order.OrderType;
import kozelek.entity.worker.Worker;
import kozelek.entity.worker.WorkerWork;
import kozelek.event.Event;
import kozelek.simulation.Simulation;
import kozelek.simulation.SimulationCore;

public class StartPaintingEvent extends Event {
    private final Worker worker;

    public StartPaintingEvent(SimulationCore simulationCore, double time, Worker worker) {
        super(simulationCore, time);
        this.worker = worker;
    }

    @Override
    public void execute() {
        Simulation simulation = (Simulation) getSimulationCore();

        if (Constants.DEBUG)
            System.out.format("S: [%.2f] %s painting order #%d\n",
                this.getTime(), worker, worker.getCurrentOrder().getId());

        worker.setCurrentWork(WorkerWork.PAINTING);
        worker.getCurrentOrder().setStartPaintingTime(this.getTime());

        double offset = this.getPaintingTimeBasedOnOrderType(simulation);
        double endTime = this.getTime() + offset;
        if (endTime < Constants.SIMULATION_TIME)
            simulation.addEvent(new EndPaintingEvent(getSimulationCore(), endTime, worker));
    }

    private double getPaintingTimeBasedOnOrderType(Simulation simulation) {
        return switch (this.worker.getCurrentOrder().getOrderType()) {
            case OrderType.CHAIR -> simulation.getMorenieLakovanieChairGenerator().sample();
            case OrderType.TABLE -> simulation.getMorenieLakovanieTableGenerator().sample();
            case OrderType.CUPBOARD -> simulation.getMorenieLakovanieCupboardGenerator().sample();
        };
    }
}
