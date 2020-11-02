package com.poseintelligence.cssd.transaction;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Intbox;

import com.poseintelligence.cssd.utillity.Cons;

@SuppressWarnings("rawtypes")
public class DepartmentSetting extends GenericForwardComposer{
	
	private static final long serialVersionUID = -8716887210135693330L;
	private EventQueue<Event> qe;
	// Variable Session
	private boolean B_IsCreate = true;
	private Session session = Sessions.getCurrent();
			
	private String 	S_UserId,
					S_DeptId,
					S_UserName,
					S_IsAdmin,
					S_EmpCode,
					S_DepName,
					S_DB;
	
	// Widget
	private Checkbox Checkbox_RC_IsUsedReceiveManual;
	private Checkbox Checkbox_RC_IsUsedReceiveByBarCode;
	
	private Checkbox Checkbox_DP_IsUsedDispatch;
	private Checkbox Checkbox_DP_IsUsedDispatchByBarCode;
	
	private Checkbox Checkbox_GN_IsUsedWarningExpiringSoon; 
	private Checkbox Checkbox_GN_IsUsedWarningExpire; 
	
	private Checkbox Checkbox_SS_IsConfirmSendSterile; 
	private Checkbox Checkbox_SS_IsUsedApproveBeforeSterile; 
	private Checkbox Checkbox_SS_IsUsedConfirmScanEmployee;
	private Checkbox Checkbox_SS_IsFindItemDepartment;
	private Combobox Combobox_SS_IsFindStatus;
	
	private Intbox Intbox_GN_WarningExpiringSoonDay;
	
	public void onCreate() throws Exception {
		
		bySession();
		
		init();
		
		onDisplayWebConfig();
		
    }
	
