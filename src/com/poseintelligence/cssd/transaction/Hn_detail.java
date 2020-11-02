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
import org.zkoss.zul.Button;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import com.poseintelligence.cssd.document.SendSterile;
import com.poseintelligence.cssd.model.ModelMaster;
import com.poseintelligence.cssd.utillity.DateTime;
import org.zkoss.zk.ui.util.GenericForwardComposer;
@SuppressWarnings({ "unused", "serial" })
public class Hn_detail  extends Window{
	private Session session = Sessions.getCurrent();
	
	private Textbox Hn_detail;
	private Textbox Date_detail;
	private Textbox LSName_detail;
	private Textbox Operation_detail;
	private Listbox Listbox_HnDetail;
	public String  DocHn ;

	
	public String 	S_UserId,
	S_DeptId,
					S_UserName,
					S_IsAdmin,
					S_EmpCode,
					S_DepName,
					B_ID,
					S_DB,
					i_itemcode,
					sDatex,
					eDatex,
					HnCode,
					UsageCode ,
					LastSterileDetailID ,
					hncode1;
	
	
	
	public void onCreate() throws Exception {

		bySession();
		init();
//		timer();
    }
	
	private void init() throws Exception{
		
		Listbox_HnDetail = ((Listbox) getFellow("Listbox_HnDetail"));
		Hn_detail = ((Textbox) getFellow("Hn_detail"));
		Date_detail = ((Textbox) getFellow("Date_detail"));
		LSName_detail = ((Textbox) getFellow("LSName_detail"));
		Operation_detail = ((Textbox) getFellow("Operation_detail"));

	}
	
	public void bySession()throws Exception{
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
				i_itemcode = (String)session.getAttribute("itemcode");
				HnCode = (String)session.getAttribute("HnCode");
				sDatex = (String)session.getAttribute("sDate");
				eDatex = (String)session.getAttribute("eDate");
	        }
			
			
		
		}catch (Exception e) {
			System.out.println("ERROR bySession : " + e.getMessage());
		}
	}
	
	
	public void onDisplaywindow()throws Exception {

			com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
	        Class.forName(c.S_MYSQL_DRIVER);
	        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
	        conn.setAutoCommit(false);
	        
	        Statement stmt = conn.createStatement();
	    	ResultSet rs = null;
	    	
			try{	
				
		    	DocHn =  (String) ((Window) getFellow("Window_PopUp_hn_detail")).getAttribute("DocNo");
		    	
		    	System.out.println(DocHn);
				
				String sSql_send = "SELECT" + 
						"                  item.itemname," + 
						"                  hncode.HnCode," + 
						"                  itemstock.UsageCode," + 
						"                  hncode.DocNo," + 
						"                  hncode.Remark," + 
						"                  DATE_FORMAT( hncode.DocDate, '%d-%m-%Y' ) AS DocDate," + 
						"                  hncode_detail.ItemStockID," + 
						"                  hotpitalnumber.FName " + 
						"                FROM" + 
						"                  hncode" + 
						"                  INNER JOIN hncode_detail ON hncode.DocNo = hncode_detail.DocNo" + 
						"                  INNER JOIN itemstock ON hncode_detail.ItemStockID = itemstock.RowID" + 
						"                  INNER JOIN item ON itemstock.ItemCode = item.itemcode" + 
						"                  INNER JOIN hotpitalnumber ON hncode.HnCode = hotpitalnumber.HnCode " + 
						"                WHERE" + 
						"                  hncode.DocNo = '" + DocHn + "'  ";
			rs = stmt.executeQuery(sSql_send);
			
			Listbox_HnDetail.getItems().clear();
			
			int I_No = 1;
			
			while(rs.next()){
				
				Hn_detail.setText(rs.getString("HnCode"));
				Date_detail.setText(rs.getString("FName"));
				LSName_detail.setText(rs.getString("FName"));
				
				Listitem list = new Listitem();
				Button btn = new Button();				
		        Listcell cell = new Listcell();
				btn.setIconSclass("z-icon-trash");
				btn.setSclass("btn");
		        String DocNo = rs.getString("DocNo");
		        String ItemStockID = rs.getString("ItemStockID");
				
				btn.addEventListener("onClick", new EventListener<Event>() {
					public void onEvent(Event event) throws Exception {
						
						Deleteitem(DocNo,ItemStockID);
			        }
			    });
				
				cell.appendChild(btn);
				list.appendChild(new Listcell(I_No + "."));
				list.appendChild(new Listcell(rs.getString("itemname")));
				list.appendChild(new Listcell(rs.getString("UsageCode")));
				list.appendChild(cell);
				
				
				Listbox_HnDetail.appendChild(list);
                
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
	
	public void Deleteitem(final String DocNo , final String ID) throws Exception{
		
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;
    	
		try{	
			

		        
		       String sSql_updateSend = " DELETE FROM hncode_detail WHERE ItemStockID = '" + ID + "'  AND hncode_detail.DocNo = '" + DocNo + "'  ";
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
            
//            
            onDisplaywindow();
		}
    	
    	
	}
}
