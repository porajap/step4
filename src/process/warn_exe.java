package process;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import connect.DBConn;
import connect.SqlSelection;
import general.DateTime;

public class warn_exe extends Div{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ResultSet rs = null;
	private Connection conn = null;
	private Statement stmt = null;
	private boolean B_IsCreate = true;
	private Session session = Sessions.getCurrent();
	private String xUsername = null;
	private String xPassword = null;
	private Listbox Listboxwarn_exe;
	private Datebox Datebox;
	
	private String 	S_UserId,
	S_DeptId,
	S_UserName,
	S_IsAdmin,
	S_EmpCode,
	S_DepName,
	B_ID,
	S_DB,
	i_itemcode;
	public void onCreate() throws Exception{
		init();
		bySession();
		onDisplay();
	}
	public void init() throws Exception{
//		SqlSelection sql = new SqlSelection();
//		sql.uName = xUsername;
//		sql.Pwd = xPassword;
//		DBConn objConnection = new DBConn();
//		conn = objConnection.getConnection("root", "A$192dijd");
//		stmt = conn.createStatement();
		
		Listboxwarn_exe = ((Listbox)getFellow("Listboxwarn_exe"));
		Datebox = ((Datebox) getFellow("Datebox"));
		Datebox.setText(DateTime.getDateNow());
	}
	private void bySession()throws Exception{
		try {
		if (B_IsCreate) {
//			System.out.println("S_UserId : " + S_UserId);
			if (session.getAttribute("S_UserId") == null) {
				Executions.sendRedirect("/index.zul");
			} else {	
				S_UserId = (String)session.getAttribute("S_UserId");
				S_DeptId = (String)session.getAttribute("S_DeptId");
				S_UserName = (String)session.getAttribute("S_UserName");
				S_IsAdmin = (String)session.getAttribute("S_IsAdmin");
				S_EmpCode = (String)session.getAttribute("S_EmpCode");
				S_DepName = (String)session.getAttribute("S_DepName");
				S_DB = (String)session.getAttribute("S_DB");
				B_ID = (String)session.getAttribute("B_ID");
				i_itemcode = (String)session.getAttribute("itemcode");
				
	        }
			
			B_IsCreate = false;
		}
		}catch (Exception e) {
//			Messagebox.show("ERROR bySessionTracking : " + e.getMessage());
//			System.out.println("ERROR bySession : " + e.getMessage());
		}
	}
	
public String getSql()throws Exception {
		
		try {
		 String Sql = "SELECT" + 
		 		"		            itemstock.UsageCode," + 
		 		"		            DATE(itemstock.ExpireDate) AS ExpireDate," + 
		 		"		            itemstock.IsStatus," + 
		 		"		            itemstock.DepID," + 
		 		"		            item.itemname," + 
		 		"		            department.DepName2" + 
		 		"		            FROM" + 
		 		"		            itemstock" + 
		 		"		            INNER JOIN item ON itemstock.ItemCode = item.itemcode" + 
		 		"		            INNER JOIN department ON itemstock.DepID = department.ID" + 
		 		"                WHERE (itemstock.IsStatus = 5 ) AND itemstock.DepID = '"+S_DeptId+"'" + 
		 		"								AND (DATE(itemstock.ExpireDate) BETWEEN DATE_ADD(DATE(NOW()), INTERVAL 1 DAY) AND" + 
		 		"                 DATE_SUB(DATE(NOW()),INTERVAL -(SELECT Nobeforeexp" + 
		 		"                 FROM settingapp" + 
		 		"                 WHERE settingapp.DeptID = '"+S_DeptId+"') DAY))" + 
		 		"                 ORDER BY DATE(itemstock.ExpireDate) ASC";
		 
		 		

		 		System.out.println("getSql : " +Sql);
		 		return Sql;
		 		
		}catch (Exception e) {
			System.out.println("ERROR getSql : " + e.getMessage());
		}
		return null;
	}

	
	public void onDisplay()throws Exception {
		
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;
		
		
		try {
			rs = stmt.executeQuery(getSql());
			
			Listboxwarn_exe.getItems().clear();
			int no = 1;
			
			while(rs.next()) {
				Listitem list = new Listitem();
				
				list.appendChild(new Listcell(no + "."));
				list.appendChild(new Listcell(rs.getString("UsageCode")));
				list.appendChild(new Listcell(rs.getString("itemname")));
				list.appendChild(new Listcell(rs.getString("ExpireDate")));

				Listboxwarn_exe.appendChild(list);
				no++;
			}
		}catch (Exception e) {
			e.printStackTrace();
			Messagebox.show("ERROR onDisplayReport_HN : " + e.getMessage());
			System.out.println("ERROR onDisplay : " + e.getMessage());
		}
	}

}
