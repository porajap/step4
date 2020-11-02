package com.poseintelligence.cssd.transaction;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.A;
import org.zkoss.zul.Button;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import com.poseintelligence.cssd.document.SendSterile;
import com.poseintelligence.cssd.model.ModelMaster;
import com.poseintelligence.cssd.utillity.DateTime;
import org.zkoss.zk.ui.util.GenericForwardComposer;

@SuppressWarnings({ "unused", "rawtypes", "serial" })
public class DepartmentSendSterile2 extends Div{

	private static final long serialVersionUID = 3525844790119368775L;
	private boolean B_IsCreate = true;
	private int SS_IsFindStatus = 0;
	public String DocNo_Payout;
	private Session session = Sessions.getCurrent();
	private Button Button_addHN;
	private Button Button_saveHN;
	private Button Button_deleteHN;
	private String 	S_UserId,
					S_DeptId,
					S_UserName,
					S_IsAdmin,
					S_EmpCode,
					S_DepName,
					S_DB,
					B_ID;
	private String S_DocNo = null;
	private String S_IsStatus = "3";
	private String S_TempStatus = "3";
	private Listbox Listbox_Document;
	private Listbox Listbox_ItemStock;
	private Listbox Listbox_DocumentDetail;
	private Textbox Textbox_SearchItemStock;
	private Textbox Textbox_Search;
	private Textbox Textbox_SearchDocNo;
	private Textbox QRCODE;
	private Datebox Datebox_SDocDate;
	private Combobox Combobox_Status;
	private Combobox Combobox_type;
	private Combobox Combox_Employee;
	private Combobox ComboboxNameHN;
	private Checkbox Checkbox_expire;
	private Checkbox Check_ems;
	public  Checkbox Checkbox_hn;
	private Textbox Textbox_Document;
	private Textbox Textbox_SSDate;
	private Textbox Textbox_hn_name;
	private Button Button_Save;
	private Button Button_Send;
	
	
	private Button Button_SearchItemStock;

	
	
	private Label Label_Search;
	private Image Image_Search;
	private Window WinProcess;
	private Window Window_PopUp;
	private Window Window_PopUp_image;
	private Window Window_PopUp_hn;
	private Window Window_PopUp_hn_detail;

	
	
	List<ModelMaster> Model_ResterileType = new ArrayList<>();
	
	public void onCreate() throws Exception {
		
		bySession();
		init();
	
    }

	private void bySession(){
		try {

//			System.out.println("S_UserId : " + S_UserId);
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
			
			
		
		}catch (Exception e) {
			System.out.println("ERROR bySession : " + e.getMessage());
		}
	}
	
