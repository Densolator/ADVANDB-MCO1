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
	static String[] names = {"Households with gadgets", "etc."};
	
	static JFrame frame = new JFrame("hello");
	static JPanel queryPanel = new JPanel();
	static JPanel buttonPanel = new JPanel();
	static JPanel resultsPanel = new JPanel();
	static JPanel statementPanel = new JPanel();
	static JTextField textfield = new JTextField("         ");
	static JTextArea textArea = new JTextArea(20,40); ///
	static JTable table;
	static JButton run = new JButton("Run Query");
	static JComboBox combo = new JComboBox(names);
	static JLabel label1 = new JLabel("Sample");
	
	static ArrayList<String> columnNames = new ArrayList<String>();
	static ArrayList<ArrayList> data = new ArrayList<ArrayList>();
//	static String DB_URL = "jdbc:mysql://localhost:3306/new_schema";
	static String DB_URL = "jdbc:mysql://localhost:3306/advandb_mco1?useSSL=false";
	static String USER = "root";
	static String PASS = "root";
	static String sql;
	static long startTime;
	static long endTime;
	static long executionTime;
	
	public static void main(String[] args) {
		
//		frame.show();
		//frame.setSize(500,500);
		frame.setBounds(500, 500, 500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new FlowLayout());
		
		

		textfield.setBounds(50, 50, 50, 50);
		
		queryPanel.setPreferredSize(new Dimension(400,200));
		queryPanel.setLayout(new BoxLayout(queryPanel,BoxLayout.Y_AXIS));
		queryPanel.add(combo);
		queryPanel.add(statementPanel);
		
		statementPanel.add(label1);
		statementPanel.add(textfield);
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
					case 0: statementPanel.remove(label1);
					statementPanel.validate();
					break;
					case 1: statementPanel.add(label1);
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
		
		resultsPanel.setPreferredSize(new Dimension(400,200));
		
//		resultsPanel.setPreferredSize(new Dimension(200,200));
		frame.add(buttonPanel,BorderLayout.CENTER);
		frame.add(queryPanel,BorderLayout.CENTER);
		frame.add(resultsPanel,BorderLayout.CENTER);

		
		run.addActionListener(new ActionListener(){  
		    public void actionPerformed(ActionEvent e){
		    	sql = textfield.getText();
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
				   
				   executionTime = (endTime - startTime)/1000;
				   System.out.println("Execution time is:" + executionTime + " seconds");
				   
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
