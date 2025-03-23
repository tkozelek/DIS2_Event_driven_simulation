package kozelek.simulation;

import kozelek.config.CupboardComparator;
import kozelek.config.OrderActivityComparator;
import kozelek.entity.Workstation;
import kozelek.entity.worker.Worker;
import kozelek.entity.worker.WorkerGroup;
import kozelek.entity.worker.WorkerWork;
import kozelek.entity.order.Order;
import kozelek.entity.order.OrderType;
import kozelek.event.SystemEvent;
import kozelek.event.order.OrderArrivalEvent;
import kozelek.generator.Distribution;
import kozelek.generator.EnumGenerator;
import kozelek.generator.SeedGenerator;
import kozelek.generator.continuos.ContinuosEmpiricGenerator;
import kozelek.generator.continuos.ContinuosExponentialGenerator;
import kozelek.generator.continuos.ContinuosTriangularGenerator;
import kozelek.generator.continuos.ContinuosUniformGenerator;

import java.util.*;

public class Simulation extends SimulationCore {
    private int orderId = 0;
    private int workerId = 0;
    private int workstationId = 0;
    private int[] groups;

    public ArrayList<Order> finishedQueue;
    public PriorityQueue<Order> groupAQueue;
    public PriorityQueue<Order> groupBQueue;
    public PriorityQueue<Order> groupCQueue;
    public ArrayList<Workstation> workstations;
    public Worker[][] workers;

    private ContinuosExponentialGenerator orderArrivalGenerator;
    private ContinuosTriangularGenerator moveToStorageGenerator;
    private ContinuosTriangularGenerator materialPreparationGenerator;
    private ContinuosTriangularGenerator moveStationsGenerator;

    // stol
    private ContinuosEmpiricGenerator cuttingTableGenerator;
    private ContinuosUniformGenerator morenieLakovanieTableGenerator;
    private ContinuosUniformGenerator assemblyTableGenerator;

    // stolicka
    private ContinuosUniformGenerator cuttingChairGenerator;
    private ContinuosUniformGenerator morenieLakovanieChairGenerator;
    private ContinuosUniformGenerator assemblyChairGenerator;

    // cupboard
    private ContinuosUniformGenerator cuttingcupboardGenerator;
    private ContinuosUniformGenerator morenieLakovaniecupboardGenerator;
    private ContinuosUniformGenerator assemblycupboardGenerator;
    private ContinuosUniformGenerator fittingAssemblyGenerator;

    private EnumGenerator orderTypeGenerator;

    private Long seed;

    public Simulation(int numberOfReps, Long seed, int[] groups) {
        super(numberOfReps);
        this.seed = seed;

        this.groups = groups;
    }

    @Override
    public void replication() {
        while (!isEventCalendarEmpty()) {
            executeEvent();
        }
    }

    @Override
    public void beforeReplications() {
        SeedGenerator seedGenerator;
        if (seed != null) {
            seedGenerator = new SeedGenerator(seed);
        } else {
            seedGenerator = new SeedGenerator();
        }

        this.orderArrivalGenerator = new ContinuosExponentialGenerator(1 / 1800.0, seedGenerator);
        this.moveToStorageGenerator = new ContinuosTriangularGenerator(60, 480, 120, seedGenerator);
        this.materialPreparationGenerator = new ContinuosTriangularGenerator(300, 900, 500, seedGenerator);
        this.moveStationsGenerator = new ContinuosTriangularGenerator(120, 500, 150, seedGenerator);

        int times = 60;

        // stol
        this.cuttingTableGenerator = new ContinuosEmpiricGenerator(new Distribution[]{
                new Distribution(10 * times, 25 * times, 0.6),
                new Distribution(25 * times, 50 * times, 0.4)
        }, seedGenerator);
        this.morenieLakovanieTableGenerator = new ContinuosUniformGenerator(200 * times, 610 * times, seedGenerator);
        this.assemblyTableGenerator = new ContinuosUniformGenerator(30 * times, 60 * times, seedGenerator);

        // stolicka
        this.cuttingChairGenerator = new ContinuosUniformGenerator(12 * times, 16 * times, seedGenerator);
        this.morenieLakovanieChairGenerator = new ContinuosUniformGenerator(210 * times, 540 * times, seedGenerator);
        this.assemblyChairGenerator = new ContinuosUniformGenerator(14 * times, 24 * times, seedGenerator);

        // skrina
        this.cuttingcupboardGenerator = new ContinuosUniformGenerator(15 * times, 80 * times, seedGenerator);
        this.morenieLakovaniecupboardGenerator = new ContinuosUniformGenerator(35 * times, 75 * times, seedGenerator);
        this.assemblycupboardGenerator = new ContinuosUniformGenerator(35 * times, 75 * times, seedGenerator);
        this.fittingAssemblyGenerator = new ContinuosUniformGenerator(15 * times, 25 * times, seedGenerator);

        Map<OrderType, Double> probabilities = new HashMap<>();
        probabilities.put(OrderType.TABLE, 0.5);
        probabilities.put(OrderType.CHAIR, 0.15);
        probabilities.put(OrderType.CUPBOARD, 0.35);

        this.orderTypeGenerator = new EnumGenerator(probabilities, seedGenerator);
    }

