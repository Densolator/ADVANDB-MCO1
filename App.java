import javax.swing.table.*;

import java.awt.Component;
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

public class App extends JFrame{

	
static JPanel mainPanel = new JPanel();
private DefaultTableModel model_table;
private JScrollPane scroll_table;

String[] names = {"-----Select the focus of your analysis-----", "Students", "Farmers"};

JFrame frame = new JFrame("ADVANDB MCO1");
JPanel queryPanel = new JPanel();
JPanel buttonPanel = new JPanel();
JPanel resultsPanel = new JPanel();
JPanel statementPanel = new JPanel();
JTable table;
JScrollPane scroll;

JComboBox combo = new JComboBox(names);

JLabel scopelabel = new JLabel("Scope:  ");
ButtonGroup scope = new ButtonGroup();
JRadioButton mun = new JRadioButton("Municipality level");
JRadioButton zone = new JRadioButton("Zone level");
JRadioButton brgy = new JRadioButton("Barangay level");
JRadioButton purok = new JRadioButton("Purok level");
JRadioButton summary = new JRadioButton("Summary");



JLabel empty = new JLabel("                                                        ");
JLabel detailslabel = new JLabel("Details:  ");
ButtonGroup details = new ButtonGroup();
JRadioButton water = new JRadioButton("Water source");
JRadioButton nearest_wsource = new JRadioButton("Average distance to nearest water source");
JRadioButton dec_water = new JRadioButton("Experienced decreased water level in the last 3 years");
JRadioButton detailsnone = new JRadioButton("None");
//JRadioButton mun = new JRadioButton("Nearest water source");


JToggleButton moredetails_button = new JToggleButton("More details");
JLabel student_details = new JLabel("Student grade level");
String[] grade_levels = {"1 Day Care", 
                         "2 Nursery/Kindergarten/Preparatory",
                         "11 Grade 1",
                         "12 Grade 2",
                         "13 Grade 3",
                         "14 Grade 4",
                         "15 Grade 5",
                         "16 Grade 6",
                         "17 Grade 7",
                         "18 Grade 8",
                         "19 Grade 9/3rd Year HS",
                         "20 Grade 10/4th Year HS",
                         "21 Grade 11",
                         "22 Grade 12",
                         "23 1st year PS PS/N-T/TV",
                         "24 2nd year PS PS/N-T/TV",
                         "25 3rd year PS PS/N-T/TV",
                         "31 1st year College",
                         "32 2nd year College",
                         "33 3rd year College",
                         "34 4th year College or higher",
                         "41 Post grad with units",
                         "51 ALS Elementary",
                         "52 ALS Secondary",
                         "53 SPED Elementary",
                         "54 SPED Secondary"};
JComboBox student_level = new JComboBox(grade_levels);
JLabel crop_details = new JLabel("Type of crop");
String[] crop_types = {"1 Sugar Cane",
						"2 Palay",
						"3 Corn",
						"4 Coffee",
						"5 Other Crops"};
JComboBox planted_crops = new JComboBox(crop_types);


JButton run = new JButton("Run set query");

ArrayList<String> columnNames = new ArrayList<String>();
ArrayList<ArrayList> data = new ArrayList<ArrayList>();
//static String DB_URL = "jdbc:mysql://localhost:3306/new_schema";
String DB_URL = "jdbc:mysql://localhost:3306/marinduque_clone?allowPublicKeyRetrieval=true&useSSL=false";
String USER = "root";
String PASS = "root";
String sql;
String select;
String select_query1,
	   select_query2,
	   select_query3,
	   from_query,
	   where_query1,
	   where_query2,
	   group_by1,
	   group_by2;
double startTime;
double endTime;
double executionTime;

public App ()
{
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	this.init();
}


private void init() {
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    Main();
}   
private void Main() {
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	
	buttonPanel.setPreferredSize(new Dimension(150,70));
	buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.Y_AXIS));///
	buttonPanel.add(run);
	
	
	statementPanel.setPreferredSize(new Dimension(700,700));
	statementPanel.removeAll();

    queryPanel.setPreferredSize(new Dimension(700,200));
	queryPanel.setLayout(new BoxLayout(queryPanel,BoxLayout.Y_AXIS));
	queryPanel.add(combo);
	queryPanel.add(statementPanel);

    mainPanel.add(buttonPanel);
	mainPanel.add(queryPanel);
	mainPanel.add(resultsPanel);
	
	frame.add(mainPanel);
    frame.setSize(1000, 800);
    frame.setVisible(true);
    
    scope.add(mun);
    scope.add(zone);
    scope.add(brgy);
    scope.add(purok);
    scope.add(summary);
    
    statementPanel.add(scopelabel);
    statementPanel.add(mun);
    statementPanel.add(zone);
    statementPanel.add(brgy);
    statementPanel.add(purok);
