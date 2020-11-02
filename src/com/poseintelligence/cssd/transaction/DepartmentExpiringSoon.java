package com.poseintelligence.cssd.transaction;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;

import com.poseintelligence.cssd.utillity.Number;


@SuppressWarnings("rawtypes")
public class DepartmentExpiringSoon extends GenericForwardComposer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6637098633119469091L;
	
	private String 	S_ListStatus = "-5, 5, 6";
	
	// Widget
	private Listbox Listbox_item_stock;
	
	private Textbox Textbox_Search;
	
	// Variable Session
	private boolean B_IsCreate = true;
	private Session session = Sessions.getCurrent();
		
	private String 	S_UserId,
					S_DeptId,
					S_UserName,
					S_IsAdmin,
					S_EmpCode,
					S_DepName,
					S_DB;
	
	private int I_GN_WarningExpiringSoonDay = 7;
	
	public void onCreate() throws Exception {
		
		bySession();
		
		init();
		
		onDisplayWebConfig();
		
		onDisplayItemStock();
		
    }
	
	
	// Event
	public void onClick$Button_SearchDoc(Event event) throws Exception {
		onDisplayItemStock();
	}
	
	public void onOK$Textbox_Search(Event event) throws Exception {
		onDisplayItemStock();
	}
	
	public void onSelect$Listbox_item(Event event) throws Exception {
		onDisplayItemStock();
	}
	
	private void bySession(){
		if (B_IsCreate) {
			if (session.getAttribute("S_UserId") == null) {
				Executions.sendRedirect("/cssd_pose_logo.zul");
			} else {	
				S_UserId = (String)session.getAttribute("S_UserId");
				S_DeptId = (String)session.getAttribute("S_DeptId");
				S_UserName = (String)session.getAttribute("S_UserName");
				S_IsAdmin = (String)session.getAttribute("S_IsAdmin");
				S_EmpCode = (String)session.getAttribute("S_EmpCode");
				S_DepName = (String)session.getAttribute("S_DepName");
				S_DB = (String)session.getAttribute("S_DB");
	        }
			
			B_IsCreate = false;
		}
	}
	
	private void init(){
		
	}
	
	public void onDisplayItemStock() throws Exception{
		
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;
        
		try{	
			
			rs = stmt.executeQuery(getSqlItemStock());
			
			Listbox_item_stock.getItems().clear();
			
			int I_No = 1;
			
			while(rs.next()){
				Listitem list = new Listitem();

				list.appendChild(new Listcell(I_No + "."));
				list.appendChild(new Listcell(rs.getString("ItemCode")));
				list.appendChild(new Listcell(rs.getString("PackDate")));
				list.appendChild(new Listcell(rs.getString("ExpireDate")));
				list.appendChild(new Listcell(rs.getString("DepName2")));
				
				Listcell lc_status = new Listcell(rs.getString("S_Status"));
				Listcell lc_usage = new Listcell(rs.getString("UsageCode"));
				
				lc_status.setStyle( rs.getBoolean("IsDispatch") ? "color:#3979dd" : "color:#009900");
				lc_usage.setStyle( rs.getBoolean("IsDispatch") ? "color:#3979dd" : "color:#009900");
								
				list.appendChild(lc_status);
				list.appendChild(lc_usage);
				
                Listbox_item_stock.appendChild(list);
                
                I_No++;

			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if (rs != null) {
                rs.close();
            }
            
            if (stmt != null) {
                stmt.close();
            }
            
            if (conn != null) {
                conn.close();
            }
		}
    }

private String getSqlItemStock() {
		
		String S_Sql = "";

		S_Sql = 	
			"	SELECT		itemstock.RowID, "
		+	"				itemstock.ItemCode, "
		+	"				COALESCE(DATE_FORMAT(itemstock.PackDate, '%d-%m-%Y'),'-') AS PackDate, "
		+	"				COALESCE(DATE_FORMAT(itemstock.ExpireDate, '%d-%m-%Y'),'-') AS ExpireDate, "
		+	"				COALESCE(itemstock.DepID,'') AS DepID, "
		+	"				COALESCE(department.DepName2,'') AS DepName2, "
		+	"				itemstock.Qty,"
		+ 	"				itemstock.IsDispatch, "
		+	"				(CASE WHEN itemstock.IsDispatch = 1 THEN 'แผนกเบิกใช้' ELSE 'รับเข้าแผนก' END) AS S_Status, "
		+	"				itemstock.UsageCode, "
		+	"				itemstock.IsPay, "
		+	"				item.itemname AS Item_name "
		
		+	"	FROM		itemstock "
		
		+	"	LEFT JOIN 	department "
		+	"	ON 			itemstock.DepID = department.ID "
		
		+	"	LEFT JOIN 	item "
		+	"	ON 			item.itemcode = itemstock.ItemCode "
		
		+	"	WHERE 		itemstock.DepID = " + S_DeptId + " "

		+	"	AND 		itemstock.IsStatus IN (" + S_ListStatus + ")  "
		
		+ 	"	AND 		( item.itemname LIKE '%" + Textbox_Search.getText() + "%' OR itemstock.ItemCode LIKE '%" + Textbox_Search.getText() + "%' ) "
		+ 	"	AND 		itemstock.IsCancel = 0 "
		+ 	"	AND 		itemstock.IsPay = 1 "
		+ 	"	AND 		DATE(itemstock.ExpireDate) BETWEEN DATE(NOW()) AND DATE(NOW() + INTERVAL " + I_GN_WarningExpiringSoonDay + " DAY) "
		
		+	"	ORDER BY 	itemstock.IsStatus, itemstock.IsDispatch, itemstock.UsageCode "
		
		+	"	LIMIT 1000 ";
		
		System.out.println(S_Sql);
		
		return S_Sql;
	}

	//================================================================================
	// Web Configuration 
	// ================================================================================
	
	public void onDisplayWebConfig() throws Exception{
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
	    Class.forName(c.S_MYSQL_DRIVER);
	    Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
	    conn.setAutoCommit(false);
	     
	    Statement stmt = conn.createStatement();
	     
	 	ResultSet rs = null;
     
		try{	
			
			rs = stmt.executeQuery("SELECT GN_WarningExpiringSoonDay FROM configuration_web LIMIT 1");

			if(rs.next()){
				I_GN_WarningExpiringSoonDay = rs.getInt("GN_WarningExpiringSoonDay");	
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if (rs != null) {
				rs.close();
	     	}
	         
	    	if (stmt != null) {
	    		stmt.close();
	     	}
	         
	    	if (conn != null) {
	    		conn.close();
	    	}
		}
	}

}
