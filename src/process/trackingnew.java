package process;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.zkoss.mesg.Messages;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Window;

import connect.DBConn;
import connect.SqlSelection;

public class trackingnew extends Window{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6365073630321342768L;
	
	ResultSet rs = null;
	ResultSet rs2 = null;
	ResultSet rs3 = null;
	private Connection conn = null;
	private Statement stmt = null;
	private Statement stmt2 = null;
	public Button btnSearch;
	
	private Tree Treetracking;
	private Panel showStep;
	private Panel showDonut;
	private boolean B_IsCreate = true;
	private Session session = Sessions.getCurrent();
	private String xUsername = null;
	private String xPassword = null;
	private Textbox TextboxSearchtracking;
	
	public Textbox txtLastTime;
	public Textbox txtLastMins;
	public Textbox txtTimeDetail;
	
	private String Search = null;
	public String 	S_UserId,
					S_DeptId,
					S_UserName,
					S_IsAdmin,
					S_EmpCode,
					S_DepName,
					B_ID,
					S_DB,
					i_itemcode;

	public Integer IsStatus;

	public void onCreate() throws Exception{
		init();		
		bySession();
		onDisplay();
		SearchData();
	}
	

	
	private void init()throws SQLException {
		SqlSelection sql = new SqlSelection();
		sql.uName = xUsername;
		sql.Pwd = xPassword;
		DBConn objConnection = new DBConn();
		conn = objConnection.getConnection("root", "A$192dijd");
		stmt = conn.createStatement();
		stmt2 = conn.createStatement();
		showStep = ((Panel) getFellow("showStep"));
		showDonut = ((Panel) getFellow("showDonut"));
		TextboxSearchtracking = ((Textbox) getFellow("TextboxSearchtracking"));
		Treetracking = ((Tree) getFellow("Treetracking"));
		btnSearch = ((Button) getFellow("btnSearch"));
		txtLastTime = ((Textbox) getFellow("txtLastTime"));
		txtLastMins = ((Textbox) getFellow("txtLastMins"));
		txtTimeDetail =((Textbox) getFellow("txtTimeDetail"));
		
		
		txtLastTime.setVisible(false);
		txtLastMins.setVisible(false);
		txtTimeDetail.setVisible(false);
	}
	
	private void bySession()throws Exception{
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
				IsStatus = (Integer)session.getAttribute("IsStatus");
				
	        }
			
