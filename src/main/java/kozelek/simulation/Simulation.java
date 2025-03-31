package kozelek.simulation;

import kozelek.config.Constants;
import kozelek.config.CupboardComparator;
import kozelek.config.OrderActivityComparator;
import kozelek.entity.Workstation;
import kozelek.entity.order.Order;
import kozelek.entity.order.OrderType;
import kozelek.entity.worker.Worker;
import kozelek.entity.worker.WorkerGroup;
import kozelek.entity.worker.WorkerPosition;
import kozelek.entity.worker.WorkerWork;
import kozelek.event.SystemEvent;
import kozelek.event.order.OrderArrivalEvent;
import kozelek.generator.Distribution;
import kozelek.generator.EnumGenerator;
import kozelek.generator.SeedGenerator;
import kozelek.generator.continuos.ContinuosEmpiricGenerator;
import kozelek.generator.continuos.ContinuosExponentialGenerator;
import kozelek.generator.continuos.ContinuosTriangularGenerator;
import kozelek.generator.continuos.ContinuosUniformGenerator;
import kozelek.gui.interfaces.Observable;
import kozelek.gui.interfaces.Observer;
import kozelek.gui.model.SimulationData;
import kozelek.statistic.ContinuousStatistic;
import kozelek.statistic.DiscreteStatistic;

import java.util.*;

public class Simulation extends SimulationCore implements Observable {
    private final ArrayList<Observer> observers;
    private boolean updateChart = false;

    private int orderId = 0;
    private int workerId = 0;
    private int workstationId = 0;
    private final int[] groups;

    private ArrayList<Order> finishedQueue;
    private ArrayList<Order> orders;
    private PriorityQueue<Order> groupAQueue;
    private PriorityQueue<Order> groupBQueue;
    private PriorityQueue<Order> groupCQueue;
    private ArrayList<Workstation> workstations;
    private Worker[][] workers;

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

    private DiscreteStatistic orderTimeInSystemReplication;
    private DiscreteStatistic orderTimeInSystemTotal;

    private DiscreteStatistic orderWorkerAInSystemTotal;
    private DiscreteStatistic orderWorkerBInSystemTotal;
    private DiscreteStatistic orderWorkerCPaintingInSystemTotal;
    private DiscreteStatistic orderWorkerCAseemblyInSystemTotal;

    private DiscreteStatistic orderNotWorkedOnTotal;

    private DiscreteStatistic queueLengthGroupATotal;
    private DiscreteStatistic queueLengthGroupBTotal;
    private DiscreteStatistic queueLengthGroupCTotal;

    private ContinuousStatistic queueLengthGroupA;
    private ContinuousStatistic queueLengthGroupB;
    private ContinuousStatistic queueLengthGroupC;

    private DiscreteStatistic[][] workerWorkloadTotal;
    private DiscreteStatistic[] workloadForGroupTotal;

    private EnumGenerator orderTypeGenerator;

    private final Long seed;

    public Simulation(int numberOfReps, Long seed, int[] groups) {
        super(numberOfReps);
        this.seed = seed;

        this.groups = groups;
        this.observers = new ArrayList<>();
    }

