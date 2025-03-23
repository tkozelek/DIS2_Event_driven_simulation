package kozelek.simulation;

import kozelek.entity.Workshop;
import kozelek.entity.Workstation;
import kozelek.entity.carpenter.Worker;
import kozelek.entity.carpenter.WorkerGroup;
import kozelek.entity.carpenter.WorkerWork;
import kozelek.entity.order.OrderType;
import kozelek.generator.Distribution;
import kozelek.generator.EnumGenerator;
import kozelek.generator.SeedGenerator;
import kozelek.generator.continuos.ContinuosEmpiricGenerator;
import kozelek.generator.continuos.ContinuosExponentialGenerator;
import kozelek.generator.continuos.ContinuosTriangularGenerator;
import kozelek.generator.continuos.ContinuosUniformGenerator;

import java.util.HashMap;
import java.util.Map;

public class Simulation extends SimulationCore {
    private int orderId = 0;
    private Workshop workshop;

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

    private EnumGenerator orderTypeGenerator;

    private Long seed;

    public Simulation(int numberOfReps, Long seed, int[] groups) {
        super(numberOfReps);
        this.seed = seed;

        this.workshop = new Workshop(groups);

        this.workers = new Worker[WorkerGroup.values().length][];
    }

    @Override
    public void replication() {

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

        // stol
        this.cuttingTableGenerator = new ContinuosEmpiricGenerator(new Distribution[]{
                new Distribution(10, 25, 0.6),
                new Distribution(25, 50, 0.4)
        }, seedGenerator);
        this.morenieLakovanieTableGenerator = new ContinuosUniformGenerator(200, 610, seedGenerator);
        this.assemblyTableGenerator = new ContinuosUniformGenerator(30, 60, seedGenerator);

        // stolicka
        this.cuttingChairGenerator = new ContinuosUniformGenerator(12, 16, seedGenerator);
        this.morenieLakovanieChairGenerator = new ContinuosUniformGenerator(210, 540, seedGenerator);
        this.assemblyChairGenerator = new ContinuosUniformGenerator(14, 24, seedGenerator);

        // skrina
        this.cuttingcupboardGenerator = new ContinuosUniformGenerator(15, 80, seedGenerator);
        this.morenieLakovaniecupboardGenerator = new ContinuosUniformGenerator(35, 75, seedGenerator);
        this.assemblycupboardGenerator = new ContinuosUniformGenerator(35, 75, seedGenerator);
        this.fittingAssemblyGenerator = new ContinuosUniformGenerator(15, 25, seedGenerator);

        Map<OrderType, Double> probabilities = new HashMap<>();
        probabilities.put(OrderType.TABLE, 0.5);
        probabilities.put(OrderType.CHAIR, 0.15);
        probabilities.put(OrderType.CUPBOARD, 0.35);

        this.orderTypeGenerator = new EnumGenerator(probabilities);
    }

    @Override
    public void beforeReplication() {

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

    public Worker getFreeCarpenterFromGroup(WorkerGroup group) {
        for (Worker worker : workers[group.ordinal()]) {
            if (worker.getCurrentWork() == WorkerWork.IDLE) {
                return worker;
            }
        }
        return null;
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
}