	private void init() throws Exception{
		
		Window_PopUp_image = ((Window)getFellow("Window_PopUp_image"));
		Window_PopUp_hn_detail = ((Window)getFellow("Window_PopUp_hn_detail"));
		Window_PopUp_hn = ((Window)getFellow("Window_PopUp_hn"));
		Window_PopUp = ((Window) getFellow("Window_PopUp"));
		Button_Send = ((Button) getFellow("Button_Send"));
//		Button_addHN = ((Button) getFellow("Button_addHN"));
//		Button_saveHN = ((Button) getFellow("Button_saveHN"));
//		Button_deleteHN = ((Button) getFellow("Button_deleteHN"));
		Textbox_SSDate = ((Textbox) getFellow("Textbox_SSDate"));
		Textbox_Document = ((Textbox) getFellow("Textbox_Document"));
		Checkbox_hn = ((Checkbox) getFellow("Checkbox_hn"));
		Check_ems = ((Checkbox) getFellow("Check_ems"));
		Checkbox_expire = ((Checkbox) getFellow("Checkbox_expire"));
		Combox_Employee = ((Combobox) getFellow("Combox_Employee"));
		Combobox_type = ((Combobox) getFellow("Combobox_type"));
		Datebox_SDocDate = ((Datebox) getFellow("Datebox_SDocDate"));
		Listbox_Document = ((Listbox) getFellow("Listbox_Document"));
		Listbox_ItemStock = ((Listbox) getFellow("Listbox_ItemStock"));
		Listbox_DocumentDetail = ((Listbox) getFellow("Listbox_DocumentDetail"));
		Textbox_SearchItemStock = ((Textbox) getFellow("Textbox_SearchItemStock"));
		Textbox_SearchDocNo = ((Textbox) getFellow("Textbox_SearchDocNo"));
		QRCODE = ((Textbox) getFellow("QRCODE"));
		

	
		Datebox_SDocDate.setText(DateTime.getDateNow());
//		Datebox_EDocDate.setText(DateTime.getDateNow());
		defineReSterileType();
		onDisplayItemStock();
//		onDisplayDocument();
		onDisplayEmployee();
		

	}
	
	
	
	
//	display Itemstock =====================================================
	public void onDisplayItemStock() throws Exception{

		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;
        
		try{	
			
			rs = stmt.executeQuery(getSqlItemStock());
			
			Listbox_ItemStock.getItems().clear();
			
			int I_No = 1;
	
			while(rs.next()){
				Listitem list = new Listitem();

				Listcell lc_item= new Listcell();
				final String itemcode = rs.getString("ItemCode");
				A Item_name = new A(rs.getString("Item_name"));
				
				Item_name.addEventListener("onClick", new EventListener<Event>() {
					public void onEvent(Event event) throws Exception {
						
						callitemage(itemcode);

				    	
			        }
			    });
				
				lc_item.appendChild( Item_name );
				
				
				
				list.appendChild(new Listcell());
				list.appendChild(new Listcell(rs.getString("UsageCode")));
				list.appendChild(lc_item);
//				list.appendChild(new Listcell(rs.getString("Item_name")));
				list.appendChild(new Listcell(rs.getString("ExpireDate")));
				list.setTooltiptext(rs.getString("RowID"));
				
				list.setAttribute("RowID", rs.getString("RowID"));
								
                Listbox_ItemStock.appendChild(list);
                
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

	private String getSqlItemStock() {
		
		String S_Sql = "";
		
		S_Sql = 	
			"	SELECT		itemstock.RowID, "
		+	"				itemstock.ItemCode, "
		+	"				COALESCE(DATE_FORMAT(itemstock.PackDate, '%d-%m-%Y'),'-') AS PackDate, "
		//+	"				COALESCE(DATE_FORMAT(itemstock.ExpireDate, '%d-%m-%Y'),'-') AS ExpireDate, "
		+ 	"	(CASE "
		+	"				WHEN DATE(itemstock.ExpireDate) IS NULL THEN 'รายการที่ถูกสร้างใหม่' " 
		+	"				ELSE DATE_FORMAT(itemstock.ExpireDate, '%d-%m-%Y' ) "
		+	"	END) AS ExpireDate,"
		
		+	"				itemstock.UsageCode, "
		+	"				item.itemname AS Item_name "
		
		+	"	FROM		itemstock "
		
		+	"	LEFT JOIN 	item "
		+	"	ON 			item.itemcode = itemstock.ItemCode "
		
		+	"	WHERE 		itemstock.IsStatus = 5 ";
		
		String S_Text = Textbox_SearchItemStock.getText();
		
		if(!S_Text.trim().equals("")) {
			S_Sql +=	
			"	AND 		("
			+ 	"				itemstock.ItemCode LIKE '%" + S_Text + "%' "
			+ 	"				OR item.itemname LIKE '%" + S_Text + "%' "
			+ 	"				OR itemstock.UsageCode LIKE '%" + S_Text + "%' "
			+ 	"			)  ";
		}
		
			S_Sql +=
			"	AND 		itemstock.IsDispatch =  0" ;
		
	
		S_Sql +=
			"	ORDER BY 	itemstock.UsageCode "
		
		+	"	LIMIT 100 ";
		
		System.out.println(S_Sql);
		
		return S_Sql;
	}
// ========================================================================
	
	
// display Document =======================================================
	public void onDisplayDocument() throws Exception{
		
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;
    	
		try{	
			
			rs = stmt.executeQuery(getSqlDocument());
			
			Listbox_Document.getItems().clear();
			
			int I_No = 1;
			
	       
			while(rs.next()){
				Listitem list = new Listitem();
				Button btn = new Button();
				
				String DocNo =  rs.getString("DocNo");
		        Listcell cell = new Listcell();
				btn.setIconSclass("z-icon-search");
				btn.setSclass("btn");
				btn.addEventListener("onClick", new EventListener<Event>() {
					public void onEvent(Event event) throws Exception {
						
						onDisplayDetail(DocNo, false);

			        }
			    });
				
				cell.appendChild(btn);
				list.appendChild(new Listcell(I_No + "."));
				list.appendChild(new Listcell(rs.getString("DocNo")));
				list.appendChild(new Listcell(rs.getString("DocDate")));
				list.appendChild(new Listcell(rs.getString("Qty")));
				list.appendChild(new Listcell(rs.getString("Elc")));
				list.appendChild(cell);
				
				
				
              	

				list.setTooltiptext(rs.getString("DocNo"));
				
				list.setAttribute("DocNo", rs.getString("DocNo"));
			
								
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
           
		}
    	
    	
	}
	
	private String getSqlDocument() {
		
		String S_Sql = "";
		String S_Text = Textbox_SearchDocNo.getText().trim();

		S_Sql ="SELECT" + 
				"            DocNo, " + 
				"            DATE_FORMAT( DATE( DocDate ) , '%d/%m/%Y' ) AS DocDate ,  " + 
				"            ModifyDate,  " + 
				"            Qty,(  " + 
				"            CASE  " + 
				"                  " + 
				"                WHEN IsStatus = 3 THEN  " + 
				"                'เอกสารร่าง'   " + 
				"                WHEN IsStatus = 1 THEN  " + 
				"                'ส่งล้าง'   " + 
				"                WHEN IsStatus = 2 THEN  " + 
				"                'จ่ายกลางตรวจรับของ' ELSE 'ยกเลิก'   " + 
				"              END   " + 
				"              ) AS Elc,  " + 
				"              IsHn   " + 
				"            FROM  " + 
				"              sendsterile   " + 
				"            WHERE  " + 
				"              sendsterile.IsStatus = 3   " + 
				"              AND sendsterile.IsCancel = 0   " + 
				"              AND sendsterile.DeptID = '"+ S_DeptId +"'   " +
				"              AND DATE( sendsterile.DocDate ) = '"+ DateTime.convertDate(Datebox_SDocDate.getText()) +"'   " + 
				"            AND sendsterile.DocNo LIKE '%" + S_Text + "%'   " + 
				"            AND sendsterile.B_ID = '"+ B_ID +"' ";
		
		System.out.println(S_Sql);
		
		return S_Sql;
	}
// ========================================================================	

	
	
//	display Employee ======================================================
	public void onDisplayEmployee() throws Exception{
		
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;
    	
		try{	
			
			rs = stmt.executeQuery(getSqlEmployee());
			
			Combox_Employee.getItems().clear();
			Combox_Employee.setText("กรุณาเลือกพนักงาน");
			int I_No = 1;
	
			while(rs.next()){
				Comboitem list = new Comboitem();

				list.setLabel(rs.getString("FirstName") + " " + rs.getString("LastName"));
				list.setValue(rs.getString("EmpCode"));

				Combox_Employee.appendChild(list);

								
//				Combox_Employee.appendChild(list);
                
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
           
		}
    	
    	
	}
	
	private String getSqlEmployee() {
		
		String S_Sql = "";
		S_Sql ="SELECT" + 
				"            employee.EmpCode," + 
				"            employee.FirstName," + 
				"            employee.LastName " + 
				"          FROM" + 
				"            employee " + 
				"          WHERE" + 
				"            employee.IsAdmin = 0 AND employee.DepID = 30 ORDER BY employee.EmpCode DESC ";
		
		System.out.println(S_Sql);
		
		return S_Sql;
	}
//	========================================================================
	
	public void  QrCodeClick() throws Exception{

		addSendSterileByQR(QRCODE.getText());

	}

	public void Button_SendSterile() throws Exception {
		
		if(Textbox_Document.getText() == "") {
			Messagebox.show("กรุณาสร้างเอกสารก่อนบันทึก", "CSSD", Messagebox.OK, Messagebox.EXCLAMATION);

		}else {
			Messagebox.show("ยืนยันการบันทึก ?" , "CSSD", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,new EventListener<Event>() {
				public void onEvent(Event evt) throws Exception {
					switch (((Integer) evt.getData()).intValue()) {
		              	case Messagebox.YES:
		              		onSendSterile();
		              		break;
		              	case Messagebox.NO:
		              		break;
					}
				}
			});
		}

	}
	
	public void Button_Cancel() throws Exception {
		
		if(Textbox_Document.getText() == "") {
			Messagebox.show("กรุณาสร้างเอกสารก่อนยกเลิก", "CSSD", Messagebox.OK, Messagebox.EXCLAMATION);

		}else {
			Messagebox.show("ยืนยันการยกเลิก ?" , "CSSD", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,new EventListener<Event>() {
				public void onEvent(Event evt) throws Exception {
					switch (((Integer) evt.getData()).intValue()) {
		              	case Messagebox.YES:
		              		onCancelSendSterile();
		              		break;
		              	case Messagebox.NO:
		              		break;
					}
				}
			});
		}

	}


	String isCheck = "";
	public void Checkbox_expire() throws Exception {
		
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;

    	
		if(Checkbox_expire.isChecked()) {
			
			   String DocNo = Textbox_Document.getText();
		       String sSql_update = "UPDATE sendsteriledetail SET IsSterile = 1 , ResterileType = 7   WHERE   SendSterileDocNo = '" + DocNo + "' ";
		       stmt.executeUpdate(sSql_update);
				System.out.println(sSql_update);
				
			isCheck = "1";
			onDisplayDetail(Textbox_Document.getText().toString(),false);
		}else {
			
			   String DocNo = Textbox_Document.getText();
		       String sSql_update = "UPDATE sendsteriledetail SET IsSterile = 0 , ResterileType = 1   WHERE   SendSterileDocNo = '" + DocNo + "' ";
		       stmt.executeUpdate(sSql_update);
				System.out.println(sSql_update);
				
			isCheck = "0";
			onDisplayDetail(Textbox_Document.getText().toString(),false);
		}
		
	}
	
	String isCheck_ems = "";
	public void Check_ems() throws Exception {
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;
    	
		Iterator<Listitem> li = Listbox_DocumentDetail.getItems().iterator();
		String S_ListItemStockID = "";
		

		while(li.hasNext()){
			Listitem list = (Listitem) li.next();	
			S_ListItemStockID += (String)list.getAttribute("ItemStockID") + ",";
			
		}
		
		
		// Check S_ListItemStockID
		if(S_ListItemStockID.equals("")) {
			return;
		}else {
			S_ListItemStockID = S_ListItemStockID.substring(0, S_ListItemStockID.length() - 1);
		}
		
		if(Check_ems.isChecked()) {
			

		       String sSql_update = "UPDATE itemstock SET IsRemarkExpress = 1   WHERE  	RowID IN (" + S_ListItemStockID + ") ";
		       stmt.executeUpdate(sSql_update);
		       
				System.out.println(sSql_update);
			

			isCheck_ems = "1";
			onDisplayDetail(Textbox_Document.getText().toString(),false);
		}else {
			
		       String sSql_update = "UPDATE itemstock SET IsRemarkExpress = 0   WHERE  	RowID IN (" + S_ListItemStockID + ") ";
		       stmt.executeUpdate(sSql_update);
		       
				System.out.println(sSql_update);
			
			
			isCheck_ems = "0";
			onDisplayDetail(Textbox_Document.getText().toString(),false);
		}
		
	}
	
	public void Checkbox_hn() throws Exception {
		final String DocNo_hn = Textbox_Document.getText();

		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
        ResultSet rs = null;
			
			
			if(DocNo_hn == "") {
				Messagebox.show("กรุณาสร้างเอกสาร", "CSSD", Messagebox.OK, Messagebox.INFORMATION);
				Checkbox_hn.setChecked(false);
			}else {
				
				if(Checkbox_hn.isChecked()) {

					Window_PopUp_hn.setAttribute("DocNo",DocNo_hn);
					Window_PopUp_hn.setMode("modal");
				
				}else {
					
			        int IsHn = 0;
					String sSql = " SELECT IsHn FROM sendsterile WHERE DocNo = '" + DocNo_hn + "' ";
					rs = stmt.executeQuery(sSql);
					while(rs.next()){
						IsHn =  rs.getInt("IsHn");
					}
					
					if(IsHn == 1) {
						Window_PopUp_hn.setAttribute("DocNo",DocNo_hn);
						Window_PopUp_hn.setMode("modal");
						Checkbox_hn.setChecked(true);
					}
					
				}
			}


	

		
		
	}
	
	public void onConfirmSend() throws Exception{
		
		if(Listbox_ItemStock.getSelectedCount() == 0) {
			Messagebox.show("ไม่ได้เลือกรายการ !!", "CSSD", Messagebox.OK, Messagebox.INFORMATION);
			return;
		}
		
		if(Listbox_Document.getSelectedCount() == 1 && Listbox_Document.getSelectedItem().getAttribute("IsStatus").equals("0")) {
			Messagebox.show("ไม่สามารถนำเข้ารายการได้ !!", "CSSD", Messagebox.OK, Messagebox.INFORMATION);
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

	public void checkall(int count_click_ex) throws Exception{
		
		System.out.println(count);
		System.out.println(count_click_ex);
		if(count == count_click_ex) {
			Check_ems.setChecked(true);
		}else {
			Check_ems.setChecked(false);
		}
				
	}
	
	public void onSendSterile() throws Exception{
		
		try {
			com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
	        Class.forName(c.S_MYSQL_DRIVER);
	        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
	        conn.setAutoCommit(false);
	        
	        Statement stmt = conn.createStatement();
	    	ResultSet rs = null;
			
			Iterator<Listitem> li = Listbox_DocumentDetail.getItems().iterator();
			String S_ListItemStockID = "";
			

			while(li.hasNext()){
				Listitem list = (Listitem) li.next();	
				S_ListItemStockID += (String)list.getAttribute("ItemStockID") + ",";
				
			}
			
			
			// Check S_ListItemStockID
			if(S_ListItemStockID.equals("")) {
				return;
			}else {
				S_ListItemStockID = S_ListItemStockID.substring(0, S_ListItemStockID.length() - 1);
			}
			
			
			
		       String sSql_update = "UPDATE itemstock SET IsStatus = 0,IsPay = 0   WHERE  	RowID IN (" + S_ListItemStockID + ") ";
		       stmt.executeUpdate(sSql_update);
		       
				System.out.println(sSql_update);
			
			
			
			   final String DocNo = Textbox_Document.getText();
		       String sSql_update2 = "UPDATE sendsterile SET IsStatus = 0   WHERE  	DocNo = '" + DocNo + "'  ";
		       stmt.executeUpdate(sSql_update2);
		       
			   System.out.println(sSql_update2);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			
			final String DocNo_pay = Textbox_Document.getText();

			
			SetPayout(DocNo_pay);
			
 

        	
		}
	}
	
	public void SetPayout(final String DocNo) throws Exception{
		
		try {
			com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
	        Class.forName(c.S_MYSQL_DRIVER);
	        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
	        conn.setAutoCommit(false);
	        
	        Statement stmt = conn.createStatement();
	        Statement stmt2 = conn.createStatement();
	    	ResultSet rs = null;
	    	ResultSet rs2 = null;


	    	DocNo_Payout = generateDoc_Payout();
			String DocNo_ss = Textbox_Document.getText();
   //Payout =========================================================================================================================
	        	int Cnt = 0;
				String sSql = " SELECT COUNT(*) AS Cnt FROM sendsteriledetail WHERE SendSterileDocNo = '" + DocNo_ss + "' ";
				rs = stmt.executeQuery(sSql);
				while(rs.next()){
					Cnt =  rs.getInt("Cnt");
				}
				
				String insert = "INSERT INTO payout( " + 
						"              payout.DocNo, " + 
						"              payout.CreateDate, " + 
						"              payout.ModifyDate, " + 
						"              payout.DeptID, " + 
						"              payout.UserCode, " + 
						"              payout.IsStatus, " + 
						"              payout.Qty, " + 
						"              payout.PayQty, " + 
						"              payout.Remark, " + 
						"              payout.RefDocNo, " + 
						"              payout.IsPrint, " + 
						"              payout.DeptRec, " +
						"              payout.B_ID, " + 
						"              payout.IsWeb, " + 
						"              payout.IsCheck " + 
						"            	)VALUES( " + 
						"              '"+DocNo_Payout+"' ," + 			
						"              NOW()              ," + 						
						"              NOW()              ," + 						
						"              '"+S_DeptId+"'     ," + 						
						"              '"+S_UserId+"'     ," + 
						"              1                  ," + 						
						"              "+Cnt+"            ," + 						
						"              0                  ," + 						
						"               ''                ," + 						
						"              '"+DocNo_ss+"'     ," + 						
						"              0                  ," + 						
						"              0                  ," + 						
						"              '"+B_ID+"'         ," +
						"              1                  ," + 						
						"              1                  " + 						
						"            ) ";
		 		  System.out.println(insert);
				stmt.executeUpdate(insert);
				
	      
					String sSql2 = "SELECT" + 
							"              it.RowID," + 
							"              it.ItemCode,(" + 
							"              SELECT" + 
							"                COUNT(*) AS Cnt " + 
							"              FROM" + 
							"                sendsteriledetail" + 
							"                INNER JOIN itemstock ON sendsteriledetail.ItemstockID = itemstock.RowID" + 
							"                INNER JOIN sendsterile ON sendsterile.DocNo = sendsteriledetail.SendSterileDocNo " + 
							"              WHERE" + 
							"                sendsterile.DocNo = st.DocNo " + 
							"                AND itemstock.ItemCode = it.ItemCode " + 
							"              ) AS Cnt " + 
							"            FROM" + 
							"              sendsteriledetail" + 
							"              INNER JOIN itemstock AS it ON sendsteriledetail.ItemstockID = it.RowID" + 
							"              INNER JOIN sendsterile AS st ON st.DocNo = sendsteriledetail.SendSterileDocNo " + 
							"            WHERE" + 
							"              st.DocNo = '" + DocNo_ss + "' " + 
							"            GROUP BY" + 
							"              it.ItemCode ";
			 		  System.out.println(sSql2);
					rs = stmt.executeQuery(sSql2);
					while(rs.next()){
			
		        		
		        		final int Cnt_ss =  rs.getInt("Cnt");
		        		final int RowID =  rs.getInt("RowID");
						
						 String insert2 = "INSERT INTO payoutdetail" + 
								"        (DocNo,ItemStockID,Qty,IsStatus,Remark,PayDate,OccuranceTypeID,B_ID)" + 
								"        VALUES" + 
								"        ('"+DocNo_Payout+"','"+RowID+"','"+Cnt_ss+"',0,'',NOW(),0,'"+B_ID+"')";
				 		  System.out.println(insert2);
				 		 stmt2.executeUpdate(insert2);

					}
					
			       	int Cnt_pay = 0;
					String sSql_pay = " SELECT SUM(payoutdetail.Qty) as qty FROM payoutdetail WHERE DocNo = '" + DocNo_Payout + "' ";
			 		  System.out.println(sSql_pay);
					rs = stmt.executeQuery(sSql_pay);
					while(rs.next()){
						Cnt_pay =  rs.getInt("qty");
					}
					String sSql_update = "UPDATE payout SET Qty = '" + Cnt_pay + "' WHERE DocNo = '" + DocNo_Payout + "' ";
			 		  System.out.println(sSql_update);
					stmt.executeUpdate(sSql_update);

			//Payout =========================================================================================================================
					
			//HN =============================================================================================================================
					
			       			String DocHn = "";
							String sSqlc_hn = " SELECT hncode.DocNo FROM hncode WHERE hncode.DocNo_SS = '" + DocNo_ss + "' ";
							rs = stmt.executeQuery(sSqlc_hn);
							while(rs.next()){
								DocHn =  rs.getString("DocNo");
							}
							
					if(DocHn != "") {

							String sSql_send = "SELECT" + 
										"      sendsteriledetail.ItemStockID," + 
										"      sendsteriledetail.Qty, " + 
										"	    itemstock.LastSterileDetailID" + 
										"      FROM" + 
										"      sendsteriledetail" + 
										"      INNER JOIN itemstock ON sendsteriledetail.ItemStockID = itemstock.RowID " + 
										"      WHERE sendsteriledetail.SendSterileDocNo = '" + DocNo_ss + "' ";
							rs = stmt.executeQuery(sSql_send);
							while(rs.next()){
								
								int ItemStockID = 0;
								int Qty = 0;
								int LastSterileDetailID = 0;
								
								
								ItemStockID =  rs.getInt("ItemStockID");
								Qty =  rs.getInt("Qty");
								LastSterileDetailID =  rs.getInt("LastSterileDetailID");

								
								String insert3 = "INSERT INTO hncode_detail(" + 
										"          hncode_detail.DocNo," + 
										"          hncode_detail.ItemStockID," + 
										"          hncode_detail.Qty," + 
										"          hncode_detail.ImportID," + 
										"          hncode_detail.IsStatus," + 
										"          hncode_detail.RefDocNo," + 
										"          hncode_detail.IsCancel," + 
										"          hncode_detail.B_ID," + 
										"          hncode_detail.LastSterileDetailID" + 
										"          )VALUES(" + 
										"            '" + DocHn + "'," + 
										"            '" + ItemStockID + "'," + 
										"            '" + Qty + "'," + 
										"            0," + 
										"            1," + 
										"            '" + DocNo_Payout + "'," + 
										"            0," + 
										"            '" + B_ID + "'," + 
										"            '" + LastSterileDetailID + "'" + 
										"          )";
								stmt2.executeUpdate(insert3);		
							}
							
							Window_PopUp_hn_detail.setAttribute("DocNo",DocHn);
							Window_PopUp_hn_detail.setMode("modal");

					}else {
						
			        	onDisplayDocument();
						Listbox_DocumentDetail.getItems().clear();
			        	Textbox_Document.setText("");
			        	S_DocNo = null;
			        	Textbox_SSDate.setText("");
					}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			

			



        	
		}
	}
	
 	public String generateDoc_Payout() throws Exception{

 	    try {
 			com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
 	        Class.forName(c.S_MYSQL_DRIVER);
 	        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
 	        conn.setAutoCommit(false);
 	        
 	        Statement stmt = conn.createStatement();
 	    	ResultSet rs = null;
 	    	
 		        String sql = "SELECT CONCAT('PA',SUBSTRING(YEAR(DATE(NOW())),3,4),LPAD(MONTH(DATE(NOW())),2,0),'-', LPAD( (COALESCE(MAX(CONVERT(SUBSTRING(DocNo,8,5),UNSIGNED INTEGER)),0)+1) ,5,0)) AS DocNo" + 
 		        		"    FROM payout" + 
 		        		"    WHERE DocNo Like CONCAT('PA',SUBSTRING(YEAR(DATE(NOW())),3,4),LPAD(MONTH(DATE(NOW())),2,0),'%')" + 
 		        		"    ORDER BY DocNo DESC LIMIT 1";
 		      
 		  rs = stmt.executeQuery(sql);
 		  if(rs.next()) {
 			 DocNo_Payout = rs.getString("DocNo");
 		  }
 		  
 		  ;
 		  System.out.println(sql);
 		
 		  
 		  
 	}catch (Exception e) {
 		e.printStackTrace();
 		Messagebox.show("ERROR generateDoc_Payout : " + e.getMessage());
 		System.out.println("ERROR generateDoc_Payout : " + e.getMessage());
 	}
 	    return DocNo_Payout;
 	}
	
	
	public void Changetype() throws Exception {
		String ss = 	Combobox_type.getSelectedItem().getValue();
			
				onDisplayDetail(Textbox_Document.getText().toString(),false);
			
	}
	
	
	
	public void onCancelSendSterile() throws Exception{
		
		try {
			com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
	        Class.forName(c.S_MYSQL_DRIVER);
	        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
	        conn.setAutoCommit(false);
	        
	        Statement stmt = conn.createStatement();
	    	ResultSet rs = null;
			
			Iterator<Listitem> li = Listbox_DocumentDetail.getItems().iterator();
			String S_ListItemStockID = "";
			

			while(li.hasNext()){
				Listitem list = (Listitem) li.next();	
				S_ListItemStockID += (String)list.getAttribute("ItemStockID") + ",";
				
			}
			
			
			// Check S_ListItemStockID
			if(S_ListItemStockID.equals("")) {
				return;
			}else {
				S_ListItemStockID = S_ListItemStockID.substring(0, S_ListItemStockID.length() - 1);
			}
			
			
		       String sSql_update = "UPDATE itemstock SET IsStatus = 5,IsPay = 1,IsDispatch = 0   WHERE  	RowID IN (" + S_ListItemStockID + ") ";
		       stmt.executeUpdate(sSql_update);
		       
				System.out.println(sSql_update);
			
			
			   String DocNo = Textbox_Document.getText();
		       String sSql_update2 = "UPDATE sendsterile SET IsCancel = 1 WHERE  	DocNo = '" + DocNo + "'  ";
		       stmt.executeUpdate(sSql_update2);
		       
			   System.out.println(sSql_update2);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			onDisplayItemStock();
        	onDisplayDocument();
			Listbox_DocumentDetail.getItems().clear();
        	Textbox_Document.setText("");
        	Textbox_SSDate.setText("");

        	
		}
	}
	
	public void onSend() throws Exception{
		
		try {
			Iterator<Listitem> li = Listbox_ItemStock.getSelectedItems().iterator();
			
			String S_ListItemStockID = "";
			
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
        	
	        
        	if(S_DocNo == null) {
        		S_SSDocNo = SendSterile.getSendSterileDocNo(S_DB, "", S_DeptId, S_UserId, S_TempStatus, "");
        	}else {
        		S_SSDocNo = S_DocNo;
        	}
					
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
				+ 	"		sendsteriledetail.RefDocNo "
				+ 	"	) "
				
				+ 	"	SELECT 	"
				+ 	"			'" + S_SSDocNo + "', "
				+ 	"   		itemstock.RowID, "
				+ 	"   		1, "
				+ 	"   		'', "
				+ 	"   		itemstock.UsageCode, "
				+ 	"   		0, "
				+ 	"   		1, "
				+ 	"   		" + S_TempStatus + ", "
				+ 	"			'" + S_SSDocNo + "' "
				

				+ 	"   FROM   	itemstock "

				+ 	"   WHERE  	RowID IN (" + S_ListItemStockID + ") "
				
				+ 	"	AND 	IsStatus = 5 ";
				
				// Update Item Stock
				String S_SqlUpdateItemStock = 
						
						"UPDATE		itemstock "
					+ 	"SET		itemstock.IsDispatch = 1 "
					
					+ 	"WHERE 		RowID IN (" + S_ListItemStockID + ") "
					
					+ 	"AND 		IsStatus = 5 " ;
				
				
				

			
				stmt.executeUpdate(S_SqlInsertSendSterileDetail);
		        stmt.executeUpdate(S_SqlUpdateItemStock);
		        
		        
		        String Cnt = "";
				String sSql = " SELECT COUNT(UsageCode) AS cnt_usage FROM sendsteriledetail WHERE SendSterileDocNo = '" + S_SSDocNo + "' ";
				rs = stmt.executeQuery(sSql);
				while(rs.next()){
					Cnt =  rs.getString("cnt_usage");
				}
		        
		       String sSql_update = " UPDATE sendsterile SET Qty = '" + Cnt + "' WHERE DocNo = '" + S_SSDocNo + "'  ";
		       stmt.executeUpdate(sSql_update);
		        
		        // Clear Item Sock
				Listbox_ItemStock.getItems().clear();
				Listbox_Document.getItems().clear();

		        if(S_DocNo == null) {
		        	
		        	S_DocNo = S_SSDocNo;
		        	
//		        	onDisplayDocument();
		        	onDisplayDetail(S_SSDocNo, false);
		        	onDisplayItemStock();
		        }else {
//		        	onDisplayDocument();
		        	onDisplayDetail(S_SSDocNo, false);
		        	onDisplayItemStock();
		        }
  
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
	
	int count_click_ex = 0;
	int count = 0;

	private void addSendSterileByQR(String S_QR) throws Exception {
		
		if(Listbox_Document.getSelectedCount() == 1 && !Listbox_Document.getSelectedItem().getAttribute("IsStatus").equals("-5")) {
			Messagebox.show("ไม่สามารถนำเข้ารายการได้ !!", "CSSD", Messagebox.OK, Messagebox.INFORMATION);
			return;
		}
		
		String S_ItemStockID = findItemStock(S_QR);
		
		if(S_ItemStockID == null) {
			Messagebox.show("ไม่พบรายการ !!");
			
			Textbox_SearchItemStock.setText("");
	        
	        Textbox_SearchItemStock.focus();
	        
			return;
		}
		
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
        ResultSet rs = null;
        
        try{
        	  
        	// Create Send Sterile
        	
        	String S_SSDocNo = null;
        	    
        	if(S_DocNo == null) {
        		S_SSDocNo = SendSterile.getSendSterileDocNo(S_DB, "", S_DeptId, S_UserId, S_TempStatus ,"");
        	}else {
        		S_SSDocNo = S_DocNo;
        	}
					
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
				+ 	"		sendsteriledetail.RefDocNo "
				+ 	"	) "
				
				+ 	"	SELECT 	"
				+ 	"			'" + S_SSDocNo + "', "
				+ 	"   		itemstock.RowID, "
				+ 	"   		1, "
				+ 	"   		'', "
				+ 	"   		itemstock.UsageCode, "
				+ 	"   		0, "
				+ 	"   		1, "
				+ 	"   		" + S_TempStatus + ", "
				+ 	"			'" + S_SSDocNo + "' "
				

				+ 	"   FROM   	itemstock "

				+ 	"   WHERE  	RowID IN (" + S_ItemStockID + ") "
				
				+ 	"	AND 	IsStatus = 5 ";
				
				// Update Item Stock
				String S_SqlUpdateItemStock = 
						
						"UPDATE		itemstock "
					+ 	"SET		itemstock.IsDispatch = 1 "
					
					+ 	"WHERE 		RowID IN (" + S_ItemStockID + ") "
					
					+ 	"AND 		IsStatus = 5 " ;
				
				
				System.out.println(S_SqlInsertSendSterileDetail);
				System.out.println(S_SqlUpdateItemStock);
				
				stmt.executeUpdate(S_SqlInsertSendSterileDetail);
		        stmt.executeUpdate(S_SqlUpdateItemStock);
		        
		        conn.commit();
		        
		        // Clear Item Sock
				Listbox_ItemStock.getItems().clear();
   
		        if(S_DocNo == null) {
		        	
		        	S_DocNo = S_SSDocNo;
		        	
		        	onDisplayDetail(S_SSDocNo, false);
		        	onDisplayItemStock();
		        }else {
		        	onDisplayDetail(S_SSDocNo, false);
		        	onDisplayItemStock();
		        }
  
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
	        
	        QRCODE.setText("");
	        
	        QRCODE.focus();
		}
	}
	
	private String findItemStock(String S_QR) throws Exception{
		String S_Sql = "";
		
		S_Sql = 	
			"	SELECT		itemstock.RowID "

		+	"	FROM		itemstock "
		
		+	"	WHERE 		itemstock.IsStatus = 5 "
		
		+ 	"	AND			itemstock.UsageCode = '" + S_QR + "' ";

			S_Sql +=
			"	AND 		itemstock.IsDispatch =  0 " ; 
		
	
		S_Sql +=
			"	LIMIT 1 ";
		
		
		//System.out.println(S_Sql);
		
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;
        
		try{	
			
			rs = stmt.executeQuery(S_Sql);
			
			return rs.next() ? rs.getString("RowID") : null;	
			
		}catch(Exception e){
			e.printStackTrace();
			return null;
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
	
	
// display Detail ==========================================================================
	public void onDisplayDetail(final String S_DocNo,final boolean IsAlert) throws Exception{
		
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;
        
		try{	
			count = 0;
//			onProcess(true);
			
			this.S_DocNo = S_DocNo;
			
			rs = stmt.executeQuery(getSqlSendSterileDetail(S_DocNo));
				
			int I_No = 1;
			
			Listbox_DocumentDetail.getItems().clear();
			
			while(rs.next()){
		        String UsageCode = rs.getString("UsageCode");
		        String ID = rs.getString("ID");
		        final String HnCode = rs.getString("HnCode");
		        final String FName = rs.getString("FName");
		        int IsHn = rs.getInt("IsHn");   
		        String DocNo = S_DocNo;
		        int IsRemarkExpress = rs.getInt("IsRemarkExpress");
		        int IsSterile = rs.getInt("IsSterile");
		        int ResterileType = rs.getInt("ResterileType");

		        if(IsHn == 1) {
					Window_PopUp_hn.setAttribute("FName_hn",FName);
					Window_PopUp_hn.setAttribute("HnCode_hn",HnCode);
		        	Checkbox_hn.setChecked(true);
		        }else {
		    		Window_PopUp_hn.setAttribute("FName_hn","");
					Window_PopUp_hn.setAttribute("HnCode_hn","");
					
		        	Checkbox_hn.setChecked(false);
		        }
		        
				Listitem list = new Listitem();
				
				
				Button btn_ex = new Button();
		        Listcell cell_ex = new Listcell();

		        btn_ex.setIconSclass("z-icon-rocket");
		        if(isCheck_ems == "1") {
			        btn_ex.setSclass("btn btn-danger");
			        btn_ex.setAttribute("value_ex", "1");
			        count_click_ex = count ;
		        }else {
			        btn_ex.setSclass("btn");
			        btn_ex.setAttribute("value_ex", "0");
			        count_click_ex = 0;
		        }
		        
		        if(IsRemarkExpress == 1) {
			        btn_ex.setSclass("btn btn-danger");
			        btn_ex.setAttribute("value_ex", "1");
		        }else {
			        btn_ex.setSclass("btn");
			        btn_ex.setAttribute("value_ex", "0");
		        }
		        
		        
		        btn_ex.addEventListener("onClick", new EventListener<Event>() {
					public void onEvent(Event event) throws Exception {
						
					    
						if (btn_ex.getAttribute("value_ex") == "1") {
							btn_ex.removeSclass("btn btn-danger");
					        btn_ex.setSclass("btn");
					        btn_ex.setAttribute("value_ex", "0");
							count_click_ex --;
							checkall(count_click_ex);
							UpdateEx(UsageCode,DocNo,ID,0);

						}else {
							btn_ex.removeSclass("btn");
					        btn_ex.setSclass("btn btn-danger");
					        btn_ex.setAttribute("value_ex", "1");
							count_click_ex ++;
							checkall(count_click_ex);
							UpdateEx(UsageCode,DocNo,ID,1);

						}
			        }
			    });
		        cell_ex.appendChild(btn_ex);
				
		        
		        
				Button btn_re = new Button();
		        Listcell cell_re = new Listcell();
		        btn_re.setIconSclass("z-icon-registered");
		        
		        if(IsSterile == 1) {
			        btn_re.setSclass("btn btn-danger");

		        }else {
			        btn_re.setSclass("btn");
		        }
		        btn_re.setAttribute("ResterileTypeID", rs.getString("ResterileType"));
		        btn_re.addEventListener("onClick", new EventListener<Event>() {
						public void onEvent(Event event) throws Exception {
							
							callResterileType(cell_re, ID , btn_re ,UsageCode,DocNo ,ID);

				        }
				    });
		        cell_re.appendChild(btn_re);
		        
		        
//		        ========================================================
				Button btn_del = new Button();
		        Listcell cell_del = new Listcell();
		        btn_del.setIconSclass("z-icon-trash");
		        btn_del.setSclass("btn");
		        btn_del.addEventListener("onClick", new EventListener<Event>() {
						public void onEvent(Event event) throws Exception {
							
							Deleteitem(UsageCode,DocNo,ID);

				        }
				    });
			        
		        cell_del.appendChild(btn_del);
//	====================================================================
		        
		        
				final String S_ID = rs.getString("ID");
				final String S_ItemStockID = rs.getString("ItemStockID");
				final Checkbox chk = new Checkbox();
				
				chk.addEventListener("onClick", new EventListener<Event>() {
						public void onEvent(Event event) throws Exception {
							
							UpdateExp(UsageCode,DocNo,ID , chk);


				        }
				    });
				if(isCheck == "1") {
					chk.setChecked(true);
				}else {
					chk.setChecked(false);
				}
				
				if(ResterileType == 7) {
					chk.setChecked(true);
				}else {
					chk.setChecked(false);
				}
				System.out.println(isCheck);
		        Listcell cell_chk = new Listcell();
		        cell_chk.appendChild(chk);
		        
				list.appendChild(new Listcell(I_No + "."));
				list.appendChild(new Listcell(rs.getString("UsageCode")));				
				list.appendChild(new Listcell(rs.getString("itemname")));
				list.appendChild(cell_ex);				
				list.appendChild(cell_re);
				list.appendChild(cell_del);
				list.appendChild(cell_chk);


				list.setTooltiptext(rs.getString("UsageCode") + " " + rs.getString("itemname"));

				
				//Attribute
                list.setAttribute("ID", S_ID);
                list.setAttribute("ItemStockID", rs.getString("ItemStockID"));
                list.setAttribute("UsageCode", rs.getString("UsageCode"));

                Listbox_DocumentDetail.appendChild(list);
                
				Textbox_Document.setText(rs.getString("DocNo"));
				Textbox_SSDate.setText(rs.getString("DocDate"));
				
				
                I_No++;
                count++;

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
            
//            onProcess(false);
		}
		
		
		
		
		
    }

	private String getSqlSendSterileDetail(String S_DocNo) {
		
		String S_Sql = "";
		String item = "";
		String group = "";
		String sum = "";
		String ss = 	Combobox_type.getSelectedItem().getValue();
		if(ss.equals("1")) {
			 item = "itemstock.UsageCode,";
			 group = "GROUP BY itemstock.UsageCode";
			 sum = "sendsteriledetail.Qty,";
		}else {
			 item = "itemstock.ItemCode AS UsageCode,";
			 group = "GROUP BY itemstock.ItemCode";
			 sum = "SUM(sendsteriledetail.Qty) AS Qty,";
		}
		S_Sql =
			"	SELECT" + 
			"              sendsteriledetail.ID," + 
			"              sendsterile.DocNo," + 
			"              DATE( sendsterile.DocDate ) AS DocDate," + 
			"              sendsterile.IsHn," + 
			"              item.itemname," + 
			"              "+ item +"   " + 
			"              itemstock.IsRemarkExpress," + 
			"              sendsteriledetail.Remark," + 
			"              units.UnitName," + 
			"              sendsteriledetail.IsSterile," + 
			"              sendsteriledetail.ItemstockID," + 
			"              sendsteriledetail.ResterileType," + 
			"              sendsterile.IsWashDept," + 
			"              "+ sum +"   " + 
			"              hotpitalnumber.HnCode," + 
			"              hotpitalnumber.TitleName," + 
			"              hotpitalnumber.FName," + 
			"              hotpitalnumber.HnAge," + 
			"              hotpitalnumber.HnMonth," + 
			"              PERIOD_DIFF(" + 
			"                DATE_FORMAT( NOW(), '%Y%m' )," + 
			"              DATE_FORMAT( hotpitalnumber.CreateDate, '%Y%m' )) AS DiffMonth" + 
			"            FROM" + 
			"              sendsteriledetail" + 
			"              INNER JOIN itemstock ON sendsteriledetail.ItemstockID = itemstock.RowID" + 
			"              INNER JOIN item ON itemstock.ItemCode = item.itemcode" + 
			"              INNER JOIN units ON item.UnitID = units.ID" + 
			"              INNER JOIN sendsterile ON sendsterile.DocNo = sendsteriledetail.SendSterileDocNo" + 
			"              LEFT JOIN hncode ON hncode.DocNo_SS = sendsterile.DocNo" + 
			"              LEFT JOIN hotpitalnumber ON hncode.HnCode = hotpitalnumber.HnCode " + 
			"            WHERE" + 
			"              sendsterile.DocNo = '"+ S_DocNo +"'    " + 
			"              "+ group +"   " + 
			"              ";
		
		System.out.println(S_Sql);
	
		return S_Sql;
	
	}
//==========================================================================================

	
	public void Deleteitem(final String UsageCode , final String DocNo , final String ID) throws Exception{
		
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;
    	
		try{	
			
		       String sSql_update = "UPDATE itemstock SET IsDispatch = 0 , IsRemarkExpress = 0  WHERE UsageCode = '" + UsageCode + "'";
		       stmt.executeUpdate(sSql_update);
		       
		       String sSql_Delete = "DELETE FROM sendsteriledetail WHERE ID = '" + ID + "'";
		       stmt.executeUpdate(sSql_Delete);
		       
		        String Cnt = "";
				String sSql = " SELECT COUNT(UsageCode) AS cnt_usage FROM sendsteriledetail WHERE SendSterileDocNo = '" + DocNo + "' ";
				rs = stmt.executeQuery(sSql);
				while(rs.next()){
					Cnt =  rs.getString("cnt_usage");
				}
		        
		       String sSql_updateSend = " UPDATE sendsterile SET Qty = '" + Cnt + "' WHERE DocNo = '" + DocNo + "'  ";
		       stmt.executeUpdate(sSql_updateSend);
			
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
            
            
        	onDisplayDetail(DocNo, false);
        	onDisplayItemStock();
		}
    	
    	
	}
	
	public void UpdateEx(final String UsageCode , final String DocNo , final String ID , final int ex) throws Exception{
		
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;
    	
		try{	
			
		       String sSql_update = "UPDATE itemstock SET IsRemarkExpress = " + ex + "   WHERE UsageCode = '" + UsageCode + "'";
		       stmt.executeUpdate(sSql_update);
		       
				System.out.println(sSql_update);

			
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
            
            
        	onDisplayDetail(DocNo, false);
		}
    	
    	
	}
	
	public void UpdateExp(final String UsageCode , final String DocNo , final String ID , final Checkbox chk) throws Exception{
		
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;
    	
		try{	
			  if(chk.isChecked()) {
			       String sSql_update = "UPDATE sendsteriledetail SET IsSterile = 1 , ResterileType = 7   WHERE UsageCode = '" + UsageCode + "' AND SendSterileDocNo = '" + DocNo + "'";
			       stmt.executeUpdate(sSql_update);
					System.out.println(sSql_update);

			  }else {
			       String sSql_update = "UPDATE sendsteriledetail SET IsSterile = 0 , ResterileType = 1   WHERE UsageCode = '" + UsageCode + "' AND SendSterileDocNo = '" + DocNo + "'";
			       stmt.executeUpdate(sSql_update);
					System.out.println(sSql_update);
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
            
            
        	onDisplayDetail(DocNo, false);
		}
    	
    	
	}
	
	private void defineReSterileType() throws Exception{
		
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;
        
		try{	
			
			List<ModelMaster> list = new ArrayList<>();
			
			rs = stmt.executeQuery(getSqlReSterileType());
				
			while(rs.next()){
				list.add(
							new ModelMaster(
								rs.getString("ID"),
								rs.getString("ID"),
								rs.getString("ResterileType"),
								false
							)
						);
			}
			
			Model_ResterileType = list;
			
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
	
	private String getSqlReSterileType() {
		
		String S_Sql = "";
		
		S_Sql =
				"	SELECT 		resteriletype.ID, "
			+	"				resteriletype.ResterileType " 
						
			+	"	FROM 		resteriletype "
			
			+	"	WHERE 		IsCancel = 0 "
			
			+	"	ORDER BY 	resteriletype.ResterileType ASC ";
		
		return S_Sql;
	}
	
	private void callResterileType(final Listcell lc_opt, final String S_ID , final Button btnre , final String UsageCode , final String DocNo , final String ID) {
		

		
		
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
		
		
        Iterator li = Model_ResterileType.iterator();

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
		btn.setSclass("btn btn-primary");
		btn.addEventListener("onClick", new EventListener<Event>() {
			public void onEvent(Event event) throws Exception {	
				
				com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
		        Class.forName(c.S_MYSQL_DRIVER);
		        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
		        conn.setAutoCommit(false);
		        
		        Statement stmt = conn.createStatement();
		    	ResultSet rs = null;
		    	
				try {
					
					String S_ReSterileTypeName = cbb.getSelectedItem().getLabel();
					String S_ReSterileTypeId = (String)cbb.getSelectedItem().getValue();
						
					if(cbb.getSelectedIndex() <= 0) {
						lc_opt.setTooltiptext("");
						lc_opt.setAttribute("ResterileTypeID", null);
						
						// Update ReSterile Type ID
//						updateReSterileType(S_ID, null, "");
					       String sSql_update = "UPDATE sendsteriledetail " + 
					       		"          INNER JOIN itemstock ON sendsteriledetail.ItemStockID = itemstock.RowID " + 
					       		"            SET IsSterile = " + 
					       		"            CASE " + 
					       		"                " + 
					       		"                WHEN IsSterile = 0 THEN 1 " + 
					       		"                WHEN IsSterile = 1 THEN 0 " + 
					       		"              END," + 
					       		"              ResterileType = 1 " + 

					       		"              WHERE" + 
					       		"              itemstock.UsageCode = '" + UsageCode + "' " + 
					       		"              AND sendsteriledetail.SendSterileDocNo = '" + DocNo + "' ";
					       stmt.executeUpdate(sSql_update);
					       
							System.out.println(sSql_update);
							
						btnre.setSclass("btn");
			        	onDisplayDetail(DocNo, false);

					}else {
						lc_opt.setTooltiptext(S_ReSterileTypeName);
						lc_opt.setAttribute("ResterileTypeID", S_ReSterileTypeId);
						
					       String sSql_update = "UPDATE sendsteriledetail " + 
						       		"          INNER JOIN itemstock ON sendsteriledetail.ItemStockID = itemstock.RowID " + 
						       		"            SET IsSterile = " + 
						       		"            CASE " + 
						       		"                " + 
						       		"                WHEN IsSterile = 0 THEN 1 " + 
						       		"                WHEN IsSterile = 1 THEN 0 " + 
						       		"              END," + 
						       		"              ResterileType = '" + S_ReSterileTypeId + "'  " + 
						       		"              WHERE" + 
						       		"              itemstock.UsageCode = '" + UsageCode + "' " + 
						       		"              AND sendsteriledetail.SendSterileDocNo = '" + DocNo + "' ";
					       
					       stmt.executeUpdate(sSql_update);
						   System.out.println(sSql_update);
						   
						btnre.setSclass("btn btn-danger");
			        	onDisplayDetail(DocNo, false);
					}
					
					
		
				}catch(Exception e) {
					
					e.printStackTrace();
					
					lc_opt.setTooltiptext("");
					lc_opt.setAttribute("ResterileTypeID", null);
					
					btnre.setSclass("btn");
				}
				
				Window_PopUp.setVisible(false);
	        }
	    });
		
		vbx.appendChild(cbb);
		vbx.appendChild(new Separator());
		vbx.appendChild(new Separator());
		vbx.appendChild(btn);		
		
		Window_PopUp.appendChild(new Caption("บันทึก Resterile"));
		Window_PopUp.appendChild(vbx);
	}

	private void callitemage(final String itemcode) {
		
		Window_PopUp_image.getChildren().clear();
		Window_PopUp_image.setVisible(true);
		Window_PopUp_image.setFocus(true);
		Window_PopUp_image.setPosition("center");
		Window_PopUp_image.setMode("modal");
		Window_PopUp_image.setBorder("normal");
		Window_PopUp_image.setClosable(true);		
		Window_PopUp_image.setHeight("200px");
		Window_PopUp_image.setWidth("500px");
		
		Window_PopUp_image.appendChild(new Caption("รูปภาพ"));


	}
	
	
	
 	public void DeleteHN()throws Exception {
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
	        Class.forName(c.S_MYSQL_DRIVER);
	        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
	        conn.setAutoCommit(false);
	        
	        Statement stmt = conn.createStatement();
	    	ResultSet rs = null;
	    	
	    	String DocNo = Textbox_Document.getText();

	try {
		
	        String DocNoHN = "";
			String sSql = "SELECT" + 
					"          hncode.DocNo_SS," + 
					"          hncode.DocNo" + 
					"          FROM" + 
					"          sendsterile" + 
					"          LEFT JOIN hncode ON sendsterile.DocNo = hncode.DocNo_SS" + 
					"          WHERE hncode.DocNo_SS = '"+DocNo+"' " + 
					"          ORDER BY hncode.DocNo ASC" + 
					"          LIMIT 1 ";
			rs = stmt.executeQuery(sSql);
			while(rs.next()){
				DocNoHN =  rs.getString("DocNo");
			}
			
			String Delete ="DELETE FROM hncode WHERE hncode.DocNo = '"+DocNoHN+"' ";
			stmt.executeUpdate(Delete);

			String update ="UPDATE sendsterile SET sendsterile.IsHn = 0  WHERE sendsterile.DocNo = '"+DocNo+"' ";
			stmt.executeUpdate(update);
			
		}catch (Exception e) {
			e.printStackTrace();
			Messagebox.show("ERROR onSaveHn : " + e.getMessage());
			System.out.println("ERROR onSaveHn : " + e.getMessage());
		}finally {
			((Window) getFellow("Window_PopUp_hn")).setMode("embedded");
			((Window) getFellow("Window_PopUp_hn")).setVisible(false);
			Checkbox_hn.setChecked(false);
//			DepartmentSendSterile2 filesend = new DepartmentSendSterile2();
//			filesend.Checkbox_hn = Checkbox_hn;
		}

}
 	
 	public void successHN()throws Exception {


	try {
		
		 Messagebox.show("บันทึกสำเร็จ", "CSSD", Messagebox.OK, Messagebox.INFORMATION);

			
		}catch (Exception e) {
			e.printStackTrace();
			Messagebox.show("ERROR onSaveHn : " + e.getMessage());
			System.out.println("ERROR onSaveHn : " + e.getMessage());
		}finally {
        		onDisplayDocument();
				Listbox_DocumentDetail.getItems().clear();
	        	Textbox_Document.setText("");
	        	S_DocNo = null;
	        	Textbox_SSDate.setText("");
				Checkbox_hn.setChecked(false);
			((Window) getFellow("Window_PopUp_hn_detail")).setMode("embedded");
			((Window) getFellow("Window_PopUp_hn_detail")).setVisible(false);

		}

}
	
}
