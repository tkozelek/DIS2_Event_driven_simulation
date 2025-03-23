package kozelek.event.move;

import kozelek.config.Constants;
import kozelek.entity.worker.Worker;
import kozelek.entity.worker.WorkerPosition;
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
        if (Constants.DEBUG)
            System.out.format("E: [%.2f] %s moving to %s\n",
                    this.getTime(), worker, destination);

        worker.setCurrentPosition(destination);

        if (worker.getCurrentOrder() == null)
            throw new IllegalStateException("[EndMoveEvent] Worker has no current order");

        Simulation simulation = (Simulation) getSimulationCore();

        if (destination == WorkerPosition.STORAGE) {
            simulation.addEvent(new StartMaterialPreparationEvent(getSimulationCore(), this.getTime(), worker));
        } else if (destination == WorkerPosition.WORKSTATION) {
            simulation.addEvent(new StartCuttingEvent(getSimulationCore(), this.getTime(), worker));
        }

    }
}