			B_IsCreate = false;
		}
		}catch (Exception e) {
//			Messagebox.show("ERROR bySessionTracking : " + e.getMessage());
//			System.out.println("ERROR bySession : " + e.getMessage());
		}
	}
	
	public String getSql()throws Exception {
		
		try {
		
			Search = TextboxSearchtracking.getText();
		 String Sql = "SELECT " + 
		 		"            item.itemcode, " + 
		 		"            item.itemname, " + 
		 		"            COUNT( itemstock.UsageCode ) AS qty, " + 
		 		"            units.UnitName " + 
		 		"          FROM " + 
		 		"            item " + 
		 		"            INNER JOIN itemstock ON item.itemcode = itemstock.ItemCode " + 
		 		"            INNER JOIN units ON item.UnitID = units.ID " + 
		 		"          WHERE " + 
		 		"            itemstock.IsTag = '1'  " + 
		 		"            AND itemstock.DepID = '"+S_DeptId+"' "+ 
		 		"            AND itemstock.B_ID = '"+B_ID+"'  " + 
		 		"            AND itemstock.IsStatus != '5' " + 
		 		"            AND NOT item.NoWash = 1 " + 
		 		"            AND NOT item.NoWashType = 2 " ;
//		 		"			AND item.itemcode = '"+i_itemcode+"' ";
		 		
				if(!Search.equals("")) {
					Sql += "AND ((item.itemname LIKE  '%"+Search.replace(" ", "%")+"%') ";
				}
				if(!Search.equals("")) {
					Sql +=		"OR (itemstock.UsageCode LIKE  '%"+Search.replace(" ", "%")+"%')) ";
				}

		 		Sql += "   GROUP BY " + 
		 		"            item.itemcode  " + 
		 		"          ORDER BY " + 
		 		"            item.itemname ASC ";
		 
		 		

//		 		System.out.println("getSql : " +Sql);
		 		return Sql;
		 		
		}catch (Exception e) {
//			Messagebox.show("ERROR getSqlTracking: " + e.getMessage());
//			System.out.println("ERROR getSql : " + e.getMessage());
		}
		return null;
	}
	
	public void onDisplay()throws Exception {
		try {
			
			rs = stmt.executeQuery(getSql());
			
			Treetracking.getChildren().clear();
			
			Treechildren treeChild = new Treechildren();
			
			while(rs.next()) {
				
				Treeitem treeItem = new Treeitem();
	        	Treerow treeRow = new Treerow();
	           	Treecell treecell = new Treecell();
	           	
	                treeRow.appendChild(treecell);

				treeRow.appendChild(new Treecell(rs.getString("itemname")));
				treeRow.appendChild(new Treecell(rs.getString("qty")));
				treeRow.appendChild(new Treecell(rs.getString("UnitName")));
				treeItem.appendChild(treeRow);
                treeChild.appendChild(treeItem);
                
                Treetracking.appendChild(treeChild);

                
		 		
	 			 String Sql2 = "SELECT " +
	 			         " item.itemcode, "+
	 			         " itemstock.IsStatus, "+
	 			        " item.itemname, "+
	 			         " itemstock.UsageCode "+
	 			       " FROM "+
	 			         " itemstock "+
	 			         " INNER JOIN item ON item.itemcode = itemstock.ItemCode "+
	 			       " WHERE "+
	 			         " item.itemcode = '"+rs.getString("itemcode")+"' "+
	 			         " AND	itemstock.IsTag = '1' "+
	 			         " AND itemstock.DepID = '30' "+
	 			         " AND itemstock.B_ID = '"+B_ID+"' "+
	 			         " AND NOT item.NoWash = 1 "+
	 			         " AND NOT item.NoWashType = 2 "+
	 			         " AND itemstock.IsStatus != '5' " ;
	 			 
	 			        if(!Search.equals("")) {
	 						Sql2 += "AND (item.itemname LIKE  '%"+Search.replace(" ", "%")+"%') ";
	 			        }
	 			       if(!Search.equals("")) {			
	 						Sql2 +=	"OR (itemstock.UsageCode LIKE  '%"+Search.replace(" ", "%")+"%') ";
	 					}
	 			        
	 			       Sql2 += " ORDER BY  itemstock.UsageCode ASC " ; 
	 			 
                rs2 = stmt2.executeQuery(Sql2);
                Treechildren treeChild2 = new Treechildren();
                while(rs2.next()) {
                	Treeitem treeItem2 = new Treeitem();
                	treeItem.setOpen(false);

    	        	Treerow treeRow2 = new Treerow();
    	           	Treecell treecell2 = new Treecell();
    	           	treeRow2.appendChild(new Treecell(""));
    	           	treecell2.setLabel(rs2.getString("UsageCode"));
    	           	
                    treeRow2.appendChild(treecell2);
                    treeRow2.appendChild(Status(rs2.getInt("IsStatus"),rs2.getString("itemcode")));
                    treeRow2.appendChild(new Treecell(""));
                    
                    treeItem2.appendChild(treeRow2);
                    treeChild2.appendChild(treeItem2);
                    treeItem.appendChild(treeChild2);
                    
                }

			}
		}catch (Exception e) {
			e.printStackTrace();
//			System.out.println("ERROR onDisplay : " + e.getMessage());
//			Messagebox.show("ERROR onDisplayTracking : " + e.getMessage());
//			
		}
	}
	
	
	private Treecell Status(int IsStatus,String itemcode)throws Exception{
		
		Treecell tcc = new Treecell();
		
		Button btnStatus = new Button();
		btnStatus.setWidth("80px");
		btnStatus.setHeight("40px");
		btnStatus.setLabel("สถานะ"
				); 	
		btnStatus.setStyle("font-size : 15px; text-align: center; background: #28a745; font-family: myFirstFont;");
		btnStatus.addEventListener("onClick", new EventListener<Event>() {
			public void onEvent(Event event) throws Exception{
				try {	
//					Panel panel = new Panel();
//					showStep.getPanelchildren();
//					System.out.println("11111111111111111111");
					Panelchildren pn = new Panelchildren();	
					pn.setStyle("align: left");
					Panelchildren pnR = new Panelchildren();	
					pnR.setStyle("align: right");
					
					showDonut.getChildren().clear();
					showStep.getChildren().clear();
					
					showDonut.setWidth("100%");
					showDonut.setHeight("100%");
					if(IsStatus==0) {		
						Image img = new Image();
						img.setWidth("100%");
						img.setHeight("650px");
						img.setStyle("align:center");
						img.setSrc("/img/pic/step1.png");

						txtLastTime.setVisible(true);
						txtLastTime.setText("เวลาที่เหลือ  "
								);
						
						txtLastMins.setVisible(true);
						txtLastMins.setText(" 0 : 00 : 00 ");
						
						txtTimeDetail.setVisible(true);
						txtTimeDetail.setText(" ( HH : MM : SS ) ");

						pn.appendChild(img);
						
						pnR.appendChild(txtLastTime);
						pnR.appendChild(txtLastMins);
						pnR.appendChild(txtTimeDetail);
						
						}
					
						else if(IsStatus==1) {
							Image img = new Image();
							img.setWidth("100%");
							img.setHeight("650px");
							img.setStyle("align:center");
							img.setSrc("/img/pic/step2.png");
							pn.appendChild(img);
							}
					
							else if(IsStatus==1) {
								Image img = new Image();
								img.setWidth("100%");
								img.setHeight("650px");
								img.setStyle("align:center");
								img.setSrc("/img/pic/step3.png");
								pn.appendChild(img);
								}
					
								else if(IsStatus==2) {
									Image img = new Image();
									img.setWidth("100%");
									img.setHeight("650px");
									img.setStyle("align:center");
									img.setSrc("/img/pic/step4.png");
									pn.appendChild(img);
									}
					 
									else if(IsStatus==3) {
										Image img = new Image();
										img.setWidth("100%");
										img.setHeight("650px");
										img.setStyle("align:center");
										img.setSrc("/img/pic/step5.png");
										pn.appendChild(img);
										}
					
										else if(IsStatus==4) {
											Image img = new Image();
											img.setWidth("100%");
											img.setHeight("650px");
											img.setStyle("align:center");
											img.setSrc("/img/pic/step6.png");
											pn.appendChild(img);
											}
					
											else if(IsStatus==6) {
//												pn.appendChild(new Image("/img/pic/step7.png"));
												}
											else if(IsStatus==7) {
//												pn.appendChild(new Image("/img/pic/step7.png"));
												}
					
					showStep.appendChild(pn);
					showDonut.appendChild(pnR);
//					onStep(status,itemcode);
				}catch (Exception ex) {
//					System.out.println("ERROR Status" + ex.getMessage() );
//					Messagebox.show("ERROR Status : " + ex.getMessage());
					ex.printStackTrace();
				}
			}
		});
		tcc.appendChild(btnStatus);
		return tcc;
		
	}
	

	
	protected Component img(String string) {
		// TODO Auto-generated method stub
		return null;
	}



	public void SearchData() throws Exception {
		
			onDisplay();
	}
	
	
}
