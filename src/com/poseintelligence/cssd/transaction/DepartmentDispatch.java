package com.poseintelligence.cssd.transaction;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.poseintelligence.cssd.document.Dispatch;
import com.poseintelligence.cssd.model.ModelMaster;
import com.poseintelligence.cssd.utillity.DateTime;

@SuppressWarnings("rawtypes")
public class DepartmentDispatch extends GenericForwardComposer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1104997994768769325L;
	
	private boolean B_IsCreate = true;
		
	// Variable Session
	private Session session = Sessions.getCurrent();
	
	private String 	S_UserId,
					S_DeptId,
					S_UserName,
					S_IsAdmin,
					S_EmpCode,
					S_DepName,
					S_DB;
	
	// Widget
	private Listbox Listbox_item_stock;
	private Listbox Listbox_DocumentDetail;
	
	private Textbox Textbox_SearchItemStock;
	
	private Datebox Datebox_SDocDate;
	
	private Combobox Combobox_Document;
	
	private Checkbox Checkbox_Mode;
	
	private Label Label_Search;
	private Image Image_Search;
	
	private String S_DocNo = null;
	private int I_IsStatus = 0;
	
	// Variable Local
	List<ModelMaster> Model_OccurrenceType = new ArrayList<>();
	
	public void onCreate() throws Exception {
		
		bySession();
		
		init();
	
    }
	
	private void bySession(){
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
	        }
			
			B_IsCreate = false;
		}
	}
	
	private void init() throws Exception{
		Datebox_SDocDate.setText(DateTime.getDateNow());
		
		defineDocument();
	}
	
	// Event
	public void onClick$Button_Close(Event event) throws Exception {
		
		if(I_IsStatus == 1 || Combobox_Document.getText().equals("")) {
			return;
		}
			
		Messagebox.show("ยืนยันการปิดเอกสารเบิกใช้ ?" , "CSSD", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,new EventListener<Event>() {
    		public void onEvent(Event evt) throws Exception {
    			switch (((Integer) evt.getData()).intValue()) {
                  	case Messagebox.YES:
                  		updateDispatch(Combobox_Document.getText());
                  		break;
                  	case Messagebox.NO:
                  		break;
    			}
    		}
    	});
	}
	
	public void onOK$Textbox_SearchItemStock(Event event) throws Exception {
		if(Checkbox_Mode.isChecked()) {
			// Find ItemStock
			addDispatchByQR(Textbox_SearchItemStock.getText());
		}else {
			// Display ItemStock
			displayItemStock();
		}
	}
	
	public void onClick$Image_Search(Event event) throws Exception {
		displayItemStock();
	}
	
	public void onChange$Datebox_EDocDate(Event event) throws Exception {
		// Display Pay
		defineDocument();
	}
	
	public void onSelect$Combobox_Document(Event event) throws Exception {
		// Display Dispatch Detail
		try {
			
			S_DocNo = Combobox_Document.getSelectedItem().getValue();
			I_IsStatus = (int) Combobox_Document.getSelectedItem().getAttribute("IsStatus");
	
			displayDispatchDetail(S_DocNo, I_IsStatus);
			
		}catch(Exception e) {
			Listbox_DocumentDetail.getItems().clear();
		}
	}
	
	public void onOK$Combobox_Document(Event event) throws Exception {
		try {
			Combobox_Document.setText("");
			S_DocNo = null;
			I_IsStatus = 0;
			Listbox_DocumentDetail.getItems().clear();
			Textbox_SearchItemStock.setText("");
			Textbox_SearchItemStock.focus();
		}catch(Exception e) {
			
		}
	}
	
	public void onCheck$Checkbox_Mode(Event event) throws Exception {
		try {
			boolean B_Chk = !Checkbox_Mode.isChecked();
			
			//Button_SearchItemStock.setVisible(B_Chk);
			Label_Search.setValue(B_Chk ? "ค้นหาอุปกรณ์" : "สแกนรหัสอุปกรณ์");
			
			Image_Search.setSrc(B_Chk ? "/images/ic_search_c.png" : "/images/ic_barcode.jpg");
			
			if(!B_Chk)
				Listbox_item_stock.getItems().clear();
			else {
				displayItemStock();
			}
			
			Textbox_SearchItemStock.setText("");
			Textbox_SearchItemStock.focus();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// ----------------------------------------------------------------------
	
	private String getSqlItemStock() {
		
		String S_Sql = "";

		S_Sql = 	
			"	SELECT		itemstock.ItemCode, "
		+	"				item.itemname AS Item_name, "
		+	"				SUM(itemstock.Qty) Qty "	
		
		+	"	FROM		itemstock "
		
		+	"	INNER JOIN 	item "
		+	"	ON 			item.itemcode = itemstock.ItemCode "
		
		+	"	WHERE 		itemstock.IsStatus = 5  "
		+	"	AND 		itemstock.IsDispatch = 0 "		
		+	"	AND 		itemstock.IsPay = 1 ";
		
		String S_Text = Textbox_SearchItemStock.getText();
		
		if (!S_Text.equals("")) {
			
			S_Sql +="	AND  		(item.itemname LIKE '%" + S_Text + "%' " 
				+	"	OR 			item.Alternatename LIKE '%" + S_Text + "%' " 
				+	"	OR 			item.itemcode LIKE '%" + S_Text + "%' ) ";
		}	
		
		S_Sql +=
		 	"	GROUP BY 	itemstock.ItemCode," 
		+ 	"				item.itemname "
		
		+	"	LIMIT 1000 ";
		
		// System.out.println(S_Sql);
		
		return "SELECT * FROM (" + S_Sql + ") AS i ORDER BY i.Qty DESC, i.ItemCode ";
	}
	
	public void displayItemStock() throws Exception{
		
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
				list.appendChild(new Listcell());
				list.appendChild(new Listcell(I_No + "."));
				list.appendChild(new Listcell(rs.getString("ItemCode")));
				list.appendChild(new Listcell(rs.getString("Item_name")));
				
				final Listcell lc_qty = new Listcell(rs.getString("Qty"));
				lc_qty.setId("Listcell" + rs.getString("ItemCode"));
				
				list.appendChild(lc_qty);
				list.appendChild(newListcell(lc_qty, rs.getString("ItemCode")));
				
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
	
	private Listcell newListcell(final Listcell lc_qty, final String S_ItemCode){
		final Listcell lc = new Listcell();
		final Hlayout hlo = new Hlayout();
		final Decimalbox dbx = new Decimalbox();
		
		dbx.setHeight("30px");
		dbx.setWidth("55px");
		dbx.setStyle("background: #FEFEFE; color:#333333; text-align:center; font-size:20px;");
		
		final Button btn_p = new Button("+");
		final Button btn_m = new Button("-");
		
		btn_p.setHeight("30px");
		btn_p.setWidth("33px");
		btn_p.setStyle("background: #52BE80; color:#ffffff;");
		
		btn_m.setHeight("30px");
		btn_m.setWidth("30px");
		btn_m.setStyle("background: #52BE80; color:#ffffff;");
		
		btn_p.addEventListener("onClick", new EventListener<Event>() {
			public void onEvent(Event event) throws Exception {
				
				int I_TextQty = 0;
				int I_Qty = Integer.valueOf(lc_qty.getLabel()).intValue();
				
				try {
					I_TextQty = Integer.valueOf(dbx.getText()).intValue();
				}catch(Exception e) {
					
				}
				
				if(I_Qty > 0) {
					if(onAddDispatch(S_ItemCode)){
						dbx.setText(Integer.toString(I_TextQty+1));	
						lc_qty.setLabel(Integer.toString(I_Qty-1));	
					
					}
				}
										
		    }
		});
		
		btn_m.addEventListener("onClick", new EventListener<Event>() {
			public void onEvent(Event event) throws Exception {
	
				if(S_DocNo == null) {
					Messagebox.show("ยังไม่ได้เลือกเอกสารเบิก !!");
					return;
				}
				
				int I_TextQty = 0;
				int I_Qty = Integer.valueOf(lc_qty.getLabel()).intValue();
				
				try {
					I_TextQty = Integer.valueOf(dbx.getText()).intValue();
				}catch(Exception e) {
					
				}
				
				if(I_TextQty > 0) {
					if(onRemoveDispatchDetail(S_DocNo, S_ItemCode)){
						dbx.setText(Integer.toString(I_TextQty - 1));	
						lc_qty.setLabel(Integer.toString(I_Qty + 1));
						
						displayDispatchDetail(S_DocNo, I_IsStatus);
					}
				}			
		    }
		});
		
		dbx.addEventListener("onOK", new EventListener<Event>() {
			public void onEvent(Event event) throws Exception {
							
		    }
		});
		
		hlo.appendChild(btn_m);
		hlo.appendChild(dbx);
		hlo.appendChild(btn_p);
		hlo.setSpacing("3px");
		
		lc.appendChild(hlo);
		
		
		return lc;
	}

	private boolean onAddDispatch(final String S_ItemCode) throws Exception{
		// Check have document
		if(S_DocNo == null) {
			
			S_DocNo = Dispatch.getdispatchstockDocNo(S_DB, "", S_DeptId, S_UserId, "0", "");
			
			if(S_DocNo == null)
				return false;
			
			defineDocument();
			
			Combobox_Document.setText(S_DocNo);
			
			I_IsStatus = 0;
		}
		
		return addDispatch(S_DocNo, S_ItemCode);
		
	}
	
	private boolean addDispatchByQR(final String S_Qr) throws Exception{
		if(I_IsStatus == 1) {
			Messagebox.show("เอกสารเบิกใช้นี้ ถูกปิดแล้ว !!" , "CSSD", Messagebox.OK, Messagebox.EXCLAMATION,new EventListener<Event>() {
	    		public void onEvent(Event evt) throws Exception {
	    			switch (((Integer) evt.getData()).intValue()) {
	                  	case Messagebox.OK:
	                  		
	                  		Textbox_SearchItemStock.setText("");
	                  		Textbox_SearchItemStock.focus();
	                  		
	                  		break;    	
	    			}
	    		}
	    	});
			
			return false;
		}
		
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;
    	     
		try{	

			String S_Sql = 
						"	SELECT 		itemstock.RowID "
					+ 	"	FROM 		itemstock "
					+	"	WHERE 		itemstock.UsageCode = '" + S_Qr + "' "
					+ 	"	AND			itemstock.IsStatus = 5 "
					+	"	AND 		itemstock.IsDispatch = 0 "		
					+	"	AND 		itemstock.IsPay = 1 "
					+ 	"	ORDER BY 	itemstock.ExpireDate ASC, itemstock.RowID ASC "
					+ 	"	LIMIT 1 ";
			
			System.out.println(S_Sql);
			
			rs = stmt.executeQuery(S_Sql);
			
			if(rs.next()) {
				
				// Check have Doc
				if(S_DocNo == null || S_DocNo.equals("")) {
					
					S_DocNo = Dispatch.getdispatchstockDocNo(S_DB, "", S_DeptId, S_UserId, "0", "");
					
					if(S_DocNo == null)
						return false;
					
					defineDocument();
					
					Combobox_Document.setText(S_DocNo);
					
					I_IsStatus = 0;
				}
	
				// Add to Dispatch
				String S_SqlInsert = 
							"INSERT INTO	dispatchstockdetail "
							+ 	"("
							+ 	"	DocNo, " 
							+ 	"	ItemstockID, " 
							+ 	"	Qty, " 
							+ 	"	IsStatus, " 
							+ 	"	IsScan, " 
							+ 	"	B_ID " 
							+ 	") "
							+ 	"	VALUES "
							+ 	"( "
							+ 	"	'" + S_DocNo + "', "
							+ 	"	" + rs.getString("RowID") + ", "
							+ 	"	1,"
							+ 	"	1,"
							+ 	"	0,"
							+ 	"	1 "
							+ 	") ";
							
				
				// Update ItemStock
				String S_SqlUpdate = 
							"UPDATE		itemstock "
						+ 	"SET 		IsDispatch = 1, "
						+ 	"			LastDispatchModify = NOW()  "
						+ 	"WHERE		RowID = " + rs.getString("RowID");
				
				System.out.println(S_SqlInsert);
				System.out.println(S_SqlUpdate);
		
				stmt.executeUpdate(S_SqlInsert);	
				stmt.executeUpdate(S_SqlUpdate);
				
				displayDispatchDetail(S_DocNo, I_IsStatus);
				
				return true;
				
			}else {
				
				Messagebox.show("ไม่พบรายการ !!" , "CSSD", Messagebox.OK, Messagebox.EXCLAMATION,new EventListener<Event>() {
		    		public void onEvent(Event evt) throws Exception {
		    			switch (((Integer) evt.getData()).intValue()) {
		                  	case Messagebox.OK:
		                  		Textbox_SearchItemStock.setText("");
		                  		Textbox_SearchItemStock.focus();
		                  		break;
		                  	
		    			}
		    		}
		    	});
				
				return false;
			}

		}catch(Exception e){
			e.printStackTrace();
			return false;
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
            
            Textbox_SearchItemStock.setText("");
			Textbox_SearchItemStock.focus();
		} 
	}
	
	private boolean addDispatch(final String S_DocNo, final String S_ItemCode) throws Exception{
		
		if(I_IsStatus == 1) {
			Messagebox.show("เอกสารเบิกใช้นี้ ถูกปิดแล้ว !!" , "CSSD", Messagebox.OK, Messagebox.EXCLAMATION,new EventListener<Event>() {
	    		public void onEvent(Event evt) throws Exception {
	    			switch (((Integer) evt.getData()).intValue()) {
	                  	case Messagebox.OK:
	                  		
	                  		Textbox_SearchItemStock.setText("");
	                  		Textbox_SearchItemStock.focus();
	                  		
	                  		break;    	
	    			}
	    		}
	    	});
			
			return false;
		}
		
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;
        
		try{	

			String S_Sql = 
						"	SELECT 		itemstock.RowID "
					+ 	"	FROM 		itemstock "
					+	"	WHERE 		itemstock.ItemCode = '" + S_ItemCode + "' "
					+ 	"	AND			itemstock.IsStatus = 5 "
					+	"	AND 		itemstock.IsDispatch = 0 "		
					+	"	AND 		itemstock.IsPay = 1 "
					+ 	"	ORDER BY 	itemstock.ExpireDate ASC, itemstock.RowID ASC "
					+ 	"	LIMIT 1 ";
			
			System.out.println(S_Sql);
			
			rs = stmt.executeQuery(S_Sql);
			
			if(rs.next()) {
				// Add to Dispatch
				String S_SqlInsert = 
							"INSERT INTO	dispatchstockdetail "
							+ 	"("
							+ 	"	DocNo, " 
							+ 	"	ItemstockID, " 
							+ 	"	Qty, " 
							+ 	"	IsStatus, " 
							+ 	"	IsScan, " 
							+ 	"	B_ID " 
							+ 	") "
							+ 	"	VALUES "
							+ 	"( "
							+ 	"	'" + S_DocNo + "', "
							+ 	"	" + rs.getString("RowID") + ", "
							+ 	"	1,"
							+ 	"	1,"
							+ 	"	0,"
							+ 	"	1 "
							+ 	") ";
							
				
				// Update ItemStock
				String S_SqlUpdate = 
							"UPDATE		itemstock "
						+ 	"SET 		IsDispatch = 1, "
						+ 	"			LastDispatchModify = NOW()  "
						+ 	"WHERE		RowID = " + rs.getString("RowID");
				
				System.out.println(S_SqlInsert);
				System.out.println(S_SqlUpdate);
		
				stmt.executeUpdate(S_SqlInsert);	
				stmt.executeUpdate(S_SqlUpdate);
				
				displayDispatchDetail(S_DocNo, I_IsStatus);
				
				return true;
				
			}else {
				return false;
			}

		}catch(Exception e){
			e.printStackTrace();
			return false;
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
	
	private boolean onDeleteDispatchDetail(final String S_ID, final String S_RowID) throws Exception {
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
    	
        try {
			// Remove Dispatch
			String S_SqlDelete = 
					"	DELETE	"
				+ 	"	FROM	dispatchstockdetail "
				+ 	"	WHERE 	ID = " + S_ID + " ";

			// Update ItemStock
			String S_SqlUpdate = 
						"UPDATE		itemstock "
					+ 	"SET 		IsDispatch = 0,"
					+ 	"			LastDispatchModify = NOW()  "
					+ 	"WHERE		RowID = " + S_RowID;
	
			stmt.executeUpdate(S_SqlDelete);	
			stmt.executeUpdate(S_SqlUpdate);
			
			displayDispatchDetail(Combobox_Document.getText(), I_IsStatus);
			
			return true;
				
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
            
            if (stmt != null) {
                stmt.close();
            }
            
            if (conn != null) {
                conn.close();
            }
		}
	}

	private boolean onRemoveDispatchDetail(final String S_DocNo, final String S_ItemCode) throws Exception{
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;
        
		try{	

			String S_Sql = 
						"	SELECT 		itemstock.RowID "
					
					+ 	"	FROM 		itemstock "
								
					+ 	"	INNER JOIN 	dispatchstockdetail "
					+ 	"	ON			dispatchstockdetail.ItemstockID = itemstock.RowID "
					
					+	"	WHERE 		itemstock.ItemCode = '" + S_ItemCode + "' "
					+ 	"	AND			dispatchstockdetail.DocNo = '" + S_DocNo + "' "
					+ 	"	AND			itemstock.IsStatus = 5 "
					+	"	AND 		itemstock.IsDispatch = 1 "		
					+	"	AND 		itemstock.IsPay = 1 "
					
					+ 	"	ORDER BY 	itemstock.ExpireDate DESC, itemstock.RowID DESC "
					
					+ 	"	LIMIT 1 ";
			
			rs = stmt.executeQuery(S_Sql);
			
			if(rs.next()) {
				
				// Remove Dispatch
				String S_SqlDelete = 
						"	DELETE	"
					+ 	"	FROM	dispatchstockdetail "
					+ 	"	WHERE 	DocNo = '" + S_DocNo + "' "
					+ 	"	AND		ItemstockID = " + rs.getString("RowID");

				// Update ItemStock
				String S_SqlUpdate = 
							"UPDATE		itemstock "
						+ 	"SET 		IsDispatch = 0,"
						+ 	"			LastDispatchModify = NOW()  "
						+ 	"WHERE		RowID = " + rs.getString("RowID");
		
				stmt.executeUpdate(S_SqlDelete);	
				stmt.executeUpdate(S_SqlUpdate);
				
				return true;
				
			}else {
				return false;
			}

		}catch(Exception e){
			e.printStackTrace();
			return false;
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
	
	private void updateDispatch(final String S_DocNo) throws Exception{
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
        
		try{	
			// Update Dispatch
			String S_SqlUpdate = 
						"UPDATE		dispatchstock "
					+ 	"SET 		IsStatus = 1,"
					+ 	"			ModifyDate = NOW()  "
					+ 	"WHERE		DocNo = '" + S_DocNo + "' " ;
	
			stmt.executeUpdate(S_SqlUpdate);
				
		}catch(Exception e){
			e.printStackTrace();
		}finally{
            
            if (stmt != null) {
                stmt.close();
            }
            
            if (conn != null) {
                conn.close();
            }
            
            // Set Document
            defineDocument();
            
            // Display ItemStock
         	displayItemStock();
                   
		}
	}
	
	private void displayDispatchDetail(final String S_DocNo, final int IsStatus) throws Exception{
		
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;
        
		try{	

			String S_Sql = 
					"	SELECT 		dispatchstockdetail.ID,"
				+ 	"				itemstock.RowID,"
				+ 	"				itemstock.ItemCode,"
				+ 	"				itemstock.UsageCode,"
				+ 	"				item.itemname,"
				+	"				COALESCE(DATE_FORMAT(itemstock.PackDate, '%d-%m-%Y'),'-') AS PackDate, "
				+	"				COALESCE(DATE_FORMAT(itemstock.ExpireDate, '%d-%m-%Y'),'-') AS ExpireDate "
				
				+ 	"	FROM 		dispatchstockdetail "
							
				+ 	"	INNER JOIN 	itemstock "
				+ 	"	ON			dispatchstockdetail.ItemstockID = itemstock.RowID "
				
				+ 	"	LEFT JOIN 	item "
				+ 	"	ON			item.itemcode = itemstock.ItemCode "
				
				+	"	WHERE 		dispatchstockdetail.DocNo = '" + S_DocNo + "' "
				
				+ 	"	AND			itemstock.IsStatus = 5 "
				+	"	AND 		itemstock.IsDispatch = 1 "		
				+	"	AND 		itemstock.IsPay = 1 "
				
				+ 	"	ORDER BY 	itemstock.ItemCode, itemstock.UsageCode "
				
				+ 	"	LIMIT 1000 ";
			
			rs = stmt.executeQuery(S_Sql);
			
			Listbox_DocumentDetail.getItems().clear();
			int I_No = 1;
			
			while(rs.next()){
				Listitem li = new Listitem();
				li.appendChild(new Listcell(I_No + "."));
				li.appendChild(new Listcell(rs.getString("UsageCode")));
				li.appendChild(new Listcell(rs.getString("itemname")));
				li.appendChild(new Listcell(rs.getString("PackDate")));
				li.appendChild(new Listcell(rs.getString("ExpireDate")));
				
				if(IsStatus == 1) {
					li.appendChild(new Listcell());
				}else {
					final String S_RowID = rs.getString("RowID");
					final String S_ID = rs.getString("ID");
					
					Listcell lc_del = new Listcell("", "/images/ic_delete.png");
					
					lc_del.addEventListener("onClick", new EventListener<Event>() {
						public void onEvent(Event event) throws Exception {
							
							Messagebox.show("ยืนยันการลบรายการ ?" , "CSSD", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,new EventListener<Event>() {
					    		public void onEvent(Event evt) throws Exception {
					    			switch (((Integer) evt.getData()).intValue()) {
					                  	case Messagebox.YES:
					                  		onDeleteDispatchDetail(S_ID, S_RowID);
					                  		break;
					                  	case Messagebox.NO:
					                  		break;
					    			}
					    		}
					    	});
				        }
				    });
					
					li.appendChild(lc_del);
					li.setAttribute("RowID", S_RowID);
				}
	
				Listbox_DocumentDetail.appendChild(li);
				
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
	
	// ================================================================================
	// Drop-down
	// ================================================================================
	
	private void defineDocument() throws Exception{
		
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;
        
		try{	

			String S_Sql = 
						"SELECT 	dispatchstock.DocNo, "
					+ 	"			(CASE WHEN dispatchstock.IsStatus = 0 THEN 'ยังไม่ปิด' ELSE 'ปิดเอกสาร' END) AS Status, "
					+ 	"			dispatchstock.IsStatus, "
					+ 	"			DATE_FORMAT(DocDate, '%d-%m-%Y') AS DocDate,"
					+ 	" 			( "
					+ 	"				SELECT 		COUNT(*) "
					+ 	"				FROM 		dispatchstockdetail "
					+ 	"				INNER JOIN 	itemstock "
					+ 	"				ON			dispatchstockdetail.ItemstockID = itemstock.RowID "
					+ 	"				WHERE 		dispatchstockdetail.DocNo = dispatchstock.DocNo "
					+ 	"				AND			itemstock.IsStatus = 5 "
					+	"				AND 		itemstock.IsDispatch = 1 "		
					+	"				AND 		itemstock.IsPay = 1 "
					+ 	"			) AS c "
					
					+ 	"FROM 		dispatchstock ";
			
			if(Datebox_SDocDate.getText().length() == 10) {
				S_Sql += 	
						"WHERE		DATE(dispatchstock.DocDate) = DATE('" + DateTime.convertDate( Datebox_SDocDate.getText() ) + "') ";
			}
					
			S_Sql += 	"ORDER BY 	ID DESC "
					
					+ 	"LIMIT 		50 ";
			
			System.out.println(S_Sql);
			
			rs = stmt.executeQuery(S_Sql);
			
			Combobox_Document.getItems().clear();
			Combobox_Document.setText("");
				
			while(rs.next()){
				Comboitem cbi = new Comboitem();
				cbi.setLabel(rs.getString("DocNo"));
				cbi.setValue(rs.getString("DocNo"));
				
				cbi.setDescription("วันที่ - " + rs.getString("DocDate") + " , สถานะ - " + rs.getString("Status") + " , จำนวน " + rs.getString("c") + " รายการ.");
				
				cbi.setImage(rs.getBoolean("IsStatus") ? "/images/ic_check_green.png" : "/images/ic_alert.png");
				
				cbi.setAttribute("IsStatus", rs.getInt("IsStatus"));
				
				Combobox_Document.appendChild(cbi);
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
            
            Listbox_DocumentDetail.getItems().clear();
		}
    }

}
