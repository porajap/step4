package core;

import java.sql.Connection;
import java.sql.Date;

/*	
 *  -- ============================================================
 -- Author		:	PARADOX (A)
 -- Create date	:	29-01-2016
 -- Update By	:	PARADOX (A)
 -- Update date	:   29-01-2016
 -- Description	:	To Manage Main Project
 -- ============================================================
 */

import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
//import java.util.ArrayList;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.A;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.West;
import org.zkoss.zul.Window;
import org.zkoss.zul.Tabbox;

import connect.Conn;
import connect.OperationData;
import general.DateTime;
import general.Number;

@SuppressWarnings("rawtypes")
public class Mainnn extends GenericForwardComposer{

	/**
	 * 
	 */
	
	private static final long serialVersionUID = -5130708574175242573L;
	
	// Variable Component
	private Borderlayout Main;
	private Tabbox TabboxMain;
	private Tabbox Tabbox;
	private Tabs Tabs;
	private Tabpanels Tabpanels;
	
	private Listbox ListboxDocumentForApprove;
	private Listbox ListboxManufacturingRawmat;
	private Listbox ListboxManufacturingCostValue;
	private Listbox ListboxManufacturingFinishGoods;
	private Listbox ListboxManufacturingStatus;
	private Listbox ListboxPurchaseRequest;
	
	//private Borderlayout BorderlayoutApprove;
	
	private Panel PanelApprove;
	private Panel PanelIsManufacturingStatus;
	private Panel PanelIsManufacturingCostValue;
	private Panel PanelIsManufacturingFinishGoods;
	private Panel PanelIsManufacturingPrint;
	
	private Textbox TextboxDocSearch;
	private Datebox DateboxSDocDate;
	private Datebox DateboxEDocDate;
	
	private Textbox TextboxDocSearch_;
	private Datebox DateboxSDocDate_;
	private Datebox DateboxEDocDate_;
	
	private Toolbarbutton TbbBranch;
	private Toolbarbutton TbbUser;
	
	private org.zkoss.zul.Caption Caption2;
	private West west;

	private Window WinProcess;
	
	private Date SDate;
	private Date EDate;
	
	// Variable
    private String LinkURL;
    private String ApplicationName;
    
    private boolean is_order_by_2 = false;
	
	// Event
	public void onClick$TbbLogout(Event event) throws Exception {
		onLogout(); 
	}
	
	public void onClick$TbbRefresh(Event event) throws Exception {
		refresh(); 
	}
	
	public void onOK$TextboxDocSearch(Event event) throws Exception {
		if( IsApprove )
			onDisplayDocumentForApprove();
	}
	
	public void onClick$ButtonSearchDocApprove(Event event) throws Exception {
		if( IsApprove )
			onDisplayDocumentForApprove(); 
	}
	
	public void onOK$TextboxDocSearch_(Event event) throws Exception {
		onDisplayPR();
	}
	
	public void onClick$ButtonSearchDocPR_(Event event) throws Exception {
		onDisplayPR();
	}
	
	public void onTimer$Time(Event event) throws Exception {
		refresh();
	}
	
	public void onClick$Caption2(Event event) throws Exception {
		is_order_by_2 = !is_order_by_2;
		
		Caption2.setImage(is_order_by_2 ? "/images/sort-ascend.png" : "/images/sort-descend.png");
		
		onDisplayTransmit(ListboxManufacturingRawmat, 1, 250);
	}
	
	// Variable System
	private String SessionUserCode;
	private boolean BooleanCreate = true;
    private Session session = Sessions.getCurrent();
    private String xUsername = null;
	private String xPassword = null;
	
	//Variable Local
	private boolean IsApprove = false;
	private boolean IsManufacturingStatus = false;
	private boolean IsManufacturingCostValue = false;
	private boolean IsManufacturingFinishGoods = false;
	private boolean IsManufacturingPrint = false;
			
	private String Branch_Code = null;
	 
	public void onCreate() throws Exception {
		if (BooleanCreate) {
			if (session.getAttribute("UserCode") == null) {
				Executions.sendRedirect("/Login.zul");
			} else {
				Main.setVisible(true);
				
				SessionUserCode = session.getAttribute("UserCode").toString();
				xUsername = (String)session.getAttribute("xUsrName");
		    	xPassword = (String)session.getAttribute("xPassword");
		    	
				init();
	        }
			BooleanCreate = false;
		}
    }
	
	
	private EventQueue<Event> qe;

	@SuppressWarnings("unchecked")
	public void doAfterCompose(org.zkoss.zk.ui.Component comp) throws Exception {
		super.doAfterCompose(comp); 
		qe = EventQueues.lookup(MyConsts.EVENTQUEUE_CONNECTION, EventQueues.DESKTOP, true);
		qe.subscribe(new EventListener<Event>() {
			public void onEvent(Event event) throws Exception {
				//System.out.println("Event = " + event.getName());
				if(MyConsts.EVENT_CLOSETAB.equals(event.getName())){
					onCloseTab();
					//System.out.println("Close2");
				}else if(MyConsts.EVENT_REFRESH_POLTAL.equals(event.getName())){
					refresh();
					//System.out.println("Refresh2");
				}
			}
		});
	}
    
	// Close Tab
    public void onCloseTab() throws Exception {
        if (Tabbox.getSelectedIndex() != 0 ) {
            Tabbox.getSelectedTab().close();     
        }
    }
    
    public void refresh() throws Exception {
    	displayWindow();
    }
	
	public void onLogout(){
		Messagebox.show("ยืนยันการออกจากระบบ?", " ", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,new EventListener<Event>() {
    		public void onEvent(Event evt) throws Exception {
    			switch (((Integer) evt.getData()).intValue()) {
                  	case Messagebox.YES:
                  		try{
                			new OperationData(
                					xUsername,
            						xPassword,
                		   	 		"Insert",
                					"login_log", 
                					new String[][] {
                		   	 			{"Login_Code", "'" + SessionUserCode + "'" },
                		   	 			{"date", "CURDATE()" },
                		   	 			{"time", "CURTIME()" },
                		   	 			{"mode", "0" },
                					},
                					null
                				);
                		}catch(Exception e){
                			e.printStackTrace();
                		}finally{
                			Executions.sendRedirect("/Login.zul");
                		}
                  		break;
                  	case Messagebox.NO:
                  		break;
    			}
    		}
      });
	}

