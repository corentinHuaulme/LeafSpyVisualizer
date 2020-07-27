package View;

import Model.CarData;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PanelChart{

    private ArrayList<CarData> data;

    public PanelChart(ArrayList<CarData> cd){
        this.data = cd;
    }

    public XYSeries getSeriesCharge(){
        XYSeries s2 = new XYSeries("State Of Charge");
        int num = 1;
        for(CarData d : this.data){
            s2.add(num,d.getCharge());
            num++;
        }
        return s2;
    }

    public XYSeries getSeriesTemperatureBattery(){
        XYSeries s2 = new XYSeries("Temperature of the Battery");
        int num = 1;
        for(CarData d : this.data){
            s2.add(num,d.getTemperature());
            num++;
        }
        return s2;
    }

    public XYSeries getSeriesCapacity(){
        XYSeries s2 = new XYSeries("Capacity of the Battery");
        int num = 1;
        for(CarData d : this.data){
            s2.add(num,d.getAHr());
            num++;
        }
        return s2;
    }

    public JPanel getPanelChart(String title, String xLabel, String yLabel, XYDataset dataset){

        JFreeChart chart = ChartFactory.createXYLineChart(
                title,
                xLabel,
                yLabel,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        JPanel pan = new JPanel();
        pan.setLayout(new BorderLayout());
        ChartPanel CP = new ChartPanel(chart);
        CP.setPreferredSize(new Dimension(800,500));
        pan.add(CP, BorderLayout.CENTER);
        pan.validate();
        return pan;
    }

}
