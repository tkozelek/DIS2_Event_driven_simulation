package kozelek.event;

import kozelek.simulation.SimulationCore;

public class SystemEvent extends Event {

    public SystemEvent(SimulationCore simulationCore, double time) {
        super(simulationCore, time);
    }

    @Override
    public void execute() {
        System.out.println("SystemEvent + time = " + time);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // vytvor iba ak je nizsia rychlost, inac vynechat systemove udallsti
        if (this.getSimulationCore().getSpeed() < 1000)
            this.getSimulationCore().addEvent(new SystemEvent(this.getSimulationCore(), this.getTime() + this.getSimulationCore().getSpeed()));

    }


}
