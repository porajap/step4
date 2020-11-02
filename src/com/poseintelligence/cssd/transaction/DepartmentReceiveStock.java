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
import org.zkoss.zul.Caption;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import com.poseintelligence.cssd.document.Occurrence;
import com.poseintelligence.cssd.document.SendSterile;
import com.poseintelligence.cssd.model.ModelMaster;
import com.poseintelligence.cssd.utillity.DateTime;

@SuppressWarnings("rawtypes")
public class DepartmentReceiveStock extends GenericForwardComposer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3525844790119368775L;
	
	private boolean B_IsCreate = true;
	private String 	S_DocNo = null;
	
	// Variable Configuration
	private boolean B_RC_IsUsedReceiveByBarCode = false;
	
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
	private Listbox Listbox_Document;
	private Listbox Listbox_Item;
		
	private Textbox Textbox_Search;
	private Datebox Datebox_SDocDate;
	private Datebox Datebox_EDocDate;
	
	private Combobox Combobox_Status;
	
	private Window Window_PopUp;
	
	private Textbox Textbox_Input;
	
	private Textbox Textbox_Recipient;
	private Textbox Textbox_Approve;
	private Textbox Textbox_Datepay;
	private Textbox Textbox_Timepay;
	
	private Image Image_Input;
	private Window WinProcess;
	
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
		Datebox_SDocDate.setText(DateTime.getDateLastMonth());
		Datebox_EDocDate.setText(DateTime.getDateNow());
		
		// Display Pay
		onDisplayDocument();
		
		// Drop-down
		defineOccurrenceType();
		
		// Configuration
		onDisplayWebConfig();
		
		
	}

	// Event
	public void onSelect$Listbox_Document(Event event) throws Exception {
		
		S_DocNo = (String)Listbox_Document.getSelectedItem().getAttribute("DocNo");
		
		// Display Pay Detail
		if(S_DocNo != null && (!S_DocNo.equals("")))
			onDisplayDetail(S_DocNo);
	}
	
	public void onClick$Listbox_Document(Event event) throws Exception {
		if(B_RC_IsUsedReceiveByBarCode) {
	    	Textbox_Input.focus();
	    }
	}
	
	public void onClick$Listbox_Item(Event event) throws Exception {
		if(B_RC_IsUsedReceiveByBarCode) {
	    	Textbox_Input.focus();
	    }
	}
	
	public void onSelect$Combobox_Status(Event event) throws Exception {
		// Display Pay
		onDisplayDocument();
	}
	
	public void onOK$Textbox_Search(Event event) throws Exception {
		// Display Pay
		onDisplayDocument();
	}
	
	public void onClick$Button_SearchDoc(Event event) throws Exception {
		// Display Pay
		onDisplayDocument();
	}
	
	public void onChange$Datebox_SDocDate(Event event) throws Exception {
		// Display Pay
		onDisplayDocument();
	}
	
	public void onChange$Datebox_EDocDate(Event event) throws Exception {
		// Display Pay
		onDisplayDocument();
	}
	
	public void onOK$Textbox_Input(Event event) throws Exception {
		checkPayoutDetail(Textbox_Input.getText());
	}
	
	public void onClick$Button_Receive(Event event) throws Exception {
		
		if(S_DocNo == null){
			return;
		}
		
		if(Listbox_Item.getItemCount() == 0)
			onDisplayDetail(S_DocNo);
		
		Messagebox.show("ยืนยันการรับเข้า ?" , "CSSD", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,new EventListener<Event>() {
    		public void onEvent(Event evt) throws Exception {
    			switch (((Integer) evt.getData()).intValue()) {
                  	case Messagebox.YES:
                  		onConfirmDepartmentReceive(S_DocNo);
                  		break;
                  	case Messagebox.NO:
                  		break;
    			}
    		}
    	});
	}
	// ==================================================================================================
	
	public void onDisplayDocument() throws Exception{
		
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;
        
		try{	
			
			onProcess(true);
			
			rs = stmt.executeQuery(getSqlPayout());
				
			int I_No = 1;
			
			Listbox_Document.getItems().clear();
			Listbox_Item.getItems().clear();
			
			while(rs.next()){
				Listitem list = new Listitem();

				list.appendChild(new Listcell(I_No + "."));
				list.appendChild(new Listcell(rs.getString("CreateDate")));
				list.appendChild(new Listcell(rs.getString("DocNo")));
				list.appendChild(new Listcell(rs.getString("Count_Detail")));
				list.appendChild(new Listcell(rs.getString("Count_Detail_Sub")));
				list.appendChild(new Listcell(rs.getString("StatusPO")));	
				list.appendChild(newListcell(rs.getString("DocNo"), rs.getString("IsStatus")));
				
				//Attribute
                list.setAttribute("DocNo", rs.getString("DocNo"));
                //list.setAttribute("Recipient", rs.getString("Recipient"));
                
                Listbox_Document.appendChild(list);
      
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
            
            S_DocNo = null;
            
            onProcess(false);
            
		}
    }
	
	private Listcell newListcell(final String S_DocNo_, final String IsStatus){
		Listcell lc = new Listcell();
		
		Button btn = new Button("รับเข้าทั้งหมด");
		
		if(IsStatus.equals("3")) {
			lc.setStyle("color:#3979dd;");
			lc.setLabel("ปิดเอกสาร");
		}else {
	
			btn.setSclass("btn-success");
			btn.setHeight("25px");
			btn.setWidth("99%");
			btn.setImage("/images/ic_import.png");
			btn.setStyle("color:#ffffff;background: #1eac74;border-radius: 4px;");
			
			btn.addEventListener("onClick", new EventListener<Event>() {
				public void onEvent(Event event) throws Exception {
					
					//if(Listbox_Item.getItemCount() == 0)
					
					S_DocNo = S_DocNo_;
					
					onDisplayDetail(S_DocNo);
					
					Messagebox.show("ยืนยันการรับเข้า ?" , "CSSD", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,new EventListener<Event>() {
			    		public void onEvent(Event evt) throws Exception {
			    			switch (((Integer) evt.getData()).intValue()) {
			                  	case Messagebox.YES:
			                  		onConfirmDepartmentReceive(S_DocNo);
			                  		break;
			                  	case Messagebox.NO:
			                  		break;
			    			}
			    		}
			    	});
		        }
		    });
			
			lc.appendChild(btn);
		}
		
		return lc;
	}
	
	private void onConfirmDepartmentReceive(final String S_DocNo) throws Exception{

		try{
			
			// Start
			onProcess(true);
			
			Iterator<Listitem> li = Listbox_Item.getItems().iterator();
			
			boolean B_IsSelected = false;
			
			String S_ID = null;
				
			String S_Occurrence_ListID = "";		// Occurrence Pass
			String S_Occurrence_SS_ListID = "";		// Occurrence Not passed	
			String S_ListID = "";					// Normal
			
			String S_ListItemStockID = "";
			String S_Occurrence_ListItemStockID = "";
			String S_Occurrence_SS_ListItemStockID = "";
			
			while(li.hasNext()){
				Listitem list = (Listitem) li.next();	
				
				//System.out.println(list.isDisabled());
				
				if(list.isDisabled()) {
					continue;
				}
				
				S_ID = (String)((Listcell)list.getChildren().get(3)).getAttribute("ID");
				B_IsSelected = ((Checkbox)((Listcell)list.getChildren().get(4)).getChildren().get(0)).isChecked();
				
				// -----------------------------------------------------------
				// Receive Normal && Occurrence
				// Roll-back Send Sterile
				// -----------------------------------------------------------
				if(B_IsSelected) {
					if(S_ID != null && (!S_ID.equals("")) ) {
						S_Occurrence_ListID += 				(String)list.getAttribute("ID") + ",";
						S_Occurrence_ListItemStockID += 	(String)list.getAttribute("ItemStockID") + ",";
					}else{
						S_ListID += 						(String)list.getAttribute("ID") + ",";	
						S_ListItemStockID += 				(String)list.getAttribute("ItemStockID") + ",";
					}		
				}else {
					if(S_ID != null && (!S_ID.equals("")) ) {
						S_Occurrence_SS_ListID += 			(String)list.getAttribute("ID") + ",";
						S_Occurrence_SS_ListItemStockID += 	(String)list.getAttribute("ItemStockID") + ",";
					}else {
						// Non Selected
					}
				}
			}
			
			//System.out.println(S_ListID);
				
			// Check Select Item
			if(S_ListID.equals("") && S_Occurrence_ListID.equals("") && S_Occurrence_SS_ListID.equals("")) {
				return;
			}
			
			System.out.println( S_ListID + " - " + S_Occurrence_ListID + " - " + S_Occurrence_SS_ListID );
			
			// -----------------------------------------------------------
			// 1. Create Occurrence
			// -----------------------------------------------------------
						
			if(!S_Occurrence_ListID.equals("") || !S_Occurrence_SS_ListID.equals("")) {
				//Create Occurrence
				createOccurrence(S_DocNo);
			}
				
			// -----------------------------------------------------------
			// 2. ID Select Normal
			// -----------------------------------------------------------
			
			if(!S_ListID.equals("")) {
				S_ListID = S_ListID.substring(0, S_ListID.length() - 1);
				S_ListItemStockID = S_ListItemStockID.substring(0, S_ListItemStockID.length() - 1);

				// Update Receive Pay Detail Sub
				updateReceivePayout(S_DocNo, S_ListID, S_ListItemStockID, "รับเข้าแผนก");
			}
			
			// -----------------------------------------------------------
			// 3. ID Select Occurrence Pass
			// -----------------------------------------------------------
			
			if(!S_Occurrence_ListID.equals("")) {
				S_Occurrence_ListID = S_Occurrence_ListID.substring(0, S_Occurrence_ListID.length() - 1);
				S_Occurrence_ListItemStockID = S_Occurrence_ListItemStockID.substring(0, S_Occurrence_ListItemStockID.length() - 1);

				// Update Receive Pay Detail Sub
				updateReceivePayout(S_DocNo, S_Occurrence_ListID, S_Occurrence_ListItemStockID, "รับเข้าแผนก (ติดความเสี่ยง)");
			}
			
			// -----------------------------------------------------------
			// 4. ID Occurrence Not Passed
			// -----------------------------------------------------------
			
			if(!S_Occurrence_SS_ListID.equals("")) {
				S_Occurrence_SS_ListID = S_Occurrence_SS_ListID.substring(0, S_Occurrence_SS_ListID.length() - 1);
				S_Occurrence_SS_ListItemStockID = S_Occurrence_SS_ListItemStockID.substring(0, S_Occurrence_SS_ListItemStockID.length() - 1);
				
				// Update Not Receive Pay Detail Sub && Create Send Sterile
				notAcceptToSendSterile(S_DocNo, S_Occurrence_SS_ListID, S_Occurrence_SS_ListItemStockID);
			}
			
			
						
			
			// Update Pay
			updatePayout(S_DocNo);
            
			Listbox_Item.getItems().clear();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			onDisplayDocument();
			
			WinProcess.setVisible(false);
		}
    }
	
	private void updatePayout(String S_DocNo) throws Exception {
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
        
        try{
			String S_SqlDetail = 
					
					"UPDATE		payoutdetail "
				+ 	"SET		IsStatus = ( "
				+ 	"			CASE WHEN ( "
				+ 	"			( "
				+ 	"				SELECT 	COUNT(*) "
				+ 	"				FROM 	payoutdetailsub "
				+ 	"				WHERE 	payoutdetailsub.Payoutdetail_RowID = payoutdetail.ID "
				+ 	"				AND 	IsStatus = 1 "
				+ 	"			) = 0 "
				+ 	"			) THEN 3 ELSE 2 END "
				+ 	"			) "
				+ 	"WHERE		DocNo = '" + S_DocNo + "' ";
	
			String S_Sql = 
					
					"UPDATE		payout "
				+ 	"SET		IsStatus = ( "
				+ 	"			CASE WHEN ( "
				+ 	"			( "
				+ 	"				SELECT 	COUNT(*) "
				+ 	"				FROM 	payoutdetail "
				+ 	"				WHERE 	payoutdetail.DocNo = payout.DocNo "
				+ 	"				AND 	IsStatus <> 3 "
				+ 	"			) = 0 "
				+ 	"			) THEN 3 ELSE 2 END "
				+ 	"			), "
				+ 	"			ModifyDate = NOW(), "
				+ 	"			Remark = CONCAT(COALESCE(Remark, ''), ', แผนกรับเข้า') "
				+ 	"WHERE		DocNo = '" + S_DocNo + "' ";
			
			System.out.println(S_SqlDetail);
			System.out.println(S_Sql);
			
			stmt.executeUpdate(S_SqlDetail);
	        stmt.executeUpdate(S_Sql);
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
	
	private void updateReceivePayout(String S_DocNo, String S_ListID, String S_ListItemStockID, String Remark) throws Exception {
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
        
        try{
			String S_SqlDetailSub = 
					
					"UPDATE		payoutdetailsub "
				+ 	"SET		IsStatus = 3, "
				+ 	"			ReceiveDateTime = NOW(), "
				+ 	"			UserReceiveID = " + S_UserId + ", "
				+ 	"			Remark = '" + Remark + "' "
				
				+ 	"WHERE 		ID IN (" + S_ListID + ") "
				
				+ 	"AND 		IsStatus = 1 " ; 

			
			String S_SqlUpdateItemStock = 
					
					"UPDATE		itemstock "
				+ 	"SET		IsStatus = 5, "
				+ 	"			LastReceiveInDeptDate = NOW() "
				
				+ 	"WHERE 		RowID IN (" + S_ListItemStockID + ") "
				
				+ 	"AND 		IsStatus = 4 " ;
			
			System.out.println(S_SqlDetailSub);
			
			System.out.println(S_SqlUpdateItemStock);
			
	        stmt.executeUpdate(S_SqlDetailSub);
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

		}
	}
	
	private void notAcceptToSendSterile(String S_DocNo, String S_ListID, String S_ListItemStockID) throws Exception {
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
        ResultSet rs = null;
        
        try{
        	
        	
			String S_SqlDetailSub = 
					
					"UPDATE		payoutdetailsub "
				+ 	"SET		IsStatus = 2, "
				+ 	"			ReceiveDateTime = NOW(), "
				+ 	"			UserReceiveID = " + S_UserId + ", "
				+ 	"			Remark = 'แผนกไม่รับ ส่งคืนกลับไปฆ่าเชื้อ' "
				
				+ 	"WHERE 		ID IN (" + S_ListID + ") "
				
				+ 	"AND 		IsStatus = 1 " ; 
	
			
			String S_SqlUpdateItemStock = 
					
					"UPDATE		itemstock "
				+ 	"SET		itemstock.IsStatus = 0, " 
				+ 	"        	itemstock.IsNewUsage = 0, " 
				+ 	"        	itemstock.IsNew = 0, " 
				+ 	"         	itemstock.IsPay = 0 , " 
				+ 	"        	itemstock.IsDispatch = 0, " 
				+ 	"        	LastReceiveDeptDate = NOW(), "  						
				+ 	"			LastSendDeptDate = NOW() "
				
				+ 	"WHERE 		RowID IN (" + S_ListItemStockID + ") "
				
				+ 	"AND 		IsStatus = 4 " ;
			
			System.out.println(S_SqlDetailSub);

	        stmt.executeUpdate(S_SqlDetailSub);
	        
	        // Create Send Sterile
			String S_SSDocNo = SendSterile.getSendSterileDocNo(S_DB, S_DocNo, S_DeptId, S_UserId, "0",  "แผนกไม่รับส่งกลับมาล้างใหม่");
					
			if( S_SSDocNo != null && (!S_SSDocNo.equals("")) ){

				String S_SqlInsertSendSterileDetail = 
						
					"INSERT INTO  sendsteriledetail ( "
				+ 	"   sendsteriledetail.SendSterileDocNo, "
				+ 	"   sendsteriledetail.ItemStockID, "
				+ 	"   sendsteriledetail.Qty, "
				+ 	"   sendsteriledetail.Remark, "
				+ 	"   sendsteriledetail.UsageCode, "
				+ 	"   sendsteriledetail.IsSterile, "
				+ 	"   sendsteriledetail.IsStatus,"
				+ 	"	sendsteriledetail.SSdetail_IsWeb, "
				+ 	"	sendsteriledetail.RefDocNo "
				+ 	"   ) "
				
				+ 	"   SELECT "
				+ 	"	'" + S_SSDocNo + "', "
				+ 	"   itemstock.RowID, "
				+ 	"   1, "
				+ 	"   '', "
				+ 	"   itemstock.UsageCode, "
				+ 	"   0, "
				+ 	"   0, "
				+ 	"   1, "
				+ 	"	'" + S_SSDocNo + "' "
				
				//+ 	"   (SELECT ID FROM payoutdetailsub WHERE payoutdetailsub.ItemStockID = itemstock.RowID ORDER BY ID DESC LIMIT 1) "

				+ 	"   FROM        itemstock "

				+ 	"   WHERE       RowID IN (" + S_ListItemStockID + ") ";
				
				
				System.out.println(S_SqlInsertSendSterileDetail);
				System.out.println(S_SqlUpdateItemStock);
				
				stmt.executeUpdate(S_SqlInsertSendSterileDetail);
		        stmt.executeUpdate(S_SqlUpdateItemStock);
				
			}
 
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
		}
	}
	
	private void createOccurrence(String S_DocNo) throws Exception {
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        Statement stmt = conn.createStatement();
        
        try{
        	
	        // Create Send Sterile
			String S_OCDocNo = Occurrence.getOccurrenceDocNo(S_DB, S_DocNo, S_DeptId, S_UserId, "1",  "บันทึกความเสี่ยงจากแผนก");
					
			if( S_OCDocNo != null && (!S_OCDocNo.equals("")) ){

				String S_Sql = 
						
					"	INSERT INTO  occurancedetail ( "
				+ 	"   DocNo, "
				+ 	"   ItemStockID, "
				+ 	"   Remark, "
				+ 	"	RefDocNo, "
				+ 	"	OccurrenceDate,"
				
				+ 	"	OccuranceID "

				+ 	"   ) "
				
				+ 	"   SELECT "
				
				+ 	"	'" + S_OCDocNo + "', "
				+ 	"   payoutdetailsub.ItemStockID, "
				+ 	"   'บันทึกความเสี่ยงจากแผนก', "
				+ 	"	'" + S_DocNo + "', "
				+ 	"	NOW(),"
				
				+ 	"	payoutdetailsub.OccuranceTypeID "

				+ 	"   FROM        payoutdetailsub "
				
				+ 	"   INNER JOIN  payoutdetail "
				+	"	ON			payoutdetail.ID = payoutdetailsub.Payoutdetail_RowID "

				+ 	"   WHERE     	payoutdetail.DocNo = '" + S_DocNo + "' "
				
				+ 	"	AND 		payoutdetailsub.OccuranceTypeID IS NOT NULL "
				+ 	"	AND 		payoutdetailsub.IsStatus = 1 "; 
					
				System.out.println(S_Sql);
				
				stmt.executeUpdate(S_Sql);
				
			}
 
	        conn.commit();
	        
		}catch(Exception e){
			e.printStackTrace();
		}finally{
	        
	        if (conn != null) {
	            conn.close();
	        }
		}
	}
	
	private String getSqlPayout() {
		
		final String S_Text = Textbox_Search.getText();
		
		String S_Sql = "";
		
		S_Sql =
			"	SELECT 	payout.DocNo, "
		+	"			DATE_FORMAT(payout.CreateDate, '%d-%m-%Y') AS CreateDate, " 
		+	"			DATE_FORMAT( DATE(payout.ModifyDate),'%d/%m/%Y' ) AS date_pay, " 
		+	"           TIME(payout.ModifyDate) AS time_pay, "
		//+	"			COALESCE(payout.Desc, '') AS Descriptions, "
		//+	"			payout.RefDocNo, " 
		+	"			payout.`IsStatus` AS IsStatus, " 
		
		+	"			CASE "
		+	"			      payout.IsStatus "
		+	"			      WHEN '2' THEN 'รอรับเข้า' "
		+	"			      WHEN '3' THEN 'แผนกรับเข้าแล้ว' "
		+	"			      WHEN '6' THEN 'รอรับเข้า(ของค้างบางตัว)' "
		+	"			      WHEN '9' THEN 'แผนกรับเข้าแล้ว(ของค้างบางตัว)' "
		+	"			  END AS StatusPO, "
		
		+	"			("
		+ 	"				SELECT 		COUNT(*) "
		+ 	"				FROM 		payoutdetailsub "
		+ 	"				INNER JOIN 	payoutdetail "
		+ 	"				ON  		payoutdetailsub.Payoutdetail_RowID = payoutdetail.ID "
		+ 	"				WHERE 		payoutdetail.DocNo = payout.DocNo "
		+ 	"			) AS Count_Detail_Sub, " 
		
		+	"			(SELECT COUNT(*) FROM payoutdetail WHERE payoutdetail.DocNo = payout.DocNo) AS Count_Detail, " 
		+	"	employee_1.FirstName AS Recipient , "  
		+	"	employee_2.FirstName AS Approve "
								
		+	"	FROM 	payout "
		
		+	"	INNER JOIN 		employee AS employee_1 "
		+ 	"	ON 				employee_1.EmpCode = payout.RecipientCode "
		
		+	"	INNER JOIN 		employee AS employee_2 "
		+ 	"	ON 				employee_2.EmpCode = payout.Approve "
	
		+	"	WHERE 	payout.IsCancel = 0 "
		+ 	"	AND		payout.IsSpecial = 0 "
		+ 	"	AND		payout.IsBorrow = 0 "
		+ 	"	AND		payout.DeptID = " + S_DeptId + " ";
	
		if (!S_Text.equals("")) {		
			S_Sql +=
			"	AND  	(payout.DocNo LIKE '%" + S_Text + "%') ";
		}
		
		try {
			switch (Combobox_Status.getSelectedIndex()) {
			case 1: 
				S_Sql +=
				"	AND  	payout.IsStatus = 2 ";
				break;
				
			case 2:
				S_Sql +=
				"	AND  	payout.IsStatus = 3 ";
				break;
				
			default : 
					S_Sql += "	AND		(payout.IsStatus = 2 OR payout.IsStatus = 3) ";
					
			}
		}catch(Exception e) {
			S_Sql += "	AND		(payout.IsStatus = 2 OR payout.IsStatus = 3) ";
		}
		
		if (!Datebox_SDocDate.getText().trim().equals("") && !Datebox_EDocDate.getText().trim().equals("")) {		
			S_Sql +=
			"	AND  	( DATE(payout.CreateDate) BETWEEN DATE('" + DateTime.convertDate(Datebox_SDocDate.getText()) + "') AND DATE('" + DateTime.convertDate(Datebox_EDocDate.getText()) + "') ) ";
		}
		
		S_Sql += 
			"	ORDER BY payout.`IsStatus`, DATE(payout.CreateDate) DESC, payout.DocNo DESC LIMIT 100 ";
	

		System.out.println(S_Sql);
	
		return S_Sql;
	
	}
	
	public void onDisplayDetail(String S_DocNo) throws Exception{
		
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;
        
		try{	
			
			onProcess(true);
			
			rs = stmt.executeQuery(getSqlPayoutDetail(S_DocNo));
			
				
			int I_No = 1;
			String S_IsStatus = "";
			boolean IsHide = false;
			
			Listbox_Item.getItems().clear();
			
			while(rs.next()){
				Listitem list = new Listitem();
				
				Textbox_Recipient.setText(rs.getString("Recipient"));
				Textbox_Approve.setText(rs.getString("Approve"));
				Textbox_Datepay.setText(rs.getString("date_pay"));
				Textbox_Timepay.setText(rs.getString("time_pay"));
								
				S_IsStatus = rs.getString("IsStatus");
				IsHide = !rs.getString("its_status").equals("4") || !rs.getString("its_pay").equals("1");
				
				final Listcell lc_opt = new Listcell("", getIconStatus( S_IsStatus, rs.getString("OccuranceTypeID")));
				lc_opt.setTooltiptext(rs.getString("OccuranceName"));
				//System.out.print("Log Occurance : " + rs.getString("OccuranceName"));
				
				lc_opt.setAttribute("ID", rs.getString("OccuranceTypeID"));

				list.appendChild(new Listcell(I_No + "."));
				list.appendChild(new Listcell(rs.getString("UsageCode")));				
				list.appendChild(new Listcell(rs.getString("itemname")));
				list.appendChild(lc_opt);
				
				list.appendChild(newCheckbox(lc_opt, rs.getString("ID"), S_IsStatus, IsHide));
				//System.out.print("Check Occurance : " + rs.getString("ID"));
				
				//Attribute
                list.setAttribute("ID", rs.getString("ID"));
                list.setAttribute("ItemStockID", rs.getString("ItemStockID"));
                list.setAttribute("UsageCode", rs.getString("UsageCode"));
                
                list.setDisabled(!S_IsStatus.equals("1") || IsHide);
                
                list.setTooltiptext( rs.getString("its_status") + rs.getString("its_pay"));
                
				Listbox_Item.appendChild(list);
      
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
            
            onProcess(false);
            
            if(B_RC_IsUsedReceiveByBarCode) {
            	Textbox_Input.focus();
            }
		}
    }
	
	private String getIconStatus(final String S_IsStatus, final String S_OccuranceId){
		
		//System.out.println("S_IsStatus = " + S_IsStatus + " , S_OccuranceId = " + S_OccuranceId);
		
		switch(S_IsStatus) {
			case "1" : return (S_OccuranceId == null) ? ( B_RC_IsUsedReceiveByBarCode ? "/images/ic_alert.png" : "/images/ic_check.png" ) : "/images/ic_warning.png";
			case "2" : return "/images/ic_warning.png";
			case "3" : return "/images/ic_check_green.png";
			default : return "";
		}
	}
	
	private Listcell newCheckbox(final Listcell lc_opt, final String S_ID, final String S_IsStatus, final boolean IsHide){
		
		Listcell lc = new Listcell();
		
		final Checkbox chk = new Checkbox();
		
		//chk.setTooltiptext("S_IsStatus = " + S_IsStatus + ", " + !IsHide);
		
		if(S_IsStatus.equals("1") && !IsHide) {
			chk.setChecked(!B_RC_IsUsedReceiveByBarCode);
			
			chk.addEventListener("onCheck", new EventListener<Event>() {
				public void onEvent(Event event) throws Exception {
					
					if(lc_opt.getTooltiptext() == null || lc_opt.getTooltiptext().equals("")) {
						lc_opt.setImage(chk.isChecked() ? "/images/ic_check.png" : "/images/ic_alert.png");	
					}
					
					/*
					if(!chk.isChecked()) {
						lc_opt.setTooltiptext("");
						lc_opt.setAttribute("ID", null);
						
						// Update Occurrence Type ID
						updateOccurenceType(S_ID, null);
					}
					*/
		        }
		    });
			
			lc_opt.addEventListener("onClick", new EventListener<Event>() {
				public void onEvent(Event event) throws Exception {
					//lc_opt.setImage("/images/ic_warning.png");
					callOccurrence(lc_opt, chk, S_ID);
					System.out.print("Occ : " + lc_opt + "Chk : " +chk+ "ID : " + S_ID);
		        }
		    });
			
		}else if(IsHide || S_IsStatus.equals("2") || S_IsStatus.equals("3")) {
			chk.setChecked(false);
			chk.setDisabled(true);
		}

		lc.appendChild(chk);
		
		return lc;
	}
	
	private void callOccurrence(final Listcell lc_opt, final Checkbox chk, final String S_ID) {
		Window_PopUp.getChildren().clear();
		Window_PopUp.setVisible(true);
		Window_PopUp.setFocus(true);
		Window_PopUp.setPosition("center");
		Window_PopUp.setMode("modal");
		Window_PopUp.setBorder("normal");
		Window_PopUp.setClosable(true);		
		Window_PopUp.setHeight("200px");
		Window_PopUp.setWidth("500px");
		
		Vbox vbx = new Vbox();		
		vbx.setWidth("100%");
		vbx.setAlign("center");
		
		Combobox cbb = new Combobox();
		cbb.setWidth("250px");
		
		Comboitem citem_ = new Comboitem();
		citem_.setLabel("-");
		citem_.setValue(null);
		cbb.appendChild(citem_);
		
        Iterator li = Model_OccurrenceType.iterator();

        while(li.hasNext()) {
        	ModelMaster m = (ModelMaster) li.next();
        	
			Comboitem citem = new Comboitem();
			citem.setLabel(m.getS_Name());
			citem.setValue(m.getS_Id());

			cbb.appendChild(citem);
		}
        
        cbb.setSelectedIndex(0);
		
		Button btn = new Button("บันทึก");
		btn.setWidth("150px");
		btn.setStyle("color:#ffffff;background: #2e5fbd;border-radius: 4px;");
		btn.addEventListener("onClick", new EventListener<Event>() {
			public void onEvent(Event event) throws Exception {	
				
				try {
					
					String S_OccurrenceName = cbb.getSelectedItem().getLabel();
					String S_OccurrenceId = (String)cbb.getSelectedItem().getValue();
						
					if(cbb.getSelectedIndex() <= 0) {
						lc_opt.setTooltiptext("");
						lc_opt.setAttribute("ID", null);
						
						// Update Occurrence Type ID
						updateOccurenceType(S_ID, null);
						
						lc_opt.setImage(chk.isChecked() ? "/images/ic_check.png" : "/images/ic_alert.png"); 
						
					}else {
						lc_opt.setTooltiptext(S_OccurrenceName);
						lc_opt.setAttribute("ID", S_OccurrenceId);
						
						System.out.print("OccuranceName : " + S_OccurrenceName);
						
						// Update Occurrence Type ID
						updateOccurenceType(S_ID, S_OccurrenceId);
						
						lc_opt.setImage("/images/ic_warning.png");
						
					}
					
					
		
				}catch(Exception e) {
					
					e.printStackTrace();
					
					lc_opt.setTooltiptext("");
					lc_opt.setAttribute("ID", null);
					
					lc_opt.setImage("/images/ic_check.png");
				}
				
				Window_PopUp.setVisible(false);
	        }
	    });
		
		vbx.appendChild(cbb);
		vbx.appendChild(new Separator());
		vbx.appendChild(new Separator());
		vbx.appendChild(btn);		
		
		Window_PopUp.appendChild(new Caption("บันทึกความเสี่ยง (Occurrence)"));
		Window_PopUp.appendChild(vbx);
	}
	
	
	private boolean updateOccurenceType(final String S_ID, final String S_OccurrenceId) throws Exception{
		
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        Statement stmt = conn.createStatement();
        
		try {
  
			String S_SqlUpdate = 
					
					"UPDATE		payoutdetailsub "
				+ 	"SET		OccuranceTypeID = " + S_OccurrenceId + " "			
				+ 	"WHERE 		ID = " + S_ID + " ";
				
			
			System.out.println(S_SqlUpdate);
	
			int I_AffectedRows = stmt.executeUpdate(S_SqlUpdate);
			
			System.out.println("I_AffectedRows = " + I_AffectedRows);
			
			return true;
			
		}catch(Exception e) {
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
	
	private String getSqlPayoutDetail(String S_DocNo) {
			
		String S_Sql = "";
		
		S_Sql =
			"	SELECT 		item.itemcode, " 
		+	"				item.itemname, "
		+ 	"				payoutdetailsub.ID AS ID, "
		+	"				payoutdetailsub.ItemStockID, "
		+	"				DATE_FORMAT(DATE(payout.ModifyDate),'%d/%m/%Y') AS date_pay, " 
		+	"				TIME(payout.ModifyDate) AS time_pay, "
		+	"				payoutdetailsub.UsageCode, "
		+	"				payoutdetailsub.IsStatus, "
		+	"				payoutdetailsub.OccuranceTypeID, "
		+	"				occurancetype.OccuranceName, "
		+	" 				(SELECT settingapp.chkreceiveIn " 
		+	"            	FROM settingapp "
		+	"            	WHERE settingapp.DeptID = " + S_DeptId + " "  
		+	"            	LIMIT 1 ) AS chkreceiveIn, "
		//+ 	"				itemstock.IsCancel AS its_cancel, "
		+ 	"	  			itemstock.IsStatus AS its_status, "
		+ 	"	  			itemstock.IsPay AS its_pay, "
		+	"				employee_1.FirstName AS Recipient, " 
		+	"				employee_2.FirstName AS Approve "
		
		+	"	FROM 		payoutdetailsub "
		
		+	"	INNER JOIN	payoutdetail "
		+	"	ON 			payoutdetailsub.Payoutdetail_RowID = payoutdetail.ID  "
		
		+	"	INNER JOIN	payout "
		+	"	ON 			payout.DocNo = payoutdetail.DocNo "
		
		+	"	INNER JOIN 	itemstock  "
		+	"	ON			payoutdetailsub.ItemStockID = itemstock.RowID  "
		
		+	"	INNER JOIN 	item  "
		+	"	ON			itemstock.ItemCode = item.itemcode  "
		
		+	"	INNER JOIN 	employee AS employee_1 "
		+	"	ON 			employee_1.EmpCode = payout.RecipientCode "  
			
		+	"	INNER JOIN 	employee AS employee_2 "
		+	"	ON 			employee_2.EmpCode = payout.Approve "
		
		+	"	LEFT JOIN 	occurancetype  "
		+	"	ON			occurancetype.ID = payoutdetailsub.OccuranceTypeID  "
								
		+	"	WHERE  		payoutdetail.DocNo = '" + S_DocNo + "' "
		
		+ 	"	AND			itemstock.DepID = " + S_DeptId + " "
		
		//+ "	AND			itemstock.IsCancel = 0 "
		
		//+	"	AND  		itemstock.IsStatus = 4 "
		//+	"	AND  		itemstock.IsPay = 1 "
		
		+	"	ORDER BY 	item.itemcode "
		
		+ 	"	LIMIT 1000 ";
		
		System.out.println(S_Sql);
	
		return S_Sql;
	
	}
	
	private void checkPayoutDetail(String S_Input){
		try{
			
			Iterator<Listitem> li = Listbox_Item.getItems().iterator();
			
			boolean B_IsFine = false;
			
			while(li.hasNext()){
				Listitem list = (Listitem) li.next();	
				
				if( ((String)list.getAttribute("UsageCode")).equals(S_Input) ) {
					
					Listcell lc_3 = (Listcell)list.getChildren().get(3);
					String S_OccId = (String) lc_3.getAttribute("ID");
					
					((Checkbox)((Listcell)list.getChildren().get(4)).getChildren().get(0)).setChecked(true);
					lc_3.setImage( ( S_OccId != null && !S_OccId.equals("") ) ? "/images/ic_check.png" : "/images/ic_warning.png");
					
					B_IsFine = true;
					
					break;
				}	
			}
			
			
			if(!B_IsFine) {
				Messagebox.show("ไม่พบรายการ !!");
			}
	
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			Textbox_Input.setText("");
			Textbox_Input.focus();
		}
	}
	
	// ================================================================================
	// Drop-down
	// ================================================================================
	
	private void defineOccurrenceType() throws Exception{
		
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;
        
		try{	
			
			List<ModelMaster> list = new ArrayList<>();
			
			rs = stmt.executeQuery(getSqlOccurrenceType());
				
			while(rs.next()){
				list.add(
							new ModelMaster(
								rs.getString("ID"),
								rs.getString("ID"),
								rs.getString("OccuranceName"),
								false
							)
						);
			}
			
			Model_OccurrenceType = list;
			
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
	
	private String getSqlOccurrenceType() {
		
		String S_Sql = "";
		
		S_Sql =
				"	SELECT 		occurancetype.ID, "
			+	"				occurancetype.OccuranceName " 
			+	"	FROM 		occurancetype "
			+	"	WHERE 		ProcessOccID = 3 "
			+	"	ORDER BY 	occurancetype.OccuranceName ASC ";
		
		System.out.println("Occurance : " + S_Sql);
		
		return S_Sql;
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
			
			rs = stmt.executeQuery("SELECT RC_IsUsedReceiveByBarCode FROM configuration_web LIMIT 1");

			if(rs.next()){
				B_RC_IsUsedReceiveByBarCode = rs.getBoolean("RC_IsUsedReceiveByBarCode");
				
				Textbox_Input.setVisible(B_RC_IsUsedReceiveByBarCode);
				Image_Input.setVisible(B_RC_IsUsedReceiveByBarCode);
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
	
	public void onProcess(boolean b) throws Exception {
		
		WinProcess.setVisible(b);
		
		if(b) {	    	
	    	WinProcess.setFocus(b);
	    	WinProcess.setPosition("center");
	    	WinProcess.setMode("modal");
		}
    }
}


