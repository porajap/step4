package connect;
import general.Report.Reports;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.zkoss.zul.Messagebox;

import core.ReadFile;
import core.WriteFile;

public class DBConn2 {

	private Connection conn = null;
//    public static java.sql.Connection Connection = null;  

  
    
    public String host(String Username, String Password) {
        String Host =  "";
    	try {
        	String Drivers = "jdbc:mysql://";
            String ServerName = "localhost";
            String Port = "3306";
            String DatabaseName = "cssd_web_4";
            Host = Drivers + ServerName + ":" + Port + "/" + DatabaseName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Host;
    }
    
    //JTDS
    public java.sql.Connection getConnection(String Username, String Password) {

            String Host = host(Username,Password); //"jdbc:mysql://localhost:3306/phc";
              try {
              	Class.forName("com.mysql.jdbc.Driver");
  			} catch (ClassNotFoundException e) {
  				// TODO Auto-generated catch block
  				System.out.println("Error 1: "+e.getMessage());
  			}

          	try {
          		String classForName = Host; // + "?useUnicode=true&characterEncoding=utf-8";
          		System.out.println("Host : " + classForName);
  				 conn=DriverManager.getConnection(classForName,"root","A$192dijd");
  			} catch (SQLException e) {
  				// TODO Auto-generated catch block
  				System.out.println("Error 2: "+e.getMessage());
  			}

        return conn;
    }

}
