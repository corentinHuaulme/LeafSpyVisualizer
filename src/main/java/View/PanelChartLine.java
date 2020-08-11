package View;

import Model.CarData;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.Dataset;
import org.jfree.data.time.*;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.util.ArrayList;

public class PanelChartLine extends PanelChart{

    public PanelChartLine(ArrayList<ArrayList<CarData>> cd) {
        super(cd);
    }

    @Override
    public XYDataset getDatasetSpeed(){
        TimeSeriesCollection dataset = new TimeSeriesCollection();

        int numSeries = 1;
        for(ArrayList<CarData> cd : this.getData()){
            TimeSeries series1 = new TimeSeries("Series "+numSeries);
            for(CarData data : cd) {
                series1.add(new Second(data.getDate()), data.getSpeed());
            }
            numSeries++;
            dataset.addSeries(series1);
        }
        return dataset;
    }

    @Override
    public XYDataset getDatasetCharge(){
        TimeSeriesCollection dataset = new TimeSeriesCollection();

        int numSeries = 1;
        for(ArrayList<CarData> cd : this.getData()){
            TimeSeries series1 = new TimeSeries("Series "+numSeries);
            for(CarData data : cd) {
                series1.add(new Second(data.getDate()), data.getCharge());
            }
            numSeries++;
            dataset.addSeries(series1);
        }
        return dataset;
    }

    @Override
    public XYDataset getDatasetCapacity(){
        TimeSeriesCollection dataset = new TimeSeriesCollection();

        int numSeries = 1;
        for(ArrayList<CarData> cd : this.getData()){
            TimeSeries series1 = new TimeSeries("Series "+numSeries);
            for(CarData data : cd) {
                series1.add(new Second(data.getDate()), data.getAHr());
            }
            numSeries++;
            dataset.addSeries(series1);
        }
        return dataset;
    }
    @Override
    public XYDataset getDatasetMotorPower(){
        TimeSeriesCollection dataset = new TimeSeriesCollection();

        int numSeries = 1;
        for(ArrayList<CarData> cd : this.getData()){
            TimeSeries series1 = new TimeSeries("Series "+numSeries);
            for(CarData data : cd) {
                series1.add(new Second(data.getDate()), data.getMotorPower());
            }
            numSeries++;
            dataset.addSeries(series1);
        }
        return dataset;
    }
    @Override
    public XYDataset getDatasetTemperature(){
        TimeSeriesCollection dataset = new TimeSeriesCollection();

        for(int i=0;i<4;i++) {
            TimeSeries series1 = new TimeSeries("Sensor " + (i+1));
            for (ArrayList<CarData> cd : this.getData()) {
                for (CarData data : cd) {
                    series1.add(new Second(data.getDate()), data.getTemperature()[i]);
                }
            }
            dataset.addSeries(series1);
        }
        return dataset;
    }

    @Override
    public JFreeChart getPanelChart(String title, String xLabel, String yLabel, Dataset dataset){


        return ChartFactory.createTimeSeriesChart(
                title,
                xLabel,
                yLabel,
                (XYDataset) dataset,
                true,
                false,
                false);

    }
}
