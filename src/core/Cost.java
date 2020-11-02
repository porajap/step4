package core;

import general.DateTime;
import general.Number;

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

public class Cost extends Window{
	String xUser;
	private Session session = Sessions.getCurrent();

	private String xUsername = "root";
	private String xPassword = "A$192dijd";
	Decimalbox W01;
	Decimalbox W02;
	Decimalbox W03;
	
	Decimalbox xB01;
	Decimalbox xB02;
	Decimalbox xB03;
	Decimalbox xB04;
	Decimalbox xB05;
	Decimalbox xB06;
	Decimalbox xB07;
	Decimalbox xB08;

	Textbox xC97;
	Textbox xC98;
	Textbox xC99;
	
	Decimalbox xS01;
	Decimalbox xS02;
	Decimalbox xS03;
	Decimalbox xS04;
	Decimalbox xS05;
	Decimalbox xS06;

	Div D01;
	Div D02;
	Div D03;
	int[] id = new int[10];
	
	private static final long serialVersionUID = 7394394042153638109L;
	
	public void onCreate() throws Exception {
		if (session.getAttribute("User") == null) {
			Executions.sendRedirect("/CostLogin.zul");
		} else {
			xUser = (String)session.getAttribute("User");
        }
		W01 = ((Decimalbox) getFellow("W01"));
		W02 = ((Decimalbox) getFellow("W02"));
		W03 = ((Decimalbox) getFellow("W03"));
		
		xB01 = ((Decimalbox) getFellow("xB01"));
		xB02 = ((Decimalbox) getFellow("xB02"));
		xB03 = ((Decimalbox) getFellow("xB03"));
		xB04 = ((Decimalbox) getFellow("xB04"));
		xB05 = ((Decimalbox) getFellow("xB05"));
		xB06 = ((Decimalbox) getFellow("xB06"));
		xB07 = ((Decimalbox) getFellow("xB07"));
		xB08 = ((Decimalbox) getFellow("xB08"));

		xC97 = ((Textbox) getFellow("xC97"));
		xC98 = ((Textbox) getFellow("xC98"));
		xC99 = ((Textbox) getFellow("xC99"));
		
		D01 = ((Div) getFellow("D01"));
		D02 = ((Div) getFellow("D02"));
		D03 = ((Div) getFellow("D03"));
		
		D01.setVisible(true);
		D02.setVisible(false);
		D03.setVisible(false);
		
		xS01 = ((Decimalbox) getFellow("xS01"));
		xS02 = ((Decimalbox) getFellow("xS02"));
		xS03 = ((Decimalbox) getFellow("xS03"));
		xS04 = ((Decimalbox) getFellow("xS04"));
		xS05 = ((Decimalbox) getFellow("xS05"));
		xS06 = ((Decimalbox) getFellow("xS06"));
	}
	
	public void onSetting() throws Exception{
		D01.setVisible(false);
		D02.setVisible(true);
		onDisplay();
	}
	
