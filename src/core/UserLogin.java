package core;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

import org.zkoss.zk.ui.ComponentNotFoundException;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import connect.OperationData;
import connect.SqlOperation;
import connect.SqlSelection;

public class UserLogin extends Borderlayout{
	
	/**
	 * 
	 */
	
	private static final long serialVersionUID = -6072915538491674626L;
	public String SessionUserCode;
	
	private boolean mode1 = false;
	private String xUsername = null;
	private String xPassword = null;
	
	private String[] StrDataCombo = {
		"ComboboxBranch", "branch", "Branch_Code", "Branch_Name",
		"ComboboxProvince", "th_province", "Pv_Code", "Name_Th",
	};
			
	public void onCreate() throws Exception {
		Session session = Sessions.getCurrent();

		if (session.getAttribute("UserCode") == null) {
			Executions.sendRedirect("/Login.zul");
		} else {
			SessionUserCode = session.getAttribute("UserCode").toString();
			xUsername = (String)session.getAttribute("xUsrName");
	    	xPassword = (String)session.getAttribute("xPassword");
			init();
			((Borderlayout) getFellow("Usr")).setVisible(true);
        }
    }

	public void init() throws Exception{
		//Display User
		onDisplay("");
		
		// Difine
		addData(0, false);
	}
	
