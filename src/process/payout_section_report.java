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

public class payout_section_report extends Window{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6365073630321342768L;
	
	ResultSet rs = null;
	private Connection conn = null;
	private Statement stmt = null;
	public boolean B_IsCreate = true;
	private Session session = Sessions.getCurrent();
	private String xUsername = null;
	private String xPassword = null;
	private String Search = null;	
	public String xSearch = null;	
	public String DocNo = null;

	public String BID;
	public String combobox_report1;
	public String sdate;
	public String edate;
	
	public Combobox combobox_report;
	public Datebox sdate_report;
	public Datebox edate_report;
	public Button view_report;
	


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

		
	}

	private void init()throws SQLException {
		SqlSelection sql = new SqlSelection();
		sql.uName = xUsername;
		sql.Pwd = xPassword;
		DBConn objConnection = new DBConn();
		conn = objConnection.getConnection("root", "A$192dijd");
		stmt = conn.createStatement();

		combobox_report = ((Combobox) getFellow("combobox_report"));
		sdate_report = ((Datebox)getFellow("sdate_report"));
		edate_report = ((Datebox)getFellow("edate_report"));
		view_report = ((Button)getFellow("view_report"));
		
		combobox_report.setSelectedIndex(0);
		sdate_report.setText(DateTime.getDateNow());
		edate_report.setText(DateTime.getDateNow());
		
		
		
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
