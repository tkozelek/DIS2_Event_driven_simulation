package kozelek.event;

import kozelek.simulation.SimulationCore;

public class SystemEvent extends Event {

    public SystemEvent(SimulationCore simulationCore, double time) {
        super(simulationCore, time);
    }

    @Override
    public void execute() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        this.getSimulationCore().addEvent(new SystemEvent(this.getSimulationCore(), this.getTime() + this.getSimulationCore().getSpeed()));
    }


}
