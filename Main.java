import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Main {
	static String[] names = {"List all households with at least x electronics", "etc."};
	
	static JFrame frame = new JFrame("hello");
	static JPanel queryPanel = new JPanel();
	static JPanel buttonPanel = new JPanel();
	static JPanel resultsPanel = new JPanel();
	static JPanel statementPanel = new JPanel();
	static JTextArea textArea = new JTextArea(20,40); ///
	static JTable table;
	static JButton run = new JButton("Run query from text area");
	static JComboBox combo = new JComboBox(names);
	
	//components for query 1
	static SpinnerModel model = new SpinnerNumberModel(1, //initial value
	                               				1, //min
	                               				100, //max
	                               				1);  //step  
	static JLabel query1label1 = new JLabel("Find all families who have at least");
	static JSpinner query1spinner = new JSpinner(model);
	static String[] query1electronics = {"radio", "tv", "stereo", "karaoke","ref", "efan",  "iron", "wmach", "microw", "computer", "celfone", "telefone", "airc", "sewmach"};
	static JComboBox query1electronicsCombo = new JComboBox(query1electronics);
	static JButton test = new JButton("Run set query");
	
	static ArrayList<String> columnNames = new ArrayList<String>();
	static ArrayList<ArrayList> data = new ArrayList<ArrayList>();
//	static String DB_URL = "jdbc:mysql://localhost:3306/new_schema";
	static String DB_URL = "jdbc:mysql://localhost:3306/advandb_mco1?useSSL=false";
	static String USER = "root";
	static String PASS = "root";
	static String sql;
	static double startTime;
	static double endTime;
	static double executionTime;
	
	public static void main(String[] args) {
		
//		frame.show();
		//frame.setSize(500,500);
		frame.setBounds(500, 500, 500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new FlowLayout());
		
		
		queryPanel.setPreferredSize(new Dimension(400,200));
		queryPanel.setLayout(new BoxLayout(queryPanel,BoxLayout.Y_AXIS));
		queryPanel.add(combo);
		queryPanel.add(statementPanel);
		
		statementPanel.add(query1label1);
		statementPanel.add(query1spinner);
		statementPanel.add(query1electronicsCombo);
		//queryPanel.add(textfield);
		queryPanel.add(textArea);
		
		combo.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				if(e.getSource() == combo)
				{
					switch(combo.getSelectedIndex())
					{
					case 0: statementPanel.removeAll();
							statementPanel.add(query1label1);
							statementPanel.add(query1spinner);
							statementPanel.add(query1electronicsCombo);
							statementPanel.validate();
							break;
						
					case 1: statementPanel.remove(query1label1);
							statementPanel.validate();
							break;
					}
//					label1.setText(combo.getSelectedItem().toString());
				}
			}
			
		});

		
		buttonPanel.setPreferredSize(new Dimension(100,50));
		buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.Y_AXIS));///
		buttonPanel.add(run);
		buttonPanel.add(test);
		
		resultsPanel.setPreferredSize(new Dimension(400,200));
		
//		resultsPanel.setPreferredSize(new Dimension(200,200));
		frame.add(buttonPanel,BorderLayout.CENTER);
		frame.add(queryPanel,BorderLayout.CENTER);
		frame.add(resultsPanel,BorderLayout.CENTER);

		
		run.addActionListener(new ActionListener(){  
		    public void actionPerformed(ActionEvent e){
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
				   
				   table = new JTable(buildTableModel(rs));
				   
				   
				   resultsPanel.add(table);
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
				sql = textArea.getText();
		    	Connection conn = null;
				Statement stmt = null;
				try
				{

				   System.out.println("Connecting to database...");
				   conn = DriverManager.getConnection(DB_URL,USER,PASS);

				   System.out.println("Creating statement...");
				   stmt = conn.createStatement();
				      
				   sql = "SELECT COUNT('main.id') FROM hpq_hh WHERE " + query1electronicsCombo.getSelectedItem().toString() + ">= " + (int)query1spinner.getValue(); //"SELECT COUNT(cshforwrk_mem_refno) FROM hpq_cshforwrk_mem"
				   startTime = System.currentTimeMillis();
				   ResultSet rs = stmt.executeQuery(sql);
				   endTime = System.currentTimeMillis();
				   
				   executionTime = (endTime - startTime)/1000.00000000000;
				   System.out.println("Execution time is:" + executionTime + " seconds");
				   System.out.println(executionTime);
				   
				   table = new JTable(buildTableModel(rs));
				   
				   
				   resultsPanel.add(table);
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
		
//		panel.add(table);
		
		frame.show();
		
		
		
		
		final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";//  di na daw need sa bagong version ng mysql connector pero uncomment na lang pag kailangan pa sa ibang version
		
		
		   
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
