package process;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import org.zkoss.zhtml.Cite;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.ComponentNotFoundException;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.A;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Image;
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
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Window;

import com.lowagie.text.List;
import com.lowagie.text.ListItem;
import com.sun.java.swing.plaf.windows.resources.windows;

import connect.DBConn;
import connect.SqlSelection;
import general.DateTime;
import general.Number;
import general.Report.Reports;

public class payout_section extends Tabbox{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6365073630321342768L;
	
	ResultSet rs = null;
	ResultSet rs1 = null;
	ResultSet rs2 = null;
	ResultSet rs3 = null;
	private Connection conn = null;
	private Statement stmt = null;
	private Statement stmt1 = null;
	private Statement stmt2 = null;
	private Statement stmt3 = null;
	public boolean B_IsCreate = true;
	private Session session = Sessions.getCurrent();
	private String xUsername = null;
	private String xPassword = null;
	private String Search = null;	
	public String xSearch = null;	
	public String DocNo = null;
	public String Docno;
	public String dep;
	private Datebox DateboxTab2;
	private Datebox DateboxTab22;
	private Datebox Datebox1;
	private Combobox ComboboxpayoutTab1;
	
	
	public Combobox combobox_report;
	public Datebox sdate_report;
	public Datebox edate_report;
	public Button view_report;
	
	public Button btnDelete;
	public Button btnAdd;
	public Button btnLend;
	public Button btnCreate1;
	public Button btnSearch1;
	public Button btnSearchTab2_2;
//	public Checkbox chk ;
	
	private boolean IsFinish = false;
	private Textbox TextboxSearchpayoutTab2;
	
	private Listbox Listboxpayout_sectionTab1;
	public Listbox Listboxpayout_sectionTab2;
	public Listbox Listboxpayout_sectionTab1_2;
	public Listbox Listboxpayout_sectionSouthTab1_2;
	public Listbox Listboxpayout_sectionTab2_2;
	private Textbox TextboxSearchpayout;
	private Textbox TextboxSearchpayoutTab1;
	private Textbox TextboxSearchpayoutSouthTab1;
	
	private Textbox TextboxSearchpayoutDateTab2;
	private Textbox TextboxSearchpayoutStatusTab2_1;
	private Textbox TextboxSearchpayoutDepTab2_2;
	public String 	S_UserId,
					S_DeptId,
					Dep_Id,
					S_UserName,
					S_IsAdmin,
					S_EmpCode,
					S_DepName,
					DevRefID,
					B_ID,
					S_DB,
					i_itemcode,
					sDatex,
					eDatex,
					HnCode;
	

	public void onCreate() throws Exception{
		init();	
		getDep("");
		bySession();
		onDisplay();
		SearchData();
		onDisplayTab2();
		
	}
	

	
	private void init()throws SQLException {
		SqlSelection sql = new SqlSelection();
		sql.uName = xUsername;
		sql.Pwd = xPassword;
		DBConn objConnection = new DBConn();
		conn = objConnection.getConnection("root", "A$192dijd");
		stmt = conn.createStatement();
		stmt1 = conn.createStatement();
		stmt2 = conn.createStatement();
		stmt3 = conn.createStatement();
		
		Listboxpayout_sectionTab1 = ((Listbox) getFellow("Listboxpayout_sectionTab1"));
		Listboxpayout_sectionTab2 = ((Listbox) getFellow("Listboxpayout_sectionTab2"));
		Listboxpayout_sectionTab1_2 = ((Listbox) getFellow("Listboxpayout_sectionTab1_2"));
		Listboxpayout_sectionSouthTab1_2 = ((Listbox) getFellow("Listboxpayout_sectionSouthTab1_2"));
		Listboxpayout_sectionTab2 = ((Listbox) getFellow("Listboxpayout_sectionTab2"));
		Listboxpayout_sectionTab2_2 = ((Listbox) getFellow("Listboxpayout_sectionTab2_2"));
		DateboxTab2 = ((Datebox)getFellow("DateboxTab2"));
		DateboxTab22 = ((Datebox)getFellow("DateboxTab22"));
		btnSearch1 = ((Button)getFellow("btnSearch1")); 
		btnDelete = ((Button)getFellow("btnDelete")); 
		btnAdd = ((Button)getFellow("btnAdd")); 
		btnLend = ((Button)getFellow("btnLend")); 
		btnCreate1 = ((Button)getFellow("btnCreate1")); ;
		btnSearchTab2_2 = ((Button)getFellow("btnSearchTab2_2")); 
		Datebox1 = ((Datebox)getFellow("Datebox1"));
		ComboboxpayoutTab1 = ((Combobox) getFellow("ComboboxpayoutTab1"));
		TextboxSearchpayoutTab2 = ((Textbox) getFellow("TextboxSearchpayoutTab2"));
		TextboxSearchpayout = ((Textbox) getFellow("TextboxSearchpayout"));
		TextboxSearchpayoutTab1 = ((Textbox) getFellow("TextboxSearchpayoutTab1"));
		TextboxSearchpayoutSouthTab1 = ((Textbox) getFellow("TextboxSearchpayoutSouthTab1"));
		TextboxSearchpayoutDateTab2 = ((Textbox) getFellow("TextboxSearchpayoutDateTab2"));
		TextboxSearchpayoutStatusTab2_1 = ((Textbox) getFellow("TextboxSearchpayoutStatusTab2_1"));
		TextboxSearchpayoutDepTab2_2 = ((Textbox) getFellow("TextboxSearchpayoutDepTab2_2"));
		
		
//		combobox_report = ((Combobox) getFellow("combobox_report"));
//		sdate_report = ((Datebox)getFellow("sdate_report"));
//		edate_report = ((Datebox)getFellow("edate_report"));
//		view_report = ((Button)getFellow("view_report"));
//		chk = ((Checkbox) getFellow("chk"));
		
		DateboxTab2.setText(DateTime.getDateNow());
		DateboxTab22.setText(DateTime.getDateNow());
		Datebox1.setText(DateTime.getDateNow());
		ComboboxpayoutTab1.setSelectedIndex(0);
		DevRefID = ComboboxpayoutTab1.getSelectedItem().getValue();
	}
	
