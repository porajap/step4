package process;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.A;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Window;

import connect.DBConn;
import connect.SqlOperation;
import connect.SqlSelection;
import core.xStock;
import general.DateTime;
import javassist.bytecode.stackmap.BasicBlock.Catch;

public class report_hn extends Borderlayout{

	/**
	 * 
	 */
	public static final long serialVersionUID = -6365073630321342768L;
	
	public ResultSet rs = null;
	public ResultSet rs2 = null;
	public Connection conn = null;
	public Statement stmt = null;
	public Statement stmt2 = null;
	
	public boolean B_IsCreate = true;
	private Session session = Sessions.getCurrent();
	public String xUsername = null;
	public String xPassword = null;
	
	private Datebox SDatebox;
	private Datebox EDatebox;
	private Datebox SDateboxTab2;
	private Datebox EDateboxTab2;
	private Datebox DateboxBefore;
	
	public String Search = null;	
	
	private Listbox ListboxHNTab2;
	private Listbox ListboxHN;
	private Listbox ListboxHNTotal;
	
	private Combobox ComboboxNameHN;
	private Combobox ComboboxNameHN2;
	private Textbox TextboxNameHN;
	private Textbox TextboxQR;
	private Textbox TextboxQR2;
	private Textbox TextboxDocNo;
	private Textbox TextboxNameHN2;
	public Window HNwindow;
	private Label lb_Name;
	
	public Button btnAddEmp;
	public Button btnStock;
	private Checkbox Checkbox_Mode;
	public String xHn_Code;
	
//	public Textbox Hn_w_n;
//	public Textbox Fname_w_n;
//	public Textbox Date_w_n;
//	public Textbox Approve_w_n;
////	public Window HNwindow;
//	public Textbox Date_c_n;
//	public Textbox FItem_c_n;
//	public Textbox Round_c_n;
//	public Textbox sTime_c_n;
//	public Textbox eTime_c_n;
//	public Textbox Approve_c_n;
//	
//	public Textbox Date_w_c;
//	public Textbox FItem_w_c;
//	public Textbox Usage_w_c;
//	public Textbox Approve_c_w;
//	
//	public Textbox Approve_c_c;
//	
//	public Textbox Ready_e_c;
//	public Textbox Check_e_c;
//
//	public Textbox Date_c_s;
//	public Textbox FItem_c_s;
//	public Textbox Round_c_s;
//	public Textbox sTime_c_s;
//	public Textbox eTime_c_s;
//	
////	public String xHn_Code;
//	private String FName;
//	private String DocDate;
	
	public String UsageCodee;
	public String Docno;
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
					usagecode ,
					LastSterileDetailID ,
					hncode1;
	

