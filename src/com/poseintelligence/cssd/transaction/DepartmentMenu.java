package com.poseintelligence.cssd.transaction;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Include;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import com.poseintelligence.cssd.utillity.Cons;

import general.Report.Reports;

import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;

@SuppressWarnings("rawtypes")
public class DepartmentMenu extends GenericForwardComposer{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3669018259070311653L;
	private EventQueue<Event> qe;
	private boolean B_IsCreate = true;
	
	private boolean B_DP_IsUsedDispatch;
	
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
	private Toolbarbutton Toolbarbutton_Project;
	private Toolbarbutton Toolbarbutton_Branch;
	private Toolbarbutton Toolbarbutton_User;
	
	private Tree TreeMenu;
	private Window Windows;
	private Treeitem Treeitem_0;
	private Treeitem Treeitem_1;
	private Treeitem Treeitem_5;
	private Treeitem Treeitem_4;
	
	private Window WinProcess;
	
	private Include Include_Src;
	
	
	

	public void onCreate() throws Exception {
		
		bySession();
		
		// Configuration
		onDisplayWebConfig();
		
		init();

    }
	
	
	@SuppressWarnings("unchecked")
	public void doAfterCompose(org.zkoss.zk.ui.Component comp) throws Exception {
		super.doAfterCompose(comp); 
		qe = EventQueues.lookup(Cons.EVENTQUEUE_CONNECTION, EventQueues.DESKTOP, true);
		qe.subscribe(new EventListener<Event>() {
			public void onEvent(Event event) throws Exception {
				
				Include_Src.setSrc(null);
				
				switch (event.getName()) {
				case Cons.EVENT_CALL_DEPARTMENT_RECEIVE:
					callPage("department_receive_stock_2.zul");
					TreeMenu.setSelectedItem(Treeitem_1);
					break;
				case Cons.EVENT_CALL_EXPIRING_SOON:
					callPage("warn_exp_new.zul");
					TreeMenu.setSelectedItem(Treeitem_0);
					break;
				case Cons.EVENT_CALL_EXPIRE:
					callPage("warn_before_exp_new.zul");
					TreeMenu.setSelectedItem(Treeitem_0);
					break;
				case Cons.EVENT_CALL_DEPARTMENT_RECEIVE_NOW:
					callPage("department_receive_stock_now_2.zul");
					TreeMenu.setSelectedItem(Treeitem_0);
					break;
//				case Cons.EVENT_SHOW_DISPATCH_MENU:
//					callPage("department_setting.zul");
//					Treeitem_5.setVisible(true);
//					break;
//				case Cons.EVENT_HIDE_DISPATCH_MENU:
//					callPage("department_setting.zul");
//					Treeitem_5.setVisible(false);
//					break;
				default:
					break;
				}
				
				
			}
		});
	}
	
	// Event
	
	public void onClick$Toolbarbutton_Logout(Event event) throws Exception {
		onLogout(); 
	}
		
	public void onClick$Toolbarbutton_Refresh(Event event) throws Exception {
		refresh(); 
	}
	
	// Click
		public void onClick$Treeitem_0(Event event) throws Exception {
			callPage("department_portal.zul");
		}
		public void onClick$Treeitem_1(Event event) throws Exception {
			callPage("department_receive_stock_2.zul");
		}
		public void onClick$Treeitem_2(Event event) throws Exception {
			callPage("department_send_sterile.zul");
		}
		public void onClick$Treeitem_22(Event event) throws Exception {
			callPage("department_send_sterile_2.zul");
		}
		
		public void onClick$Treeitem_3(Event event) throws Exception {
			callPage("payout2.zul");
		}
		
		public void onClick$Treeitem_4(Event event) throws Exception {
			callPage("trackingnew2.zul");
		}
		
		public void onClick$Treeitem_5(Event event) throws Exception {
			callPage("stock2.zul");
		}
		
		public void onClick$Treeitem_6(Event event) throws Exception {
			callPage("payout_section2.zul");
		}
		public void onClick$Treeitem_7(Event event) throws Exception {
			callPage("reports_hn2.zul");
		}
		public void onClick$Treeitem_8(Event event) throws Exception {
			callPage("report.zul");
		}
		public void onClick$Treeitem_9(Event event) throws Exception {
			callPage("department_item2.zul");
		}
		
		public void onClick$Treeitem_10(Event event) throws Exception {
			callPage("manual.zul");
		}

		
		public void onClick$Treeitem_11(Event event) throws Exception {
			callPage("setting.zul");
		}
		
