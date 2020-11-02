package connect;

import java.sql.Connection;

public class DBConLocal {

	private Connection conn = null;
    public static java.sql.Connection Connection = null;  

    //JTDS
    public java.sql.Connection getConnection(String Username, String Password) {
        try {
        	
        	String Drivers = "jdbc:mysql://";
            String ServerName = "localhost";
            String Port = "3306";
            String DatabaseName = "phcc";
            String Host = Drivers + ServerName + ":" + Port + "/" + DatabaseName;
            System.out.println( Host );
            Class.forName("com.mysql.jdbc.Driver");
            conn = java.sql.DriverManager.getConnection(Host, "root","A$192dijd");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
    
    
	
}
