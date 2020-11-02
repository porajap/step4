package com.poseintelligence.cssd.transaction;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
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
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.poseintelligence.cssd.document.SendSterile;
import com.poseintelligence.cssd.model.ModelMaster;
import com.poseintelligence.cssd.utillity.DateTime;

@SuppressWarnings("rawtypes")
public class DepartmentSendSterileNewUsage extends GenericForwardComposer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3525844790119368775L;
	
	private boolean B_IsCreate = true;
	
	// Variable Configuration
	private int SS_IsFindStatus = 0;
	private boolean SS_IsFindItemDepartment = true;
		
	// Variable Session
	private Session session = Sessions.getCurrent();
	
	private String 	S_UserId,
					S_DeptId,
					S_UserName,
					S_IsAdmin,
					S_EmpCode,
					S_DepName,
					S_DB;
	
	private String S_DocNo = null;
	private String S_TempStatus = "-55";
	//private String S_TempCreateItemStockStatus = "-55";
	
	// Widget
	private Listbox Listbox_Item;
	private Listbox Listbox_Document;	
	private Listbox Listbox_DocumentDetail;
	
	private Textbox Textbox_SearchItemStock;
	private Textbox Textbox_Search;
	
	private Datebox Datebox_SDocDate;
	private Datebox Datebox_EDocDate;	
	
	private Combobox Combobox_Status;
	
	private Checkbox Checkbox_Mode;
	
	private Button Button_Send;
	//private Button Button_SearchItemStock;
	private Label Label_Search;
	private Image Image_Search;
	
	// private Checkbox Checkbox_IsDispatch;
	
	
	// Variable Local
	List<ModelMaster> Model_OccurrenceType = new ArrayList<>();
	
	public void onCreate() throws Exception {
		
		bySession();
		
		// Configuration
		onDisplayWebConfig();
		
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
		Datebox_SDocDate.setText(DateTime.getDateLastMonth());
		Datebox_EDocDate.setText(DateTime.getDateNow());
	}
	
	// Event
	public void onSelect$Listbox_Document(Event event) throws Exception {
		
		S_DocNo = (String)Listbox_Document.getSelectedItem().getAttribute("DocNo");
		
		// Display Pay Detail
		if(S_DocNo != null && (!S_DocNo.equals("")))
			onDisplayDetail(S_DocNo, true);
	}
	
	public void onSelect$Combobox_Status(Event event) throws Exception {
		// Display Pay
		onDisplayDocument(null);
	}
	
	public void onOK$Textbox_Search(Event event) throws Exception {
		// Display Pay
		onDisplayDocument(null);
	}
	
	public void onClick$Button_SearchDoc(Event event) throws Exception {
		// Display Pay
		onDisplayDocument(null);
	}
	
	public void onClick$Image_SearchDoc(Event event) throws Exception {
		// Display Pay
		onDisplayDocument(null);
	}
	
	public void onChange$Datebox_SDocDate(Event event) throws Exception {
		// Display Pay
		onDisplayDocument(null);
	}
	
	public void onChange$Datebox_EDocDate(Event event) throws Exception {
		// Display Pay
		onDisplayDocument(null);
	}
	
	public void onOK$Textbox_SearchItemStock(Event event) throws Exception {
		if(Checkbox_Mode.isChecked()) {
			// Find ItemStock
			addItemByQR(Textbox_SearchItemStock.getText());
		}else {
			// Display ItemStock
			onDisplayItem();
		}
	}
	
	public void onClick$Button_SearchItemStock(Event event) throws Exception {
		// Display ItemStock
		onDisplayItem();
	}
	
	public void onClick$Image_Search(Event event) throws Exception {
		// Display ItemStock
		onDisplayItem();
	}
	
	public void onCheck$Checkbox_IsDispatch(Event event) throws Exception {
		// Display ItemStock
		onDisplayItem();
	}
	
	public void onClick$Button_Send(Event event) throws Exception {
		onConfirmSend();
	}
	
	public void onCheck$Checkbox_Mode(Event event) throws Exception {
		try {
			boolean B_Chk = !Checkbox_Mode.isChecked();
			
			Button_Send.setVisible(B_Chk);
			//Button_SearchItemStock.setVisible(B_Chk);
			Label_Search.setValue(B_Chk ? "ค้นหาอุปกรณ์" : "สแกนรหัสอุปกรณ์");
			
			Image_Search.setSrc(B_Chk ? "/images/ic_search_c.png" : "/images/ic_barcode.jpg");
			
			if(!B_Chk)
				Listbox_Item.getItems().clear();
			
			Textbox_SearchItemStock.setText("");
			Textbox_SearchItemStock.focus();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void addItemByQR(String S_QR) throws Exception {
		
		String S_Sql = "";
		
		S_Sql = 	
			"	SELECT		item.itemcode, "
		+ 	"				item.itemname,"
		+	"				COALESCE(units.UnitName, '-') AS UnitName " 

		+	"	FROM		item ";
		
		if(SS_IsFindItemDepartment) {
			
			S_Sql +=
					
		 	"	INNER JOIN	itemdepartment "
		+ 	"	ON			itemdepartment.itemcode = item.itemcode ";
			
		}
		
		S_Sql +=	
			"	LEFT JOIN 	units  "
		+	"	ON			units.ID = `item`.UnitID  "
		
		+	"	WHERE 		item.itemcode = '" + S_QR + "' ";
		
		S_Sql +=
			"	LIMIT 1 ";
		
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;
        
		try{	
			
			rs = stmt.executeQuery(S_Sql);
			
			if(rs.next()) {
				final Listitem list = new Listitem();
				
				final Intbox inb = new Intbox();
				inb.setWidth("99%");
				inb.setStyle("text-align:center;font-weight:bold;");
				
				inb.addEventListener("onBlur", new EventListener<Event>() {
					public void onEvent(Event event) throws Exception {						
						list.setSelected( !inb.getText().trim().equals("") );
			        }
			    });
				
				Listcell lc = new Listcell();
				lc.appendChild(inb);

				list.appendChild(new Listcell());
				list.appendChild(new Listcell(rs.getString("itemcode")));
				list.appendChild(new Listcell(rs.getString("itemname")));
				list.appendChild(lc);
				list.appendChild(new Listcell(rs.getString("UnitName")));
							
				list.setAttribute("itemcode", rs.getString("itemcode"));
								
                Listbox_Item.appendChild(list);
                
			}else {
				Messagebox.show("ไม่พบรายการ !!");
				return;				
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
            
            Textbox_SearchItemStock.setText("");
	        
	        Textbox_SearchItemStock.focus();
		}	
	}
	
	// ==================================================================================================
	
	public void onDisplayItem() throws Exception{
		
		if(Checkbox_Mode.isChecked())
			return;
		
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;
        
		try{	
			
			rs = stmt.executeQuery(getSqlItem());
			
			Listbox_Item.getItems().clear();
			
			int I_No = 1;
	
			while(rs.next()){
				final Listitem list = new Listitem();
				
				final Intbox inb = new Intbox();
				inb.setWidth("99%");
				inb.setStyle("text-align:center;font-weight:bold;");
				
				inb.addEventListener("onBlur", new EventListener<Event>() {
					public void onEvent(Event event) throws Exception {						
						list.setSelected( !inb.getText().trim().equals("") );
			        }
			    });
				
				Listcell lc = new Listcell();
				lc.appendChild(inb);

				list.appendChild(new Listcell());
				list.appendChild(new Listcell(rs.getString("itemcode")));
				list.appendChild(new Listcell(rs.getString("itemname")));
				list.appendChild(lc);
				list.appendChild(new Listcell(rs.getString("UnitName")));
							
				list.setAttribute("itemcode", rs.getString("itemcode"));
								
                Listbox_Item.appendChild(list);
                
                I_No++;
                
			}
			
			if(I_No == 1) {
				 Messagebox.show("ไม่พบรายการ !!", "CSSD", Messagebox.OK, Messagebox.EXCLAMATION);
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
            
            Textbox_SearchItemStock.setText("");
            Textbox_SearchItemStock.focus();
            
		}
    }

	private String getSqlItem() {
		
		String S_Sql = "";
		
		S_Sql = 	
			"	SELECT		item.itemcode, "
		+	"				item.itemname, "
		+ 	"				COALESCE(units.UnitName, '-') AS UnitName "
		
		+	"	FROM		item "
		
		+	"	LEFT JOIN 	units  "
		+	"	ON			units.ID = `item`.UnitID  ";
		
		
		String S_Text = Textbox_SearchItemStock.getText();
		
		if(!S_Text.trim().equals("")) {
			S_Sql +=	
			"	WHERE 		("
			+ 	"				item.itemcode 		LIKE '%" + S_Text + "%' "
			+ 	"				OR item.itemname 	LIKE '%" + S_Text + "%' "
			+ 	"			) ";
		}
	
		S_Sql +=
			"	ORDER BY 	item.itemcode "
		
		+	"	LIMIT 100 ";
		
		System.out.println(S_Sql);
		
		return S_Sql;
	}
	
	public void onConfirmSend() throws Exception{
				
		if(Listbox_Item.getSelectedCount() == 0) {
			Messagebox.show("ไม่ได้เลือกรายการ !!", "CSSD", Messagebox.OK, Messagebox.INFORMATION);
			return;
		}
						
		Messagebox.show("ยืนยันนำเข้ารายการ ?" , "CSSD", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,new EventListener<Event>() {
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
		
		try {
			
			Iterator<Listitem> li = Listbox_Item.getSelectedItems().iterator();
			
			ArrayList<String> AS_ListItemCode = new ArrayList<String>();
			ArrayList<String> AS_ListQty = new ArrayList<String>();
			
			while(li.hasNext()){
				Listitem list = (Listitem) li.next();	
				
				AS_ListItemCode.add( (String)list.getAttribute("itemcode") );			
				AS_ListQty.add( ((Intbox)((Listcell) list.getChildren().get(3)).getChildren().get(0)).getText() );
				
			}

			// Create SendSterile
			createSendSterile(AS_ListItemCode, AS_ListQty);
	
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			
		}
	}
	
	private void createSendSterile(ArrayList<String> AS_ListItemCode, ArrayList<String> AS_ListQty) throws Exception {
		
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
        ResultSet rs = null;
        
        try{
        	// -------------------------------------------------------  
        	// Create Send Sterile
        	// -------------------------------------------------------
        	String S_SSDocNo = null;
        	
	        
        	if(S_DocNo == null) {
        		S_SSDocNo = SendSterile.getSendSterileDocNo(S_DB, "", S_DeptId, S_UserId, S_TempStatus, "");
        	}else {
        		S_SSDocNo = S_DocNo;
        	}
        	
        	// -------------------------------------------------------  
        	// Create Item Stock
        	// -------------------------------------------------------
        	for(int i=0; i< AS_ListItemCode.size(); i++) {
        		addSendSterileDetail(S_SSDocNo, AS_ListItemCode.get(i), AS_ListQty.get(i));
        	} 
        	
        	if(S_DocNo == null) {
	        	
	        	S_DocNo = S_SSDocNo;
	        	
	        	onDisplayDocument(S_SSDocNo);
	        	
	        	onDisplayDetail(S_SSDocNo, false);
	        }else {
	        	onDisplayDetail(S_SSDocNo, false);
	        	
	        	updateLabelQty();
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
	        
	        Textbox_SearchItemStock.setText("");
	        
	        Textbox_SearchItemStock.focus();
	        
	        Listbox_Item.getItems().clear();
		}
	}
	
	
	private void addSendSterileDetail(String S_DocNo, String S_ItemCode, String S_QTY) throws Exception {
		
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
        
        
        try {
        	
        	// -----------------------------------------------------
    	    // Create Item Stock
    	    // -----------------------------------------------------
        	
		    String S_Sql = 
		    			"	INSERT INTO itemstock( "
		    		
		         	+ 	"	ItemCode, "
		         	+ 	"	DepID, "
		         	+ 	"	Qty, "
		         	+ 	"	IsStatus, "
		         	+ 	"	IsPay, "
	
					+ 	"	IsNew, "
					+ 	"	IsNewUsage, "
					+ 	"	IsDispatch, "
					+ 	"	UsageCode, "
					+ 	"	PackingMatID, "
					
					+ 	"	CreateDate, "
					+ 	"	LastSendDeptDate, "
					+ 	"	B_ID, "
					+ 	"	IsCancel "
					
					+ 	"	) VALUES ";
		     
		     int I_Max = Integer.valueOf(S_QTY).intValue();
	
		     String S_Sql_ = "";
	
		     for (int i = 0; i < I_Max; i++) { 
	
		        S_Sql_ += 
		            "( "
		            + 	"	'" + S_ItemCode + "', "
		            + 	"	" + S_DeptId + ", "
		            + 	"	1, "
		            + 	"	" + S_TempStatus + ", "
		            + 	"	0, "
	
					+ 	"	1, "
					+ 	"	1, "
					+ 	"	0, "
					+ 	"	( "
					+ 	"		SELECT      CONCAT('" + S_ItemCode + "', '-', DATE_FORMAT(NOW(),'%y'), HEX(MONTH(NOW())), '-', LPAD( (COALESCE(MAX(CONVERT(SUBSTRING(COALESCE(its.UsageCode, '00000'), 12, 16),UNSIGNED INTEGER)),0) + 1) , 5, 0)) AS UsageCode "
					+ 	"		FROM        itemstock AS its " 
					+ 	"		WHERE       its.ItemCode = '" + S_ItemCode + "' "
					+ 	"		AND         DATE_FORMAT(CreateDate,'%y') = DATE_FORMAT(NOW(),'%y') "
					+ 	"		ORDER BY    its.UsageCode DESC "
					+ 	"		LIMIT       1 "
					+ 	"	), "
					+ 	"	( "
					+ 	"		SELECT      item.PackingMatID "
					+ 	"		FROM        item "
					+ 	"		WHERE       item.itemcode = '" + S_ItemCode + "' "
					+ 	"		LIMIT       1 "
					+ 	"	), "
					
					+ 	"	NOW(), "
					+ 	"	NOW(), "
					+ 	"	1, "
					+ 	"	0 "
					+ 	"	),";
	
	
		        if( i == (I_Max-1) || (i>0 && i%9 == 0) ){
		            
		        	System.out.println(S_Sql + S_Sql_.substring(0, S_Sql_.length() - 1));
		        	
		        	stmt.executeUpdate(S_Sql + S_Sql_.substring(0, S_Sql_.length() - 1));
			        
			        conn.commit();

		            S_Sql_ = "";
		        }
		        
		        
		        
		        // -----------------------------------------------------
			    // Update Item Stock
			    // -----------------------------------------------------
		        /*
		        String S_Sql_Update = 
		        		
		        		"		UPDATE 		itemstock "  

                    	+	"	INNER JOIN 	sendsteriledetail "  
                      	+	"	ON 			sendsteriledetail.ItemStockID = itemstock.RowID "  
                                    
                     	+	"	SET 		itemstock.IsStatus = 0 " 
                                    
                   		+	"	WHERE 		sendsteriledetail.SendSterileDocNo = '" + S_DocNo + "' "  
                     	+	"	AND         itemstock.IsNew = 1 " 
                       	+	"	AND         itemstock.IsNewUsage = 1 " 
                     	+	"	AND         itemstock.IsStatus = " + S_TempStatus + " "  
                      	+	"	AND         itemstock.IsPay = 0 "     
                     	+	"	AND         itemstock.IsDispatch = 0 ";
                */
		        
		        
				//System.out.println(S_Sql_Update);  
				//stmt.executeUpdate(S_Sql_Update);  

		        
		     }
		     
		     // -----------------------------------------------------
		     // Create Send Sterile
		     // -----------------------------------------------------
		     String S_Sql_Insert =  
		        		
		        		"	INSERT INTO sendsteriledetail ( " 
                    +	"	sendsteriledetail.SendSterileDocNo, " 
                    +	"	sendsteriledetail.ItemStockID, " 
                    +	"	sendsteriledetail.Qty, " 
                    +	"	sendsteriledetail.Remark, " 
                    +	"	sendsteriledetail.UsageCode, " 
                    +	"	sendsteriledetail.IsSterile, " 
                    +	"	sendsteriledetail.IsStatus, " 
                    +	"	sendsteriledetail.B_ID " 
                    +	"	) " 
                    +	"	SELECT " 
                    +	"				'" + S_DocNo + "', " 
                    +	"				itemstock.RowID, " 
                    +	"				1, " 
                    +	"				'', " 
                    +	"				itemstock.UsageCode, " 
                    +	"				0, " 
                    +	"				0, " 
                    +	"				1  " 

					+	"	FROM        itemstock " 
						
					+	"	WHERE       itemstock.ItemCode = '" + S_ItemCode + "'  " 
					+	"	AND         itemstock.IsNew = 1 " 
					+	"	AND         itemstock.IsNewUsage = 1 " 
					+	"	AND         itemstock.IsStatus = " + S_TempStatus + "  " 
					+	"	AND         itemstock.IsPay = 0  "    
					+	"	AND         itemstock.IsDispatch = 0  " 
					+	"	ORDER BY    itemstock.RowID ASC " 
						
					+	"	LIMIT       " + S_QTY ;
		     
		    System.out.println(S_Sql_Insert);
		    
			stmt.executeUpdate(S_Sql_Insert);
				
			conn.commit();
				
        }catch(Exception e){
			e.printStackTrace();
		}finally{
		        
		 	if (stmt != null) {
		    	stmt.close();
		 	}
		        
		 	if (conn != null) {
		 		conn.close();
		 	}
        }
	}
		
	
	private void updateLabelQty(){
		
		// Update List Cell QTY
		
    	try {
    		
    		if(Listbox_Document.getSelectedCount() == 0 && Listbox_Document.getItemCount() > 0) {
    			Listbox_Document.setSelectedIndex(0);	
    			((Listcell) Listbox_Document.getSelectedItem().getChildren().get(3)).setLabel( Integer.toString( Listbox_DocumentDetail.getItemCount() ) );
    		}
    		
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
	}
	
	private String getSqlSendSterile(String S_SSDocNo) {
		
		final String S_Text = Textbox_Search.getText();
		
		String S_Sql = "";
		
		S_Sql =
			"	SELECT 	sendsterile.DocNo, "
		+	"			DATE_FORMAT(sendsterile.DocDate, '%d-%m-%Y') AS DocDate, " 
		+	"			COALESCE(sendsterile.Remark, '') AS Descriptions, "
		+	"			sendsterile.`IsStatus` AS IsStatus, " 
		
		+	"			(SELECT COUNT(*) FROM sendsteriledetail WHERE sendsteriledetail.SendSterileDocNo = sendsterile.DocNo) AS Count_Detail " 
								
		+	"	FROM 	sendsterile "	
	
		+	"	WHERE 	sendsterile.IsCancel = 0 "
		+ 	"	AND		sendsterile.IsWeb = 1 "
		+ 	"	AND		sendsterile.DeptID = " + S_DeptId + " ";
	
		if(S_SSDocNo != null) {
			S_Sql +=
				"	AND  	sendsterile.DocNo = '" + S_SSDocNo + "' ";
		}else {
		
			if (!S_Text.equals("")) {		
				S_Sql +=
				"	AND  	(sendsterile.DocNo LIKE '%" + S_Text + "%') ";
			}
			
			try {
				switch (Combobox_Status.getSelectedIndex()) {
				case 1: 
					S_Sql +=
					"	AND  	sendsterile.IsStatus = " + S_TempStatus + " ";
					break;
					
				case 2:
					S_Sql +=
					"	AND  	sendsterile.IsStatus = 0 ";
					break;
					
				default : 
						//S_Sql += "	AND		(sendsterile.IsStatus = " + S_TempStatus + " OR sendsterile.IsStatus = 0) ";
						
				}
			}catch(Exception e) {
				//S_Sql += "	AND		(sendsterile.IsStatus = " + S_TempStatus + " OR sendsterile.IsStatus = 0) ";
			}
			
			if (!Datebox_SDocDate.getText().trim().equals("") && !Datebox_EDocDate.getText().trim().equals("")) {		
				S_Sql +=
				"	AND  	( DATE(sendsterile.DocDate) BETWEEN DATE('" + DateTime.convertDate(Datebox_SDocDate.getText()) + "') AND DATE('" + DateTime.convertDate(Datebox_EDocDate.getText()) + "') ) ";
			}
			
			S_Sql += 
				"	ORDER BY sendsterile.`IsStatus`, DATE(sendsterile.DocDate) DESC, sendsterile.DocNo DESC LIMIT 100 ";
	
		}

		System.out.println(S_Sql);
	
		return S_Sql;
	
	}

	public void onDisplayDocument(String S_SSDocNo) throws Exception{
		
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;
        
		try{	
			
			rs = stmt.executeQuery(getSqlSendSterile(S_SSDocNo));
				
			int I_No = 1;
			
			Listbox_Document.getItems().clear();
			Listbox_DocumentDetail.getItems().clear();
			
			while(rs.next()){
				Listitem list = new Listitem();

				list.appendChild(new Listcell(I_No + "."));
				list.appendChild(new Listcell(rs.getString("DocDate")));
				list.appendChild(new Listcell(rs.getString("DocNo")));
				list.appendChild(new Listcell(rs.getString("Count_Detail")));
				list.appendChild(new Listcell(rs.getString("Descriptions")));	
				list.appendChild(newListcell(rs.getString("DocNo"), rs.getString("IsStatus")));
				
				//Attribute
                list.setAttribute("DocNo", rs.getString("DocNo"));
                list.setTooltiptext(rs.getString("IsStatus"));
                
                Listbox_Document.appendChild(list);
      
                I_No++;

			}
			
			if(I_No == 1) {
				 Messagebox.show("ไม่พบรายการ !!", "CSSD", Messagebox.OK, Messagebox.EXCLAMATION);
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
            
            S_DocNo = null;
		}
    }
	
	private Listcell newListcell(final String S_DocNo, final String IsStatus){
		Listcell lc = new Listcell();
		
		if(IsStatus.equals(S_TempStatus)) {
			
			Button btn = new Button("บันทึก");
			btn.setSclass("btn-success");
			btn.setHeight("25px");
			btn.setWidth("99%");
			btn.setStyle("color:#ffffff;background: #0275d8;border-radius: 4px;");
			
			btn.addEventListener("onClick", new EventListener<Event>() {
				public void onEvent(Event event) throws Exception {
					
					if(Listbox_DocumentDetail.getItemCount() == 0)
						onDisplayDetail(S_DocNo, false);
					
					Messagebox.show("ยืนยันส่งล้าง ?" , "CSSD", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,new EventListener<Event>() {
			    		public void onEvent(Event evt) throws Exception {
			    			switch (((Integer) evt.getData()).intValue()) {
			                  	case Messagebox.YES:
			                  		onConfirmToSendSterile(S_DocNo);
			                  		break;
			                  	case Messagebox.NO:
			                  		break;
			    			}
			    		}
			    	});
		        }
		    });
			
			lc.appendChild(btn);
		}else {
			lc.setStyle("color:#3979dd;");
			lc.setLabel("ส่งล้าง");
		}
		
		return lc;
	}
	
	private void onConfirmToSendSterile(final String S_DocNo) throws Exception{

		try{
			// Update ItemStock & Send Sterile
			updatesendsterile(S_DocNo);
            
			
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			
		}
    }
	
	private void updatesendsterile(String S_DocNo) throws Exception {
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
        
        try{
        	
        	// Update Send Sterile Detail
			String S_SqlDetail = 
					
					"UPDATE		sendsteriledetail "
				+ 	"SET		IsStatus = 0 "
				+ 	"WHERE		SendSterileDocNo = '" + S_DocNo + "' ";
	
			String S_Sql = 
					
					"UPDATE		sendsterile "
				+ 	"SET		IsStatus = 0, "
				+ 	"			ModifyDate = NOW(), "
				+ 	"			Remark = 'แผนกส่งล้าง' "
				+ 	"WHERE		DocNo = '" + S_DocNo + "' ";
			
			// Update Item Stock
			String S_SqlUpdateItemStock = 
					
					"UPDATE		itemstock "
					
				+ 	"INNER JOIN	sendsteriledetail "
				+ 	"ON 		itemstock.RowID = sendsteriledetail.ItemStockID "
	                
				+ 	"SET		itemstock.IsStatus = 0, " 
				+ 	"        	itemstock.IsNewUsage = 0, " 
				+ 	"        	itemstock.IsNew = 0, " 
				+ 	"         	itemstock.IsPay = 0 , " 
				+ 	"        	itemstock.IsDispatch = 0, " 
				+ 	"        	itemstock.LastReceiveDeptDate = NOW(), "  						
				+ 	"			itemstock.LastSendDeptDate = NOW() "
				
				+ 	"WHERE 		sendsteriledetail.SendSterileDocNo = '" + S_DocNo + "' "
				
				+ 	"AND 		itemstock.IsStatus = " + S_TempStatus + " " ;
			
			
			
			System.out.println(S_SqlDetail);
			System.out.println(S_Sql);
			System.out.println(S_SqlUpdateItemStock);
			
			stmt.executeUpdate(S_SqlUpdateItemStock);			
			stmt.executeUpdate(S_SqlDetail);
	        stmt.executeUpdate(S_Sql);
	        
	        conn.commit();
	        
	        Messagebox.show("บันทึกสำเร็จ !!", "CSSD", Messagebox.OK, Messagebox.INFORMATION);
   
	        
		}catch(Exception e){
			e.printStackTrace();
		}finally{
	        
	        if (stmt != null) {
	            stmt.close();
	        }
	        
	        if (conn != null) {
	            conn.close();
	        }
	        
	        this.S_DocNo = null;
	    	Listbox_Document.getItems().clear();	
	    	Listbox_DocumentDetail.getItems().clear();
	    	
	    	Textbox_SearchItemStock.setText("");
	    	Textbox_SearchItemStock.focus();

		}
	}
		

	public void onDisplayDetail(String S_DocNo, boolean IsAlert) throws Exception{
		
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;
        
		try{	
			
			this.S_DocNo = S_DocNo;
			
			rs = stmt.executeQuery(getSqlSendSterileDetail(S_DocNo));
				
			int I_No = 1;
			
			Listbox_DocumentDetail.getItems().clear();
			
			while(rs.next()){
				Listitem list = new Listitem();
				
				final String S_ID = rs.getString("ID");
				final String S_ItemStockID = rs.getString("ItemStockID");
				
				Listcell lc_del = new Listcell("", "/images/ic_delete.png");
				lc_del.addEventListener("onClick", new EventListener<Event>() {
					public void onEvent(Event event) throws Exception {
						
						Messagebox.show("ยืนยันการลบรายการ ?" , "CSSD", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,new EventListener<Event>() {
				    		public void onEvent(Event evt) throws Exception {
				    			switch (((Integer) evt.getData()).intValue()) {
				                  	case Messagebox.YES:
				                  		onUpdateSterileDetail(S_ID, S_ItemStockID);
				                  		break;
				                  	case Messagebox.NO:
				                  		break;
				    			}
				    		}
				    	});
			        }
			    });

				list.appendChild(new Listcell(I_No + "."));
				list.appendChild(new Listcell(rs.getString("UsageCode")));				
				list.appendChild(new Listcell(rs.getString("itemname")));
				list.appendChild(new Listcell(rs.getString("PackDate")));				
				list.appendChild(new Listcell(rs.getString("ExpireDate")));
				list.appendChild(lc_del);
				
				//Attribute
                list.setAttribute("ID", S_ID);
                list.setAttribute("ItemStockID", rs.getString("ItemStockID"));
                
   
                Listbox_DocumentDetail.appendChild(list);
      
                I_No++;

			}
			
			if(IsAlert && I_No == 1) {
				 Messagebox.show("ไม่พบรายการ !!", "CSSD", Messagebox.OK, Messagebox.EXCLAMATION);
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

	
	private String getSqlSendSterileDetail(String S_DocNo) {
			
		String S_Sql = "";
		
		S_Sql =
			"	SELECT 		item.itemcode, " 
		+	"				item.itemname, "
		+ 	"				sendsteriledetail.ID AS ID, "
		+	"				sendsteriledetail.ItemStockID, "
		+	"				sendsteriledetail.UsageCode, "
		+	"				COALESCE(DATE_FORMAT(itemstock.PackDate, '%d-%m-%Y'),'-') AS PackDate, "
		+	"				COALESCE(DATE_FORMAT(itemstock.ExpireDate, '%d-%m-%Y'),'-') AS ExpireDate, "
		+	"				sendsteriledetail.IsStatus  "
		
		+	"	FROM 		sendsteriledetail "
		
		+	"	INNER JOIN 	itemstock  "
		+	"	ON			sendsteriledetail.ItemStockID = itemstock.RowId  "
		
		+	"	INNER JOIN 	item  "
		+	"	ON			item.itemcode = itemstock.ItemCode  "
					
		+	"	WHERE  		sendsteriledetail.SendSterileDocNo = '" + S_DocNo + "' "
		+ 	"	AND			itemstock.DepID = " + S_DeptId + " "
		+	"	AND  		itemstock.IsStatus = " + S_TempStatus + " "
		+	"	AND  		itemstock.IsCancel = 0 "
		+	"	AND  		itemstock.IsPay = 0 "
		+	"	AND  		itemstock.IsNew = 1 "
		
		+	"	ORDER BY 	item.itemcode "
		
		+ 	"	LIMIT 5000 ";
		
		System.out.println(S_Sql);
	
		return S_Sql;
	
	}
	
	private void onUpdateSterileDetail(String S_ID, String S_ItemStockID) throws Exception {
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
        ResultSet rs = null;
        
        try{
        	 
        	// Delete Send Sterile Detail
        	String S_SqlDeleteSendSterileDetail = 
        						
        			"DELETE	"
        		+ 	"FROM 	sendsteriledetail "
        		+ 	"WHERE 	ID = " + S_ID ;
        				
        	// Update Item Stock
			String S_SqlUpdateItemStock = 
					
					"UPDATE		itemstock "
				+ 	"SET		itemstock itemstock.IsStatus = ( CASE WHEN IsNew = 0 THEN 5 ELSE 10 END), " 
				+ 	" 			itemstock itemstock.IsCancel = ( CASE WHEN IsNew = 0 THEN 0 ELSE 1 END) "
				
				+ 	"WHERE 		RowID = " + S_ItemStockID + " "
				
				+ 	"AND 		IsStatus = " + S_TempStatus + " " ;
			
			
			System.out.println(S_SqlDeleteSendSterileDetail);
			System.out.println(S_SqlUpdateItemStock);
			
			stmt.executeUpdate(S_SqlDeleteSendSterileDetail);
	        stmt.executeUpdate(S_SqlUpdateItemStock);
	        
	        conn.commit();
  
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
	        
	        onDisplayDetail(S_DocNo, false);
	        
	        updateLabelQty();
	        
	        Listbox_Item.getItems().clear();
	        
	        Textbox_SearchItemStock.setText("");
	        Textbox_SearchItemStock.focus();
		}
	}
	
	// ================================================================================
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
			
			rs = stmt.executeQuery(
						"SELECT 	SS_IsFindStatus, "
					+ 	"			SS_IsFindItemDepartment "
					+ 	"FROM 		configuration_web "
					+ 	"LIMIT 		1 "
			);

			if(rs.next()){
				SS_IsFindStatus = rs.getInt("SS_IsFindStatus");	
				SS_IsFindItemDepartment = rs.getBoolean("SS_IsFindItemDepartment");
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


