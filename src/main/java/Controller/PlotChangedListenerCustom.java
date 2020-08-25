package Controller;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.event.PlotChangeEvent;
import org.jfree.chart.event.PlotChangeListener;

/**
 * A {@link PlotChangeListener} that links two charts together.
 * This listener is made to zoom the two charts when one is being zoomed in by the user.
 */
public class PlotChangedListenerCustom implements PlotChangeListener {

    /** The {@link JFreeChart} containing the {@link org.jfree.chart.plot.XYPlot} being listened **/
    private JFreeChart firstChart;
    /** The {@link JFreeChart} to be zoomed in. **/
    private JFreeChart secondChart;
    /** The upper bound of the Domain (X) Axis of the firstChart **/
    private double upperFirstChart ;
    /** The lower bound of the Domain (X) Axis of the firstChart **/
    private double lowerFirstChart;
    /** The lower bound of the Domain (X) Axis of the secondChart **/
    private double lowerSecondChart;
    /** The upper bound of the Domain (X) Axis of the secondChart **/
    private double upperSecondChart;

    /**
     * Creates a PlotChangedListenerCustom.
     * @param firstChart The {@link JFreeChart} containing the {@link org.jfree.chart.plot.XYPlot} being listened.
     * @param secondChart The {@link JFreeChart} to be zoomed in.
     */
    public PlotChangedListenerCustom(JFreeChart firstChart, JFreeChart secondChart){
        this.firstChart = firstChart;
        this.secondChart = secondChart;
        this.upperFirstChart = firstChart.getXYPlot().getDomainAxis().getUpperBound();
        this.lowerFirstChart = firstChart.getXYPlot().getDomainAxis().getLowerBound();
        this.lowerSecondChart = secondChart.getXYPlot().getDomainAxis().getLowerBound();
        this.upperSecondChart = secondChart.getXYPlot().getDomainAxis().getUpperBound();
    }

    /**
     * Method being called on a change to the {@link org.jfree.chart.plot.XYPlot}.
     * Zoom the secondChart if the firstChart has been zoomed.
     * @param event The event being fired by the {@link org.jfree.chart.plot.XYPlot}
     */
    @Override
    public void plotChanged(PlotChangeEvent event) {

        if(upperFirstChart != firstChart.getXYPlot().getDomainAxis().getUpperBound() && lowerFirstChart != firstChart.getXYPlot().getDomainAxis().getLowerBound()) {
            upperFirstChart = firstChart.getXYPlot().getDomainAxis().getUpperBound();
            lowerFirstChart = firstChart.getXYPlot().getDomainAxis().getLowerBound();
            try {
                secondChart.getXYPlot().getDomainAxis().setLowerBound(lowerFirstChart);
                secondChart.getXYPlot().getDomainAxis().setUpperBound(upperFirstChart);
            } catch (Exception e) {
                System.out.print(e);
            }
        }
    }
}
