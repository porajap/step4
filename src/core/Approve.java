package core;


import java.sql.ResultSet;
import java.util.Iterator;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import connect.*;


public class Approve extends Borderlayout{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6299559606033992255L;
	public String SessionUserCode;
	private String xUsername = null;
	private String xPassword = null;
	
	public void onCreate() throws Exception {
		Session session = Sessions.getCurrent();

		if (session.getAttribute("UserCode") == null) {
			Executions.sendRedirect("/Login.zul");
		} else {
			SessionUserCode = (String) session.getAttribute("UserCode");
			xUsername = (String)session.getAttribute("xUsrName");
	    	xPassword = (String)session.getAttribute("xPassword");
			init();
        }
    }

	public void init() throws Exception{
		onDisplayUsers(null);
		onDisplayUserApproves();
	}
	
	private String getSqlUsers(String StrUsers){
		return 	"SELECT  Login_Code," +
						"FName,"+
				   		"LName,"+
				   		"Username "+
			 
			  	"FROM 	login "+
		
				( 
					(StrUsers != null && !StrUsers.trim().equals("")) 
					? ("WHERE Username LIKE '%"+ StrUsers.replace(" ", "%") +"%' OR FName LIKE '%"+ StrUsers.replace(" ", "%") +"%' ")
					: ""
				);

	}
	
	private String getSqlUserApproves(){
		
		return 	"SELECT  approve.Approve_Code," 
						+ "approve.Login_Code,"
						+ "login.FName,"
						+ "login.LName,"
						+ "login.Username "
			 
				+ "FROM 		approve "
			  	
			  	+ "INNER JOIN 	login "
			  	+ "ON			login.Login_Code = approve.Login_Code ";
	}
	
