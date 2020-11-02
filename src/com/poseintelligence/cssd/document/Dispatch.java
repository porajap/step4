package com.poseintelligence.cssd.document;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Dispatch {
	
	public static String getdispatchstockDocNo(String S_DB, String S_RefDocNo, String S_DeptId, String S_UserId, String S_Status, String S_Remark) throws Exception {
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
		Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
        ResultSet rs = null;
        
        String S_DocNo = null;
        
        try{
        	
        	String S_SqlGetDocNo = 
        			"SELECT      CONCAT('DP',SUBSTRING(YEAR(DATE(NOW())), 3, 4),LPAD(MONTH(DATE(NOW())), 2, 0),'-', LPAD( (COALESCE(MAX(CONVERT(SUBSTRING(DocNo, 8, 5),UNSIGNED INTEGER)), 0) +1) , 5, 0)) AS DocNo "
        		+ 	"FROM        dispatchstock "
        		+ 	"WHERE       DocNo LIKE CONCAT('DP',SUBSTRING(YEAR(DATE(NOW())),3,4),LPAD(MONTH(DATE(NOW())),2,0),'%') "
             	+ 	"ORDER BY    DocNo DESC "
             	+ 	"LIMIT       1 ";
	        
	        // Create Send Sterile
	        
			rs = stmt.executeQuery(S_SqlGetDocNo);
			
			if(rs.next()){
				
				S_DocNo = rs.getString("DocNo");
				
				String S_SqlCreateDispatchstock = 
						
				"  INSERT INTO dispatchstock ( "
				+ 	"   DocNo, "
		      	+ 	"   DocDate, "
		       	+ 	"   ModifyDate, "
		       	+ 	"   DeptID, "
		       	+ 	"   UserCode, "
		       	+ 	"   Qty, "
		       	+ 	"   IsCancel, "
		     	+ 	"   Remark, "
		       	+ 	"   RefDocNo, "
		       	+ 	"   IsStatus "
		      	+ 	"   ) "
		        + 	"VALUES "
		      	+ 	"   (   "
		      	+ 	"	'" + S_DocNo + "', "
		      	+ 	"   NOW(), "
		      	+ 	"   NOW(), "
		       	+ 	"   '" + S_DeptId + "', "
		      	+ 	"   '" + S_UserId + "', "
		       	+ 	"   0, "
		       	+ 	"   0, "
		       	+ 	"   '" + S_Remark + "', "
		      	+ 	"   '" + S_RefDocNo + "', "
		      	+ 	" 	" + S_Status 
		      	+ 	") ";
	
				System.out.println(S_SqlCreateDispatchstock);
				
				stmt.executeUpdate(S_SqlCreateDispatchstock);
						
			}
 
	        conn.commit();
	        
		}catch(Exception e){
			e.printStackTrace();
		}finally{
	        
	        if (stmt != null) {
	            stmt.close();
	        }
	        
	        if (conn != null) {
	            conn.close();
	        }
	        
	        if (rs != null) {
                rs.close();
            }
		}
        
        return S_DocNo;
	}
}
