package process;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import connect.DBConn;
import general.DateTime;

public class stockOR extends GenericForwardComposer<Component>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ResultSet rs = null;
	private Connection conn = null;
	private Statement stmt = null;
	private String xUsername = null;
	private String xPassword = null;
	
	private Listbox Listboxstock;
	private Listbox Listboxstock2;
	private Datebox Datebox;
	private Textbox TextboxSearchStock;
	private Button btnSearch;
	private Window WinProcess;
	private boolean B_IsCreate = true;
	private Session session = Sessions.getCurrent();
	
	private String 	S_UserId,
					S_DeptId,
					S_UserName,
					S_IsAdmin,
					S_EmpCode,
					S_DepName,
					B_ID,
					S_DB;
	
	private String Search = null;	
	private String S_DocNo = null;
	private String S_IsStatus = "-5";
	private String S_TempStatus = "-5";
	private String 	S_ListStatus = "-5, 5, 6";
	
	public void onCreate() throws Exception{
		init();
		onDisplaystock();
		bySession();
	}
	public void onClose() throws Exception {
		  stmt.close();
		  conn.close();
		 }
	public void init() throws Exception{
		try {
		DBConn objConnection = new DBConn();
		conn = objConnection.getConnection("root", "A$192dijd");
		stmt = conn.createStatement();
		
		Datebox.setText(DateTime.getDateNow());
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void onOK$TextboxSearchStock(Event event) throws Exception {
		onDisplaystock();
	}
	public void onClick$btnSearch(Event event) throws Exception {
		onDisplaystock();
	}
	
	public void onSelect$Listboxstock(Event event) throws Exception {
		onDisplaystock2("");
	}
	
	private void bySession(){
		try {
		if (B_IsCreate) {
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
				
				
	        }
			
			B_IsCreate = false;
		}
		}catch (Exception e) {
			// TODO: handle exception
		}
	}

	public String getSql(final String S_Text) throws Exception{
		
		String S_Sql = "";
		
		Search = TextboxSearchStock.getText();
		S_Sql =" SELECT " + 
				"	item.itemname, " + 
				"	item.itemcode, " + 
				"	DATE_FORMAT( itemstock.ExpireDate, '%d/%m/%Y' ) AS ExpireDate, " +
//				"	itemstock.ExpireDate, " + 
				"	itemstock.UsageCode, " + 
				"	itemstock.DepID, " + 
				"	( " + 
				"		SELECT " + 
				"			COALESCE (SUM(T2.Qty), '0') " + 
				"		FROM " + 
				"			itemstock AS T2 " + 
				"		WHERE " + 
				"			itemstock.ItemCode = T2.ItemCode " + 
				"		AND T2.IsStatus = '5' " + 
				"		AND T2.DepID = '"+S_DeptId+"' " +
				"		AND T2.B_ID = "+B_ID+" " + 
				"		AND T2.IsPay = '1' " + 
				"	) AS Qty, " + 
				"	( " + 
				"		SELECT " + 
				"			COALESCE (SUM(T1.Qty), '0') " + 
				"		FROM " + 
				"			itemstock AS T1 " + 
				"		WHERE " + 
				"			itemstock.ItemCode = T1.ItemCode " + 
				"		AND T1.IsDispatch = 1 " + 
				"		AND T1.IsStatus = '5' " + 
				"		AND T1.DepID = '"+S_DeptId+"' " + 
				"		AND T1.B_ID = "+B_ID+" " + 
				"		AND T1.IsPay = '1' " + 
				"	) AS DQty " + 
				"FROM " + 
				"	itemstock " + 
				"INNER JOIN item ON itemstock.ItemCode = item.itemcode " + 
				"WHERE " + 
				"	itemstock.IsStatus = '5' " +
				"AND itemstock.DepID = '"+S_DeptId+"' " + 
				"AND itemstock.IsPay = '1' " + 
				"AND itemstock.B_ID = "+B_ID+" " ;
				
		
		if(!Search.equals("")) {
			S_Sql += "AND (item.itemname LIKE  '%"+Search.replace(" ", "%")+"%')";
		}
		
		if(!Search.equals("")) {
			S_Sql += "OR (itemstock.UsageCode LIKE  '%"+Search.replace(" ", "%")+"%')";
		}
		
			S_Sql +=	"GROUP BY " + 
				"	itemstock.ItemCode " + 
				"ORDER BY " + 
				"	item.itemname ";
		
		System.out.println("S_Sql" + S_Sql);
		
		
		return S_Sql;
		
	}
	public void onDisplaystock() throws Exception{
		
		try {
		final String S_Text = TextboxSearchStock.getText().toString();
		Search = TextboxSearchStock.getText();
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;
    	
		try {
			Listboxstock.getItems().clear();
			Listboxstock2.getItems().clear();
			
//			onProcess(true);
			
			rs = stmt.executeQuery(getSql(S_Text));
			int no = 1;
			
			while(rs.next()) {
				
				Listitem list = new Listitem();
				
				list.appendChild(new Listcell(no + ""));
				list.appendChild(new Listcell(rs.getString("itemname")));
				list.appendChild(new Listcell(rs.getString("Qty")));
				list.appendChild(newListcell(rs.getString("itemcode")));
				
				
				list.setAttribute("Listboxstock2", rs.getString("itemcode"));
				list.setAttribute("Listboxstock2", rs.getString("UsageCode"));
				list.setAttribute("Listboxstock2", rs.getString("ExpireDate"));
				Listboxstock.appendChild(list);
				no++;
			}
		}catch (Exception e) {
			Messagebox.show("ERROR onDisplaystock : " + e.getMessage());
		}
		}catch (Exception e) {
			// TODO: handle exception
			Messagebox.show("ERROR onDisplaystock : " + e.getMessage());
		}
	}
	
	public void onDisplaystock2(final String Item_Code) throws Exception{
		
		try {
			String sql="SELECT " + 
					"DATE_FORMAT( itemstock.ExpireDate, '%d/%m/%Y' ) AS ExpireDate, " +
//					"itemstock.ExpireDate, " + 
					"itemstock.UsageCode, " + 
					"item.itemname " + 
					"FROM " + 
					"itemstock  " + 
					"INNER JOIN item ON item.itemcode = itemstock.ItemCode " + 
					" " + 
					"WHERE  " + 
					"itemstock.ItemCode ='"+Item_Code+"' " + 
					"AND itemstock.IsStatus = '5' " + 
					"AND itemstock.DepID = '"+S_DeptId+"' " + 
					"AND itemstock.IsPay = '1' " + 
					"AND itemstock.B_ID = "+B_ID+"";
//			System.out.println("onDisplaystock2  " + sql);
			rs = stmt.executeQuery(sql);
			Listboxstock2.getItems().clear();
		
			while(rs.next()) {
				Listitem list = new Listitem();
				
				list.appendChild(new Listcell(rs.getString("itemname")));
				list.appendChild(new Listcell(rs.getString("UsageCode")));
				list.appendChild(new Listcell(rs.getString("ExpireDate")));
				
				list.setAttribute("itemcode", rs.getString("itemcode"));
				
				
				Listboxstock.appendChild(list);
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	public void onProcess(boolean b) throws Exception {
    	WinProcess.setVisible(b);
    	WinProcess.setFocus(b);
    	WinProcess.setPosition("center");
    	WinProcess.setMode("modal");
    }
	private Listcell newListcell(final String Item_Code) throws Exception{

		Listcell lc = new Listcell();
		
		Image img = new Image();
		
		String sql="SELECT " + 
				"DATE_FORMAT( itemstock.ExpireDate, '%d/%m/%Y' ) AS ExpireDate, " +
//				"itemstock.ExpireDate, " + 
				"itemstock.UsageCode, " + 
				"item.itemname " + 
				"FROM " + 
				"itemstock  " + 
				"INNER JOIN item ON itemstock.ItemCode = item.itemcode " + 
				"WHERE itemstock.ItemCode ='"+Item_Code+"' " + 
				"AND itemstock.IsStatus = '5' " + 
				"AND itemstock.DepID = '"+S_DeptId+"' " + 
				"AND itemstock.IsPay = '1' " + 
				"AND itemstock.B_ID = "+B_ID+" ";
		
		
		

		img.setHeight("25px");
		img.setSrc("/images/ic_search.png");
		img.setStyle("align:center;");
			lc.setStyle("align:center; ");
			img.addEventListener("onClick", new EventListener<Event>() {
				public void onEvent(Event event) throws Exception {
					
					rs = stmt.executeQuery(sql);
					Listboxstock2.getItems().clear();
					while(rs.next()){
						
						Listitem list = new Listitem();
						
						list.appendChild(new Listcell(rs.getString("itemname")));
						list.appendChild(new Listcell(rs.getString("UsageCode")));
						list.appendChild(new Listcell(rs.getString("ExpireDate")));
						
						
						
						
						Listboxstock2.appendChild(list);
				
			}
					
					    }
					});
//			System.out.println("newListcell " + sql);
			lc.appendChild(img);
		
		
		return lc;
	}
	
	public void SearchData() throws Exception {
		
			onDisplaystock();

}
}
