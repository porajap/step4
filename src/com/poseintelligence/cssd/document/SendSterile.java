package com.poseintelligence.cssd.document;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class SendSterile {
	
	public static String getSendSterileDocNo(String S_DB, String S_RefDocNo, String S_DeptId, String S_UserId, String S_Status, String S_Remark) throws Exception {
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
        ResultSet rs = null;
        
        String S_SSDocNo = null;
        
        try{
        	
        	String S_SqlGetDocNo = 
        			"SELECT      CONCAT('SS',SUBSTRING(YEAR(DATE(NOW())), 3, 4),LPAD(MONTH(DATE(NOW())), 2, 0),'-', LPAD( (COALESCE(MAX(CONVERT(SUBSTRING(DocNo, 8, 5),UNSIGNED INTEGER)), 0) +1) , 5, 0)) AS DocNo "
        		+ 	"FROM        sendsterile "
        		+ 	"WHERE       DocNo Like CONCAT('SS',SUBSTRING(YEAR(DATE(NOW())),3,4),LPAD(MONTH(DATE(NOW())),2,0),'%') "
             	+ 	"ORDER BY    DocNo DESC "
             	+ 	"LIMIT       1 ";
	        
	        // Create Send Sterile
	        
			rs = stmt.executeQuery(S_SqlGetDocNo);
			
			if(rs.next()){
				
				S_SSDocNo = rs.getString("DocNo");
				
				String S_SqlCreateSendSterile = 
						
				"  INSERT INTO sendsterile ( "
				+ 	"   DocNo, "
		      	+ 	"   DocDate, "
		       	+ 	"   ModifyDate, "
		       	+ 	"   DeptID, "
		       	+ 	"   UserCode, "
		       	+ 	"   Qty, "
		       	+ 	"   IsCancel, "
		     	+ 	"   Remark, "
		       	+ 	"   RefDocNo, "
		       	+ 	"   IsWeb, "
		       	+ 	"   IsStatus "
		      	+ 	"   ) "
		        + 	"VALUES "
		      	+ 	"   (   "
		      	+ 	"	'" + S_SSDocNo + "', "
		      	+ 	"   NOW(), "
		      	+ 	"   NOW(), "
		       	+ 	"   '" + S_DeptId + "', "
		      	+ 	"   '" + S_UserId + "', "
		       	+ 	"   0, "
		       	+ 	"   0, "
		       	+ 	"   '" + S_Remark + "', "
		      	+ 	"   '" + S_RefDocNo + "', "
		      	+ 	"   1,"
		      	+ 	" 	" + S_Status 
		      	+ 	") ";
	
				System.out.println(S_SqlCreateSendSterile);
				
				stmt.executeUpdate(S_SqlCreateSendSterile);
						
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
        
        return S_SSDocNo;
	}
}
