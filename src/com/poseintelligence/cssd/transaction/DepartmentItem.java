package com.poseintelligence.cssd.transaction;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Window;

import connect.DBConn;

public class DepartmentItem extends Window{

	private static final long serialVersionUID = -2946044092657050866L;
	private Connection conn = null;
    private Statement stmt = null;
    
    private Combobox Combobox_Unit;
    private Radiogroup Radio_ItemMode;
    private Button Button_Import;
    
    public void onCreate() throws Exception{
    	init();
    	onDisplayUnit();
    }
    
    public void init() throws SQLException{
    	DBConn objConnection = new DBConn();
		conn = objConnection.getConnection("root", "A$192dijd");
    	
        stmt = conn.createStatement();
        
        Combobox_Unit = ((Combobox) getFellow("Combobox_Unit"));
        Radio_ItemMode = ((Radiogroup) getFellow("Radio_ItemMode"));
        Button_Import = ((Button) getFellow("Button_Import"));
    }
    
    public void onClick$Radio_ItemMode(Event event) throws Exception{
    	try {
    		int imode_chk = Radio_ItemMode.getSelectedIndex();
    		if(imode_chk == 1) {
    			Button_Import.isDisabled();
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
   
    
    private String getUnitItem(){
    	
    	String S_Sql = "";
		S_Sql = 	
				"SELECT " 
			+	"units.ID, " 
			+	"units.UnitName " 
			+	"FROM " 
			+	"units "
			
			+	"WHERE units.IsCancel = 0 "
			+ 	"GROUP BY units.UnitName ";
		
		System.out.println(S_Sql);
		return S_Sql;
    }
    
    public void onDisplayUnit() throws Exception{
    	ResultSet rs = null;
    	rs = stmt.executeQuery(getUnitItem());

    		while(rs.next()){
    			Comboitem citem = new Comboitem();
    			
    			citem.setValue(rs.getString("ID"));
    			citem.setLabel(rs.getString("UnitName"));
    			 
    			Combobox_Unit.appendChild(citem);
    		}
    		rs.close();
    }
    

}
