package kozelek.event.groups.groupa;

import kozelek.config.Constants;
import kozelek.entity.Workstation;
import kozelek.entity.carpenter.Worker;
import kozelek.entity.carpenter.WorkerPosition;
import kozelek.entity.carpenter.WorkerWork;
import kozelek.entity.order.Order;
import kozelek.event.Event;
import kozelek.event.move.StartMoveEvent;
import kozelek.event.storage.StartMaterialPreparationEvent;
import kozelek.simulation.Simulation;
import kozelek.simulation.SimulationCore;

public class StartWorkOnOrderGroupAEvent extends Event {
    private Order order;
    private final Workstation workstation;
    private final Worker worker;
    public StartWorkOnOrderGroupAEvent(SimulationCore simulationCore, double time, Worker worker, Workstation workstation) {
        super(simulationCore, time);
        this.workstation = workstation;
        this.worker = worker;
    }

    @Override
    public void execute() {
        System.out.format("StartWorkOnOrderGroupAEvent, worker: %d, time: %.2f\n", worker.getId(), time);

        Simulation simulation = (Simulation) getSimulationCore();
        if (this.worker.getCurrentWork() != WorkerWork.IDLE)
            throw new IllegalStateException("[Start group A] Worker is not IDLE");
        if (this.getTime() > Constants.SIMULATION_TIME)
            throw new IllegalStateException("[Start group A] Simulation time exceeds current time");

        this.order = simulation.pollFromQueueA();
        if (this.order == null)
            throw new IllegalStateException("[Start group A] Order is null");

        this.worker.setCurrentOrder(order);
        this.worker.setCurrentWorkstation(workstation);
        this.order.setWorkstation(workstation);
        this.order.setCurrentWorker(worker);

        // move event
        if (this.worker.getCurrentPosition() == WorkerPosition.MOUNTING_PLACE) {
            StartMoveEvent nextEvent = new StartMoveEvent(
                    this.getSimulationCore(),
                    this.getTime(),
                    WorkerPosition.STORAGE,
                    this.worker);
            simulation.addEvent(nextEvent);
        } else if (this.worker.getCurrentPosition() == WorkerPosition.STORAGE) {
            StartMaterialPreparationEvent nextEvent = new StartMaterialPreparationEvent(
                    this.getSimulationCore(),
                    this.getTime(),
                    worker);
            simulation.addEvent(nextEvent);
        }
    }
}
