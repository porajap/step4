package process;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Button;
import org.zkoss.zul.Messagebox;

import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

import connect.DBConn;
import connect.SqlSelection;


public class report_hn_window extends Window{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6365073630321342768L;
	
	public ResultSet rs = null;
	public Connection conn = null;
	public Statement stmt = null;
	public Statement stmt2 = null;
	public boolean B_IsCreate = true;
	public Session session = Sessions.getCurrent();
	public String xUsername = null;
	public String xPassword = null;
	public String xUser = null;
	public String hncode = null;
	public String xPwd = null;
	
	public Textbox HnCode_w_n;
	public Textbox Fname_w_n;
	public Textbox Date_w_n;
	
//	public Window HNwindow;
	public Textbox Date_c_n;
	public Textbox FItem_c_n;
	public Textbox Round_c_n;
	public Textbox sTime_c_n;
	public Textbox eTime_c_n;
	public Textbox Approve_c_n;
	public Textbox Approve_c_n2;
	
	public Textbox Date_w_c;
	public Textbox FItem_w_c;
	public Textbox Usage_w_c;
	
	public Textbox Approve_c_c;
	public Textbox Approve_c_c2;
	
	public Textbox Ready_e_c;
	public Textbox Check_e_c;

	public Textbox Date_c_s;
	public Textbox FItem_c_s;
	public Textbox Round_c_s;
	public Textbox sTime_c_s;
	public Textbox eTime_c_s;
	
	public String hncode1;
	public String FName;
	public String DocDate;
	public String CreateDate;
	public String  usagecode ;
	public String  LastSterileDetailID ;
	public Button btnAdd;
	public String 	S_UserId,
					S_DeptId,
					S_UserName,
					S_IsAdmin,
					S_EmpCode,
					S_DepName,
					B_ID,
					S_DB,
					i_itemcode,
					sDatex,
					eDatex,
					HnCode,
					UsageCode;
	
	public void onCreate() throws Exception{
		init();		
		bySession();
		timer();
//		((Timer) getFellow("Time")).start();

	}

	public void init()throws SQLException {
		SqlSelection sql = new SqlSelection();
		sql.uName = xUsername;
		sql.Pwd = xPassword;
		DBConn objConnection = new DBConn();
		conn = objConnection.getConnection("root", "A$192dijd");
		stmt = conn.createStatement();
		stmt2 = conn.createStatement();
		
		HnCode_w_n = ((Textbox) getFellow("HnCode_w_n"));
		Fname_w_n = ((Textbox) getFellow("Fname_w_n"));
		Date_w_n = ((Textbox) getFellow("Date_w_n"));
		
		Date_c_n = ((Textbox) getFellow("Date_c_n"));
		FItem_c_n = ((Textbox) getFellow("FItem_c_n")); 
		Round_c_n = ((Textbox) getFellow("Round_c_n"));
		sTime_c_n = ((Textbox) getFellow("sTime_c_n"));
		eTime_c_n = ((Textbox) getFellow("eTime_c_n"));
		Approve_c_n = ((Textbox) getFellow("Approve_c_n"));
		Approve_c_n2 = ((Textbox) getFellow("Approve_c_n2"));
		
		Date_w_c = ((Textbox) getFellow("Date_w_c"));
		FItem_w_c = ((Textbox) getFellow("FItem_w_c"));
		Usage_w_c = ((Textbox) getFellow("Usage_w_c"));
		
		Approve_c_c = ((Textbox) getFellow("Approve_c_c"));
		Approve_c_c2 = ((Textbox) getFellow("Approve_c_c2"));
		
		Ready_e_c = ((Textbox) getFellow("Ready_e_c"));
		Check_e_c = ((Textbox) getFellow("Check_e_c"));
		
		Date_c_s = ((Textbox) getFellow("Date_c_s"));
		FItem_c_s = ((Textbox) getFellow("FItem_c_s"));
		Round_c_s = ((Textbox) getFellow("Round_c_s"));
		sTime_c_s = ((Textbox) getFellow("sTime_c_s"));
		eTime_c_s = ((Textbox) getFellow("eTime_c_s"));
		
		btnAdd = ((Button) getFellow("btnAdd"));
		
//		HNwindow = ((Window) getFellow("HNwindow"));
	}
	
