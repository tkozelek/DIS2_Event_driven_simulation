package kozelek.event;

import kozelek.config.Constants;
import kozelek.simulation.Simulation;
import kozelek.simulation.SimulationCore;

public class SystemEvent extends Event {

    public SystemEvent(SimulationCore simulationCore, double time) {
        super(simulationCore, time);
    }

    @Override
    public void execute() {
        Simulation simulation = (Simulation) getSimulationCore();
        if (Constants.DEBUG)
            System.out.format("S: [%.2f] System event \n",
                    this.getTime());

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        double next = this.getTime() + this.getSimulationCore().getSpeed();

        // vytvor iba ak je nizsia rychlost, inac vynechat systemove udallsti
        if (this.getSimulationCore().getSpeed() < Constants.MAX_SPEED && next < Constants.SIMULATION_TIME)
            this.getSimulationCore().addEvent(new SystemEvent(this.getSimulationCore(), next));

    }


}
