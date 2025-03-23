package kozelek.event.groups;

import kozelek.config.Constants;
import kozelek.entity.Workstation;
import kozelek.entity.worker.Worker;
import kozelek.entity.worker.WorkerGroup;
import kozelek.entity.worker.WorkerPosition;
import kozelek.entity.order.Order;
import kozelek.entity.worker.WorkerWork;
import kozelek.event.Event;
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
        if (worker == null) {
            // Ak nie je voľný pracovník
            return;
        }

        // Vyber objednávku z frontu
        this.order = simulation.pollFromQueue(workerGroup);

        if (Constants.DEBUG)
            System.out.format("S: [%.2f] %s starts working on %s\n",
                    this.getTime(), worker, workerGroup, order);

        if (this.order == null)
            throw new IllegalStateException("[StartWork] No order available for group " + workerGroup);

        Workstation workstation;
        if (workerGroup == WorkerGroup.GROUP_A) {
            workstation = simulation.getFreeWorkstation();
            this.order.setWorkstation(workstation);
            workstation.setCurrentOrder(order);
        } else {
            workstation = order.getWorkstation();
        }

        worker.setCurrentOrder(order);
        order.setCurrentWorker(worker);

        switch (workerGroup) {
            case GROUP_A:
                if (worker.getCurrentPosition() != WorkerPosition.STORAGE) {
                    simulation.addEvent(new StartMoveEvent(simulation, this.getTime(), WorkerPosition.STORAGE, worker));
                } else {
                    simulation.addEvent(new StartMaterialPreparationEvent(simulation, this.getTime(), worker));
                }
                worker.setCurrentWork(WorkerWork.CUTTING);
                worker.setCurrentWorkstation(workstation);
                break;

            case GROUP_C:
                // je v sklade
                if (worker.getCurrentPosition() != WorkerPosition.MOUNTING_PLACE) {
                    simulation.addEvent(new StartMoveEvent(simulation, this.getTime(), WorkerPosition.MOUNTING_PLACE, worker));
                    worker.setCurrentWorkstation(workstation);
                } else {
                    if (worker.getCurrentWorkstation() != workstation) {
                        simulation.addEvent(new StartMovePlacesEvent(simulation, this.getTime(), worker, workstation));
                    } else {

                    }
                }
                break;

            default:
                throw new IllegalStateException("[StartWork] Unknown worker group!");
        }
    }
}
