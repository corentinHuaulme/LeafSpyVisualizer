package Controller;

import View.PanelChart;
import org.jfree.chart.*;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.Dataset;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

/**
 * An {@link ActionListener} that change the datasets displayed on the charts.
 * Refreshes the {@link PlotChangedListenerCustom} linked to the charts.
 */
public class ChartChooserListener implements ActionListener {

    /** The {@link JCheckBox} being listened **/
    private JCheckBox chooser;
    /** The {@link} ChartPanel linked to the {@link JCheckBox} **/
    private ChartPanel cp;
    /** The {@link PanelChart} needed to retrieve the datasets**/
    private PanelChart pc;
    /** The {@link ChartPanel} of the other chart used to refresh the {@link PlotChangedListenerCustom}. **/
    private ChartPanel otherChart;
    /** The {@link PlotChangedListenerCustom} linked to the second chart. Used to remove obsolete listener **/
    private PlotChangedListenerCustom listenerOther;
    /** The {@link PlotChangedListenerCustom} linked to the first chart. Used to remove obsolete listener **/
    private PlotChangedListenerCustom listener;

    /**
     * Default constructor of the ChartChooserListener
     * @param chooser The {@link JCheckBox} being listened.
     * @param cp The {@link ChartPanel} linked to the {@link JCheckBox}.
     * @param pc The {@link PanelChart} needed to retrieve the datasets.
     * @param otherChart The {@link ChartPanel} of the other chart used to refresh the {@link PlotChangedListenerCustom}.
     * @param listener The {@link PlotChangedListenerCustom} linked to the second chart. Used to remove obsolete listener.
     * @param listenerOther The {@link PlotChangedListenerCustom} linked to the first chart. Used to remove obsolete listener.
     */
    public ChartChooserListener(JCheckBox chooser, ChartPanel cp, PanelChart pc, ChartPanel otherChart, PlotChangedListenerCustom listener, PlotChangedListenerCustom listenerOther){
        this.chooser = chooser;
        this.cp = cp;
        this.pc = pc;
        this.otherChart = otherChart;
        this.listenerOther = listenerOther;
        this.listener = listener;
    }

    /**
     * Method called upon a change made to the {@link JCheckBox}
     * @param e The {@link ActionEvent} being fired.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch(chooser.getText()){
            case "SOC" :
                performChange("Percentage of the Battery", pc.getDatasetCharge());
                break;
            case "Speed" :
                performChange("Speed of the Car", pc.getDatasetSpeed());
                break;
            case "Battery Temperature" :
                performChange("Temperature of the Battery", pc.getDatasetTemperature());
                break;
            case "Battery Capacity" :
                performChange("Capacity of the Battery", pc.getDatasetCapacity());
                break;
            case "Motor Power" :
                performChange("Motor Power", pc.getDatasetMotorPower());
                break;
            case "SOH":
                performChange("State of Health", pc.getDatasetSOH());
                break;
            case "Quick Charge":
                performChange("Quick Charges", pc.getDatasetQuickCharge());
                break;
        }
        XYPlot p = cp.getChart().getXYPlot();
        DateAxis da = (DateAxis) p.getDomainAxis();
        String dateF= "dd/MM HH:mm";
        da.setDateFormatOverride(new SimpleDateFormat(dateF));
    }

    /**
     *  Builds a {@link JFreeChart} without the dataset that is to be removed.
     * @param plot The former {@link XYPlot} needed to retrieve the datasets and the axis
     * @param datasetToRemove The {@link XYDataset} to be removed from the chart
     * @return The new {@link JFreeChart} without the unwanted dataset
     */
    public JFreeChart buildChart(XYPlot plot, XYDataset datasetToRemove) {

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "",
                "Time",
                "",
                null,
                true,
                false,
                false);
        LegendItemCollection legends = new LegendItemCollection();
        Paint[] cs = ChartColor.createDefaultPaintArray();
        for (int i = 0; i < plot.getDatasetCount(); i++) {
            if(plot.getDataset(i) != null && !plot.getDataset(i).equals(datasetToRemove)){


                final NumberAxis axis2 = new NumberAxis(plot.getRangeAxis(i).getLabel());
                //axis2.setAutoRangeIncludesZero(false);
                LegendItem legend = new LegendItem(axis2.getLabel());

                XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
                renderer.setDefaultShapesVisible(false);

                if(chart.getXYPlot().getRangeAxis().getLabel().equals("")){
                    chart.getXYPlot().setRangeAxis(axis2);
                    chart.getXYPlot().setDataset(0, plot.getDataset(i));
                    for( int j = 0 ; j < plot.getDataset(0).getSeriesCount() ; j++) {
                        renderer.setSeriesPaint(j, cs[chart.getXYPlot().getDatasetCount()-1]);
                    }
                    chart.getXYPlot().setRenderer(0,renderer);
                }else {
                    chart.getXYPlot().setRangeAxis(chart.getXYPlot().getRangeAxisCount(), axis2);
                    chart.getXYPlot().setDataset(chart.getXYPlot().getDatasetCount(), plot.getDataset(i));
                    for( int j = 0 ; j < plot.getDataset(0).getSeriesCount() ; j++) {
                        renderer.setSeriesPaint(j, cs[chart.getXYPlot().getDatasetCount() - 1]);
                    }
                    chart.getXYPlot().setRenderer(chart.getXYPlot().getDatasetCount()-1,renderer);
                }

                legend.setDataset(chart.getXYPlot().getDataset(i));
                legend.setFillPaint(cs[chart.getXYPlot().getDatasetCount()-1]);
                legend.setLinePaint(cs[chart.getXYPlot().getDatasetCount()-1]);
                legends.add(legend);
            }
        }


