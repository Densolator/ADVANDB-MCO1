import javax.swing.table.*;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;

public class Main extends JFrame{

static JPanel mainPanel = new JPanel();
private DefaultTableModel model_table;
private JScrollPane scroll_table;

static String[] names = {"-----Select your query-----","List all households with at least x electronics",
		"List all households which have experienced death by x and its counts", "etc."};/////

static JFrame frame = new JFrame("hello");
static JPanel queryPanel = new JPanel();
static JPanel buttonPanel = new JPanel();
static JPanel resultsPanel = new JPanel();
static JPanel statementPanel = new JPanel();
static JTextArea textArea = new JTextArea(20,40); ///
static JTable table;
static JScrollPane scroll;
static JButton run = new JButton("Run query from text area");
static JComboBox combo = new JComboBox(names);

//components for query 1
static SpinnerModel model = new SpinnerNumberModel(1, //initial value
                               				1, //min
                               				100, //max
                               				1);  //step  
static JLabel query1label1 = new JLabel("Find all families who have at least");
static JLabel query2label1 = new JLabel("Find all families and its counts of who experienced death by");//////
static JSpinner query1spinner = new JSpinner(model);
static String[] query1electronics = {"radio", "tv", "stereo", "karaoke","ref", "efan",  "iron", "wmach", "microw", "computer", "celfone", "telefone", "airc", "sewmach"};
static String[] query2death = {"Diseases of the heart", "Diseases of the vascular system",
		"Pneumonia", "Tuberculosis", "Cancer", "Diarrhea", "Measles",
		"Complication during pregnancy of childbirth", "Accident", "Diabetes", "Disease on the lungs",
		"Disease of the kidney", "Drowned from flood", "Victim of landslide", "Electrocuted during typhoon",
		"Murder", "Others"};////
static JComboBox query1electronicsCombo = new JComboBox(query1electronics);
static JComboBox query2deathCombo = new JComboBox(query2death);////
static JButton test = new JButton("Run set query");

static ArrayList<String> columnNames = new ArrayList<String>();
static ArrayList<ArrayList> data = new ArrayList<ArrayList>();
//static String DB_URL = "jdbc:mysql://localhost:3306/new_schema";
static String DB_URL = "jdbc:mysql://localhost:3306/advandb_mco1?useSSL=false";
static String USER = "root";
static String PASS = "root";
static String sql;
static double startTime;
static double endTime;
static double executionTime;


public static void main(String[] args) {
    Main main = new Main();
}   
public Main() {
	
	buttonPanel.setPreferredSize(new Dimension(200,70));
	buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.Y_AXIS));///
	buttonPanel.add(run);
	buttonPanel.add(test);
	
	
	statementPanel.setPreferredSize(new Dimension(500,200));
	statementPanel.add(query1label1);
	statementPanel.add(query1spinner);
	statementPanel.add(query1electronicsCombo);
	statementPanel.add(query2label1);
	statementPanel.add(query2deathCombo);
	statementPanel.removeAll();

    queryPanel.setPreferredSize(new Dimension(400,200));
	queryPanel.setLayout(new BoxLayout(queryPanel,BoxLayout.Y_AXIS));
	queryPanel.add(combo);
	queryPanel.add(statementPanel);
	queryPanel.add(textArea);
    /*
    table = new JTable();
    model_table = new DefaultTableModel();
    model_table.addColumn("1");
    model_table.addColumn("2");
    model_table.addColumn("3");
    model_table.addColumn("4");
    model_table.addColumn("5");
    model_table.addColumn("6");
    model_table.addColumn("7");
    model_table.addColumn("8");
    model_table.addColumn("9");
    table.setModel(model_table);
	   table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

    for(int i=0;i<30;i++){                             // add value to table
           Vector<String> r  = new Vector<String>();
           r.addElement("a");
           r.addElement("b");
           r.addElement("c");
           r.addElement("d");
           r.addElement("e");
           r.addElement("f");
           r.addElement("g");
           r.addElement("h");
            model_table.addRow(r);
    } 

    scroll_table = new JScrollPane(table);            // add table to scroll panel
    scroll_table.setBounds(1, 2, 100, 100);
    scroll_table.setVisible(true);
    

    resultsPanel.add(scroll_table);
*/

    
    mainPanel.add(buttonPanel);
	mainPanel.add(queryPanel);
	mainPanel.add(resultsPanel);
	
	frame.add(mainPanel);
    frame.setSize(500, 800);
    frame.setVisible(true);
    
    combo.addItemListener(new ItemListener() {

		@Override
		public void itemStateChanged(ItemEvent e) {
			// TODO Auto-generated method stub
			if(e.getSource() == combo)
			{
				switch(combo.getSelectedIndex())
				{
				case 0: statementPanel.removeAll();
						statementPanel.validate();
						break;
				case 1: statementPanel.removeAll();
						statementPanel.add(query1label1);
						statementPanel.add(query1spinner);
						statementPanel.add(query1electronicsCombo);
						statementPanel.validate();
						break;

				case 2: statementPanel.removeAll();
						statementPanel.add(query2label1);
						statementPanel.add(query2deathCombo);
						statementPanel.validate();
						break;
				case 3: statementPanel.removeAll();
						statementPanel.validate();
						break;
				}
//				label1.setText(combo.getSelectedItem().toString());
			}
		}
		
	});
    
    run.addActionListener(new ActionListener(){  
	    public void actionPerformed(ActionEvent e){
	    	resultsPanel.removeAll();
	    	sql = textArea.getText();
	    	Connection conn = null;
			Statement stmt = null;
			try
			{

			   System.out.println("Connecting to database...");
			   conn = DriverManager.getConnection(DB_URL,USER,PASS);

			   System.out.println("Creating statement...");
			   stmt = conn.createStatement();
			      
			   sql = textArea.getText(); //"SELECT COUNT(cshforwrk_mem_refno) FROM hpq_cshforwrk_mem"
			   startTime = System.currentTimeMillis();
			   ResultSet rs = stmt.executeQuery(sql);
			   endTime = System.currentTimeMillis();
			   
			   executionTime = (endTime - startTime)/1000.00000000000;
			   System.out.println("Execution time is:" + executionTime + " seconds");
			   System.out.println(executionTime);
			   
			   table = new JTable();
			   table.setModel(buildTableModel(rs));
			   table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			   scroll = new JScrollPane(table);
			   scroll.setBounds(1, 2, 100, 100);
			   scroll.setVisible(true);
			   
			   resultsPanel.add(scroll);
			   resultsPanel.revalidate();
			   resultsPanel.repaint();
			      
			   rs.close();
			   stmt.close();
			   conn.close();
			      
			}
			catch(SQLException se)
			{
			   //Handle errors for JDBC
			   se.printStackTrace();
			}
			   
			catch(Exception ex)
			{
			   //Handle errors for Class.forName
			   ex.printStackTrace();
			}
			   
			finally
			{
			   try
			   {
			      if(stmt!=null)
			         stmt.close();
			   }
			   catch(SQLException se2)
			   {
				   //pag umabot pa dito wala na talaga hehe
			   }
			      
			   try
			   {
			      if(conn!=null)
			         conn.close();
			   }
			   catch(SQLException se)
			   {
			      se.printStackTrace();
			   }
			}
			   
			System.out.println("Goodbye!");
	    }  
	    }); 
	
	test.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			resultsPanel.removeAll();
			sql = textArea.getText();
	    	Connection conn = null;
			Statement stmt = null;
			try
			{

			   System.out.println("Connecting to database...");
			   conn = DriverManager.getConnection(DB_URL,USER,PASS);

			   System.out.println("Creating statement...");
			   stmt = conn.createStatement();
			   
			   if(e.getSource() == test)
				{
					switch(combo.getSelectedIndex())
					{
					case 0: 
							break;
					case 1: 
							sql = "SELECT `zone`, `brgy`, `purok`, `hcn` FROM hpq_hh WHERE " + query1electronicsCombo.getSelectedItem().toString() + ">= " + (int)query1spinner.getValue(); //"SELECT COUNT(cshforwrk_mem_refno) FROM hpq_cshforwrk_mem"
							break;
					case 2: 
							sql = "SELECT hpq_hh.`main.id`, COUNT(hpq_hh.ndeath) FROM hpq_hh, hpq_death"+ 
									" WHERE hpq_hh.`main.id` = hpq_death.`main.id`" + 
									" AND hpq_death.mdeady = " + (query2deathCombo.getSelectedIndex()+1) +
									" GROUP BY hpq_hh.`main.id`";
							break;
					case 3: 
							break;
					}
				}
			   
			   				   
			   startTime = System.currentTimeMillis();
			   ResultSet rs = stmt.executeQuery(sql);
			   endTime = System.currentTimeMillis();
			   
			   executionTime = (endTime - startTime)/1000.00000000000;
			   System.out.println("Execution time is:" + executionTime + " seconds");
			   System.out.println(executionTime);
			   
			   table = new JTable();
			   table.setModel(buildTableModel(rs));
			   table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			   scroll = new JScrollPane(table);
			   scroll.setBounds(1, 2, 100, 100);
			   scroll.setVisible(true);
			   
			   resultsPanel.add(scroll);
			   resultsPanel.revalidate();
			   resultsPanel.repaint();
			      
			   rs.close();
			   stmt.close();
			   conn.close();
			      
			}
			catch(SQLException se)
			{
			   //Handle errors for JDBC
			   se.printStackTrace();
			}
			   
			catch(Exception ex)
			{
			   //Handle errors for Class.forName
			   ex.printStackTrace();
			}
			   
			finally
			{
			   try
			   {
			      if(stmt!=null)
			         stmt.close();
			   }
			   catch(SQLException se2)
			   {
				   //pag umabot pa dito wala na talaga hehe
			   }
			      
			   try
			   {
			      if(conn!=null)
			         conn.close();
			   }
			   catch(SQLException se)
			   {
			      se.printStackTrace();
			   }
			}
			   
			System.out.println("Goodbye!");
			
		}
		
	});
    
    
    
    
  }







public static DefaultTableModel buildTableModel(ResultSet rs)
        throws SQLException {

    ResultSetMetaData metaData = rs.getMetaData();

    // names of columns
    Vector<String> columnNames = new Vector<String>();
    int columnCount = metaData.getColumnCount();
    for (int column = 1; column <= columnCount; column++) {
        columnNames.add(metaData.getColumnName(column));
    }

    // data of the table
    Vector<Vector<Object>> data = new Vector<Vector<Object>>();
    while (rs.next()) {
        Vector<Object> vector = new Vector<Object>();
        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
            vector.add(rs.getObject(columnIndex));
        }
        data.add(vector);
    }

    return new DefaultTableModel(data, columnNames);

}



}



