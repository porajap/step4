package process;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import connect.DBConn;
import general.Report.Reports;

public class pdf extends Window{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -810268810837869564L;
	private Connection conn = null;
	private Statement stmt = null;
	private String xUsername = null;
	private String xPassword = null;
	
	private Textbox inputUsername;
	private Textbox inputPassword;
	private Combobox ComboboxSelect;
	private Window Windows;
	
	public void onCreate() throws Exception{

	}
	
	private void init()throws SQLException {
		DBConn objConnection = new DBConn();
		conn = objConnection.getConnection("root", "A$192dijd");
		stmt = conn.createStatement();

		
		
		
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

	    	Button aa = new Button();
	    	aa.setWidth("50px");
	    	aa.setHeight("50px");

	    }
	    
	   
}
