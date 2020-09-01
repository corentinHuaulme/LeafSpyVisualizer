package View;

import Controller.CSVReader;
import Controller.ChartChooserListener;
import Controller.ChartMouseOverListener;
import Controller.PlotChangedListenerCustom;
import Model.CarData;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.*;
import org.jfree.data.general.Dataset;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

/**
 * A mainWindow is a {@link JPanel} containing all the components of the window displayed to the user.
 * A mainWindow is composed of 4 {@link JPanel} (one for each corner of the window). The place of each {@link JPanel} can be easily changed as the layout used is {@link GridBagLayout}.
 */
public class mainWindow extends JPanel {

    /** The {@link PanelChart} use to gather the datasets and the {@link JFreeChart} generated with the datasets. **/
    private PanelChart panelChart;
    /** The Data read from the selected files. **/
    private ArrayList<ArrayList<CarData>> cd;

    /**
     * Creates a mainWindow displaying the window to load the files and the mainWindow with all the components with the data from the files.
     */
    public mainWindow(){

        refreshWindow();

        this.setVisible(true);

    }

    /**
     * Refreshes the components with the data read by the files selected by the user.
     * Pop a {@link JFileChooser} window to let the user select files.
     */
    public void refreshWindow(){
        this.removeAll();
        loadComponents(this.fileChooser(new ArrayList<ArrayList<CarData>>()));
        this.validate();
        this.repaint();
    }

    /**
     * Refreshes the components with the data read by the files selected by the user while keeping the previously read data. Used to add a file to the selection of files.
     * Pop a {@link JFileChooser} window to let the user select files.
     * @param cd The already loaded data.
     */
    public void refreshWindow(ArrayList<ArrayList<CarData>> cd){
        this.removeAll();
        loadComponents(this.fileChooser(cd));
        this.validate();
        this.repaint();
    }

