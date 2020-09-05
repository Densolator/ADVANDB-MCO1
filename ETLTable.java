import javax.swing.table.*;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.*;
import java.util.*;

import javax.swing.*;

public class ETLTable {
	Connection conn = null;	
	Statement stmt = null;
	Statement stmt2 = null;
	
	 

	//this class is exclusively for mco2 purpose
	
	void execute(String a, int b) {
		ResultSet rs = null;
		//start extracting. a is the sql
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			String url = "jdbc:mysql://localhost:3306/advandb_mco1?allowPublicKeyRetrieval=true&useSSL=false";
			String user = "root";
			String password = "root";
			conn = DriverManager.getConnection(url,user,password);

			float water_dist;
			stmt = conn.createStatement();
			stmt2 = conn.createStatement();
			
			String sql = new String(a);//sql statements
			rs = stmt.executeQuery(sql);
			if(b == 1) {//read hh
				stmt2.execute("DROP TABLE facttable;");
				while(rs.next()) {
					//read &write part
					
					int id = rs.getInt("main.id");
					int mun = rs.getInt("mun");
					int zone = rs.getInt("zone");
					int brgy = rs.getInt("brgy");
					int purok = rs.getInt("purok");
					int water =rs.getInt("water");
					water_dist = rs.getFloat("water_dist");
					int water_supply=-1;
					if(rs.getString("water_supply")!=null)
						water_supply = rs.getInt("water_supply");
					int low_wsupp=-1;
					if(rs.getString("low_wsupp")!=null)
						low_wsupp = rs.getInt("low_wsupp");
					int[] row = {id,mun,zone,brgy,purok,water,water_supply,low_wsupp};
					stmt2.executeUpdate(transform(row,b,water_dist));//pass a row
				}
			}
			else if(b == 2) {//mem
				stmt2.execute("DROP TABLE members;");
				while(rs.next()) {
					int hh_id = rs.getInt("main.id");
					int gradel;
					if (rs.getString("gradel")==null) gradel = 0;
					else gradel = rs.getInt("gradel");

					//System.out.print(rs.getString("gradel")+" ");
					int[] row = {hh_id,gradel};
					//stmt2.executeUpdate(transform(row,b,0));
				}
			}
			else if(b == 3) {//farm
				stmt2.execute("DROP TABLE farming;");
				while(rs.next()) {
					int hh_id = rs.getInt("main.id");
					int crop_type;
					if(rs.getString("croptype")==null) crop_type=0;
					else crop_type = rs.getInt("croptype");
					
					//System.out.print(rs.getString("croptype")+" ");
					int[] row = {hh_id,crop_type};
					//stmt2.executeUpdate(transform(row,b,0));
				}
			}
			
			//drop,copy,paste
			stmt2.execute("DROP TABLE marinduque_clone.farming, marinduque_clone.members, marinduque_clone.facttable;");//drop, copy, paste 
			stmt2.execute("CREATE TABLE marinduque_clone.facttable LIKE advandb_mco1.facttable;"); 
			stmt2.execute("CREATE TABLE marinduque_clone.members LIKE advandb_mco1.members;"); 
			stmt2.execute("CREATE TABLE marinduque_clone.farming LIKE advandb_mco1.farming;"); 
			stmt2.execute("INSERT INTO marinduque_clone.facttable SELECT * FROM advandb_mco1.facttable;"); 
			stmt2.execute("INSERT INTO marinduque_clone.members SELECT * FROM advandb_mco1.members;"); 
			stmt2.execute("INSERT INTO marinduque_clone.farming SELECT * FROM advandb_mco1.farming;");
			
			
		} catch(ClassNotFoundException e1) {
			System.out.println("can't find the oracle driver class");
		} catch(SQLException e) {
			System.out.println("sql error = " + e.getMessage());
		} finally {
			if(stmt != null)
				try {
					if(rs!=null) rs.close();
					if(stmt!=null) stmt.close();
					if(stmt2!=null) stmt2.close();
					if(conn!=null)conn.close();
				} catch(SQLException e) {
					
				}
		}
	}
	
	String transform(int[] a, int b, float c) {
			String sql = new String();

			boolean is_student = true;
			boolean is_farmer = true;
			String student_level = new String();
			String crop_type = new String();
			
			if(b==1) {//facttable
				if(a[6]==-1&&a[7]==-1)
					sql = "INSERT INTO facttable values("+ a[0]+"," + a[1]+"," + a[2]+"," + a[3]+"," 
							+ a[4]+"," + a[5]+"," + c+"," + "null,null)";
				else if(a[6]==-1)
					sql = "INSERT INTO facttable values("+ a[0]+"," + a[1]+"," + a[2]+"," + a[3]+"," 
							+ a[4]+"," + a[5]+"," + c+"," +"null," + a[7] +")";
				else if(a[7]==-1)
					sql = "INSERT INTO facttable values("+ a[0]+"," + a[1]+"," + a[2]+"," + a[3]+"," 
							+ a[4]+"," + a[5]+"," + c+"," + a[6]+","+"null)";
				else
					sql = "INSERT INTO facttable values("+ a[0]+"," + a[1]+"," + a[2]+"," + a[3]+"," 
						+ a[4]+"," + a[5]+"," + c+"," + a[6]+"," + a[7] +")";
			}
			else if(b==2) {//dealing with mem
				if(a[1]==0)
					is_student= false;
				else{
					if (a[1]==1) student_level="Day Care";
					else if (a[1]==2)student_level="Nursery/Kindergarten/Preparatory";
					else if (a[1]==11)student_level="Grade 1";
					else if (a[1]==12)student_level="Grade 2";
					else if (a[1]==13)student_level="Grade 3";
					else if (a[1]==14)student_level="Grade 4";
					else if (a[1]==15)student_level="Grade 5";
					else if (a[1]==16)student_level="Grade 6";
					else if (a[1]==17)student_level="Grade 7";
					else if (a[1]==18)student_level="Grade 8";
					else if (a[1]==19)student_level="Grade 9/3rd Year HS";
					else if (a[1]==20)student_level="Grade 10/4th Year HS";
					else if (a[1]==21)student_level="Grade 11";
					else if (a[1]==22)student_level="Grade 12";
					else if (a[1]==23)student_level="1st year PS PS/N-T/TV";
					else if (a[1]==24)student_level="2nd year PS PS/N-T/TV";
					else if (a[1]==25)student_level="3rd year PS PS/N-T/TV";
					else if (a[1]==31)student_level="1st year College";
					else if (a[1]==32)student_level="2nd year College";
					else if (a[1]==33)student_level="3rd year College";
					else if (a[1]==34)student_level="4th year College or higher";
					else if (a[1]==41)student_level="Post grad with units";
					else if (a[1]==51)student_level="ALS Elementary";
					else if (a[1]==52)student_level="ALS Secondary";
					else if (a[1]==53)student_level="SPED Elementary";
					else if (a[1]==54)student_level="SPED Secondary";
				}
				
				sql = "INSERT INTO members values(" +a[0]+","+ is_student+",'" +student_level +"')";
			}
			else if (b==3) {//farmers
				
				if (a[1]==1) crop_type="Sugar Cane";
				else if (a[1]==2)crop_type="Palay";
				else if (a[1]==3)crop_type="Corn";
				else if (a[1]==4)crop_type="Coffee";
				else if (a[1]==5)crop_type="Other Crops";
				else if (a[1]==0) {
					crop_type=null;
					is_farmer=false;
				}
				sql = "INSERT INTO farming values(" +a[0]+","+ is_farmer+",'" +crop_type +"')";
			}

			return sql;
	}
}