package kozelek.event.move;

import kozelek.config.Constants;
import kozelek.entity.Workstation;
import kozelek.entity.worker.Worker;
import kozelek.entity.worker.WorkerPosition;
import kozelek.entity.worker.WorkerWork;
import kozelek.event.Event;
import kozelek.simulation.Simulation;
import kozelek.simulation.SimulationCore;

public class StartMovePlacesEvent extends Event {
    private final Workstation to;
    private final Worker worker;

    public StartMovePlacesEvent(SimulationCore simulationCore, double time, Worker worker, Workstation to) {
        super(simulationCore, time);
        this.to = to;
        this.worker = worker;
    }

    @Override
    public void execute() {
        if (Constants.DEBUG)
            System.out.format("S: [%.2f] %s moving to Workstation %d\n",
                    this.getTime(), worker, to.getId());

        Simulation simulation = (Simulation) getSimulationCore();

        if (worker.getCurrentWorkstation() == to) {
            throw new IllegalStateException("Worker is already at the destination workstation.");
        }

        worker.setCurrentPosition(WorkerPosition.MOVING);
        worker.setCurrentWork(WorkerWork.MOVING, time);
        worker.setCurrentWorkstation(null);

        double travelTime = simulation.getMoveStationsGenerator().sample();

        double timeOfArrival = this.getTime() + travelTime;

        if (timeOfArrival < Constants.SIMULATION_TIME)
            simulation.addEvent(new EndMovePlacesEvent(simulation, timeOfArrival, worker, to));
    }
}
