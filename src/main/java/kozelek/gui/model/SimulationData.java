package kozelek.gui.model;

import kozelek.entity.Workstation;
import kozelek.entity.order.Order;
import kozelek.entity.worker.Worker;
import kozelek.statistic.ContinuousStatistic;
import kozelek.statistic.DiscreteStatistic;

import java.util.List;

public record SimulationData(
        Worker[][] workers,
        List<Workstation> workstations,
        List<Order> orders,
        int currentReplication,
        int[] queues,
        DiscreteStatistic[] orderTimeInSystem,
        DiscreteStatistic[] queueLengthTotal,
        ContinuousStatistic[] queueLengthReplication,
        DiscreteStatistic[][] workerWorkloadTotal, DiscreteStatistic[] workloadForGroupTotal,
        DiscreteStatistic orderNotWorkedOnTotal) { }
