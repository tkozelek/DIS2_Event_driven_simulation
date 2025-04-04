package kozelek.event.extrawork;

import kozelek.config.Constants;
import kozelek.entity.order.Order;
import kozelek.entity.order.OrderActivity;
import kozelek.event.Event;
import kozelek.event.groups.groupb.EndAssemblyEvent;
import kozelek.simulation.Simulation;
import kozelek.simulation.SimulationCore;

public class StartSusenieEvent extends Event {
    private final Order order;
    public StartSusenieEvent(SimulationCore simulationCore, double time, Order order) {
        super(simulationCore, time);
        this.order = order;
    }

    @Override
    public void execute() {
        Simulation simulation = (Simulation) getSimulationCore();

        if (Constants.DEBUG)
            System.out.format("S: [%.2f] start drying order %d\n",
                    this.getTime(), order.getId());

        this.order.setStartDryingTime(this.getTime());
        this.order.setOrderActivity(OrderActivity.Drying);

        double offset = simulation.getSusenieGenerator().sample();
        double nextEventTime = offset + this.getTime();

        if (nextEventTime < Constants.SIMULATION_TIME)
            simulation.addEvent(new EndSusenieEvent(getSimulationCore(), nextEventTime, order));
    }
}
