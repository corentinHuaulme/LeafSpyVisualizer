package View;

import Model.CarData;
import org.jfree.chart.*;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.SeriesException;
import org.jfree.data.time.*;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.xml.crypto.Data;
import java.awt.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;


/**
 * A {@link PanelChart} creating datasets made for a line chart.
 * This class should be used to create a dataset made for a Time series Chart. It also generate a chart with a dataset generated by another method from this class
 */
public class PanelChartLine extends PanelChart{

    /**
     * Constructor of a PanelChartLine
     * Only calls the constructor of {@link PanelChart}
     * @param cd The data to be processed
     */
    public PanelChartLine(ArrayList<ArrayList<CarData>> cd) {
        super(cd);
    }

    /**
     * Creates a {@link XYDataset} with the Speed of the car data from the data read from the files.
     * @return The {@link XYDataset} filled with the Speed of the car and the corresponding time
     */
    @Override
    public XYDataset getDatasetSpeed(){
        TimeSeriesCollection dataset = new TimeSeriesCollection();

        int numSeries = 1;
        for(ArrayList<CarData> cd : this.getData()){
            TimeSeries series1 = new TimeSeries("SpeedSeries "+numSeries);
            for(CarData data : cd) {
                series1.add(new Second(data.getDate()), data.getSpeed());
            }
            numSeries++;
            dataset.addSeries(series1);
        }
        return dataset;
    }

    /**
     * Creates a {@link XYDataset} with the Charge of the Battery data from the data read from the files.
     * @return The {@link XYDataset} filled with the Charge of the Battery and the corresponding time
     */
    @Override
    public XYDataset getDatasetCharge(){
        TimeSeriesCollection dataset = new TimeSeriesCollection();

        int numSeries = 1;
        for(ArrayList<CarData> cd : this.getData()){
            TimeSeries series1 = new TimeSeries("ChargeSeries "+numSeries);
            for(CarData data : cd) {
                series1.add(new Second(data.getDate()), data.getCharge());
            }
            numSeries++;
            dataset.addSeries(series1);
        }
        return dataset;
    }

    /**
     * Creates a {@link XYDataset} with the Capacity of the Battery data from the data read from the files.
     * @return The {@link XYDataset} filled with the Capacity of the Battery and the corresponding time
     */
    @Override
    public XYDataset getDatasetCapacity(){
        TimeSeriesCollection dataset = new TimeSeriesCollection();

        int numSeries = 1;
        for(ArrayList<CarData> cd : this.getData()){
            TimeSeries series1 = new TimeSeries("CapacitySeries "+numSeries);
            for(CarData data : cd) {
                series1.add(new Second(data.getDate()), data.getAHr());
            }
            numSeries++;
            dataset.addSeries(series1);
        }
        return dataset;
    }
    /**
     * Creates a {@link XYDataset} with the Power of the Motor data from the data read from the files.
     * @return The {@link XYDataset} filled with the Power of the Motor and the corresponding time
     */
    @Override
    public XYDataset getDatasetMotorPower(){
        TimeSeriesCollection dataset = new TimeSeriesCollection();

        int numSeries = 1;
        for(ArrayList<CarData> cd : this.getData()){
            TimeSeries series1 = new TimeSeries("MotorPowerSeries "+numSeries);
            for(CarData data : cd) {
                series1.add(new Second(data.getDate()), data.getMotorPower());
            }
            numSeries++;
            dataset.addSeries(series1);
        }
        return dataset;
    }

    /**
     * Creates a {@link XYDataset} with the Temperature of the Battery data from the data read from the files.
     * @return The {@link XYDataset} filled with the Temperature of the Battery and the corresponding time
     */
    @Override
    public XYDataset getDatasetTemperature(){
        TimeSeriesCollection dataset = new TimeSeriesCollection();


        for(int i=0;i<4;i++) {
            TimeSeries series1 = new TimeSeries("TemperatureSensor " + (i+1));
            for (ArrayList<CarData> cd : this.getData()) {
                for (CarData data : cd) {
                    series1.add(new Second(data.getDate()), data.getTemperature()[i]);
                }
            }
            dataset.addSeries(series1);
        }
        return dataset;
    }

    /**
     * Creates a {@link XYDataset} with the State of Health of the Battery data from the data read from the files.
     * @return The {@link XYDataset} filled with the State of Health of the Battery and the corresponding time
     */
    @Override
    public XYDataset getDatasetSOH(){
        TimeSeriesCollection dataset = new TimeSeriesCollection();

        int numSeries = 1;
        TimeSeries series1 = new TimeSeries("HealthSeries "+numSeries);
        for(ArrayList<CarData> cd : this.getData()){
            series1.add(new Second(cd.get(0).getDate()), cd.get(0).getSOH());
            series1.add(new Second(cd.get(cd.size()-1).getDate()), cd.get(cd.size()-1).getSOH());
        }
        dataset.addSeries(series1);
        return dataset;
    }

    /**
     * Creates a {@link XYDataset} with the number of Quick Charges from the data read from the files.
     * @return The {@link XYDataset} filled with the number of Quick Charges and the corresponding time
     */
    @Override
    public XYDataset getDatasetQuickCharge(){
        TimeSeriesCollection dataset = new TimeSeriesCollection();

        int numSeries = 1;
        TimeSeries series1 = new TimeSeries("QCSeries "+numSeries);
        int lastValue = 0;
        for(ArrayList<CarData> cd : this.getData()){
            for (CarData data : cd) {
                //if(data.getQuickCharge() != lastValue){
                    series1.add(new Second(data.getDate()), data.getQuickCharge());
                //}
            }
        }
        dataset.addSeries(series1);
        return dataset;
    }

    /**
     * Creates a {@link JFreeChart} with the data provided in the parameters.
     * Generate the corresponding legend items and link it to chart.
     * @param title The title of the Chart
     * @param xLabel The label of the x axis (domain axis)
     * @param yLabel The label of the y axis (range axis)
     * @param dataset The data to be displayed on the chart
     * @return The {@link JFreeChart} with the data provided
     */
    @Override
    public JFreeChart getPanelChart(String title, String xLabel, String yLabel, Dataset dataset){


        JFreeChart chart =  ChartFactory.createTimeSeriesChart(
                title,
                xLabel,
                yLabel,
                (XYDataset) dataset,
                true,
                false,
                false);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        //renderer.setDefaultPaint(Color.CYAN);
        Paint[] cs = ChartColor.createDefaultPaintArray();
        chart.getXYPlot().setDataset(0, (XYDataset) dataset);
        for( int i = 0 ; i < ((XYDataset) dataset).getSeriesCount() ; i++){
            renderer.setSeriesPaint(i,cs[0]);
        }
        renderer.setDefaultShapesVisible(false);


        chart.getXYPlot().setRenderer(0,renderer);
        LegendItemCollection collection = new LegendItemCollection();
        LegendItem legend = new LegendItem(yLabel);
        LegendItem retVal = new LegendItem(yLabel, yLabel,
                yLabel, "", true, legend.getShape(),
                legend.isShapeFilled(), chart.getXYPlot().getRenderer().getSeriesPaint(0), legend.isShapeOutlineVisible(),
                legend.getOutlinePaint(), legend.getOutlineStroke(), legend.isLineVisible(),
                legend.getLine(), legend.getLineStroke(), legend.getLinePaint());
        collection.add(retVal);
        collection.get(0).setDataset(dataset);
        chart.getXYPlot().setFixedLegendItems(collection);
        return chart;
    }
}
