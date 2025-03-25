package kozelek.gui.interfaces;

import kozelek.gui.model.SimulationData;

public interface Observer {
    void update(SimulationData data);
    void updateTime(double time);
}
