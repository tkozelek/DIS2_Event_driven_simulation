package kozelek.event.groups.groupa;

import kozelek.entity.carpenter.Worker;
import kozelek.entity.carpenter.WorkerPosition;
import kozelek.entity.order.OrderActivity;
import kozelek.entity.order.OrderType;
import kozelek.event.Event;
import kozelek.simulation.Simulation;
import kozelek.simulation.SimulationCore;

public class StartCuttingEvent extends Event {
    private Worker worker;
    public StartCuttingEvent(SimulationCore simulationCore, double time, Worker worker) {
        super(simulationCore, time);
        this.worker = worker;
    }

    @Override
    public void execute() {
        Simulation simulation = (Simulation) this.getSimulationCore();

        if (this.worker.getCurrentOrder().getOrderActivity() != OrderActivity.Empty)
            throw new IllegalStateException("[StartCuttingEvent] Order activity is ahead of expected situation, current activity is " + this.worker.getCurrentOrder().getOrderActivity());
        if (this.worker.getCurrentPosition() != WorkerPosition.MOUNTING_PLACE)
            throw new IllegalStateException("[StartCuttingEvent] Worker is not at mounting place, current location = " + worker.getCurrentPosition());

        double offset = this.getCuttingTimeBasedOnOrderType(simulation);
        double nextEventTime = getTime() + offset;

    }

    private double getCuttingTimeBasedOnOrderType(Simulation simulation) {
        return switch (this.worker.getCurrentOrder().getOrderType()) {
            case OrderType.CHAIR -> simulation.getCuttingChairGenerator().sample();
            case OrderType.TABLE -> simulation.getCuttingTableGenerator().sample();
            case OrderType.CUPBOARD -> simulation.getCuttingcupboardGenerator().sample();
            default -> throw new IllegalStateException("[StartCuttingEvent] Order type is unknown");
        };
    }
}
