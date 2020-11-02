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

import com.poseintelligence.cssd.document.Occurrence;
import com.poseintelligence.cssd.document.SendSterile;
import com.poseintelligence.cssd.model.ModelMaster;
import com.poseintelligence.cssd.utillity.DateTime;
import org.zkoss.zk.ui.util.GenericForwardComposer;

@SuppressWarnings({ "unused", "serial" })
public class DepartmentReceiveStock2  extends Div{
	private Session session = Sessions.getCurrent();
	private Datebox Datebox_SDocDate;
	private Datebox Datebox_EDocDate;
	private Listbox Listbox_Document;
	private Listbox Listbox_Detail;
	
	
	private Window Window_PopUp;

	private Textbox Textbox_Recipient;
	private Textbox Textbox_Approve;
	private Textbox Textbox_date_pay;
	private Textbox Textbox_time_pay;
	private Textbox Textbox_chk_recive;
	
	private int Chk_check = 0;
	private int Chk_total = 0;
	private String 	S_UserId,
					S_DeptId,
					S_UserName,
					S_IsAdmin,
					S_EmpCode,
					S_DepName,
					S_DB,
					B_ID;
	List<ModelMaster> Model_ResterileType = new ArrayList<>();
	
	List<ModelMaster> Model_uncheck = new ArrayList<>();
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
		
		Listbox_Document = ((Listbox) getFellow("Listbox_Document"));
		Listbox_Detail = ((Listbox) getFellow("Listbox_Detail"));
		Datebox_SDocDate = ((Datebox) getFellow("Datebox_SDocDate"));
		Datebox_EDocDate = ((Datebox) getFellow("Datebox_EDocDate"));
		Window_PopUp = ((Window) getFellow("Window_PopUp"));
		
		Textbox_chk_recive = ((Textbox) getFellow("Textbox_chk_recive"));
		Textbox_Recipient = ((Textbox) getFellow("Textbox_Recipient"));
		Textbox_Approve = ((Textbox) getFellow("Textbox_Approve"));
		Textbox_date_pay = ((Textbox) getFellow("Textbox_date_pay"));
		Textbox_time_pay = ((Textbox) getFellow("Textbox_time_pay"));


		if(Textbox_chk_recive.getValue().equals("1")) {
			Datebox_SDocDate.setText(DateTime.getDateNow());
		}else {
			Datebox_SDocDate.setText(DateTime.getDateLastthreeMonth());
		}
		Datebox_EDocDate.setText(DateTime.getDateLastdate());

		
		onDisplayDocument();
		defineReSterileType();

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
								rs.getString("OccuranceName"),
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
				"SELECT" + 
				"            occurancetype.ID," + 
				"            occurancetype.OccuranceName," + 
				"            occurancetype.IsStatus" + 
				"          FROM" + 
				"          occurancetype  ORDER BY ID DESC";
		
		return S_Sql;
	}
	
	
//	DisplayDocument =====================================================
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
				int IsStatus = rs.getInt("IsStatus");
				int Qty_status9_check = rs.getInt("Qty_status9_check");
				int Qty_Status9 = rs.getInt("Qty_Status9");
				int Qty_Status6_check = rs.getInt("Qty_Status6_check");
				int Qty_status6 = rs.getInt("Qty_status6");
				int Qty = rs.getInt("Qty");
				String pre_qty = "0";
				String Text = "";
				if(IsStatus == 9) {
					if(Qty_status9_check >= Qty_Status9) {
						 pre_qty = String.valueOf((Qty_status9_check - Qty_Status9));
					}else {
						 pre_qty = String.valueOf(Qty);
					}
				}else if(IsStatus == 6) {
					 	pre_qty = String.valueOf((Qty_status6 - Qty_Status6_check));
				}else {
					 	pre_qty = String.valueOf(Qty);
				}
				
				if(IsStatus == 2 || IsStatus == 3) {
					 Text = "(ครบ)";
				}else {
					 Text = "(ค้าง)";
				}
				
				
				
				Listcell lc_DocNo= new Listcell();
				A DocNo = new A(rs.getString("DocNo"));
				final String text_DocNo = rs.getString("DocNo");

				DocNo.addEventListener("onClick", new EventListener<Event>() {
					public void onEvent(Event event) throws Exception {
						
						ShowDetail(text_DocNo);

				    	
			        }
			    });
				
				lc_DocNo.appendChild( DocNo );
				
//		        ========================================================
				Button btn_re = new Button("รับของ");
		        Listcell cell_re = new Listcell();
		        btn_re.setSclass("btn btn-primary");
				 String DocNo1 = rs.getString("DocNo");
		        btn_re.addEventListener("onClick", new EventListener<Event>() {
						public void onEvent(Event event) throws Exception {
							
							Setpayout(DocNo1);

				        }
				    });
			        
		        cell_re.appendChild(btn_re);
