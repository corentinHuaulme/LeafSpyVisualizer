package View;

import Controller.CSVReader;
import Model.CarData;
import org.jfree.data.io.CSV;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
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

        PanelChart panelChart = new PanelChart(cd);
        XYDataset charge = panelChart.getSeriesCharge();
        XYDataset temp = panelChart.getSeriesTemperatureBattery();
        this.setSize(800,800);
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        JPanel chart = panelChart.getPanelChart("test","Time","Charge of the battery (%)",charge);
        JPanel chart2 = panelChart.getPanelChart("test2","Time","Temperature of the battery (°C)",temp);

        var list = new JList();

        list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String name = (String) list.getSelectedValue();
            }
        });

        JScrollPane spane = new JScrollPane();
        spane.getViewport().add(list);

        JLabel label = new JLabel("Aguirre, der Zorn Gottes");
        label.setFont(new Font("Serif", Font.PLAIN, 12));

        spane.add(label);


        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        this.add(chart, c);

        c.fill = java.awt.GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridx = 2;
        c.gridy = 0;
        c.gridwidth = 1;

        this.add(new mapPanel(cd).getMap(), c);

        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 2;
        this.add(chart2, gc);

        gc.fill = GridBagConstraints.BOTH;
        gc.gridx = 2;
        gc.gridy = 1;
        gc.gridwidth = 1;

        this.add(spane,gc);

        this.setVisible(true);

        System.out.println(this.getLayout().getClass());
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("GridBagLayoutDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,800);

        //Set up the content pane.
        frame.getContentPane().add(new mainWindow());

        //Display the window.
        frame.pack();
        frame.setVisible(true);

    }
}
