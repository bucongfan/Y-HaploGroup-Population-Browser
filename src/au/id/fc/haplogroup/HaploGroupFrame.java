/*
 * Project: HaploGroupBrowser
 * File: "au.id.fc.haplogroup/HaploGroupFrame.java
 * Developer: Felix Jeyareuben <i@fc.id.au>
 * Website: www.y-str.org
 */
package au.id.fc.haplogroup;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.AbstractListModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.StatisticalBarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.statistics.DefaultStatisticalCategoryDataset;

import au.id.fc.haplogroup.objects.HaploGroupRecord;
import au.id.fc.haplogroup.objects.RecordValue;

public class HaploGroupFrame extends JFrame
{

    private final JPanel contentPanel = new JPanel();
    DefaultPieDataset pieDataset = null;
    JFreeChart chart = null;
    ChartPanel panel_obj = new ChartPanel(ChartFactory.createPieChart("Y-DNA HaploGroup Browser", new DefaultPieDataset(), true, true, true));
    JFreeChart chart_closest = null;
    ChartPanel panel_closest = new ChartPanel(ChartFactory.createPieChart("Y-DNA HaploGroup Browser", new DefaultPieDataset(), true, true, true));
    JFreeChart chart_ancestral = null;
    ChartPanel panel_ancestral = new ChartPanel(ChartFactory.createPieChart("Y-DNA HaploGroup Browser", new DefaultPieDataset(), true, true, true));
    JTabbedPane tabbedPane;
    JSlider slider;
    JList list;
    JLabel lblNewLabel = new JLabel();