    @Override
    public void beforeReplication() {
        workstationId = 0;
        workerId = 0;
        orderId = 0;
        finishedQueue = new ArrayList<>();
        groupAQueue = new PriorityQueue<>(new OrderActivityComparator());
        groupBQueue = new PriorityQueue<>(new OrderActivityComparator());
        groupCQueue = new PriorityQueue<>(new CupboardComparator());
        workstations = new ArrayList<>();

        int a = WorkerGroup.values().length;
        workers = new Worker[a][];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Worker[groups[i]];
            for (int j = 0; j < groups[i]; j++) {
                switch (i) {
                    case 0 -> workers[i][j] = new Worker(WorkerGroup.GROUP_A, this.getWorkerId());
                    case 1 -> workers[i][j] = new Worker(WorkerGroup.GROUP_B, this.getWorkerId());
                    case 2 -> workers[i][j] = new Worker(WorkerGroup.GROUP_C, this.getWorkerId());
                    default -> throw new IllegalArgumentException("Illegal order type");
                }
            }
        }

        // naplanovanie prveho prichodu
        this.resetTime();

        if (this.getSpeed() < 1000) {
            SystemEvent sysEvent = new SystemEvent(this, 0.0);
            addEvent(sysEvent);
        }

        double firstOrderTime = this.orderArrivalGenerator.sample();
        OrderArrivalEvent firstOrder = new OrderArrivalEvent(this, firstOrderTime);
        addEvent(firstOrder);
    }

    @Override
    public void afterReplication() {

    }

    @Override
    public void afterReplications() {

    }

    public int getOrderId() {
        return ++orderId;
    }

    public int getWorkerId() {
        return ++workerId;
    }

    public int getWorkstationId() {
        return ++workstationId;
    }

    public Worker getFreeWorkerFromGroup(WorkerGroup group) {
        for (Worker worker : workers[group.ordinal()]) {
            if (worker.getCurrentWork() == WorkerWork.IDLE) {
                return worker;
            }
        }
        return null;
    }

    public Workstation getFreeWorkstation() {
        List<Workstation> stations = workstations.stream()
                .filter(workstation -> workstation.getCurrentOrder() != null)
                .toList();

        if (!stations.isEmpty()) {
            return stations.getFirst();
        }

        Workstation newWorkstation = new Workstation(this.getWorkstationId());
        workstations.add(newWorkstation);
        return newWorkstation;
    }

    public void addToQueueA(Order order) {
        this.groupAQueue.add(order);
    }

    public void addToQueueB(Order order) {
        this.groupBQueue.add(order);
    }

    public void addToQueueC(Order order) {
        this.groupCQueue.add(order);
    }

    public Order pollFromQueueA() {
        return this.groupAQueue.poll();
    }

    public Order pollFromQueueB() {
        return this.groupBQueue.poll();
    }

    public Order pollFromQueueC() {
        return this.groupCQueue.poll();
    }

    public ContinuosExponentialGenerator getOrderArrivalGenerator() {
        return orderArrivalGenerator;
    }

    public ContinuosTriangularGenerator getMoveToStorageGenerator() {
        return moveToStorageGenerator;
    }

    public ContinuosTriangularGenerator getMaterialPreparationGenerator() {
        return materialPreparationGenerator;
    }

    public ContinuosTriangularGenerator getMoveStationsGenerator() {
        return moveStationsGenerator;
    }

    public ContinuosEmpiricGenerator getCuttingTableGenerator() {
        return cuttingTableGenerator;
    }

    public ContinuosUniformGenerator getMorenieLakovanieTableGenerator() {
        return morenieLakovanieTableGenerator;
    }

    public ContinuosUniformGenerator getAssemblyTableGenerator() {
        return assemblyTableGenerator;
    }

    public ContinuosUniformGenerator getCuttingChairGenerator() {
        return cuttingChairGenerator;
    }

    public ContinuosUniformGenerator getMorenieLakovanieChairGenerator() {
        return morenieLakovanieChairGenerator;
    }

    public ContinuosUniformGenerator getAssemblyChairGenerator() {
        return assemblyChairGenerator;
    }

    public ContinuosUniformGenerator getCuttingcupboardGenerator() {
        return cuttingcupboardGenerator;
    }

    public ContinuosUniformGenerator getMorenieLakovaniecupboardGenerator() {
        return morenieLakovaniecupboardGenerator;
    }

    public ContinuosUniformGenerator getAssemblycupboardGenerator() {
        return assemblycupboardGenerator;
    }

    public ContinuosUniformGenerator getFittingAssemblyGenerator() {
        return fittingAssemblyGenerator;
    }

    public EnumGenerator getOrderTypeGenerator() {
        return orderTypeGenerator;
    }

    public Order pollFromQueue(WorkerGroup workerGroup) {
        return switch (workerGroup) {
            case WorkerGroup.GROUP_A -> this.groupAQueue.poll();
            case WorkerGroup.GROUP_B -> this.groupBQueue.poll();
            case WorkerGroup.GROUP_C -> this.groupCQueue.poll();
            default -> throw new IllegalStateException("Unexpected value: " + workerGroup);
        };
    }
}
