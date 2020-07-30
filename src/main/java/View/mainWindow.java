package View;

import Controller.CSVReader;
import Model.CarData;
import org.jfree.chart.*;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ui.Layer;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.io.CSV;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class mainWindow extends JPanel {

    public mainWindow(){

        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setMultiSelectionEnabled(true);
        File[] selectedFile = null;
        int returnValue = jfc.showOpenDialog(null);
        // int returnValue = jfc.showSaveDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedFile = jfc.getSelectedFiles();
        }
        ArrayList<ArrayList<CarData>> cd = new ArrayList<ArrayList<CarData>>();
        cd = new CSVReader().readFiles(selectedFile);

        mapPanel map = new mapPanel(cd);


        PanelChart panelChart = new PanelChart(cd);
        XYDataset charge = panelChart.getSeriesSpeed();
        XYDataset temp = panelChart.getSeriesMotorPower();
        this.setSize(800,800);
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        JFreeChart firstChart = panelChart.getPanelChart("test","Time","Speed (km/h)",charge);

        JPanel chartpan1 = new JPanel();
        chartpan1.setLayout(new BorderLayout());
        ChartPanel CP = new ChartPanel(firstChart);
        CP.setPreferredSize(new Dimension(800,470));
        chartpan1.add(CP, BorderLayout.CENTER);
        chartpan1.validate();


        JFreeChart secondChart = panelChart.getPanelChart("test","Time","Drive motor power (W)",temp);

        JPanel chartpan2 = new JPanel();
        chartpan2.setLayout(new BorderLayout());
        ChartPanel CP2 = new ChartPanel(secondChart);
        CP2.setPreferredSize(new Dimension(800,470));
        chartpan2.add(CP2, BorderLayout.CENTER);
        chartpan2.validate();


        var list = new JList();

        list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String name = (String) list.getSelectedValue();
            }
        });

        CP.addChartMouseListener(new ChartMouseListener() {
            @Override
            public void chartMouseClicked(ChartMouseEvent event) {

            }

            @Override
            public void chartMouseMoved(ChartMouseEvent event) {
                Rectangle2D dataArea = CP.getScreenDataArea();
                JFreeChart chart = event.getChart();
                XYPlot plot = (XYPlot) chart.getPlot();
                ValueAxis xAxis = plot.getDomainAxis();
                double x = xAxis.java2DToValue(event.getTrigger().getX(), dataArea,
                        RectangleEdge.BOTTOM);

                ValueMarker marker = new ValueMarker(x);
                marker.setPaint(Color.black);

                XYPlot plot2 = (XYPlot) secondChart.getPlot();

                plot.clearDomainMarkers();
                plot.clearRangeMarkers();
                plot.addDomainMarker(marker);
                //plot.addRangeMarker(new ValueMarker((Double) plot.getDataset().getY(0, (int) x-1)));


                plot2.clearDomainMarkers();
                plot2.clearRangeMarkers();
                plot2.addDomainMarker(marker);
                //plot2.addRangeMarker(new ValueMarker((Double) plot2.getDataset().getY(0, (int) x-1)));

                map.addWaypoint((int) x);


            }
        });


        JScrollPane spane = new JScrollPane();
        spane.getViewport().add(list);

        JLabel label = new JLabel("Aguirre, der Zorn Gottes");
        label.setFont(new Font("Serif", Font.PLAIN, 12));

        list.add(label);


        JPanel topLeft = new JPanel();
        JComboBox chartChooser = new JComboBox();

        chartChooser.addItem("SOC");
        chartChooser.addItem("Speed");
        chartChooser.addItem("Battery Temperature");
        chartChooser.addItem("Battery Capacity");
        chartChooser.addItem("Motor Power");

        topLeft.setLayout(new BorderLayout());

        topLeft.add(chartpan1,BorderLayout.CENTER);
        topLeft.add(chartChooser, BorderLayout.NORTH);

        chartChooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(e.getActionCommand()+ " - "+chartChooser.getItemAt(chartChooser.getSelectedIndex()));
                switch(chartChooser.getSelectedIndex()){
                    case 0 :
                        System.out.println("test");
                        firstChart.getXYPlot().setDataset(panelChart.getSeriesCharge());
                        break;
                    case 1 :
                        firstChart.getXYPlot().setDataset(panelChart.getSeriesSpeed());
                        break;
                    case 2 :
                        firstChart.getXYPlot().setDataset(panelChart.getSeriesTemperatureBattery());
                        break;
                    case 3 :
                        firstChart.getXYPlot().setDataset(panelChart.getSeriesCapacity());
                        break;
                    case 4 :
                        firstChart.getXYPlot().setDataset(panelChart.getSeriesMotorPower());
                        break;
                }
            }
        });


        JPanel bottomLeft = new JPanel();
        JComboBox chartChooserBottom = new JComboBox();

        chartChooserBottom.addItem("SOC");
        chartChooserBottom.addItem("Speed");
        chartChooserBottom.addItem("Battery Temperature");
        chartChooserBottom.addItem("Battery Capacity");
        chartChooserBottom.addItem("Motor Power");

        bottomLeft.setLayout(new BorderLayout());

        bottomLeft.add(chartpan2,BorderLayout.CENTER);
        bottomLeft.add(chartChooserBottom, BorderLayout.NORTH);

        chartChooserBottom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(e.getActionCommand()+ " - "+chartChooserBottom.getItemAt(chartChooserBottom.getSelectedIndex()));
                switch(chartChooserBottom.getSelectedIndex()){
                    case 0 :
                        System.out.println("test");
                        secondChart.getXYPlot().setDataset(panelChart.getSeriesCharge());
                        break;
                    case 1 :
                        secondChart.getXYPlot().setDataset(panelChart.getSeriesSpeed());
                        break;
                    case 2 :
                        secondChart.getXYPlot().setDataset(panelChart.getSeriesTemperatureBattery());
                        break;
                    case 3 :
                        secondChart.getXYPlot().setDataset(panelChart.getSeriesCapacity());
                        break;
                    case 4 :
                        secondChart.getXYPlot().setDataset(panelChart.getSeriesMotorPower());
                        break;
                }
            }
        });

        JPanel topRight = new JPanel();
        JButton load = new JButton("Load files");
        topRight.setLayout(new BorderLayout());

        topRight.add(load,BorderLayout.NORTH);
        topRight.add(map.getMap(),BorderLayout.CENTER);

        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                jfc.setMultiSelectionEnabled(true);
                File[] selectedFile = null;
                int returnValue = jfc.showOpenDialog(null);
                // int returnValue = jfc.showSaveDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    selectedFile = jfc.getSelectedFiles();
                }
            }
        });


        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        this.add(topLeft, c);

        c.fill = java.awt.GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridx = 2;
        c.gridy = 0;
        c.gridwidth = 2;

        this.add(topRight, c);

        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 1;
        this.add(bottomLeft, gc);

        gc.fill = GridBagConstraints.BOTH;
        gc.gridx = 2;
        gc.gridy = 1;
        gc.gridwidth = 2;

        this.add(spane,gc);

        this.setVisible(true);

        System.out.println(this.getLayout().getClass());
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("LeafSpy visualizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setSize(1920,1080);

        //Set up the content pane.
        frame.getContentPane().add(new mainWindow());

        //Display the window.
       // frame.pack();
        frame.setVisible(true);

    }
}