    /**
     * Loads all the components contained in the mainWindow. Loads all the charts, the map and the listeners linked to any components.
     * @param cd The data to be used by the components.
     */
    public void loadComponents(ArrayList<ArrayList<CarData>> cd){

       mapPanel map = new mapPanel(cd);


       panelChart = new PanelChartLine(cd);
       panelChart.getDatasetSpeed();
       Dataset charge = panelChart.getDatasetCharge();
       Dataset temp = panelChart.getDatasetSpeed();
       this.setSize(800,800);
       this.setLayout(new GridBagLayout());
       GridBagConstraints c = new GridBagConstraints();
       JFreeChart firstChart = panelChart.getPanelChart("","Time","Percentage of the Battery",charge);

       JPanel chartpan1 = new JPanel();
       chartpan1.setLayout(new BorderLayout());
       ChartPanel CP = new ChartPanel(firstChart);
       //CP.setHorizontalAxisTrace(true);
       CP.setPreferredSize(new Dimension(800,470));
       chartpan1.add(CP, BorderLayout.CENTER);
       chartpan1.validate();


       JFreeChart secondChart = panelChart.getPanelChart("","Time","Speed of the Car",temp);
       XYPlot plot = secondChart.getXYPlot();
       DateAxis axis = (DateAxis) plot.getDomainAxis();
       Date d = cd.get(0).get(0).getDate();
       Calendar calDate = Calendar.getInstance();
       calDate.setTime(d);

       Calendar calendar2 = Calendar.getInstance();
       boolean notFinished = true;
       ArrayList datesToHide = new ArrayList();
       String dateFormat = "dd/MM HH:mm";
       axis.setDateFormatOverride(new SimpleDateFormat(dateFormat));

       plot = firstChart.getXYPlot();
       axis = (DateAxis) plot.getDomainAxis();
       axis.setDateFormatOverride(new SimpleDateFormat(dateFormat));

       JPanel chartpan2 = new JPanel();
       chartpan2.setLayout(new BorderLayout());
       ChartPanel CP2 = new ChartPanel(secondChart);


       CP2.setPreferredSize(new Dimension(800,470));
       chartpan2.add(CP2, BorderLayout.CENTER);
       chartpan2.validate();


       CP.addChartMouseListener(new ChartMouseOverListener(CP,CP2,map));
       CP2.addChartMouseListener(new ChartMouseOverListener(CP2,CP,map));


        PlotChangedListenerCustom listenerCustomFirst = new PlotChangedListenerCustom(firstChart,secondChart);
        PlotChangedListenerCustom listenerCustomSecond = new PlotChangedListenerCustom(secondChart,firstChart);

        firstChart.getXYPlot().addChangeListener(listenerCustomFirst);
        secondChart.getXYPlot().addChangeListener(listenerCustomSecond);


       JPanel topLeft = new JPanel();
       JPanel topleftCategories = new JPanel();
        JCheckBox socChk = new JCheckBox("SOC");
        JCheckBox speedChk = new JCheckBox("Speed");
        JCheckBox tempChk = new JCheckBox("Battery Temperature");
        JCheckBox batCapChk = new JCheckBox("Battery Capacity");
        JCheckBox motPwrChk = new JCheckBox("Motor Power");
        JCheckBox sohChk = new JCheckBox("SOH");
        JCheckBox qcChk = new JCheckBox("Quick Charge");

        topleftCategories.add(socChk);
        topleftCategories.add(speedChk);
        topleftCategories.add(tempChk);
        topleftCategories.add(batCapChk);
        topleftCategories.add(motPwrChk);
        topleftCategories.add(sohChk);
        topleftCategories.add(qcChk);

        socChk.setSelected(true);

        socChk.addActionListener(new ChartChooserListener(socChk,CP,panelChart,CP2,listenerCustomFirst,listenerCustomSecond));
        speedChk.addActionListener(new ChartChooserListener(speedChk,CP,panelChart,CP2,listenerCustomFirst,listenerCustomSecond));
        tempChk.addActionListener(new ChartChooserListener(tempChk,CP,panelChart,CP2,listenerCustomFirst,listenerCustomSecond));
        batCapChk.addActionListener(new ChartChooserListener(batCapChk,CP,panelChart,CP2,listenerCustomFirst,listenerCustomSecond));
        motPwrChk.addActionListener(new ChartChooserListener(motPwrChk,CP,panelChart,CP2,listenerCustomFirst,listenerCustomSecond));
        sohChk.addActionListener(new ChartChooserListener(sohChk,CP,panelChart,CP2,listenerCustomFirst,listenerCustomSecond));
        qcChk.addActionListener(new ChartChooserListener(qcChk,CP,panelChart,CP2,listenerCustomFirst,listenerCustomSecond));



       topLeft.setLayout(new BorderLayout());

       topLeft.add(chartpan1,BorderLayout.CENTER);
       topLeft.add(topleftCategories,BorderLayout.NORTH);

        //chartChooser.addActionListener(new ChartChooserListener(chartChooser,CP,panelChart));



       JPanel bottomLeft = new JPanel();
       JPanel listChk = new JPanel();
       JCheckBox socChk2 = new JCheckBox("SOC");
       JCheckBox speedChk2 = new JCheckBox("Speed");
       JCheckBox tempChk2 = new JCheckBox("Battery Temperature");
       JCheckBox batCapChk2 = new JCheckBox("Battery Capacity");
       JCheckBox motPwrChk2 = new JCheckBox("Motor Power");
       JCheckBox sohChk2 = new JCheckBox("SOH");
       JCheckBox qcChk2 = new JCheckBox("Quick Charge");

       speedChk2.setSelected(true);

       listChk.add(socChk2);
       listChk.add(speedChk2);
       listChk.add(tempChk2);
       listChk.add(batCapChk2);
       listChk.add(motPwrChk2);
       listChk.add(sohChk2);
       listChk.add(qcChk2);

       socChk2.addActionListener(new ChartChooserListener(socChk2,CP2,panelChart,CP,listenerCustomFirst,listenerCustomSecond));
       speedChk2.addActionListener(new ChartChooserListener(speedChk2,CP2,panelChart,CP,listenerCustomFirst,listenerCustomSecond));
       tempChk2.addActionListener(new ChartChooserListener(tempChk2,CP2,panelChart,CP,listenerCustomFirst,listenerCustomSecond));
       batCapChk2.addActionListener(new ChartChooserListener(batCapChk2,CP2,panelChart,CP,listenerCustomFirst,listenerCustomSecond));
       motPwrChk2.addActionListener(new ChartChooserListener(motPwrChk2,CP2,panelChart,CP,listenerCustomFirst,listenerCustomSecond));
       sohChk2.addActionListener(new ChartChooserListener(sohChk2,CP2,panelChart,CP,listenerCustomFirst,listenerCustomSecond));
       qcChk2.addActionListener(new ChartChooserListener(qcChk2,CP2,panelChart,CP,listenerCustomFirst,listenerCustomSecond));

       bottomLeft.setLayout(new BorderLayout());

       bottomLeft.add(chartpan2,BorderLayout.CENTER);
       bottomLeft.add(listChk, BorderLayout.NORTH);


        UtilDateModel model = new UtilDateModel();
        model.setDate(20,04,2014);
        // Need this...
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);