    public static void main(String[] args)
    {
	try
	{
	    HaploGroupFrame dialog = new HaploGroupFrame();
	    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	    dialog.setVisible(true);
	} catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public JFrame getDialog()
    {
	return this;
    }
    
    public void autoSelectList()
    {
	list.setSelectedValue("Indian Dravidians", true);
    }

    /**
     * Create the dialog.
     */
    public HaploGroupFrame()
    {
	setIconImage(Toolkit.getDefaultToolkit().getImage(HaploGroupFrame.class.getResource("/au/id/fc/haplogroup/ico/icon.png")));
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	addWindowListener(new WindowAdapter()
	{
	    @Override
	    public void windowClosed(WindowEvent arg0)
	    {
		try
		{
		    DbUtils.Shutdown();
		} catch (SQLException e)
		{
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	});
	setBounds(100, 100, 986, 752);
	setTitle("Y-DNA HaploGroup Browser");
	getContentPane().setLayout(new BorderLayout());
	contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
	getContentPane().add(contentPanel, BorderLayout.CENTER);
	contentPanel.setLayout(new BorderLayout(0, 0));
	{
	    list = new JList();
	    list.addListSelectionListener(new ListSelectionListener()
	    {
		public void valueChanged(ListSelectionEvent arg0)
		{
		    //
		    JList listBox = (JList) arg0.getSource();
		    String population = (String) listBox.getSelectedValue();
		    pieDataset = new DefaultPieDataset();
		    HaploGroupRecord hgr;
		    ArrayList<HaploGroupRecord> hgr_array;
		    try
		    {
			// main panel
			hgr = DbUtils.getHaploRecord(population);
			setTitle("Y-DNA HaploGroup Browser : " + population);
			ArrayList<RecordValue> recVal = hgr.getHaploGroup();
			for (RecordValue rec : recVal)
			{
			    if (rec.getPercentAvailable() != 0)
				pieDataset.setValue(rec.getHaploGroup() + " - (" + Double.toString(rec.getPercentAvailable()) + ")", rec.getPercentAvailable());
			}
			int selected = tabbedPane.getSelectedIndex();

			chart = ChartFactory.createPieChart(population, pieDataset, true, true, true);
			chart.setTitle(population);
			panel_obj.setChart(chart);

			// closest panels
			hgr_array = DbUtils.getCloseRelatives(population, slider.getValue());
			chart_closest = createClosestRelativePopulationChart(createClosestRelativesDataset(hgr_array));
			panel_closest.setChart(chart_closest);

			// ancestral panels
			chart_ancestral = createAncestralPopulationChart(population);
			panel_ancestral.setChart(chart_ancestral);
			
			tabbedPane.setSelectedIndex(selected);
		    } catch (ClassNotFoundException e)
		    {
			e.printStackTrace();
		    } catch (SQLException e)
		    {
			e.printStackTrace();
		    }
		}
	    });
	    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    try
	    {
		list.setModel(new AbstractListModel()
		{
		    Object[] values = DbUtils.getAllPopulations();

		    public int getSize()
		    {
			return values.length;
		    }

		    public Object getElementAt(int index)
		    {
			return values[index];
		    }
		});
	    } catch (ClassNotFoundException e)
	    {
		e.printStackTrace();
	    } catch (SQLException e)
	    {
		e.printStackTrace();
	    }
	    {
		JScrollPane scrollPane = new JScrollPane(list);
		contentPanel.add(scrollPane, BorderLayout.WEST);
	    }
	}
	{
	    tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	    contentPanel.add(tabbedPane, BorderLayout.CENTER);
	    tabbedPane.addTab("HaploGroup Distribution", null, panel_obj, null);
	    tabbedPane.addTab("Closest Relative Population", null, panel_closest, null);
	    tabbedPane.addTab("Ancestral Population", null, panel_ancestral, null);
	    panel_ancestral.setLayout(new BorderLayout(0, 0));
	}
	{
	    JPanel buttonPane = new JPanel();
	    buttonPane.setLayout(new FlowLayout(FlowLayout.LEFT));
	    getContentPane().add(buttonPane, BorderLayout.SOUTH);
	    {
		JLabel lblNewLabel_1 = new JLabel("Relative Factor:");
		buttonPane.add(lblNewLabel_1);
	    }
	    {
		slider = new JSlider();
		slider.setValue(20);
		slider.setMaximum(50);
		slider.setMinimum(1);
		slider.addChangeListener(new ChangeListener()
		{
		    public void stateChanged(ChangeEvent arg0)
		    {
			JSlider s = (JSlider) arg0.getSource();
			lblNewLabel.setText(Integer.toString(s.getValue()));
			try
			{
			    ArrayList<HaploGroupRecord> hgr_array = DbUtils.getCloseRelatives((String) list.getSelectedValue(), slider.getValue());
			    chart_closest = createClosestRelativePopulationChart(createClosestRelativesDataset(hgr_array));
			    panel_closest.setChart(chart_closest);
			    if (tabbedPane.getSelectedIndex() != 1)
				tabbedPane.setSelectedIndex(1);
			} catch (ClassNotFoundException e)
			{
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			} catch (SQLException e)
			{
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
		    }
		});
		buttonPane.add(slider);
	    }
	    {
		lblNewLabel = new JLabel("20");
		buttonPane.add(lblNewLabel);
	    }
	}
    }

    private CategoryDataset createClosestRelativesDataset(ArrayList<HaploGroupRecord> hgr_array) throws ClassNotFoundException, SQLException
    {

	DefaultCategoryDataset result = new DefaultCategoryDataset();

	ArrayList<RecordValue> rv = null;
	for (HaploGroupRecord hgr : hgr_array)
	{
	    rv = hgr.getHaploGroup();
	    for (RecordValue r : rv)
	    {
		result.addValue(r.getPercentAvailable(), r.getHaploGroup(), hgr.getPopulation());
	    }
	}
	return result;
    }

    private JFreeChart createClosestRelativePopulationChart(final CategoryDataset dataset)
    {
	JFreeChart chart = ChartFactory.createStackedBarChart("Closest Relative Population", "Population", "HaploGroups", dataset, PlotOrientation.VERTICAL, true, true, false);
	return chart;
    }

    private JFreeChart createAncestralPopulationChart(String population) throws ClassNotFoundException, SQLException
    {	
	DefaultStatisticalCategoryDataset dataset = new DefaultStatisticalCategoryDataset();	
	ArrayList<RecordValue> rv_array = DbUtils.getHaploRecord(population).getHaploGroup();

	double pop_val;
	String max_pop=null;
	Object[] o=null;
	HashMap<String, Object[]> maxlist = DbUtils.getMaxHaploRecords();
	//ancestral_report
	//
	ArrayList<HaploGroupRecord> max_pop_arr=new ArrayList<HaploGroupRecord>();
	for(RecordValue rv:rv_array)
	{
	    pop_val=rv.getPercentAvailable();
	    //if(pop_val<5)
	    //	continue;
	    o=maxlist.get(rv.getHaploGroup());
	    if(o!=null)
	    {
    	    	max_pop=(String) o[0];
    	    	HaploGroupRecord hgr=DbUtils.getHaploRecord(max_pop);
    	    	max_pop_arr.add(hgr);
	    }
	}
	//
	
	for(RecordValue rv:rv_array)
	{
	    pop_val=rv.getPercentAvailable();
	    //if(pop_val<5)
	    //	continue;
	    for(HaploGroupRecord hgr:max_pop_arr)
	    {
		 dataset.add(pop_val, 0, population, rv.getHaploGroup());
		 for(HaploGroupRecord hgr2:max_pop_arr)
		 {
		     for(RecordValue rv2:hgr2.getHaploGroup())
		     {
			 if(rv2.getHaploGroup().equalsIgnoreCase(rv.getHaploGroup()))
			 {
			     dataset.add(rv2.getPercentAvailable(), 0, hgr2.getPopulation(), rv2.getHaploGroup());			     
			 }
			 
		     }		     
		 }
	    }	    
	}
	
	CategoryAxis xAxis = new CategoryAxis("HaploGroup Type");
        xAxis.setLowerMargin(0.01d);
        xAxis.setUpperMargin(0.01d);
        xAxis.setCategoryMargin(0.1d);
        ValueAxis yAxis = new NumberAxis("Percent Value");

        // define the plot
        CategoryItemRenderer renderer = new StatisticalBarRenderer();
        CategoryPlot plot = new CategoryPlot(dataset, xAxis, yAxis, renderer);

        JFreeChart chart = new JFreeChart("Ancestral Population",plot);
            
	return chart;
    }
}
