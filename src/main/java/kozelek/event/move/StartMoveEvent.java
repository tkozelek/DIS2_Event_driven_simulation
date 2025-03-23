package kozelek.event.move;

import kozelek.config.Constants;
import kozelek.entity.worker.Worker;
import kozelek.entity.worker.WorkerPosition;
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
        if (Constants.DEBUG)
            System.out.format("S: [%.2f] %s moving to %s\n",
                    this.getTime(), worker, this.endPosition);

        Simulation simulation = (Simulation) getSimulationCore();

        if (this.worker.getCurrentPosition().equals(this.endPosition))
            throw new IllegalStateException("Worker is already at the destination");

        this.worker.setCurrentPosition(WorkerPosition.MOVING);

        double offset = simulation.getMoveToStorageGenerator().sample();
        double timeOfNextEvent = this.getTime() + offset;
        if (timeOfNextEvent < Constants.SIMULATION_TIME)
            simulation.addEvent(new EndMoveEvent(simulation, timeOfNextEvent, worker, endPosition));
    }
}
