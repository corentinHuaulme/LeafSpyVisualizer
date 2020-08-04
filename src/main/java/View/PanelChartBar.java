package View;

import Model.CarData;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.util.ArrayList;

public class PanelChartBar extends PanelChart{

    public PanelChartBar(ArrayList<ArrayList<CarData>> cd){
        super(cd);
    }

    @Override
    public CategoryDataset getDatasetSpeed(){
        DefaultCategoryDataset dset = new DefaultCategoryDataset();
        int num = 1;
        int numSeries = 1;
        for(ArrayList<CarData> cd : this.getData()){
            for(CarData data : cd) {
                dset.addValue((Number) data.getSpeed(),"Serie "+numSeries,num);
                num++;
            }
            numSeries++;
        }
        return dset;
    }

    @Override
    public CategoryDataset getDatasetCharge(){
        DefaultCategoryDataset dset = new DefaultCategoryDataset();
        int num = 1;
        int numSeries = 1;
        for(ArrayList<CarData> cd : this.getData()){
            for(CarData data : cd) {
                dset.addValue((Number) data.getCharge(),"Serie "+numSeries,num);
                num++;
            }
            numSeries++;
        }
        return dset;
    }

    @Override
    public CategoryDataset getDatasetCapacity(){
        DefaultCategoryDataset dset = new DefaultCategoryDataset();
        int num = 1;
        int numSeries = 1;
        for(ArrayList<CarData> cd : this.getData()){
            for(CarData data : cd) {
                dset.addValue((Number) data.getAHr(),"Serie "+numSeries,num);
                num++;
            }
            numSeries++;
        }
        return dset;
    }
    @Override
    public CategoryDataset getDatasetMotorPower(){
        DefaultCategoryDataset dset = new DefaultCategoryDataset();
        int num = 1;
        int numSeries = 1;
        for(ArrayList<CarData> cd : this.getData()){
            for(CarData data : cd) {
                dset.addValue((Number) data.getMotorPower(),"Serie "+numSeries,num);
                num++;
            }
            numSeries++;
        }
        return dset;
    }
    @Override
    public CategoryDataset getDatasetTemperature(){
        DefaultCategoryDataset dset = new DefaultCategoryDataset();
        int num = 1;
        int numSeries = 1;
        for(ArrayList<CarData> cd : this.getData()){
            for(CarData data : cd) {
                dset.addValue((Number) data.getTemperature(),"Serie "+numSeries,num);
                num++;
            }
            numSeries++;
        }
        return dset;
    }
    @Override
    public JFreeChart getPanelChart(String title, String xLabel, String yLabel, Dataset dataset){

        JFreeChart chart = ChartFactory.createBarChart(
                title,
                xLabel,
                yLabel,
                (CategoryDataset) dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        return chart;
    }


}
