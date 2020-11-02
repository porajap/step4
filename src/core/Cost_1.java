package core;

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

public class Cost_1 extends Window{
	String xUser;
	String Dosing;
	private Session session = Sessions.getCurrent();

	private String xUsername = "root";
	private String xPassword = "A$192dijd";
	Decimalbox xB01;
	Decimalbox xB02;
	Decimalbox xB03;
	Decimalbox xB04;

	Textbox xC97;
	Textbox xC98;
	Textbox xC99;
	
	Decimalbox xS01;
	Decimalbox xS02;
	Decimalbox xS03;
	Decimalbox xS04;

	Div D01;
	Div D02;
	Div D03;
	int[] id = new int[100];
	
	private static final long serialVersionUID = 7394394042153638109L;
	
	public void onCreate() throws Exception {
		if (session.getAttribute("User") == null) {
			Executions.sendRedirect("/CostLogin.zul");
		} else {
			xUser = (String)session.getAttribute("User");
			Dosing = (String)session.getAttribute("Dosing");
        }
		xB01 = ((Decimalbox) getFellow("xB01"));
		xB02 = ((Decimalbox) getFellow("xB02"));
		xB03 = ((Decimalbox) getFellow("xB03"));
		xB04 = ((Decimalbox) getFellow("xB04"));

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
	}
	
	public void onSetting() throws Exception{
		session.setAttribute("Page", "Cost_1.zul");
		Executions.sendRedirect("/CostSettingDosing.zul");
	}
	
