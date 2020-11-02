package connect;

import java.io.IOException;

import core.ReadFile;
import general.Report.Reports;

public class Conn {
	
	public final String DOX_USER = "root";
	public final String DOX_PASSWORD = "A$192dijd";
	public final String DOX_MYSQL_DRIVER = "com.mysql.jdbc.Driver";
	
	private String getHostPath() throws IOException{ 

    	String db = null;
    	Reports rp = new Reports();
    	ReadFile rf = new ReadFile();
    	db = rf.OpenFile( rp.getPahtProjectDir() + "Config/host.txt" );
    	return db;
		
		//return "192.168.1.10";
    } 

    private String getDatabasePath() throws IOException{
    	String db = null;
    	Reports rp = new Reports();
    	ReadFile rf = new ReadFile();
    	db = rf.OpenFile( rp.getPahtProjectDir() + "Config/db.txt" );
    	return db;
    }

    public String getHost() throws IOException {
    	String Drivers = "jdbc:mysql://";
        String ServerName = getHostPath();
        String Port = "3306";
        String DatabaseName = getDatabasePath();
        return Drivers + ServerName + ":" + Port + "/" + DatabaseName + "?useUnicode=true&characterEncoding=utf-8";        
    }
 
}
