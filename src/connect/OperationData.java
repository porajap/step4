/*	
 *  -- ************************************************************
 -- Author		:	PARADOX
 -- Create date	:	18-07-2011
 -- Update date	:	PARADOX
 -- Update By	:   18-07-2011
 -- Description	:	Database Operation [Version 1.0]
 -- ************************************************************
 */
package connect;

public class OperationData {
	private String[][] Data;
	private String[][] Condition;
	private String xUsername = null;
	private String xPassword = null;
	
	public OperationData(String xUser,String xPwd,String Opration,String Table, final String[][] Data, final String[][] Condition)throws Exception{
		xUsername = xUser;
		xPassword = xPwd;
		if(Opration == null)
			return ;
		
		this.Data = Data;
		this.Condition = Condition;
		
		Opration = Opration.toLowerCase();
		
		if(Opration.equals("insert"))
			insert(Table);
		else if(Opration.equals("delete"))
			delete(Table);
		else if(Opration.equals("update"))
			update(Table);
		else if(Opration.equals("deleteall"))
			deleteall(Table);
		else
			return;
		
	}
	
	private boolean insert(String Table) throws Exception{
		SqlOperation sqlopt = new SqlOperation();
		sqlopt.uName = xUsername;
		sqlopt.Pwd = xPassword;
		String StrSql = null;

		try{
    		StrSql =  	"INSERT INTO "+ Table +" " +
    				  	"(";	
    		
	    				  	for(int i = 0;i < Data.length ;i++)
	    				  		StrSql += "`" + Data[i][0] + "`,";
	    				  	
	    				  	StrSql = StrSql.substring(0, StrSql.length()-1);
	    				  	
    		StrSql += 	") "+
    				  	"VALUES " +
    				  	"(";
    		
				    		for(int i = 0;i < Data.length ;i++)
						  		StrSql += Data[i][1] + ",";
						  	
						  	StrSql = StrSql.substring(0, StrSql.length()-1);

    		StrSql += ") ";

//    		System.out.println(	"INSERT : " + StrSql );
    		sqlopt.executeUpdateSQL(StrSql);
    		
    	}catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}finally{
    		sqlopt.closeConnection();
    		//System.out.println(StrSql);
    		//System.gc();
    	}
		
		return true;
	}
	
	private boolean delete(String Table) throws Exception{
		SqlOperation sqlopt = new SqlOperation();
		sqlopt.uName = xUsername;
		sqlopt.Pwd = xPassword;
		String StrSql = null;
		
		try{
    		StrSql =  	"DELETE FROM "+ Table +" " +
    				  	"WHERE ";	
    		
    				  	for(int i = 0;i < Condition.length ;i++)
    				  		StrSql += (i==0 ? "" : "AND ") + "`" + Condition[i][0] + "` = " + Condition[i][1] + " ";
    				  	
    				  	StrSql = StrSql.substring(0, StrSql.length()-1);

    		sqlopt.executeUpdateSQL(StrSql);
    		
    	}catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}finally{
    		sqlopt.closeConnection();
    		//System.out.println(StrSql);
    		//System.gc();
    	}	

		return true;
	}

	private boolean deleteall(String Table) throws Exception{
		SqlOperation sqlopt = new SqlOperation();
		sqlopt.uName = xUsername;
		sqlopt.Pwd = xPassword;
		String StrSql = null;
		
		try{
    		StrSql =  	"DELETE FROM "+ Table;	
    		sqlopt.executeUpdateSQL(StrSql);
    		
    	}catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}finally{
    		sqlopt.closeConnection();
    	}	

		return true;
	}	
	
	private boolean update(String Table) throws Exception{
    	SqlOperation sqlopt = new SqlOperation();
    	sqlopt.uName = xUsername;
		sqlopt.Pwd = xPassword;
    	String StrSql = null;
    	
		try{
    		StrSql =  "UPDATE "+ Table +" "+
				   	  "SET ";
    		
    		for(int i = 0;i < Data.length ;i++)
		  		StrSql += "`" + Data[i][0] + "` = " + Data[i][1] + ",";
		  	
		  	StrSql = StrSql.substring(0, StrSql.length()-1);
			
			for(int i = 0;i < Condition.length ;i++)
		  		StrSql += (i==0 ? " WHERE " : "AND ") + "`"  + Condition[i][0] + "` = " + Condition[i][1] + " ";
		  	
		  	StrSql = StrSql.substring(0, StrSql.length()-1);
		  	
		  	//System.out.println( StrSql );
		  	
    		sqlopt.executeUpdateSQL(StrSql);
    		
    	}catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}finally{
    		sqlopt.closeConnection();
    		//System.out.println(StrSql);
    		//System.gc();
    	}	
		
		return true;
	} 
	
	
	public OperationData(String xUser,String xPwd,String Opration) throws Exception {
		xUsername = xUser;
		xPassword = xPwd;
		if(Opration == null)
			return ;

		SqlExecute(Opration);
	}
			
	private boolean SqlExecute(String StrSql) throws Exception{
    	SqlOperation sqlopt = new SqlOperation();
    	sqlopt.uName = xUsername;
		sqlopt.Pwd = xPassword;

		try{
		  	
    		sqlopt.executeUpdateSQL(StrSql);
    		
    	}catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}finally{
    		sqlopt.closeConnection();
    		//System.out.println(StrSql);
    		//System.gc();
    	}	
		
		return true;
	} 
	
}
