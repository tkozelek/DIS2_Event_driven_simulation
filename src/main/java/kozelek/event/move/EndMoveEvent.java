package kozelek.event.move;

import kozelek.entity.carpenter.Worker;
import kozelek.entity.carpenter.WorkerPosition;
import kozelek.event.Event;
import kozelek.event.groups.groupa.StartCuttingEvent;
import kozelek.event.storage.StartMaterialPreparationEvent;
import kozelek.simulation.Simulation;
import kozelek.simulation.SimulationCore;

public class EndMoveEvent extends Event {
    private Worker worker;
    private WorkerPosition destination;

    public EndMoveEvent(SimulationCore simulationCore, double time, Worker worker, WorkerPosition destination) {
        super(simulationCore, time);
        this.worker = worker;
        this.destination = destination;
    }

    @Override
    public void execute() {
        System.out.format("EndMoveEvent, worker: %d, time: %.2f\n", worker.getId(), time);

        worker.setCurrentPosition(destination);

        if (worker.getCurrentOrder() == null)
            throw new IllegalStateException("[EndMoveEvent] Worker has no current order");

        Simulation simulation = (Simulation) getSimulationCore();

        if (destination == WorkerPosition.STORAGE) {
            simulation.addEvent(new StartMaterialPreparationEvent(getSimulationCore(), this.getTime(), worker));
        } else if (destination == WorkerPosition.MOUNTING_PLACE) {
            simulation.addEvent(new StartCuttingEvent(getSimulationCore(), this.getTime(), worker));
        }

    }
}
