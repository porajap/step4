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

public class Cost_4 extends Window{
	String xUser;
	String Dosing;
	private Session session = Sessions.getCurrent();

	private String xUsername = "root";
	private String xPassword = "A$192dijd";
	Decimalbox xB01;
	Decimalbox xB02;
	Decimalbox xB03;
	Decimalbox xB04;
	
	Decimalbox xB05;
	Decimalbox xB06;
	Decimalbox xB07;
	Decimalbox xB08;
	
	Decimalbox xB09;
	Decimalbox xB10;
	Decimalbox xB11;
	Decimalbox xB12;
	
	Decimalbox xB13;
	Decimalbox xB14;
	Decimalbox xB15;
	Decimalbox xB16;
	
	Decimalbox xB17;
	Decimalbox xB18;
	Decimalbox xB19;
	Decimalbox xB20;

	
	Textbox xC97;
	Textbox xC98;
	Textbox xC99;
	
	Decimalbox xS01;
	Decimalbox xS02;
	Decimalbox xS03;
	Decimalbox xS04;

	Decimalbox xS05;
	Decimalbox xS06;
	Decimalbox xS07;
	Decimalbox xS08;

	Decimalbox xS09;
	Decimalbox xS10;
	Decimalbox xS11;
	Decimalbox xS12;

	Decimalbox xS13;
	Decimalbox xS14;
	Decimalbox xS15;
	Decimalbox xS16;

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
		
		xB05 = ((Decimalbox) getFellow("xB05"));
		xB06 = ((Decimalbox) getFellow("xB06"));
		xB07 = ((Decimalbox) getFellow("xB07"));
		xB08 = ((Decimalbox) getFellow("xB08"));
		
		xB09 = ((Decimalbox) getFellow("xB09"));
		xB10 = ((Decimalbox) getFellow("xB10"));
		xB11 = ((Decimalbox) getFellow("xB11"));
		xB12 = ((Decimalbox) getFellow("xB12"));
		