	public void bySession()throws Exception{
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
				S_DB = (String)session.getAttribute("S_DB");
				B_ID = (String)session.getAttribute("B_ID");
				i_itemcode = (String)session.getAttribute("itemcode");		
				HnCode = (String)session.getAttribute("HnCode");
				sDatex = (String)session.getAttribute("sDate");
				eDatex = (String)session.getAttribute("eDate");
				UsageCode = (String)session.getAttribute("UsageCode");
	        }

		}catch (Exception e) {
			System.out.println("ERROR bySession : " + e.getMessage());
		}
	}

	public String getSqlWestNorth(String hncode1)throws Exception {
		
		try {
			
			String Sql="SELECT " + 
					"	hotpitalnumber.HnCode, " + 
					"	hotpitalnumber.FName, " + 
					"	itemstock.UsageCode, " + 
					"	hncode_detail.LastSterileDetailID, " + 
					"	item.itemname, " + 
					"	DATE_FORMAT(hncode.DocDate, '%d/%m/%Y') AS DocDate, " + 
					"	hotpitalnumber.HnAge, " + 
					"	hotpitalnumber.HnMonth, " + 
					"	PERIOD_DIFF( " + 
					"		DATE_FORMAT(NOW(), '%Y%m'), " + 
					"		DATE_FORMAT( " + 
					"			hotpitalnumber.CreateDate, " + 
					"			'%Y%m' " + 
					"		) " + 
					"	) AS DiffMonth " + 
					"FROM " + 
					"	hncode " + 
					"INNER JOIN hncode_detail ON hncode.DocNo = hncode_detail.DocNo " + 
					"INNER JOIN itemstock ON hncode_detail.ItemStockID = itemstock.RowID " + 
					"INNER JOIN item ON itemstock.ItemCode = item.itemcode " + 
					"INNER JOIN hotpitalnumber ON hncode.HnCode = hotpitalnumber.HnCode " + 
					"WHERE " + 
					"	hncode.IsCancel = 0 " +
					"AND hncode.HnCode = '"+hncode1+"' " + 
					"ORDER BY " + 
					"	hncode.DocDate DESC " + 
					"LIMIT 1  ";
			
//			System.out.println("getSqlWestNorth : " + Sql);
			return Sql;
		}catch (Exception e) {
			e.printStackTrace();
			Messagebox.show("getSqlWestNorth : " + e.getMessage());
			System.out.println("getSqlWestNorth : " + e.getMessage());
		}
		return null;
		
		
		
	}
	
	public void onDisplayWestNorth()throws Exception {
		try {
//			System.out.println("hncode : "+hncode1);
		
			timer();

			hncode1 =  (String) ((Window) getFellow("HNwindow")).getAttribute("HnCode");
			
			rs = stmt.executeQuery(getSqlWestNorth(hncode1));

			while(rs.next()) {
			 String HnCode = rs.getString("HnCode");
			 String FName = rs.getString("FName");
			String CreateDate = rs.getString("DocDate");
			
			HnCode_w_n.setText(HnCode);
			Fname_w_n.setText(FName);
			Date_w_n.setText(CreateDate);
			}

		}catch (Exception e) {
			e.printStackTrace();

			System.out.println("onDisplayWestNorth : " + e.getMessage());
		}
	}

	
	public void timer() {

		((Timer) getFellow("Time")).start();

	}

	