	public void onDisplayUsers(String StrUsers) throws Exception{
		ResultSet rs = null;
        SqlSelection sqlsel = new SqlSelection();
        sqlsel.uName = xUsername;
        sqlsel.Pwd = xPassword;
		try{	
			rs = sqlsel.getReSultSQL(getSqlUsers(StrUsers));
			((Listbox) getFellow("ListboxUsers")).getItems().clear();
			
			while(rs.next()){
				Listitem list = new Listitem();	
				list.appendChild(new Listcell());
				list.appendChild(new Listcell(rs.getString("Username") + " [ " + rs.getString("FName") + "  " + rs.getString("LName")+ " ] ","/images/login.gif" ) );
				list.setAttribute("Login_Code", rs.getString("Login_Code"));
				
				((Listbox) getFellow("ListboxUsers")).appendChild(list);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			sqlsel.closeConnection();

            if (rs != null) {
                rs.close();
            }
		}
	}

	public void onDisplayUserApproves() throws Exception{
		ResultSet rs = null;
        SqlSelection sqlsel = new SqlSelection();
        sqlsel.uName = xUsername;
        sqlsel.Pwd = xPassword;
		try{	
			rs = sqlsel.getReSultSQL(getSqlUserApproves());
			((Listbox) getFellow("ListboxUserApproves")).getItems().clear();
			
			while(rs.next()){
				Listitem list = new Listitem();	
				list.appendChild(new Listcell());
				list.appendChild(new Listcell(rs.getString("Username") + " [ " + rs.getString("FName") + "  " + rs.getString("LName")+ " ] ","/images/user.png" ) );
				list.setAttribute("Approve_Code", rs.getString("Approve_Code"));
				
				((Listbox) getFellow("ListboxUserApproves")).appendChild(list);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			sqlsel.closeConnection();

            if (rs != null) {
                rs.close();
            }
            
            ((Listbox) getFellow("ListboxUsers")).clearSelection();
		}
	}
	
	public void onAddUsers(){
		if(((Listbox) getFellow("ListboxUsers")).getSelectedCount() != 1){
        	 Messagebox.show("ยังไม่ได้เลือกผู้ใช้งาน.", "แจ้งเตือน", Messagebox.OK, Messagebox.ERROR);
        	 return;
        }
	
		String Login_Code = null;
		
        Iterator<Listitem> i = ((Listbox)getFellow("ListboxUsers")).getSelectedItems().iterator();
        
        int AddComplete = 0;
        
        try {

            while (i.hasNext()) {
            	Listitem li = (Listitem) i.next();
            	
            	 try {
            		 
            		 Login_Code = (String) li.getAttribute("Login_Code");
            		 
            		 if(!isDupplicate(Login_Code)){
		            	 new OperationData(
		            			xUsername,
		 						xPassword,
		         				"Insert",
		         				"approve", 
		         				new String[][] {
		         					{"Login_Code", "'" + Login_Code + "'"},
		         				}, 
		         				null
		         			);
	            	 
		            	 AddComplete++;
            		 }
            	 }catch(Exception e){
            		 AddComplete = 0;
            		 e.printStackTrace();
            	 }	 
            }
            
            if(AddComplete > 0){
            	Messagebox.show("เพิ่มข้อมูลสำเร็จ "+ AddComplete + " รายการ.", "แจ้งเตือน", Messagebox.OK, Messagebox.INFORMATION);
            	onDisplayUserApproves();
            } else{
            	Messagebox.show("เพิ่มข้อมูลไม่สำเร็จ.", "แจ้งเตือน", Messagebox.OK, Messagebox.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Messagebox.show("เพิ่มข้อมูลไม่สำเร็จ.\n โปรดทำรายการใหม่อีกครั้ง.\n" + e.getMessage(), "แจ้งเตือน", Messagebox.OK, Messagebox.ERROR);
            return;
        }
	}
	
	public boolean isDupplicate(String Login_Code) throws Exception {
		ResultSet rs = null;
        SqlSelection sqlsel = new SqlSelection();
        sqlsel.uName = xUsername;
        sqlsel.Pwd = xPassword;
        try {
            rs = sqlsel.getReSultSQL(	"SELECT  	approve.Approve_Code " + 
            							"FROM 		approve " +
					            		"WHERE 		approve.Login_Code = '" + Login_Code + "' " +
					            		"LIMIT 1 "
            						);
            
            return rs.next(); 
            
        } catch (Exception e) {
        	return false;
        } finally {
        	sqlsel.closeConnection();

            if (rs != null) {
                rs.close();
            }
        }
	}
	
	public void onDel() throws Exception {
        Messagebox.show("ยืนยันการลบผู้อนุมัติ ?", "แจ้งเตือน", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
        	new EventListener<Event>() {
            	public void onEvent(Event evt) throws Exception {
            		switch (((Integer) evt.getData()).intValue()) {
                    	case Messagebox.YES: onDelete(); break;
                 	}
            	}
        	}
        );
    }
	
	public void onDelete(){
		if(((Listbox) getFellow("ListboxUserApproves")).getSelectedCount() != 1){
       	 	Messagebox.show("ยังไม่ได้เลือกรายการ.", "แจ้งเตือน", Messagebox.OK, Messagebox.ERROR);
       	 	return;
		}

       int DelComplete = 0;
       
       try {

    	   String Approve_Code = null;
   		
           Iterator<Listitem> i = ((Listbox)getFellow("ListboxUserApproves")).getSelectedItems().iterator();
           
    	   while (i.hasNext()) {
           		Listitem li = (Listitem) i.next();
           	
           		try {
           		 
           			Approve_Code = (String) li.getAttribute("Approve_Code");
           		 
           		 	new OperationData(
		            			xUsername,
		 						xPassword,
		         				"Delete",
		         				"approve", 
		         				null, 
		         				new String[][] {
		         					{"Approve_Code", "'" + Approve_Code + "'"},
		         				}
		         			);
	            	 
           		 	DelComplete++;
           		 	
           		}catch(Exception e){
           			DelComplete = 0;
           			e.printStackTrace();
           	 	}	 
           	}
           
           	if(DelComplete > 0){
           		Messagebox.show("ลบข้อมูลสำเร็จ "+ DelComplete + " รายการ.", "แจ้งเตือน", Messagebox.OK, Messagebox.INFORMATION);
           		onDisplayUserApproves();
           	} else{
           		Messagebox.show("ลบข้อมูลไม่สำเร็จ.", "แจ้งเตือน", Messagebox.OK, Messagebox.ERROR);
           	}
       	} catch (Exception e) {
       		e.printStackTrace();
       		Messagebox.show("ลบข้อมูลไม่สำเร็จ.\n โปรดทำรายการใหม่อีกครั้ง.\n" + e.getMessage(), "แจ้งเตือน", Messagebox.OK, Messagebox.ERROR);
       		return;
       	}
	}
	

}
