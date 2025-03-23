package kozelek.event.storage;

import kozelek.entity.carpenter.Worker;
import kozelek.event.Event;
import kozelek.simulation.Simulation;
import kozelek.simulation.SimulationCore;

public class StartMaterialPreparationEvent extends Event {
    private Worker worker;
    public StartMaterialPreparationEvent(SimulationCore simulationCore, double time, Worker worker) {
        super(simulationCore, time);
        this.worker = worker;
    }

    @Override
    public void execute() {
        Simulation simulation = (Simulation) getSimulationCore();

        double offset = simulation.getMaterialPreparationGenerator().sample();
        double nextEventTime = this.getTime() + offset;
        simulation.addEvent(new EndMaterialPreparationEvent(getSimulationCore(), nextEventTime, worker));
    }
}
