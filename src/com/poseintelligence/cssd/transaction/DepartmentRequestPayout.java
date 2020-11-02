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

import com.poseintelligence.cssd.document.Payout;
import com.poseintelligence.cssd.document.SendSterile;
import com.poseintelligence.cssd.model.ModelMaster;
import com.poseintelligence.cssd.utillity.DateTime;

@SuppressWarnings("rawtypes")
public class DepartmentRequestPayout extends GenericForwardComposer{

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
	private Checkbox Checkbox_Catagory;
	
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
		
		onDisplayItem();
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
	
	public void  onCheck$Checkbox_Catagory(Event event) throws Exception {
		try {
			boolean C_Chk = !Checkbox_Catagory.isChecked();
			
			if(C_Chk == true) {
				onDisplayItem();
			}else {
				onDisplayItemCatagory();
				//System.out.print("Check Log Checkbox : " + C_Chk);
			}
			
			System.out.print("Check Catagory : " + C_Chk);
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
		+	"	ON			units.ID = `item`.UnitID  "
		
		+	"	INNER JOIN 	itemstock "
		+ 	"	ON 			item.itemcode = itemstock.ItemCode "
		
		+	"	WHERE  	 	NOT ( NoWash = '1' AND NoWashType = '2' ) AND itemstock.DepID = '" + S_DeptId + "'";
		
		
		String S_Text = Textbox_SearchItemStock.getText();
		
		if(!S_Text.trim().equals("")) {
			S_Sql +=	
				"		AND	("
			+ 	"				item.itemcode 		LIKE '%" + S_Text + "%' "
			+ 	"				OR item.itemname 	LIKE '%" + S_Text + "%' "
			+ 	"			) ";
		}
	
		S_Sql +=
			"	GROUP BY 	item.itemcode "
		+	"	ORDER BY 	item.itemcode "	
		
		+	"	LIMIT 100 ";
		
		System.out.println(S_Sql);
		
		return S_Sql;
	}
	
	private String getSqlItemCatagory() {
		String S_Sql = "";
		S_Sql = 	
				"	SELECT		item.itemcode, "
			+	"				item.itemname, "
			+ 	"				COALESCE(units.UnitName, '-') AS UnitName "
			
			+	"	FROM		item "
			
			+	"	LEFT JOIN 	units  "
			+	"	ON			units.ID = `item`.UnitID  "
			
			+	"	INNER JOIN 	itemstock "
			+ 	"	ON 			item.itemcode = itemstock.ItemCode "
			
			+	"	WHERE 		NoWash = '1' AND NoWashType = '2' AND itemstock.DepID = '" + S_DeptId + "' ";
			
			String S_Text = Textbox_SearchItemStock.getText();
			
			if(!S_Text.trim().equals("")) {
				S_Sql +=	
					"	AND	("
				+ 	"				item.itemcode 		LIKE '%" + S_Text + "%' "
				+ 	"				OR item.itemname 	LIKE '%" + S_Text + "%' "
				+ 	"			) ";
				
			}
		
			S_Sql +=
				"	GROUP BY 	item.itemcode "
			+	"	ORDER BY 	item.itemcode "	
			+	"	LIMIT 100 ";
			
			System.out.println(S_Sql);
			
			return S_Sql;
		
	}
	
	
	public void onDisplayItemCatagory() throws Exception{
		
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;
        
		try{	
			
			rs = stmt.executeQuery(getSqlItemCatagory());
			
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
	
	public void onConfirmSend() throws Exception{
				
		if(Listbox_Item.getSelectedCount() == 0) {
			Messagebox.show("ไม่ได้เลือกรายการ !!", "CSSD", Messagebox.OK, Messagebox.INFORMATION);
			return;
		}
						
		Messagebox.show("ยืนยันขอเบิกรายการ ?" , "CSSD", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,new EventListener<Event>() {
    		public void onEvent(Event evt) throws Exception {
    			switch (((Integer) evt.getData()).intValue()) {
                  	case Messagebox.YES:
                  		onRequest();
                  		break;
                  	case Messagebox.NO:
                  		break;
    			}
    		}
    	});
				
	}
	
	public void onRequest() throws Exception{
		
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
			crearePayout(AS_ListItemCode, AS_ListQty);
	
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			
		}
	}
	
	private void crearePayout(ArrayList<String> AS_ListItemCode, ArrayList<String> AS_ListQty) throws Exception {
		
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
        ResultSet rs = null;
        
        try{
        	// -------------------------------------------------------  
        	// Create Payout
        	// -------------------------------------------------------
        	String S_PADocNo = null;
        	
	        
        	if(S_DocNo == null) {
        		S_PADocNo = Payout.getPayoutDocNo(S_DB, "", S_DeptId, S_UserId, S_TempStatus, "สร้างใบจ่ายขอเบิกจากแผนก");
        	}else {
        		S_PADocNo = S_DocNo;
        	}
        	
        	// -------------------------------------------------------  
        	// Create Payout Detail
        	// -------------------------------------------------------
        	
        	String S_Value = "";
        	
        	for(int i=0; i< AS_ListItemCode.size(); i++) {        		
        		S_Value += "('" + S_PADocNo + "', null, '" + AS_ListItemCode.get(i) + "', " + AS_ListQty.get(i) + ", '', NOW(), 1, " + S_TempStatus + "),";
        	} 
        	
        	if(!S_Value.equals("")) {
        		addPayoutDetail(S_Value.substring(0, S_Value.length() - 1));
        	}
        	
        	if(S_DocNo == null) {
	        	
	        	S_DocNo = S_PADocNo;
	        	
	        	onDisplayDocument(S_PADocNo);
	        	
	        	onDisplayDetail(S_PADocNo, false);
	        	
	        }else {
	        	onDisplayDetail(S_PADocNo, false);
	        	
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
	
	private void addPayoutDetail(String S_Value) throws Exception {
		
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
        
        
        try {
   
		     // -----------------------------------------------------
		     // Create Payout Detail
		     // -----------------------------------------------------
		     String S_Sql_Insert =  
		        		
		        		"	INSERT INTO payoutdetail ( " 
		    		 
		        	+	"	DocNo, " 
		        	+	"	ItemStockID, " 
					+	"	ItemCode, " 
					+	"	Qty, " 
					+	"	Remark, " 
					+	"	PayDate, " 
					+	"	IsCheckPay, " 
					+	"	IsStatus " 
					
					+	"	) VALUES ";
		     
		    System.out.println(S_Sql_Insert + S_Value);
		    
			stmt.executeUpdate(S_Sql_Insert + S_Value);
				
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
	
	private String getSqlPayout(String S_PADocNo) {
		
		final String S_Text = Textbox_Search.getText();
		
		String S_Sql = "";
		
		S_Sql =
			"	SELECT 	payout.DocNo, "
		+	"			DATE_FORMAT(payout.CreateDate, '%d-%m-%Y') AS DocDate, " 
		+	"			COALESCE(payout.Remark, '') AS Descriptions, "
		+	"			payout.`IsStatus` AS IsStatus, " 
		
		+	"			(SELECT COUNT(*) FROM payoutdetail WHERE payoutdetail.DocNo = payout.DocNo) AS Count_Detail " 
								
		+	"	FROM 	payout "	
	
		+	"	WHERE 	payout.IsCancel = 0 "
		+ 	"	AND		payout.IsWeb = 1 "
		+ 	"	AND		payout.DeptID = " + S_DeptId + " ";
	
		if(S_PADocNo != null) {
			S_Sql +=
				"	AND  	payout.DocNo = '" + S_PADocNo + "' ";
		}else {
		
			if (!S_Text.equals("")) {		
				S_Sql +=
				"	AND  	(payout.DocNo LIKE '%" + S_Text + "%') ";
			}
			
			try {
				switch (Combobox_Status.getSelectedIndex()) {
				case 1: 
					S_Sql +=
					"	AND  	payout.IsStatus = " + S_TempStatus + " ";
					break;
					
				case 2:
					S_Sql +=
					"	AND  	payout.IsStatus = 0 ";
					break;
					
				default : 
						S_Sql += "	AND		(payout.IsStatus = " + S_TempStatus + " OR payout.IsStatus = 0) ";
						
				}
			}catch(Exception e) {
				S_Sql += "	AND		(payout.IsStatus = " + S_TempStatus + " OR payout.IsStatus = 0) ";
			}
			
			if (!Datebox_SDocDate.getText().trim().equals("") && !Datebox_EDocDate.getText().trim().equals("")) {		
				S_Sql +=
				"	AND  	( DATE(payout.CreateDate) BETWEEN DATE('" + DateTime.convertDate(Datebox_SDocDate.getText()) + "') AND DATE('" + DateTime.convertDate(Datebox_EDocDate.getText()) + "') ) ";
			}
			
			S_Sql += 
				"	ORDER BY payout.`IsStatus`, DATE(payout.CreateDate) DESC, payout.DocNo DESC LIMIT 100 ";
	
		}

		System.out.println(S_Sql);
	
		return S_Sql;
	
	}

	public void onDisplayDocument(String S_PADocNo) throws Exception{
		
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;
        
		try{	
			
			rs = stmt.executeQuery(getSqlPayout(S_PADocNo));
				
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
				list.appendChild(newListcell(true, rs.getString("DocNo"), rs.getString("IsStatus")));
				list.appendChild(newListcell(false, rs.getString("DocNo"), rs.getString("IsStatus")));
				
				//Attribute
                list.setAttribute("DocNo", rs.getString("DocNo"));
                
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
	
	private Listcell newListcell(final boolean IsSave, final String S_DocNo, final String IsStatus){
		Listcell lc = new Listcell();

		if(IsStatus.equals("0")) {
			lc.setStyle("color:#3979dd;");
			lc.setLabel(IsSave ? "" : "ขอเบิก");
		}else {
			
			Button btn = new Button(IsSave ? "บันทึก" : "ลบ");
			
			btn.setSclass("btn-success");
			btn.setHeight("25px");
			btn.setWidth("99%");
			btn.setStyle("background: " + (IsSave ? "#0275d8;" : "#d9534f;") + "color:#ffffff;border-radius: 4px;"); 
			
			btn.addEventListener("onClick", new EventListener<Event>() {
				public void onEvent(Event event) throws Exception {
					
					if(IsSave) {
						if(Listbox_DocumentDetail.getItemCount() == 0)
							onDisplayDetail(S_DocNo, false);
						
						Messagebox.show("ยืนยันขอเบิก ?" , "CSSD", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,new EventListener<Event>() {
				    		public void onEvent(Event evt) throws Exception {
				    			switch (((Integer) evt.getData()).intValue()) {
				                  	case Messagebox.YES:
				                  		onConfirmToPayout(S_DocNo);
				                  		break;
				                  	case Messagebox.NO:
				                  		break;
				    			}
				    		}
				    	});
					}else {
						Messagebox.show("ยืนยันลบเอกสารขอเบิก ?" , "CSSD", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,new EventListener<Event>() {
				    		public void onEvent(Event evt) throws Exception {
				    			switch (((Integer) evt.getData()).intValue()) {
				                  	case Messagebox.YES:
				                  		onDeletePayout(S_DocNo);
				                  		break;
				                  	case Messagebox.NO:
				                  		break;
				    			}
				    		}
				    	});
					}
		        }
		    });
			
			lc.appendChild(btn);
		}
		
		return lc;
	}
	
	private void onConfirmToPayout(final String S_DocNo) throws Exception{

		try{
			// Update payout
			updatepayout(S_DocNo);
            			
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			
		}
    }
	
	private void updatepayout(String S_DocNo) throws Exception {
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
        
        try{
        	
        	// Update Detail
			String S_SqlDetail = 
					
					"UPDATE		payoutdetail "
				+ 	"SET		IsStatus = 0 "
				+ 	"WHERE		DocNo = '" + S_DocNo + "' ";
	
			String S_Sql = 
					
					"UPDATE		payout "
				+ 	"SET		IsStatus = 0, "
				+ 	"			ModifyDate = NOW(), "
				+ 	"			Remark = 'แผนกขอเบิก' "
				+ 	"WHERE		DocNo = '" + S_DocNo + "' ";
	
			System.out.println(S_SqlDetail);
			System.out.println(S_Sql);
					
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
			
			rs = stmt.executeQuery(getSqlPayoutDetail(S_DocNo));
				
			int I_No = 1;
			
			Listbox_DocumentDetail.getItems().clear();
			
			while(rs.next()){
				Listitem list = new Listitem();
				
				final String S_ID = rs.getString("ID");

				list.appendChild(new Listcell(I_No + "."));
				list.appendChild(new Listcell(rs.getString("itemcode")));				
				list.appendChild(new Listcell(rs.getString("itemname")));
				list.appendChild(new Listcell(rs.getString("Qty")));
				list.appendChild(new Listcell(rs.getString("UnitName")));
				
				if(rs.getString("IsStatus").equals(S_TempStatus)) {
					
					Listcell lc_del = new Listcell("", "/images/ic_delete.png");
					lc_del.addEventListener("onClick", new EventListener<Event>() {
						public void onEvent(Event event) throws Exception {
							
							Messagebox.show("ยืนยันการลบรายการ ?" , "CSSD", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,new EventListener<Event>() {
					    		public void onEvent(Event evt) throws Exception {
					    			switch (((Integer) evt.getData()).intValue()) {
					                  	case Messagebox.YES:
					                  		onUpdatePayoutDetail(S_ID);
					                  		break;
					                  	case Messagebox.NO:
					                  		break;
					    			}
					    		}
					    	});
				        }
				    });
					
					list.appendChild(lc_del);
					
				}else {
					list.appendChild(new Listcell(""));
				}
				
				//Attribute
                list.setAttribute("ID", S_ID);
                
   
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

	
	private String getSqlPayoutDetail(String S_DocNo) {
			
		String S_Sql = "";
		
		S_Sql =
			"	SELECT 		item.itemcode, " 
		+	"				item.itemname, "
		+ 	"				payoutdetail.ID AS ID, "
		+	"				payoutdetail.Qty, "
		+	"				COALESCE(units.UnitName, '-') AS UnitName, "
		+ 	"				payoutdetail.IsStatus " 
		
		+	"	FROM 		payoutdetail "
		
		+	"	INNER JOIN 	item  "
		+	"	ON			item.itemcode = payoutdetail.ItemCode  "
		
		+	"	LEFT JOIN 	units  "
		+	"	ON			units.ID = `item`.UnitID  "
					
		+	"	WHERE  		payoutdetail.DocNo = '" + S_DocNo + "' "

		//+	"	AND  		payoutdetail.IsStatus = " + S_TempStatus + " "
		
		+	"	ORDER BY 	item.itemcode "
		
		+ 	"	LIMIT 5000 ";
		
		System.out.println(S_Sql);
	
		return S_Sql;
	
	}
	
	private void onUpdatePayoutDetail(String S_ID) throws Exception {
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
        ResultSet rs = null;
        
        try{
        	 
        	// Delete Detail
        	String S_SqlDelete = 
        						
        			"DELETE	"
        		+ 	"FROM 	payoutdetail "
        		+ 	"WHERE 	ID = " + S_ID ;
  	
			System.out.println(S_SqlDelete);
			
			stmt.executeUpdate(S_SqlDelete);
	        
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
	
	private void onDeletePayout(String S_PADocNo) throws Exception {
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
        ResultSet rs = null;
        
        try{
        	 
        	// Delete Detail
        	String S_SqlDelete = 
        						
        			"DELETE	"
        		+ 	"FROM 	payoutdetail "
        		+ 	"WHERE 	DocNo = '" + S_PADocNo + "' " ;
        	
        	// Update Payout
        	String S_SqlUpdate = 
        						
        			"UPDATE 	payout "
        		+ 	"SET		IsStatus = -1, "
        		+ 	"			IsCancel = 1, "
        		+ 	"			ModifyDate = NOW(), "
        		+ 	"			Remark = 'แผนกยกเลิกใบขอเบิก' "
        		+ 	"WHERE 		DocNo = '" + S_PADocNo + "' " ;
  	
			System.out.println(S_SqlDelete);	
			System.out.println(S_SqlUpdate);
			
			stmt.executeUpdate(S_SqlDelete);
			stmt.executeUpdate(S_SqlUpdate);
	        
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
	        
	        onDisplayDocument(null);
	        
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


