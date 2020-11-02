package connect;

import java.sql.ResultSet;

public class xPermission {
	
	public String xUser = null;
	public String xPwd = null;
	
	private String getSqlConfigPMS( String username,String menucode ){
		
		String SQL = 	"SELECT " +
				"menu_permission.add AS add1, " +
				"menu_permission.del AS del1, " +
				"menu_permission.update AS update1, " +
				"menu_permission.isAcc AS isAcc1 " +
				"FROM login " +
				"INNER JOIN menu_permission ON login.Login_Code = menu_permission.Login_Code " +
				"WHERE login.Username = '" + username + "' AND " +
				"menu_permission.menu_code = '" + menucode + "'";
		//System.out.println( SQL );
		return SQL;
	
	}
	
	public boolean[] chkConfigPMS( String username,String menucode ) throws Exception{
		ResultSet rs = null;
        SqlSelection sqlsel = new SqlSelection();
        sqlsel.uName = xUser;
        sqlsel.Pwd = xPwd;
        boolean[] chk = new boolean[10];
        
		try{	
			
			rs = sqlsel.getReSultSQL(getSqlConfigPMS( username,menucode ) );
			while(rs.next()){
				//System.out.println( rs.getBoolean("add1")+"");
				 chk[0] = rs.getBoolean("add1");
				 chk[1] = rs.getBoolean("del1");
				 chk[2] = rs.getBoolean("update1");
				 chk[3] = rs.getBoolean("isAcc1");
				 
				 //System.out.println( chk[0] + ":" + chk[1] + ":" + chk[2] );
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			sqlsel.closeConnection();
            if (rs != null) {
                rs.close();
            }
		}
		return chk;
	}
	
}