    private void addData(int StartIndex, boolean isBreak) throws Exception {
    	//0.ComponentName, 1.TableName, 2.ID, 3.Name
    	ResultSet rs = null;
        SqlSelection sqlsel = new SqlSelection();
        sqlsel.uName = xUsername;
        sqlsel.Pwd = xPassword;
        try {
            for (int i = (StartIndex * 4); i < StrDataCombo.length; i = i + 4) {
                rs = null;
                String 
                
                StrSql =  "SELECT  " 	+ StrDataCombo[i + 2] + ","
                						+ StrDataCombo[i + 3] + " ";

                StrSql += "FROM  " 		+ StrDataCombo[i + 1] + " ";

                StrSql += "ORDER BY " 	+ StrDataCombo[i + 3] + " ASC ";

                rs = sqlsel.getReSultSQL(StrSql);

                ((Combobox) getFellow(StrDataCombo[i])).getItems().clear();

                while (rs.next()) {
                    Comboitem citem = new Comboitem();
                    citem.setLabel(rs.getString(StrDataCombo[i + 3]));
                    citem.setValue(rs.getString(StrDataCombo[i + 2]));
                    ((Combobox) getFellow(StrDataCombo[i])).appendChild(citem);
                }
                
                if(isBreak)
                	break;
            }   
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }

            sqlsel.closeConnection();
        }
    }
	
	private String getSql(String txt){
		String Sql =  	"SELECT  login.Login_Code, " +
								"login.FName, " +
								"login.LName, " +
								"login.Username, " +
								"login.password, " +
								"login.Login_Status, " +
								"login.Branch_Code, " +
								"login.Address, " +
								"login.Tb_Code, " +
								"login.Tel, " +
								"login.Post, " +
								"login.Email," +
								"th_tambon.Tb_Name," +
								"th_amphur.Ap_Name," +
								"th_province.Name_Th," +
								"branch.Branch_Name " +
					
						"FROM 	login " +
						
						"LEFT JOIN 	branch " +
						"ON			branch.Branch_Code = login.Branch_Code " +
		
						"LEFT JOIN 	th_tambon " +
						"ON  		th_tambon.Tb_Code = login.Tb_Code " +
						
						"LEFT JOIN 	th_amphur " +
						"ON  		th_amphur.Ap_Code = th_tambon.Ap_Code " +
						
						"LEFT JOIN 	th_province " +
						"ON  		th_province.Pv_Code = th_amphur.Pv_Code " ;

						
		
		if(!txt.trim().equals("")){		
			Sql +=		"WHERE	login.FName LIKE '%" + txt.replace(" ", "%") + "%' " ;
			Sql +=		"OR		login.LName LIKE '%" + txt.replace(" ", "%") + "%' " ;
			Sql +=		"OR		login.Username LIKE '%" + txt.replace(" ", "%") + "%' " ;
			Sql +=		"OR		login.Login_Code LIKE '%" + txt.replace(" ", "%") + "%' " ;
		}
					
		Sql +=			"ORDER BY login.Username ASC ";
		
		return Sql;
	}
	
    // Find Aumpher & Tambon
    private String getSqlAmphur(String Pv_Code){
		return 	"SELECT 	 th_amphur.Ap_Code, " +
							"th_amphur.Ap_Name, " +
							"th_amphur.Pv_Code " +
				
				"FROM 		th_amphur " +

				"WHERE		th_amphur.Pv_Code = '" + Pv_Code + "' " +
				
				"ORDER BY 	th_amphur.Ap_Name ASC ";
	}
    
	private String getSqlTambon(String id){
		return 	"SELECT 	 th_tambon.Tb_Code, " +
							"th_tambon.Tb_Name, " +
							"th_tambon.Ap_Code, " +
							"th_amphur.Ap_Name " +
				
				"FROM 		th_tambon " +
				
				"INNER JOIN th_amphur " +
				"ON  		th_amphur.Ap_Code = th_tambon.Ap_Code " +
				
				"WHERE		th_tambon.Ap_Code = '" + id + "' " +
				
				"ORDER BY 	th_tambon.Tb_Name ASC ";
	}
	
	public void onSelectProvince(Combobox Cbb_Province, Combobox Cbb_Amphur, Combobox Cbb_District) throws ComponentNotFoundException, Exception{
		if(!isSelected(Cbb_Province))
			return;
		
		ResultSet rs = null;
        SqlSelection sqlsel = new SqlSelection();
        sqlsel.uName = xUsername;
        sqlsel.Pwd = xPassword;
		try{	
			
			rs = sqlsel.getReSultSQL(getSqlAmphur( (String) Cbb_Province.getSelectedItem().getValue()) );
			
			Cbb_Amphur.getItems().clear();

			while(rs.next()){
				Comboitem cbi = new Comboitem();
				cbi.setLabel(rs.getString("Ap_Name"));
				cbi.setValue(rs.getString("Ap_Code"));
				
				Cbb_Amphur.appendChild(cbi);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			sqlsel.closeConnection();

            if (rs != null) {
                rs.close();
            }
            
            Cbb_Amphur.setText("");
            Cbb_District.getItems().clear();
            Cbb_District.setText("");
		}
	}
	
	public void onSelectAumpher(Combobox Cbb_Amphur, Combobox Cbb_District) throws ComponentNotFoundException, Exception{
		if(!isSelected(Cbb_Amphur))
			return;
		
		ResultSet rs = null;
        SqlSelection sqlsel = new SqlSelection();
        sqlsel.uName = xUsername;
        sqlsel.Pwd = xPassword;
		try{	

			rs = sqlsel.getReSultSQL(getSqlTambon( (String) Cbb_Amphur.getSelectedItem().getValue()) );
			
			Cbb_District.getItems().clear();

			while(rs.next()){
				Comboitem cbi = new Comboitem();
				cbi.setLabel(rs.getString("Tb_Name"));
				cbi.setValue(rs.getString("Tb_Code"));
				
				Cbb_District.appendChild(cbi);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			sqlsel.closeConnection();

            if (rs != null) {
                rs.close();
            }
            
            Cbb_District.setText("");
		}
	}
	
	// =============== Operation  ===============
	
	public void onDisplay(String txt) throws Exception{
		ResultSet rs = null;
        SqlSelection sqlsel = new SqlSelection();
        sqlsel.uName = xUsername;
        sqlsel.Pwd = xPassword;
		try{	
			rs = sqlsel.getReSultSQL(getSql(txt));
			ResultSetMetaData metaData = rs.getMetaData();
            
    		int count = metaData.getColumnCount();
    		
    		ArrayList<String> a = new ArrayList<String>();
    		
    		for (int i = 1; i <= count; i++){
    			a.add(metaData.getColumnName(i)); 
    		}
    		
			((Listbox) getFellow("ListboxUserLogin")).getItems().clear();
			int row = 1;
			
			while(rs.next()){
				Listitem list = new Listitem();
				list.appendChild(new Listcell(row + "."));
				list.appendChild(new Listcell(rs.getString("Branch_Name")));
				list.appendChild(new Listcell(rs.getString("Username")));
				list.appendChild(new Listcell(rs.getString("FName")));
				list.appendChild(new Listcell(rs.getString("LName")));
				list.appendChild(new Listcell(rs.getString("Address")));
				list.appendChild(new Listcell(rs.getString("Tel")));
				list.appendChild(new Listcell(rs.getString("Email")));

				//Attribute
                for(int i=0; i<a.size(); i++)
					list.setAttribute(a.get(i), rs.getString(a.get(i)));

				((Listbox) getFellow("ListboxUserLogin")).appendChild(list);
				
				row++;
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
	
	public void onSelect() throws ComponentNotFoundException, Exception{
		try{
			Listitem li = ((Listbox) getFellow("ListboxUserLogin")).getSelectedItem();
			mode1 = true;
			((Textbox) getFellow("TextboxUserName")).setText( (String) li.getAttribute("Username") );
			((Textbox) getFellow("TextboxPassword")).setText( (String) li.getAttribute("Password") );
			((Textbox) getFellow("TextboxFName")).setText( (String) li.getAttribute("FName") );
			((Textbox) getFellow("TextboxLName")).setText( (String) li.getAttribute("LName") );
			
			((Combobox) getFellow("ComboboxBranch")).setText( (String) li.getAttribute("Branch_Name") );
			((Textbox) getFellow("TextboxAddress")).setText( (String) li.getAttribute("Address") );
			
			((Textbox) getFellow("TextboxPostCode")).setText( (String) li.getAttribute("Post") );
			((Textbox) getFellow("TextboxTel")).setText( (String) li.getAttribute("Tel") );
			((Textbox) getFellow("TextboxEmail")).setText( (String) li.getAttribute("Email") );
			
			((Combobox) getFellow("ComboboxProvince")).setText( (String) li.getAttribute("Name_Th") );
			onSelectProvince((Combobox) getFellow("ComboboxProvince"), (Combobox) getFellow("ComboboxAmphur"), (Combobox) getFellow("ComboboxDistrict"));
			((Combobox) getFellow("ComboboxAmphur")).setText( (String) li.getAttribute("Ap_Name") );
			onSelectAumpher((Combobox) getFellow("ComboboxAmphur"), (Combobox) getFellow("ComboboxDistrict"));
			((Combobox) getFellow("ComboboxDistrict")).setText( (String) li.getAttribute("Tb_Name") );	
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void onSave() throws Exception{
		try{
			// Check Form
			if( !((Textbox) getFellow("TextboxUserName")).getText().trim().equals("") && 
				!((Textbox) getFellow("TextboxPassword")).getText().trim().equals("") && 
				!((Textbox) getFellow("TextboxFName")).getText().trim().equals("") && 
				!((Textbox) getFellow("TextboxLName")).getText().trim().equals("") &&
				isSelected((Combobox) getFellow("ComboboxBranch"))
			){
				
				if(mode1){
			   	 	// Operation Update
					new OperationData(
							xUsername,
							xPassword,
			   	 		"Update",
						"login", 
						new String[][] {
							//{"Username", "'" + ((Textbox) getFellow("TextboxUserName")).getText() + "'" },
							{"Password", "'" + ((Textbox) getFellow("TextboxPassword")).getText() + "'" },
							{"FName", "'" + ((Textbox) getFellow("TextboxFName")).getText() + "'" },
							{"LName", "'" + ((Textbox) getFellow("TextboxLName")).getText() + "'" },
							{"Login_Status", "1" },
							{"Branch_Code", isSelected((Combobox) getFellow("ComboboxBranch")) ? ("'" + (String)((Combobox) getFellow("ComboboxBranch")).getSelectedItem().getValue() + "'") : null },
							{"Address", "'" + ((Textbox) getFellow("TextboxAddress")).getText() + "'" },
							{"Tb_Code", isSelected((Combobox) getFellow("ComboboxDistrict")) ? ("'" + (String)((Combobox) getFellow("ComboboxDistrict")).getSelectedItem().getValue() + "'") : null },
							{"Tel", "'" + ((Textbox) getFellow("TextboxTel")).getText() + "'" },
							{"Post", "'" + ((Textbox) getFellow("TextboxPostCode")).getText() + "'" },
							{"Email", "'" + ((Textbox) getFellow("TextboxEmail")).getText() + "'" },
						},
						new String[][] {
							{"Login_Code", "'" + (String) ((Listbox) getFellow("ListboxUserLogin")).getSelectedItem().getAttribute("Login_Code") + "'" }
						}
					);
				}else{
					// Operation Insert
					new OperationData(
							xUsername,
							xPassword,
			   	 		"Insert",
						"login", 
						new String[][] {
			   	 			{"Username", "'" + ((Textbox) getFellow("TextboxUserName")).getText() + "'" },
							{"Password", "'" + ((Textbox) getFellow("TextboxPassword")).getText() + "'" },
							{"FName", "'" + ((Textbox) getFellow("TextboxFName")).getText() + "'" },
							{"LName", "'" + ((Textbox) getFellow("TextboxLName")).getText() + "'" },
							{"Login_Status", "1" },
							{"Branch_Code", isSelected((Combobox) getFellow("ComboboxBranch")) ? ("'" + (String)((Combobox) getFellow("ComboboxBranch")).getSelectedItem().getValue() + "'") : null },
							{"Address", "'" + ((Textbox) getFellow("TextboxAddress")).getText() + "'" },
							{"Tb_Code", isSelected((Combobox) getFellow("ComboboxDistrict")) ? ("'" + (String)((Combobox) getFellow("ComboboxDistrict")).getSelectedItem().getValue() + "'") : null },
							{"Tel", "'" + ((Textbox) getFellow("TextboxTel")).getText() + "'" },
							{"Post", "'" + ((Textbox) getFellow("TextboxPostCode")).getText() + "'" },
							{"Email", "'" + ((Textbox) getFellow("TextboxEmail")).getText() + "'" },
						},
						null
					);
					
					//Insert User In DB
					SqlOperation sqlopt = new SqlOperation();
					//sqlopt.executeUpdateSQL("CREATE USER '" + ((Textbox) getFellow("TextboxUserName")).getText() + "'@'%' IDENTIFIED BY 'password';");
					
					sqlopt.executeUpdateSQLByRoot(
						"INSERT INTO mysql.user " +
						"(Host,User,Password,Select_priv,Insert_priv,Update_priv, Delete_priv, Create_priv, Create_user_priv, ssl_cipher, x509_issuer,x509_subject) " +
						"VALUES('%','" + ((Textbox) getFellow("TextboxUserName")).getText() + "',PASSWORD('password'),'Y','Y','Y','Y','Y','Y',1,1,1)"
					);	
				}

				// Clear
		   	 	onClear();
		   	 	
		   	 	// Display
		   	 	onDisplay("");
			}else{
				Messagebox.show("ป้อนข้อมูลไม่ครบ โปรดป้อนข้อมูลให่อีกครั้ง.", "แจ้งเตือน", Messagebox.OK , "");
			}
	   	}catch(Exception e){
	   		e.printStackTrace();
	   		// Clear
	   	 	onClear();		
	   	}
	}
	
	public void onDelete() throws Exception{
		 Messagebox.show("Confirm the deletion.", " ", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,new EventListener<Event>() {
             public void onEvent(Event evt) throws Exception {
                 switch (((Integer) evt.getData()).intValue()) {
                     case Messagebox.YES:
                    	 onDel();
                         break;
                     case Messagebox.NO:
                         break;
                 }
             }
         });
	}
	
	public void onDel() throws Exception{
		if(((Listbox) getFellow("ListboxUserLogin")).getSelectedCount() == 1){
			try{
				// Operation
	   	 		new OperationData(
	   	 			xUsername,
					xPassword,
					"Delete",
					"login", 
					null,
					new String[][] {
						{"Login_Code", "'" + (String) ((Listbox) getFellow("ListboxUserLogin")).getSelectedItem().getAttribute("Login_Code") + "'" }
					}
				);
	   	 		
	   	 		// Clear
		   	 	onClear();
		   	 	
		   	 	// Display
		   	 	onDisplay("");
		   	 	
		   	 	//Drop User
		   	 	SqlOperation sqlopt = new SqlOperation();
				sqlopt.executeUpdateSQLByRoot(
					"DROP USER '" + ((Textbox) getFellow("TextboxUserName")).getText() + "'@'%'" 
				);	
				
	   	 	}catch(Exception e){
	   	 		// Clear
		   	 	onClear();
	   	 	}
		}
	}
	
	public void onClear() throws Exception{
		mode1 = false;
		((Listbox) getFellow("ListboxUserLogin")).clearSelection();
		
		((Textbox) getFellow("TextboxUserName")).setText("");
		((Textbox) getFellow("TextboxPassword")).setText("");
		((Textbox) getFellow("TextboxFName")).setText("");
		((Textbox) getFellow("TextboxLName")).setText("");
		
		((Textbox) getFellow("TextboxAddress")).setText("");

		((Combobox) getFellow("ComboboxProvince")).setText("");
		((Combobox) getFellow("ComboboxAmphur")).setText("");
		((Combobox) getFellow("ComboboxDistrict")).setText("");
		((Combobox) getFellow("ComboboxBranch")).setText("");
		
		((Combobox) getFellow("ComboboxAmphur")).getItems().clear();
		((Combobox) getFellow("ComboboxDistrict")).getItems().clear();
		
		((Textbox) getFellow("TextboxPostCode")).setText("");
		((Textbox) getFellow("TextboxTel")).setText("");
	}
	
	// Utility
	public boolean isSelected(Combobox cbb){
		try {
			return cbb.getSelectedItem().getValue() != null; 
        } catch (Exception e) {
        	return false;
        }
	}
}
