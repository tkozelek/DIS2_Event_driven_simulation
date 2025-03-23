package kozelek.event.move;

import kozelek.config.Constants;
import kozelek.entity.Workstation;
import kozelek.entity.worker.Worker;
import kozelek.event.Event;
import kozelek.event.groups.groupc.StartPaintingEvent;
import kozelek.simulation.Simulation;
import kozelek.simulation.SimulationCore;

public class EndMovePlacesEvent extends Event {
    private final Workstation destination;
    private final Worker worker;
    public EndMovePlacesEvent(SimulationCore simulationCore, double time, Worker worker, Workstation to) {
        super(simulationCore, time);
        this.destination = to;
        this.worker = worker;
    }

    @Override
    public void execute() {
        Simulation simulation = (Simulation) getSimulationCore();

        if (Constants.DEBUG)
            System.out.format("E: [%.2f] %s arrived at Workstation %d",
                    this.getTime(), worker, destination.getId());

        worker.setCurrentWorkstation(destination);

        simulation.addEvent(new StartPaintingEvent(getSimulationCore(), time, worker));
    }
}
