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

public class Main extends JFrame{

	
static JPanel mainPanel = new JPanel();
private DefaultTableModel model_table;
private JScrollPane scroll_table;

static String[] names = {"-----Select your query-----","List all households with at least x electronics",
		"List all households which have experienced death by x and its counts",
		"List the number of fishes caught per household in kilograms, their equipments, years of fishing experience, asked if they were hungry in the past 3 months, and boat ownerships",
		"List the number of farmers in a household, the average harvested crop volume, the annual average family income, and the number of members who are under the cash for work program"};/////

static JFrame frame = new JFrame("ADVANDB MCO1");
static JPanel queryPanel = new JPanel();
static JPanel buttonPanel = new JPanel();
static JPanel resultsPanel = new JPanel();
static JPanel statementPanel = new JPanel();
static JTextArea textArea = new JTextArea(20,40); ///
static JTable table;
static JScrollPane scroll;
static JComboBox combo = new JComboBox(names);


//components for query 1
static SpinnerModel model = new SpinnerNumberModel(1, //initial value
                               				1, //min
                               				100, //max
                               				1);  //step  
static JLabel query1label1 = new JLabel("Find all families who have at least");
static JSpinner query1spinner = new JSpinner(model);
static String[] query1electronics = {"radio", "tv", "stereo", "karaoke","ref", "efan",  "iron", "wmach", "microw", "computer", "celfone", "telefone", "airc", "sewmach"};
static String[] query1electronicsindex = {"radio_comp", "tv_comp", "stereo_comp", "karaoke_comp","ref_comp", "efan_comp",  "iron_comp", "wmach_comp", "microw_comp", "computer_comp", "cellphone_comp", "telephone_comp", "airc_comp", "sewmach_comp"};
static JComboBox query1electronicsCombo = new JComboBox(query1electronics);


//components for query 2
static String[] query2death = {"Diseases of the heart", "Diseases of the vascular system",
		"Pneumonia", "Tuberculosis", "Cancer", "Diarrhea", "Measles",
		"Complication during pregnancy of childbirth", "Accident", "Diabetes", "Disease on the lungs",
		"Disease of the kidney", "Drowned from flood", "Victim of landslide", "Electrocuted during typhoon",
		"Murder", "Others"};
static String[] query3equip = {"Fish net", "Electricity", "Bagnets", "Gillnets", "Traps", "Hook and line", "Sift net", "Others"};
static JComboBox query2deathCombo = new JComboBox(query2death);
static JComboBox query3equipCombo = new JComboBox(query3equip);
static JLabel query2label1 = new JLabel("Find all families and its counts of who experienced death by");


//components for query 3
static JLabel query3label1 = new JLabel("List the number of fishes caught per household in kilograms");
static JLabel query3label2 = new JLabel("their equipments, years of fishing experience,");
static JLabel query3label3 = new JLabel("asked if they were hungry in the past 3 months,");
static JLabel query3label4 = new JLabel("and boat ownerships using: ");

//components for query 4
static JLabel query4label1 = new JLabel("List the number of farmers in a household,");
static JLabel query4label2 = new JLabel(" the average harvested crop volume,");
static JLabel query4label3 = new JLabel("the annual average family income,");
static JLabel query4label4 = new JLabel(" and the number of members who are under the cash for work program for household number");

static SpinnerModel modelq4 = new SpinnerNumberModel(252162, //initial value
		0000001, //min
		1000000000, //max
		1);
static JSpinner query4spinner = new JSpinner(modelq4);
  //step 
static JSpinner.NumberEditor editor = new JSpinner.NumberEditor(query4spinner, "#");



static JButton run = new JButton("Run set query");

static ArrayList<String> columnNames = new ArrayList<String>();
static ArrayList<ArrayList> data = new ArrayList<ArrayList>();
//static String DB_URL = "jdbc:mysql://localhost:3306/new_schema";
static String DB_URL = "jdbc:mysql://localhost:3306/advandb_mco1?allowPublicKeyRetrieval=true&useSSL=false";
static String USER = "root";
static String PASS = "root";
static String sql;
static double startTime;
static double endTime;
static double executionTime;


public static void main(String[] args) {
	
    Main main = new Main();
    main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
}   
public Main() {
	Component mySpinnerEditor = query4spinner.getEditor();
	JFormattedTextField jftf = ((JSpinner.DefaultEditor)mySpinnerEditor).getTextField();
	jftf.setColumns(10);
	query4spinner.setEditor(editor);
	
	buttonPanel.setPreferredSize(new Dimension(200,70));
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
						statementPanel.add(query3label1);
						statementPanel.add(query3label2);
						statementPanel.add(query3label3);
						statementPanel.add(query3label4);
						statementPanel.add(query3equipCombo);
						statementPanel.validate();
						break;
				case 4: statementPanel.removeAll();
						statementPanel.add(query4label1);
						statementPanel.add(query4label2);
						statementPanel.add(query4label3);
						statementPanel.add(query4label4);
						statementPanel.add(query4spinner);
						statementPanel.validate();
						break;
				}
			}
		}
		
	});
