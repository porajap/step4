package core;


import java.sql.ResultSet;
import java.util.Iterator;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treecol;
import org.zkoss.zul.Treecols;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

import connect.*;


public class Permission extends Borderlayout{

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
		onDisplayMenus();
	}
	
	private String getSqlMenuUsers(String Login_Code, String MenuGCode){
		return 	"SELECT  	menu_permission.permission_code," + 
							"menu_permission.Login_Code," +
							"menu_permission.menu_code," +
							"menu_permission.add," +
							"menu_permission.del," +
							"menu_permission.update," +
							"menu_permission.show," +
							"menu_permission.full," +
							"menu_permission.isAcc," +
							"menu.menu_code," + 
							"menu.menu_no," + 
							"menu.menu_name," + 
							"menu.menu_path," + 
							"menu.menu_order," +
							"menu.menu_image " + 
								
				"FROM 		menu_permission " +
																	
				"INNER JOIN	menu " +
				"ON			menu.menu_code = menu_permission.menu_code " +
											
				"WHERE		menu_permission.Login_Code = '"+ Login_Code +"' " +
				
				"AND		menu.mm_code = '"+ MenuGCode +"' " +
				
				"ORDER BY 	menu.menu_order ASC ";
	}
	
	private String getSqlMenuUsers(String MenuGCode){
		String StrSQL = "SELECT  " +
							"menu.menu_code," + 
							"menu.menu_code," + 
							"menu.menu_no," + 
							"menu.menu_name," + 
							"menu.menu_path," + 
							"menu.menu_order," +
							"menu.menu_image " + 
								
				"FROM 		menu " +
							
				"WHERE		menu.mm_code = '"+ MenuGCode +"' " +
				
				"ORDER BY 	menu.menu_order ASC ";
		
		//System.out.println(StrSQL);
		 
        return StrSQL ;
	}
	
	private String getSqlMenuGroups(){
		String StrSQL = "SELECT  	main_menu.mm_code," +
								   "main_menu.mm_name " +
				
						"FROM 		main_menu " +
				
						"ORDER BY 	mn_order ASC " ;
		
		 //System.out.println(StrSQL);
		 
         return StrSQL ;
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
	
	public void onSelectUsers() throws Exception{
		onDisplayMenuUsers();
	}
	
	public void onDisplayMenus() throws Exception{
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        
        SqlSelection sqlsel = new SqlSelection();
        sqlsel.uName = xUsername;
        sqlsel.Pwd = xPassword;
        try {
            ((Tree) getFellow("TreeMenu")).getChildren().clear();
            
            //Add Column
            Treecols trecs = new Treecols();
            trecs.appendChild(getTreecol("เมนู", "", null, ""));
            ((Tree) getFellow("TreeMenu")).appendChild(trecs);

            Treechildren treeChild = new Treechildren();

            rs1 = sqlsel.getReSultSQL(getSqlMenuGroups());

            while (rs1.next()) {
            	Treeitem treeItem = new Treeitem();
	        	Treerow treeRow = new Treerow();
	           	Treecell treecell = new Treecell();
	
	           	treecell.setLabel(rs1.getString("mm_name"));
	           	treecell.setStyle("color:#009900");
	                
	         	treeRow.appendChild(treecell);
	        	treeItem.appendChild(treeRow);
	          	treeChild.appendChild(treeItem);
	                
	           	((Tree) getFellow("TreeMenu")).appendChild(treeChild);
	
	          	rs2 = sqlsel.getReSultSQL(getSqlMenuUsers(rs1.getString("mm_code")));

	          	Treechildren treeChild2 = new Treechildren();
		
	         	while (rs2.next()) {
		             	Treeitem treeItem2 = new Treeitem();
		             	treeItem2.setOpen(false);
		             	treeItem2.setId(rs2.getString("menu_code"));
		               	treeItem2.setAttribute("path", rs2.getString("menu_path"));
		               	treeItem2.setAttribute("ID", rs2.getString("menu_code"));
		            	Treerow treeRow2 = new Treerow();
		             	Treecell treeCell2 = new Treecell();

		             	treeCell2.setLabel(rs2.getString("menu_name"));
		             	treeCell2.setTooltiptext(rs2.getString("menu_code") +" : " +rs2.getString("menu_name")+"; "+rs2.getString("menu_path"));
		
		             	treeRow2.appendChild(treeCell2);
		              	treeItem2.appendChild(treeRow2);
		              	treeChild2.appendChild(treeItem2);
		              	treeItem.appendChild(treeChild2); 
		      	}     
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	sqlsel.closeConnection();

            if (rs1 != null) {
                rs1.close();
            }
            
            if (rs2 != null) {
                rs2.close();
            }
        }
	}
	
	public void onDisplayMenuUsers() throws Exception{
		String Login_Code = null;
		
		if(((Listbox) getFellow("ListboxUsers")).getSelectedCount() == 1){		
			Login_Code = (String) ((Listbox)getFellow("ListboxUsers")).getSelectedItem().getAttribute("Login_Code");
		}else{
			return;
		}
			
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        SqlSelection sqlsel = new SqlSelection();
        sqlsel.uName = xUsername;
        sqlsel.Pwd = xPassword;
        try {
            ((Tree) getFellow("TreeMenuUser")).getChildren().clear();
            //Add Column
            Treecols trecs = new Treecols();
            trecs.setSizable(true);
            trecs.appendChild(getTreecol("เมนู", "", null, ""));
            trecs.appendChild(getTreecol("ทั้งหมด", "50px", null, "center"));
            trecs.appendChild(getTreecol("เพิ่ม", "50px", null, "center"));
            trecs.appendChild(getTreecol("ลบ", "50px", null, "center"));
            trecs.appendChild(getTreecol("แก้ไข", "50px", null, "center"));
            trecs.appendChild(getTreecol("แสดง", "50px", null, "center"));
            trecs.appendChild(getTreecol("บัญชี", "50px", null, "center"));
            
            ((Tree) getFellow("TreeMenuUser")).appendChild(trecs);
            
            Treechildren treeChild = new Treechildren();
            rs1 = sqlsel.getReSultSQL(getSqlMenuGroups());

            while (rs1.next()) {
                Treeitem treeItem = new Treeitem();
                Treerow treeRow = new Treerow();
                
                Treecell treecell = new Treecell();
                treecell.setLabel(rs1.getString("mm_name"));
                treecell.setStyle("color:#009900");
                treeRow.appendChild(treecell);
                
                treeRow.appendChild(new Treecell());
                treeRow.appendChild(new Treecell());
                treeRow.appendChild(new Treecell());
                treeRow.appendChild(new Treecell());
                treeRow.appendChild(new Treecell());
                
                treeItem.appendChild(treeRow);
                treeChild.appendChild(treeItem);
                
                ((Tree) getFellow("TreeMenuUser")).appendChild(treeChild);

                rs2 = sqlsel.getReSultSQL(getSqlMenuUsers(Login_Code, rs1.getString("mm_code")));
                
                Treechildren treeChild2 = new Treechildren();

                while (rs2.next()) {
                    Treeitem treeItem2 = new Treeitem();
                    treeItem2.setOpen(false);
                    treeItem2.setId(rs2.getString("permission_code"));
                    treeItem2.setAttribute("ID", rs2.getString("permission_code"));
                    treeItem2.setAttribute("path", rs2.getString("menu_path"));
                    
                    Treerow treeRow2 = new Treerow();
                    Treecell treeCell2 = new Treecell();

                    treeCell2.setLabel(rs2.getString("menu_name"));
                    treeRow2.appendChild(treeCell2);

                    //Add CHK
                    treeRow2.appendChild(getTreecellCheckbox("ChkFull", rs2.getString("permission_code"), true, rs2.getBoolean("full")));
                    treeRow2.appendChild(getTreecellCheckbox("ChkAdd", rs2.getString("permission_code"), false, rs2.getBoolean("add")));
                    treeRow2.appendChild(getTreecellCheckbox("ChkDelete", rs2.getString("permission_code"), false, rs2.getBoolean("del")));
                    treeRow2.appendChild(getTreecellCheckbox("ChkEdit", rs2.getString("permission_code"), false, rs2.getBoolean("update")));
                    treeRow2.appendChild(getTreecellCheckbox("ChkShow", rs2.getString("permission_code"), false, rs2.getBoolean("show")));
                    treeRow2.appendChild(getTreecellCheckbox("ChkAcc", rs2.getString("permission_code"), false, rs2.getBoolean("isAcc")));
                    
                    treeItem2.appendChild(treeRow2);
                    treeChild2.appendChild(treeItem2);
                    treeItem.appendChild(treeChild2);
                }
            }  
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	sqlsel.closeConnection();

            if (rs1 != null) {
                rs1.close();
            }
            
            if (rs2 != null) {
                rs2.close();
            }
            
            System.gc();
        }
	}
	
	private Treecol getTreecol(String label,String width,String style,String align){
		Treecol tcol = new Treecol(label);
		tcol.setStyle("background: #478FCA;font-size:12px;");
				
		tcol.setWidth(width);
		
		if(style != null)
			tcol.setStyle(style);
		
		tcol.setAlign(align);
		
		return tcol;
	}

	private Treecell getTreecellCheckbox(String prefix, final String id, boolean isFull, boolean isCheck) {
        Treecell treecell = new Treecell();
        final Checkbox chkbox = new Checkbox();
        chkbox.setId(prefix+id);
        chkbox.setChecked(isCheck);
        treecell.appendChild(chkbox);
        
        String Attribute_ ="";

        if(prefix.equals("ChkFull"))
        	Attribute_ = "full";	
        else if(prefix.equals("ChkAdd"))
        	Attribute_ = "add";	
        else if(prefix.equals("ChkDelete"))
        	Attribute_ = "del";	
        else if(prefix.equals("ChkEdit"))
        	Attribute_ = "update";	
        else if(prefix.equals("ChkShow"))
        	Attribute_ = "show";
        else if(prefix.equals("ChkAcc"))
        	Attribute_ = "isAcc";

        final String Attribute = Attribute_;
        
        //New Event Check box
        if(isFull){
	        chkbox.addEventListener("onCheck", new EventListener<Event>() {
	            public void onEvent(Event event) throws Exception {
	                boolean ischk = ((Checkbox) getFellow("ChkFull" + id)).isChecked();
	
	                ((Checkbox) getFellow("ChkAdd" + id)).setChecked(ischk);
	                ((Checkbox) getFellow("ChkEdit" + id)).setChecked(ischk);
	                ((Checkbox) getFellow("ChkDelete" + id)).setChecked(ischk);
	                ((Checkbox) getFellow("ChkShow" + id)).setChecked(ischk);
	                ((Checkbox) getFellow("ChkAcc" + id)).setChecked(ischk);
	                
	                ((Treeitem) getFellow(id)).setAttribute("IsSelect", "1");
	                
	                updatePermission(id, chkbox.isChecked() ? "1" : "0");
	            }
	        });
        }else{
        	chkbox.addEventListener("onCheck", new EventListener<Event>() {
	            public void onEvent(Event event) throws Exception {
	            	((Treeitem) getFellow(id)).setAttribute("IsSelect", "1");
	                checkAction(id);
	                
	                updatePermission(id, Attribute, chkbox.isChecked() ? "1" : "0");
	            }
	        });
        }
        
        return treecell;
    }
	
    private void checkAction(String id) {
        if (((Checkbox) getFellow("ChkAdd" + id)).isChecked()
         && ((Checkbox) getFellow("ChkEdit" + id)).isChecked()
         && ((Checkbox) getFellow("ChkDelete" + id)).isChecked()
         && ((Checkbox) getFellow("ChkShow" + id)).isChecked()
         && ((Checkbox) getFellow("ChkAcc" + id)).isChecked()
        ) {
            ((Checkbox) getFellow("ChkFull" + id)).setChecked(true);
        } else {
            ((Checkbox) getFellow("ChkFull" + id)).setChecked(false);
        } 
    }
	
	public void onAddMenuUsers(){
		if (((Tree) getFellow("TreeMenu")).getSelectedCount() < 1) {
			 Messagebox.show("ยังไม่ได้เลือกเมนู.", "แจ้งเตือน", Messagebox.OK, Messagebox.ERROR);
			 return;
        }else if(((Listbox) getFellow("ListboxUsers")).getSelectedCount() != 1){
        	 Messagebox.show("ยังไม่ได้เลือกผู้ใช้งาน.", "แจ้งเตือน", Messagebox.OK, Messagebox.ERROR);
        	 return;
        }
	
		String Login_Code = (String) ((Listbox)getFellow("ListboxUsers")).getSelectedItem().getAttribute("Login_Code");
		
        Iterator<Treeitem> i = ((Tree) getFellow("TreeMenu")).getSelectedItems().iterator();
        int AddComplete = 0;
        
        try {

            while (i.hasNext()) {
            	 Treeitem titem = (Treeitem) i.next();
            	 try {
            		 if(!isDupplicate(Login_Code, (String) titem.getAttribute("ID"))){
		            	 new OperationData(
		            			 xUsername,
		 						xPassword,
		         				"Insert",
		         				"menu_permission", 
		         				new String[][] {
		         					{"Login_Code", "'" + Login_Code + "'"},
		         					{"menu_code", "'" + (String) titem.getAttribute("ID") + "'"}	
		         				}, 
		         				null
		         			);
	            	 
		            	 AddComplete++;
            		 }
            	 }catch(Exception e){
            		 AddComplete = 0;
            	 }	 
            }
            
            if(AddComplete > 0){
            	Messagebox.show("เพิ่มข้อมูลสำเร็จ "+ AddComplete + " รายการ.", "แจ้งเตือน", Messagebox.OK, Messagebox.INFORMATION);
            	onDisplayMenuUsers();
            	((Tree) getFellow("TreeMenu")).clearSelection();
            } else{
            	Messagebox.show("เพิ่มข้อมูลไม่สำเร็จ.", "แจ้งเตือน", Messagebox.OK, Messagebox.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Messagebox.show("เพิ่มข้อมูลไม่สำเร็จ.\n โปรดทำรายการใหม่อีกครั้ง.\n" + e.getMessage(), "แจ้งเตือน", Messagebox.OK, Messagebox.ERROR);
            return;
        }
	}
	
	public boolean isDupplicate(String Login_Code, String menucode) throws Exception {
		ResultSet rs = null;
        SqlSelection sqlsel = new SqlSelection();
        sqlsel.uName = xUsername;
        sqlsel.Pwd = xPassword;
        try {
            rs = sqlsel.getReSultSQL(	"SELECT  	menu_permission.permission_code " + 
            							"FROM 		menu_permission " +
					            		"WHERE 		menu_permission.Login_Code = '" + Login_Code + "' " +
										"AND		menu_permission.menu_code = '" + menucode + "' " +
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
	
	public void onDelMenuUsers() throws Exception {
        Messagebox.show("ยืนยันการลบสิทธิ์เข้าใช้งานเมนู ?", "แจ้งเตือน", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
        	new EventListener<Event>() {
            	public void onEvent(Event evt) throws Exception {
            		switch (((Integer) evt.getData()).intValue()) {
                    	case Messagebox.YES: onDeleteMenuUsers(); break;
                 	}
            	}
        	}
        );
    }
	
	public void onDeleteMenuUsers(){
		if (((Tree) getFellow("TreeMenu")).getSelectedCount() < 1) {
			Messagebox.show("ยังไม่ได้เลือกเมนู.", "แจ้งเตือน", Messagebox.OK, Messagebox.ERROR);
			return;
		}else if(((Listbox) getFellow("ListboxUsers")).getSelectedCount() != 1){
       	 	Messagebox.show("ยังไม่ได้เลือกผู้ใช้งาน.", "แจ้งเตือน", Messagebox.OK, Messagebox.ERROR);
       	 	return;
		}

       Iterator<Treeitem> i = ((Tree) getFellow("TreeMenuUser")).getSelectedItems().iterator();
       int AddComplete = 0;
       
       try {
           while (i.hasNext()) {
           	 	Treeitem titem = (Treeitem) i.next(); 
           	 	try{
           	 	//System.out.println((String) titem.getAttribute("ID"));
           	 		new OperationData(
           	 			xUsername,
						xPassword,
    					"Delete",
    					"menu_permission", 
    					null,
    					new String[][] {
    						{"permission_code", "'" + (String) titem.getAttribute("ID") + "'" }
    					}
    				);

           	 		AddComplete++;	
           	 	}catch(Exception e){
           	 		AddComplete = 0;
           	 	}
           }
           
           if(AddComplete > 0){
           		Messagebox.show("ลบข้อมูลสำเร็จ "+ AddComplete + " รายการ.", "แจ้งเตือน", Messagebox.OK, Messagebox.INFORMATION);
           		onDisplayMenuUsers();
           		((Tree) getFellow("TreeMenu")).clearSelection();
           }else{
           		Messagebox.show("ลบข้อมูลไม่สำเร็จ.", "แจ้งเตือน", Messagebox.OK, Messagebox.ERROR);
           }
       } catch (Exception e) {
           e.printStackTrace();
           Messagebox.show("ลบข้อมูลไม่สำเร็จ.\n โปรดทำรายการใหม่อีกครั้ง.\n" + e.getMessage(), "แจ้งเตือน", Messagebox.OK, Messagebox.ERROR);
           return;
       }
	}
	
	private void updatePermission(String ID,String Attribute, String Value){
		try{
	 		new OperationData(
	 				xUsername,
					xPassword,
				"Update",
				"menu_permission", 
				new String[][] {
						{Attribute, Value}
					},
				new String[][] {
					{"permission_code", "'" + ID + "'" }
				}
			);
	 	}catch(Exception e){
	 		return;
	 	}
	}
	
	private void updatePermission(String ID, String Value){
		try{
	 		new OperationData(
	 				xUsername,
					xPassword,
				"Update",
				"menu_permission", 
				new String[][] {
						{"full", Value },
						{"add", Value },
						{"del", Value },
						{"update", Value },
						{"show", Value },
						{"isAcc", Value }
					},
				new String[][] {
					{"permission_code", "'" + ID + "'" }
				}
			);
	 	}catch(Exception e){
	 		return;
	 	}
	}
		
	public String getChecked(String chkbox,String id){
		try{
			return ((Checkbox) getFellow(chkbox + id)).isChecked() ? "1" : "0";
		}catch(Exception e){
			return "0";
		}
	}
}