	public void onSave(){
		
		try {
			new OperationData(
					xUsername,
					xPassword,
			 		"Update",
					"cost_dosing", 
					new String[][] {
					  	{"Qty", "'" + xS01.getValue() + "'" },
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
					  	{"Qty", "'" + xS02.getValue() + "'" },
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
					  	{"Qty", "'" + xS03.getValue() + "'" },
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
					  	{"Qty", "'" + xS04.getValue() + "'" },
					},
					new String[][] {
						{"id", "'" + id[3] + "'" }
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
        double[] Qty = new double[100];
        double B01 = 0;
        double B02 = 0;
        double B03 = 0;
        double B04 = 0;
        double B99 = 0;

        double S01 = 0;
        double S02 = 0;
        double S03 = 0;
        double S04 = 0;

        double SS01 = 0;
        double SS02 = 0;
        double SS03 = 0;
        double SS04 = 0;
        
        double SSS01 = 0;
        double SSS02 = 0;
        double SSS03 = 0;
        double SSS04 = 0;
        
        double SSSS01 = 0;
        double SSSS02 = 0;
        double SSSS03 = 0;
        double SSSS04 = 0;
 
        double SSSSS01 = 0;
        double SSSSS02 = 0;
        double SSSSS03 = 0;
        double SSSSS04 = 0;
        
        double SumC = 0;
        double SumS = 0;
        
        try{ B01 = Number.unCommaReturnDouble( xB01.getValue()+"" );}catch(Exception e){};
        try{ B02 = Number.unCommaReturnDouble( xB02.getValue()+"" );}catch(Exception e){};
        try{ B03 = Number.unCommaReturnDouble( xB03.getValue()+"" );}catch(Exception e){};
        try{ B04 = Number.unCommaReturnDouble( xB04.getValue()+"" );}catch(Exception e){};

        double C01 = 0;
        double C02 = 0;
        double C03 = 0;
        double C04 = 0;

        D01.setVisible(false);
		D02.setVisible(false);
		D03.setVisible(true);
		
		try{	
			
			if(B01>0)B99 = B02/(B01*1000);
	
			String xSql = "SELECT cost_dosing.id,cost_dosing.xName,cost_dosing.Qty FROM cost_dosing WHERE username = '"+xUser+"' ORDER BY cost_dosing.id ASC";
			rs = sqlsel.getReSultSQL( xSql );
			int n=0;
			while(rs.next()){
				Qty[n] = rs.getDouble("Qty");
				n++;
			}

			if( (B01>0) && (B02>0) && (B03>0) && (B04>0)){
					C01 = (Qty[2]*Qty[3]);
					C02 = B03*B99;
					C03 = Qty[0]*(B04/60)*Qty[1];
					C04 = C01+C02+C03;
					SumC = C04;
			}

			if(Integer.parseInt(Dosing)==1){
				S01 = (Qty[2]*Qty[3]);
				S02 = (Qty[6]*Qty[5]);
				S03 = Qty[0]*(Qty[7]/60)*Qty[1];
				S04 = S01+S02+S03;
				SumS = S04;
			}if(Integer.parseInt(Dosing)==2){
				S01 = (Qty[2]*Qty[3]);
				S02 = (Qty[6]*Qty[5]);
				S03 = Qty[0]*(Qty[7]/60)*Qty[1];
				S04 = S01+S02+S03;
				
				SS01 = (Qty[2]*Qty[3]);
				SS02 = (Qty[9]*Qty[8]);
				SS03 = Qty[0]*(Qty[10]/60)*Qty[1];
				SS04 = SS01+SS02+SS03;
				
				SumS = S04+SS04;
			}if(Integer.parseInt(Dosing)==3){
				S01 = (Qty[2]*Qty[3]);
				S02 = (Qty[6]*Qty[5]);
				S03 = Qty[0]*(Qty[7]/60)*Qty[1];
				S04 = S01+S02+S03;
				
				SS01 = (Qty[2]*Qty[3]);
				SS02 = (Qty[9]*Qty[8]);
				SS03 = Qty[0]*(Qty[10]/60)*Qty[1];
				SS04 = SS01+SS02+SS03;
				
				SSS01 = (Qty[2]*Qty[3]);
				SSS02 = (Qty[12]*Qty[11]);
				SSS03 = Qty[0]*(Qty[13]/60)*Qty[1];
				SSS04 = SSS01+SSS02+SSS03;
				
				SumS = S04+SS04+SSS04;
			}if(Integer.parseInt(Dosing)==4){
				S01 = (Qty[2]*Qty[3]);
				S02 = (Qty[6]*Qty[5]);
				S03 = Qty[0]*(Qty[7]/60)*Qty[1];
				S04 = S01+S02+S03;
				
				SS01 = (Qty[2]*Qty[3]);
				SS02 = (Qty[9]*Qty[8]);
				SS03 = Qty[0]*(Qty[10]/60)*Qty[1];
				SS04 = SS01+SS02+SS03;
				
				SSS01 = (Qty[2]*Qty[3]);
				SSS02 = (Qty[12]*Qty[11]);
				SSS03 = Qty[0]*(Qty[13]/60)*Qty[1];
				SSS04 = SSS01+SSS02+SSS03;
				
				SSSS01 = (Qty[2]*Qty[3]);
				SSSS02 = (Qty[15]*Qty[14]);
				SSSS03 = Qty[0]*(Qty[16]/60)*Qty[1];
				SSSS04 = SSSS01+SSSS02+SSSS03;
				
				SumS = S04+SS04+SSS04+SSSS04;
			}if(Integer.parseInt(Dosing)==5){
				S01 = (Qty[2]*Qty[3]);
				S02 = (Qty[6]*Qty[5]);
				S03 = Qty[0]*(Qty[7]/60)*Qty[1];
				S04 = S01+S02+S03;
				
				SS01 = (Qty[2]*Qty[3]);
				SS02 = (Qty[9]*Qty[8]);
				SS03 = Qty[0]*(Qty[10]/60)*Qty[1];
				SS04 = SS01+SS02+SS03;
				
				SSS01 = (Qty[2]*Qty[3]);
				SSS02 = (Qty[12]*Qty[11]);
				SSS03 = Qty[0]*(Qty[13]/60)*Qty[1];
				SSS04 = SSS01+SSS02+SSS03;
				
				SSSS01 = (Qty[2]*Qty[3]);
				SSSS02 = (Qty[15]*Qty[14]);
				SSSS03 = Qty[0]*(Qty[16]/60)*Qty[1];
				SSSS04 = SSSS01+SSSS02+SSSS03;
				
				SSSSS01 = (Qty[2]*Qty[3]);
				SSSSS02 = (Qty[18]*Qty[17]);
				SSSSS03 = Qty[0]*(Qty[19]/60)*Qty[1];
				SSSSS04 = SSSSS01+SSSSS02+SSSSS03;
				
				SumS = S04+SS04+SSS04+SSSS04+SSSSS04;
			}
			
			xC97.setText( Number.addComma2d( (SumS-SumC)+"" ));
			xC98.setText( Number.addComma2d( ((SumS-SumC)*Qty[4])*30+"" ));
			xC99.setText( Number.addComma2d( (((SumS-SumC)*Qty[4])*30)*12+"" ));
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
