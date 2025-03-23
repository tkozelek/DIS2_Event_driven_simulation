package kozelek.event.groups.groupa;

import kozelek.config.Constants;
import kozelek.entity.Workstation;
import kozelek.entity.carpenter.Worker;
import kozelek.entity.carpenter.WorkerPosition;
import kozelek.entity.carpenter.WorkerWork;
import kozelek.entity.order.Order;
import kozelek.event.Event;
import kozelek.event.move.StartMoveEvent;
import kozelek.simulation.Simulation;
import kozelek.simulation.SimulationCore;

public class StartWorkOnOrderGroupAEvent extends Event {
    private final Order order;
    private final Workstation workstation;
    private final Worker worker;
    public StartWorkOnOrderGroupAEvent(SimulationCore simulationCore, double time, Worker worker, Order order, Workstation workstation) {
        super(simulationCore, time);
        this.order = order;
        this.workstation = workstation;
        this.worker = worker;
    }

    @Override
    public void execute() {
        Simulation simulation = (Simulation) getSimulationCore();
        if (this.worker.getCurrentWork() != WorkerWork.IDLE)
            throw new IllegalStateException("Worker is not IDLE");
        if (this.getTime() > Constants.SIMULATION_TIME)
            throw new IllegalStateException("Simulation time exceeds current time");

        this.worker.setCurrentOrder(order);
        this.worker.setCurrentWorkstation(workstation);

        // move event
        if (this.worker.getCurrentPosition() == WorkerPosition.MOUNTING_PLACE) {
            StartMoveEvent nextEvent = new StartMoveEvent(this.getSimulationCore(),
                    this.getTime(),
                    WorkerPosition.STORAGE,
                    this.worker);
            simulation.addEvent(nextEvent);
        } else {
            StartMoveEvent nextEvent = new StartMoveEvent(this.getSimulationCore(),
                    this.getTime(),
                    WorkerPosition.MOUNTING_PLACE,
                    this.worker);
            simulation.addEvent(nextEvent);
        }
    }
}
