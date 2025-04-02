package kozelek.gui.view;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Chart {
    private final JFreeChart chart;
    private final XYSeriesCollection dataset;
    private final String lineTitle;
    private final XYSeries seriesMain;
    private final XYSeries seriesBottom;
    private final XYSeries seriesTop;

    public Chart(String lineTitle, String title) {
        this.lineTitle = lineTitle;
        dataset = new XYSeriesCollection();

        seriesMain = new XYSeries(lineTitle);
        seriesBottom = new XYSeries(String.format("%s (spodny IS)", lineTitle));
        seriesTop = new XYSeries(String.format("%s (horny IS)", lineTitle));

        dataset.addSeries(seriesMain);
        dataset.addSeries(seriesBottom);
        dataset.addSeries(seriesTop);

        chart = ChartFactory.createXYLineChart(
                title,
                "Replikácie",
                "Priemerny čas",
                dataset
        );
        chart.getXYPlot().getRenderer().setSeriesPaint(0, java.awt.Color.RED);
        chart.getXYPlot().getRenderer().setSeriesPaint(1, java.awt.Color.BLUE);
        chart.getXYPlot().getRenderer().setSeriesPaint(2, java.awt.Color.BLUE);

        chart.getXYPlot().getRangeAxis().setAutoRange(false);
    }

    public JFreeChart getChart() {
        return chart;
    }

    public void updateRange(double offset) {
        double min = seriesBottom.getMinY();
        double max = seriesTop.getMaxY();
        double range = max - min;
        if (range > 0)
            chart.getXYPlot().getRangeAxis().setRange(min - (range * offset), max + (range * offset));
    }

    public void resetChart() {
        dataset.removeAllSeries();

        seriesMain.clear();
        seriesBottom.clear();
        seriesTop.clear();

        dataset.addSeries(seriesMain);
        dataset.addSeries(seriesBottom);
        dataset.addSeries(seriesTop);

        chart.getXYPlot().getRangeAxis().setAutoRange(false);
    }
}