        chart.getXYPlot().removeChangeListener(this.listener);
        this.listener = new PlotChangedListenerCustom(chart,this.otherChart.getChart());
        chart.getXYPlot().addChangeListener(this.listener);
        this.otherChart.getChart().getXYPlot().removeChangeListener(this.listenerOther);
        this.listenerOther = new PlotChangedListenerCustom(this.otherChart.getChart(),chart);
        this.otherChart.getChart().getXYPlot().addChangeListener(this.listenerOther);
        chart.getXYPlot().setFixedLegendItems(legends);
        return chart;
    }

    /**
     * Perform changes to the chart according to the event
     * If the {@link JCheckBox} is unchecked, the corresponding dataset is to be removed. The {@link JFreeChart} has to be rebuilt in order to remove a dataset from a chart.
     * If the {@link JCheckBox} is checked, the dataset is added to the chart and an entry is added in the legend box.
     * @param rangeAxisLabel The label of the dataset to be added or removed.
     * @param dataset The dataset to be added or removed.
     */
    public void performChange(String rangeAxisLabel, Dataset dataset){
        final XYPlot plot = cp.getChart().getXYPlot();
        final NumberAxis axis2 = new NumberAxis(rangeAxisLabel);
        axis2.setAutoRangeIncludesZero(false);

        LegendItemCollection legends = new LegendItemCollection();

        if(!chooser.isSelected() && plot.getDatasetCount() > 1){
            for(int i = 0 ; i < plot.getRangeAxisCount() ; i++){
                if(plot.getRangeAxis(i).getLabel().equals(rangeAxisLabel)){
                    plot.getRangeAxis(i).setVisible(false);
                    cp.setChart(null);
                    cp.setChart(buildChart(plot, plot.getDataset(i)));
                }else {
                    legends.add(plot.getFixedLegendItems().get(i));
                }
            }
        }else if(chooser.isSelected()){

            XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

            legends = plot.getLegendItems();
            LegendItem legend = new LegendItem(rangeAxisLabel);
            plot.setRangeAxis(plot.getRangeAxisCount(), axis2);
            plot.setDataset(plot.getDatasetCount(), (XYDataset) dataset);

            legend.setDataset(dataset);

            Paint[] cs = ChartColor.createDefaultPaintArray();
            for( int i = 0 ; i < plot.getDataset(plot.getDatasetCount()-1).getSeriesCount() ; i++){
                renderer.setSeriesPaint(i,cs[cp.getChart().getXYPlot().getDatasetCount()-1]);
            }

            renderer.setDefaultShapesVisible(false);
            plot.setRenderer(plot.getDatasetCount()-1,renderer);


            legend.setFillPaint(cs[plot.getDatasetCount()-1]);
            legend.setLinePaint(cs[plot.getDatasetCount()-1]);

            legends.add(legend);
            //legend.setLinePaint(cs[plot.getLegendItems().getItemCount()]);
            plot.setFixedLegendItems(legends);

            plot.mapDatasetToRangeAxis(plot.getDatasetCount() - 1, plot.getRangeAxisCount() - 1);
        }else{
            chooser.setSelected(true);
        }
    }
}
