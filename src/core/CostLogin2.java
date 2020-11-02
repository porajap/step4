package core;

import java.sql.ResultSet;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import connect.SqlSelectionLocal;

public class CostLogin2 extends Window{
	Textbox User;
	Textbox Pwd;

	private Session session = Sessions.getCurrent();
	private static final long serialVersionUID = 7394394042153638109L;
	String xUsername = null;
	String dosing = null;
	public void onCreate() throws Exception {
		User = ((Textbox) getFellow("user"));
		Pwd = ((Textbox) getFellow("pwd"));
	}

	public void xB01_Click() throws Exception{
		ResultSet rs = null;
		SqlSelectionLocal sqlsel = new SqlSelectionLocal();
        sqlsel.uName = "root";
        sqlsel.Pwd = "A$192dijd";
        String Sqlx = "";
        int row = 0;
			Sqlx = "SELECT cost_login.username,cost_login.dosing " +
			"FROM cost_login " +
			"WHERE cost_login.username = '" +User.getText()+ "' AND cost_login.`password` = '" +Pwd.getText()+ "'";
			
			rs = sqlsel.getReSultSQL( Sqlx );
			while(rs.next()){
				xUsername = rs.getString("username");
				dosing = rs.getString("dosing");
				row++;
			}
			
		if(row == 1) {
			session.setAttribute("User", xUsername);
			session.setAttribute("Dosing", dosing);
			Executions.sendRedirect("/CostMenuEffective2.zul");
		}
	}
}
