package kozelek.gui.view.talbemodel;

import kozelek.statistic.DiscreteStatistic;

import java.util.List;

public class WorkerTotalTable extends Table<DiscreteStatistic> {
    public WorkerTotalTable() {
        super(new String[]{"Id", "WL%"}, List.of(
                DiscreteStatistic::getName,
                w -> String.format("%.2f", w.getMean() * 100)
        ));
    }
}
