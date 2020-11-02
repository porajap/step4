package process;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Window;

import com.lowagie.text.List;
import com.lowagie.text.ListItem;
import com.sun.java.swing.plaf.windows.resources.windows;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Text;

import connect.DBConn;
import connect.SqlSelection;
import general.DateTime;
import general.Number;

public class payout extends Window{

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
	private Statement stmt2 = null;
	public boolean B_IsCreate = true;
	private Session session = Sessions.getCurrent();
	private String xUsername = null;
	private String xPassword = null;
	private String Search = null;	
	private String Search2 = null;
	public String xSearch = null;	
	public String DocNo = null;
	public String Docno;
	public Textbox TextboxSearch;
	public Textbox Textbox_EastNorth;
	public Textbox Textbox_EastSouth;
	public Listbox ListboxPayout_west;
	public Listbox ListboxPayout_EastNorth;
	public Listbox ListboxPayout_EastSouth;
	public Button btnSearch;
	public Button btn_EastNorth;
	public Button btnReport;
	public Button btnAdd;
	public Button btnCeate_EastNorth;
	public Button btnDelete_EastSouth;
	public Button btnSendCssD_EastSouth;
	public Checkbox Checkbox_Mode;
	public Datebox Datebox;
	
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
		bySession();
		onDisplay_west();

		
	}
	

	
	private void init()throws SQLException {
		SqlSelection sql = new SqlSelection();
		sql.uName = xUsername;
		sql.Pwd = xPassword;
		DBConn objConnection = new DBConn();
		conn = objConnection.getConnection("root", "A$192dijd");
		stmt = conn.createStatement();
		stmt2 = conn.createStatement();
		TextboxSearch = ((Textbox)getFellow("TextboxSearch"));
		Textbox_EastNorth = ((Textbox)getFellow("Textbox_EastNorth"));
		Textbox_EastSouth = ((Textbox)getFellow("Textbox_EastSouth"));
		btnSearch = ((Button)getFellow("btnSearch"));
		btn_EastNorth = ((Button)getFellow("btn_EastNorth"));
		btnReport = ((Button)getFellow("btnReport"));
		btnAdd = ((Button) getFellow("btnAdd"));
		btnCeate_EastNorth = ((Button) getFellow("btnCeate_EastNorth"));
		Checkbox_Mode = ((Checkbox)getFellow("Checkbox_Mode"));
		ListboxPayout_west = ((Listbox)getFellow("ListboxPayout_west"));
		ListboxPayout_EastNorth = ((Listbox)getFellow("ListboxPayout_EastNorth"));
		ListboxPayout_EastSouth = ((Listbox)getFellow("ListboxPayout_EastSouth"));
		Datebox = ((Datebox)getFellow("Datebox"));
		btnDelete_EastSouth = ((Button)getFellow("btnDelete_EastSouth")) ;
		btnSendCssD_EastSouth = ((Button)getFellow("btnSendCssD_EastSouth")) ;
		
		Datebox.setText(DateTime.getDateNow());
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
	
	public void onSearch()throws Exception {
		
		onDisPlay_EastNorth("");

}
	public void onImport(Listbox ListboxPayout_west) throws WrongValueException, ComponentNotFoundException, Exception{
		// Operation Insert
		DocNo = Textbox_EastSouth.getText();

		if(DocNo.trim().equals("")) {
			System.out.println(" if DocNo : " + DocNo);
			
		}else if(!DocNo.trim().equals("")){
			System.out.println(" ELSE DocNo : " + DocNo);
			btnAdd.setDisabled(false);
			
		Iterator<Listitem> li = ListboxPayout_west.getSelectedItems().iterator();
		int count = 0;
		int count2 = 0;
		while(li.hasNext()){
			Listitem list = (Listitem) li.next();
			
			String Enter_QTY = Number.unCommaReturnString( ((Intbox)((Listcell)list.getChildren().get(4)).getChildren().get(1)).getText() );
			
			System.out.println("RowID : "+list.getAttribute("RowID"));
			
			String Sql3="SELECT COUNT(*) AS cnt,Qty FROM payoutdetail WHERE ItemStockID ='"+list.getAttribute("RowID")+"' AND DocNo ='"+DocNo+"' ";
			
			rs = stmt.executeQuery(Sql3);
			System.out.println("RowID : "+Sql3);
			int qty2 = 0;
			if(rs.next()) {
				count2 = rs.getInt("cnt");
				qty2 = rs.getInt("Qty");
			}
			if(count2 > 0) {
				int qty3 = qty2 +Integer.parseInt(Enter_QTY);
				
				String Sql2 = "UPDATE payoutdetail SET Qty = "+qty3+ " WHERE ItemStockID ='"+list.getAttribute("RowID")+"' AND DocNo ='"+DocNo+"' ";
				stmt.executeUpdate(Sql2);
				count++;
			}else {
			
			String Sql = "INSERT INTO payoutdetail "+
				      " (DocNo,ItemStockID,Qty,IsStatus,Remark,PayDate,OccuranceTypeID,B_ID) "+
				     "  VALUES " +
				     " ('"+DocNo+"', "+list.getAttribute("RowID")+","+Enter_QTY+",0,'',NOW(),0,'"+B_ID+"') ";

			stmt.executeUpdate(Sql);
			count++;
			}
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
			
			onDisplayDoc_EastSouth(DocNo);
			onDisPlay_EastNorth("");
		}
		}
		
	}
	
	public String getSql_west()throws Exception {
		
		try {
			if(Checkbox_Mode.isChecked()) {
				Search = TextboxSearch.getText();
			String S_Sql = "SELECT itemstock.RowID,item.itemcode,item.itemname,units.UnitName " + 
				"  FROM itemstock " + 
				"  INNER JOIN item ON itemstock.ItemCode = item.itemcode " + 
				"  INNER JOIN units ON item.UnitID = units.ID " ;
			
				if(!Search.equals("")) {
				S_Sql +="  WHERE ( (itemstock.UsageCode LIKE '%"+Search+"%') OR (item.itemname LIKE '%$"+Search+"%') ) " ;
				}
				
				S_Sql += "  AND item.IsCSSDComfirm = 1 " + 
				
						
				"  AND NoWash = '1' AND NoWashType = '2'  AND  item.DepartmentID = '"+S_DeptId+"'  "+ 
				
				
				"  AND itemstock.B_ID  = '"+B_ID+"' " + 
				"  AND itemstock.DepID = '"+S_DeptId+"' " + 
				"  GROUP BY item.itemcode " + 
				"  ORDER BY item.itemname ASC LIMIT 50"; 
				
			
			System.out.println("IF getSql_west " +S_Sql);
			
			return S_Sql;
			}else {
				Search = TextboxSearch.getText();
				String S_Sql2 = "SELECT itemstock.RowID,item.itemcode,item.itemname,units.UnitName " + 
					"  FROM itemstock " + 
					"  INNER JOIN item ON itemstock.ItemCode = item.itemcode " + 
					"  INNER JOIN units ON item.UnitID = units.ID " ;
					if(!Search.equals("")) {
					S_Sql2 +="  WHERE ( (itemstock.UsageCode LIKE '%"+Search+"%') OR (item.itemname LIKE '%$"+Search+"%') ) " ;
					}
					
					S_Sql2 += "  AND item.IsCSSDComfirm = 1 " + 
					
							
					"  AND NOT ( NoWash = '1' AND NoWashType = '2' )  "+ 
					
					
					"  AND itemstock.B_ID  = '"+B_ID+"' " + 
					"  AND itemstock.DepID = '"+S_DeptId+"' " + 
					"  GROUP BY item.itemcode " + 
					"  ORDER BY item.itemname ASC LIMIT 50"; 
					
				
				System.out.println("ELSE getSql_west " +S_Sql2);
				
				return S_Sql2;
			}
		}catch (Exception e) {
			e.printStackTrace();
			Messagebox.show("ERROR getSql : " + e.getMessage());
			System.out.println("ERROR getSql : " + e.getMessage());
		}
		return null;
	}
	
	public void onDisplay_west()throws Exception {
		try {
			rs = stmt.executeQuery(getSql_west());		
			ListboxPayout_west.getItems().clear();
			int no = 1;
			
			while(rs.next()) {
				Listitem list = new Listitem();
			
				list.appendChild(new Listcell(""));
				list.appendChild(new Listcell(no + "."));
				list.appendChild(new Listcell(rs.getString("itemcode")));
				list.appendChild(new Listcell(rs.getString("itemname")));
				list.appendChild(Num(rs.getInt("RowID")));

				
				list.setAttribute("RowID", rs.getInt("RowID"));
			

				ListboxPayout_west.appendChild(list);
				
			
				no++;
			}
		}catch (Exception e) {
			e.printStackTrace();
			Messagebox.show("ERROR onDisplay_west : " + e.getMessage());
			System.out.println("ERROR onDisplay_west : " + e.getMessage());
		}
	}

	private Listcell Num(final int ID) throws Exception{

		Listcell lc = new Listcell();
		
		
		Button btn1 = new Button();
		Intbox intbox = new Intbox();
		Button btn2 = new Button();
		btn1.setLabel(" - ");
		btn1.setStyle("background: #dc3545;");
		btn2.setLabel(" + ");
		btn2.setStyle("background: #28a745;");
		intbox.setText("1");
		intbox.setId("intbox" +ID);
		intbox.setWidth("45px");
			lc.setStyle("align:center; ");
			btn1.addEventListener("onClick", new EventListener<Event>() {
				public void onEvent(Event event) throws Exception {
				String total =	((Intbox) getFellow("intbox"+ID)).getText();
				int	total2 = Integer.parseInt(total) -1;
					((Intbox) getFellow("intbox"+ID)).setText(Integer.toString(total2));
					
					    }
					});
			
			
			btn2.addEventListener("onClick", new EventListener<Event>() {
				public void onEvent(Event event) throws Exception {
					String total =	((Intbox) getFellow("intbox"+ID)).getText();
					int	total2 = Integer.parseInt(total) +1;
						((Intbox) getFellow("intbox"+ID)).setText(Integer.toString(total2));
						
						    
						
					    }
					});

			lc.appendChild(btn1);
			lc.appendChild(intbox);
			lc.appendChild(btn2);
		
		return lc;
	}
	public void onSaveDoc()throws Exception{
		try {

				Docno = generateDoc();
				
				String insert = "INSERT INTO payout " + 
						"      (DocNo,CreateDate,ModifyDate,DeptID,UserCode,IsStatus,Qty,PayQty,Remark,RefDocNo,IsPrint,DeptRec,B_ID) " + 
						"      VALUES " + 
						"      ('"+Docno+"',NOW(),NOW(),"+S_DeptId+","+S_UserId+",0,0,0,'','',0,0,'"+B_ID+"')";
				stmt.executeUpdate(insert);
				onDisPlay_EastNorth(Docno);
//				ListboxPayout_EastNorth.getItems().clear();
				System.out.println("onSaveDoc insert : " + insert);
		
		}catch(Exception e){
			e.printStackTrace();
			Messagebox.show("ERROR onSaveDoc : " + e.getMessage());
			System.out.println("ERROR onSaveDoc : " + e.getMessage());
			
		}
	}
	
	public String generateDoc()throws Exception {
		 try {
		    	
		        String sql = "SELECT CONCAT('PA',SUBSTRING(YEAR(DATE(NOW())),3,4),LPAD(MONTH(DATE(NOW())),2,0),'-', LPAD( (COALESCE(MAX(CONVERT(SUBSTRING(DocNo,8,5),UNSIGNED INTEGER)),0)+1) ,5,0)) AS DocNo " + 
		        		"    FROM payout " + 
		        		"    WHERE DocNo Like CONCAT('PA',SUBSTRING(YEAR(DATE(NOW())),3,4),LPAD(MONTH(DATE(NOW())),2,0),'%') " + 
		        		"    ORDER BY DocNo DESC LIMIT 1";
		      
		  rs = stmt.executeQuery(sql);
		  if(rs.next()) {
			  Docno = rs.getString("DocNo");
		  }

		  System.out.println("generateDoc : " +sql);

		  
	}catch (Exception e) {
		e.printStackTrace();
		Messagebox.show("ERROR generateDoc : " + e.getMessage());
		System.out.println("ERROR generateDoc : " + e.getMessage());
	}
	    return Docno;
	}
	
	public String getDocument(String DocNo)throws Exception {
	
			Search2 = Textbox_EastNorth.getText();
		String getDoc = "SELECT payout.ID, " + 
				"			 payout.DocNo, " + 
				"            DATE_FORMAT(payout.CreateDate,'%d-%m-%Y') AS DocDate, " + 
				"            payout.ModifyDate, " + 
				"            payout.Qty, " + 
				"            ( CASE " + 
				"              WHEN IsStatus = 0 THEN 'เอกสารร่าง' " + 
				"              WHEN IsStatus = 1 THEN 'รอเบิก' " + 
				"              ELSE 'ยกเลิก' " + 
				"            END ) AS Elc, " + 
				"            payout.IsStatus, " + 
				"            payout.IsCancel " + 
				"            FROM payout " + 
				"            WHERE ( payout.IsStatus = 0 OR payout.IsStatus = 1 OR payout.IsCancel = 1) " + 
				"            AND payout.DeptID = '"+S_DeptId+"' " + 
				"            AND DATE(payout.CreateDate) = '"+DateTime.convertDate(Datebox.getText())+"'  " ;
				
				if(!Search2.equals("")) {
					getDoc += "   AND payout.DocNo LIKE '%"+Search2+"%' " ;
				}
				getDoc += "    AND payout.B_ID = '"+B_ID+"' " + 
				"            ORDER BY payout.ID DESC ";
		
		System.out.println("getDoc : " + getDoc);
		return getDoc;

		
	}
	public void onDisPlay_EastNorth(String DocNo)throws Exception{
		try{
			System.out.println("get text DocNo2222 : " + DocNo);
			rs = stmt.executeQuery(getDocument(DocNo));
			ListboxPayout_EastNorth.getItems().clear();
			int No = 1;
			
			while(rs.next()) {
				Listitem list = new Listitem();
				
				if (rs.getInt("IsStatus") == 0) {
					
				list.appendChild(View(rs.getString("DocNo")));
				list.appendChild(new Listcell(No + "."));
				list.appendChild(new Listcell(rs.getString("DocNo")));
				list.appendChild(new Listcell(rs.getString("DocDate")));
				list.appendChild(new Listcell(rs.getString("Qty")));
				list.appendChild(new Listcell(rs.getString("Elc")));
				list.appendChild(Delete(rs.getString("DocNo"),(rs.getString("ID"))));
				
				ListboxPayout_EastNorth.appendChild(list);
				No++;
				}else if(rs.getInt("IsCancel") == 1){
				
					list.appendChild(View1(rs.getString("DocNo")));
					list.appendChild(new Listcell(No + "."));
					list.appendChild(new Listcell(rs.getString("DocNo")));
					list.appendChild(new Listcell(rs.getString("DocDate")));
					list.appendChild(new Listcell(rs.getString("Qty")));
					list.appendChild(new Listcell(rs.getString("Elc")));
					list.appendChild(new Listcell(""));
					
					ListboxPayout_EastNorth.appendChild(list);
					No++;
					 
				
				}else if ( rs.getInt("IsStatus") == 1) {
					list.appendChild(View1(rs.getString("DocNo")));
					list.appendChild(new Listcell(No + "."));
					list.appendChild(new Listcell(rs.getString("DocNo")));
					list.appendChild(new Listcell(rs.getString("DocDate")));
					list.appendChild(new Listcell(rs.getString("Qty")));
					list.appendChild(new Listcell(rs.getString("Elc")));
					list.appendChild(new Listcell(""));
					
					ListboxPayout_EastNorth.appendChild(list);
					No++;
					
				}
				
			}	
		}catch (Exception e) {
			e.printStackTrace();
			Messagebox.show("ERROR onDisPlay_EastNorth : " + e.getMessage());
			System.out.println("ERROR onDisPlay_EastNorth : " + e.getMessage());
		}
	}
		
public Listcell Delete(final String DocNo,final String ID)throws Exception {
		
		Listcell lc = new Listcell();
		
		Image imgdel = new Image();
		imgdel.setHeight("25px");
		imgdel.setSrc("/img/delete-24.png");
		imgdel.setStyle("align:center;");
		lc.setStyle("align:center; ");
		
			imgdel.addEventListener("onClick", new EventListener<Event>() {
				public void onEvent(Event event) throws Exception {
					Messagebox.show("ลบข้อมูล ? \n คุณต้องการลบข้อมูลนี้   \n [ "+DocNo+" ] ใช่หรือไม่...!" ," ", Messagebox.YES,Messagebox.INFORMATION, Messagebox.NO);
					
					onDeleteItem(ID);
					onDisPlay_EastNorth("");
					Textbox_EastSouth.setText("");
					
				}
					});
		lc.appendChild(imgdel);
		
		return lc;
		
	}	
	public void onDeleteItem(final String ID) throws Exception {
		try {
			
		
			stmt2.executeUpdate("UPDATE payout SET IsStatus=2 ,IsCancel=1  WHERE payout.ID = '"+ID+"' ");
			

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
					Textbox_EastSouth.setText(DocNo);
					onDisplayDoc_EastSouth(DocNo);
					btnAdd.setVisible(true);
					btnDelete_EastSouth.setVisible(true);
					btnSendCssD_EastSouth.setVisible(true);
					    }
					});

			lc.appendChild(img);
		
		
		return lc;
	}
	
	private Listcell View1(final String DocNo) throws Exception{

		Listcell lc = new Listcell();
		
		Image img = new Image();
		
		img.setHeight("25px");
		img.setSrc("/images/ic_search.png");
		img.setStyle("align:center;");
			lc.setStyle("align:center; ");
			img.addEventListener("onClick", new EventListener<Event>() {
				public void onEvent(Event event) throws Exception {
					Textbox_EastSouth.setText(DocNo);
					onDisplayDoc_EastSouth(DocNo);
					
					btnAdd.setVisible(false);
					btnDelete_EastSouth.setVisible(false);
					btnSendCssD_EastSouth.setVisible(false);
					    }
					});

			lc.appendChild(img);
		
		
		return lc;
	}
	
	public String getSql_EastSouth(String DocNo)throws Exception {
		try {

		 String Sql = "SELECT payoutdetail.ID, " + 
		 		"      payoutdetail.DocNo, " + 
		 		"      itemstock.ItemCode, " + 
		 		"      item.itemname, " + 
		 		"      units.UnitName, " + 
		 		"      payoutdetail.Qty, " + 
		 		"      payout.IsStatus " + 
		 		"      FROM payout " + 
		 		"      LEFT JOIN payoutdetail ON payout.DocNo = payoutdetail.DocNo " + 
		 		"      LEFT JOIN itemstock ON payoutdetail.ItemstockID = itemstock.RowID " + 
		 		"      LEFT JOIN item ON itemstock.ItemCode = item.itemcode " + 
		 		"      LEFT JOIN units ON item.UnitID = units.ID " + 
		 		"      WHERE payout.DocNo = '"+DocNo+"'  " + 
		 		"      AND payoutdetail.Qty != 0 " + 
		 		"      ORDER BY payoutdetail.ID DESC";

		 		System.out.println("getSql : " +Sql);
		 		return Sql;
		 		
		}catch (Exception e) {
			System.out.println("ERROR getSql : " + e.getMessage());
		}
		return null;
	}
	
	public void onDisplayDoc_EastSouth(String DocNo)throws Exception {
		try {
			rs = stmt.executeQuery(getSql_EastSouth(DocNo));
			
			ListboxPayout_EastSouth.getItems().clear();
			int no = 1;
			
			while(rs.next()) {
				Listitem list = new Listitem();
				
				list.appendChild(new Listcell(""));
				list.appendChild(new Listcell(no + "."));
				list.appendChild(new Listcell(rs.getString("ItemCode")));
				list.appendChild(new Listcell(rs.getString("itemname")));
				list.appendChild(new Listcell(rs.getString("UnitName")));
				list.appendChild(new Listcell(rs.getString("Qty")));

				list.setAttribute("ID", rs.getString("ID"));
				
				ListboxPayout_EastSouth.appendChild(list);
				no++;
				
				
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			Messagebox.show("ERROR onDisplayDoc_EastSouth : " + e.getMessage());
			System.out.println("ERROR onDisplayDoc_EastSouth : " + e.getMessage());
		}
	}
	
	public void onDelete_EastSouth(Listbox ListboxPayout_EastSouth) throws WrongValueException, ComponentNotFoundException, Exception{
		// Operation Insert
		
		DocNo = Textbox_EastSouth.getText();
try {
	if(ListboxPayout_EastSouth == null) {
		Messagebox.show("เกิดข้อผิดพลาด");
	}else {
		Iterator<Listitem> li = ListboxPayout_EastSouth.getSelectedItems().iterator();
		int count = 0;

		while(li.hasNext()){
			Listitem list = (Listitem) li.next();
			System.out.println("ID : "+list.getAttribute("ID"));

			
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
			onDisplayDoc_EastSouth(DocNo);
			onDisPlay_EastNorth("");
		}
	}
			}catch (Exception e) {
				e.printStackTrace();
				Messagebox.show("ERROR onDelete_EastSouth : " + e.getMessage());
				System.out.println("ERROR onDelete_EastSouth : " + e.getMessage());
			}
			
	}
	public void onSave_EastSouth()throws Exception {
		try {

			
			if(ListboxPayout_EastSouth.getItemCount()==0) {
			}else {
				DocNo = Textbox_EastSouth.getText();
				
				String sqll="SELECT SUM(payoutdetail.Qty) AS Cnt FROM payoutdetail " + 
						"      INNER JOIN itemstock ON payoutdetail.ItemstockID = itemstock.RowID " + 
						"      INNER JOIN item ON itemstock.ItemCode = item.itemcode " + 
						"      INNER JOIN units ON item.UnitID = units.ID " + 
						"      INNER JOIN payout ON payout.DocNo = payoutdetail.DocNo " + 
						"      WHERE  payoutdetail.DocNo = '"+DocNo+"' " + 
						"      AND DATE(payout.CreateDate) = DATE(NOW())";
				rs2 = stmt2.executeQuery(sqll);
				String qty = null;
				if(rs2.next()) {
						qty = rs2.getString("Cnt");
				System.out.println("qty6666666666666 : " + qty);
				}
	            String Sql = "UPDATE payout,payoutdetail SET payout.IsStatus = 1,payout.Qty = '"+qty+"',payout.IsCheck = 1,payout.IsWeb = 1 ,"
	            			+ "payoutdetail.IsWeb = 1 WHERE payout.DocNo = '"+DocNo+"' AND payoutdetail.DocNo = '"+DocNo+"' "; 
	               				    
	            stmt.executeUpdate(Sql);

	            onDisplayDoc_EastSouth(DocNo);
    			onDisPlay_EastNorth(""); 		
	            ListboxPayout_EastSouth.getItems().clear();
	            Textbox_EastSouth.setText("");     
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			Messagebox.show("ERROR onUpdate : " + e.getMessage());
			System.out.println("ERROR onUpdate : " + e.getMessage());
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
}
