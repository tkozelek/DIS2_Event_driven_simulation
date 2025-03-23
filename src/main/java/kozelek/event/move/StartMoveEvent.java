package kozelek.event.move;

import kozelek.entity.carpenter.Worker;
import kozelek.entity.carpenter.WorkerPosition;
import kozelek.event.Event;
import kozelek.simulation.Simulation;
import kozelek.simulation.SimulationCore;

public class StartMoveEvent extends Event {
    private Worker worker;
    private WorkerPosition endPosition;

    public StartMoveEvent(SimulationCore simulationCore, double time, WorkerPosition endPosition, Worker worker) {
        super(simulationCore, time);
        this.worker = worker;
        this.endPosition = endPosition;
    }

    @Override
    public void execute() {
        Simulation simulation = (Simulation) getSimulationCore();

        if (this.worker.getCurrentPosition().equals(this.endPosition))
            throw new IllegalStateException("Worker is already at the destination");

        this.worker.setCurrentPosition(WorkerPosition.TRAVELLING);

        double offset = simulation.getMoveToStorageGenerator().sample();
        double timeOfNextEvent = this.getTime() + offset;

        simulation.addEvent(new EndMoveEvent(simulation, timeOfNextEvent, worker, endPosition));
    }
}
