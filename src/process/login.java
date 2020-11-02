package process;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.zkoss.zul.Button;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import connect.DBConn;
import connect.SqlSelection;

public class login extends Window{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6345489843345172046L;


	ResultSet rs = null;

	private Connection conn = null;
	private Statement stmt = null;

	private String xUsername = null;
	private String xPassword = null;

	public Textbox user;
	public Textbox bfpassword;
	public Textbox afpassword;
	public Button Button_cancel;
	public Button Button_enter;
	
	public Window chengeregis;
	public String 	S_UserId,
					S_DeptId,
					S_UserName,
					S_IsAdmin,
					S_EmpCode,
					S_DepName,
					B_ID,
					S_DB,
					i_itemcode;

	public Integer IsStatus;

	public void onCreate() throws Exception{
//		init();		


	}

	private void init()throws SQLException {
		SqlSelection sql = new SqlSelection();
		sql.uName = xUsername;
		sql.Pwd = xPassword;
		DBConn objConnection = new DBConn();
		conn = objConnection.getConnection("root", "A$192dijd");
		stmt = conn.createStatement();
		
		user = ((Textbox) getFellow("user"));
		bfpassword = ((Textbox) getFellow("bfpassword"));
		afpassword = ((Textbox) getFellow("afpassword"));
		Button_cancel = ((Button) getFellow("Button_cancel"));
		Button_enter = ((Button) getFellow("Button_enter"));

	}
	
	
	
	public void onSave() throws Exception {
		
		String sql ="SELECT EmpCode,UserName,Password FROM users WHERE UserName ='"+user.getText()+"' ";
		rs = stmt.executeQuery(sql);
		String pass_old = null;
		String users = null;
		if(rs.next()) {
			pass_old = rs.getString("Password");
			users = rs.getString("UserName");
		}
		if(bfpassword.getText().equals(pass_old)) {
			String update ="UPDATE users SET Password = '"+afpassword.getText()+"'  WHERE UserName = '"+users+"' ";
			
			stmt.executeUpdate(update);
			Messagebox.show("บันทึกสำเร็จ");
			user.setText("");
			bfpassword.setText("");
			afpassword.setText("");
			
			closeWindow("chengeregis");
		}else {
			Messagebox.show("Username หรือ Password ไม่ถูกต้อง"," ", Messagebox.YES, Messagebox.INFORMATION);
		}
		
		
	}
	
	public void onClear() throws Exception {
		user.setText("");
		bfpassword.setText("");
		afpassword.setText("");
	}
	
	
	public void openWindow(String StrWindow) throws Exception {
		((Window) getFellow(StrWindow)).setVisible(true);
		((Window) getFellow(StrWindow)).setFocus(true);
		((Window) getFellow(StrWindow)).setPosition("center");
		((Window) getFellow(StrWindow)).setMode("modal");
	}


	public void closeWindow(String StrWindow) throws Exception {
		((Window) getFellow(StrWindow)).setMode("embedded");
		((Window) getFellow(StrWindow)).setVisible(false);
		((Window) getFellow(StrWindow)).setFocus(false);
	}
	
}
