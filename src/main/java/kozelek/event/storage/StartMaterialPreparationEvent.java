package kozelek.event.storage;

import kozelek.config.Constants;
import kozelek.entity.worker.Worker;
import kozelek.entity.worker.WorkerWork;
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
        if (Constants.DEBUG)
            System.out.format("S: [%.2f] %s prep. material for %s\n",
                    this.getTime(), worker, this.worker.getCurrentOrder());

        Simulation simulation = (Simulation) getSimulationCore();

        this.worker.setCurrentWork(WorkerWork.PREPARING_MATERIAL);

        double offset = simulation.getMaterialPreparationGenerator().sample();
        double nextEventTime = this.getTime() + offset;
        if (nextEventTime < Constants.SIMULATION_TIME)
            simulation.addEvent(new EndMaterialPreparationEvent(getSimulationCore(), nextEventTime, worker));
    }
}
