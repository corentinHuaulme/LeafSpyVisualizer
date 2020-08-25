package View;

import Model.CarData;
import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.resources.JFreeChartResources;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;
import org.jfree.data.time.Month;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public abstract class PanelChart{

    private ArrayList<ArrayList<CarData>> data;

    public PanelChart(ArrayList<ArrayList<CarData>> cd){
        this.data = cd;
    }

    public ArrayList<ArrayList<CarData>> getData() {
        return data;
    }

    public Dataset getDatasetSpeed(){
        return null;
    }
    public Dataset getDatasetCharge(){
        return null;
    }
    public Dataset getDatasetTemperature(){
        return null;
    }
    public Dataset getDatasetCapacity(){
        return null;
    }
    public Dataset getDatasetMotorPower(){
        return null;
    }
    public Dataset getDatasetSOH(){ return null; }
    public Dataset getDatasetQuickCharge(){ return null; }
    public JFreeChart getPanelChart(String title, String xLabel, String yLabel, Dataset dataset){
        return null;
    }
}
