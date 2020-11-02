package process;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import connect.DBConn;
import general.DateTime;

public class receivein extends GenericForwardComposer<Component>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ResultSet rs = null;
	private Connection conn = null;
	private Statement stmt = null;
	private String xUsername = null;
	private String xPassword = null;
	
	private Listbox Listboxwarn_before_exe;
	private Listbox Listboxwarn_before_exee;
	private Datebox Datebox;
	private Textbox TextboxSearch;
	
	public void onCreate() throws Exception{
		init();
		
	}
	public void init() throws Exception{
//		DBConn objConnection = new DBConn();
//		conn = objConnection.getConnection("root", "A$192dijd");
//		stmt = conn.createStatement();
		
		Datebox.setText(DateTime.getDateNow());
	}
//	public void getSql() throws Exception{
//		String Sql = "";
//		rs = stmt.executeQuery(Sql);
//	}
//	public void onDisplayexp() throws Exception{
//		final String S_Text = TextboxSearch.getText().toString();
//		try {
//			Listboxwarn_before_exe.getItems().clear();
//			Listboxwarn_before_exee.getItems().clear();
//			
//			int no = 1;
//			while(rs.next()) {
//				Listitem list = new Listitem();
//				
//				list.appendChild(new Listcell(no + "."));
//				list.appendChild(new Listcell(rs.getString("")));
//				list.appendChild(new Listcell(rs.getString("")));
//				
//				list.setAttribute("itemcode", rs.getString("itemcode"));
//				Listboxwarn_before_exe.appendChild(list);
//			}
//		}catch (Exception e) {
//			// TODO: handle exception
//		}
//	}
//	
//	public void onDisplayexpp() throws Exception{
//		
//		try {
//			
//			Listboxwarn_before_exee.getItems().clear();
//		
//			while(rs.next()) {
//				Listitem list = new Listitem();
//				
//				list.appendChild(new Listcell(rs.getString("")));
//				list.appendChild(new Listcell(rs.getString("")));
//				list.appendChild(new Listcell(rs.getString("")));
//				
//				list.setAttribute("itemcode", rs.getString("itemcode"));
//				Listboxwarn_before_exee.appendChild(list);
//			}
//		}catch (Exception e) {
//			// TODO: handle exception
//		}
//	}
}
