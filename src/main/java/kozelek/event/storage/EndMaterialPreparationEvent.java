package kozelek.event.storage;

import kozelek.entity.carpenter.Worker;
import kozelek.entity.carpenter.WorkerPosition;
import kozelek.event.Event;
import kozelek.event.move.StartMoveEvent;
import kozelek.simulation.Simulation;
import kozelek.simulation.SimulationCore;

public class EndMaterialPreparationEvent extends Event {
    private Worker worker;
    public EndMaterialPreparationEvent(SimulationCore simulationCore, double time, Worker worker) {
        super(simulationCore, time);
        this.worker = worker;
    }

    @Override
    public void execute() {
        Simulation simulation = (Simulation) getSimulationCore();

        simulation.addEvent(new StartMoveEvent(getSimulationCore(), getTime(), WorkerPosition.MOUNTING_PLACE, worker));
    }
}
