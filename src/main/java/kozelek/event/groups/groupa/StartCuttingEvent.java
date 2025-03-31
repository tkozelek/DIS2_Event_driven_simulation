package kozelek.event.groups.groupa;

import kozelek.config.Constants;
import kozelek.entity.order.OrderActivity;
import kozelek.entity.order.OrderType;
import kozelek.entity.worker.Worker;
import kozelek.entity.worker.WorkerPosition;
import kozelek.entity.worker.WorkerWork;
import kozelek.event.Event;
import kozelek.simulation.Simulation;
import kozelek.simulation.SimulationCore;

public class StartCuttingEvent extends Event {
    private final Worker worker;

    public StartCuttingEvent(SimulationCore simulationCore, double time, Worker worker) {
        super(simulationCore, time);
        this.worker = worker;
    }

    @Override
    public void execute() {
        if (Constants.DEBUG)
            System.out.format("S: [%.2f] %s cutting %s\n",
                    this.getTime(), worker, worker.getCurrentOrder());

        Simulation simulation = (Simulation) this.getSimulationCore();

        if (this.worker.getCurrentOrder().getOrderActivity() != OrderActivity.Empty)
            throw new IllegalStateException("[StartCuttingEvent] Order activity is ahead of expected situation, current activity is " + this.worker.getCurrentOrder().getOrderActivity());
        if (this.worker.getCurrentPosition() != WorkerPosition.WORKSTATION)
            throw new IllegalStateException("[StartCuttingEvent] Worker is not at mounting place, current location is " + worker.getCurrentPosition());

        double offset = this.getCuttingTimeBasedOnOrderType(simulation);
        double nextEventTime = getTime() + offset;
        this.worker.setCurrentWork(WorkerWork.CUTTING, time);
        this.worker.getCurrentOrder().setStartCuttingTime(this.time);
        this.worker.getCurrentOrder().setOrderActivity(OrderActivity.Cutting);

        if (nextEventTime < Constants.SIMULATION_TIME) {
            EndCuttingEvent nextEvent = new EndCuttingEvent(simulation, nextEventTime, worker);
            simulation.addEvent(nextEvent);
        }

    }

    private double getCuttingTimeBasedOnOrderType(Simulation simulation) {
        return switch (this.worker.getCurrentOrder().getOrderType()) {
            case OrderType.CHAIR -> simulation.getCuttingChairGenerator().sample();
            case OrderType.TABLE -> simulation.getCuttingTableGenerator().sample();
            case OrderType.CUPBOARD -> simulation.getCuttingCupboardGenerator().sample();
            default -> throw new IllegalStateException("[StartCuttingEvent] Order type is unknown");
        };
    }
}
