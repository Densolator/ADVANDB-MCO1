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

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ETLTable creator = new ETLTable();//table submitter to the db
		String a = new String();//hh
		a="SELECT `main.id`, mun, zone, brgy, purok, water, water_dist, water_supply, low_wsupp\r\n" + 
				"FROM hpq_hh";
		System.out.println("adding lines to facttable");
		creator.execute(a,1);//for hh to facttable
		
		//~~will modify the a to b here
		
		String b = new String("SELECT hpq_hh.`main.id`, gradel\r\n" + 
				"FROM hpq_hh, hpq_mem\r\n" + 
				"WHERE hpq_hh.`main.id` = hpq_mem.`main.id`");//for mem to members

		System.out.println("adding lines to members");
		creator.execute(b,2);//commit to the db
		
		
		String c = new String("SELECT hpq_hh.`main.id`, croptype\r\n" + 
				"FROM hpq_hh LEFT OUTER JOIN hpq_crop\r\n" + 
				"ON hpq_hh.`main.id` = hpq_crop.`main.id`");//for crop to farming

		System.out.println("adding lines to farming");
		creator.execute(c,3);//commit to the db
		
		App app = new App();
	}

}
