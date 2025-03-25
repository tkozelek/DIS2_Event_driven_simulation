package kozelek.gui.view.talbemodel;

import kozelek.entity.worker.Worker;

import javax.swing.*;
import java.util.List;
import java.util.function.Function;

public class WorkerTable extends Table<Worker> {

    public WorkerTable() {
        super(new String[]{"ID", "Work", "Pos", "WL%", "ks"},
                List.of(
                Worker::getId,
                Worker::getCurrentWork,
                Worker::getCurrentPosition,
                w -> String.format("%.2f", w.getStatisticWorkload().getMean() * 100),
                Worker::getFinishedTasks
        ));
    }
}
