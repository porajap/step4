package process;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import connect.DBConn;
import general.DateTime;
import general.Report.Reports;

public class report extends Window{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -810268810837869564L;
	ResultSet rs = null;
	private Connection conn = null;
	private Statement stmt = null;
	private String xUsername = null;
	private String xPassword = null;
	
	private Textbox TextboxSearchReport;
	private Datebox textboxreportsdate;
	private Datebox textboxreportedate;
	private Combobox cboReport;

	
	public void onCreate() throws Exception{
		init();
		
		
		
	}
	
	private void init()throws SQLException {
		DBConn objConnection = new DBConn();
		conn = objConnection.getConnection("root", "A$192dijd");
		stmt = conn.createStatement();

		
		TextboxSearchReport = ((Textbox)getFellow("TextboxSearchReport"));
		textboxreportsdate = ((Datebox)getFellow("textboxreportsdate"));
		textboxreportedate = ((Datebox)getFellow("textboxreportedate"));
		cboReport = ((Combobox)getFellow("cboReport"));
		
		cboReport.setSelectedIndex(0);
		textboxreportsdate.setText(DateTime.getDateNow());
		textboxreportedate.setText(DateTime.getDateNow());
	}
	
	
	public void onPrint() throws Exception{
		try {
//		combobox.getSelectedItem().getValue();
//		comboboxM.getSelectedItem().getValue();
//		comboboxY.getSelectedItem().getValue();
//		TextboxPV.getText();
		
		textboxreportsdate.getText();
		textboxreportedate.getText();
		cboReport.getSelectedItem().getValue();
		
		String StrReportFile = "present_report1_1";
		Reports r = new Reports();
		r.uName = xUsername;
		r.uPwd = xPassword;
				
		r.setFileName(StrReportFile);
		r.setFileType("PDF");
		r.setStrParameter(new ArrayList<String>() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 172828866608578712L;

			{
				add("sDate");
				add(textboxreportsdate.getText());
				add("eDate");
				add(textboxreportedate.getText());
				add("TestProgramName");
				add(cboReport.getSelectedItem().getValue());
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
		
		((Window) getFellow("Windows")).getChildren().clear();
		((Window) getFellow("Windows")).setTitle("...");
		((Window) getFellow("Windows")).setWidth("100%");
		((Window) getFellow("Windows")).setHeight("100%");
		((Window) getFellow("Windows")).appendChild(iframe);
		((Window) getFellow("Windows")).appendChild(aa);

		openWindow("Windows");
		}catch (Exception e) {
			Messagebox.show("ERROR onPrintReport : " + e.getMessage());
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