    @Override
    public void replication() {
        while (!isEventCalendarEmpty() && !stopped) {
            executeEvent();

            if (this.getSpeed() < Constants.MAX_SPEED)
                this.notifyObservers();
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

        // 2 za hodinu, 60 * 60 = 3600 -> 2 / 3600
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
        this.morenieLakovaniecupboardGenerator = new ContinuosUniformGenerator(600 * times, 700 * times, seedGenerator);
        this.assemblycupboardGenerator = new ContinuosUniformGenerator(35 * times, 75 * times, seedGenerator);
        this.fittingAssemblyGenerator = new ContinuosUniformGenerator(15 * times, 25 * times, seedGenerator);

        Map<OrderType, Double> probabilities = new HashMap<>();
        probabilities.put(OrderType.TABLE, 0.5);
        probabilities.put(OrderType.CHAIR, 0.15);
        probabilities.put(OrderType.CUPBOARD, 0.35);

        this.orderTypeGenerator = new EnumGenerator(probabilities, seedGenerator);

        orderTimeInSystemTotal = new DiscreteStatistic("Order time in system total");
        orderTimeInSystemReplication = new DiscreteStatistic("Order time in system replications");

        orderWorkerAInSystemTotal = new DiscreteStatistic("Order worker A in system total");
        orderWorkerBInSystemTotal = new DiscreteStatistic("Order worker B in system total");
        orderWorkerCPaintingInSystemTotal = new DiscreteStatistic("Order worker C painting in system total");
        orderWorkerCAseemblyInSystemTotal = new DiscreteStatistic("Order worker C assembly in system total");

        orderNotWorkedOnTotal = new DiscreteStatistic("Order not worked on total");

        queueLengthGroupATotal = new DiscreteStatistic("Queue length group A");
        queueLengthGroupBTotal = new DiscreteStatistic("Queue length group B");
        queueLengthGroupCTotal = new DiscreteStatistic("Queue length group C");

        queueLengthGroupA = new ContinuousStatistic("Queue A length orders");
        queueLengthGroupB = new ContinuousStatistic("Queue B length orders");
        queueLengthGroupC = new ContinuousStatistic("Queue C length orders");

        workloadForGroupTotal = new DiscreteStatistic[WorkerGroup.values().length];
        for (int i = 0; i < workloadForGroupTotal.length; i++) {
            workloadForGroupTotal[i] = new DiscreteStatistic(String.format("Workload for group %c", i + 'A'));
        }

        workerWorkloadTotal = new DiscreteStatistic[WorkerGroup.values().length][];
        for (int i = 0; i < workerWorkloadTotal.length; i++) {
            workerWorkloadTotal[i] = new DiscreteStatistic[groups[i]];
            for (int j = 0; j < workerWorkloadTotal[i].length; j++) {
                workerWorkloadTotal[i][j] = new DiscreteStatistic(String.format("%d", i + j + 1));
            }
        }

        this.orders = new ArrayList<>();
    }

    @Override
    public void beforeReplication() {
        workstationId = 0;
        workerId = 0;
        orderId = 0;
        finishedQueue = new ArrayList<>();
        groupAQueue = new PriorityQueue<>(new OrderActivityComparator());
        groupBQueue = new PriorityQueue<>(new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                return Double.compare(o1.getFinishPaintingTime(), o2.getFinishPaintingTime());
            }
        });
        groupCQueue = new PriorityQueue<>(new CupboardComparator());
        orders = new ArrayList<>();
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

        if (this.getSpeed() < Constants.MAX_SPEED) {
            SystemEvent sysEvent = new SystemEvent(this, 0.0);
            addEvent(sysEvent);
        }

        double firstOrderTime = this.orderArrivalGenerator.sample();
        OrderArrivalEvent firstOrder = new OrderArrivalEvent(this, firstOrderTime);
        addEvent(firstOrder);

