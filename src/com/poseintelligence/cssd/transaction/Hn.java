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
import com.poseintelligence.cssd.transaction.DepartmentSendSterile2;

@SuppressWarnings({ "rawtypes", "serial", "unused" })
public class Hn extends Window{
	
	private Session session = Sessions.getCurrent();
	private Combobox ComboboxNameHN;
	private Textbox Textbox_hn_name;
	private Button Button_addHN;
	private Button Button_saveHN;
	private Button Button_deleteHN;
	public String Docno;
	public Checkbox Checkbox_hn;

	public String  DocNo ;
	public String  FName_hn ;
	public String  HnCode_hn ;

	public String xHn_Code;
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
		
		Button_addHN = ((Button) getFellow("Button_addHN"));
		Button_saveHN = ((Button) getFellow("Button_saveHN"));
		Button_deleteHN = ((Button) getFellow("Button_deleteHN"));
//		Checkbox_hn = ((Checkbox) getFellow("Checkbox_hn"));

		ComboboxNameHN = ((Combobox) getFellow("ComboboxNameHN"));
		Textbox_hn_name = ((Textbox) getFellow("Textbox_hn_name"));
		defindName("");
		

		
	
	}

	public void onDisplaywindow()throws Exception {

		
			FName_hn =  (String) ((Window) getFellow("Window_PopUp_hn")).getAttribute("FName_hn");
			HnCode_hn =  (String) ((Window) getFellow("Window_PopUp_hn")).getAttribute("HnCode_hn");
			
			System.out.println(HnCode_hn);
			System.out.println(FName_hn);
	
			ComboboxNameHN.setText(HnCode_hn);
			Textbox_hn_name.setText(FName_hn);

	}

	public void timer() {
		
		((Timer) getFellow("Time")).start();

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

	public void defindName(String xSearch) throws Exception {
		String HnCode = null;


		if (xSearch != null) {
			if (xSearch.length() >= 1) {
				ComboboxNameHN.setOpen(false);
				ComboboxNameHN.getItems().clear();

				com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
		        Class.forName(c.S_MYSQL_DRIVER);
		        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
		        conn.setAutoCommit(false);
		        
		        Statement stmt = conn.createStatement();
		    	ResultSet rs = null;
				
				
				try {
					String sSql = "SELECT " + 
							"                      hotpitalnumber.HnCode,  " + 
							"                      hotpitalnumber.Id , " + 
							"                      hotpitalnumber.FName " + 
							"                    FROM  hotpitalnumber";
					if(!xSearch.equals("")) {
						sSql += "  WHERE ( (hotpitalnumber.HnCode LIKE  '%"+xSearch.replace(" ", "%")+"%') "+
								"	OR (hotpitalnumber.FName LIKE  '%"+xSearch.replace(" ", "%")+"%') )  " ;

					}
					
					System.out.println(sSql);

					rs = stmt.executeQuery(sSql);
					while(rs.next()){

						Comboitem citem = new Comboitem();

						HnCode = rs.getString("FName") + " : " + rs.getString("HnCode") ;

						citem.setLabel(HnCode);
						citem.setValue(rs.getString("HnCode"));
						

						ComboboxNameHN.appendChild(citem);
					}

					if (ComboboxNameHN.getItemCount() > 0) {
						ComboboxNameHN.setOpen(true);
					}
				
					} catch (Exception e) {
					e.printStackTrace();
				} finally {
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
	}
	
 	public void onName()throws Exception {
		onNameTH (ComboboxNameHN.getText());

	}
	
 	private void onNameTH(String xSearch) throws Exception {
		int i = (Integer) xSearch.trim().indexOf(':');

		if (i < 0)
			return;

		ComboboxNameHN.getItems().clear();
		
		
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;
//		System.out.println("ComboboxNameHN : " + xSearch);

		try {
			rs = stmt.executeQuery(getSqlTab1(xSearch.trim().substring(0, i).replace(" ", "%")));

			if (rs.next()) {
				xHn_Code = rs.getString("HnCode");
				ComboboxNameHN.setText(rs.getString("HnCode"));
				Textbox_hn_name.setText(rs.getString("FName"));
				Button_addHN.setVisible(false);
				Button_saveHN.setVisible(true);
			}	
	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}
	
 	public String getSqlTab1(String xSearch) {
		
		String Sql="SELECT " + 
				"                      hotpitalnumber.HnCode,  " + 
				"                      hotpitalnumber.Id , " + 
				"                      hotpitalnumber.FName " + 
				"                    FROM  hotpitalnumber";
				if(!xSearch.equals("")) {
				Sql += "  WHERE ( (hotpitalnumber.HnCode LIKE  '%"+xSearch.replace(" ", "%")+"%') "+
				"	OR (hotpitalnumber.FName LIKE  '%"+xSearch.replace(" ", "%")+"%') )  " ;
		 		}
				
		
		System.out.println("getSqlTab1 : " + Sql);
		return Sql;

}

 	public void CreateDocument_HN()throws Exception {
			com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
 	        Class.forName(c.S_MYSQL_DRIVER);
 	        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
 	        conn.setAutoCommit(false);
 	        
 	        Statement stmt = conn.createStatement();
 	    	ResultSet rs = null;
 	    	
		DocNo =  (String) ((Window) getFellow("Window_PopUp_hn")).getAttribute("DocNo");
		
		String HnCode_add = ComboboxNameHN.getText();
		System.out.println(DocNo);
		
		try {
			

				Docno = generateDoc();
				
				System.out.println("gen Doc = "+Docno);
				String insert = "INSERT INTO hncode( " + 
						"              hncode.DocNo, " + 
						"              hncode.DocDate, " + 
						"              hncode.HnCode, " + 
						"              hncode.ModifyDate, " + 
						"              hncode.UserCode, " + 
						"              hncode.DeptID, " + 
						"              hncode.Qty, " + 
						"              hncode.DocNo_SS, " + 
						"              hncode.IsStatus, " + 
						"              hncode.Remark, " + 
						"              hncode.IsCancel, " + 
						"              hncode.B_ID " + 
						"            	)VALUES( " + 
						"              '"+Docno+"', " + 
						"              DATE(NOW()), " + 
						"              '"+HnCode_add+"', " + 
						"              NOW(), " + 
						"              '"+S_UserId+"', " + 
						"              '"+S_DeptId+"', " + 
						"              0, " + 
						"               '"+DocNo+"', " + 
						"              0, " + 
						"              '', " + 
						"              0, " + 
						"              '1' " + 
						"            ) ";
				stmt.executeUpdate(insert);

				String update ="UPDATE sendsterile SET sendsterile.IsHn = 1  WHERE sendsterile.DocNo = '"+DocNo+"' ";
				stmt.executeUpdate(update);
				
			}catch (Exception e) {
				e.printStackTrace();
				Messagebox.show("ERROR onSaveHn : " + e.getMessage());
				System.out.println("ERROR onSaveHn : " + e.getMessage());
			}finally {
				((Window) getFellow("Window_PopUp_hn")).setMode("embedded");
				((Window) getFellow("Window_PopUp_hn")).setVisible(false);
			}

	}
 	
 	public String generateDoc() throws Exception{

 	    try {
 			com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
 	        Class.forName(c.S_MYSQL_DRIVER);
 	        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
 	        conn.setAutoCommit(false);
 	        
 	        Statement stmt = conn.createStatement();
 	    	ResultSet rs = null;
 	    	
 		        String sql = "SELECT CONCAT('HN',SUBSTRING(YEAR(DATE(NOW())),3,4),LPAD(MONTH(DATE(NOW())),2,0),'-', LPAD( (COALESCE(MAX(CONVERT(SUBSTRING(DocNo,8,5),UNSIGNED INTEGER)),0)+1) ,5,0)) AS DocNo " + 
 						"        FROM hncode " + 
 						"        WHERE DocNo Like CONCAT('HN',SUBSTRING(YEAR(DATE(NOW())),3,4),LPAD(MONTH(DATE(NOW())),2,0),'%') " + 
 						"        ORDER BY DocNo DESC LIMIT 1";
 		      
 		  rs = stmt.executeQuery(sql);
 		  if(rs.next()) {
 			  Docno = rs.getString("DocNo");
 		  }
 		  
 		  ;
 		  System.out.println(sql);
 		
 		  
 		  
 	}catch (Exception e) {
 		e.printStackTrace();
 		Messagebox.show("ERROR generateDoc : " + e.getMessage());
 		System.out.println("ERROR generateDoc : " + e.getMessage());
 	}
 	    return Docno;
 	}

 	public void DeleteHN()throws Exception {
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
	        Class.forName(c.S_MYSQL_DRIVER);
	        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
	        conn.setAutoCommit(false);
	        
	        Statement stmt = conn.createStatement();
	    	ResultSet rs = null;
	    	
	DocNo =  (String) ((Window) getFellow("Window_PopUp_hn")).getAttribute("DocNo");


	
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
			
//			DepartmentSendSterile2 filesend = new DepartmentSendSterile2();
//			filesend.Checkbox_hn = Checkbox_hn;
		}

}
}
