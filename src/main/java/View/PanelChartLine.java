package View;

import Model.CarData;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
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
        XYSeriesCollection dataset = new XYSeriesCollection();
        int num = 1;
        int numSeries = 1;
        for(ArrayList<CarData> cd : this.getData()){
            XYSeries s2 = new XYSeries("Serie "+numSeries);
            for(CarData data : cd) {
                s2.add(num, data.getSpeed());
                num++;
            }
            numSeries++;
            dataset.addSeries(s2);
        }
        return dataset;
    }

    @Override
    public XYDataset getDatasetCharge(){
        XYSeriesCollection dataset = new XYSeriesCollection();
        int num = 1;
        int numSeries = 1;
        for(ArrayList<CarData> cd : this.getData()){
            XYSeries s2 = new XYSeries("Serie "+numSeries);
            for(CarData data : cd) {
                s2.add(num, data.getCharge());
                num++;
            }
            numSeries++;
            dataset.addSeries(s2);
        }
        return dataset;
    }

    @Override
    public XYDataset getDatasetCapacity(){
        XYSeriesCollection dataset = new XYSeriesCollection();
        int num = 1;
        int numSeries = 1;
        for(ArrayList<CarData> cd : this.getData()){
            XYSeries s2 = new XYSeries("Serie "+numSeries);
            for(CarData data : cd) {
                s2.add(num, data.getAHr());
                num++;
            }
            numSeries++;
            dataset.addSeries(s2);
        }
        return dataset;
    }
    @Override
    public XYDataset getDatasetMotorPower(){
        XYSeriesCollection dataset = new XYSeriesCollection();
        int num = 1;
        int numSeries = 1;
        for(ArrayList<CarData> cd : this.getData()){
            XYSeries s2 = new XYSeries("Serie "+numSeries);
            for(CarData data : cd) {
                s2.add(num, data.getMotorPower());
                num++;
            }
            numSeries++;
            dataset.addSeries(s2);
        }
        return dataset;
    }
    @Override
    public XYDataset getDatasetTemperature(){
        XYSeriesCollection dataset = new XYSeriesCollection();


        int num = 1;
        int numSeries = 1;
        for(ArrayList<CarData> cd : this.getData()){
            XYSeries series1 = new XYSeries("Series "+numSeries);
            for(CarData data : cd) {
                series1.add(num, data.getTemperature());
                num++;
            }
            numSeries++;
            dataset.addSeries(series1);
        }
        return dataset;
    }

    @Override
    public JFreeChart getPanelChart(String title, String xLabel, String yLabel, Dataset dataset){


        JFreeChart chart = ChartFactory.createXYLineChart(
                title,
                xLabel,
                yLabel,
                (XYDataset) dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        return chart;
    }
}