	public void onCreate() throws Exception{
		init();		
		bySession();
		onDisplay();
		SearchData();
		
	}
	

	
	public void init()throws Exception {
		SqlSelection sql = new SqlSelection();
		sql.uName = xUsername;
		sql.Pwd = xPassword;
		DBConn objConnection = new DBConn();
		conn = objConnection.getConnection("root", "A$192dijd");
		stmt = conn.createStatement();
		stmt2 = conn.createStatement();
		ListboxHNTab2 = ((Listbox) getFellow("ListboxHNTab2"));
		ListboxHN = ((Listbox) getFellow("ListboxHN"));
		ListboxHNTotal = ((Listbox) getFellow("ListboxHNTotal"));
		
		SDatebox = ((Datebox)getFellow("SDatebox"));
		EDatebox = ((Datebox)getFellow("EDatebox"));
		SDateboxTab2 = ((Datebox)getFellow("SDateboxTab2"));
		EDateboxTab2 = ((Datebox)getFellow("EDateboxTab2"));
		DateboxBefore = ((Datebox)getFellow("DateboxBefore"));
		Checkbox_Mode = ((Checkbox)getFellow("Checkbox_Mode"));
		ComboboxNameHN = ((Combobox) getFellow("ComboboxNameHN"));
		ComboboxNameHN2 = ((Combobox) getFellow("ComboboxNameHN2"));
		lb_Name = ((Label) getFellow("lb_Name"));
		TextboxNameHN = ((Textbox) getFellow("TextboxNameHN"));
		TextboxQR = ((Textbox) getFellow("TextboxQR"));
		TextboxQR2 = ((Textbox) getFellow("TextboxQR2"));
		TextboxDocNo = ((Textbox) getFellow("TextboxDocNo"));
		TextboxNameHN2 = ((Textbox) getFellow("TextboxNameHN2"));
		btnAddEmp = ((Button) getFellow("btnAddEmp"));
		btnStock = ((Button) getFellow("btnStock"));
		HNwindow = ((Window) getFellow("HNwindow"));
		defindName("");
		defindName2("");
		
		
		SDatebox.setText(DateTime.getDateNow());
		EDatebox.setText(DateTime.getDateNow());
		SDateboxTab2.setText(DateTime.getDateNow());
		EDateboxTab2.setText(DateTime.getDateNow());
		DateboxBefore.setText(DateTime.getDateNow());
		TextboxNameHN2.setVisible(false);
		TextboxQR2.setVisible(false);
		Checkbox_Mode.isChecked();
		
		ComboboxNameHN.setValue("");
		ComboboxNameHN2.setText("");

//		Hn_w_n = ((Textbox) getFellow("Hn_w_n"));
//		Fname_w_n = ((Textbox) getFellow("Fname_w_n"));
//		Date_w_n = ((Textbox) getFellow("Date_w_n"));
//		Approve_w_n = ((Textbox) getFellow("Approve_w_n"));
//		
//		Date_c_n = ((Textbox) getFellow("Date_c_n"));
//		FItem_c_n = ((Textbox) getFellow("FItem_c_n")); 
//		Round_c_n = ((Textbox) getFellow("Round_c_n"));
//		sTime_c_n = ((Textbox) getFellow("sTime_c_n"));
//		eTime_c_n = ((Textbox) getFellow("eTime_c_n"));
//		Approve_c_n = ((Textbox) getFellow("Approve_c_n"));
//		
//		Date_w_c = ((Textbox) getFellow("Date_w_c"));
//		FItem_w_c = ((Textbox) getFellow("FItem_w_c"));
//		Usage_w_c = ((Textbox) getFellow("Usage_w_c"));
//		Approve_c_w = ((Textbox) getFellow("Approve_c_w"));
//		
//		Approve_c_c = ((Textbox) getFellow("Approve_c_c"));
//		
//		Ready_e_c = ((Textbox) getFellow("Ready_e_c"));
//		Check_e_c = ((Textbox) getFellow("Check_e_c"));
//		
//		Date_c_s = ((Textbox) getFellow("Check_e_c"));
//		FItem_c_s = ((Textbox) getFellow("Check_e_c"));
//		Round_c_s = ((Textbox) getFellow("Check_e_c"));
//		sTime_c_s = ((Textbox) getFellow("Check_e_c"));
//		eTime_c_s = ((Textbox) getFellow("Check_e_c"));
		
		lb_Name.setVisible(false);
		TextboxNameHN.setVisible(false);
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
	
	public void onCheck()throws Exception {
		try {

		if(!Checkbox_Mode.isChecked()) {	
//close
			lb_Name.setVisible(false);
			TextboxNameHN.setVisible(false);
			TextboxNameHN2.setVisible(false);
			TextboxQR2.setVisible(false);
			
			ComboboxNameHN2.setVisible(true);
			TextboxQR.setVisible(true);
			
			
			TextboxDocNo.setText("");
			TextboxQR.setText("");
			TextboxQR2.setText("");
			ListboxHNTotal.getItems().clear();
			ComboboxNameHN2.setText("");
		}else {
//open			
			lb_Name.setVisible(true);
			TextboxNameHN2.setVisible(true);
			TextboxNameHN.setVisible(true);
			TextboxQR2.setVisible(true);
			
			ComboboxNameHN2.setVisible(false);
			TextboxQR.setVisible(false);
			
			TextboxDocNo.setText("");
			TextboxQR.setText("");
			TextboxQR2.setText("");
			ListboxHNTotal.getItems().clear();
			TextboxNameHN2.setText("");
			TextboxNameHN.setText("");
		}
			 
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public String getSql()throws Exception {
		
		try {
//		String Search = ((Textbox) getFellow("TextboxSearchtracking")).getText();
			String sdate = SDateboxTab2.getText();
//			System.out.println("sdate : " +sdate);
			String edate  = EDateboxTab2.getText();
//			System.out.println("edate : " +edate);
			
			
		 String Sql = "SELECT " + 
		 		"            hotpitalnumber.HnCode, " + 
				"			hncode.DocNo,	"+
		 		"            hotpitalnumber.FName, " + 
		 		"            itemstock.UsageCode, " + 
		 		"            hncode_detail.LastSterileDetailID, " + 
		 		"            item.itemname, " + 
		 		"            DATE_FORMAT( hncode.DocDate, '%d/%m/%Y' ) AS DocDate, " + 
		 		"            hotpitalnumber.HnAge, " + 
		 		"            hotpitalnumber.HnMonth, " + 
		 		"            PERIOD_DIFF( " + 
		 		"              DATE_FORMAT( NOW(), '%Y%m' ), " + 
		 		"            DATE_FORMAT( hotpitalnumber.CreateDate, '%Y%m' )) AS DiffMonth  " + 
		 		"          FROM " + 
		 		"            hncode " + 
		 		"            INNER JOIN hncode_detail ON hncode.DocNo = hncode_detail.DocNo " + 
		 		"            INNER JOIN itemstock ON hncode_detail.ItemStockID = itemstock.RowID " + 
		 		"            INNER JOIN item ON itemstock.ItemCode = item.itemcode " + 
		 		"            INNER JOIN hotpitalnumber ON hncode.HnCode = hotpitalnumber.HnCode  " + 
		 		"          WHERE " + 
//		 		"            CONCAT(hotpitalnumber.FName,' : ',hotpitalnumber.HnCode) LIKE '%"+HnCode+"%'   " + 
		 		"             hncode.IsCancel = 0  " + 
		 		"            AND hncode.DeptID = "+S_DeptId+"  " + 
		 		
		 		"            AND DocDate BETWEEN '" + DateTime.convertDate(sdate) + "' AND '" + DateTime.convertDate(edate) + "'  " + 
		 		"			GROUP BY hncode.DocNo "	+
		 		"          ORDER BY  hncode.DocDate DESC ";
		 
		 		

		 		System.out.println("getSql : " +Sql);
		 		return Sql;
		 		
		}catch (Exception e) {
			System.out.println("ERROR getSql : " + e.getMessage());
		}
		return null;
	}
	
	public void onDisplay()throws Exception {
		try {
			rs = stmt.executeQuery(getSql());
			ListboxHNTab2.getItems().clear();
			int no = 1;
			
			while(rs.next()) {
				final String DocNo = rs.getString("DocNo");
				final String HnCode = rs.getString("HnCode");

				Listitem list = new Listitem();
				
				list.appendChild(new Listcell(no + "."));
				list.appendChild(new Listcell(rs.getString("DocNo")));
				list.appendChild(new Listcell(rs.getString("DocDate")));
				
				Listcell lc_HnCode= new Listcell();
				
				A L_HnCoe = new A(rs.getString("FName"));
				
				L_HnCoe.addEventListener("onClick", new EventListener<Event>() {
					public void onEvent(Event event) throws Exception {
						((Tabbox) getFellow("s")).setSelectedIndex(0);
						onDisplayTab1(DocNo,HnCode);
				    	
			        }
			    });
				
				lc_HnCode.appendChild( L_HnCoe );
				list.appendChild( lc_HnCode );
				
				
				
				
				ListboxHNTab2.appendChild(list);
				no++;
			}
		}catch (Exception e) {
			e.printStackTrace();
			Messagebox.show("ERROR onDisplayReport_HN : " + e.getMessage());
			System.out.println("ERROR onDisplayReport_HN : " + e.getMessage());
		}
	}


	
	public void onDisplayTab1(String DocNo,String HnCode)throws Exception {
		String sdate = SDateboxTab2.getText();
		String edate = EDateboxTab2.getText();
		String Sql="SELECT " + 
				"            hotpitalnumber.HnCode, " + 
				"            hotpitalnumber.FName, " + 
				"            itemstock.UsageCode, " + 
				

				
				"            hncode_detail.LastSterileDetailID, " + 
				"            item.itemname, " + 
				"            DATE_FORMAT( hncode.DocDate, '%d/%m/%Y' ) AS DocDate, " + 
				"            hotpitalnumber.HnAge, " + 
				"            hotpitalnumber.HnMonth, " + 
				"            PERIOD_DIFF( " + 
				"            DATE_FORMAT( NOW(), '%Y%m' ), " + 
				"            DATE_FORMAT( hotpitalnumber.CreateDate, '%Y%m' )) AS DiffMonth  " + 
				"          	 FROM hncode " + 
				"            INNER JOIN hncode_detail ON hncode.DocNo = hncode_detail.DocNo " + 
				"            INNER JOIN itemstock ON hncode_detail.ItemStockID = itemstock.RowID " + 
				
				
				"            INNER JOIN item ON itemstock.ItemCode = item.itemcode " + 
				"            INNER JOIN hotpitalnumber ON hncode.HnCode = hotpitalnumber.HnCode  " + 
				"          	 WHERE hncode.DocNo ='"+DocNo+"'" +
				"            AND DocDate BETWEEN '" + DateTime.convertDate(sdate) + "' AND '" + DateTime.convertDate(edate) + "'  " ;
				
//				if(!xSearch.equals("")) {
//					Sql += "  AND ( (hotpitalnumber.HnCode LIKE  '%"+xSearch.replace(" ", "%")+"%') "+
//					"	OR (hotpitalnumber.FName LIKE  '%"+xSearch.replace(" ", "%")+"%') )  " ;
//			 		}
		
		System.out.println("newListcell : " + Sql);
		

		rs = stmt.executeQuery(Sql);
		ListboxHN.getItems().clear();
		int No = 1;
		String fName = null;
		String hncode = null;
//		String sdate = null;
		while(rs.next()) {
			fName = rs.getString("FName");
			hncode = rs.getString("HnCode");
			Listitem list = new Listitem();
			final String hncode1 = rs.getString("HnCode");
			final String usagecode = rs.getString("UsageCode");
			final String LastSterileDetailID = rs.getString("LastSterileDetailID");
			
			list.appendChild(new Listcell(No + "."));
			list.appendChild(new Listcell(rs.getString("DocDate")));
			list.appendChild(new Listcell(rs.getString("UsageCode")));
			
			Listcell Lc_itemname= new Listcell();
			A itemname = new A(rs.getString("itemname"));
			
			itemname.addEventListener("onClick", new EventListener<Event>() {
				public void onEvent(Event event) throws Exception {
					
					try {
						HNwindow.setAttribute("HnCode", hncode1);
						HNwindow.setAttribute("UsageCode", usagecode);
						HNwindow.setAttribute("ID", LastSterileDetailID);
				        openWindow("HNwindow");
				        
				        
				        
					}catch (Exception e) {
						e.printStackTrace();
					}
		        }
		    });
			Lc_itemname.appendChild( itemname );
			list.appendChild( Lc_itemname );

			ListboxHN.appendChild(list);

			No++;
		}
		ComboboxNameHN.setText(fName + " : " + hncode);
		
	}
	
//	public Listcell OpenWinDow(Event e)throws Exception {
//		 //create a window programmatically and use it as a modal dialog.
//        Window window = (Window)Executions.createComponents(
//                "report_hn_window.zul", null, null);
//        window.doModal();
//		return null;
//	}
	public void SearchData() throws Exception {
		onDisplay();

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
	
	public void defindName(String xSearch) throws Exception {
		String HnCode = null;

		if (xSearch != null) {
			if (xSearch.length() >= 1) {
				ComboboxNameHN.getItems().clear();
				ComboboxNameHN.setOpen(false);
				SqlSelection sqlsel = new SqlSelection();
				sqlsel.uName = xUsername;
				sqlsel.Pwd = xPassword;
				try {
					rs = sqlsel.getReSultSQL(getSqlTab1(xSearch));

					while (rs.next()) {
						Comboitem citem = new Comboitem();

						HnCode = rs.getString("FName") + " : " + rs.getString("HnCode") ;
						hncode1 = rs.getString("HnCode");
						citem.setLabel(HnCode);
						citem.setValue(rs.getString("HnCode"));
						

						ComboboxNameHN.appendChild(citem);
					}
					
					System.out.println("Value hncode "+hncode1);
					if (ComboboxNameHN.getItemCount() > 0) {
						ComboboxNameHN.setOpen(true);
						ComboboxNameHN.setText(xSearch);				
						}
					ComboboxNameHN.setText(xSearch);	
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					sqlsel.closeConnection();
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
		SqlSelection sqlsel = new SqlSelection();
		sqlsel.uName = xUsername;
		sqlsel.Pwd = xPassword;
//		System.out.println("ComboboxNameHN : " + xSearch);

		try {
			rs = sqlsel.getReSultSQL(getSqlTab1(xSearch.trim().substring(0, i).replace(" ", "%")));

			if (rs.next()) {
				xHn_Code = rs.getString("HnCode");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlsel.closeConnection();
			if (rs != null) {
				rs.close();
			}
		}
	}
	
	public String getSqlTab2(String xSearch) {
		
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

public void defindName2(String xSearch) throws Exception {
	String HnCode = null;

	if (xSearch != null) {
		if (xSearch.length() >= 1) {
			ComboboxNameHN2.getItems().clear();
			ComboboxNameHN2.setOpen(false);
			SqlSelection sqlsel = new SqlSelection();
			sqlsel.uName = xUsername;
			sqlsel.Pwd = xPassword;
			try {
				rs = sqlsel.getReSultSQL(getSqlTab2(xSearch));

				while (rs.next()) {
					Comboitem citem = new Comboitem();

					HnCode = rs.getString("FName") + " : " + rs.getString("HnCode") ;

					citem.setLabel(HnCode);
					citem.setValue(rs.getString("HnCode"));
					

					ComboboxNameHN2.appendChild(citem);
				}

				if (ComboboxNameHN2.getItemCount() > 0) {
					ComboboxNameHN2.setOpen(true);
					ComboboxNameHN2.setText(xSearch);
				}
				ComboboxNameHN2.setText(xSearch);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				sqlsel.closeConnection();
				if (rs != null) {
					rs.close();
				}
			}
		}
	}
}

public void onName2()throws Exception {
	onNameTH2 (ComboboxNameHN2.getText());

}
private void onNameTH2(String xSearch) throws Exception {
	int i = (Integer) xSearch.trim().indexOf(':');

	if (i < 0)
		return;

	ComboboxNameHN2.getItems().clear();
	SqlSelection sqlsel = new SqlSelection();
	sqlsel.uName = xUsername;
	sqlsel.Pwd = xPassword;
	System.out.println("ComboboxNameHN : " + xSearch);

	try {
		rs = sqlsel.getReSultSQL(getSqlTab2(xSearch.trim().substring(0, i).replace(" ", "%")));

		if (rs.next()) {
			HnCode = rs.getString("HnCode");
		}
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		sqlsel.closeConnection();
		if (rs != null) {
			rs.close();
		}
	}
}
 
public void onDisplayTab1()throws Exception {
	String sdate = SDatebox.getText();
	String edate = EDatebox.getText();
	
	System.out.println("Value hncode : "+hncode1);
	String Sql="SELECT " + 
			"			hncode.DocNo, " +
			"            hotpitalnumber.HnCode, " + 
			"            hotpitalnumber.FName, " + 
			"            itemstock.UsageCode, " + 
			

			"            hncode_detail.LastSterileDetailID, " + 
			"            item.itemname, " + 
			"            DATE_FORMAT( hncode.DocDate, '%d/%m/%Y' ) AS DocDate, " + 
			"            hotpitalnumber.HnAge, " + 
			"            hotpitalnumber.HnMonth, " + 
			"            PERIOD_DIFF( " + 
			"            DATE_FORMAT( NOW(), '%Y%m' ), " + 
			"            DATE_FORMAT( hotpitalnumber.CreateDate, '%Y%m' )) AS DiffMonth  " + 
			"          	 FROM hncode " + 
			"            INNER JOIN hncode_detail ON hncode.DocNo = hncode_detail.DocNo " + 
			"            INNER JOIN itemstock ON hncode_detail.ItemStockID = itemstock.RowID " + 
		

			"            INNER JOIN item ON itemstock.ItemCode = item.itemcode " + 
			"            INNER JOIN hotpitalnumber ON hncode.HnCode = hotpitalnumber.HnCode  " + 
			"          	 WHERE hncode.HnCode ='"+xHn_Code+"'" +
			
			"            AND DocDate BETWEEN '" + DateTime.convertDate(sdate) + "' AND '" + DateTime.convertDate(edate) + "'  " ;
			
//			if(!xSearch.equals("")) {
//				Sql += "  AND ( (hotpitalnumber.HnCode LIKE  '%"+xSearch.replace(" ", "%")+"%') "+
//				"	OR (hotpitalnumber.FName LIKE  '%"+xSearch.replace(" ", "%")+"%') )  " ;
//		 		}
	
	System.out.println("newListcell : " + Sql);
	
	
	rs = stmt.executeQuery(Sql);
	ListboxHN.getItems().clear();
	int No = 1;
	
	while(rs.next()) {
		
		Listitem list = new Listitem();
		
		list.appendChild(new Listcell(No + "."));
		list.appendChild(new Listcell(rs.getString("DocDate")));
		list.appendChild(new Listcell(rs.getString("UsageCode")));
		final String hncode1 = rs.getString("HnCode");
		final String usagecode = rs.getString("UsageCode");
		final String LastSterileDetailID = rs.getString("LastSterileDetailID");
		
		Listcell Lc_itemname= new Listcell();
		A itemname = new A(rs.getString("itemname"));
		
		itemname.addEventListener("onClick", new EventListener<Event>() {
			public void onEvent(Event event) throws Exception {
				
				try {
					HNwindow.setAttribute("HnCode", hncode1);
					HNwindow.setAttribute("UsageCode", usagecode);
					HNwindow.setAttribute("ID", LastSterileDetailID);
			        openWindow("HNwindow");
			        
				}catch (Exception e) {
					e.printStackTrace();
				}
	        }
	    });
		
		Lc_itemname.appendChild( itemname );
		list.appendChild( Lc_itemname );
		

		ListboxHN.appendChild(list);

		No++;
	}

}
public String getSqlCreateDocument(String DocNo)throws Exception {
	
	String Sql = "SELECT " + 
	 		"            hotpitalnumber.HnCode, " + 
			"			hncode.DocNo,	"+
	 		"            hotpitalnumber.FName, " + 
	 		"            hncode_detail.ID, " + 
	 		"            itemstock.UsageCode, " + 
	 		"            hncode_detail.LastSterileDetailID, " + 
	 		"            item.itemname, " + 
	 		"            DATE_FORMAT( hncode.DocDate, '%d/%m/%Y' ) AS DocDate, " + 
	 		"            hotpitalnumber.HnAge, " + 
	 		"            hotpitalnumber.HnMonth, " + 
	 		"            PERIOD_DIFF( " + 
	 		"              DATE_FORMAT( NOW(), '%Y%m' ), " + 
	 		"            DATE_FORMAT( hotpitalnumber.CreateDate, '%Y%m' )) AS DiffMonth  " + 
	 		"          FROM " + 
	 		"            hncode " + 
	 		"            INNER JOIN hncode_detail ON hncode.DocNo = hncode_detail.DocNo " + 
	 		"            INNER JOIN itemstock ON hncode_detail.ItemStockID = itemstock.RowID " + 
	 		"            INNER JOIN item ON itemstock.ItemCode = item.itemcode " + 
	 		"            INNER JOIN hotpitalnumber ON hncode.HnCode = hotpitalnumber.HnCode  " + 
	 		"          WHERE " + 
	 		"             hncode.IsCancel = 0  " + 
	 		"            AND hncode.DocNo = '"+DocNo+"'  " + 
	 		"            AND hncode.DeptID = "+S_DeptId+"  " + 
	 		"          ORDER BY  hncode.DocDate DESC ";
	 		
			
	
	System.out.println("getSqlCreateDocument : " + Sql);
	return Sql;

}
public String getSqlCreateDocument2()throws Exception {
	try {
		String sql = "SELECT CONCAT('HN',SUBSTRING(YEAR(DATE(NOW())),3,4),LPAD(MONTH(DATE(NOW())),2,0),'-', LPAD( (COALESCE(MAX(CONVERT(SUBSTRING(DocNo,8,5),UNSIGNED INTEGER)),0)+1) ,5,0)) AS DocNo  " + 
				"        FROM hncode " + 
				"        WHERE DocNo Like CONCAT('HN',SUBSTRING(YEAR(DATE(NOW())),3,4),LPAD(MONTH(DATE(NOW())),2,0),'%') " + 
				"        ORDER BY DocNo DESC LIMIT 1";
		
		
		System.out.println("getSqlCreateDocument : " + sql);
				return sql;	
	}catch (Exception e) {
		// TODO: handle exception
	}
	return null;
}

public void CreateDocument(String DocNo)throws Exception {
try {
	
	
	rs = stmt.executeQuery(getSqlCreateDocument(DocNo));
	ListboxHNTotal.getItems().clear();
	int No = 1;
	
	while(rs.next()) {
		
		Listitem list = new Listitem();
		
		list.appendChild(new Listcell(No + "."));
		list.appendChild(new Listcell(rs.getString("UsageCode")));
		list.appendChild(new Listcell(rs.getString("itemname")));
		list.appendChild(Delete(rs.getString("ID")));
		
		list.setAttribute("TextboxDocNo", rs.getString("DocNo"));
		ListboxHNTotal.appendChild(list);

		TextboxDocNo.setText(rs.getString("DocNo"));
//		ComboboxNameHN2.setText(rs.getString("HnCode"));
		No++;
	}
}catch (Exception e) {
	e.printStackTrace();
	Messagebox.show("ERROR CreateDocument : " + e.getMessage());
	System.out.println("ERROR CreateDocument : " + e.getMessage());
}
}
public void onDeleteItem(final String ID) throws Exception {
		try {
		stmt.executeUpdate("DELETE FROM hncode_detail WHERE ID = '"+ID+"' ");
		
		System.out.println("DELETE ID : " +ID);
		
	} catch (Exception e) {
		e.printStackTrace();
		Messagebox.show("ERROR Delete : " + e.getMessage());
		System.out.println("ERROR Delete : " + e.getMessage());
	}
		
		    
}
public Listcell Delete(final String ID)throws Exception {
	
	Listcell lc = new Listcell();
	
	Image imgdel = new Image();
	imgdel.setHeight("25px");
	imgdel.setSrc("/img/delete-24.png");
	imgdel.setStyle("align:center;");
	lc.setStyle("align:center; ");
	imgdel.addEventListener("onClick", new EventListener<Event>() {
		public void onEvent(Event event) throws Exception {
			onDeleteItem(ID);
			
			CreateDocument(Docno);
			
		}
		
			});
//	System.out.println("newListcell " + sql);
	
	lc.appendChild(imgdel);
	return lc;
}

public void onClr() throws Exception {	
	ListboxHNTotal.getItems().clear();
	TextboxQR.setText("");
	
	ComboboxNameHN2.setText("");
	TextboxNameHN2.setText("");
	TextboxNameHN.setText("");
	TextboxDocNo.setText("");
}
public void onSaveHn() throws Exception {
	try {
	
	String	DocNo =	TextboxDocNo.getText();

	if(DocNo.equals("")) {
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
				"              '"+HnCode+"', " + 
				"              NOW(), " + 
				"              '"+S_UserId+"', " + 
				"              '"+S_DeptId+"', " + 
				"              0, " + 
				"              '', " + 
				"              0, " + 
				"              '', " + 
				"              0, " + 
				"              '1' " + 
				"            ) ";
		stmt.executeUpdate(insert);
//		CreateDocument(Docno);
		
		onSaveHn_Detail(Docno);
	}else {
		
		String update ="UPDATE hncode SET hncode.ModifyDate = DATE(NOW()) , hncode.UserCode = '"+S_UserId+"', hncode.DeptID = '"+S_DeptId+"' WHERE DocNo = '"+Docno+"' ";
		
		
		
		stmt.executeUpdate(update);
		CreateDocument(Docno);
		onSaveHn_Detail(Docno);
		onDisplay();
	}
	}catch (Exception e) {
		e.printStackTrace();
		Messagebox.show("ERROR onSaveHn : " + e.getMessage());
		System.out.println("ERROR onSaveHn : " + e.getMessage());
	}
}
public void onSaveHn_Detail(String DocNo) throws Exception {
	try {
		
		if(TextboxQR.getText().equals("")) {
			Messagebox.show("ไม่สามารถเพิ่มรายการได้	"," ", Messagebox.YES, Messagebox.INFORMATION);
			
			return;
			}
		
		
	String sql="select UsageCode,RowID,LastSterileDetailID  from itemstock WHERE IsStatus = '5' AND IsPay = '1' AND UsageCode='"+TextboxQR.getText()+"' ";
	String RowID = null;
	String LastSterileDetailID = null;
	String UsageCode = null;
//	String HnCode = null;
	int cnt = 0;
	rs2 = stmt2.executeQuery(sql);
					System.out.println("DocNo= "+sql);	

					
				if(rs2.next()) {
					UsageCode = rs2.getString("UsageCode");
					RowID = rs2.getString("RowID");
					LastSterileDetailID = rs2.getString("LastSterileDetailID");
//					HnCode = rs2.getString("HnCode");
		System.out.println("DocNo= "+DocNo);
	
		
		
		String sqll="SELECT COUNT(hncode_detail.ItemStockID) AS ItemStockID FROM hncode_detail INNER JOIN hncode ON hncode_detail.DocNo = hncode.DocNo "
				+ "	WHERE hncode_detail.DocNo='"+DocNo+"' AND hncode_detail.ItemStockID ='"+RowID+"' ";
		
//		if(HnCode == TextboxNameHN2.getText()){
//			Messagebox.show("มีรายชื่อนี้อยู่แล้ว. \n รหัสลูกค้าเลขที่ : \n [ "
//					+TextboxNameHN2.getText()+" ]" );
//		}
		System.out.println("cnt= ItemStockID "+sqll);	
		rs = stmt.executeQuery(sqll);
		
		if(rs.next()) {
			cnt = rs.getInt("ItemStockID");
			
		}
		System.out.println("cnt= "+cnt);
		
		
		
		if(cnt>0) {
			Messagebox.show("มีรายชื่ออุปกรณ์นี้อยู่แล้ว. \n รหัสใช้งานเลขที่ : \n [ "+UsageCode+" ]" ," ", Messagebox.YES, Messagebox.INFORMATION);
		}else {
		
		

		String insert2 = "INSERT INTO hncode_detail( " + 
				"              hncode_detail.DocNo, " + 
				"              hncode_detail.ItemStockID, " + 
				"              hncode_detail.Qty, " + 
				"              hncode_detail.IsStatus, " + 
				"              hncode_detail.IsCancel, " + 
				"              hncode_detail.B_ID, " + 
				"              hncode_detail.LastSterileDetailID " + 
				"            	)VALUES( " + 
				"              '"+DocNo+"', " + 
				"              '"+RowID+"', " + 
				"               '1', " + 
				"               '1', " + 
				"              	'0', " + 
				"              	'1', " + 
				"               " +isNull(LastSterileDetailID) + " " + 
				"            )  ";
		stmt.executeUpdate(insert2);
		
		}

		
		
					
			}	
		CreateDocument(Docno);

				
		
	}catch (Exception e) {
		e.printStackTrace();
		Messagebox.show("ERROR onSaveHn_Detail : " + e.getMessage());
		System.out.println("ERROR onSaveHn_Detail : " + e.getMessage());
	}
}
public String generateDoc() throws Exception{
    try {
    	
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
private String isNull(String Value){
	return Value == null ? null : ("'" + Value + "'");
}


public void oncheck_save() throws Exception {
	
	if(!Checkbox_Mode.isChecked()) {	

		onSaveHn();
	}else {
		onSaveNewEmp();
}
	
}
public void onSaveNewEmp() throws Exception {
	try {
		
		String sqll="SELECT COUNT(*) AS cnt FROM hotpitalnumber WHERE HnCode ='"+TextboxNameHN2.getText()+"'";
		int cnt =0;
		rs= stmt.executeQuery(sqll);
		if(rs.next()) {
			cnt = rs.getInt("cnt");
		}
		
		if(cnt>0) {
			
			String updatee = "UPDATE  hotpitalnumber SET CreateDate = NOW() WHERE HnCode ='"+TextboxNameHN2.getText()+"'";
			stmt.executeUpdate(updatee);
//			Messagebox.show("มีรายชื่อHNนี้อยู่แล้ว. \n รหัสHNเลขที่ : \n [ "+TextboxNameHN2.getText()+" ]" );
		}else {
		
		String insert2 = "INSERT INTO hotpitalnumber( " + 
				"              hotpitalnumber.FName, " + 
				"              hotpitalnumber.CreateDate, " + 
				"              hotpitalnumber.HnCode " + 
				"              )VALUES( " + 
				"              '"+TextboxNameHN.getText()+"', " + 
				"              NOW(), " + 
				"              '"+TextboxNameHN2.getText()+"' " + 
				
				"            ) ";
		stmt.executeUpdate(insert2);
		}
	String	DocNo =	TextboxDocNo.getText();

	
	
	if(DocNo.equals("")) {
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
				"              '"+TextboxNameHN2.getText()+"', " + 
				"              NOW(), " + 
				"              '"+S_UserId+"', " + 
				"              '"+S_DeptId+"', " + 
				"              0, " + 
				"              '', " + 
				"              0, " + 
				"              '', " + 
				"              0, " + 
				"              '1' " + 
				"            ) ";
		stmt.executeUpdate(insert);
//		CreateDocument(Docno);
		
		onSaveNewEmp2(Docno);
	}else {
		
		String update ="UPDATE hncode SET hncode.ModifyDate = DATE(NOW()) , hncode.UserCode = '"+S_UserId+"', hncode.DeptID = '"+S_DeptId+"' WHERE DocNo = '"+Docno+"' ";
		
		stmt.executeUpdate(update);
		CreateDocument(Docno);
		onSaveNewEmp2(Docno);
		onDisplay();
	}
	}catch (Exception e) {
		e.printStackTrace();
		Messagebox.show("ERROR onSaveHn : " + e.getMessage());
		System.out.println("ERROR onSaveHn : " + e.getMessage());
	}
}
public void onSaveNewEmp2(String DocNo) throws Exception {
try {
		
		if(TextboxQR2.getText().equals("")) {
			Messagebox.show("ไม่สามารถเพิ่มรายการได้	"," ", Messagebox.YES, Messagebox.INFORMATION);
			
			return;
			}
		
		
	String sql="select UsageCode,RowID,LastSterileDetailID  from itemstock WHERE IsStatus = '5' AND IsPay = '1' AND UsageCode='"+TextboxQR2.getText()+"' ";
	String RowID = null;
	String LastSterileDetailID = null;
	String UsageCode = null;
//	String HnCode = null;

	int cntHn = 0;
	rs2 = stmt2.executeQuery(sql);
					System.out.println("DocNo= "+sql);	

					
				if(rs2.next()) {
					UsageCode = rs2.getString("UsageCode");
					RowID = rs2.getString("RowID");
					LastSterileDetailID = rs2.getString("LastSterileDetailID");
//					HnCode = rs2.getString("HnCode");
		System.out.println("DocNo= "+DocNo);
	
		
		
		String sqll="SELECT COUNT(hncode_detail.ItemStockID) AS ItemStockID FROM hncode_detail INNER JOIN hncode ON hncode_detail.DocNo = hncode.DocNo "
				+ "	WHERE hncode_detail.DocNo='"+DocNo+"' AND hncode_detail.ItemStockID ='"+RowID+"' ";
		
//		
		System.out.println("cnt= ItemStockID "+sqll);	
		rs = stmt.executeQuery(sqll);
		
		if(rs.next()) {
			cntHn = rs.getInt("ItemStockID");
			
		}
		System.out.println("cntHn= "+cntHn);
		
		
		
		if(cntHn>0) {
			Messagebox.show("มีรายชื่ออุปกรณ์นี้อยู่แล้ว. \n รหัสใช้งานเลขที่ : \n [ "+UsageCode+" ]" ," ", Messagebox.YES, Messagebox.INFORMATION);
			System.out.println("AAAAAAAAAAAA");
		}else {
		
		

		String insert2 = "INSERT INTO hncode_detail( " + 
				"              hncode_detail.DocNo, " + 
				"              hncode_detail.ItemStockID, " + 
				"              hncode_detail.Qty, " + 
				"              hncode_detail.IsStatus, " + 
				"              hncode_detail.IsCancel, " + 
				"              hncode_detail.B_ID, " + 
				"              hncode_detail.LastSterileDetailID " + 
				"            	)VALUES( " + 
				"              '"+DocNo+"', " + 
				"              '"+RowID+"', " + 
				"               '1', " + 
				"               '1', " + 
				"              	'0', " + 
				"              	'1', " + 
				"               " +isNull(LastSterileDetailID) + " " + 
				"            )  ";
		stmt.executeUpdate(insert2);
		System.out.println("SSSSSSSSSSSSSSsssss");
		}

		
		
					
			}	
		CreateDocument(Docno);

				
		
	}catch (Exception e) {
		e.printStackTrace();
		Messagebox.show("ERROR onSaveHn_Detail : " + e.getMessage());
		System.out.println("ERROR onSaveHn_Detail : " + e.getMessage());
	}
}
public void openWindow(String StrWindow) throws Exception {
	((Window) getFellow(StrWindow)).setVisible(true);
	((Window) getFellow(StrWindow)).setFocus(true);
	((Window) getFellow(StrWindow)).setPosition("center");
	((Window) getFellow(StrWindow)).setMode("modal");
}


public void closeWindow(String StrWindow) throws Exception {
	((Window) getFellow(StrWindow)).setMode("embedded");
	((Window) getFellow(StrWindow)).setVisible(false);
	((Window) getFellow(StrWindow)).setFocus(false);
}

public void onUpdate() throws Exception {
	try {
	
	String	DocNo =	TextboxDocNo.getText();
	
		if(DocNo.trim().equals("")) {
			
		}else {
		
		String update ="UPDATE hncode SET hncode.IsStatus = 1  WHERE DocNo = '"+DocNo+"' ";
		
		stmt.executeUpdate(update);
		Messagebox.show("บันทึกสำเร็จ"," ", Messagebox.YES, Messagebox.INFORMATION);
		onClr();
		onDisplay();
		}
	}catch (Exception e) {
		e.printStackTrace();
		Messagebox.show("ERROR onSaveHn : " + e.getMessage());
		System.out.println("ERROR onSaveHn : " + e.getMessage());
	}
}


}
