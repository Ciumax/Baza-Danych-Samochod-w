package ApplicationPackage;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

public class Chart extends JFrame
{
    public Chart(String appTitle, String chartTitle, String[] desc, int[]values)
    {
        super(appTitle);
        PieDataset dataset = createDataset(desc, values);        
        JFreeChart chart = createChart(dataset, chartTitle);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 500));
        setContentPane(chartPanel);
        setPreferredSize(new Dimension(500, 400));
        setResizable(false);
        pack();
    }
    
    public PieDataset createDataset(String[] desc, int[]values)
    {
        DefaultPieDataset result = new DefaultPieDataset();
        for(int i = 0; i < desc.length; i++)
        {
            result.setValue(desc[i], values[i]);
        }
        return result;
    }
    
     
    public JFreeChart createChart(PieDataset dataset, String title)
    {
        JFreeChart chart = ChartFactory.createPieChart3D(title, dataset, true, true, false);
      //  PiePlot3D plot = (PiePlot3D) chart.getPlot();
       // plot.setStartAngle(0);
     //  plot.setDirection(Rotation.CLOCKWISE);
       // plot.setForegroundAlpha((float)0.5);
        return chart;
    }
    
    public JFreeChart createBarChart(DefaultCategoryDataset dataset, String title)
    {
        JFreeChart chart = ChartFactory.createBarChart(title,"wóz", "Profit", dataset, PlotOrientation.VERTICAL,false, true, false);

      //  PiePlot3D plot = (PiePlot3D) chart.getPlot();
       // plot.setStartAngle(0);
     //  plot.setDirection(Rotation.CLOCKWISE);
       // plot.setForegroundAlpha((float)0.5);
        return chart;
    }

    private DefaultCategoryDataset  createBarDataset(double d, String[] desc, int[] values) {
         DefaultCategoryDataset  result = new DefaultCategoryDataset ();
        for(int i = 0; i < desc.length; i++)
        {
            result.setValue(values[i],"Car", desc[i]);
        }
        return result;
    }
}