       JPanel topRight = new JPanel();
       JPanel buttonsTopRight = new JPanel();
       JButton load = new JButton("Load files");
       JButton addFiles = new JButton("Add files");
       topRight.setLayout(new BorderLayout());
       buttonsTopRight.setLayout(new GridBagLayout());

       GridBagConstraints constraints = new GridBagConstraints();
       constraints.gridheight = 1;
       constraints.gridwidth = 1;
       constraints.gridy = 0;
       constraints.gridx = 0;

       buttonsTopRight.add(load,constraints);

       constraints.gridx = 1;
       buttonsTopRight.add(addFiles,constraints);

       constraints.gridx = 2;
      // buttonsTopRight.add(datePanel,constraints);
       topRight.add(buttonsTopRight,BorderLayout.NORTH);
       topRight.add(map.getMap(),BorderLayout.CENTER);

       load.addActionListener(e -> refreshWindow());
       addFiles.addActionListener(e -> refreshWindow(cd));

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
       c.gridheight = 1;
       this.add(topLeft, c);

       c.fill = java.awt.GridBagConstraints.BOTH;
       c.weightx = 1.0;
       c.weighty = 1.0;
       c.gridx = 2;
       c.gridy = 0;
       c.gridwidth = 2;
       c.gridheight = 1;

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

    /**
     * Pop a {@link JFileChooser} to let the user select files.
     * @param cd The already loaded data. An empty {@link ArrayList} is provided if no other data is to be kept.
     * @return The data read append to the already loaded data.
     */
    public ArrayList<ArrayList<CarData>> fileChooser(ArrayList<ArrayList<CarData>> cd){
       JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
       jfc.setMultiSelectionEnabled(true);
       File[] selectedFile = null;
       int returnValue = jfc.showOpenDialog(null);
       // int returnValue = jfc.showSaveDialog(null);

       if (returnValue == JFileChooser.APPROVE_OPTION) {
           selectedFile = jfc.getSelectedFiles();
       }

        ArrayList<ArrayList<CarData>> cd2 = new ArrayList<ArrayList<CarData>>();
        cd2 = new CSVReader().readFiles(selectedFile);

        System.out.println("C KWA LA TAIELLE : "+cd2.size());

        boolean isIn = false;

        for(ArrayList<CarData> carData : cd2) {
            isIn = false;
            for(ArrayList<CarData> carData2 : cd) {
                if (carData.get(0).getDate().getDate() == carData2.get(0).getDate().getDate() &&
                        carData.get(0).getDate().getMonth() == carData2.get(0).getDate().getMonth() &&
                        carData.get(0).getDate().getYear() == carData2.get(0).getDate().getYear()) {
                    isIn = true;
                    System.out.println(selectedFile[cd2.indexOf(carData)].getName() + " already loaded - " + carData.get(0).getDate().toString() + " - " + carData2.get(0).getDate().toString());
                    break;
                }
            }
            if(!isIn){
                cd.add(carData);
            }
        }


       return cd;
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("LeafSpy visualizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setSize(1920,1080);

        //Set up the content pane.
        frame.getContentPane().add(new mainWindow());

        //Display the window.
        //frame.pack();
        frame.setVisible(true);

    }
    }