//	====================================================================
				
				
				list.appendChild(new Listcell(rs.getString("CreateDate")));
				list.appendChild(lc_DocNo);
				list.appendChild(new Listcell(rs.getString("StatusPO")));
				list.appendChild(new Listcell(pre_qty + Text));
				list.appendChild(cell_re);
//				list.setAttribute("RowID", rs.getString("RowID"));
								
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
		}
    }
	
	private String getSqlDocument() {
		String date1 = "";
		String date2 = "";
		if(Textbox_chk_recive.getValue().equals("1")) {
			 date1 = Datebox_SDocDate.getText();
			 date2 = date1;	
		}else {
			 date1 = Datebox_SDocDate.getText();
			 date2 = Datebox_EDocDate.getText();

		}
		String S_Sql = "";
		
		S_Sql = 	
			"SELECT" + 
			"  pa.ID," + 
			"  pa.DocNo," + 
			"  DATE(pa.CreateDate) AS CreateDate," + 
			"  pa.IsStatus," + 
			"  pa.IsBorrow," + 
			"  pa.IsBorrowStatus," + 
			"  CASE" + 
			"      pa.IsStatus " + 
			"      WHEN '2' THEN" + 
			"      'รอรับเข้า' " + 
			"      WHEN '3' THEN" + 
			"      'แผนกรับเข้าแล้ว' " + 
			"      WHEN '6' THEN" + 
			"      'รอรับเข้า(ของค้างบางตัว)' " + 
			"      WHEN '9' THEN" + 
			"      'แผนกรับเข้าแล้ว(ของค้างบางตัว)' " + 
			"    END AS StatusPO," + 
			"    (" + 
			"    SELECT" + 
			"    CASE" + 
			"        pa.IsStatus " + 
			"        WHEN '2' THEN" + 
			"        COUNT( payout.DocNo ) " + 
			"        WHEN '3' THEN" + 
			"        COUNT( payout.DocNo ) " + 
			"        WHEN '9' THEN" + 
			"        SUM( paydetail.Qty ) " + 
			"      END " + 
			"      FROM" + 
			"        payout" + 
			"        INNER JOIN ( SELECT DocNo, ID, ItemStockID, IsStatus, Qty FROM payoutdetail ) paydetail ON payout.DocNo = paydetail.DocNo" + 
			"        INNER JOIN ( SELECT RowID, IsStatus FROM itemstock ) items ON paydetail.ItemStockID = items.RowID" + 
			"        LEFT JOIN payoutdetailsub ON paydetail.ID = payoutdetailsub.Payoutdetail_RowID " + 
			"      WHERE" + 
			"        payout.DocNo = pa.DocNo " + 
			"      AND" + 
			"      CASE" + 
			"          pa.IsStatus " + 
			"          WHEN '2' THEN" + 
			"          payoutdetailsub.IsStatus = 2 " + 
			"          WHEN '3' THEN" + 
			"          ( payoutdetailsub.IsStatus = 2 OR payoutdetailsub.IsStatus = 3 ) " + 
			"          WHEN '9' THEN paydetail.IsStatus = 2 " + 
			"        END " + 
			"        ) AS Qty," + 
			"        (" + 
			"        SELECT" + 
			"        CASE" + 
			"            pa.IsStatus " + 
			"            WHEN '2' THEN" + 
			"            COUNT( payout.DocNo ) " + 
			"            WHEN '3' THEN" + 
			"            COUNT( payout.DocNo ) " + 
			"            WHEN '9' THEN" + 
			"            SUM( paydetail.Qty ) " + 
			"          END " + 
			"          FROM" + 
			"            payout" + 
			"            INNER JOIN ( SELECT DocNo, ID, ItemStockID, IsStatus, Qty FROM payoutdetail ) paydetail ON payout.DocNo = paydetail.DocNo" + 
			"            INNER JOIN ( SELECT RowID, IsStatus FROM itemstock ) items ON paydetail.ItemStockID = items.RowID" + 
			"          WHERE" + 
			"            payout.DocNo = pa.DocNo " + 
			"          AND" + 
			"          CASE" + 
			"              pa.IsStatus " + 
			"              WHEN '9' THEN ( paydetail.IsStatus = 2 OR paydetail.IsStatus = 1 OR paydetail.IsStatus = 0)" + 
			"            END " + 
			"            ) AS Qty_status9_check," + 
			"            (" + 
			"            SELECT" + 
			"        CASE" + 
			"            pa.IsStatus " + 
			"            WHEN '6' THEN" + 
			"            SUM( paydetail.Qty ) " + 
			"          END " + 
			"          FROM" + 
			"            payout" + 
			"            INNER JOIN ( SELECT DocNo, ID, ItemStockID, IsStatus, Qty FROM payoutdetail ) paydetail ON payout.DocNo = paydetail.DocNo" + 
			"            INNER JOIN ( SELECT RowID, IsStatus FROM itemstock ) items ON paydetail.ItemStockID = items.RowID " + 
			"          WHERE" + 
			"            payout.DocNo = pa.DocNo " + 
			"          AND" + 
			"          CASE" + 
			"              pa.IsStatus " + 
			"              WHEN '6' THEN" + 
			"              (paydetail.IsStatus = 0  OR paydetail.IsStatus = 1 OR paydetail.IsStatus = 2)" + 
			"            END " + 
			"            ) AS Qty_status6," + 
			"            (" + 
			"            SELECT" + 
			"              COUNT( payout.DocNo ) " + 
			"            FROM" + 
			"              payout" + 
			"              INNER JOIN ( SELECT DocNo, ID, ItemStockID, IsStatus, Qty FROM payoutdetail ) paydetail ON payout.DocNo = paydetail.DocNo" + 
			"              INNER JOIN ( SELECT RowID, IsStatus FROM itemstock ) items ON paydetail.ItemStockID = items.RowID" + 
			"              LEFT JOIN payoutdetailsub ON paydetail.ID = payoutdetailsub.Payoutdetail_RowID " + 
			"            WHERE" + 
			"              payout.DocNo = pa.DocNo " + 
			"              AND payoutdetailsub.IsStatus = 2 " + 
			"            ) AS Qty_Status6_check," + 
			"            (" + 
			"            SELECT" + 
			"              COUNT( payout.DocNo ) " + 
			"            FROM" + 
			"              payout" + 
			"              INNER JOIN ( SELECT DocNo, ID, ItemStockID, IsStatus, Qty FROM payoutdetail ) paydetail ON payout.DocNo = paydetail.DocNo" + 
			"              INNER JOIN ( SELECT RowID, IsStatus FROM itemstock ) items ON paydetail.ItemStockID = items.RowID" + 
			"              LEFT JOIN payoutdetailsub ON paydetail.ID = payoutdetailsub.Payoutdetail_RowID " + 
			"            WHERE" + 
			"              payout.DocNo = pa.DocNo " + 
			"              AND payoutdetailsub.IsStatus = 3 " + 
			"            ) AS Qty_Status9," + 
			"            ( SELECT settingapp.chkreceiveIn FROM settingapp WHERE settingapp.DeptID = dpt.ID LIMIT 1 ) AS chkreceiveIn " + 
			"          FROM" + 
			"            payout AS pa" + 
			"            INNER JOIN department AS dpt ON pa.DeptID = dpt.ID " + 
			"          WHERE" + 
			"            DATE( pa.CreateDate ) BETWEEN DATE( '"+ DateTime.convertDate(date1) +"' ) AND DATE( '"+ DateTime.convertDate(date2) +"' )  " + 
			"            AND pa.IsCancel = 0 " + 
			"            AND ( pa.IsStatus = 2 OR pa.IsStatus = 6 OR pa.IsStatus = 9 ) " + 
			"            AND pa.DeptID = '"+ S_DeptId +"' " + 
			"            AND pa.B_ID = '"+ B_ID +"' " + 
			"            AND pa.IsPrint != 0" + 
			"        ORDER BY" + 
			"    pa.DocNo DESC ";
		
		System.out.println(S_Sql);
		
		return S_Sql;
	}


	

	public void ShowDetail(final String text_DocNo) throws Exception {
		

		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;
        
		try{	
			Chk_check = 0;
			Chk_total = 0;
			rs = stmt.executeQuery(getSqlDetail(text_DocNo));
			
			Listbox_Detail.getItems().clear();
			
			int I_No = 0;
			
			List<ModelMaster> list_uncheck = new ArrayList<>();

			while(rs.next()){
				Listitem list = new Listitem();
				String Recipient = rs.getString("Recipient");
				String Approve = rs.getString("Approve");
				String date_pay = rs.getString("date_pay");
				String time_pay = rs.getString("time_pay");
				String OccuranceTypeID = rs.getString("OccuranceTypeID");
		        String UsageCode = rs.getString("UsageCode");
		        String ID = rs.getString("ID");

		        
				Textbox_Recipient.setText(Recipient);
				Textbox_Approve.setText(Approve);
				Textbox_date_pay.setText(date_pay);
				Textbox_time_pay.setText(time_pay);

				
				final Checkbox chk = new Checkbox();
				chk.addEventListener("onClick", new EventListener<Event>() {
					public void onEvent(Event event) throws Exception {
						
						
						if(chk.isChecked()) {
							Chk_check ++ ;
						}else {
	
							Chk_check -- ;
							
							if(Chk_check <= 0) {
								Chk_check = 0;
							}

						}
						
						//System.out.println(Chk_check);
			        }
			    });
			      Listcell cell_chk = new Listcell();
			      cell_chk.appendChild(chk);
				
			      
//			        ========================================================
					Button btn_re = new Button();
			        Listcell cell_re = new Listcell();
			        btn_re.setIconSclass("z-icon-exclamation-circle");
			        if(!OccuranceTypeID.equals("0") ) {
				        btn_re.setSclass("btn btn-danger");
			        }else {
				        btn_re.setSclass("btn");
			        }
			        String DocNo = rs.getString("DocNo");
			        btn_re.addEventListener("onClick", new EventListener<Event>() {
							public void onEvent(Event event) throws Exception {
								
								callResterileType(list , cell_re,  btn_re ,UsageCode,DocNo );

					        }
					    });
				        
			        cell_re.appendChild(btn_re);
//		====================================================================
			      
			      
				list.appendChild(new Listcell(rs.getString("DocNo")));
				list.appendChild(new Listcell(rs.getString("UsageCode")));
				list.appendChild(new Listcell(rs.getString("itemname")));
				list.appendChild(new Listcell(rs.getString("Qty")));
				list.appendChild(cell_re);
				list.appendChild(cell_chk);
				
				list.setAttribute("UsageCode", rs.getString("RowID"));
				list.setAttribute("Payout_Rowid", rs.getString("ID"));
				list.setAttribute("OccuranceTypeID", OccuranceTypeID);
				
				Listbox_Detail.appendChild(list);
				

                I_No++;
                Chk_total ++;
                
	
                
			}
			
			if(I_No == 0) {
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
	
	
	public void Setpayout(final String DocNo) throws Exception {
		
		Messagebox.show("ยืนยันการรับเข้า ?" , "CSSD", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,new EventListener<Event>() {
			public void onEvent(Event evt) throws Exception {
				switch (((Integer) evt.getData()).intValue()) {
	              	case Messagebox.YES:
	              		
	            		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
	                    Class.forName(c.S_MYSQL_DRIVER);
	                    Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
	                    conn.setAutoCommit(false);
	                    
	                    Statement stmt = conn.createStatement();
	                	ResultSet rs = null;
	                	System.out.println("จำนวนทั้งหมด" + Chk_total + "  " + "จำนวนที่รับเข้า" + Chk_check);
	                	
	            		String S_UsageCode_check = "";
	            		String Payout_Rowid_check = "";
	            		String S_UsageCode_uncheck = "";// Occurrence Pass
	            		String Payout_Rowid_uncheck = "";// Occurrence Pass
	            		String S_OccuranceTypeID = "";// Occurrence Pass
	            		String B_OccuranceTypeID = "0";// Occurrence Pass
	            		int count = 0;
	            		int chk_false = 0;
	            		int chk_create_oc = 0;
	            		boolean B_IsSelected = false;
	            		
//	            		System.out.println(DocNo);


	            		Iterator<Listitem> li = Listbox_Detail.getItems().iterator();

	            		while(li.hasNext()){
	            			Listitem list = (Listitem) li.next();	
	            			
	            			
	            			B_IsSelected = ((Checkbox)((Listcell)list.getChildren().get(5)).getChildren().get(0)).isChecked();
	            			S_OccuranceTypeID += (String)list.getAttribute("OccuranceTypeID") + ",";

	            			B_OccuranceTypeID = (String)list.getAttribute("OccuranceTypeID");
	            	    	
	            			if(B_IsSelected) {
	            				S_UsageCode_check += (String)list.getAttribute("UsageCode") + ",";	
	            				Payout_Rowid_check += (String)list.getAttribute("Payout_Rowid") + ",";	

	            				count ++;
	            			}else {
	            				S_UsageCode_uncheck += (String)list.getAttribute("UsageCode") + ",";
	            				Payout_Rowid_uncheck += (String)list.getAttribute("Payout_Rowid") + ",";	
	            			}
	            			
	            			
	            			//System.out.println(B_OccuranceTypeID + "-" + B_IsSelected);
	            			if(B_OccuranceTypeID.equals("0")  && B_IsSelected == false) {
	            				chk_false ++;
	            			}
	            			
	            			if(!B_OccuranceTypeID.equals("0") ) {
	            				chk_create_oc ++;
	            			}
	            		}
	            		//System.out.println(chk_false);

	            			if(chk_false == 0) {
	            				
	            				if(count == Chk_total) {

	            					S_UsageCode_check = S_UsageCode_check.substring(0, S_UsageCode_check.length() - 1);
	            					Payout_Rowid_check = Payout_Rowid_check.substring(0, Payout_Rowid_check.length() - 1);

	            					SetPayout_all(DocNo,chk_create_oc,S_UsageCode_check,Payout_Rowid_check);
	            				}else {
	            					
	        	            		System.out.println(S_UsageCode_check);

	            					if(!S_UsageCode_check.equals("")) {
		            					S_UsageCode_check = S_UsageCode_check.substring(0, S_UsageCode_check.length() - 1);
	            					}
	            					if(!S_UsageCode_uncheck.equals("")) {
		            					S_UsageCode_uncheck = S_UsageCode_uncheck.substring(0, S_UsageCode_uncheck.length() - 1);
	            					}
	            					if(!Payout_Rowid_uncheck.equals("")) {
		            					Payout_Rowid_uncheck = Payout_Rowid_uncheck.substring(0, Payout_Rowid_uncheck.length() - 1);
	            					}
	            					
	            					if(!Payout_Rowid_check.equals("")) {
		            					Payout_Rowid_check = Payout_Rowid_check.substring(0, Payout_Rowid_check.length() - 1);
	            					}
//	            					System.out.println("2");
	            					
	            					SetPayout_some(DocNo,chk_create_oc,S_UsageCode_check,S_UsageCode_uncheck,Payout_Rowid_uncheck,Payout_Rowid_check);
	            				}
	            			}else {
	            				 Messagebox.show("กรุณาระบุความเสี่ยงของรายการที่ไม่รับเข้า" + "จำนวน : " + chk_false + " รายการ", "CSSD", Messagebox.OK, Messagebox.EXCLAMATION);
	            			}
	              		
	              		
	              		break;
	              	case Messagebox.NO:
	              		break;
				}
			}
		});

		

	}
	
	public void SetPayout_some(final String DocNo , final int S_OccuranceTypeID , final String S_UsageCode_check , final String S_UsageCode_uncheck , final String Payout_Rowid_uncheck  , final String Payout_Rowid_check     ) throws Exception {
		

		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;
    	
		String S_OCDocNo = Occurrence.getOccurrenceDocNo(S_DB, DocNo, S_DeptId, S_UserId, "1",  "บันทึกความเสี่ยงจากแผนก");

    	try {
    		if(S_OccuranceTypeID > 0) {

        		System.out.println(S_OCDocNo);
        		
				String S_Sql = 
						
						"	INSERT INTO  occurancedetail ( "
					+ 	"   DocNo, "
					+ 	"   ItemStockID, "
					+ 	"	RefDocNo, "					
					+ 	"	OccuranceID "
					+ 	"   ) "
					+ 	"   SELECT "
					+ 	"	'" + S_OCDocNo + "', "
					+ 	"   payoutdetailsub.ItemStockID, "
					+ 	"	'" + DocNo + "', "
					+ 	"	payoutdetailsub.OccuranceTypeID "
					+ 	"   FROM        payoutdetailsub "
					
					+ 	"   INNER JOIN  payoutdetail "
					+	"	ON			payoutdetail.ID = payoutdetailsub.Payoutdetail_RowID "

					+ 	"   WHERE     	payoutdetail.DocNo = '" + DocNo + "' "
					
					+ 	"	AND 		payoutdetailsub.OccuranceTypeID <> 0 "
					+ 	"	AND 		payoutdetailsub.IsStatus <> 3 "; 
						
					System.out.println(S_Sql);
					
					stmt.executeUpdate(S_Sql);
    		}
    		
			String S_SSDocNo = SendSterile.getSendSterileDocNo(S_DB, DocNo, S_DeptId, S_UserId, "0",  "ความเสี่ยงจาก" + DocNo);

			String S_SqlInsertSendSterileDetail = 
					
					"INSERT INTO  sendsteriledetail ( "
				+ 	"   sendsteriledetail.SendSterileDocNo, "
				+ 	"   sendsteriledetail.ItemStockID, "
				+ 	"   sendsteriledetail.Qty, "
				+ 	"   sendsteriledetail.UsageCode, "
				+ 	"	sendsteriledetail.SSdetail_IsWeb, "
				+ 	"	sendsteriledetail.RefDocNo, "
				+ 	"	sendsteriledetail.B_ID "
				+ 	"   ) "
				
				+ 	"   SELECT "
				+ 	"	'" + S_SSDocNo + "', "
				+ 	"   itemstock.RowID, "
				+ 	"   1, "
				+ 	"   itemstock.UsageCode, "
				+ 	"   1, "
				+ 	"	'" + S_OCDocNo + "', "
			    + 	"   '"+B_ID+"' "
				

				+ 	"   FROM        itemstock "

				+ 	"   WHERE       RowID IN (" + S_UsageCode_uncheck + ") ";
	 		  System.out.println(S_SqlInsertSendSterileDetail);
			stmt.executeUpdate(S_SqlInsertSendSterileDetail);

			String sSql2 = "SELECT" + 
					"            payout.IsStatus," + 
					"            COALESCE(payout.Qty - (SELECT COUNT(payoutdetailsub.Payoutdetail_RowID) " + 
					"                      FROM payoutdetailsub" + 
					"                      INNER JOIN payoutdetail ON payoutdetailsub.Payoutdetail_RowID = payoutdetail.ID" + 
					"                      INNER JOIN itemstock ON payoutdetailsub.ItemStockID = itemstock.RowID" + 
					"                      INNER JOIN item ON itemstock.ItemCode = item.itemcode" + 
					"                      WHERE payoutdetail.DocNo = payout.DocNo" + 
					"        ),'-') AS ChkQty" + 
					"          FROM payout" + 
					"          INNER JOIN payoutdetail ON payout.DocNo = payoutdetail.DocNo" + 
					"          INNER JOIN payoutdetailsub ON payoutdetail.ID = payoutdetailsub.Payoutdetail_RowID" + 
					"          INNER JOIN itemstock ON payoutdetailsub.ItemStockID = itemstock.RowID" + 
					"          INNER JOIN item ON itemstock.ItemCode = item.itemcode" + 
					"          WHERE payout.DocNo = '"+DocNo+"' ";
	 		  System.out.println(sSql2);
			rs = stmt.executeQuery(sSql2);
			while(rs.next()){
				
				
        		final int IsStatus =  rs.getInt("IsStatus");
        		final int ChkQty =  rs.getInt("ChkQty");
				
        		if(IsStatus ==2) {
					String sSql_update = " UPDATE payout SET IsStatus = 3 WHERE DocNo = '"+DocNo+"' ";
			 		System.out.println(sSql_update);
					stmt.executeUpdate(sSql_update);
        		}else if(IsStatus ==6) {
        			String sSql_update = " UPDATE payout SET IsStatus = 9 WHERE DocNo = '"+DocNo+"' ";
			 		System.out.println(sSql_update);
					stmt.executeUpdate(sSql_update);
        		}else if(IsStatus ==9) {
        			
        			if(ChkQty ==0) {
        				String sSql_update = " UPDATE payout SET IsStatus = 3 WHERE DocNo = '"+DocNo+"' ";
    			 		System.out.println(sSql_update);
    					stmt.executeUpdate(sSql_update);
        			}else {
        				String sSql_update = " UPDATE payout SET IsStatus = 9 WHERE DocNo = '"+DocNo+"' ";
    			 		System.out.println(sSql_update);
    					stmt.executeUpdate(sSql_update);
        			}
        			
        		}
        		if(!Payout_Rowid_check.equals("")) {
        			String S_SqlDetailSub = 
        					
        					"UPDATE		payoutdetailsub "
        				+ 	"SET		IsStatus = 3 "
        				+ 	"WHERE 		ID IN (" + Payout_Rowid_check + ") "; 
    		 		System.out.println(S_SqlDetailSub);
    				stmt.executeUpdate(S_SqlDetailSub);
        		}

        		if(!Payout_Rowid_uncheck.equals("")) {
        			String S_SqlDetailSub2 = 
        					
        					"UPDATE		payoutdetailsub "
        				+ 	"SET		IsStatus = 3 "
        				+ 	"WHERE 		ID IN (" + Payout_Rowid_uncheck + ") "; 
    		 		System.out.println(S_SqlDetailSub2);
    				stmt.executeUpdate(S_SqlDetailSub2);      			
        		}

        		if(!S_UsageCode_check.equals("")) {
        			
        			String S_SqlUpdateItemStock = 
        					
        					"UPDATE itemstock SET IsStatus = 5,"
        					+ "IsPay = 1,"
        					+ "IsDispatch = 0,"
        					+ "IsTag = 0 ,"
        					+ "IsRemarkExpress = 0 "
        					+ "WHERE RowID IN (" + S_UsageCode_check + ") "; 
    		 		System.out.println(S_SqlUpdateItemStock);
    				stmt.executeUpdate(S_SqlUpdateItemStock);
        		}

        		if(!S_UsageCode_uncheck.equals("")) {
        			
        			String S_SqlUpdateItemStock2 = 
        					
        					"UPDATE itemstock SET IsStatus = 0,"
        					+ "IsPay = 0,"
        					+ "IsDispatch = 1 "
        					+ "WHERE RowID IN (" + S_UsageCode_uncheck + ") "; 
    		 		System.out.println(S_SqlUpdateItemStock2);
    				stmt.executeUpdate(S_SqlUpdateItemStock2);
        		}

        		
			}



				
				
			

    		
    		
		} catch (Exception e) {
			e.printStackTrace();			
		}finally {
//	        if (stmt != null) {
//	            stmt.close();
//	        }
//	        
//	        if (conn != null) {
//	            conn.close();
//	        }
//	        
//	        if (rs != null) {
//                rs.close();
//            }
//	        
			 Messagebox.show("รับเข้าสำเร็จ", "CSSD", Messagebox.OK, Messagebox.INFORMATION);
//			ShowDetail(DocNo);
			Listbox_Detail.getItems().clear();

			Textbox_Recipient.setText("");
			Textbox_Approve.setText("");
			Textbox_date_pay.setText("");
			Textbox_time_pay.setText("");
			onDisplayDocument();
		}
        



	}
	
	
	
	public void SetPayout_all(final String DocNo , final int S_OccuranceTypeID , final String S_UsageCode_check , final String Payout_Rowid_check) throws Exception {
		

		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;
    	
    	try {
    		if(S_OccuranceTypeID > 0) {
    			String S_OCDocNo = Occurrence.getOccurrenceDocNo(S_DB, DocNo, S_DeptId, S_UserId, "1",  "บันทึกความเสี่ยงจากแผนก");

        		System.out.println(S_OCDocNo);
        		
				String S_Sql = 
						
						"	INSERT INTO  occurancedetail ( "
					+ 	"   DocNo, "
					+ 	"   ItemStockID, "
					+ 	"	RefDocNo, "					
					+ 	"	OccuranceID "
					+ 	"   ) "
					+ 	"   SELECT "
					+ 	"	'" + S_OCDocNo + "', "
					+ 	"   payoutdetailsub.ItemStockID, "
					+ 	"	'" + DocNo + "', "
					+ 	"	payoutdetailsub.OccuranceTypeID "
					+ 	"   FROM        payoutdetailsub "
					
					+ 	"   INNER JOIN  payoutdetail "
					+	"	ON			payoutdetail.ID = payoutdetailsub.Payoutdetail_RowID "

					+ 	"   WHERE     	payoutdetail.DocNo = '" + DocNo + "' "
					
					+ 	"	AND 		payoutdetailsub.OccuranceTypeID <> 0 "
					+ 	"	AND 		payoutdetailsub.IsStatus <> 3 "; 
						
					System.out.println(S_Sql);
					
					stmt.executeUpdate(S_Sql);
    		}
    		
			String sSql2 = "SELECT" + 
					"            payout.IsStatus," + 
					"            COALESCE(payout.Qty - (SELECT COUNT(payoutdetailsub.Payoutdetail_RowID) " + 
					"                      FROM payoutdetailsub" + 
					"                      INNER JOIN payoutdetail ON payoutdetailsub.Payoutdetail_RowID = payoutdetail.ID" + 
					"                      INNER JOIN itemstock ON payoutdetailsub.ItemStockID = itemstock.RowID" + 
					"                      INNER JOIN item ON itemstock.ItemCode = item.itemcode" + 
					"                      WHERE payoutdetail.DocNo = payout.DocNo" + 
					"        ),'-') AS ChkQty" + 
					"          FROM payout" + 
					"          INNER JOIN payoutdetail ON payout.DocNo = payoutdetail.DocNo" + 
					"          INNER JOIN payoutdetailsub ON payoutdetail.ID = payoutdetailsub.Payoutdetail_RowID" + 
					"          INNER JOIN itemstock ON payoutdetailsub.ItemStockID = itemstock.RowID" + 
					"          INNER JOIN item ON itemstock.ItemCode = item.itemcode" + 
					"          WHERE payout.DocNo = '"+DocNo+"' ";
	 		  System.out.println(sSql2);
			rs = stmt.executeQuery(sSql2);
			while(rs.next()){
				
				
        		final int IsStatus =  rs.getInt("IsStatus");
        		final int ChkQty =  rs.getInt("ChkQty");
				
        		if(IsStatus ==2) {
					String sSql_update = " UPDATE payout SET IsStatus = 3 WHERE DocNo = '"+DocNo+"' ";
			 		System.out.println(sSql_update);
					stmt.executeUpdate(sSql_update);
        		}else if(IsStatus ==6) {
        			String sSql_update = " UPDATE payout SET IsStatus = 9 WHERE DocNo = '"+DocNo+"' ";
			 		System.out.println(sSql_update);
					stmt.executeUpdate(sSql_update);
        		}else if(IsStatus ==9) {
        			
        			if(ChkQty ==0) {
        				String sSql_update = " UPDATE payout SET IsStatus = 3 WHERE DocNo = '"+DocNo+"' ";
    			 		System.out.println(sSql_update);
    					stmt.executeUpdate(sSql_update);
        			}else {
        				String sSql_update = " UPDATE payout SET IsStatus = 9 WHERE DocNo = '"+DocNo+"' ";
    			 		System.out.println(sSql_update);
    					stmt.executeUpdate(sSql_update);
        			}
        			
        		}
        		
    			String S_SqlDetailSub = 
    					
    					"UPDATE		payoutdetailsub "
    				+ 	"SET		IsStatus = 3 "
    				+ 	"WHERE 		ID IN (" + Payout_Rowid_check + ") "; 
		 		System.out.println(S_SqlDetailSub);
				stmt.executeUpdate(S_SqlDetailSub);
				
    			String S_SqlUpdateItemStock = 
    					
    					"UPDATE itemstock SET IsStatus = 5,"
    					+ "IsPay = 1,"
    					+ "IsDispatch = 0,"
    					+ "IsTag = 0 ,"
    					+ "IsRemarkExpress = 0 "
    					+ "WHERE RowID IN (" + S_UsageCode_check + ") "; 
		 		System.out.println(S_SqlUpdateItemStock);
				stmt.executeUpdate(S_SqlUpdateItemStock);
        		
			}


				
				
			

    		
    		
		} catch (Exception e) {
			e.printStackTrace();												
		}finally {
	        if (stmt != null) {
	            stmt.close();
	        }
	        
	        if (conn != null) {
	            conn.close();
	        }
	        
	        if (rs != null) {
                rs.close();
            }
	        
			 Messagebox.show("รับเข้าสำเร็จ", "CSSD", Messagebox.OK, Messagebox.INFORMATION);

//			ShowDetail(DocNo);
			Listbox_Detail.getItems().clear();
			Textbox_Recipient.setText("");
			Textbox_Approve.setText("");
			Textbox_date_pay.setText("");
			Textbox_time_pay.setText("");
			onDisplayDocument();
		}
        



	}
	

	
	private void callResterileType(final Listitem list , final Listcell lc_opt , final Button btnre , final String UsageCode , final String DocNo ) {
		

		
		
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
//					System.out.println(S_ReSterileTypeName + S_ReSterileTypeId);
			        String RowID = "";
			        String ID = "";
					String sSql = " SELECT itemstock.RowID,"
							+ "			   payoutdetailsub.ID "
							+ "FROM itemstock"
							+ " INNER JOIN payoutdetailsub ON itemstock.UsageCode = payoutdetailsub.UsageCode"
							+ " INNER JOIN payoutdetail ON payoutdetail.ID = payoutdetailsub.Payoutdetail_RowID"
							+ " WHERE itemstock.UsageCode = '" + UsageCode + "' "
							+ " AND payoutdetail.DocNo =  '" + DocNo + "' ";
					
					
					System.out.println(sSql);

					rs = stmt.executeQuery(sSql);
					while(rs.next()){
						RowID =  rs.getString("RowID");
						ID =  rs.getString("ID");
					}
					
					if(cbb.getSelectedIndex() <= 0) {
						lc_opt.setTooltiptext("");
						lc_opt.setAttribute("ResterileTypeID", null);


				        
				       String sSql_update = " UPDATE payoutdetailsub SET OccuranceTypeID = 0 WHERE ItemStockID = '" + RowID + "' AND ID = '" + ID + "' ";
				       stmt.executeUpdate(sSql_update);
				       
				       btnre.setSclass("btn");
				       list.setAttribute("OccuranceTypeID", "0");
						
					}else {
						lc_opt.setTooltiptext(S_ReSterileTypeName);
						lc_opt.setAttribute("ResterileTypeID", S_ReSterileTypeId);
						
					       String sSql_update = " UPDATE payoutdetailsub SET OccuranceTypeID = '"+ S_ReSterileTypeId +"' WHERE ItemStockID = '" + RowID + "' AND ID = '" + ID + "' ";
					       stmt.executeUpdate(sSql_update);
					       
					       btnre.setSclass("btn btn-danger");
					       list.setAttribute("OccuranceTypeID", S_ReSterileTypeId);
					}
					
					
		
				}catch(Exception e) {
					
					e.printStackTrace();
					
					lc_opt.setTooltiptext("");
					lc_opt.setAttribute("ResterileTypeID", null);
					
					btnre.setSclass("btn");
				}
//	            ShowDetail(DocNo);
				Window_PopUp.setVisible(false);
	        }
	    });
		
		vbx.appendChild(cbb);
		vbx.appendChild(new Separator());
		vbx.appendChild(new Separator());
		vbx.appendChild(btn);		
		
		Window_PopUp.appendChild(new Caption("บันทึก ความเสี่ยง"));
		Window_PopUp.appendChild(vbx);
	}
	
	
	private String getSqlDetail(String text_DocNo) {
		
		String S_Sql = "";
		
		S_Sql =
			"SELECT" + 
			"            payout.DocNo," + 
			"            itemstock.UsageCode," +
			"            itemstock.RowID," +
			"            payout.IsStatus," + 
			"            item.itemname," + 
			"            payoutdetailsub.Qty," +
			"            payoutdetailsub.ID," + 
			"            payoutdetailsub.IsStatus AS IsStatusDetail," + 
			"            payoutdetailsub.OccuranceTypeID," + 
			"          (SELECT settingapp.chkreceiveIn" + 
			"            FROM settingapp" + 
			"            WHERE settingapp.DeptID = '"+ S_DeptId +"'  " + 
			"            LIMIT 1 ) AS chkreceiveIn," + 
			"            employee_1.FirstName AS Recipient ," + 
			"            employee_2.FirstName AS Approve ," + 
			"            DATE_FORMAT( DATE(ModifyDate),'%d/%m/%Y' ) AS date_pay," + 
			"            TIME(ModifyDate) AS time_pay" + 
			"          FROM" + 
			"          payout" + 
			"          INNER JOIN payoutdetail ON payout.DocNo = payoutdetail.DocNo" + 
			"					INNER JOIN payoutdetailsub ON payoutdetail.ID = payoutdetailsub.Payoutdetail_RowID" + 
			"          INNER JOIN itemstock ON payoutdetailsub.ItemStockID = itemstock.RowID" + 
			"          INNER JOIN item ON itemstock.ItemCode = item.itemcode" + 
			"          INNER JOIN employee AS employee_1 ON employee_1.EmpCode = payout.RecipientCode" + 
			"      	  INNER JOIN employee AS employee_2 ON employee_2.EmpCode = payout.Approve " + 
			"          WHERE payout.DocNo = '"+ text_DocNo +"'  " + 
			"          AND payout.DeptID = '"+ S_DeptId +"'  " + 
			"          AND itemstock.IsStatus = 4";
		
		System.out.println(S_Sql);
	
		return S_Sql;
	
	}

}