public String getSqlCenterNorth(String usagecode, String LastSterileDetailID)throws Exception {
		
		try {
			
			String Sql="SELECT " + 
					"            DATE_FORMAT( NOW(), '%d-%m-%Y' ) AS ddate, " + 
					"            item.itemname, " + 
					"			 steriledetail.ID, "+
//					"            hncode_detail.LastSterileDetailID, " + 
					"            itemstock.UsageCode, " + 
					"            DATE_FORMAT( wash.DocDate, '%d-%m-%Y' ) AS washdate, " + 
					"            washmachine.MachineName AS WashMachineName, " + 
					"            wash.WashRoundNumber AS WashRoundNumber, " + 
					"            TIME( wash.StartTime ) AS timeSdatew, " + 
					"            TIME( wash.FinishTime ) AS timeEdatew, " + 
					"            CONCAT( emp1.FirstName, ' ', emp1.LastName ) AS washBeforeApprovename, " + 
					"            CONCAT( emp2.FirstName, ' ', emp2.LastName ) AS washAfterApprovename, " + 
					"            DATE_FORMAT( sterile.DocDate, '%d-%m-%Y' ) AS steriledate, " + 
					"            sterilemachine.MachineName2 AS SterileMachineName, " + 
					"            sterile.SterileRoundNumber AS SterileRoundNumber, " + 
					"            TIME( sterile.StartTime ) AS timeSdates, " + 
					"            TIME( sterile.FinishTime ) AS timeEdates, " + 
					"            CONCAT( emp3.FirstName, ' ', emp3.LastName ) AS ppsname, " + 
					"            CONCAT( emp4.FirstName, ' ', emp4.LastName ) AS appsname, " + 
					"            CONCAT( emp5.FirstName, ' ', emp5.LastName ) AS sterilesname, " + 
					"            CONCAT( emp6.FirstName, ' ', emp6.LastName ) AS sterileBeforeApprovename, " + 
					"            CONCAT( emp7.FirstName, ' ', emp7.LastName ) AS sterileAfterApprovename  " + 
					"          FROM " + 
					"            itemstock " + 
					"            LEFT JOIN item ON itemstock.ItemCode = item.itemcode " + 
					"            LEFT JOIN washdetail ON itemstock.RowID = washdetail.ItemStockID " + 
//					"			 INNER JOIN hncode_detail ON itemstock.LastSterileDetailID = hncode_detail.LastSterileDetailID "+
					"            LEFT JOIN wash ON washdetail.WashDocNo = wash.DocNo " + 
					"            LEFT JOIN washmachine ON wash.WashMachineID = washmachine.ID " + 
					"            LEFT JOIN employee AS emp1 ON wash.BeforeApprove = emp1.EmpCode " + 
					"            LEFT JOIN employee AS emp2 ON wash.AfterApprove = emp2.EmpCode " + 
					"            LEFT JOIN steriledetail ON itemstock.RowID = steriledetail.ItemStockID " + 
					"            LEFT JOIN sterile ON steriledetail.DocNo = sterile.DocNo " + 
					"            LEFT JOIN sterilemachine ON sterile.SterileMachineID = sterilemachine.ID " + 
					"            LEFT JOIN employee AS emp3 ON sterile.PrepareCode = emp3.ID " + 
					"            LEFT JOIN employee AS emp4 ON sterile.ApproveCode = emp4.ID " + 
					"            LEFT JOIN employee AS emp5 ON sterile.SterileCode = emp5.ID " + 
					"            LEFT JOIN employee AS emp6 ON sterile.BeforeApprove = emp6.EmpCode " + 
					"            LEFT JOIN employee AS emp7 ON sterile.AfterApprove = emp7.EmpCode  " + 
					"          WHERE " + 
					"            itemstock.UsageCode = '"+usagecode+"'  " + 
					"            AND steriledetail.ID = '"+LastSterileDetailID+"' " + 
					"          ORDER BY " + 
					"            wash.DocDate DESC, " + 
					"            sterile.DocDate DESC  " + 
					"            LIMIT 1 ";
			
//			System.out.println("getSqlCenterNorth : " + Sql);
			return Sql;
		}catch (Exception e) {
			e.printStackTrace();
			Messagebox.show("getSqlCenterNorth : " + e.getMessage());
			System.out.println("getSqlCenterNorth : " + e.getMessage());
		}
		return null;
}	

		public void onDisplayCenterNorth()throws Exception {
			try {
//				System.out.println("hncode : "+hncode1);
			
				timer();
				
				usagecode =  (String) ((Window) getFellow("HNwindow")).getAttribute("UsageCode");
				LastSterileDetailID =  (String) ((Window) getFellow("HNwindow")).getAttribute("ID");
				
				rs = stmt.executeQuery(getSqlCenterNorth(usagecode,LastSterileDetailID));

				while(rs.next()) {
				 String washdate = rs.getString("washdate");
				 String WashMachineName = rs.getString("WashMachineName");
				 String WashRoundNumber = rs.getString("WashRoundNumber");
				 String timeSdatew = rs.getString("timeSdatew");
				 String timeEdatew = rs.getString("timeEdatew");
				 
				 String washBeforeApprovename = rs.getString("washBeforeApprovename");
				 String washAfterApprovename = rs.getString("washAfterApprovename");	
					
				
				Date_c_n.setText(washdate);
				FItem_c_n.setText(WashMachineName);
				Round_c_n.setText(WashRoundNumber);
				sTime_c_n.setText(timeSdatew);
				eTime_c_n.setText(timeEdatew);
				
				Approve_c_n.setText(washBeforeApprovename);
				Approve_c_n2.setText(washAfterApprovename);
				}
				
			}catch (Exception e) {
				e.printStackTrace();

				System.out.println("onDisplayCenterNorth : " + e.getMessage());
			}
		}

			public void onDisplayWestCenter()throws Exception {
				try {
//					System.out.println("hncode : "+hncode1);
				
					timer();
					
					usagecode =  (String) ((Window) getFellow("HNwindow")).getAttribute("UsageCode");
					LastSterileDetailID =  (String) ((Window) getFellow("HNwindow")).getAttribute("ID");
					
					rs = stmt.executeQuery(getSqlCenterNorth(usagecode,LastSterileDetailID));

					while(rs.next()) {
					 String ddate = rs.getString("ddate");
					 String itemname = rs.getString("itemname");
					 String UsageCode = rs.getString("UsageCode");
			
						
					
					 Date_w_c.setText(ddate);
					 FItem_w_c.setText(itemname);
					 Usage_w_c.setText(UsageCode);

					}
					
				}catch (Exception e) {
					e.printStackTrace();

					System.out.println("onDisplayWestCenter : " + e.getMessage());
				}
			}
			public void onDisplayCenterCenter()throws Exception {
				try {
//					System.out.println("hncode : "+hncode1);
				
					timer();

					
					rs = stmt.executeQuery(getSqlCenterNorth(usagecode,LastSterileDetailID));

					while(rs.next()) {
					 String sterileBeforeApprovename = rs.getString("sterileBeforeApprovename");
					 String sterileAfterApprovename = rs.getString("sterileAfterApprovename");

					 Approve_c_c.setText(sterileBeforeApprovename);
					 Approve_c_c2.setText(sterileAfterApprovename);

					}
					
				}catch (Exception e) {
					e.printStackTrace();

					System.out.println("onDisplayCenterCenter : " + e.getMessage());
				}
			}
			public void onDisplayEestCenter()throws Exception {
				try {
//					System.out.println("hncode : "+hncode1);
				
					timer();

					rs = stmt.executeQuery(getSqlCenterNorth(usagecode,LastSterileDetailID));

					while(rs.next()) {
					 String ppsname = rs.getString("ppsname");
					 String appsname = rs.getString("appsname");
			
						
					
					 Ready_e_c.setText(ppsname);
					 Check_e_c.setText(appsname);

					}
					
				}catch (Exception e) {
					e.printStackTrace();

					System.out.println("onDisplayEestCenter : " + e.getMessage());
				}
			}
			
			public void onDisplaySouthCenter()throws Exception {
				try {
//					System.out.println("hncode : "+hncode1);
				
					timer();
					
					
					rs = stmt.executeQuery(getSqlCenterNorth(usagecode,LastSterileDetailID));

					while(rs.next()) {
					 String steriledate = rs.getString("steriledate");
					 String SterileMachineName = rs.getString("SterileMachineName");
					 String SterileRoundNumber = rs.getString("SterileRoundNumber");
					 String timeSdates = rs.getString("timeSdates");
					 String timeEdates = rs.getString("timeEdates");
					 
	
						
					
					 Date_c_s.setText(steriledate);
					 FItem_c_s.setText(SterileMachineName);
					 Round_c_s.setText(SterileRoundNumber);
					 sTime_c_s.setText(timeSdates);
					 eTime_c_s.setText(timeEdates);
					

					}
					
				}catch (Exception e) {
					e.printStackTrace();

					System.out.println("onDisplaySouthCenter : " + e.getMessage());
				}
			}
	public void closeWindow(String StrWindow) throws Exception {
		((Window) getFellow(StrWindow)).setMode("embedded");
		((Window) getFellow(StrWindow)).setVisible(false);
		((Window) getFellow(StrWindow)).setFocus(false);
		
		((Timer) getFellow("Time")).stop();
	}
}
