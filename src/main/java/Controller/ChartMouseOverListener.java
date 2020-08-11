package Controller;

import Model.CarData;
import View.mapPanel;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.*;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.time.*;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.sql.Time;
import java.util.Date;

public class ChartMouseOverListener implements ChartMouseListener {

    private ChartPanel cp;
    private ChartPanel cp1;
    private mapPanel map;

    public ChartMouseOverListener(ChartPanel cp, ChartPanel cp1, mapPanel map){
        this.cp = cp;
        this.cp1 = cp1;
        this.map = map;
    }

    @Override
    public void chartMouseClicked(ChartMouseEvent event) {

    }

    @Override
    public void chartMouseMoved(ChartMouseEvent event) {
        Rectangle2D dataArea = this.cp.getScreenDataArea( );
        JFreeChart chart = event.getChart();
        long x = 0;
        int index = 0;
        ValueMarker marker;
        Plot plot = chart.getPlot();
        if(plot instanceof XYPlot){
            XYPlot xyplot = (XYPlot) chart.getPlot();
            ValueAxis xAxis = xyplot.getDomainAxis();
            x = (long) xAxis.java2DToValue(event.getTrigger().getX(), dataArea,
                    RectangleEdge.BOTTOM);
            index = getIndex(x);
            xyplot.clearDomainMarkers();
            xyplot.clearRangeMarkers();
            marker = new ValueMarker(x);
            xyplot.addDomainMarker(marker);
        }else{
                   /*CategoryPlot catplot = chart.getCategoryPlot();
                   CategoryAxis xAxis = catplot.getDomainAxis();
                   CategoryItemEntity itemEntity = (CategoryItemEntity) event.getEntity();
                       x = Double.parseDouble(itemEntity.getColumnKey().toString());
                   catplot.clearDomainMarkers();
                   catplot.clearRangeMarkers();
                   CategoryMarker mark = new CategoryMarker(x);
                   catplot.addDomainMarker(mark);*/
        }
        marker = new ValueMarker(x);
        marker.setPaint(Color.black);
        try {
            XYPlot plot2 =  this.cp1.getChart().getXYPlot();
            plot2.clearDomainMarkers();
            plot2.clearRangeMarkers();
            plot2.addDomainMarker(marker);
        }catch(Exception e){
            CategoryPlot plot2 = this.cp1.getChart().getCategoryPlot();
            plot2.clearDomainMarkers();
            plot2.clearRangeMarkers();
            CategoryMarker mark = new CategoryMarker(x);
            plot2.addDomainMarker(mark);
        }

        //plot.addRangeMarker(new ValueMarker((Double) plot.getDataset().getY(0, (int) x-1)));



        //plot2.addRangeMarker(new ValueMarker((Double) plot2.getDataset().getY(0, (int) x-1)));

        this.map.addWaypoint(index);


    }
    private int getIndex(long d){
        TimeSeriesCollection dataset = (TimeSeriesCollection) this.cp.getChart().getXYPlot().getDataset();
        int num = 0;
        for(Object o : dataset.getSeries()){
            TimeSeries ts = (TimeSeries)o;
            for(Object o1 : ts.getItems()){
                TimeSeriesDataItem tsdi = (TimeSeriesDataItem) o1;
                Date cd = tsdi.getPeriod().getEnd();
                if(cd.getTime() >= d){
                    return num;
                }
                num++;
            }
        }
        return num;
    }
}
