package View;

import Controller.CSVReader;
import Controller.ChartMouseOverListener;
import Model.CarData;
import org.jfree.chart.*;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.plot.*;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.general.Dataset;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.ArrayList;


public class mainWindow extends JPanel {

    private PanelChart panelChart;
    private ArrayList<ArrayList<CarData>> cd;

    public mainWindow(){

        refreshWindow();

        this.setVisible(true);

    }

    public void refreshWindow(){
        this.removeAll();
        loadComponents(this.fileChooser());
    }

    public void loadComponents(File[] files){

       cd = new ArrayList<ArrayList<CarData>>();
       cd = new CSVReader().readFiles(files);

       mapPanel map = new mapPanel(cd);


       panelChart = new PanelChartLine(cd);
       panelChart.getDatasetSpeed();
       Dataset charge = panelChart.getDatasetCharge();
       Dataset temp = panelChart.getDatasetTemperature();
       this.setSize(800,800);
       this.setLayout(new GridBagLayout());
       GridBagConstraints c = new GridBagConstraints();
       JFreeChart firstChart = panelChart.getPanelChart("test","Time","Speed (km/h)",charge);

       JPanel chartpan1 = new JPanel();
       chartpan1.setLayout(new BorderLayout());
       ChartPanel CP = new ChartPanel(firstChart);
       CP.setHorizontalAxisTrace(true);
       CP.setPreferredSize(new Dimension(800,470));
       chartpan1.add(CP, BorderLayout.CENTER);
       chartpan1.validate();


       JFreeChart secondChart = panelChart.getPanelChart("test","Time","Drive motor power (W)",temp);

       JPanel chartpan2 = new JPanel();
       chartpan2.setLayout(new BorderLayout());
       ChartPanel CP2 = new ChartPanel(secondChart);

       // ((DateAxis)(secondChart.getXYPlot().getRangeAxis())).setDateFormatOverride(new SimpleDateFormat("hh:mm:ss"));

       CP2.setPreferredSize(new Dimension(800,470));
       chartpan2.add(CP2, BorderLayout.CENTER);
       chartpan2.validate();


       CP.addChartMouseListener(new ChartMouseOverListener(CP,CP2,map));
       CP2.addChartMouseListener(new ChartMouseOverListener(CP2,CP,map));




       JPanel topLeft = new JPanel();
       JComboBox chartChooser = new JComboBox();

       chartChooser.addItem("SOC");
       chartChooser.addItem("Speed");
       chartChooser.addItem("Battery Temperature");
       chartChooser.addItem("Battery Capacity");
       chartChooser.addItem("Motor Power");

       JComboBox chartTypeChooser = new JComboBox();

       chartTypeChooser.addItem("Line");
       chartTypeChooser.addItem("Bar");


       topLeft.setLayout(new BorderLayout());

       JPanel topLeftChooser = new JPanel();
       topLeftChooser.setLayout(new GridLayout(0,2));
       topLeftChooser.add(chartChooser);
       topLeftChooser.add(chartTypeChooser);

       topLeft.add(chartpan1,BorderLayout.CENTER);
       topLeft.add(topLeftChooser,BorderLayout.NORTH);

        chartChooser.addActionListener(e -> {
            switch (chartChooser.getSelectedIndex()) {
                case 0:
                    CP.setChart(panelChart.getPanelChart("Charge of the Battery", "Time", "Percentage of the Charge", panelChart.getDatasetCharge()));
                    break;
                case 1:
                    CP.setChart(panelChart.getPanelChart("Speed of the Car", "Time", "Speed of the Car (km/h)", panelChart.getDatasetSpeed()));
                    break;
                case 2:
                    CP.setChart(panelChart.getPanelChart("Temperature of the Battery", "Time", "Temperature of the Battery", panelChart.getDatasetTemperature()));
                    break;
                case 3:
                    CP.setChart(panelChart.getPanelChart("Capacity of the Battery", "Time", "Capacity of the Battery", panelChart.getDatasetCapacity()));
                    break;
                case 4:
                    CP.setChart(panelChart.getPanelChart("Motor Power", "Time", "Power comsumed by the Motor (W)", panelChart.getDatasetMotorPower()));
                    break;
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

       chartChooserBottom.addActionListener(e -> {
           System.out.println(e.getActionCommand()+ " - "+chartChooserBottom.getItemAt(chartChooserBottom.getSelectedIndex()));
           switch(chartChooserBottom.getSelectedIndex()){
               case 0 :
                   CP2.setChart(panelChart.getPanelChart("Charge of the Battery","Time","Percentage of the Charge",panelChart.getDatasetCharge()));
                   break;
               case 1 :
                   CP2.setChart(panelChart.getPanelChart("Speed of the Car","Time","Speed of the Car (km/h)",panelChart.getDatasetSpeed()));
                   break;
               case 2 :
                   CP2.setChart(panelChart.getPanelChart("Temperature of the Battery","Time","Temperature of the Battery (°C)",panelChart.getDatasetTemperature()));
                   break;
               case 3 :
                   CP2.setChart(panelChart.getPanelChart("Capacity of the Battery","Time","Capacity of the Battery",panelChart.getDatasetCapacity()));
                   break;
               case 4 :
                   CP2.setChart(panelChart.getPanelChart("Motor Power","Time","Drive motor power (W)",panelChart.getDatasetMotorPower()));
                   break;
           }
       });


        chartTypeChooser.addActionListener(e -> {
            switch(chartTypeChooser.getSelectedIndex()) {
                case 0:
                    panelChart = new PanelChartLine(cd);
                    CP.setChart(panelChart.getPanelChart(String.valueOf(CP.getChart().getTitle()),
                            CP.getChart().getCategoryPlot().getDomainAxis().getLabel(),
                            CP.getChart().getCategoryPlot().getRangeAxis().getLabel(),
                            panelChart.getDatasetCharge()));
                    CP2.setChart(panelChart.getPanelChart(String.valueOf(CP2.getChart().getTitle()),
                            CP2.getChart().getCategoryPlot().getDomainAxis().getLabel(),
                            CP2.getChart().getCategoryPlot().getRangeAxis().getLabel(),
                            panelChart.getDatasetCharge()));
                    chartChooser.setSelectedIndex(chartChooser.getSelectedIndex());
                    chartChooserBottom.setSelectedIndex(chartChooserBottom.getSelectedIndex());
                    break;
                case 1:
                    panelChart = new PanelChartBar(cd);
                    CP.setChart(panelChart.getPanelChart(String.valueOf(CP.getChart().getTitle()),
                            CP.getChart().getXYPlot().getDomainAxis().getLabel(),
                            CP.getChart().getXYPlot().getRangeAxis().getLabel(),
                            panelChart.getDatasetCharge()));
                    CP2.setChart(panelChart.getPanelChart(String.valueOf(CP2.getChart().getTitle()),
                            CP2.getChart().getXYPlot().getDomainAxis().getLabel(),
                            CP2.getChart().getXYPlot().getRangeAxis().getLabel(),
                            panelChart.getDatasetCharge()));
                    chartChooser.setSelectedIndex(chartChooser.getSelectedIndex());
                    chartChooserBottom.setSelectedIndex(chartChooserBottom.getSelectedIndex());
                    break;
            }
        });



       JPanel topRight = new JPanel();
       JButton load = new JButton("Load files");
       topRight.setLayout(new BorderLayout());

       topRight.add(load,BorderLayout.NORTH);
       topRight.add(map.getMap(),BorderLayout.CENTER);

       load.addActionListener(e -> refreshWindow());

       PanelChartPie pcp = new PanelChartPie(cd);
        JFreeChart pieChart = pcp.getPanelChart("Battery usage","Time","Speed (km/h)",pcp.getDatasetSpeed());
        JPanel chartpie = new JPanel();
        chartpie.setLayout(new BorderLayout());
        ChartPanel CP3 = new ChartPanel(pieChart);
        //CP3.setHorizontalAxisTrace(true);
        CP3.setPreferredSize(new Dimension(800,470));
        chartpie.add(CP3, BorderLayout.CENTER);
        chartpie.validate();


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

       gc.gridx = 1;
       gc.gridy = 1;
       gc.gridwidth = 2;
       this.add(chartpie,gc);


    }

    public File[] fileChooser(){
       JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
       jfc.setMultiSelectionEnabled(true);
       File[] selectedFile = null;
       int returnValue = jfc.showOpenDialog(null);
       // int returnValue = jfc.showSaveDialog(null);

       if (returnValue == JFileChooser.APPROVE_OPTION) {
           selectedFile = jfc.getSelectedFiles();
       }
       return selectedFile;
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
