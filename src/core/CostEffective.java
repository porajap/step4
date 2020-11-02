package core;

import java.sql.ResultSet;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import connect.SqlSelection;
import general.Number;

public class CostEffective extends Window{
	public String SessionUserCode;

	private String xUsername = "root";
	private String xPassword = "A$192dijd";
	Decimalbox xB03;
	Decimalbox xB04;
	Decimalbox xB05;
	Decimalbox xB06;
	Decimalbox xB07;
	Decimalbox xB10;
	Decimalbox xB11;
	Decimalbox xB12;
	Decimalbox xB13;
	
	Textbox xC01;
	Textbox xC02;
	Textbox xC03;
	Textbox xC04;
	Textbox xC05;

	Div D01;
	Div D03;
	int[] id = new int[8];
	
	private static final long serialVersionUID = 7394394042153638109L;
	
	public void onCreate() throws Exception {
		xB03 = ((Decimalbox) getFellow("xB01"));
		xB04 = ((Decimalbox) getFellow("xB02"));
		xB05 = ((Decimalbox) getFellow("xB03"));
		xB06 = ((Decimalbox) getFellow("xB04"));
		xB07 = ((Decimalbox) getFellow("xB05"));
		xB10 = ((Decimalbox) getFellow("xB06"));
		xB11 = ((Decimalbox) getFellow("xB07"));
		xB12 = ((Decimalbox) getFellow("xB08"));
		xB13 = ((Decimalbox) getFellow("xB09"));
		
		xC01 = ((Textbox) getFellow("xC01"));
		xC02 = ((Textbox) getFellow("xC02"));
		xC03 = ((Textbox) getFellow("xC03"));
		xC04 = ((Textbox) getFellow("xC04"));
		xC05 = ((Textbox) getFellow("xC05"));

		D01 = ((Div) getFellow("D01"));
		D03 = ((Div) getFellow("D03"));
		D01.setVisible(true);
		D03.setVisible(false);

		
	}
	
	public void onSetting() throws Exception{
		D01.setVisible(false);
		onDisplay();
	}
	
	public void onBack(){
		D01.setVisible(true);
		D03.setVisible(false);
	}
	
	public void onClr(){
		xB03.setText("");
		xB04.setText("");
		xB05.setText("");
		xB06.setText("");
		xB07.setText("");
		xB10.setText("");
		xB11.setText("");
		xB12.setText("");
		xB13.setText("");
		
		xC01.setText("");
		xC02.setText("");
		xC03.setText("");
		xC04.setText("");
		xC05.setText("");

	}
	
	public void onMenu() throws Exception{
		Executions.sendRedirect("/CostMenu.zul");
	}
	
	public void onCal() throws Exception{
		ResultSet rs = null;
        SqlSelection sqlsel = new SqlSelection();
        sqlsel.uName = xUsername;
        sqlsel.Pwd = xPassword;
        double B3 = 0;
        double B4 = 0;
        double B5 = 0;
        double B6 = 0;
        double B7 = 0;
        double B10 = 0;
        double B11 = 0;
        double B12 = 0;
        double B13 = 0;
        
        		
        try{ B3 = Number.unCommaReturnDouble( xB03.getValue()+"" );}catch(Exception e){};
        try{ B4 = Number.unCommaReturnDouble( xB04.getValue()+"" );}catch(Exception e){};
        try{ B5 = Number.unCommaReturnDouble( xB05.getValue()+"" );}catch(Exception e){};
        try{ B6 = Number.unCommaReturnDouble( xB06.getValue()+"" );}catch(Exception e){};
        try{ B7 = Number.unCommaReturnDouble( xB07.getValue()+"" );}catch(Exception e){};
        try{ B10 = Number.unCommaReturnDouble( xB10.getValue()+"" );}catch(Exception e){};
        try{ B11 = Number.unCommaReturnDouble( xB11.getValue()+"" );}catch(Exception e){};
        try{ B12 = Number.unCommaReturnDouble( xB12.getValue()+"" );}catch(Exception e){};
        try{ B13 = Number.unCommaReturnDouble( xB13.getValue()+"" );}catch(Exception e){};
        
        double C01 = 0;
        double C02 = 0;
        double C03 = 0;
        double C04 = 0;
        double C05 = 0;
        
        D01.setVisible(false);
		D03.setVisible(true);
		
		try{	
			if( ((B5+B6) > 0) && (B10 > 0) && ((B12+B13) > 0) ){
				C01 = (((B4*B5)/(B5+B6))*B7)-(((B3*B7)/B10)*((B11*B12)/(B12+B13)));
				C02 = C01*12;
				C03 = C02*3;
				C04 = C02*5;
				C05 =((((B4*B5)/(B5+B6))*B7)-(((B3*B7)/B10)*((B11*B12)/(B12+B13))))/(((B4*B5)/(B5+B6))*B7);
			}
			xC01.setText( Number.addComma0d( C01+"" ));
			xC02.setText( Number.addComma0d( C02+"" ));
			xC03.setText( Number.addComma0d( C03+"" ));
			xC04.setText( Number.addComma0d( C04+"" ));
			xC05.setText( Number.addComma2d( (C05*100)+"" ));

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
        double[] Qty = new double[8];
 
		try{	

			String xSql = "SELECT cost.id,cost.xName,cost.Qty FROM cost ORDER BY cost.id ASC";
			rs = sqlsel.getReSultSQL( xSql );
			int n=0;
			while(rs.next()){
				id[n] = rs.getInt("id");
				Qty[n] = rs.getDouble("Qty");
				n++;
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
}