        this.notifyObservers();
    }

    @Override
    public void afterReplication() {
        this.orderTimeInSystemTotal.addValue(this.orderTimeInSystemReplication.getMean());
        this.queueLengthGroupATotal.addValue(this.queueLengthGroupA.getMean());
        this.queueLengthGroupBTotal.addValue(this.queueLengthGroupB.getMean());
        this.queueLengthGroupCTotal.addValue(this.queueLengthGroupC.getMean());

        for (int i = 0; i < workers.length; i++) {
            for (int j = 0; j < workers[i].length; j++) {
                workerWorkloadTotal[i][j].addValue(workers[i][j].getStatisticWorkload().getMean());
            }
        }
        for (int i = 0; i < workers.length; i++) {
            workloadForGroupTotal[i].addValue(Arrays.stream(workers[i])
                    .mapToDouble(w -> w.getStatisticWorkload().getMean())
                    .average().getAsDouble());
        }

        orderNotWorkedOnTotal.addValue(getGroupAQueueSize());

        this.updateChart = true;
        this.notifyObservers();
        this.updateChart = false;

        this.queueLengthGroupA.clear();
        this.queueLengthGroupB.clear();
        this.queueLengthGroupC.clear();
        this.orderTimeInSystemReplication.clear();
    }

    @Override
    public void afterReplications() {
        System.out.println(this.queueLengthGroupATotal);
        System.out.println(this.queueLengthGroupBTotal);
        System.out.println(this.queueLengthGroupCTotal);
        System.out.println(this.orderNotWorkedOnTotal);
        for (DiscreteStatistic ds : workloadForGroupTotal) {
            System.out.println(ds);
        }
        for (DiscreteStatistic[] dss : workerWorkloadTotal) {
            for (DiscreteStatistic ds : dss) {
                System.out.println(ds);
            }
        }
        System.out.println(this.orderTimeInSystemTotal);
        System.out.printf("%s: %.2f hours\n", this.orderTimeInSystemTotal.getName(), this.orderTimeInSystemTotal.getMean() / 60 / 60);
        System.out.println(orderWorkerAInSystemTotal);
        System.out.println(orderWorkerBInSystemTotal);
        System.out.println(orderWorkerCPaintingInSystemTotal);
        System.out.println(orderWorkerCAseemblyInSystemTotal);
    }

    public void addOrder(Order order) {
        this.orders.add(order);
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
            if (worker.getCurrentWork() == WorkerWork.IDLE && worker.getCurrentPosition() != WorkerPosition.MOVING) {
                return worker;
            }
        }
        return null;
    }

    public Workstation getFreeWorkstation() {
        Optional<Workstation> freeWorkstation = workstations.stream()
                .filter(workstation -> workstation.getCurrentOrder() == null)
                .findFirst();

        if (freeWorkstation.isPresent()) {
            return freeWorkstation.get();
        }

        Workstation newWorkstation = new Workstation(this.getWorkstationId());
        workstations.add(newWorkstation);
        return newWorkstation;
    }

    public void addToQueueA(Order order, double time) {
        this.groupAQueue.add(order);
        this.queueLengthGroupA.addValue(time, groupAQueue.size());
    }

    public void addToQueueB(Order order, double time) {
        this.groupBQueue.add(order);
        this.queueLengthGroupB.addValue(time, groupBQueue.size());

    }

    public void addToQueueC(Order order, double time) {
        this.groupCQueue.add(order);
        this.queueLengthGroupC.addValue(time, groupCQueue.size());
    }

    public Order pollFromQueue(WorkerGroup workerGroup, double time) {
        Order order;
        switch (workerGroup) {
            case WorkerGroup.GROUP_A:
                order = this.groupAQueue.poll();
                this.queueLengthGroupA.addValue(time, groupAQueue.size());
                break;
            case WorkerGroup.GROUP_B:
                order = this.groupBQueue.poll();
                this.queueLengthGroupB.addValue(time, groupBQueue.size());
                break;
            case WorkerGroup.GROUP_C:
                order = this.groupCQueue.poll();
                this.queueLengthGroupC.addValue(time, groupCQueue.size());
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + workerGroup);
        }
        return order;
    }

    public int getGroupAQueueSize() {
        return groupAQueue.size();
    }

    public int getGroupBQueueSize() {
        return groupBQueue.size();
    }

    public int getGroupCQueueSize() {
        return groupCQueue.size();
    }

    public void addToFinished(Order order) {
        this.finishedQueue.add(order);
        this.orderTimeInSystemReplication.addValue(order.getFinishTime() - order.getArrivalTime());
        orderWorkerAInSystemTotal.addValue(order.getFinishCuttingTime() - order.getStartCuttingTime());
        orderWorkerBInSystemTotal.addValue(order.getFinishAssemblyTime() - order.getStartAssemblyTime());
        orderWorkerCPaintingInSystemTotal.addValue(order.getFinishPaintingTime() - order.getStartPaintingTime());
        if (order.getOrderType() == OrderType.CUPBOARD)
            orderWorkerCAseemblyInSystemTotal.addValue(order.getFinishFittingAssemblyTime() - order.getStartFittingAssemblyTime());
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

    public ContinuosUniformGenerator getCuttingCupboardGenerator() {
        return cuttingcupboardGenerator;
    }

    public ContinuosUniformGenerator getMorenieLakovanieCupboardGenerator() {
        return morenieLakovaniecupboardGenerator;
    }

    public ContinuosUniformGenerator getAssemblyCupboardGenerator() {
        return assemblycupboardGenerator;
    }

    public ContinuosUniformGenerator getFittingAssemblyGenerator() {
        return fittingAssemblyGenerator;
    }

    public EnumGenerator getOrderTypeGenerator() {
        return orderTypeGenerator;
    }

    @Override
    public void addObserver(Observer o) {
        this.observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        this.observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this.getSimulationData());
        }
    }

    public SimulationData getSimulationData() {
        return new SimulationData(
                getCurrentTime(),
                workers,
                workstations,
                orders,
                getCurrentRep(),
                new int[]{getGroupAQueueSize(), getGroupBQueueSize(), getGroupCQueueSize()},
                new DiscreteStatistic[]{orderTimeInSystemReplication, orderTimeInSystemTotal},
                new DiscreteStatistic[]{queueLengthGroupATotal, queueLengthGroupBTotal, queueLengthGroupCTotal},
                new ContinuousStatistic[]{queueLengthGroupA, queueLengthGroupB, queueLengthGroupC},
                getCurrentRep() > 0 ? workerWorkloadTotal : null,
                getCurrentRep() > 0 ? workloadForGroupTotal : null,
                getCurrentRep() > 0 ? orderNotWorkedOnTotal : null,
                updateChart);

    }
}