//    statementPanel.add(summary);
    
    details.add(water);
    details.add(nearest_wsource);
    details.add(dec_water);
    details.add(detailsnone);
    
    statementPanel.add(empty);
    
    statementPanel.add(detailslabel);
    statementPanel.add(water);
    statementPanel.add(nearest_wsource);
    statementPanel.add(dec_water);
    statementPanel.add(detailsnone);
    
    statementPanel.add(moredetails_button);
    student_details.setVisible(false);
    student_level.setVisible(false);
    crop_details.setVisible(false);
    planted_crops.setVisible(false);
    statementPanel.add(student_details);
    statementPanel.add(student_level);
    statementPanel.add(crop_details);
    statementPanel.add(planted_crops);
    
    moredetails_button.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
           JToggleButton button = (JToggleButton)e.getSource();
           if (button.isSelected()) 
           {
              switch(combo.getSelectedIndex())
              {
              	case 0: break;
              	case 1: student_details.setVisible(true);
      	    		  	student_level.setVisible(true);
      	    		  	break;
              	case 2: crop_details.setVisible(true);
      	              	planted_crops.setVisible(true);
      	              	break;
              }
           } 
           else 
           {
        	   student_details.setVisible(false);
        	   student_level.setVisible(false);
        	   crop_details.setVisible(false);
        	   planted_crops.setVisible(false);
           }
        }
     });
    
    combo.addItemListener(new ItemListener() {

		@Override
		public void itemStateChanged(ItemEvent e) {
			// TODO Auto-generated method stub
			if(e.getSource() == combo)
			{
				switch(combo.getSelectedIndex())
				{
				case 0: 
						break;
						
				case 1: 
						select_query2 = " ,COUNT(is_student = TRUE) AS 'number of students' ";
						from_query = "FROM facttable, members ";
						where_query1 = "WHERE facttable.id = members.hh_id AND is_student = TRUE ";
						break;

				case 2: 
						select_query2 = " ,COUNT(is_farmer = TRUE) AS 'number of farmers' ";
						from_query = "FROM facttable, farming ";
						where_query1 = "WHERE facttable.id = farming.hh_id AND is_farmer = TRUE ";
						break;

				}
			}
		}
		
	});
    
    //Scope action listeners
    mun.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            select_query1 = "SELECT mun ";
            group_by1 = "GROUP BY mun ";

        }
    });
    
    zone.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            select_query1 = "SELECT zone ";
            group_by1 = "GROUP BY zone ";

        }
    });
    
    brgy.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            select_query1 = "SELECT brgy ";
            group_by1 = "GROUP BY brgy ";

        }
    });
    
    purok.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            select_query1 = "SELECT purok ";
            group_by1 = "GROUP BY purok ";

        }
    });
    
    //Details action listeners
    water.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            select_query3 = ", water, COUNT(water) AS 'number of " + combo.getSelectedItem().toString() +" that use this water source' " ;
            group_by2 = ",water ";
            where_query2 = " ";
            moredetails_button.setEnabled(true);
        }
    });
    
    nearest_wsource.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            select_query3 = ", AVG(water_dist) AS 'average distance to nearest water source' " ;
            group_by2 = "";
            where_query2 = " ";
            moredetails_button.setEnabled(true);

        }
    });
    
    dec_water.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            select_query3 = ",  water_supply AS 'Most common answer', COUNT(water_supply) AS 'number of times answered' " ;
            group_by2 = " ORDER BY 'most prevalent answer' DESC LIMIT 1 ";
            where_query2 = " ";
            moredetails_button.setEnabled(true);
        }
    });
    
    detailsnone.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            select_query3 = " " ;
            group_by2 = " ";
            where_query2 = " ";
            moredetails_button.setEnabled(false);

        }
    });
    

	run.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			resultsPanel.removeAll();
	    	Connection conn = null;
			Statement stmt = null;
			try
			{

			   System.out.println("Connecting to database...");
			   conn = DriverManager.getConnection(DB_URL,USER,PASS);

			   System.out.println("Creating statement...");
			   stmt = conn.createStatement();
			   
			   if(moredetails_button.isSelected() && moredetails_button.isEnabled())
			   {
				   switch(combo.getSelectedIndex())
				   {
				   case 0: break;
				   case 1: where_query2 = " AND student_level = " + student_level.getSelectedItem().toString().substring(0,2) + " ";
				   		   break;
				   case 2: where_query2 = " AND crop_type = " + planted_crops.getSelectedItem().toString().substring(0,2) + " ";
		   		   		   break;
				   }
			   }
			   
			   sql = select_query1 + select_query2 + select_query3 + from_query + where_query1 + where_query2 + group_by1 + group_by2;
			   System.out.println(sql);
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
			
//			System.out.println(sql);
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