		public void onClick$wbe(Event event) throws Exception {
			callPage("warn_before_exp.zul");
		}
		
		public void onClick$we(Event event) throws Exception {
			callPage("warn_exp.zul");
		}
		
		public void onClick$rec(Event event) throws Exception {
			callPage("receivein.zul");
		}
		
		public void onClick$rec_retro(Event event) throws Exception {
			callPage("receivein_retroact.zul");
		}
	
	public void onURIChange$Include_Src(Event event) throws Exception {
		String S_Src = Include_Src.getSrc();
    	S_Src.replace("/", "");
    	
    	if(S_Src.equals("timeout.zul"))
    		Executions.sendRedirect("/index.zul");
	}
	
	
	private void callPage(String page)throws Exception {
		onProcess(true);
		
		Include_Src.setSrc(null);
		Include_Src.setSrc(page);
		
		onProcess(false);
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
		
		//System.out.println("S_DB == " + S_DB);
	}
	
	private void init(){
		Toolbarbutton_Project.setLabel( ( S_DB == null || S_DB.equals("") || S_DB.equals("cssd_2_usage_test")) ? "cssd_test" : ( S_DB.replace("2_usage_", "") ) );
		Toolbarbutton_Branch.setLabel(S_DepName);
		Toolbarbutton_User.setLabel(S_UserName);
		
		TreeMenu.setSelectedItem(Treeitem_0);
		
		// Default page
		Include_Src.setSrc(null);
		Include_Src.setSrc("department_portal.zul");
		
//		Treeitem_5.setVisible(B_DP_IsUsedDispatch);
	}
	
	public void refresh() throws Exception {
    	
    }
	
	public void onLogout(){
		Messagebox.show("ยืนยันการออกจากระบบ?", " ", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,new EventListener<Event>() {
    		public void onEvent(Event evt) throws Exception {
    			switch (((Integer) evt.getData()).intValue()) {
                  	case Messagebox.YES:
                  		
                  		//get cookie
    		 			Cookie [] cookies = ((HttpServletRequest)Executions.getCurrent().getNativeRequest()).getCookies();
    		 			int i = 0;
    		 			
    		 			String S_DB = "";
    		 			
    		 			if (cookies != null) {
    		 			 	for (Cookie cookie : cookies) {
    		 			   		if (cookie.getName().equals("S_DB")) {
    		 				  		S_DB = "?db=" + cookies[i].getValue();
    		 			     		break;
    		 			    	}
    		 			   
    		 			   		i++;
    		 			  	}	
    		 			}
    		 					 		
    					Executions.sendRedirect("index.zul" + S_DB);
                  		
                		//Executions.sendRedirect("/index.zul");
                		
                  		break;
                  	case Messagebox.NO:
                  		break;
    			}
    		}
      });
	}

	public void onDisplayWebConfig() throws Exception{
		
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;
        
		try{	
			
			rs = stmt.executeQuery("SELECT DP_IsUsedDispatch FROM configuration_web LIMIT 1");

			if(rs.next()){
				B_DP_IsUsedDispatch = rs.getBoolean("DP_IsUsedDispatch");
			}
		}catch(Exception e){
			Messagebox.show("ERROR onDisplayWebConfig : " + e.getMessage());
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
    public void onPrintall() throws Exception{

    	
  		
    	String StrReportFile = "../assets/pdf/คู่มือ.pdf";
    	Reports r = new Reports();
    			
    	r.setFileName(StrReportFile);
    	r.setFileType("PDF");
    	r.setStrParameter(new ArrayList<String>() {
    		private static final long serialVersionUID = -2892797364135239183L;
    		{
    			
    		
    		}
    	});
    	

    	//Open Window
    	Iframe iframe = new Iframe();
    	iframe.setWidth("100%");
    	iframe.setHeight("100%");
    	iframe.setContent(r.doReport());
    	
    	Button aa = new Button();
    	aa.setWidth("50px");
    	aa.setHeight("50px");
    	
    	Windows.getChildren().clear();
    	Windows.setTitle("...");
    	Windows.setWidth("100%");
    	Windows.setHeight("100%");
    	Windows.appendChild(iframe);
    	Windows.appendChild(aa);

    	openWindow("Windows");
    }
    
    public void openWindow(String StrWindow) throws Exception {
    	Windows.setVisible(true);
    	Windows.setFocus(true);
    	Windows.setPosition("center");
    	Windows.setMode("modal");
	}
	
	public void closeWindow(String StrWindow) throws Exception {
		Windows.setMode("embedded");
		Windows.setVisible(false);
		Windows.setFocus(false);
	}  

}