		xB13 = ((Decimalbox) getFellow("xB13"));
		xB14 = ((Decimalbox) getFellow("xB14"));
		xB15 = ((Decimalbox) getFellow("xB15"));
		xB16 = ((Decimalbox) getFellow("xB16"));
		
		
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
		xS07 = ((Decimalbox) getFellow("xS07"));
		xS08 = ((Decimalbox) getFellow("xS08"));
		xS09 = ((Decimalbox) getFellow("xS09"));
		xS10 = ((Decimalbox) getFellow("xS10"));
		xS11 = ((Decimalbox) getFellow("xS11"));
		xS12 = ((Decimalbox) getFellow("xS12"));
		xS13 = ((Decimalbox) getFellow("xS13"));
		xS14 = ((Decimalbox) getFellow("xS14"));
		xS15 = ((Decimalbox) getFellow("xS15"));
		xS16 = ((Decimalbox) getFellow("xS16"));
	}
	
	public void onSetting() throws Exception{
		session.setAttribute("Page", "Cost_4.zul");
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
			
			new OperationData(
					xUsername,
					xPassword,
			 		"Update",
					"cost_dosing", 
					new String[][] {
					  	{"Qty", "'" + xS05.getValue() + "'" },
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
					  	{"Qty", "'" + xS06.getValue() + "'" },
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
					  	{"Qty", "'" + xS07.getValue() + "'" },
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
					  	{"Qty", "'" + xS08.getValue() + "'" },
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
					  	{"Qty", "'" + xS09.getValue() + "'" },
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
					  	{"Qty", "'" + xS10.getValue() + "'" },
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
					  	{"Qty", "'" + xS11.getValue() + "'" },
					},
					new String[][] {
						{"id", "'" + id[10] + "'" }
					}
				);
			new OperationData(
					xUsername,
					xPassword,
			 		"Update",
					"cost_dosing", 
					new String[][] {
					  	{"Qty", "'" + xS12.getValue() + "'" },
					},
					new String[][] {
						{"id", "'" + id[11] + "'" }
					}
				);
			new OperationData(
					xUsername,
					xPassword,
			 		"Update",
					"cost_dosing", 
					new String[][] {
					  	{"Qty", "'" + xS13.getValue() + "'" },
					},
					new String[][] {
						{"id", "'" + id[12] + "'" }
					}
				);
			new OperationData(
					xUsername,
					xPassword,
			 		"Update",
					"cost_dosing", 
					new String[][] {
					  	{"Qty", "'" + xS14.getValue() + "'" },
					},
					new String[][] {
						{"id", "'" + id[13] + "'" }
					}
				);
			new OperationData(
					xUsername,
					xPassword,
			 		"Update",
					"cost_dosing", 
					new String[][] {
					  	{"Qty", "'" + xS15.getValue() + "'" },
					},
					new String[][] {
						{"id", "'" + id[14] + "'" }
					}
				);
			new OperationData(
					xUsername,
					xPassword,
			 		"Update",
					"cost_dosing", 
					new String[][] {
					  	{"Qty", "'" + xS16.getValue() + "'" },
					},
					new String[][] {
						{"id", "'" + id[15] + "'" }
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
		xB07.setText("");
		xB08.setText("");
		xB09.setText("");
		xB10.setText("");
		xB11.setText("");
		xB12.setText("");
		xB13.setText("");
		xB14.setText("");
		xB15.setText("");
		xB16.setText("");
		xB17.setText("");
		xB18.setText("");
		xB19.setText("");
		xB20.setText("");

		
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
        
        double BB01 = 0;
        double BB02 = 0;
        double BB03 = 0;
        double BB04 = 0;

        double BB99 = 0;
        
        double BBB01 = 0;
        double BBB02 = 0;
        double BBB03 = 0;
        double BBB04 = 0;

        double BBB99 = 0;
        
        double BBBB01 = 0;
        double BBBB02 = 0;
        double BBBB03 = 0;
        double BBBB04 = 0;
 
        double BBBB99 = 0;
        		
        try{ B01 = Number.unCommaReturnDouble( xB01.getValue()+"" );}catch(Exception e){};
        try{ B02 = Number.unCommaReturnDouble( xB02.getValue()+"" );}catch(Exception e){};
        try{ B03 = Number.unCommaReturnDouble( xB03.getValue()+"" );}catch(Exception e){};
        try{ B04 = Number.unCommaReturnDouble( xB04.getValue()+"" );}catch(Exception e){};

        try{ BB01 = Number.unCommaReturnDouble( xB05.getValue()+"" );}catch(Exception e){};
        try{ BB02 = Number.unCommaReturnDouble( xB06.getValue()+"" );}catch(Exception e){};
        try{ BB03 = Number.unCommaReturnDouble( xB07.getValue()+"" );}catch(Exception e){};
        try{ BB04 = Number.unCommaReturnDouble( xB08.getValue()+"" );}catch(Exception e){};

        try{ BBB01 = Number.unCommaReturnDouble( xB09.getValue()+"" );}catch(Exception e){};
        try{ BBB02 = Number.unCommaReturnDouble( xB10.getValue()+"" );}catch(Exception e){};
        try{ BBB03 = Number.unCommaReturnDouble( xB11.getValue()+"" );}catch(Exception e){};
        try{ BBB04 = Number.unCommaReturnDouble( xB12.getValue()+"" );}catch(Exception e){};

        try{ BBBB01 = Number.unCommaReturnDouble( xB13.getValue()+"" );}catch(Exception e){};
        try{ BBBB02 = Number.unCommaReturnDouble( xB14.getValue()+"" );}catch(Exception e){};
        try{ BBBB03 = Number.unCommaReturnDouble( xB15.getValue()+"" );}catch(Exception e){};
        try{ BBBB04 = Number.unCommaReturnDouble( xB16.getValue()+"" );}catch(Exception e){};

        double C01 = 0;
        double C02 = 0;
        double C03 = 0;
        double C04 = 0;
        
        double CC01 = 0;
        double CC02 = 0;
        double CC03 = 0;
        double CC04 = 0;
        
        double CCC01 = 0;
        double CCC02 = 0;
        double CCC03 = 0;
        double CCC04 = 0;
        
        double CCCC01 = 0;
        double CCCC02 = 0;
        double CCCC03 = 0;
        double CCCC04 = 0;
        
        double SumC = 0;
        
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

        double SumS = 0;
        
        D01.setVisible(false);
		D02.setVisible(false);
		D03.setVisible(true);
		
		try{	
			if(B01>0)B99 = B02/(B01*1000);
			if(BB01>0)BB99 = BB02/(BB01*1000);
			if(BBB01>0)BBB99 = BBB02/(BBB01*1000);
			if(BBBB01>0)BBBB99 = BBBB02/(BBBB01*1000);
			
			String xSql = "SELECT cost_dosing.id,cost_dosing.xName,cost_dosing.Qty FROM cost_dosing WHERE cost_dosing.username = '"+xUser+"' ORDER BY cost_dosing.id ASC";
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
				SumC += C04;
			}
			
			if( (BB01>0) && (BB02>0) && (BB03>0) && (BB04>0)){
				CC01 = (Qty[2]*Qty[3]);
				CC02 = BB03*BB99;
				CC03 = Qty[0]*(BB04/60)*Qty[1];
				CC04 = CC01+CC02+CC03;
				SumC += CC04;
			}
			
			if( (BBB01>0) && (BBB02>0) && (BBB03>0) && (BBB04>0)){
				CCC01 = (Qty[2]*Qty[3]);
				CCC02 = BBB03*BBB99;
				CCC03 = Qty[0]*(BBB04/60)*Qty[1];
				CCC04 = CCC01+CCC02+CCC03;
				SumC += CCC04;
			}
			if( (BBBB01>0) && (BBBB02>0) && (BBBB03>0) && (BBBB04>0)){
				CCCC01 = (Qty[2]*Qty[3]);
				CCCC02 = BBBB03*BBBB99;
				CCCC03 = Qty[0]*(BBBB04/60)*Qty[1];
				CCCC04 = CCCC01+CCCC02+CCCC03;
				SumC += CCCC04;
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
