package kozelek.entity;

import kozelek.config.OrderActivityComparator;
import kozelek.entity.carpenter.Worker;
import kozelek.entity.carpenter.WorkerGroup;
import kozelek.entity.order.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Workshop {
    public PriorityQueue<Order> orderQueue;
    public ArrayList<Workstation> workstations;
    public Worker[][] workers;

    public Workshop(int[] groups) {
        orderQueue = new PriorityQueue<>(new OrderActivityComparator());
        workstations = new ArrayList<>();
        workers = new Worker[WorkerGroup.values().length][];
        for (int i = 0; i < workers.length; i++) {
            for (int j = 0; j < groups[i]; j++) {
                switch (i) {
                    case 0 -> workers[i][j] = new Worker(WorkerGroup.GROUP_A);
                    case 1 -> workers[i][j] = new Worker(WorkerGroup.GROUP_B);
                    case 2 -> workers[i][j] = new Worker(WorkerGroup.GROUP_C);
                    default -> throw new IllegalArgumentException("Illegal order type");
                }
            }
        }
    }

    public void addOrderToQueue(Order order) {
        orderQueue.add(order);
    }

    public void addWorkstation(Workstation workstation) {
        workstations.add(workstation);
    }

    public Workstation getFreeWorkstation() {
        List<Workstation> stations = workstations.stream()
                .filter(workstation -> workstation.getCurrentOrder() == null)
                .toList();

        if (!stations.isEmpty()) {
            return stations.getFirst();
        }
        Workstation newWorkstation = new Workstation();
        workstations.add(newWorkstation);
        return newWorkstation;
    }
}
