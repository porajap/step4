package core;

import java.sql.ResultSet;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import connect.SqlOperation;
import connect.SqlSelectionLocal;

public class CostSettingDosing extends Window{
	private Session session = Sessions.getCurrent();
	private static final long serialVersionUID = 7394394042153638109L;
	int Dosing = 1;
	String xUser = null;
	public String SessionUserCode;

	public void onCreate() throws Exception {
		if (session.getAttribute("User") == null) {
			Executions.sendRedirect("/CostLogin.zul");
		} else {
			xUser = (String)session.getAttribute("User");
        }
		ResultSet rs = null;
		SqlSelectionLocal sqlsel = new SqlSelectionLocal();
        sqlsel.uName = "root";
        sqlsel.Pwd = "A$192dijd";
        String Sqlx = "";

			Sqlx = "SELECT cost_login.dosing " +
			"FROM cost_login " +
			"WHERE cost_login.username = '" +xUser+ "'";
			
			rs = sqlsel.getReSultSQL( Sqlx );
			while(rs.next()){
				Dosing = rs.getInt("dosing");
			}
			//System.out.println( "Dosing : " + Dosing );
		((Radio) getFellow("Rd"+Dosing)).setChecked(true);
	}

	public void xB01_Click() throws Exception{
		String StrSql = null;
		if( ((Radio) getFellow("Rd1")).isChecked() ){
			StrSql = "UPDATE cost_login SET dosing = '1' WHERE username = '" + xUser + "'";
			Add_Data( StrSql );
			session.setAttribute("Dosing", "1");
			Executions.sendRedirect("/CostSetting1.zul");
		}else if( ((Radio) getFellow("Rd2")).isChecked() ){
			StrSql = "UPDATE cost_login SET dosing = '2' WHERE username = '" + xUser + "'";
			Add_Data( StrSql );
			session.setAttribute("Dosing", "2");
			Executions.sendRedirect("/CostSetting2.zul");
		}else if( ((Radio) getFellow("Rd3")).isChecked() ){
			StrSql = "UPDATE cost_login SET dosing = '3' WHERE username = '" + xUser + "'";
			Add_Data( StrSql );
			session.setAttribute("Dosing", "3");
			Executions.sendRedirect("/CostSetting3.zul");
		}else if( ((Radio) getFellow("Rd4")).isChecked() ){
			StrSql = "UPDATE cost_login SET dosing = '4' WHERE username = '" + xUser + "'";
			Add_Data( StrSql );
			session.setAttribute("Dosing", "4");
			Executions.sendRedirect("/CostSetting4.zul");
		}else if( ((Radio) getFellow("Rd5")).isChecked() ){
			StrSql = "UPDATE cost_login SET dosing = '5' WHERE username = '" + xUser + "'";
			Add_Data( StrSql );
			session.setAttribute("Dosing", "5");
			Executions.sendRedirect("/CostSetting5.zul");
		}
	}
	
	private void Add_Data(String StrSql) throws Exception{
		SqlOperation sqlopt = new SqlOperation();
		sqlopt.uName = "root";
		sqlopt.Pwd = "A$192dijd";
		try{
			//System.out.println(StrSql);
    		sqlopt.executeUpdateSQL(StrSql);
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		sqlopt.closeConnection();
    	}	
	}
}
