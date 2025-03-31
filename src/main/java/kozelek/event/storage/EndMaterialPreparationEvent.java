package kozelek.event.storage;

import kozelek.config.Constants;
import kozelek.entity.worker.Worker;
import kozelek.entity.worker.WorkerPosition;
import kozelek.event.Event;
import kozelek.event.move.StartMoveEvent;
import kozelek.simulation.Simulation;
import kozelek.simulation.SimulationCore;

public class EndMaterialPreparationEvent extends Event {
    private final Worker worker;

    public EndMaterialPreparationEvent(SimulationCore simulationCore, double time, Worker worker) {
        super(simulationCore, time);
        this.worker = worker;
    }

    @Override
    public void execute() {
        if (Constants.DEBUG)
            System.out.format("E: [%.2f] %s prep. material for %s\n",
                    this.getTime(), worker, this.worker.getCurrentOrder());

        Simulation simulation = (Simulation) getSimulationCore();

        simulation.addEvent(new StartMoveEvent(getSimulationCore(), getTime(), WorkerPosition.WORKSTATION, worker));
    }
}