//    
//    run.addActionListener(new ActionListener(){  
//	    public void actionPerformed(ActionEvent e){
//	    	resultsPanel.removeAll();
//	    	sql = textArea.getText();
//	    	Connection conn = null;
//			Statement stmt = null;
//			try
//			{
//
//			   System.out.println("Connecting to database...");
//			   conn = DriverManager.getConnection(DB_URL,USER,PASS);
//
//			   System.out.println("Creating statement...");
//			   stmt = conn.createStatement();
//			      
//			   sql = textArea.getText(); //"SELECT COUNT(cshforwrk_mem_refno) FROM hpq_cshforwrk_mem"
//			   startTime = System.currentTimeMillis();
//			   ResultSet rs = stmt.executeQuery(sql);
//			   endTime = System.currentTimeMillis();
//			   
//			   executionTime = (endTime - startTime)/1000.00000000000;
//			   System.out.println("Execution time is:" + executionTime + " seconds");
//			   System.out.println(executionTime);
//			   
//			   table = new JTable();
//			   table.setModel(buildTableModel(rs));
//			   table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//			   scroll = new JScrollPane(table);
//			   scroll.setBounds(1, 2, 100, 100);
//			   scroll.setVisible(true);
//			   
//			   resultsPanel.add(scroll);
//			   resultsPanel.revalidate();
//			   resultsPanel.repaint();
//			      
//			   rs.close();
//			   stmt.close();
//			   conn.close();
//			      
//			}
//			catch(SQLException se)
//			{
//			   //Handle errors for JDBC
//			   se.printStackTrace();
//			}
//			   
//			catch(Exception ex)
//			{
//			   //Handle errors for Class.forName
//			   ex.printStackTrace();
//			}
//			   
//			finally
//			{
//			   try
//			   {
//			      if(stmt!=null)
//			         stmt.close();
//			   }
//			   catch(SQLException se2)
//			   {
//				   //pag umabot pa dito wala na talaga hehe
//			   }
//			      
//			   try
//			   {
//			      if(conn!=null)
//			         conn.close();
//			   }
//			   catch(SQLException se)
//			   {
//			      se.printStackTrace();
//			   }
//			}
//			   
//			System.out.println("Goodbye!");
//	    }  
//	    }); 
	
	run.addActionListener(new ActionListener() {

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
			   
			   if(e.getSource() == run)
				{
					switch(combo.getSelectedIndex())
					{
					case 0: 
							break;
					case 1: 
							sql = "SELECT `zone`, `brgy`, `purok`, `hcn` FROM hpq_hh USE INDEX(" + query1electronicsindex[query1electronicsCombo.getSelectedIndex()] +") WHERE " + query1electronicsCombo.getSelectedItem().toString() + ">= " + (int)query1spinner.getValue(); //"SELECT COUNT(cshforwrk_mem_refno) FROM hpq_cshforwrk_mem"
							break;
					case 2: 
							sql = "SELECT hpq_hh.`main.id`, COUNT(hpq_hh.ndeath) FROM hpq_hh, hpq_death"+ 
									" WHERE hpq_hh.`main.id` = hpq_death.`main.id`" + 
									" AND hpq_death.mdeady = " + (query2deathCombo.getSelectedIndex()+1) +
									" GROUP BY hpq_hh.`main.id`";
							break;
					case 3: 
							sql = "SELECT hpq_hh.`main.id` AS ID, COUNT(hpq_aquani.aquanitype) AS \"Number of Fish Caught (KG)\"," + 
									"hpq_aquaequip.`aquaequiptype` AS ' Fishing Equipment Used', hpq_hh.`yrs_in_fishind` as \" Years of Fishing Experience\"," + 
									"hpq_hh.fshort as \"Got Hungry past 3 months?\", hpq_hh.`boat1` as \"Boat w/ Engine and Outrigger\"," + 
									"hpq_hh.`boat1_own` as \"Owned Boat w/ Engine and Outrigger?\", hpq_hh.`boat2` as \"Boat w/ Engine but w/o Outrigger\"," + 
									"hpq_hh.`boat2_own` as \"Owned Boatw/ Engine but w/o Outrigger?\", hpq_hh.`boat3` as \"Boat w/o Engine, w/Outrigger\"," + 
									"hpq_hh.`boat3_own` as \"Owned Boat w/o Engine, w/o Outrigger?\", hpq_hh.`boat4` as \"Boat w/o Engine, w/o Outrigger\"," + 
									"hpq_hh.`boat4_own` as \"Owned Boat w/o Engine, w/o Outrigger\", hpq_hh.`boat5` as \"Raft\", hpq_hh.`boat5_own` as \"Owned Raft?\"" + 
									"FROM hpq_hh, hpq_aquaequip, hpq_aquani " + 
									" WHERE hpq_hh.`main.id`=hpq_aquaequip.`main.id` AND hpq_hh.`main.id`=hpq_aquani.`main.id` AND hpq_aquaequip.aquaequiptype =" + (query3equipCombo.getSelectedIndex()+1) + 
									" GROUP BY ID" + 
									" ORDER BY COUNT(hpq_aquani.aquanitype) DESC";
							break;
					case 4:
							sql = "SELECT hpq_hh.`main.id` AS ID, COUNT(hpq_crop.`main.id`) AS 'Farmers in the household', AVG(hpq_crop.crop_vol) AS 'Average Crop Vol', AVG(hpq_mem.wagcshm) AS 'Average Income', a.CFW AS 'CashForWork Program Members' " + 
									"FROM hpq_hh, hpq_crop, hpq_mem, " + 
									"	(SELECT hpq_hh.`main.id` AS ID, COUNT(hpq_cshforwrk_mem.`main.id`) AS CFW" + 
									"	FROM hpq_hh  INNER JOIN  hpq_cshforwrk_mem" + 
									"	WHERE hpq_cshforwrk_mem.`main.id`= hpq_hh.`main.id` AND hpq_hh.`main.id`= " + (int)query4spinner.getValue() +
									"	GROUP BY ID) as a " + 
									"WHERE hpq_hh.`main.id`=hpq_crop.`main.id` AND hpq_hh.`main.id`=hpq_mem.`main.id`AND hpq_hh.`main.id`=" + (int)query4spinner.getValue() +
									" GROUP BY ID";
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
