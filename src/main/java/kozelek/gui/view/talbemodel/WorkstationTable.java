package kozelek.gui.view.talbemodel;

import kozelek.entity.Workstation;
import kozelek.entity.worker.Worker;

import java.util.List;

public class WorkstationTable extends Table<Workstation> {

    public WorkstationTable() {
        super(new String[]{"ID", "Order", "Worker"},
                List.of(
                        Workstation::getId,
                        Workstation::getCurrentOrder,
                        w -> (w.getCurrentOrder() != null ? w.getCurrentOrder().getCurrentWorker() : "")
                ));
    }
}
