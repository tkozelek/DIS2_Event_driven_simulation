package kozelek.gui.view.talbemodel;

import kozelek.statistic.DiscreteStatistic;

import java.util.List;

public class WorkerTotalTable extends Table<DiscreteStatistic> {
    public WorkerTotalTable() {
        super(new String[]{"Id", "WL%", "IS<B, T>"}, List.of(
                DiscreteStatistic::getName,
                w -> String.format("%.2f", w.getMean() * 100),
                w -> String.format("<%.2f%% | %.2f%%>", w.getConfidenceInterval()[0] * 100, w.getConfidenceInterval()[1] * 100)
        ));
    }
}
