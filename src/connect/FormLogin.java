package connect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import org.zkoss.zk.ui.ComponentNotFoundException;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Messagebox;

public class FormLogin {
    //-------------------- Variable -------------------------   
    private String StrUsername, StrPassword;
    private ResultSet rs = null;
    private Connection conn = null;
    private Statement stmt = null;
    //-------------------- Create (init) -------------------------	
    public void onCreate() {
    	/*
    	System.out.println("**********************");
        Session session = Sessions.getCurrent();
        for(int i=0; i< Data.length;i++){
        	session.setAttribute(Data[i], null);
        	System.out.println( Data[i] );
        }
        //String xUser = session.getAttribute("username").toString();
        //String xPwd = session.getAttribute("password").toString();
        
        System.out.println("**********************");
        */

    }

    private String[] getSessions(String usrlogin) throws Exception {
        if (usrlogin != null){
        	return getUserData(usrlogin, usrlogin);
        }else
            return null;
    }

    //-------------------- Get(), Set() -------------------------	
    private String getUsername() {
    	System.out.println("str username" + StrUsername);
        return StrUsername;
    }

    private String getPassword() {
    	System.out.println("str password" + StrPassword);
        return StrPassword;
    }


    private void setUsername(String username) {
        StrUsername = username;
    }

    private void setPassword(String password) {
        StrPassword = password;
    }

    //-------------------- SET -------------------------	
    private boolean set(String StrUser, String StrPassword) {
        if (!StrUser.trim().equals("")) {
            setUsername(StrUser);
        } else {
            return false;
        }

        if (!StrPassword.trim().equals("")) {
            setPassword(StrPassword);
        } else {
            return false;
        }
        return true;
    }

    //-------------------- Events -------------------------	
    public String[] onLogin(String StrUser_, String StrPassword_) throws WrongValueException, ComponentNotFoundException, Exception {       
    	if (set(StrUser_, StrPassword_)) {
        	if (checkLogin()) {
        		return getSessions(getUsername());
        	} else {
        		return null;
        	}
        }
        return null;
    }

    private boolean checkLogin() throws Exception {
    	DBConn objConnection = new DBConn();
    	conn = objConnection.getConnection(StrUsername,StrPassword);
        stmt = conn.createStatement();
        String StrSql = "SELECT  	users.ID, "  + 
        		        "users.UserName, " + 
        				"users.Password, " + 
        				"users.EmpCode, " + 
        				"users.IsCancel, " + 
        				"users.EmpCode," +  
        				"department.ID AS Deptid,"  + 
        				"department.DepName2,"  + 
        			    "employee.FirstName,"  + 
        				"employee.LastName" + 
        				" FROM 		users " +
        				"INNER JOIN employee ON users.EmpCode = employee.EmpCode "+
        			    "INNER JOIN department ON employee.DepID = department.ID "+
        				"WHERE 	Username = '" + getUsername() + "' " +
        				"AND 	Password = '" + getPassword() + "' " +
        				"LIMIT 1";
       	
        SqlSelection sqlsel = new SqlSelection();
        
        System.out.println("StrSql 1 :"+ StrSql );
        try {
        	sqlsel.uName = getUsername();
            sqlsel.Pwd =	getPassword();
            rs = sqlsel.getReSultSQL(StrSql);

            if (rs.next()) {
                return true;
            }

        } catch (Exception e) {
        	e.printStackTrace();
            return false;
        } finally {
            if (rs != null) {
                rs.close();
                stmt.close();
                conn.close();
            }
            
            sqlsel.closeConnection();
        }
        
        return false;
    }
   
    private String[] getUserData(String StrUsrName, String StrPassWord) throws Exception {
     	System.out.println("see password and username"+StrUsername+ "and" + StrPassword);
    	DBConn objConnection = new DBConn();
    	conn = objConnection.getConnection(StrUsername,StrPassword);
        stmt = conn.createStatement();
        
        String StrSql = "SELECT  	users.ID, " +
        							"users.UserName, " +
		        					"users.Password, " +
		        					"users.EmpCode, " +
		        					"users.IsCancel, " +
		        					"users.EmpCode," + 
		        					"department.ID AS Deptid," +
		        				    "department.DepName2," +
		        				    "employee.FirstName," +
		        				    "employee.LastName " +
		        					
        				"FROM 		users " +
        				

        				"INNER JOIN employee ON users.EmpCode = employee.EmpCode "+
        			    "INNER JOIN department ON employee.DepID = department.ID "+
		        				
		                "WHERE 	users.Username = '" + StrUsrName + "' AND users.`Password` = '"+ StrPassWord +"' AND users.IsCancel = 0" +
		                
		                " LIMIT 1  ";
        
        SqlSelection sqlsel = new SqlSelection();
        
        
        sqlsel.uName = getUsername();
        sqlsel.Pwd =	getPassword();
        
        System.out.println("StrSql 2 :"+ StrSql );
        rs = sqlsel.getReSultSQL(StrSql);
        
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        
        String data[] = new String[columnCount]; 
        
        try {   
            if (rs.next()) 
            	for(int i = 0 ; i < data.length ;i++)
            		data[i] = rs.getString(i+1); 
        } catch (Exception e) {
        	e.printStackTrace();
            return null;
        } finally {
            if (rs != null) {
                rs.close();
                stmt.close();
                conn.close();
            }

            sqlsel.closeConnection();
        }

        return data;
    }
    
    public void onLigin(String user_code) throws Exception {
    	new OperationData(
    			getUsername(),
    			getPassword(),
   	 		"Insert",
			"testresult_log", 
			new String[][] {
   	 			{"UserApprove", "'" + user_code + "'" },
   	 			{"Dateprocess", "CURDATE()" },
   	 			{"Process", "CURTIME()" },
   	 			{"B_ID", "1" },
			},
			null
		);
    }
}