	private void bySession()throws Exception{
		try {
		

			if (session.getAttribute("S_UserId") == null) {
				Executions.sendRedirect("/index.zul");
			} else {	
				S_UserId = (String)session.getAttribute("S_UserId");
				S_DeptId = (String)session.getAttribute("S_DeptId");
				S_UserName = (String)session.getAttribute("S_UserName");
				S_IsAdmin = (String)session.getAttribute("S_IsAdmin");
				S_EmpCode = (String)session.getAttribute("S_EmpCode");
				S_DepName = (String)session.getAttribute("S_DepName");
				DevRefID = (String)session.getAttribute("DevRefID");
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
	
	
	
	
	public String getSql()throws Exception {
		
		try {

			Search = TextboxSearchpayout.getText();
			
		 String Sql = "SELECT itemstock.RowID,itemstock.UsageCode,item.itemname,units.UnitName,DATE(itemstock.ExpireDate) AS ExpireDate  " + 
		 		"  FROM itemstock  " + 
		 		"  INNER JOIN item ON itemstock.ItemCode = item.itemcode  " + 
		 		"  INNER JOIN units ON item.UnitID = units.ID  " + 
		 		"  WHERE itemstock.IsNewUsage = 0   " + 
		 		"  AND itemstock.IsNew = 0   " + 
		 		"  AND itemstock.IsStatus = 5   " + 
		 		"  AND itemstock.IsPay = 1  " + 
		 		"  AND itemstock.IsDispatch = 0   " ; 
		 		
 				if(!Search.equals("")) {
		 		Sql += "  AND ( (itemstock.UsageCode LIKE  '%"+Search.replace(" ", "%")+"%') "+
		 		"	OR (item.itemname LIKE  '%"+Search.replace(" ", "%")+"%') )  " ;
 				}
		 
		 		Sql += "  AND itemstock.DepID = '"+S_DeptId+"'  " +
		 		"  ORDER BY itemstock.ExpireDate ASC,itemstock.UsageCode ASC Limit 50 ";
		 
		 		

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
			
			Listboxpayout_sectionTab1.getItems().clear();
			int no = 1;
			
			while(rs.next()) {
				Listitem list = new Listitem();
				
				list.appendChild(new Listcell(no + "."));
				list.appendChild(new Listcell(rs.getString("UsageCode")));
				list.appendChild(new Listcell(rs.getString("itemname")));
				list.appendChild(new Listcell(rs.getString("UnitName")));
				list.appendChild(new Listcell(rs.getString("ExpireDate")));

				list.setAttribute("UsageCode", rs.getString("UsageCode"));
				list.setAttribute("RowID", rs.getString("RowID"));
				Listboxpayout_sectionTab1.appendChild(list);
				no++;
			}
		}catch (Exception e) {
			e.printStackTrace();
			Messagebox.show("ERROR onDisplayReport_HN : " + e.getMessage());
			System.out.println("ERROR onDisplay : " + e.getMessage());
		}
	}


	
	public String getSqlDep(String xSearch) {
		
		String Sql="SELECT " + 
				"                      department.DepName,  " + 
				"                      department.ID , " + 
				"                    FROM  department";
				if(!xSearch.equals("")) {
				Sql += "  WHERE ( (department.ID LIKE  '%"+xSearch.replace(" ", "%")+"%') "+
				"	OR (department.DepName LIKE  '%"+xSearch.replace(" ", "%")+"%') )  " ;
		 		}
				
		
		System.out.println("getSqlDep : " + Sql);
		return Sql;

}
	public void SearchData() throws Exception {
		
		onDisplay();		
		

	}
	
	public void getDep(String string)throws Exception {

		try {	
			
			// SELECT Combobox getDep			
			String Sql = "SELECT ID,DepName FROM department";	
//			System.out.println(" SQL Combobox getDep " + Sql);
		rs = stmt.executeQuery(Sql);		
		ComboboxpayoutTab1.getItems().clear();

		while(rs.next()) {
			Comboitem citem = new Comboitem();
			citem.setLabel(rs.getString("DepName"));
			citem.setValue(rs.getString("ID"));

			ComboboxpayoutTab1.appendChild(citem);
			
//			ComboboxpayoutTab1.appendItem(rs.getString("DepName"));	
			
			}
//		ComboboxpayoutTab1.appendItem(rs.getString("DepName"));
		
		}catch(Exception e) {
			System.out.println(" SQL Combobox getDep " + e.getMessage());
		}
	}
	
	public String getDocument (String DocNo)throws Exception {
	
		
		String getDoc = "SELECT  " + 
				"            payout.ID AS payID, " + 
				"            payout.DocNo, " + 
				"            DATE(payout.CreateDate) AS DocDate, " + 
				"            payout.ModifyDate, " + 
				"            payout.Qty, " + 
				"            department.ID, " + 
				"            departmentRec.DepName, " + 
				"			 CASE payout.IsBorrowStatus " + 
				"            WHEN '6' THEN " + 
			 	"              'ยืมร่าง' " + 
			 	"            WHEN '7' THEN " + 
			 	"              'ยืมบันทึก (รอคืน)' " + 
			 	"            WHEN '8' THEN " + 
			 	"              'คืน (ค้างบางตัว)' " + 
			 	"            WHEN '9' THEN " + 
			 	"              'คืนครบ' " + 
			 	"            END AS borrowName " + 
				"          FROM  " + 
				"            payout " + 
				"          INNER JOIN  " + 
				"            department ON payout.DeptID = department.ID " + 
				"          LEFT JOIN  " + 
				"            department As departmentRec ON payout.DeptRec = departmentRec.ID " + 
				" " + 
				"          WHERE  " + 
				"            payout.IsStatus = 10 " + 
				"          AND  " + 
				"            (payout.IsBorrowStatus = 6 OR payout.IsBorrowStatus = 7) " + 
				"          AND  " + 
				"            payout.IsCancel = 0 " + 
				"          AND  " + 
				"            DATE(payout.CreateDate) = '"+DateTime.convertDate(Datebox1.getText())+"' " ;
						if(!DocNo.equals("")) {
				        getDoc+= " AND   payout.DocNo LIKE '%"+DocNo+"%' " ;
						}
						 getDoc+=" ORDER BY  " ;
						getDoc+="  payout.DocNo DESC ";
		
		System.out.println("getSqlCreateDocument : " + getDoc);
		return getDoc;
	}
	
	public void onDisPlayDocument(String DocNo)throws Exception {
		try {
			
			
			
			System.out.println("get text DocNo2222 : " + DocNo);
			rs = stmt.executeQuery(getDocument(DocNo));
			Listboxpayout_sectionTab1_2.getItems().clear();
			int No = 1;
			
			while(rs.next()) {
				Listitem list = new Listitem();
				
				if(!rs.getString("borrowName").equals("ยืมบันทึก (รอคืน)"
						)) {
					
					list.appendChild(View(rs.getString("DocNo")));
					list.appendChild(new Listcell(No + "."));
					list.appendChild(new Listcell(rs.getString("DocNo")));
					list.appendChild(new Listcell(rs.getString("DocDate")));
					list.appendChild(new Listcell(rs.getString("Qty")));
					list.appendChild(new Listcell(rs.getString("DepName")));
					list.appendChild(new Listcell(rs.getString("borrowName")));
					list.appendChild(Delete(rs.getString("DocNo")));

					Listboxpayout_sectionTab1_2.appendChild(list);
					No++;
					
					
				}else {
					list.appendChild(View2(rs.getString("DocNo")));
					list.appendChild(new Listcell(No + "."));
					list.appendChild(new Listcell(rs.getString("DocNo")));
					list.appendChild(new Listcell(rs.getString("DocDate")));
					list.appendChild(new Listcell(rs.getString("Qty")));
					list.appendChild(new Listcell(rs.getString("DepName")));
					list.appendChild(new Listcell(rs.getString("borrowName")));
					list.appendChild(new Listcell(""));
					

					
					Listboxpayout_sectionTab1_2.appendChild(list);
					No++;
					
					
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			Messagebox.show("ERROR onDisPlayDocument : " + e.getMessage());
			System.out.println("ERROR onDisPlayDocument : " + e.getMessage());
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
//					if(Listboxpayout_sectionSouthTab1_2.getSelectedCount() > 0 ){
						onDelete22(Listboxpayout_sectionTab1);
						
						
						onDeleteItem(ID);
						onDisplayDoc_Detail("");
						onDisPlayDocument("");	
						onDisplay();
						TextboxSearchpayoutSouthTab1.setText("");
						Listboxpayout_sectionSouthTab1_2.clearSelection();
						
//					}
					 	
				}
					});
		lc.appendChild(imgdel);
		
		return lc;
		
	}	
	public void onDeleteItem(final String DocNo) throws Exception {
		try {
			
		
		
		String sqldel = "SELECT ID,ItemStockID FROM payoutdetail WHERE DocNo='"+DocNo+"' ";
		rs2 = stmt3.executeQuery(sqldel);
		while(rs2.next()) {
			String ID_Detail = rs2.getString("ID");
			String ItemStockID = rs2.getString("ItemStockID");
			String Sql2 = "UPDATE itemstock SET IsDispatch = 0 WHERE itemstock.RowID = '"+ItemStockID+"'";
			stmt.executeUpdate(Sql2);
			stmt2.executeUpdate("DELETE FROM payoutdetail WHERE payoutdetail.ID = '"+ID_Detail+"' ");
		}
		stmt.executeUpdate("DELETE FROM payout WHERE payout.DocNo = '"+DocNo+"' ");
		
		System.out.println("DELETE ID : " +DocNo);
	} catch (Exception e) {
		e.printStackTrace();
		Messagebox.show("ERROR Delete : " + e.getMessage());
		System.out.println("ERROR Delete : " + e.getMessage());
	}	    
}
	
	private Listcell View(final String DocNo) throws Exception{

		Listcell lc = new Listcell();
		
		Image img = new Image();

		img.setHeight("25px");
		img.setSrc("/images/ic_search.png");
		img.setStyle("align:center;");
			lc.setStyle("align:center; ");
			img.addEventListener("onClick", new EventListener<Event>() {
				public void onEvent(Event event) throws Exception {
					
					btnLend.setDisabled(false);
					btnDelete.setDisabled(false);
					btnAdd.setDisabled(false);
					
					TextboxSearchpayoutSouthTab1.setText(DocNo);
					
					onDisplayDoc_Detail(DocNo);
					    }
					});
//			System.out.println("newListcell " + sql);
			lc.appendChild(img);
		
		
		return lc;
	}
	
	private Listcell View2(final String DocNo) throws Exception{

		Listcell lc = new Listcell();
		
		Image img = new Image();

		img.setHeight("25px");
		img.setSrc("/images/ic_search.png");
		img.setStyle("align:center;");
			lc.setStyle("align:center; ");
			img.addEventListener("onClick", new EventListener<Event>() {
				public void onEvent(Event event) throws Exception {
					
					btnLend.setDisabled(false);
					btnDelete.setDisabled(false);
					btnAdd.setDisabled(false);
					
					TextboxSearchpayoutSouthTab1.setText(DocNo);
					btnLend.setDisabled(true);
					btnDelete.setDisabled(true);
					btnAdd.setDisabled(true);
					onDisplayDoc_Detail(DocNo);
					    }
					});
//			System.out.println("newListcell " + sql);
			lc.appendChild(img);
		
		
		return lc;
	}
	public void onSave()throws Exception{

		if(ComboboxpayoutTab1.getText().equals("กรุณาเลือกแผนกปลายทาง")) {
			Messagebox.show("กรุณาเลือกแผนกปลายทาง"
					," ", Messagebox.YES, Messagebox.INFORMATION);
		}else {
		onSaveDoc();
		}
	}
	
	public void onSaveDoc()throws Exception{
		try {
			
//			String DevRefID = ComboboxpayoutTab1.getSelectedItem().getValue();
				Docno = generateDoc();
				
				String insert = "INSERT INTO payout " + 
						"                (DocNo, " + 
						"                  CreateDate, " + 
						"                  ModifyDate, " + 
						"                  DeptID, " + 
						"                  UserCode, " + 
						"                  IsStatus, " + 
						"                  Qty, " + 
						"                  PayQty, " + 
						"                  IsWeb, " + 
						"                  IsBorrow, " + 
						"                  IsBorrowStatus, " + 
						"                  DeptRec) " + 
						"              VALUES " + 
						"                ('"+Docno+"', " + 
						"                  NOW(), " + 
						"                  NOW(), " + 
						"              "+S_DeptId+", " + 
						"              "+S_UserId+", " + 
						"                  10, " + 
						"                  0, " + 
						"                  0, " + 
						"                  1, " + 
						"                  1, " + 
						"                  6, " + 
						"                  "+DevRefID+")";
				stmt.executeUpdate(insert);
				onDisPlayDocument("");
//				Listboxpayout_sectionSouthTab1_2.getItems().clear();
				System.out.println("onSaveDoc insert : " + insert);
		
		}catch(Exception e){
			e.printStackTrace();
			Messagebox.show("ERROR onSaveDoc : " + e.getMessage());
			System.out.println("ERROR onSaveDoc : " + e.getMessage());
			System.out.println("DevRefID  : " + DevRefID);
			
		}
	}
	
	public String generateDoc()throws Exception {
		 try {
		    	
		        String sql = "SELECT " + 
		        		"            CONCAT( " + 
		        		"              'PA', " + 
		        		"              SUBSTRING( YEAR ( DATE( NOW())), 3, 4 ), " + 
		        		"              LPAD( MONTH ( DATE( NOW())), 2, 0 ), " + 
		        		"              '-', " + 
		        		"            LPAD( ( COALESCE ( MAX( CONVERT ( SUBSTRING( DocNo, 9, 4 ), UNSIGNED INTEGER )), 0 )+ 1 ), 5, 0 )) AS DocNo  " + 
		        		"          FROM " + 
		        		"            payout  " + 
		        		"          WHERE " + 
		        		"            DocNo LIKE CONCAT( " + 
		        		"              'PA', " + 
		        		"              SUBSTRING( YEAR ( DATE( NOW())), 3, 4 ), " + 
		        		"              LPAD( MONTH ( DATE( NOW())), 2, 0 ),'%'  " + 
		        		"            )  " + 
		        		"          ORDER BY " + 
		        		"            DocNo DESC  " + 
		        		"            LIMIT 1 ";
		      
		  rs = stmt.executeQuery(sql);
		  if(rs.next()) {
			  Docno = rs.getString("DocNo");
		  }
		  
		  
		  System.out.println(sql);
		
		  
		  
	}catch (Exception e) {
		e.printStackTrace();
		Messagebox.show("ERROR generateDoc : " + e.getMessage());
		System.out.println("ERROR generateDoc : " + e.getMessage());
	}
	    return Docno;
	}
	
	public void onSearch()throws Exception {
		
			onDisPlayDocument(TextboxSearchpayoutTab1.getText());
			
			System.out.println("get text DocNo : " + TextboxSearchpayoutTab1.getText());
		
	}
	
	public void onImport(Listbox Listboxpayout_sectionTab1) throws WrongValueException, ComponentNotFoundException, Exception{
		// Operation Insert
		DocNo = TextboxSearchpayoutSouthTab1.getText();

		if(DocNo.trim().equals("")) {
			System.out.println(" if DocNo : " + DocNo);
			
		}else if(!DocNo.trim().equals("")){
			System.out.println(" ELSE DocNo : " + DocNo);
			btnAdd.setDisabled(false);
			
		Iterator<Listitem> li = Listboxpayout_sectionTab1.getSelectedItems().iterator();
		int count = 0;

		while(li.hasNext()){
			Listitem list = (Listitem) li.next();
			System.out.println("RowID : "+list.getAttribute("RowID"));
			String Sql2 = "UPDATE itemstock SET IsDispatch = 1, IsNew = 0, IsNewUsage = 0, IsStatus = 5, IsPay = 1 WHERE itemstock.RowID = '"+list.getAttribute("RowID")+"'";
			stmt.executeUpdate(Sql2);
			
			String Sql = "INSERT INTO payoutdetail "+
				      " (DocNo,ItemStockID,Qty,IsStatus,Remark,PayDate,OccuranceTypeID) "+
				     "  VALUES " +
				     " ('"+DocNo+"', "+list.getAttribute("RowID")+",1,0,'',NOW(),0) ";
			
			stmt.executeUpdate(Sql);
			count++;
		}
		
		String Sql3="SELECT COUNT(*) AS QTY FROM payoutdetail WHERE DocNo ='"+DocNo+"' ";
		rs = stmt.executeQuery(Sql3);
			String qty = null;
		if(rs.next()){
			qty = rs.getString("QTY");
		}
		String Sql2 = "UPDATE payout SET Qty = "+qty+" WHERE DocNo ='"+DocNo+"' ";
		stmt.executeUpdate(Sql2);
		
		System.out.println("count : "+count);

		if(count > 0){
			//Update Doc
//			onUpdateSale();
			
			//Display Sale Detail
			onDisplayDoc_Detail(DocNo);
			onDisPlayDocument("");
			onDisplay();
		}
		}
		
	}
	
public String getSqlDetail(String DocNo)throws Exception {
		try {

		 String Sql = "SELECT payoutdetail.ID,payoutdetail.DocNo,itemstock.UsageCode,item.itemname,units.UnitName,payoutdetail.Qty,payoutdetail.ItemStockID   " + 
		 		"  FROM payoutdetail   " + 
		 		"  INNER JOIN itemstock ON payoutdetail.ItemstockID = itemstock.RowID   " + 
		 		"  LEFT JOIN item ON itemstock.ItemCode = item.itemcode   " + 
		 		"  LEFT JOIN units ON item.UnitID = units.ID   " + 
		 		"  WHERE  payoutdetail.DocNo = '"+DocNo+"' ";

		 		System.out.println("getSql : " +Sql);
		 		return Sql;
		 		
		}catch (Exception e) {
			System.out.println("ERROR getSql : " + e.getMessage());
		}
		return null;
	}

	public void onDisplayDoc_Detail(String DocNo)throws Exception {
		try {
			rs = stmt.executeQuery(getSqlDetail(DocNo));
			
			Listboxpayout_sectionSouthTab1_2.getItems().clear();
			int no = 1;
			
			while(rs.next()) {
				Listitem list = new Listitem();
				
				list.appendChild(new Listcell(no + "."));
				list.appendChild(new Listcell(rs.getString("UsageCode")));
				list.appendChild(new Listcell(rs.getString("itemname")));
				list.appendChild(new Listcell(rs.getString("UnitName")));
				list.appendChild(new Listcell(rs.getString("Qty")));

				list.setAttribute("ID", rs.getString("ID"));
				list.setAttribute("UsageCode", rs.getString("UsageCode"));
				Listboxpayout_sectionSouthTab1_2.appendChild(list);
				no++;
				
				
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			Messagebox.show("ERROR onDisplayReport_HN : " + e.getMessage());
			System.out.println("ERROR onDisplay : " + e.getMessage());
		}
	}
	public void onDelete2(Listbox Listboxpayout_sectionTab1) throws WrongValueException, ComponentNotFoundException, Exception{
		// Operation Insert
		
		DocNo = TextboxSearchpayoutSouthTab1.getText();
try {
	if(Listboxpayout_sectionSouthTab1_2 == null) {
		Messagebox.show("เกิดข้อผิดพลาด");
	}else {
		Iterator<Listitem> li = Listboxpayout_sectionTab1.getSelectedItems().iterator();
		int count = 0;

		while(li.hasNext()){
			Listitem list = (Listitem) li.next();
			System.out.println("ID : "+list.getAttribute("ID"));
			String Sql2 = "UPDATE itemstock SET IsDispatch = 0 WHERE itemstock.UsageCode = '"+list.getAttribute("UsageCode")+"'";
			stmt.executeUpdate(Sql2);
			
			String Sql = "DELETE FROM payoutdetail WHERE payoutdetail.ID ='"+list.getAttribute("ID")+"' ";
			
			stmt.executeUpdate(Sql);
			count++;
		}
		
		String Sql3="SELECT COUNT(*) AS QTY FROM payoutdetail WHERE DocNo ='"+DocNo+"' ";
		rs = stmt.executeQuery(Sql3);
			String qty = null;
		if(rs.next()){
			qty = rs.getString("QTY");
		}
		String Sql2 = "UPDATE payout SET Qty = "+qty+" WHERE DocNo ='"+DocNo+"' ";
		stmt.executeUpdate(Sql2);

		if(count > 0){
			//Update Doc
//			onUpdateSale();
			
			//Display Sale Detail
			onDisplayDoc_Detail(DocNo);
			onDisPlayDocument("");
			onDisplay();
		}
	}
			}catch (Exception e) {
				e.printStackTrace();
				Messagebox.show("ERROR onDelete2 : " + e.getMessage());
				System.out.println("ERROR onDelete2 : " + e.getMessage());
			}
			
	}
	
	public void onDelete22(Listbox Listboxpayout_sectionTab1) throws WrongValueException, ComponentNotFoundException, Exception{
		// Operation Insert
		
		DocNo = TextboxSearchpayoutSouthTab1.getText();
try {
	String sql = "SELECT * FROM itemstock";
	rs = stmt.executeQuery(sql);
	String sql2 = "SELECT * FROM payoutdetail";
	rs = stmt.executeQuery(sql2);
	
		Iterator<Listitem> li = Listboxpayout_sectionTab1.getSelectedItems().iterator();
		int count = 0;

		while(li.hasNext()){
			Listitem list = (Listitem) li.next();
			System.out.println("ID : "+list.getAttribute("ID"));
			
			
			String Sql2 = "UPDATE itemstock SET IsDispatch = 0 WHERE itemstock.UsageCode = '"+ list.getAttribute("UsageCode")+"'";
			stmt.executeUpdate(Sql2);
			System.out.println("ERROR UPDATEonDelete22_22_22 : " + Sql2);
			
			String Sql = "DELETE FROM payoutdetail WHERE payoutdetail.ID ='"+ list.getAttribute("ID")+"' ";
			System.out.println("ERROR DELETEonDelete22_22_22 : " + Sql);
			stmt.executeUpdate(Sql);
			count++;
		}
		
		String Sql3="SELECT COUNT(*) AS QTY FROM payoutdetail WHERE DocNo ='"+DocNo+"' ";
		rs = stmt.executeQuery(Sql3);
			String qty = null;
		if(rs.next()){
			qty = rs.getString("QTY");
		}
		String Sql2 = "UPDATE payout SET Qty = "+qty+" WHERE DocNo ='"+DocNo+"' ";
		stmt.executeUpdate(Sql2);

		if(count >= 0){
			//Update Doc
//			onUpdateSale();
			
			//Display Sale Detail
			onDisplayDoc_Detail("");
			onDisPlayDocument("");
			onDisplay();
			
		}
	
			}catch (Exception e) {
				e.printStackTrace();
				Messagebox.show("ERROR onDelete22 : " + e.getMessage());
				System.out.println("ERROR onDelete22 : " + e.getMessage());
			}
			
	}
	

	
	
	
	public void onUpdate()throws Exception {
		try {
			if(Listboxpayout_sectionSouthTab1_2.getItemCount()==0) {
				Messagebox.show("เกิดข้อผิดพลาดในการยืม");
			}else {
				DocNo = TextboxSearchpayoutSouthTab1.getText();
		 Messagebox.show("บันทึกข้อมูล ? \n .คุณต้องการบันทึกข้อมูลนี้ \n ["+DocNo+"] ใช่หรือไม่...! ", " ", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,  new EventListener<Event>() {
			 	
	           public void onEvent(Event evt) throws Exception {
	        	   
	               switch (((Integer) evt.getData()).intValue()) {
	                   case Messagebox.YES:
	                	   DocNo = TextboxSearchpayoutSouthTab1.getText();
	               		String Sql = "UPDATE payout SET IsStatus = 10, IsBorrowStatus = 7 WHERE DocNo = '"+DocNo+"' ";
	               		stmt.executeUpdate(Sql);
	               		
	               		String Sqlupdate = "UPDATE payoutdetail SET IsStatus = 0 WHERE DocNo = '"+DocNo+"' ";
	               		stmt.executeUpdate(Sqlupdate);
	               		
	               		Listboxpayout_sectionSouthTab1_2.getItems().clear();
	               		TextboxSearchpayoutSouthTab1.setText("");
	               		
	               		onDisPlayDocument("");
	               		onDisplayTab2();
	                       break;
	                   case Messagebox.NO:
	                       break;
	               }
	           }
	       });
			}
		}catch (Exception e) {
			e.printStackTrace();
			Messagebox.show("ERROR onUpdate : " + e.getMessage());
			System.out.println("ERROR onUpdate : " + e.getMessage());
		}
	}
	public String getSqlTab2()throws Exception {
		try {
		 String Sql = "SELECT " + 
		 		"            pa.ID, " + 
		 		"            DATE_FORMAT(pa.CreateDate, '%d/%m/%Y') AS doc_date, " + 
		 		"            pa.DocNo, " + 
		 		"            pa.IsBorrowStatus, " + 
		 		"            CASE pa.IsBorrowStatus " + 
		 		"            WHEN '7' THEN " + 
		 		"              'รอคืน' " + 
		 		"            WHEN '8' THEN " + 
		 		"              'คืน (ค้างบางตัว)' " + 
		 		"            WHEN '9' THEN " + 
		 		"              'คืนครบ' " + 
		 		"            END AS borrowName, " + 
		 		"            pa.Qty, " + 
		 		"            departmentRec.DepName2 " + 
		 		" " + 
		 		"          FROM " + 
		 		"            payout pa " + 
		 		"          INNER JOIN department AS dpt ON pa.DeptID = dpt.ID " + 
		 		"			LEFT JOIN department AS departmentRec ON pa.DeptRec = departmentRec.ID " + 
		 		"          WHERE " + 
		 		"            DATE(pa.CreateDate) BETWEEN DATE('"+DateTime.convertDate(DateboxTab2.getText())+"') AND DATE('"+DateTime.convertDate(DateboxTab22.getText())+"') " + 
		 		"            AND pa.IsCancel = 0 " + 
		 		"            AND pa.IsStatus = 10 " + 
		 		"            AND ( pa.IsBorrowStatus = 7 OR pa.IsBorrowStatus = 8 OR pa.IsBorrowStatus = 9 )  " + 
		 		"            AND pa.B_ID = '"+B_ID+"' ";

		 		System.out.println("getSqlTab2 : " +Sql);
		 		return Sql;
		 		
		}catch (Exception e) {
			System.out.println("ERROR getSqlTab2 : " + e.getMessage());
		}
		return null;
	}
	
	public void onDisplayTab2()throws Exception {
		try {
			rs = stmt.executeQuery(getSqlTab2());
			
			Listboxpayout_sectionTab2.getItems().clear();
			int no = 1;
			
			while(rs.next()) {
				Listitem list = new Listitem();
				
				
				final String DocNo = rs.getString("DocNo");
				final String doc_date = rs.getString("doc_date");
				final String borrowName = rs.getString("borrowName");
				final String DepName2 = rs.getString("DepName2");
				
				
				
				list.appendChild(new Listcell(no + "."));
				list.appendChild(new Listcell(rs.getString("doc_date")));
				Listcell Lc_DocNo= new Listcell();
				A a_DocNo = new A(rs.getString("DocNo"));
				
				a_DocNo.addEventListener("onClick", new EventListener<Event>() {
					public void onEvent(Event event) throws Exception {
						onDisplayTab2_2(DocNo,doc_date,borrowName,DepName2);
						
			        }
			    });
				Lc_DocNo.appendChild( a_DocNo );
				list.appendChild( Lc_DocNo );
				list.appendChild(new Listcell(rs.getString("borrowName")));
				list.appendChild(new Listcell(rs.getString("Qty")));
				list.appendChild(new Listcell(rs.getString("DepName2")));

				
				Listboxpayout_sectionTab2.appendChild(list);
				no++;
				
			}
		}catch (Exception e) {
			e.printStackTrace();
			Messagebox.show("ERROR onDisplayTab2 : " + e.getMessage());
			System.out.println("ERROR onDisplayTab2 : " + e.getMessage());
		}
	}
	
	
	public String getSqlTab2_2(String DocNo)throws Exception {
		try {
			
		 String Sql = "SELECT  " + 
		 		"            payoutdetail.ID, " + 
		 		"            payoutdetail.IsStatus, " + 
		 		"            payoutdetail.ItemStockID, " + 
		 		"            itemstock.UsageCode, " + 
		 		"            item.itemname, " + 
		 		"            units.UnitName, " + 
		 		"            payoutdetail.Qty " + 
		 		" " + 
		 		"          FROM payoutdetail " + 
		 		"          INNER JOIN itemstock ON payoutdetail.ItemstockID = itemstock.RowID " + 
		 		"          INNER JOIN item ON itemstock.ItemCode = item.itemcode " + 
		 		"          INNER JOIN units ON item.UnitID = units.ID " + 
		 		" " + 
		 		
		 		"          WHERE  payoutdetail.DocNo = '"+DocNo+"' ";

		 		System.out.println("getSqlTab2 : " +Sql);
		 		return Sql;
		 		
		}catch (Exception e) {
			e.printStackTrace();
			Messagebox.show("ERROR getSqlTab2_2 : " + e.getMessage());
			System.out.println("ERROR getSqlTab2_2 : " + e.getMessage());
		}
		return null;
	}
	
	public void onDisplayTab2_2(String DocNo ,String doc_date, String borrowName, String DepName)throws Exception {
		try {
			
			TextboxSearchpayoutTab2.setText(DocNo);
			TextboxSearchpayoutDateTab2.setText(doc_date);
			TextboxSearchpayoutStatusTab2_1.setText(borrowName);
			TextboxSearchpayoutDepTab2_2.setText(DepName);

			rs = stmt.executeQuery(getSqlTab2_2(DocNo));
			
			Listboxpayout_sectionTab2_2.getItems().clear();
			
			
			int no = 1;
			
			while(rs.next()) {
				Listitem list = new Listitem();
				
				if(rs.getInt("IsStatus")==1) {
					System.out.println("IF IsStatus1 : " + rs.getInt("IsStatus"));
					btnSearchTab2_2.setDisabled(true);

				list.appendChild(Check(rs.getString("ID"),rs.getInt("IsStatus")));	
			
//				Listboxpayout_sectionTab2_2.setCheckmark(false);
//				list.appendChild(setCheckmark(rs.getString("ID")));
				
				list.appendChild(new Listcell(""));
				list.appendChild(new Listcell(no + "."));
				list.appendChild(new Listcell(rs.getString("UsageCode")));
				list.appendChild(new Listcell(rs.getString("itemname")));
				list.appendChild(new Listcell(rs.getString("UnitName")));
				list.appendChild(new Listcell(rs.getString("Qty")));
				
				list.setAttribute("ID", rs.getString("ID"));
				
				Listboxpayout_sectionTab2_2.appendChild(list);
				no++;
					
			}
				else if(rs.getInt("IsStatus")==0) {
					System.out.println("else IsStatus2 : " + rs.getInt("IsStatus"));
					
				list.appendChild(Check2(rs.getString("ID"),rs.getInt("IsStatus")));
					
//				list.appendChild(setCheckmark2(rs.getString("ID")));
				list.appendChild(new Listcell(no + "."));
				list.appendChild(new Listcell(rs.getString("UsageCode")));
				list.appendChild(new Listcell(rs.getString("itemname")));
				list.appendChild(new Listcell(rs.getString("UnitName")));
				list.appendChild(new Listcell(rs.getString("Qty")));
				
				list.setAttribute("ID", rs.getString("ID"));
				
				Listboxpayout_sectionTab2_2.appendChild(list);
				no++;
			}
			}
		}catch (Exception e) {
			e.printStackTrace();
			Messagebox.show("ERROR onDisplayTab2_2 : " + e.getMessage());
			System.out.println("ERROR onDisplayTab2_2 : " + e.getMessage());
		}
	}

	
	private Listcell Check(final String ID,final int IsStatus) throws Exception{

		Listcell lc = new Listcell();

		lc.setVisible(false);
		lc.setStyle("background:#D3D3D3; background-color:#D3D3D3;");
		Listboxpayout_sectionTab2_2.setDisabled(IsFinish);
		btnSearchTab2_2.setDisabled(true);
			lc.setStyle("align:center; ");		
			Listboxpayout_sectionTab2_2.setCheckmark(false);
			Listboxpayout_sectionTab2_2.setMultiple(false);
			lc.addEventListener("onClick", new EventListener<Event>() {
				public void onEvent(Event event) throws Exception {

					    }
					});

//			lc.appendChild(lc);	
		return lc;
	}
	
	private Listcell Check2(final String ID,final int IsStatus) throws Exception{
		
		Listcell lc = new Listcell();

		btnSearchTab2_2.setDisabled(false);
			lc.setStyle("align:center; ");
			Listboxpayout_sectionTab2_2.setCheckmark(true);
			Listboxpayout_sectionTab2_2.setMultiple(true);
			lc.addEventListener("onClick", new EventListener<Event>() {
				public void onEvent(Event event) throws Exception {
					
				

					    }
					});
//			lc.appendChild(lc);	
		return lc;
	}
	
	public void onUpdate2(Listbox Listboxpayout_sectionTab2_2) throws WrongValueException, ComponentNotFoundException, Exception{
		// Operation Insert
		try {
		DocNo = TextboxSearchpayoutTab2.getText();
		
		
		
		Iterator<Listitem> li = Listboxpayout_sectionTab2_2.getSelectedItems().iterator();
		
//		boolean ck = chk.isChecked();
		int count = 0;

		while(li.hasNext()){
			
			Listitem list = (Listitem) li.next();
			System.out.println("ID : "+list.getAttribute("ID"));
			String Sql2 = "UPDATE payoutdetail SET IsStatus = 1 WHERE payoutdetail.ID = '"+list.getAttribute("ID")+"'";
			stmt.executeUpdate(Sql2);
			
			String sel = "SELECT Qty FROM payout WHERE DocNo ='"+DocNo+"' ";
			System.out.println("sel : "+sel);
			rs2 = stmt2.executeQuery(sel);
			int Qty = 0;
			if(rs2.next()) {
				Qty = rs2.getInt("Qty");
			}
			System.out.println("Qty : "+Qty);
			String sel_count ="SELECT COUNT(*) As count FROM payoutdetail WHERE IsStatus =0 AND DocNo ='"+DocNo+"'";
			rs3 = stmt3.executeQuery(sel_count);
			int status_count = 0;
			if(rs3.next()) {
				status_count = rs3.getInt("count");
			}
			
			
			System.out.println("status_count : "+status_count);
			if(status_count<Qty) {
				System.out.println("IFFFFFFFFFFFF1");
				String Sql = "UPDATE payout SET IsBorrowStatus = 8 WHERE DocNo = '"+DocNo+"'";
				stmt2.executeUpdate(Sql);
			}
			
			if(status_count == 0) {
				System.out.println("IFFFFFFFFFFFF2");
				String Sql = "UPDATE payout SET IsBorrowStatus = 9 WHERE DocNo = '"+DocNo+"'";
				stmt2.executeUpdate(Sql);
			}
			
			
			count++;
		}
		
	

		if(count > 0){
			//Update Doc
//			onUpdateSale();
			
			//Display Sale Detail
			
			onDisplayTab2_2(DocNo,"","","");
			onDisplayTab2();
			onDisPlayDocument("");
		}
		
		TextboxSearchpayoutTab2.setText("");
		TextboxSearchpayoutDateTab2.setText("");
		TextboxSearchpayoutStatusTab2_1.setText("");
		TextboxSearchpayoutDepTab2_2.setText("");
		Listboxpayout_sectionTab2_2.getItems().clear();
		
		
		}catch (Exception e) {
			e.printStackTrace();
			Messagebox.show("ERROR onUpdate2 : " + e.getMessage());
			System.out.println("ERROR onUpdate2 : " + e.getMessage());
		}
	}
	
	public void onPrint() throws Exception{
//		combobox_report.getSelectedItem().getValue();

		String sdate =(String) ((Window) getFellow("open_report")).getAttribute("sdate");
		String edate = (String) ((Window) getFellow("open_report")).getAttribute("edate");
		String BID = (String) ((Window) getFellow("open_report")).getAttribute("BID");
		
	String StrReportFile = "Report_Borrow";
	Reports r = new Reports();
	r.uName = "root";
	r.uPwd = "A$192dijd";
			
	r.setFileName(StrReportFile);
	r.setFileType("PDF");
	r.setStrParameter(new ArrayList<String>() {
		private static final long serialVersionUID = -2892797364135239183L;
		{
//			add("combobox_report");
//			add(combobox_report.getSelectedItem().getValue());
			
			add("sDate");
			add(sdate);
			
			add("eDate");
			add(edate);

			add("BID");
			add(BID);
		}

	});
	
//	System.out.println("combobox_report : " +combobox_report);
	System.out.println("sdate_report : " +sdate);
	System.out.println("edate_report : " +edate);
	System.out.println("B_ID : " +BID);

	//Open Window
	Iframe iframe = new Iframe();
	iframe.setWidth("100%");
	iframe.setHeight("100%");
	iframe.setContent(r.doReport());
	
	Button aa = new Button();
	aa.setWidth("50px");
	aa.setHeight("50px");
	
	((Window) getFellow("Windows")).getChildren().clear();
	((Window) getFellow("Windows")).setTitle("...");
	((Window) getFellow("Windows")).setWidth("100%");
	((Window) getFellow("Windows")).setHeight("100%");
	((Window) getFellow("Windows")).appendChild(iframe);
	((Window) getFellow("Windows")).appendChild(aa);

	openWindow("Windows");

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
}