	private void bySession(){
		if (B_IsCreate) {
			if (session.getAttribute("S_UserId") == null) {
				Executions.sendRedirect("/cssd_pose_logo.zul");
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
	}
	
	private void init(){
		
	}
	
	// Event
	
	
	public void onCheck$Checkbox_RC_IsUsedReceiveManual(Event event) throws Exception {
		updateConfig("RC_IsUsedReceiveManual" , (Checkbox_RC_IsUsedReceiveManual.isChecked() ? "1" : "0"));
	}
	
	public void onCheck$Checkbox_RC_IsUsedReceiveByBarCode(Event event) throws Exception {
		updateConfig("RC_IsUsedReceiveByBarCode" , (Checkbox_RC_IsUsedReceiveByBarCode.isChecked() ? "1" : "0"));
	}
		
	public void onCheck$Checkbox_DP_IsUsedDispatch(Event event) throws Exception {
		updateConfig("DP_IsUsedDispatch" , (Checkbox_DP_IsUsedDispatch.isChecked() ? "1" : "0"));
	}
	
	public void onCheck$Checkbox_DP_IsUsedDispatchByBarCode(Event event) throws Exception {
		updateConfig("DP_IsUsedDispatchByBarCode" , (Checkbox_DP_IsUsedDispatchByBarCode.isChecked() ? "1" : "0"));
	}
	
	public void onCheck$Checkbox_SS_IsConfirmSendSterile(Event event) throws Exception {
		updateConfig("SS_IsConfirmSendSterile" , (Checkbox_SS_IsConfirmSendSterile.isChecked() ? "1" : "0"));
	}
	
	public void onCheck$Checkbox_SS_IsUsedApproveBeforeSterile(Event event) throws Exception {
		updateConfig("SS_IsUsedApproveBeforeSterile" , (Checkbox_SS_IsUsedApproveBeforeSterile.isChecked() ? "1" : "0"));
	}
	
	public void onCheck$Checkbox_SS_IsUsedConfirmScanEmployee(Event event) throws Exception {
		updateConfig("SS_IsUsedConfirmScanEmployee" , (Checkbox_SS_IsUsedConfirmScanEmployee.isChecked() ? "1" : "0"));
	}
	
	public void onCheck$Checkbox_GN_IsUsedWarningExpiringSoon(Event event) throws Exception {
		updateConfig("GN_IsUsedWarningExpiringSoon" , (Checkbox_GN_IsUsedWarningExpiringSoon.isChecked() ? "1" : "0"));
	}
	
	public void onCheck$Checkbox_GN_IsUsedWarningExpire(Event event) throws Exception {
		updateConfig("GN_IsUsedWarningExpire" , (Checkbox_GN_IsUsedWarningExpire.isChecked() ? "1" : "0"));
	}
	
	public void onChange$Intbox_GN_WarningExpiringSoonDay(Event event) throws Exception {
		updateConfig("GN_WarningExpiringSoonDay" , Intbox_GN_WarningExpiringSoonDay.getText());
	}
	
	public void onSelect$Combobox_SS_IsFindStatus(Event event) throws Exception {
		try {
			updateConfig("SS_IsFindStatus" , Integer.toString( Combobox_SS_IsFindStatus.getSelectedIndex() ) );
		}catch(Exception e) {
			return;
		}
	}
	
	public void onCheck$Checkbox_SS_IsFindItemDepartment(Event event) throws Exception {
		updateConfig("Checkbox_SS_IsFindItemDepartment" , (Checkbox_SS_IsFindItemDepartment.isChecked() ? "1" : "0"));
	}
	
	
	

	public void updateConfig(String S_Field, String S_Value) throws Exception{
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
        
        String S_Sql = null;
        
        try{	
        	
        	if(S_Field.contentEquals("DP_IsUsedDispatch") && S_Value.equals("0")) {
        		S_Sql = "UPDATE configuration_web SET DP_IsUsedDispatch = 0, SS_IsFindStatus = 2 ";
        		
        		Combobox_SS_IsFindStatus.setSelectedIndex(2);
        		Combobox_SS_IsFindStatus.setDisabled(true);
        		
        		qe = EventQueues.lookup(Cons.EVENTQUEUE_CONNECTION, EventQueues.DESKTOP, true);
				qe.publish(new Event(Cons.EVENT_HIDE_DISPATCH_MENU, null, null));
        		
        	}else {
        		S_Sql = "UPDATE configuration_web SET " + S_Field + " = " + S_Value;
        		
        		if(S_Field.contentEquals("DP_IsUsedDispatch")) {
        			Combobox_SS_IsFindStatus.setDisabled(false);
        			
        			qe = EventQueues.lookup(Cons.EVENTQUEUE_CONNECTION, EventQueues.DESKTOP, true);
    				qe.publish(new Event(Cons.EVENT_SHOW_DISPATCH_MENU, null, null));
    				
        		}
        	}
        	
        	stmt.executeUpdate( S_Sql );
        	
        	System.out.println(S_Sql);
        	
		}catch(Exception e){
			e.printStackTrace();
		}finally{

	        if (stmt != null) {
	            stmt.close();
	        }
	        
	        if (conn != null) {
	            conn.close();
	        }
		}
	}

	public void onDisplayWebConfig() throws Exception{
		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
        Class.forName(c.S_MYSQL_DRIVER);
        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
        conn.setAutoCommit(false);
        
        Statement stmt = conn.createStatement();
    	ResultSet rs = null;
        
		try{	
			
			rs = stmt.executeQuery("SELECT * FROM configuration_web LIMIT 1");

			if(rs.next()){
				Checkbox_RC_IsUsedReceiveManual.setChecked( rs.getBoolean("RC_IsUsedReceiveManual") );
				Checkbox_RC_IsUsedReceiveByBarCode.setChecked( rs.getBoolean("RC_IsUsedReceiveByBarCode") );
				
				Checkbox_DP_IsUsedDispatch.setChecked( rs.getBoolean("DP_IsUsedDispatch") );
				Checkbox_DP_IsUsedDispatchByBarCode.setChecked( rs.getBoolean("DP_IsUsedDispatchByBarCode") );
				
				Checkbox_GN_IsUsedWarningExpiringSoon.setChecked( rs.getBoolean("GN_IsUsedWarningExpiringSoon") );
				Checkbox_GN_IsUsedWarningExpire.setChecked( rs.getBoolean("GN_IsUsedWarningExpire") ); 
				Intbox_GN_WarningExpiringSoonDay.setText(rs.getString("GN_WarningExpiringSoonDay"));
				
				Checkbox_SS_IsConfirmSendSterile.setChecked( rs.getBoolean("SS_IsConfirmSendSterile") );
				Checkbox_SS_IsUsedApproveBeforeSterile.setChecked( rs.getBoolean("SS_IsUsedApproveBeforeSterile") ); 
				Checkbox_SS_IsUsedConfirmScanEmployee.setChecked( rs.getBoolean("SS_IsUsedConfirmScanEmployee") );	
				Checkbox_SS_IsFindItemDepartment.setChecked( rs.getBoolean("SS_IsFindItemDepartment") );
	
				try {
					Combobox_SS_IsFindStatus.setSelectedIndex(rs.getInt("SS_IsFindStatus")); 
					
					Combobox_SS_IsFindStatus.setDisabled(!Checkbox_DP_IsUsedDispatch.isChecked());
				}catch(Exception e) {
					e.printStackTrace();
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
	
}
