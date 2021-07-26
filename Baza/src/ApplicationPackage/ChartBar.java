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

public class ChartBar extends JFrame
{
    public ChartBar(String appTitle, String chartTitle,String data1,String data2, String[] desc, int[]values)
    {
        super(appTitle);
        DefaultCategoryDataset dataset = createBarDataset(1,desc, values);        
        JFreeChart chart = createBarChart(dataset, chartTitle,data1,data2);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 500));
        setContentPane(chartPanel);
        setPreferredSize(new Dimension(500, 400));
        setResizable(false);
        pack();
    }
    
   
  
    
    public JFreeChart createBarChart(DefaultCategoryDataset dataset, String title,String data1,String data2)
    {
        JFreeChart chart = ChartFactory.createBarChart(title,data1, data2, dataset, PlotOrientation.VERTICAL,false, true, false);
        
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