	public void init() throws Exception{
		createMenu();
		
        //String Branch_Code = (String)session.getAttribute("Branch_Code");
        String Branch_Name = (String)session.getAttribute("Branch_Name");
        String Branch_ID = (String)session.getAttribute("Branch_ID"); 
		 
        TbbBranch.setLabel(
    		" รหัสสาขา : " + Branch_ID +
    		" สาขา : " + Branch_Name
    	);
        
		TbbUser.setLabel(
			"ผู้เข้าใช้งาน : " + session.getAttribute("UserName").toString()
		);
			
		
		DateboxSDocDate.setText(DateTime.getDateLastMonth());
		DateboxEDocDate.setText(DateTime.getDateNow());
		
		DateboxSDocDate_.setText(DateTime.getDateLastMonth());
		DateboxEDocDate_.setText(DateTime.getDateNow());
		
		// Display Document For Approve
		//System.out.println(getPermission( (String) session.getAttribute("UserCode") ));
		
		getPermission( (String) session.getAttribute("UserCode") );

		displayWindow();

		onDisplayPR();
	}
	
	private void displayWindow() throws Exception{
		if( IsApprove ){
    		onDisplayDocumentForApprove();
		}

		if( IsManufacturingCostValue ){
			//coding by Start 07222020
			//onDisplayTransmit(ListboxManufacturingRawmat, 1, 250);
			onDisplayTransmit(ListboxManufacturingRawmat, 1, 250);
		}
		
		if(IsManufacturingPrint){
			onDisplayTransmit(ListboxManufacturingCostValue, 2, 250);
		}
		
		if(IsManufacturingFinishGoods){
			onDisplayTransmit(ListboxManufacturingFinishGoods, 3, 100);
		}
		
		if(IsManufacturingStatus){
			//coding by Start 07222020
			//onDisplayTransmit(ListboxManufacturingRawmat, 1, 300);
			onDisplayTransmitStatus(ListboxManufacturingStatus, 300);
		}
		

	}
	
	// ================================= Manufacturing RawMaterial ========================================
	
    public String getSqlTransmit( int Display_Status, int Limit , String Sort){
    	String Sql = 	"SELECT " +	
									"wh_stock_transmit_iso.DocNo," +
									"DATE_FORMAT(wh_stock_transmit_iso.DocDate ,'%d-%m-%Y') AS DocDate, " + 
								  	//"wh_stock_transmit_iso.Total," +
								  	"wh_stock_transmit_iso.Detail," +
								  	"wh_stock_transmit_iso.Status," +
								  	"wh_stock_transmit_iso.Modify_Date," +
								  	"wh_stock_transmit_iso.Manufacturing_Name," +
								  	"wh_stock_transmit_iso.Manufacturing_Value," +
								  	"wh_stock_transmit_iso.Manufacturing_Unit," +
								  	"wh_stock_transmit_iso.Manufacturing_LotNo," +
								  	"wh_stock_transmit_iso.Display_Status," +
								  	"DATE_FORMAT(wh_stock_transmit_iso.MFG_Date ,'%d-%m-%Y') AS MFG_Date, " + 
								  	"DATE_FORMAT(wh_stock_transmit_iso.EXP_Date ,'%d-%m-%Y') AS EXP_Date, " + 
								  	"DATE_FORMAT(wh_stock_transmit_iso.Print_Date ,'%d-%m-%Y') AS Print_Date, " + 
								  	"DATE_FORMAT(wh_stock_transmit_iso.Receive_Date ,'%d-%m-%Y') AS Receive_Date, " + 

								  	"item_unit.Unit_Name " +
					
						"FROM 		wh_stock_transmit_iso " +

						"LEFT JOIN 	item_unit " +
						"ON			item_unit.Unit_Code = wh_stock_transmit_iso.Manufacturing_Unit " +

						"WHERE 		wh_stock_transmit_iso.Status <> '4' " +
						
    					"AND		Branch_Code = " + Branch_Code + " ";
    	
    			
		if(Display_Status > 0)		
			Sql += "AND 	wh_stock_transmit_iso.Display_Status = " + Display_Status + " ";	

		Sql += "ORDER BY 	wh_stock_transmit_iso.DocNo " + Sort + " LIMIT " + Limit + " ";
		
//		System.out.println(Sql);
		
		return Sql;
		
    }
 
    public String getSqlTransmit2( int Display_Status, int Limit , String Sort){
    	String Sql = 	"SELECT " +	
									"wh_stock_transmit_iso.DocNo," +
									"DATE_FORMAT(wh_stock_transmit_iso.DocDate ,'%d-%m-%Y') AS DocDate, " + 
								  	//"wh_stock_transmit_iso.Total," +
								  	"wh_stock_transmit_iso.Detail," +
								  	"wh_stock_transmit_iso.Status," +
								  	"DATEDIFF(NOW(),wh_stock_transmit_iso.Modify_Date) AS diffff, " +
								  	"wh_stock_transmit_iso.Modify_Date," +
								  	"wh_stock_transmit_iso.Manufacturing_Name," +
								  	"wh_stock_transmit_iso.Manufacturing_Value," +
								  	"wh_stock_transmit_iso.Manufacturing_Unit," +
								  	"wh_stock_transmit_iso.Manufacturing_LotNo," +
								  	"wh_stock_transmit_iso.Display_Status," +
								  	"DATE_FORMAT(wh_stock_transmit_iso.MFG_Date ,'%d-%m-%Y') AS MFG_Date , " + 
								  	"DATE_FORMAT(wh_stock_transmit_iso.EXP_Date ,'%d-%m-%Y') AS EXP_Date, " + 
								  	"DATE_FORMAT(wh_stock_transmit_iso.Print_Date ,'%d-%m-%Y') AS Print_Date, " + 
								  	"DATE_FORMAT(wh_stock_transmit_iso.Receive_Date ,'%d-%m-%Y') AS Receive_Date, " + 
								  	"item_unit.Unit_Name " +					
						"FROM 		wh_stock_transmit_iso " +
						"LEFT JOIN 	item_unit " +
						"ON			item_unit.Unit_Code = wh_stock_transmit_iso.Manufacturing_Unit " +										
    					"WHERE		Branch_Code = " + Branch_Code + " ";
						
		if(Display_Status > 0)	
			Sql += "AND wh_stock_transmit_iso.`Status` != 4	" ;
			Sql += "AND CASE WHEN wh_stock_transmit_iso.Display_Status = " + Display_Status + " THEN DATEDIFF(NOW(),wh_stock_transmit_iso.Modify_Date) < 3 ";	
			Sql += "WHEN wh_stock_transmit_iso.Display_Status <> 4 THEN DATEDIFF(NOW(),wh_stock_transmit_iso.Modify_Date) >= 0  END ";
			Sql += "ORDER BY 	wh_stock_transmit_iso.DocNo " + Sort + " LIMIT " + Limit + " ";
		
		System.out.println(Sql);

		return Sql;
    }
    
