package com.poseintelligence.cssd.transaction;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.poseintelligence.cssd.document.SendSterile;
import com.poseintelligence.cssd.utillity.Number;


@SuppressWarnings("rawtypes")
public class DepartmentExpire extends GenericForwardComposer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6637098633119469091L;
	
	private String 	S_ListStatus = "5";
	
	// Widget
	private Listbox Listbox_item_stock;
	
	private Textbox Textbox_Search;
	
	private Button Button_Send;
	
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
		
		onDisplayItemStock();
		
    }
	
	
	// Event
	public void onClick$Image_SearchDoc(Event event) throws Exception {
		onDisplayItemStock();
	}
	
	public void onOK$Textbox_Search(Event event) throws Exception {
		onDisplayItemStock();
	}
	
	public void onSelect$Listbox_item(Event event) throws Exception {
		onDisplayItemStock();
	}
	
	public void onClick$Button_Send(Event event) throws Exception {
		onConfirmSend();
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
				list.appendChild(new Listcell(rs.getString("Item_name")));
				list.appendChild(new Listcell(rs.getString("PackDate")));
				list.appendChild(new Listcell(rs.getString("ExpireDate")));
				list.appendChild(new Listcell(rs.getString("DepName2")));
				
				Listcell lc_status = new Listcell(rs.getString("S_Status"));
				Listcell lc_usage = new Listcell(rs.getString("UsageCode"));
				Listcell lc_exp_day = new Listcell(rs.getString("Exp_day") + " วัน");
				
				lc_status.setStyle( rs.getBoolean("IsDispatch") ? "color:#3979dd" : "color:#009900");
				lc_usage.setStyle( rs.getBoolean("IsDispatch") ? "color:#3979dd" : "color:#009900");
				lc_exp_day.setStyle( "color:#d9534f");
								
				list.appendChild(lc_status);
				list.appendChild(lc_usage);
				list.appendChild(lc_exp_day);
				
				list.setAttribute("RowID", rs.getString("RowID"));
				
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
		+	"				item.itemname AS Item_name, "
		+	"				DATEDIFF(DATE(NOW()), DATE(itemstock.ExpireDate)) AS Exp_day "
		
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
		+ 	"	AND 		DATE(itemstock.ExpireDate) <= DATE(NOW()) "
		
		+	"	ORDER BY 	itemstock.IsStatus, itemstock.IsDispatch, itemstock.UsageCode "
		
		+	"	LIMIT 1000 ";
		
		System.out.println(S_Sql);
		
		return S_Sql;
	}
	
	public void onConfirmSend() throws Exception{
		
		if(Listbox_item_stock.getSelectedCount() == 0) {
			Messagebox.show("ไม่ได้เลือกรายการ !!", "CSSD", Messagebox.OK, Messagebox.INFORMATION);
			return;
		}

		Messagebox.show("ยืนยันส่งล้างทั้งหมด ?" , "CSSD", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,new EventListener<Event>() {
    		public void onEvent(Event evt) throws Exception {
    			switch (((Integer) evt.getData()).intValue()) {
                  	case Messagebox.YES:
                  		onSend();
                  		break;
                  	case Messagebox.NO:
                  		break;
    			}
    		}
    	});
				
	}
	
	public void onSend() throws Exception{
		
		String S_ListItemStockID = "";
		
		try {
			
			Iterator<Listitem> li = Listbox_item_stock.getSelectedItems().iterator();
						
			while(li.hasNext()){
				Listitem list = (Listitem) li.next();	
				
				S_ListItemStockID += (String)list.getAttribute("RowID") + ",";
			}
			
				
			// Check S_ListItemStockID
			
			if(S_ListItemStockID.equals("")) {
				return;
			}else {
				S_ListItemStockID = S_ListItemStockID.substring(0, S_ListItemStockID.length() - 1);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		try {
			
			// Create SendSterile
			createSendSterile(S_ListItemStockID);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			
		}
	}
	
	private void createSendSterile(String S_ListItemStockID) throws Exception {
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
        ResultSet rs = null;
        
        try{
        	  
        	// Create Send Sterile
        	
        	String S_SSDocNo = null;
        	   
        	S_SSDocNo = SendSterile.getSendSterileDocNo(S_DB, "", S_DeptId, S_UserId, "0", "ส่งล้างจากรายการหมดอายุ");
        			
			if( S_SSDocNo != null && (!S_SSDocNo.equals("")) ){

				String S_SqlInsertSendSterileDetail = 
						
					"	INSERT INTO  sendsteriledetail "
				+ 	"	( "
				+ 	"   	sendsteriledetail.SendSterileDocNo, "
				+ 	"   	sendsteriledetail.ItemStockID, "
				+ 	"   	sendsteriledetail.Qty, "
				+ 	"   	sendsteriledetail.Remark, "
				+ 	"   	sendsteriledetail.UsageCode, "
				+ 	"   	sendsteriledetail.IsSterile, "
				+ 	"   	sendsteriledetail.IsStatus,"
				+ 	"		sendsteriledetail.SSdetail_IsWeb, "
				+ 	"		sendsteriledetail.RefDocNo, "
				+ 	"		sendsteriledetail.ResterileType "
				+ 	"	) "
				
				+ 	"	SELECT 		"
				+ 	"				'" + S_SSDocNo + "', "
				+ 	"   			itemstock.RowID, "
				+ 	"   			1, "
				+ 	"   			'หมดอายุ', "
				+ 	"   			itemstock.UsageCode, "
				+ 	"   			1, "
				+ 	"   			0, "
				+ 	"   			0, "
				+ 	"				'" + S_SSDocNo + "', "
				+ 	"				(SELECT resteriletype.ID FROM resteriletype WHERE ResterileType = 'หมดอายุ' LIMIT 1) "
				
				+ 	"   FROM   		itemstock "
				
				+	"	LEFT JOIN 	item "
				+	"	ON 			item.itemcode = itemstock.ItemCode "

				+	"	WHERE 		itemstock.DepID = " + S_DeptId + " "
		
				+	"	AND 		itemstock.IsStatus = 5 "
				
				+ 	"	AND 		itemstock.RowID IN (" + S_ListItemStockID + ") "
				+ 	"	AND 		itemstock.IsCancel = 0 "
				+ 	"	AND 		itemstock.IsPay = 1 "
				+ 	"	AND 		DATE(itemstock.ExpireDate) <= DATE(NOW()) ";		
				
				// Update Item Stock
				String S_SqlUpdateItemStock = 
						
						"	UPDATE		itemstock s "
						
					+	"	LEFT JOIN 	item "
					+	"	ON 			item.itemcode = s.ItemCode "

					+ 	"	SET			s.IsStatus = 0 "
					
					+	"	WHERE 		s.DepID = " + S_DeptId + " "

					+	"	AND 		s.IsStatus = 5 "
					
					+ 	"	AND 		s.RowID IN (" + S_ListItemStockID + ") "
					+ 	"	AND 		s.IsCancel = 0 "
					+ 	"	AND 		s.IsPay = 1 "
					+ 	"	AND 		DATE(s.ExpireDate) <= DATE(NOW()) ";
						
				System.out.println(S_SqlInsertSendSterileDetail);
				
				System.out.println(S_SqlUpdateItemStock);
				
				int I_ResultForInsert = stmt.executeUpdate(S_SqlInsertSendSterileDetail);
				int I_ResultForUpdate = stmt.executeUpdate(S_SqlUpdateItemStock);
		        
		        conn.commit();
		        
		        // Clear Item Sock
		        Listbox_item_stock.getItems().clear();
		        
		        if(I_ResultForInsert > 0) {
		        	Messagebox.show("สร้างใบส่งล้าง " + S_SSDocNo + " จำนวน " + I_ResultForUpdate + " รายการ เรียบร้อย !!", "CSSD", Messagebox.OK, Messagebox.INFORMATION);
		        	onDisplayItemStock();
		        }else {
		        	Messagebox.show("ไม่สามารถนำรายการส่งล้างได้ !!", "CSSD", Messagebox.OK, Messagebox.EXCLAMATION);
		        }
			}else {
				Messagebox.show("ไม่สามารถสร้างเอกสารส่งล้างได้ !!", "CSSD", Messagebox.OK, Messagebox.ERROR);
			}
     
	        
		}catch(Exception e){
			e.printStackTrace();
		}finally{
	        
	        if (stmt != null) {
	            stmt.close();
	        }
	        
	        if (conn != null) {
	            conn.close();
	        }
	        
	        if (rs != null) {
                rs.close();
            }
		}
	}

}
