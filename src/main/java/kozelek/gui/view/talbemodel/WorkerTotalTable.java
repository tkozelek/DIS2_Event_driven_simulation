package kozelek.gui.view.talbemodel;

import kozelek.entity.worker.Worker;
import kozelek.statistic.DiscreteStatistic;

import java.util.List;
import java.util.function.Function;

public class WorkerTotalTable extends Table<DiscreteStatistic> {
    public WorkerTotalTable() {
        super(new String[]{"Id", "WL%"}, List.of(
                DiscreteStatistic::getName,
                w -> String.format("%.2f", w.getMean() * 100)
        ));
    }
}
