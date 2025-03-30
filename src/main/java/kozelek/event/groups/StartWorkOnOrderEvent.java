package kozelek.event.groups;

import kozelek.config.Constants;
import kozelek.entity.Workstation;
import kozelek.entity.order.OrderActivity;
import kozelek.entity.order.OrderType;
import kozelek.entity.worker.Worker;
import kozelek.entity.worker.WorkerGroup;
import kozelek.entity.worker.WorkerPosition;
import kozelek.entity.order.Order;
import kozelek.entity.worker.WorkerWork;
import kozelek.event.Event;
import kozelek.event.groups.groupb.StartAssemblyEvent;
import kozelek.event.groups.groupc.fitting.StartFittingAssemblyEvent;
import kozelek.event.groups.groupc.painting.StartPaintingEvent;
import kozelek.event.move.StartMoveEvent;
import kozelek.event.move.StartMovePlacesEvent;
import kozelek.event.storage.StartMaterialPreparationEvent;
import kozelek.simulation.Simulation;
import kozelek.simulation.SimulationCore;

public class StartWorkOnOrderEvent extends Event {
    private final WorkerGroup workerGroup;
    private Order order;

    public StartWorkOnOrderEvent(SimulationCore simulationCore, double time, WorkerGroup workerGroup) {
        super(simulationCore, time);
        this.workerGroup = workerGroup;
    }

    @Override
    public void execute() {
        Simulation simulation = (Simulation) getSimulationCore();

        Worker worker = simulation.getFreeWorkerFromGroup(workerGroup);
        if (worker == null)
            return;

        this.order = simulation.pollFromQueue(workerGroup, time);
        if (this.order == null)
            throw new IllegalStateException("[StartWork] No order available for group " + workerGroup);

        logDebug(worker);

        Workstation workstation = assignWorkstation(simulation);
        worker.setCurrentOrder(order);
        order.setCurrentWorker(worker);

        switch (workerGroup) {
            case GROUP_A -> handleGroupA(simulation, worker, workstation);
            case GROUP_B -> handleGroupB(simulation, worker, workstation);
            case GROUP_C -> handleGroupC(simulation, worker, workstation);
        }
    }

    private void handleGroupA(Simulation simulation, Worker worker, Workstation workstation) {
        if (worker.getCurrentPosition() != WorkerPosition.STORAGE) {
            simulation.addEvent(new StartMoveEvent(simulation, this.getTime(), WorkerPosition.STORAGE, worker));
        } else {
            simulation.addEvent(new StartMaterialPreparationEvent(simulation, this.getTime(), worker));
        }
        worker.setCurrentWork(WorkerWork.CUTTING, time);
        worker.setCurrentWorkstation(workstation);
    }

    private void handleGroupB(Simulation simulation, Worker worker, Workstation workstation) {
        boolean move = checkPositionAndCreateEvent(simulation, worker, workstation);
        if (move)
            return;
        simulation.addEvent(new StartAssemblyEvent(simulation, this.getTime(), worker));
    }

    private void handleGroupC(Simulation simulation, Worker worker, Workstation workstation) {
        boolean move = checkPositionAndCreateEvent(simulation, worker, workstation);
        if (move)
            return;

        if (order.getOrderType() == OrderType.CUPBOARD && order.getOrderActivity() == OrderActivity.Assembled) {
            simulation.addEvent(new StartFittingAssemblyEvent(simulation, this.getTime(), worker));
        } else {
            simulation.addEvent(new StartPaintingEvent(simulation, this.getTime(), worker));
        }
    }

    private boolean checkPositionAndCreateEvent(Simulation simulation, Worker worker, Workstation workstation) {
        if (worker.getCurrentPosition() == WorkerPosition.STORAGE) {
            simulation.addEvent(new StartMoveEvent(simulation, this.getTime(), WorkerPosition.WORKSTATION, worker));
            worker.setCurrentWorkstation(workstation);
            return true;
        }

        if (worker.getCurrentWorkstation() != workstation) {
            simulation.addEvent(new StartMovePlacesEvent(simulation, this.getTime(), worker, workstation));
            return true;
        }
        return false;
    }

    private Workstation assignWorkstation(Simulation simulation) {
        if (workerGroup == WorkerGroup.GROUP_A) {
            Workstation workstation = simulation.getFreeWorkstation();
            order.setWorkstation(workstation);
            workstation.setCurrentOrder(order);
            return workstation;
        }
        return order.getWorkstation();
    }

    private void logDebug(Worker worker) {
        if (Constants.DEBUG) {
            System.out.format("S: [%.2f] %s starts working on %s\n", this.getTime(), worker, order);
        }
    }
}
