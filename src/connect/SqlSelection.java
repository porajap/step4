
/*	
 *  -- ************************************************************
 -- Author		:	PARADOX
 -- Create date	:	18-07-2011
 -- Update date	:	PARADOX
 -- Update By	:   09-08-2011
 -- Description	:	Database Selelction [Version 1.0]
 -- ************************************************************
 */
package connect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class SqlSelection {

    private Connection conn = null;
    private Statement stmt = null;
    
    public String uName;
    public String Pwd;
    //Session 
    //public String SessionUserCode;
    //private Session session = Sessions.getCurrent();
    
    public ResultSet getReSultSQL(String sql) throws Exception {
    	//SessionUserCode = (String) session.getAttribute("UserCode");
    	DBConn objConnection = new DBConn();
    	System.out.println(uName+" :: "+Pwd);
        Connection conn = objConnection.getConnection(uName,Pwd);
        stmt = conn.createStatement();
        return stmt.executeQuery(sql);
    }
    
    public ResultSet getReSultSQL(String sql, String SessionUserCode) throws Exception {
    	DBConn objConnection = new DBConn();
        Connection conn = objConnection.getConnection(uName,Pwd);
        stmt = conn.createStatement();
        return stmt.executeQuery(sql);
    }

    public void closeConnection() throws Exception {
        if (stmt != null) {
            stmt.close();
        }
        if (conn != null) {
        	
            conn.close();
        }
    }
}
