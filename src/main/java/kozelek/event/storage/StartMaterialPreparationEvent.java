package kozelek.event.storage;

import kozelek.config.Constants;
import kozelek.entity.carpenter.Worker;
import kozelek.entity.carpenter.WorkerWork;
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
        System.out.format("StartMaterialPreparationEvent, worker: %d,time: %.2f\n", worker.getId(), time);

        Simulation simulation = (Simulation) getSimulationCore();

        this.worker.setCurrentWork(WorkerWork.PREPARING_MATERIAL);

        double offset = simulation.getMaterialPreparationGenerator().sample();
        double nextEventTime = this.getTime() + offset;
        if (nextEventTime < Constants.SIMULATION_TIME)
            simulation.addEvent(new EndMaterialPreparationEvent(getSimulationCore(), nextEventTime, worker));
    }
}
