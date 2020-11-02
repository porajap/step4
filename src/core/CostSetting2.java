package core;

import java.sql.ResultSet;
import org.zkoss.zk.ui.ComponentNotFoundException;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import connect.OperationData;
import connect.SqlSelection;

public class CostSetting2 extends Window{
	String xUser;
	String Page;
	private Session session = Sessions.getCurrent();

	private String xUsername = "root";
	private String xPassword = "A$192dijd";
	Decimalbox W01;
	Decimalbox W02;
	Decimalbox W03;
	Decimalbox W04;
	Decimalbox W05;
	
	Decimalbox xS01;
	Decimalbox xS02;
	Decimalbox xS03;
	Decimalbox xS04;
	Decimalbox xS05;
	Decimalbox xS06;
	
	int[] id = new int[100];
	
	private static final long serialVersionUID = 7394394042153638109L;
	
	public void onCreate() throws Exception {
		if (session.getAttribute("User") == null) {
			Executions.sendRedirect("/CostLogin.zul");
		}else {
			xUser = (String)session.getAttribute("User");
			Page = (String)session.getAttribute("Page");
        }
		
		W01 = ((Decimalbox) getFellow("W01"));
		W02 = ((Decimalbox) getFellow("W02"));
		W03 = ((Decimalbox) getFellow("W03"));
		W04 = ((Decimalbox) getFellow("W04"));
		W05 = ((Decimalbox) getFellow("W05"));
		
		xS01 = ((Decimalbox) getFellow("xS01"));
		xS02 = ((Decimalbox) getFellow("xS02"));
		xS03 = ((Decimalbox) getFellow("xS03"));
		xS04 = ((Decimalbox) getFellow("xS04"));
		xS05 = ((Decimalbox) getFellow("xS05"));
		xS06 = ((Decimalbox) getFellow("xS06"));

		onDisplay();
	}
	
	public void onSave(){
		
		try {
			new OperationData(
					xUsername,
					xPassword,
			 		"Update",
					"cost_dosing", 
					new String[][] {
					  	{"Qty", "'" + W01.getValue() + "'" },
					},
					new String[][] {
						{"id", "'" + id[0] + "'" }
					}
				);
			new OperationData(
					xUsername,
					xPassword,
			 		"Update",
					"cost_dosing", 
					new String[][] {
					  	{"Qty", "'" + W02.getValue() + "'" },
					},
					new String[][] {
						{"id", "'" + id[1] + "'" }
					}
				);
			new OperationData(
					xUsername,
					xPassword,
			 		"Update",
					"cost_dosing", 
					new String[][] {
					  	{"Qty", "'" + W03.getValue() + "'" },
					},
					new String[][] {
						{"id", "'" + id[2] + "'" }
					}
				);
			new OperationData(
					xUsername,
					xPassword,
			 		"Update",
					"cost_dosing", 
					new String[][] {
					  	{"Qty", "'" + W04.getValue() + "'" },
					},
					new String[][] {
						{"id", "'" + id[3] + "'" }
					}
				);
			new OperationData(
					xUsername,
					xPassword,
			 		"Update",
					"cost_dosing", 
					new String[][] {
					  	{"Qty", "'" + W05.getValue() + "'" },
					},
					new String[][] {
						{"id", "'" + id[4] + "'" }
					}
				);
			new OperationData(
					xUsername,
					xPassword,
			 		"Update",
					"cost_dosing", 
					new String[][] {
					  	{"Qty", "'" + xS01.getValue() + "'" },
					},
					new String[][] {
						{"id", "'" + id[5] + "'" }
					}
				);
			
			new OperationData(
					xUsername,
					xPassword,
			 		"Update",
					"cost_dosing", 
					new String[][] {
					  	{"Qty", "'" + xS02.getValue() + "'" },
					},
					new String[][] {
						{"id", "'" + id[6] + "'" }
					}
				);
			new OperationData(
					xUsername,
					xPassword,
			 		"Update",
					"cost_dosing", 
					new String[][] {
					  	{"Qty", "'" + xS03.getValue() + "'" },
					},
					new String[][] {
						{"id", "'" + id[7] + "'" }
					}
				);
			new OperationData(
					xUsername,
					xPassword,
			 		"Update",
					"cost_dosing", 
					new String[][] {
					  	{"Qty", "'" + xS04.getValue() + "'" },
					},
					new String[][] {
						{"id", "'" + id[8] + "'" }
					}
				);
			new OperationData(
					xUsername,
					xPassword,
			 		"Update",
					"cost_dosing", 
					new String[][] {
					  	{"Qty", "'" + xS05.getValue() + "'" },
					},
					new String[][] {
						{"id", "'" + id[9] + "'" }
					}
				);
			new OperationData(
					xUsername,
					xPassword,
			 		"Update",
					"cost_dosing", 
					new String[][] {
					  	{"Qty", "'" + xS06.getValue() + "'" },
					},
					new String[][] {
						{"id", "'" + id[10] + "'" }
					}
				);
			
			Executions.sendRedirect("/"+Page);
		} catch (WrongValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ComponentNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void onDisplay() throws Exception{
		ResultSet rs = null;
        SqlSelection sqlsel = new SqlSelection();
        sqlsel.uName = xUsername;
        sqlsel.Pwd = xPassword;
        double[] Qty = new double[100];
 
		try{	

			String xSql = "SELECT cost_dosing.id,cost_dosing.xName,cost_dosing.Qty FROM cost_dosing WHERE username = '"+xUser+"' ORDER BY cost_dosing.id ASC";
			rs = sqlsel.getReSultSQL( xSql );
			int n=0;
			while(rs.next()){
				id[n] = rs.getInt("id");
				Qty[n] = rs.getDouble("Qty");
				n++;
			}

			W01.setText( Qty[0]+"" );
			W02.setText( Qty[1]+"" );
			W03.setText( Qty[2]+"" );
			W04.setText( Qty[3]+"" );
			W05.setText( Qty[4]+"" );
			xS01.setText( Qty[5]+"" );
			xS02.setText( Qty[6]+"" );
			xS03.setText( Qty[7]+"" );
			xS04.setText( Qty[8]+"" );
			xS05.setText( Qty[9]+"" );
			xS06.setText( Qty[10]+"" );
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			sqlsel.closeConnection();

            if (rs != null) {
                rs.close();
            }
		}
	}
}