	public void onSave(){
		
		try {
			new OperationData(
					xUsername,
					xPassword,
			 		"Update",
					"cost", 
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
					"cost", 
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
					"cost", 
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
					"cost", 
					new String[][] {
					  	{"Qty", "'" + xS01.getValue() + "'" },
					},
					new String[][] {
						{"id", "'" + id[3] + "'" }
					}
				);
			
			new OperationData(
					xUsername,
					xPassword,
			 		"Update",
					"cost", 
					new String[][] {
					  	{"Qty", "'" + xS02.getValue() + "'" },
					},
					new String[][] {
						{"id", "'" + id[4] + "'" }
					}
				);
			new OperationData(
					xUsername,
					xPassword,
			 		"Update",
					"cost", 
					new String[][] {
					  	{"Qty", "'" + xS03.getValue() + "'" },
					},
					new String[][] {
						{"id", "'" + id[5] + "'" }
					}
				);
			new OperationData(
					xUsername,
					xPassword,
			 		"Update",
					"cost", 
					new String[][] {
					  	{"Qty", "'" + xS04.getValue() + "'" },
					},
					new String[][] {
						{"id", "'" + id[6] + "'" }
					}
				);
			new OperationData(
					xUsername,
					xPassword,
			 		"Update",
					"cost", 
					new String[][] {
					  	{"Qty", "'" + xS05.getValue() + "'" },
					},
					new String[][] {
						{"id", "'" + id[7] + "'" }
					}
				);
			new OperationData(
					xUsername,
					xPassword,
			 		"Update",
					"cost", 
					new String[][] {
					  	{"Qty", "'" + xS06.getValue() + "'" },
					},
					new String[][] {
						{"id", "'" + id[8] + "'" }
					}
				);
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
		D01.setVisible(true);
		D02.setVisible(false);
		D03.setVisible(false);
	}
	
	public void onBack(){
		D01.setVisible(true);
		D02.setVisible(false);
		D03.setVisible(false);
	}
	
	public void onClr(){
		xB01.setText("");
		xB02.setText("");
		xB03.setText("");
		xB04.setText("");
		xB05.setText("");
		xB06.setText("");
	
		xC97.setText("");
		xC98.setText("");
		xC99.setText("");
		
	}
	
	public void onMenu() throws Exception{
		Executions.sendRedirect("/CostMenu.zul");
	}
	
	public void onCal() throws Exception{
		ResultSet rs = null;
        SqlSelection sqlsel = new SqlSelection();
        sqlsel.uName = xUsername;
        sqlsel.Pwd = xPassword;
        double[] Qty = new double[10];
        double B01 = 0;
        double B02 = 0;
        double B03 = 0;
        double B04 = 0;
        
        double B99 = 0;
        
        double BB01 = 0;
        double BB02 = 0;
        double BB03 = 0;
        double BB04 = 0;
        		
        double BB99 = 0;
        
        try{ B01 = Number.unCommaReturnDouble( xB01.getValue()+"" );}catch(Exception e){};
        try{ B02 = Number.unCommaReturnDouble( xB02.getValue()+"" );}catch(Exception e){};
        try{ B03 = Number.unCommaReturnDouble( xB03.getValue()+"" );}catch(Exception e){};
        try{ B04 = Number.unCommaReturnDouble( xB04.getValue()+"" );}catch(Exception e){};
        try{ BB01 = Number.unCommaReturnDouble( xB05.getValue()+"" );}catch(Exception e){};
        try{ BB02 = Number.unCommaReturnDouble( xB06.getValue()+"" );}catch(Exception e){};
        try{ BB03 = Number.unCommaReturnDouble( xB07.getValue()+"" );}catch(Exception e){};
        try{ BB04 = Number.unCommaReturnDouble( xB08.getValue()+"" );}catch(Exception e){};

        double C01 = 0;
        double C02 = 0;
        double C03 = 0;
        
        double CC01 = 0;
        double CC02 = 0;
        double CC03 = 0;
        
        double S01 = 0;
        double S02 = 0;
        double S03 = 0;
        
        double SS01 = 0;
        double SS02 = 0;
        double SS03 = 0;
        
        D01.setVisible(false);
		D02.setVisible(false);
		D03.setVisible(true);
		
		try{	
			
			if(B01>0)B99 = B02/(B01*1000);
			if(BB01>0)BB99 = BB02/(BB01*1000);
			
			String xSql = "SELECT cost.id,cost.xName,cost.Qty FROM cost WHERE username = '"+xUser+"' ORDER BY cost.id ASC";
			rs = sqlsel.getReSultSQL( xSql );
			int n=0;
			while(rs.next()){
				Qty[n] = rs.getDouble("Qty");
				n++;
			}

			if( (B01>0) &&
				(B02>0) &&
				(B03>0) &&
				(B04>0)){
				C01 = (B03*B99);
				C02 = Qty[0]*(B04/60)*Qty[1];
				C03 = C01 + C02;
			}
			
			if( (BB01>0) &&
				(BB02>0) &&
				(BB03>0) &&
				(BB04>0)){
				CC01 = (BB03*BB99);
				CC02 = Qty[0]*(BB04/60)*Qty[1];
				CC03 = CC01 + CC02;
			}
			
			S01 = (Qty[4]*Qty[3]);
			S02 = Qty[0]*(Qty[5]/60)*Qty[1];
			S03 = S01 + S02;
			
			SS01 = (Qty[7]*Qty[6]);
			SS02 = Qty[0]*(Qty[8]/60)*Qty[1];
			SS03 = SS01 + SS02;

			xC97.setText( Number.addComma2d( (C03+CC03)-(S03+SS03)+"" ));
			xC98.setText( Number.addComma2d( (((C03+CC03)-(S03+SS03))*Qty[2])*30+"" ));
			xC99.setText( Number.addComma2d( ((((C03+CC03)-(S03+SS03))*Qty[2])*30)*12+"" ));
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			sqlsel.closeConnection();

            if (rs != null) {
                rs.close();
            }
		}
	}

	private void onDisplay() throws Exception{
		ResultSet rs = null;
        SqlSelection sqlsel = new SqlSelection();
        sqlsel.uName = xUsername;
        sqlsel.Pwd = xPassword;
        double[] Qty = new double[10];
 
		try{	

			String xSql = "SELECT cost.id,cost.xName,cost.Qty FROM cost WHERE username = '"+xUser+"' ORDER BY cost.id ASC";
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
			xS01.setText( Qty[3]+"" );
			xS02.setText( Qty[4]+"" );
			xS03.setText( Qty[5]+"" );
			xS04.setText( Qty[6]+"" );
			xS05.setText( Qty[7]+"" );
			xS06.setText( Qty[8]+"" );
			
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
