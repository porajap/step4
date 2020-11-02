package com.poseintelligence.cssd.document;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Occurrence {
	
	public static String getOccurrenceDocNo(String S_DB, String S_RefDocNo, String S_DeptId, String S_UserId, String S_OccuranceID, String S_Remark) throws Exception {
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
        ResultSet rs = null;
        
        String S_OCDocNo = null;
        
        try{
        	
        	String S_SqlGetDocNo = 
        			"SELECT      CONCAT('OC',SUBSTRING(YEAR(DATE(NOW())), 3, 4),LPAD(MONTH(DATE(NOW())), 2, 0),'-', LPAD( (COALESCE(MAX(CONVERT(SUBSTRING(DocNo, 8, 5),UNSIGNED INTEGER)), 0) +1) , 5, 0)) AS DocNo "
        		+ 	"FROM        occurance "
        		+ 	"WHERE       DocNo LIKE CONCAT('OC',SUBSTRING(YEAR(DATE(NOW())),3,4),LPAD(MONTH(DATE(NOW())),2,0),'%') "
             	+ 	"ORDER BY    DocNo DESC "
             	+ 	"LIMIT       1 ";
	        
	        // Create Send Sterile
	        
			rs = stmt.executeQuery(S_SqlGetDocNo);
			
			if(rs.next()){
				
				S_OCDocNo = rs.getString("DocNo");
				
				String S_SqlCreateOccurance = 
						
					"  INSERT INTO occurance ( "
						
				+	"	DocNo, "
				+	"	ModifyDate, "
				+	"	DepID, "
				+	"	UserCode, "
				+	"	Round, "
				
				+	"	DocDate, "
				+	"	OccuranceID, "
				+	"	Remark, "
				+	"	RefDocNo, "
				+	"	IsCancel, "
				
				+	"	MachineID, "
				+	"   IsWeb,"
				+	"	B_ID "

		      	+ 	"   ) "
		      	
		        + 	"VALUES "
		        
		      	+ 	"   (   "
		      	+ 	"	'" + S_OCDocNo + "', "
		      	+ 	"   NOW(), "
		       	+ 	"   '" + S_DeptId + "', "
		      	+ 	"   '" + S_UserId + "', " 	
		      	+ 	" 	0, "
		      	
		       	+ 	"   NOW(), "	
		       	+ 	"   '" + S_OccuranceID + "', "
		       	+ 	"   '" + S_Remark + "', "
		       	+ 	"   '" + S_RefDocNo + "', "		      	
		      	+ 	"   0,"
		       	
				+ 	"   1, "
				+ 	"   1, "
				+ 	"   1 "
		      	
		      	
		      	+ 	") ";
	
				System.out.println(S_SqlCreateOccurance);
				
				stmt.executeUpdate(S_SqlCreateOccurance);
						
			}
 
	        conn.commit();
	        
		}catch(Exception e){
			e.printStackTrace();
			return null;
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
        
        return S_OCDocNo;
	}
}
