package connect;

import java.sql.Connection;
import java.sql.Statement;

public class SqlOperation {

	private Connection conn = null;
    private Statement stmt = null;
    public String uName;
    public String Pwd;
    
    public void executeUpdateSQL(String sql) throws Exception {
        try {
        	DBConn objConnection = new DBConn();
            Connection conn = objConnection.getConnection(uName,Pwd);
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }
    
    public void executeUpdateSQLByRoot(String sql) throws Exception {
        try {
        	DBConn objConnection = new DBConn();
            Connection conn = objConnection.getConnection(uName,Pwd);
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	closeConnection();
        }
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
