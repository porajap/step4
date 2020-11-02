package com.poseintelligence.cssd.transaction;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Space;
import org.zkoss.zul.Window;

import com.poseintelligence.cssd.utillity.Cons;

@SuppressWarnings("rawtypes")
public class DepartmentPortal extends GenericForwardComposer{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2088359352495714419L;
	private EventQueue<Event> qe;
	private boolean B_IsCreate = true;
	// Variable Session
	private Session session = Sessions.getCurrent();
	
	private String 	S_UserId,
					S_DeptId,
					S_UserName,
					S_IsAdmin,
					S_EmpCode,
					S_DepName,
					B_ID,
					S_DB,
					i_itemcode;
	
	private Hbox Hbox_form;
	
	private String 	S_ListStatus = "-5, 5, 6";
	
	private boolean B_GN_IsUsedWarningExpire = false;	
	private boolean B_GN_IsUsedWarningExpiringSoon = false;
	private int I_GN_WarningExpiringSoonDay = 7;
	
	public void onCreate() throws Exception {
		
		bySession();
		
		onDisplayWebConfig();
		
		init();
	
    }
	
	private void bySession(){
		try {
		if (B_IsCreate) {
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
				
	        }
			
			B_IsCreate = false;
		}
		}catch (Exception e) {
//			Messagebox.show("ERROR bySessionTracking : " + e.getMessage());
//			System.out.println("ERROR bySession : " + e.getMessage());
		}
	}
	
	private void init() throws Exception{
		
			onDisplay(1);
			onDisplay(4);
		if(B_GN_IsUsedWarningExpiringSoon)
			onDisplay(2);
		
		if(B_GN_IsUsedWarningExpire)
			onDisplay(3);
	
	}

	public void onDisplay(int selected) throws Exception{
		
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;
        
		try{	
			
			//System.out.print(getSql(selected));
			
			rs = stmt.executeQuery(getSql(selected));
					
			if(rs.next()){
				switch (selected) {
					case 1:
						if(rs.getInt("c") == 0) {
							
						}else {
							Hbox_form.appendChild(getWindow(rs.getInt("c"))); break;
						}
					case 2:
						if(rs.getInt("c") == 0) {
							
						}else {
							Hbox_form.appendChild(getWindow2(rs.getInt("c"))); break;
						}
					case 3:
						if(rs.getInt("c") == 0) {
							
						}else {
						    Hbox_form.appendChild(getWindow3(rs.getInt("c"))); break;
						}
					case 4:
						if(rs.getInt("c") == 0) {
							
						}else {
						    Hbox_form.appendChild(getWindow4(rs.getInt("c"))); break;
						}
			
					default: break;
				}
				
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
	
	private String getSql(int selected) {
		switch (selected) {
			case 1:
				
				return getSqlCountDocument();
			case 2:
				
				return getSqlExpiringSoon();
			case 3:
				
				return getSqlExpire();
			case 4:
				
				return getSqlCountDocument2();
	
			default:
				return null;
		}
	}
	

	
	private String getSqlCountDocument() {
		
		String S_Sql = "";
		
		S_Sql = 
				
			"SELECT " + 
			"                COUNT(payout.DocNo) AS c" + 
			"                FROM payout" + 
			"                WHERE (payout.IsStatus = '2' OR payout.IsStatus = '6' OR payout.IsStatus = '9') " + 
			"                AND payout.DeptID = '"+S_DeptId+"' " + 
			"                AND payout.B_ID = '"+B_ID+"' " + 
			"                AND payout.IsCancel = '0'" + 
			"                AND payout.IsPrint != 0" + 
			"                AND DATE(payout.CreateDate) = DATE(NOW())";
		
		return S_Sql;

	}
	
	private String getSqlCountDocument2() {
		
		String S_Sql = "";
		
		S_Sql = 
				
			"SELECT " + 
			"                COUNT(payout.DocNo) AS c" + 
			"                FROM payout" + 
			"                WHERE (payout.IsStatus = '2' OR payout.IsStatus = '6' OR payout.IsStatus = '9') " + 
			"                AND payout.DeptID = '"+S_DeptId+"' " + 
			"                AND payout.B_ID = '"+B_ID+"' " + 
			"                AND payout.IsCancel = '0'" + 
			"                AND payout.IsPrint != 0" + 
			"                AND DATE(payout.CreateDate) BETWEEN DATE(NOW() - INTERVAL 3 MONTH - INTERVAL 1 DAY) " + 
			"                AND DATE(NOW() - INTERVAL 1 DAY)";
		
		return S_Sql;

	}
	
	private String getSqlExpiringSoon() {
		
		String S_Sql = "";
		
		S_Sql = 
				
			"SELECT" + 
			"	COUNT( itemstock.RowID ) AS c " + 
			"FROM" + 
			"	itemstock" + 
			"	INNER JOIN item ON itemstock.ItemCode = item.itemcode" + 
			"	INNER JOIN department ON itemstock.DepID = department.ID " + 
			"WHERE" + 
			"	( itemstock.IsStatus = 5 AND itemstock.IsDispatch = 0 AND itemstock.IsPay = 1 ) " + 
			"	AND itemstock.DepID = '"+S_DeptId+"' " + 
			"	AND itemstock.B_ID = '"+B_ID+"' " + 
			"	AND (" + 
			"	DATE( itemstock.ExpireDate ) BETWEEN DATE_ADD( DATE( NOW()), INTERVAL 1 DAY ) " + 
			"	AND DATE_SUB( DATE( NOW()), INTERVAL -( SELECT settingapp.Nobeforeexp FROM settingapp WHERE settingapp.DeptID = '"+S_DeptId+"' ) DAY )) ";
		
		return S_Sql;

	}
	
	private String getSqlExpire() {
		
		 String Sql = "SELECT " + 
			 		"		            COUNT(itemstock.RowID) AS c " + 
			 		"		            FROM " + 
			 		"		            itemstock " + 
			 		"		            INNER JOIN item ON itemstock.ItemCode = item.itemcode " + 
			 		"		            INNER JOIN department ON itemstock.DepID = department.ID " + 
			 		"		            WHERE (itemstock.IsStatus = 5 AND itemstock.IsDispatch = 0 AND itemstock.IsPay = 1) AND itemstock.DepID = '"+S_DeptId+"' " + 
			 		"                AND itemstock.B_ID = '"+B_ID+"' " + 
			 		"                AND DATE(NOW())>=DATE(itemstock.ExpireDate) " + 
			 		"                AND NOT MONTH(itemstock.ExpireDate)  = '2' " + 
			 		"                AND NOT MONTH(itemstock.ExpireDate)  = '3' " + 
			 		"                AND NOT MONTH(itemstock.ExpireDate)  = '4' " + 
			 		"				 AND NOT MONTH(itemstock.ExpireDate)  = '5' " + 
			 		"                AND NOT MONTH(itemstock.ExpireDate)  = '6' " +
			 		"                AND NOT DATE(itemstock.ExpireDate)  BETWEEN '2020-07-01' AND '2020-08-31' " + 
			 		"                AND NOT item.NoWash  = '1' " + 
			 		"                AND NOT item.NoWashType  = '2' " + 
			 		"                ORDER BY DATE(itemstock.ExpireDate) ASC";
		return Sql;

	}
	

	
	private Window getWindow(int S_Count) {
		Window w = new Window();
		w.setBorder("normal");
		w.setSclass("sysWin");
		w.setHeight("auto");
		w.setWidth("300px");
//		w.setStyle("background: #28a745;");
		Button btn = new Button("ไปยังหน้ารับของเข้าสต๊อกใหม่");
		btn.setSclass("btn-success");
		//btn.setIconSclass("z-icon-ban");
		btn.setHeight("auto");
		btn.setWidth("99%");
		btn.setStyle("background: #28a745; color:#ffffff;border-radius: 4px;");
		//btn.setImage("/images/ic_search_c.png");
		//btn.setHoverImage("/images/pose_favicon.png");
		
		btn.addEventListener("onClick", new EventListener<Event>() {
			public void onEvent(Event event) throws Exception {
				qe = EventQueues.lookup(Cons.EVENTQUEUE_CONNECTION, EventQueues.DESKTOP, true);
				qe.publish(new Event(Cons.EVENT_CALL_DEPARTMENT_RECEIVE_NOW, null, null));
	        }
	    });
		
		Separator sep = new Separator();
		sep.setHeight("35px");
		
		w.appendChild(new Caption("เอกสารรับเข้าสต๊อกใหม่"));
		w.appendChild(new Label("(" + S_Count + " เอกสาร)")) ;		
		w.appendChild(sep);
		w.appendChild(btn);
		
		return w;
	}
	
	private Window getWindow2(int S_Count) {
		Window w = new Window();
		w.setBorder("normal");
		w.setSclass("sysWin");
		w.setHeight("auto");
		w.setWidth("300px");
		
		Button btn = new Button("ไปยังหน้าอุปกรณ์ใกล้หมดอายุ ");
		btn.setSclass("btn-success");
		//btn.setIconSclass("z-icon-ban");
		btn.setHeight("auto");
		btn.setWidth("99%");
		btn.setStyle("background: #f0ad4e; color:#ffffff;border-radius: 4px;");
		//btn.setImage("/images/ic_search_c.png");
		//btn.setHoverImage("/images/pose_favicon.png");
		
		btn.addEventListener("onClick", new EventListener<Event>() {
			public void onEvent(Event event) throws Exception {
				qe = EventQueues.lookup(Cons.EVENTQUEUE_CONNECTION, EventQueues.DESKTOP, true);
				qe.publish(new Event(Cons.EVENT_CALL_EXPIRING_SOON, null, null));
	        }
	    });
		
		Separator sep = new Separator();
		sep.setHeight("35px");
		
		

			w.appendChild(new Caption("อุปกรณ์ใกล้หมดอายุ"));
			w.appendChild(new Label("(" + S_Count + " รายการ)")) ;		
			w.appendChild(sep);
			w.appendChild(btn);
			
			return w;
		

		
	}
	
	private Window getWindow3(int S_Count) {
		Window w = new Window();
		w.setBorder("normal");
		w.setSclass("sysWin");
		w.setHeight("auto");
		w.setWidth("300px");
		
		Button btn = new Button("ไปยังหน้าอุปกรณ์หมดอายุ ");
		btn.setSclass("btn-success");
		//btn.setIconSclass("z-icon-ban");
		btn.setHeight("auto");
		btn.setWidth("99%");
		btn.setStyle("background: #d9534f; color:#ffffff;border-radius: 4px;");
		//btn.setImage("/images/ic_search_c.png");
		//btn.setHoverImage("/images/pose_favicon.png");
		
		btn.addEventListener("onClick", new EventListener<Event>() {
			public void onEvent(Event event) throws Exception {
				qe = EventQueues.lookup(Cons.EVENTQUEUE_CONNECTION, EventQueues.DESKTOP, true);
				qe.publish(new Event(Cons.EVENT_CALL_EXPIRE, null, null));
	        }
	    });
		
		Separator sep = new Separator();
		sep.setHeight("35px");
		
		w.appendChild(new Caption("อุปกรณ์หมดอายุ"));
		w.appendChild(new Label("(" + S_Count + " รายการ)")) ;		
		w.appendChild(sep);
		w.appendChild(btn);
		
		return w;
	}

	private Window getWindow4(int S_Count) {
		Window w = new Window();
		w.setBorder("normal");
		w.setSclass("sysWin");
		w.setHeight("auto");
		w.setWidth("300px");
		
		Button btn = new Button("ไปยังหน้ารับเข้าสต๊อกย้อนหลัง");
		btn.setSclass("btn-success");
		//btn.setIconSclass("z-icon-ban");
		btn.setHeight("auto");
		btn.setWidth("99%");
		btn.setStyle("background: #D882CC; color:#ffffff;border-radius: 4px;");
		//btn.setImage("/images/ic_search_c.png");
		//btn.setHoverImage("/images/pose_favicon.png");
		
		btn.addEventListener("onClick", new EventListener<Event>() {
			public void onEvent(Event event) throws Exception {
				qe = EventQueues.lookup(Cons.EVENTQUEUE_CONNECTION, EventQueues.DESKTOP, true);
				qe.publish(new Event(Cons.EVENT_CALL_DEPARTMENT_RECEIVE, null, null));
	        }
	    });
		
		Separator sep = new Separator();
		sep.setHeight("35px");
		
		w.appendChild(new Caption("เอกสารรับเข้าสต๊อกย้อนหลัง"));
		w.appendChild(new Label("(" + S_Count + " รายการ)")) ;		
		w.appendChild(sep);
		w.appendChild(btn);
		
		return w;
	}
	//================================================================================
	// Web Configuration 
	// ================================================================================
		
	public void onDisplayWebConfig() throws Exception{
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
	    Class.forName(c.S_MYSQL_DRIVER);
	    Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
	    conn.setAutoCommit(false);
	     
	    Statement stmt = conn.createStatement();
	     
	 	ResultSet rs = null;
     
		try{	
			
			rs = stmt.executeQuery("SELECT GN_IsUsedWarningExpiringSoon, GN_IsUsedWarningExpire, GN_WarningExpiringSoonDay FROM configuration_web LIMIT 1");

			if(rs.next()){
				B_GN_IsUsedWarningExpire = rs.getBoolean("GN_IsUsedWarningExpire");	
				B_GN_IsUsedWarningExpiringSoon = rs.getBoolean("GN_IsUsedWarningExpiringSoon");	
				I_GN_WarningExpiringSoonDay = rs.getInt("GN_WarningExpiringSoonDay");	
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
}


