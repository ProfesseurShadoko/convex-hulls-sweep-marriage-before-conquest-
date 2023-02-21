
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import javax.swing.JFrame;


public class Scatter extends JFrame {

    public Scatter(Dataset data) {
        super(data.scatterTitle());

        // Create dataset
        XYDataset dataset = createDataset(data);

        // Create chart
        JFreeChart chart = ChartFactory.createScatterPlot(
                data.scatterTitle(),
                "X-Axis", "Y-Axis", dataset);

        // Create Panel
        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
        this.show_me();
    }
    
    

    private XYDataset createDataset(Dataset data) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series1 = new XYSeries("Data");
        XYSeries series2 = new XYSeries("Sweep hull");
        XYSeries series3 = new XYSeries("Main hull");

        // Add data to the series
        for (Point point : data.points) {
        	series1.add(point.x,point.y);
        }
        for (Point point : data.sweep) {
        	series2.add(point.x,point.y);
        }
        for (Point point : data.main) {
        	series3.add(point.x,point.y);
        }
        
        
        dataset.addSeries(series3);
        dataset.addSeries(series2);
        dataset.addSeries(series1);

        return dataset;
    }
    
    public void show_me() {
    	this.setSize(600, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

}