    private boolean checkCreateBillTransmitISO() throws Exception{	
    	Conn c = new Conn();
        Class.forName(c.DOX_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(), c.DOX_USER, c.DOX_PASSWORD);
        conn.setAutoCommit(false);
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;
        
		try{	
			
			String sql =
					"SELECT 1 " + 					
					"FROM 		menu_permission " +							
					"WHERE		menu_permission.Login_Code = '"+ SessionUserCode +"' " +	
					"AND		menu_permission.menu_code = '0411' " +	
					"LIMIT 1 ";
			
			rs = stmt.executeQuery( sql );
			
			/*
			rs = sqlsel.getReSultSQL(
				"SELECT 1 " + 					
				"FROM 		menu_permission " +							
				"WHERE		menu_permission.Login_Code = '"+ SessionUserCode +"' " +	
				"AND		menu_permission.menu_code = '0411' " +	
				"LIMIT 1 "
			);
			*/
			
			return rs.next();
			
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

    
   
	public void onDisplayTransmitStatus(final Listbox listbox, int Limit) throws Exception{
    	/*
    	ResultSet rs = null;
        SqlSelection sqlsel = new SqlSelection();
        sqlsel.uName = xUsername;
        sqlsel.Pwd = xPassword;
        */
        
        Conn c = new Conn();
        Class.forName(c.DOX_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(), c.DOX_USER, c.DOX_PASSWORD);
        conn.setAutoCommit(false);
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;

    	try{
    		rs = stmt.executeQuery( getSqlTransmit2(4, Limit, "DESC") );		
			//rs = sqlsel.getReSultSQL(getSqlTransmit(-1, Limit, "DESC"));
			
    		listbox.getItems().clear();
    		
    		String Status = "";

			while(rs.next()){
				Listitem list = new Listitem();

				Listcell lc = new Listcell();
				
				
				
				final int d_status = rs.getInt("Display_Status");
				final String DocNo = rs.getString("DocNo");
				
				//System.out.println(d_status + "-" + rs.getString("Manufacturing_Name"));
				
				// Status = 0 and permission menu transmit ISO
				if( d_status == 0 && checkCreateBillTransmitISO() ){
					A a_mname = new A(rs.getString("Manufacturing_Name"));
					
					a_mname.addEventListener("onClick", new EventListener<Event>() {
						public void onEvent(Event event) throws Exception {
							try{
								String TabName = "Step 1 : ร่างเอกสารรายการ เบิกจ่าย.";
								String URL = "StockOut_ISO.zul?docno=" + DocNo;
								
								newTab(TabName, URL );
								
							}catch(Exception e){
								return;
							}
				        }
				    });
					
					lc.appendChild(a_mname);
				
				}else{
					lc.setLabel(rs.getString("Manufacturing_Name"));
				}
				
				list.appendChild(lc);
				
				//lc.setStyle("color:#009900;");
				
				lc.setTooltiptext(
						"เอกสาร : " + DocNo + "\n" +
						"วันที่ : " + rs.getString("DocDate") + "\n" +
						"ผลิต  : " + rs.getString("MFG_Date") + "\n" +
						"หมดอายุ  : " + rs.getString("EXP_Date") + "\n" +
						"รายละเอียด  : " + rs.getString("Detail") + "\n" +
						"วันที่พิมพ์  : " + txtNull(rs.getString("Print_Date")) + "\n" +
						"วันที่รับเข้าคลัง  : " + txtNull(rs.getString("Receive_Date")) + "\n" +
						"Status  : " + txtNull(rs.getString("Status")) + "\n" +
						"Display_Status  : " + txtNull(rs.getString("Display_Status"))
				);

				list.appendChild(lc);
				
				list.appendChild(new Listcell(Number.addComma2d(rs.getString("Manufacturing_Value")) + " " + rs.getString("Unit_Name")));
				list.appendChild(new Listcell(rs.getString("Manufacturing_LotNo")));

				// Status
				Hlayout h = new Hlayout();
				
				if( d_status == 0 ){
					h.appendChild(new Image("/images/y.gif"));
					h.appendChild(new Image("/images/y.gif"));
					h.appendChild(new Image("/images/y.gif"));
					h.appendChild(new Image("/images/y.gif"));
					
					Status = "Step 1 : ร่างเอกสารรายการ เบิกจ่าย.";
							
				}if( d_status == 1 ){
					h.appendChild(new Image("/images/g.png"));
					h.appendChild(new Image("/images/y.gif"));
					h.appendChild(new Image("/images/y.gif"));
					h.appendChild(new Image("/images/y.gif"));

					Status = "Step 2 : ขั้นตอนการ บันทึก จำนวนการผลิต และ ต้นทุนการผลิต.";
							
				}if( d_status == 2 ){
					h.appendChild(new Image("/images/g.png"));
					h.appendChild(new Image("/images/g.png"));
					h.appendChild(new Image("/images/y.gif"));
					h.appendChild(new Image("/images/y.gif"));

					Status = "Step 3 : ตรวจสอบ และ พิมพ์ ข้อมูลการผลิต.";
							
				}if( d_status == 3 ){
					h.appendChild(new Image("/images/g.png"));
					h.appendChild(new Image("/images/g.png"));
					h.appendChild(new Image("/images/g.png"));
					h.appendChild(new Image("/images/y.gif"));

					Status = "Step 4 : ขั้นตอนการ เตรียมรับสินค้าที่ผลิตเข้าคลังสินค้า.";
							
				}if( d_status == 4 ){
					h.appendChild(new Image("/images/g.png"));
					h.appendChild(new Image("/images/g.png"));
					h.appendChild(new Image("/images/g.png"));
					h.appendChild(new Image("/images/g.png"));

					Status = "Step 5 : รับสินค้าที่ผลิตเข้าคลังสินค้าเรียบร้อย.";
							
				}
				
				Listcell lc_status = new Listcell();
				lc_status.appendChild(h);
				
				lc_status.setTooltiptext(Status);
				
				list.appendChild(lc_status);
				
                listbox.appendChild(list);

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
    
    public void onDisplayTransmit(final Listbox listbox, final int Display_Status, int Limit) throws Exception{
    	/*
    	ResultSet rs = null;
        SqlSelection sqlsel = new SqlSelection();
        sqlsel.uName = xUsername;
        sqlsel.Pwd = xPassword;
        */
    	
    	Conn c = new Conn();
        Class.forName(c.DOX_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(), c.DOX_USER, c.DOX_PASSWORD);
        conn.setAutoCommit(false);
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;
        
		try{	
			
			rs = stmt.executeQuery(getSqlTransmit(Display_Status, Limit, (is_order_by_2 ? "ASC" : "DESC")));
			
			//rs = sqlsel.getReSultSQL(getSqlTransmit(Display_Status, Limit, (is_order_by_2 ? "ASC" : "DESC")));
			
			ResultSetMetaData metaData = rs.getMetaData();
            
    		int count = metaData.getColumnCount();
    		
    		ArrayList<String> a = new ArrayList<String>();
    		
    		for (int i = 1; i <= count; i++){
    			a.add(metaData.getColumnName(i)); 
    		}
    		
    		listbox.getItems().clear();
			

			while(rs.next()){
				final String DocNo = rs.getString("DocNo");
				Listitem list = new Listitem();

				Listcell lc_DocNo = new Listcell();
				A a_docno = new A(DocNo);
				
				a_docno.addEventListener("onClick", new EventListener<Event>() {
					public void onEvent(Event event) throws Exception {
						try{
							String TabName = "";
							String URL = "";
							
							if(Display_Status == 1){
								TabName = "บันทึก จำนวนการผลิต และ ต้นทุนการผลิต";
								URL = "StockOut_ISO.zul?docno=" + DocNo + "&display_status=" + Display_Status;
							}else if(Display_Status == 2){
								TabName = "ตรวจสอบ และ พิมพ์ ข้อมูลการผลิต";
								URL = "StockOut_ISO.zul?docno=" + DocNo + "&display_status=" + Display_Status;
							}else if(Display_Status == 3){
								TabName = "รับเข้าจากการผลิต";
								URL = "StockReceive.zul?mode=1&prodtion_docno=" + DocNo + "&display_status=" + Display_Status;
							}else {
								return;
							}
							
							newTab(TabName, URL );
							
						}catch(Exception e){
							return;
						}
			        }
			    });
				
				lc_DocNo.appendChild(a_docno);
				list.appendChild(lc_DocNo);
	
				list.appendChild(new Listcell(rs.getString("DocDate")));
				list.appendChild(new Listcell(rs.getString("Manufacturing_Name")));
				list.appendChild(new Listcell(Number.addComma2d(rs.getString("Manufacturing_Value")) + " " + rs.getString("Unit_Name")));
				list.appendChild(new Listcell(rs.getString("Manufacturing_LotNo")));
				
				//Attribute
                for(int i=0; i<a.size(); i++)
					list.setAttribute(a.get(i), rs.getString(a.get(i)));

                listbox.appendChild(list);

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
	
    // ================================= Document For Approve ========================================
    public String getSqlPurchaseRequest( String code ){
    	String Sql =	"SELECT " +	
		    						"purchaserequest.RowId," + 
		    						"purchaserequest.DocNo," + 
		    						"DATE_FORMAT(DocDate ,'%d-%m-%Y') AS DocDate, " + 
		    						"purchaserequest.PRT_Code," + 
		    						"pr_type.PRT_Name," + 
		    						"purchaserequest.RefDocNo," + 
		    						"purchaserequest.Sp_Code," + 
		    						"CONCAT(supplier.Sp_Code ,' : ', supplier.Sp_Name) AS Supplier," +
		    						"purchaserequest.Employee_Code," +
		    						"CONCAT(login.FName, ' ', login.LName) AS Employee," +
		    						"purchaserequest.Branch_Code," + 
		    						"purchaserequest.Modify_Code," + 
		    						"purchaserequest.Modify_Date," + 
		    						"purchaserequest.TT_Code," + 
		    						"purchaserequest.SubTotal," + 
		    						"purchaserequest.DiscountPercent," + 
		    						"purchaserequest.Discount," + 
		    						"purchaserequest.Total," + 
		    						"purchaserequest.Tax," + 
		    						"purchaserequest.NetTotal," + 
		    						"purchaserequest.Doc_Status," + 
		    						"purchaserequest.IsCancel," + 
		    						"purchaserequest.IsApprove," +
		    						"purchaserequest.DocNote," +
		    						"(SELECT COUNT(*) FROM purchaserequest_detail WHERE purchaserequest_detail.DocNo = purchaserequest.DocNo AND purchaserequest_detail.IsCancel = 0 ) AS item_count,"  +
		    						"(SELECT COUNT(*) FROM purchaserequest_detail WHERE purchaserequest_detail.DocNo = purchaserequest.DocNo AND purchaserequest_detail.IsCancel = 0 AND purchaserequest_detail.IsApprove = 1) AS approve_count "  +

						"FROM 		purchaserequest " +
						
						"LEFT JOIN	supplier " +
						"ON			supplier.Sp_Code = purchaserequest.Sp_Code " +
									
						"LEFT JOIN	pr_type " +
						"ON			pr_type.PRT_Code = purchaserequest.PRT_Code " +
									
						"LEFT JOIN	login " +
						"ON			login.Login_Code = purchaserequest.Employee_Code " +

						"WHERE 		purchaserequest.Branch_Code = " + Branch_Code + " " +
						
						"AND 		purchaserequest.Doc_Status = 1 " ;
					
						if(code != null){
							Sql += 	"AND	purchaserequest.DocNo = '" + code + "' ";
						}else {
	
							String txt = TextboxDocSearch.getText();
										
							if(! txt.equals("") ){
								Sql += 	"AND (" +
										"	supplier.Sp_Code LIKE '%" + txt.replace(" ", "%") + "%' " + 
										"OR	supplier.Sp_Name LIKE '%" + txt.replace(" ", "%") + "%' " +
										"OR purchaserequest.DocNo LIKE '%" + txt.replace(" ", "%") + "%' " +
										") ";
							}
										
							if( DateboxSDocDate.getText().length() == 10 && DateboxEDocDate.getText().length() == 10 ){
								Sql += 	"AND 	purchaserequest.DocDate between '" + DateTime.convertDate( DateboxSDocDate.getText() ) + "' AND '" + DateTime.convertDate( DateboxEDocDate.getText() ) + "' " ;
							}	
										
						}
									
		Sql += 	"ORDER BY purchaserequest.DocNo ASC LIMIT 100 ";
									
		//System.out.println(Sql);
							
		return Sql;
    }
    
    public void onDisplayDocumentForApprove() throws Exception{
    	Conn c = new Conn();
        Class.forName(c.DOX_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(), c.DOX_USER, c.DOX_PASSWORD);
        conn.setAutoCommit(false);
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;
    	
    	/*
    	ResultSet rs = null;
        SqlSelection sqlsel = new SqlSelection();
        sqlsel.uName = xUsername;
        sqlsel.Pwd = xPassword;
        */

		try{	
			
			rs = stmt.executeQuery( getSqlPurchaseRequest(null) );
			
			//rs = sqlsel.getReSultSQL(getSqlPurchaseRequest(null));
			
			ResultSetMetaData metaData = rs.getMetaData();
    		int count = metaData.getColumnCount();
    		ArrayList<String> a = new ArrayList<String>();
    		
    		for (int i = 1; i <= count; i++){
    			a.add(metaData.getColumnName(i)); 
    		}
    		
    		ListboxDocumentForApprove.getItems().clear();
			
			int row = 1;
			
			while(rs.next()){
				final String DocNo = rs.getString("DocNo");
				Listitem list = new Listitem();
				list.appendChild(new Listcell(row + "."));
				
				Listcell lc_DocNo = new Listcell();
				A a_docno = new A(DocNo);
				
				a_docno.addEventListener("onClick", new EventListener<Event>() {
					public void onEvent(Event event) throws Exception {
						if(checkApprove(DocNo))
							newTab("อนุมัติใบขอซื้อ", "PurchaseRequest.zul?docno=" + DocNo + "&mode=approve" );
						else
							onDisplayDocumentForApprove();
			        }
			    });
				
				lc_DocNo.appendChild(a_docno);
				
				list.appendChild(lc_DocNo);
				list.appendChild(new Listcell(rs.getString("DocDate")));
				list.appendChild(new Listcell(rs.getString("Employee")));
				list.appendChild(new Listcell(rs.getString("item_count")));
				list.appendChild(new Listcell(rs.getString("approve_count")));
				
				
				list.appendChild(new Listcell( "" + (rs.getInt("item_count") - rs.getInt("approve_count") ) ));
				list.appendChild(new Listcell(general.Number.addComma2d(rs.getString("Total"))));
				list.appendChild(new Listcell(rs.getString("DocNote")));

				//Attribute
                for(int i=0; i<a.size(); i++)
					list.setAttribute(a.get(i), rs.getString(a.get(i)));

				ListboxDocumentForApprove.appendChild(list);
				
				row++;
			}
	
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
			/*
			sqlsel.closeConnection();

            if (rs != null) {
                rs.close();
            }
            */
			
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
    
    public String getSqlPurchaseRequest(){
    	
    	String Sql =	"SELECT 	purchaserequest.DocNo," + 
									"DATE_FORMAT(DocDate ,'%d-%m-%Y') AS DocDate, " + 									
									"purchaserequest.Doc_Status," + 									
									"purchaserequest.DocNote," +
									"(SELECT COUNT(*) FROM purchaserequest_detail WHERE purchaserequest_detail.DocNo = purchaserequest.DocNo AND purchaserequest_detail.IsCancel = 0 ) AS item_count,"  +
									"(SELECT COUNT(*) FROM purchaserequest_detail WHERE purchaserequest_detail.DocNo = purchaserequest.DocNo AND purchaserequest_detail.IsCancel = 0 AND purchaserequest_detail.IsApprove = 1) AS approve_count "  +
					
						"FROM 		purchaserequest " +
						
						"LEFT JOIN	supplier " +
						"ON			supplier.Sp_Code = purchaserequest.Sp_Code " +
	
						"WHERE 		purchaserequest.Branch_Code = " + Branch_Code + " ";
    	
    	Sql += 			"AND		purchaserequest.Employee_Code = '" + SessionUserCode + "' "; 
						
					
		String txt = TextboxDocSearch_.getText();
										
		if(! txt.equals("") ){
			Sql += 	"AND (" +
										"	supplier.Sp_Code LIKE '%" + txt.replace(" ", "%") + "%' " + 
										"OR	supplier.Sp_Name LIKE '%" + txt.replace(" ", "%") + "%' " +
										"OR purchaserequest.DocNo LIKE '%" + txt.replace(" ", "%") + "%' " +
										") ";
		}
										
		if( DateboxSDocDate_.getText().length() == 10 && DateboxEDocDate_.getText().length() == 10 ){
			Sql += 	"AND 	purchaserequest.DocDate between '" + DateTime.convertDate( DateboxSDocDate_.getText() ) + "' AND '" + DateTime.convertDate( DateboxEDocDate_.getText() ) + "' " ;
		}	
													
									
		Sql += 	"ORDER BY purchaserequest.DocNo ASC LIMIT 100 ";
		
		//System.out.println(Sql);
							
		return Sql;
    }
    
    public void onDisplayPR() throws Exception{
    	
		Conn c = new Conn();
	    Class.forName(c.DOX_MYSQL_DRIVER);
	    Connection conn = java.sql.DriverManager.getConnection(c.getHost(), c.DOX_USER, c.DOX_PASSWORD);
	    conn.setAutoCommit(false);
	    Statement stmt = conn.createStatement();
    	
    	ResultSet rs = null;
        
        try{	
        	
        	rs = stmt.executeQuery(getSqlPurchaseRequest());
			ResultSetMetaData metaData = rs.getMetaData();
    		int count = metaData.getColumnCount();
    		ArrayList<String> a = new ArrayList<String>();
    		
    		for (int i = 1; i <= count; i++){
    			a.add(metaData.getColumnName(i)); 
    		}
    		
			ListboxPurchaseRequest.getItems().clear();
			
			int row = 1;
			
			while(rs.next()){
				final String DocNo = rs.getString("DocNo");
				Listitem list = new Listitem();
				list.appendChild(new Listcell(row + "."));
				
				Listcell lc_DocNo = new Listcell();
				A a_docno = new A(DocNo);
				
				a_docno.addEventListener("onClick", new EventListener<Event>() {
					public void onEvent(Event event) throws Exception {
						newTab("อนุมัติใบขอซื้อ", "PurchaseRequest.zul?docno=" + DocNo + "&mode=search" );
			        }
			    });
				
				lc_DocNo.appendChild(a_docno);
				
				list.appendChild(lc_DocNo);
				list.appendChild(new Listcell(rs.getString("DocDate")));
				list.appendChild(new Listcell(rs.getString("item_count")));
				list.appendChild(new Listcell(rs.getString("approve_count")));
				list.appendChild(new Listcell( Integer.toString( rs.getInt("item_count")-rs.getInt("approve_count") ) ));
				list.appendChild(new Listcell(rs.getString("DocNote")));

				//Doc_Status
				String Color ="";
				String Doc_Status = "";
				
				switch(rs.getInt("Doc_Status")){
				case 0: Doc_Status = "ร่าง";      Color = "color:#0000FF"; break;
				case 1: Doc_Status = "รออนุมัติ"  ; Color = "color:#333333"; break;
				case 2: Doc_Status = "Complete"; Color = "color:#333333"; break;
				case 3: Doc_Status = "ปิดเอกสาร"; Color = "color:#009900"; break;
				case 4: Doc_Status = "ยกเลิกเอกสาร";  Color = "color:#FF0000"; break;
					default : break;
				}
				
				
				Listcell lc = new Listcell(Doc_Status);
				lc.setStyle("font-weight:bold;" + Color);
				list.appendChild(lc);


                ListboxPurchaseRequest.appendChild(list);
				
				row++;
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
    
    private boolean checkApprove(String DocNo) throws Exception{
    	Conn c = new Conn();
        Class.forName(c.DOX_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(), c.DOX_USER, c.DOX_PASSWORD);
        conn.setAutoCommit(false);
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;
    	 
    	String Sql = 	
				"SELECT 	RowId " +			
				"FROM 		purchaserequest " +
				"WHERE 		DocNo = '" + DocNo +"' " +
    			"AND		Doc_Status = 1 " +
    			"LIMIT 1 ";
		
		try{	
			rs = stmt.executeQuery( Sql );
			
			//rs = sqlsel.getReSultSQL(Sql);
    		
			return rs.next();
			
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
    
    private String getSqlMenuUsers(String MenuGCode){
		return 	"SELECT  	menu_permission.permission_code," + 
							"menu_permission.Login_Code," +
							"menu_permission.menu_code," +
							"menu.menu_code," +  
							"menu.menu_no," + 
							"menu.menu_name," + 
							"menu.menu_path," + 
							"menu.menu_order," +
							"menu.menu_image " + 
								
				"FROM 		menu_permission " +
																	
				"INNER JOIN	menu " +
				"ON			menu.menu_code = menu_permission.menu_code " +
											
				"WHERE		menu_permission.Login_Code = '"+ SessionUserCode +"' " +
				
				"AND		menu.mm_code = '"+ MenuGCode +"' " +
				
				"ORDER BY 	menu.menu_order ASC ";
	}
	
	private String getSqlMenuGroups(){
		String StrSQL = "SELECT     main_menu.mm_code," +
								   "main_menu.mm_name " +
				
						"FROM 		menu_permission " +
						
						"INNER JOIN	menu " +
						"ON			menu.menu_code = menu_permission.menu_code " +
						
						"INNER JOIN	main_menu " +
						"ON			main_menu.mm_code = menu.mm_code " +
													
						"WHERE		menu_permission.Login_Code = '"+ SessionUserCode +"' " +
						
						"GROUP BY 	main_menu.mm_code,"
									+ "main_menu.mm_name,"
									+ "main_menu.mn_order " +
				
						"ORDER BY 	main_menu.mn_order ASC " ;
		
		 //System.out.println(StrSQL);
		 
         return StrSQL ;
	}
	
	/*
	private String getSqlMenuGroups(){
		String StrSQL = "SELECT  mm_code," +
								"mm_name," +
								"( " +
									"SELECT COUNT(*) " +
									"FROM  		menu_permission " +
									"INNER JOIN	menu " +
									"ON			menu.menu_code = menu_permission.menu_code " +
									"WHERE 		menu.mm_code = main_menu.mm_code " +
									"AND		menu_permission.Login_Code = '1' " +
								") AS listcount " +
				
						"FROM 		main_menu " +
				
						"ORDER BY 	mn_order ASC " ;
		
		 //System.out.println(StrSQL);
		 
         return StrSQL ;
	}
    */
	
	private void createMenu() throws Exception{
        
        
        Conn c = new Conn();
        Class.forName(c.DOX_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(), c.DOX_USER, c.DOX_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt1 = conn.createStatement();
        //Statement stmt2 = conn.createStatement();
        
        ResultSet rs1 = null;
        //ResultSet rs2 = null;

        try {
        	
            TabboxMain.getChildren().clear();
            
            final Tabs tabs = new Tabs();
            final Tabpanels tabpanels = new Tabpanels();
            
            rs1 = stmt1.executeQuery(getSqlMenuGroups());
            
            //rs1 = sqlsel.getReSultSQL(getSqlMenuGroups());
            
            boolean IsFrist = true;

            while (rs1.next()) {
            	final Tab tab = new Tab(rs1.getString("mm_name"));

            	tab.setStyle(IsFrist ? "background: #69AA46" : "background: #478FCA");
            	IsFrist = false;

            	tab.setImage("/images/bricksx.png");
            	
            	tab.addEventListener("onClick", new EventListener<Event>() {
    	            public void onEvent(Event event) throws Exception {
    	            	for(int i=0; i<tabs.getChildren().size();i++)
	    	            	((Tab)tabs.getChildren().get(i)).setStyle("background: #478FCA");

    	            	tab.setStyle("background: #69AA46");
    	            }
    	        }); 
            	
                Tabpanel tabpanel = new Tabpanel();
                tabpanel.setHeight("100%");;
                Vlayout vlayout = new Vlayout();
                vlayout.setVflex("true");
                vlayout.setStyle("background: #FFFFFF;overflow:auto;");
                
            	//if(rs1.getInt("listcount") > 0){
                Statement stmt2 = conn.createStatement();
                ResultSet rs2 = stmt2.executeQuery(getSqlMenuUsers(rs1.getString("mm_code")));
                	
            	//rs2 = sqlsel.getReSultSQL(getSqlMenuUsers(rs1.getString("mm_code")));
                	
                try {		
            		while (rs2.next()) {
            			final A a = new A();
            			a.setId(rs2.getString("permission_code"));
            	        a.setStyle("color:#428bca");
            	        a.setAttribute("path", rs2.getString("menu_path"));
            	        a.setLabel(rs2.getString("menu_name"));
            	        a.setImage(rs2.getString("menu_image") != null ? ( "/images/" + rs2.getString("menu_image") ) : "/images/server_components.png");
            	        a.setTooltiptext(rs2.getString("menu_code") +" : " +rs2.getString("menu_name")+"; "+rs2.getString("menu_path"));
            	        
            	        final String StrApp = rs2.getString("menu_name");
            	        final String StrUrl = rs2.getString("menu_path");
            	        
            	        a.addEventListener("onClick", new EventListener<Event>() {
            	            public void onEvent(Event event) throws Exception {
            	            	if(StrUrl.equals("Sale.zul"))
            	            		Executions.sendRedirect("/Sale.zul");
            	            	else
            	            		newTab(StrApp, StrUrl); 
            	            }
            	        }); 
            	        
            	        vlayout.appendChild(a);
            		}
	            } catch (Exception e) {
	                e.printStackTrace();
	            } finally {
	            	if (rs2 != null) {
	                    rs2.close();
	                }
	                
	                if (stmt2 != null) {
	                    stmt2.close();
	                }
	            }
                
            	//} 
            	
            	tabpanel.appendChild(vlayout);
            	tabs.appendChild(tab);
            	tabpanels.appendChild(tabpanel);
            }
            
            TabboxMain.appendChild(tabs);
            TabboxMain.appendChild(tabpanels);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	if (rs1 != null) {
                rs1.close();
            }
            
            if (stmt1 != null) {
                stmt1.close();
            }
            
            if (conn != null) {
                conn.close();
            }

        }
	}
	
    private boolean IsEvent = true;

    public void newTab(final String ApplicationName, final String LinkURL) {
    	try {	

    		this.ApplicationName = ApplicationName;
        	this.LinkURL = LinkURL;
        	
        	onProcess(true);
        	
        	if(IsEvent){
        		Tabbox.addEventListener(Events.ON_FOCUS, new EventListener<Event>() {
	                
	                public void onEvent(Event event) throws Exception {
	                	newTab();
                		WinProcess.setVisible(false);
	                }
	            });
	        	
	        	IsEvent = !IsEvent;
        	}
        	
        	Tabbox.focus();

            Events.echoEvent("onFocus", Tabbox, null);

            
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
    
    public void onProcess(boolean b) throws Exception {
    	WinProcess.setVisible(b);
    	WinProcess.setFocus(b);
    	WinProcess.setPosition("center");
    	WinProcess.setMode("modal");
    }
    
    public void newTab() {
        try {

            Tab tb = new Tab();
            tb.setLabel(this.ApplicationName);
            tb.setClosable(true);
            tb.setImage("/images/stock_new-master-document.png");
            Tabs.appendChild(tb);

            tb.setSelected(true);

            String StrUrl = this.LinkURL;
            StrUrl = StrUrl.replace('?', '&');
            StrUrl = StrUrl.replace("zul&", "zul?");

            final org.zkoss.zul.Include ifm = new org.zkoss.zul.Include(StrUrl);
            //final Iframe ifm = new Iframe(StrUrl);
            //ifm.setScrolling("auto");
            ifm.setHeight("100%");
            ifm.setWidth("100%");
            
            ifm.addEventListener("onURIChange", new EventListener<Event>() {
                public void onEvent(Event event) throws Exception {
                	String src = ifm.getSrc();
                	src.replace("/", "");
                	
                	if(src.equals("Login.jsp") || src.equals("Login.zul") || src.equals("timeout.zul"))
                		Executions.sendRedirect("/index.zul");
                }
            });

            Tabpanel tp = new Tabpanel();
            tp.appendChild(ifm);
            Tabpanels.appendChild(tp);
        } catch (Exception e) {
        
        }
    }
    
	public void getPermission(String Login_Code) throws Exception{
		Conn c = new Conn();
        Class.forName(c.DOX_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(), c.DOX_USER, c.DOX_PASSWORD);
        conn.setAutoCommit(false);
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;
    	 
    	String Sql = 	
				"SELECT 	 if(login.IsApprovePR,'1','0') AS IsApprovePR," +
							"if(login.IsManufacturingStatus,'1','0') AS IsManufacturingStatus," +
							"if(login.IsManufacturingCostValue,'1','0') AS IsManufacturingCostValue," +
							"if(login.IsManufacturingFinishGoods,'1','0') AS IsManufacturingFinishGoods," +
							"if(login.IsManufacturingPrint,'1','0') AS IsManufacturingPrint " + 
							
				"FROM 		login " +
				
				"WHERE 		Login_Code = '" + Login_Code +"' " +
				
    			"LIMIT 1 ";
    	
    	//System.out.println(Sql);
		
		try{
			
			rs = stmt.executeQuery( Sql );
			
			//rs = sqlsel.getReSultSQL(Sql);
    		
			if(rs.next()){
				IsApprove = rs.getString("IsApprovePR").equals("1");
				IsManufacturingStatus = rs.getString("IsManufacturingStatus").equals("1");
				IsManufacturingCostValue = rs.getString("IsManufacturingCostValue").equals("1");
				IsManufacturingFinishGoods = rs.getString("IsManufacturingFinishGoods").equals("1");
				IsManufacturingPrint = rs.getString("IsManufacturingPrint").equals("1");
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
		
		// Hide Panel
		
		PanelApprove.setVisible(IsApprove);											// PR
		PanelIsManufacturingStatus.setVisible(IsManufacturingStatus);				// Step 1
		
		PanelIsManufacturingCostValue.setVisible(IsManufacturingCostValue);			// Step 2
		PanelIsManufacturingPrint.setVisible(IsManufacturingPrint);					// Step 3
		PanelIsManufacturingFinishGoods.setVisible(IsManufacturingFinishGoods);		// Step 4
		
		/*
		System.out.println(IsApprove);
		System.out.println(IsManufacturingStatus);
		System.out.println(IsManufacturingCostValue);
		System.out.println(IsManufacturingPrint);
		System.out.println(IsManufacturingFinishGoods);
		*/
		
		
		if((IsApprove || IsManufacturingStatus) && !IsManufacturingCostValue && !IsManufacturingPrint && !IsManufacturingFinishGoods){
			west.setSize("100%");
		}else if((!IsApprove || !IsManufacturingStatus) && (IsManufacturingCostValue && IsManufacturingPrint && IsManufacturingFinishGoods)){
			west.setSize("0%");
		}

	}

	// Utility
	public boolean isSelected(Combobox cbb){
		try {
			return cbb.getSelectedItem().getValue() != null; 
        } catch (Exception e) {
        	return false;
        }
	}
	
	
	public void printInfo(){
        //StringBuilder result = new StringBuilder();
        
        try {
        	/*
            result.append("--------------------------------------------------------------\r");
            result.append("ZK Session\r");
            Session sess = Sessions.getCurrent();
            result.append(".getLocalAddr():\t\t" + sess.getLocalAddr() + "\r");
            result.append(".getLocalName():\t\t" + sess.getLocalName() + "\r");
            result.append(".getRemoteAddr():\t\t" + sess.getRemoteAddr() + "\r");
            result.append(".getRemoteHost():\t\t" + sess.getRemoteHost() + "\r");
            result.append(".getServerName():\t\t" + sess.getServerName() + "\r");
            result.append(".getWebApp().getAppName():\t" + sess.getWebApp().getAppName() + "\r");
 
            HttpSession hses = (HttpSession) sess.getNativeSession();
            result.append("--------------------------------------------------------------------------------------------------\r");
            result.append("HttpSession\r");
            result.append(".getId():\t\t\t" + hses.getId() + "\r");
            result.append(".getCreationTime():\t\t" + new Date(hses.getCreationTime()).toString() + "\r");
            result.append(".getLastAccessedTime():\t\t" + new Date(hses.getLastAccessedTime()).toString() + "\r");
 
            result.append("--------------------------------------------------------------------------------------------------\r");
            result.append("ServletContext\r");
            ServletContext sCon = hses.getServletContext();
            result.append(".getServerInfo():\t\t" + sCon.getServerInfo() + "\r");
            result.append(".getContextPath():\t\t" + sCon.getContextPath() + "\r");
            result.append(".getServletContextName():\t" + sCon.getServletContextName() + "\r");
 
            result.append("--------------------------------------------------------------------------------------------------\r");
            result.append("ZK Executions\r");
            result.append(".getHeader('user-agent'):\t" + Executions.getCurrent().getHeader("user-agent") + "\r");
            result.append(".getHeader('accept-language'):\t" + Executions.getCurrent().getHeader("accept-language") + "\r");
            result.append(".getHeader('referer'):\t\t" + Executions.getCurrent().getHeader("referer") + "\r");
            result.append(".getHeader('connection'):\t" + Executions.getCurrent().getHeader("connection") + "\r");
            result.append(".getHeader('zk-sid'):\t\t" + Executions.getCurrent().getHeader("zk-sid") + "\r");
            result.append(".getHeader('origin'):\t\t" + Executions.getCurrent().getHeader("origin") + "\r");
            result.append(".getHeader('host'):\t\t" + Executions.getCurrent().getHeader("host") + "\r");
            result.append(".getHeader('cookie'):\t\t" + Executions.getCurrent().getHeader("cookie") + "\r");
            result.append("--------------------------------------------------------------------------------------------------\r");
 			*/
            //System.out.println(result.toString());
 
        } catch (Exception ex) {
        	//System.out.println(ex.getMessage());
        }
    }
    
    public String isNull(String Value){
    	return Value == null ? null : ("'" + Value + "'");
    }
    
    private String txtNull(String Value){
    	return Value == null ? "" : Value;
    }
    
}
