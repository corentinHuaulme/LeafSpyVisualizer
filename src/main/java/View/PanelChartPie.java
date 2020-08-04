package View;

import Model.CarData;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import java.util.ArrayList;

public class PanelChartPie extends PanelChart{

    public PanelChartPie(ArrayList<ArrayList<CarData>> cd){
        super(cd);
    }

    @Override
    public PieDataset getDatasetSpeed(){
        DefaultPieDataset  dset = new DefaultPieDataset ();
        dset.setValue("Motor Power",0.0);
        dset.setValue("A/C Power",0.0);
        dset.setValue("Aux Power",0.0);
        for(ArrayList<CarData> cd : this.getData()){
            for(CarData data : cd) {

                dset.setValue("Motor Power", ((Double)dset.getValue("Motor Power"))+data.getMotorPower());
                dset.setValue("A/C Power",((Double)(dset.getValue("A/C Power")))+data.getAcPower());
                dset.setValue("Aux Power", ((Double)(dset.getValue("Aux Power")))+data.getAuxPower());
            }
        }

        return dset;
    }

    @Override
    public JFreeChart getPanelChart(String title, String xLabel, String yLabel, Dataset dataset){


        JFreeChart chart = ChartFactory.createPieChart(
                title,
                (PieDataset) dataset,
                true,
                true,
                false
        );

        return chart;
    }


}

