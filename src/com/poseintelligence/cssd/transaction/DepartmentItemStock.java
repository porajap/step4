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
import org.zkoss.zul.Window;

import com.poseintelligence.cssd.utillity.Number;


@SuppressWarnings("rawtypes")
public class DepartmentItemStock extends GenericForwardComposer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6637098633119469091L;
	
	private String 	S_ListStatus = "-5, 5, 6";
	
	// Widget
	private Listbox Listbox_item_stock;
	private Listbox Listbox_item;
	
	private Textbox Textbox_Search;
	private Window WinProcess;
	
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
	
	public void onCreate() throws Exception {
		
		bySession();
		
		init();
		
		onDisplayItem();
		
    }
	
	
	// Event
	public void onClick$Image_SearchDoc(Event event) throws Exception {
		onDisplayItem();
	}
	
	public void onOK$Textbox_Search(Event event) throws Exception {
		onDisplayItem();
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
	
	public void onDisplayItem() throws Exception{
		
		final String S_Text = Textbox_Search.getText().toString();
		
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;
        
		try{	
			
			onProcess(true);
			
			rs = stmt.executeQuery(getSqlItem(S_Text));
			
			Listbox_item.getItems().clear();
			Listbox_item_stock.getItems().clear();
			
			int I_No = 1;
			
			while(rs.next()){
				Listitem list = new Listitem();

				list.appendChild(new Listcell(I_No + "."));
				list.appendChild(new Listcell(rs.getString("itemcode")));
				list.appendChild(new Listcell(rs.getString("itemname") + ( rs.getBoolean("IsNonUsage") ? " *" : "" ) ));
				list.appendChild(new Listcell(Number.addComma0d(rs.getString("Qty"))));
				list.appendChild(new Listcell(Number.addComma0d(rs.getString("DispatchQty"))));
				list.appendChild(new Listcell(Number.addComma0d( rs.getInt("Qty") + rs.getInt("DispatchQty") ) ));
							
				//Attribute
                list.setAttribute("itemcode", rs.getString("itemcode"));

                Listbox_item.appendChild(list);
                
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
            
            WinProcess.setVisible(false);
		}
    }
	
	private String getSqlItem(final String S_Text) {
		String S_Sql = "";
		
		S_Sql = 	"	SELECT 		item.itemcode,"
				+ 	"				item.IsNonUsage,"
				+	"				CONCAT((CASE WHEN item.IsNonUsage = 1 THEN 'NUS - ' ELSE ( CASE WHEN item.IsSet = 1 THEN 'SET - ' ELSE 'SINGLE - ' END ) END), item.itemname ) AS itemname, "
				+	"				( "
				+	"					CASE WHEN ( item.IsNonUsage IS NULL OR item.IsNonUsage = 0 ) "
				+	"					THEN COALESCE( "
				+	"						( " 
				+	"							SELECT 		SUM(itemstock.Qty) " 
				+	"							FROM 		itemstock " 
				+	"							WHERE 		itemstock.ItemCode = item.itemcode "
				+	"							AND			itemstock.IsStatus IN (" + S_ListStatus + ") "
				+	"							AND 		itemstock.DepID = '" + S_DeptId + "' "
				+	"							AND			itemstock.IsDispatch = 0 "
				+	"						) "
				+	"						, 0) "
				+	"					ELSE "
				+	"						COALESCE( "
				+	"						( "
				+	"							SELECT 		itemstock.Qty "
				+	"							FROM 		itemstock " 
				+	"							WHERE 		itemstock.ItemCode = item.itemcode "
				+	"							AND			itemstock.IsStatus IN (" + S_ListStatus + ") " 	
				+	"							AND 		itemstock.DepID = '" + S_DeptId + "' "
				+	"							AND			itemstock.IsDispatch = 0 "
				+	"							LIMIT 1 "
				+	"						) "
				+	"						, 0) "	
				+	"					END "
				+	"				) AS Qty, "
				+	"				( "
				+	"					CASE WHEN ( item.IsNonUsage IS NULL OR item.IsNonUsage = 0 ) "
				+	"					THEN COALESCE( "
				+	"						( " 
				+	"							SELECT 		SUM(itemstock.Qty) " 
				+	"							FROM 		itemstock " 
				+	"							WHERE 		itemstock.ItemCode = item.itemcode "
				+	"							AND			itemstock.IsStatus IN (" + S_ListStatus + ") "
				+	"							AND 		itemstock.DepID = '" + S_DeptId + "' "
				+	"							AND			itemstock.IsDispatch = 1 "
				+	"						) "
				+	"						, 0) "
				+	"					ELSE "
				+	"						COALESCE( "
				+	"						( "
				+	"							SELECT 		itemstock.Qty "
				+	"							FROM 		itemstock " 
				+	"							WHERE 		itemstock.ItemCode = item.itemcode "
				+	"							AND			itemstock.IsStatus IN (" + S_ListStatus + ") " 
				+	"							AND 		itemstock.DepID = '" + S_DeptId + "' "
				+	"							AND			itemstock.IsDispatch = 1 "
				+	"							LIMIT 1 "
				+	"						) "
				+	"						, 0) "	
				+	"					END "
				+	"				) AS DispatchQty "
				
				+	"	FROM 		itemstock AS isk "  
						
				+	"	INNER JOIN 	item "
				+	"	ON			item.itemcode = isk.ItemCode "
				
				+ 	"	WHERE		isk.DepID = " + S_DeptId + " ";
			
		
		if (!S_Text.equals("")) {
			
			S_Sql +="	AND  		(item.itemname LIKE '%" + S_Text + "%' " 
				+	"	OR 			item.Alternatename LIKE '%" + S_Text + "%' " 
				+	"	OR 			item.itemcode LIKE '%" + S_Text + "%' ) ";
		}			
		
		S_Sql += "GROUP BY item.itemcode ";
		
		S_Sql = "SELECT * FROM (" + S_Sql + ") AS i ORDER BY i.Qty DESC,  i.DispatchQty DESC, i.itemcode LIMIT 100 ";
		
		System.out.println(S_Sql);
				
		return S_Sql;
		
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
		
		String S_ItemCode = (String)Listbox_item.getSelectedItem().getAttribute("itemcode");
		
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
		
		+	"	WHERE 		itemstock.IsStatus IN (" + S_ListStatus + ")  "
		
		+	"	AND 		itemstock.ItemCode = '" + S_ItemCode + "' "
		
		+	"	AND 		itemstock.DepID = '" + S_DeptId + "' "
		
		+	"	ORDER BY 	itemstock.IsStatus, itemstock.IsDispatch, itemstock.UsageCode "
		
		+	"	LIMIT 1000 ";
		
		System.out.println(S_Sql);
		
		return S_Sql;
	}

	public void onProcess(boolean b) throws Exception {
    	WinProcess.setVisible(b);
    	WinProcess.setFocus(b);
    	WinProcess.setPosition("center");
    	WinProcess.setMode("modal");
    }
}
