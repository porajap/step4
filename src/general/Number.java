
/*	
 *  -- ************************************************************
	-- Author		:	Stargreat
	-- Create date	:	18-07-2011
	-- Update date	:	PARADOX
	-- Update By	:   14-03-2012
	-- Description	:	Get And Set Number Format [Version 1.0]
	-- ************************************************************
 */

package general;
import java.sql.ResultSet;
import java.text.*;

import connect.SqlSelection;
public class Number {
	
	public static int unCommaReturnInt(String StrNumber) {
		String StrNo = "";
		
		for(int i=0;i<StrNumber.length();i++)
			if(StrNumber.charAt(i) != ',')
				StrNo += StrNumber.charAt(i);

		return Integer.valueOf(StrNo).intValue();
	} 
	
	public static float unCommaReturnFloat(String StrNumber) {
		String StrNo = "";
		
		for(int i=0;i<StrNumber.length();i++)
			if(StrNumber.charAt(i) != ',')
				StrNo += StrNumber.charAt(i);

		return Float.valueOf(StrNo).floatValue();
	} 
	
	public static double unCommaReturnDouble(String StrNumber) {
		String StrNo = "";
		
		for(int i=0;i<StrNumber.length();i++)
			if(StrNumber.charAt(i) != ',')
				StrNo += StrNumber.charAt(i);

		return Double.valueOf(StrNo).doubleValue();
	}
	
	public static String unCommaReturnString(String StrNumber) {
		String StrNo = "";
		
		if(StrNumber == null || StrNumber.trim().equals(""))
			return "0";
		
		for(int i=0;i<StrNumber.length();i++)
			if(StrNumber.charAt(i) != ',')
				StrNo += StrNumber.charAt(i);

		return StrNo;
	}
	
	public static String unCommaReturnString(String StrNumber, String Return) {
		String StrNo = "";
		
		if(StrNumber == null || StrNumber.trim().equals("") )
			return Return;
		
		for(int i=0;i<StrNumber.length();i++)
			if(StrNumber.charAt(i) != ',')
				StrNo += StrNumber.charAt(i);

		return StrNo;
	}
	
	public static String addComma(double StrNumber,int IntDigit){
		String StrFormat = "";
		if(IntDigit == 1 )
			StrFormat = "###,##0.0";
		else if(IntDigit == 2 )
			StrFormat = "###,##0.00";
		else if(IntDigit == 3 )
			StrFormat = "###,##0.000";
		else if(IntDigit == 4 )
			StrFormat = "###,##0.0000";
		else if(IntDigit == 5 )
			StrFormat = "###,##0.00000";
		else if(IntDigit == 6 )
			StrFormat = "###,##0.000000";
		else
			StrFormat = "###,##0";
		
		try{
			DecimalFormat df = new DecimalFormat(StrFormat);		
			return df.format(StrNumber).toString();
		}catch(Exception e){
			return "0";
		}
	}
	
	public static String addComma0d(double StrNumber){
		try{
			DecimalFormat df = new DecimalFormat("###,##0");		
			return df.format(StrNumber).toString();
		}catch(Exception e){
			return "0";
		}
	}

	
	
	public static String addComma1d(double StrNumber){
		try{
			DecimalFormat df = new DecimalFormat("###,##0.0");		
			return df.format(StrNumber).toString();
		}catch(Exception e){
			return "0";
		}
	}
	
	public static String addComma2d(double StrNumber){
		try{
			DecimalFormat df = new DecimalFormat("###,##0.00");		
			return df.format(StrNumber).toString();
		}catch(Exception e){
			return "0";
		}
	}
	
	public static String addComma3d(double StrNumber){
		try{
			DecimalFormat df = new DecimalFormat("###,##0.000");		
			return df.format(StrNumber).toString();
		}catch(Exception e){
			return "0";
		}
	}
	
	public static String addComma4d(double StrNumber){
		try{
			DecimalFormat df = new DecimalFormat("###,##0.0000");		
			return df.format(StrNumber).toString();
		}catch(Exception e){
			return "0";
		}
	}
	
	public static String addComma0d(String StrNumber){
		try{
			double DoubleNo = Double.valueOf(StrNumber).doubleValue(); 
			DecimalFormat df = new DecimalFormat("###,##0");		
			return df.format(DoubleNo).toString();
		}catch(Exception e){
			return "0";
		}
	}
	
	public static String addComma1d(String StrNumber){
		try{
			double DoubleNo = Double.valueOf(StrNumber).doubleValue(); 
			DecimalFormat df = new DecimalFormat("###,##0.0");		
			return df.format(DoubleNo).toString();
		}catch(Exception e){
			return "0";
		}
	}
	
	public static String addComma2d(String StrNumber){
		try{
			double DoubleNo = Double.valueOf(StrNumber).doubleValue(); 
			DecimalFormat df = new DecimalFormat("###,##0.00");		
			return df.format(DoubleNo).toString();
		}catch(Exception e){
			return "0";
		}
	}
	
	public static String addComma3d(String StrNumber){
		try{
			double DoubleNo = Double.valueOf(StrNumber).doubleValue(); 
			DecimalFormat df = new DecimalFormat("###,##0.000");		
			return df.format(DoubleNo).toString();
		}catch(Exception e){
			return "0";
		}
	}
	
	public static String addComma4d(String StrNumber){
		try{
			double DoubleNo = Double.valueOf(StrNumber).doubleValue(); 
			DecimalFormat df = new DecimalFormat("###,##0.0000");		
			return df.format(DoubleNo).toString();
		}catch(Exception e){
			return "0";
		}
	}
	
	public static String StrMoney(String Mny){
		String Num1 = "";
		String Num2 = "";
		String StrNum = "";
		Num1 = Mny.substring(0, Mny.length()-3).toString();
		Num2 = Mny.substring(Mny.length()-2,Mny.length()).toString();
		int i = 0;
		for(i=0;i<Num1.length();i++ ){
			StrNum += NumToStr(true,i,Num1.length(),Num1.substring(i,i+1),Num1);
		}
		if(Integer.parseInt(Num2)==0){
			StrNum += "ถ้วน";
		}else{
			for(i=0;i<Num2.length();i++ ){
				StrNum += NumToStr(false,i,Num2.length(),Num2.substring(i,i+1),Num2);
			}
		}
		return StrNum;
	}
	
	private static String NumToStr(boolean chk,int Point, int cPoint,String Num,String oNum){
		String Str = "";
			switch( Integer.parseInt( Num ) ){
				case 0: Str = "";break;
				case 1:	Str = "หนึ่ง";	break;
				case 2:Str = "สอง";break;
				case 3:Str = "สาม";break;
				case 4:Str = "สี่";break;
				case 5:Str = "ห้า";break;
				case 6:Str = "หก";break;
				case 7:Str = "เจ็ด";break;
				case 8:Str = "แปด";break;
				case 9:Str = "เก้า";break;
			}
			if(chk){
				Str = CheckPoint1(Point,cPoint,Num,Str,oNum);
			}else{
				Str = CheckPoint2(Point,cPoint,Num,Str,oNum);
			}
		return Str; 
	}
	
	private static String CheckPoint1(int Point,int cPoint,String Num,String xStr,String oNum){
		String nNum = oNum.substring(oNum.length()-2, oNum.length()-1);
		String Str = "";
			switch( cPoint ){
				case 1://หน่วย
					Str = xStr + "บาท";
					break;
				case 2://สิบ
					switch( Point ){
						case 0:	
							if( Num.equals("2") )
								Str = "ยี่สิบ";
							else if(Num.equals("1"))
								Str = xStr + "สิบ"; 
						break;
						case 1: 
							if(Num.equals("1") && !nNum.equals("0"))
								Str = "เอ็ดบาท";
							else
								Str = xStr + "บาท";
						break;
					}
					break;
				case 3://ร้อย
					switch( Point ){
						case 0:	
							if(!Num.equals("0")) Str = xStr + "ร้อย"; 
							break;
						case 1: 
							if( Num.equals("2") )
								Str = "ยี่สิบ";
							else if(!Num.equals("0") ) 
								Str = xStr + "สิบ"; 
							break;
						case 2:  
							if(Num.equals("1") && !nNum.equals("0"))
								Str = "เอ็ดบาท";
							else
								Str = xStr + "บาท";
							break;
					}
					break;
				case 4://พัน
					switch( Point ){
						case 0:	if(!Num.equals("0"))Str = xStr + "พัน"; break;
						case 1: if(!Num.equals("0"))Str = xStr + "ร้อย";break;
						case 2: 
							if( Num.equals("2") )
								Str = "ยี่สิบ";
							else if(!Num.equals("0") ) 
								Str = xStr + "สิบ"; 
							break;
						case 3: 
							if(Num.equals("1") && !nNum.equals("0"))
								Str = "เอ็ดบาท";
							else
								Str = xStr + "บาท";
							break;
					}
					break;
				case 5://หมื่น
					switch( Point ){
						case 0:	if(!Num.equals("0"))Str = xStr + "หมื่น"; break;
						case 1: if(!Num.equals("0"))Str = xStr + "พัน";break;
						case 2: if(!Num.equals("0"))Str = xStr + "ร้อย";break;
						case 3: 
							if( Num.equals("2") )
								Str = "ยี่สิบ";
							else if(!Num.equals("0") ) 
								Str = xStr + "สิบ"; 
							break;
						case 4:  
							if(Num.equals("1") && !nNum.equals("0"))
								Str = "เอ็ดบาท";
							else
								Str = xStr + "บาท";
							break;
					}
					break;
				case 6://แสน
					switch( Point ){
						case 0:	if(!Num.equals("0"))Str = xStr + "แสน"; break;
						case 1: if(!Num.equals("0"))Str = xStr + "หมื่น";break;
						case 2: if(!Num.equals("0"))Str = xStr + "พัน";break;
						case 3: if(!Num.equals("0"))Str = xStr + "ร้อย";break;
						case 4: 
							if( Num.equals("2") )
								Str = "ยี่สิบ";
							else if(!Num.equals("0") ) 
								Str = xStr + "สิบ"; 
							break;
						case 5:  
							if(Num.equals("1") && !nNum.equals("0"))
								Str = "เอ็ดบาท";
							else
								Str = xStr + "บาท";
							break;
					}
					break;
				case 7://ล้าน
					switch( Point ){
						case 0:	if(!Num.equals("0"))Str = xStr + "ล้าน"; break;
						case 1: if(!Num.equals("0"))Str = xStr + "แสน";break;
						case 2: if(!Num.equals("0"))Str = xStr + "หมื่น";break;
						case 3: if(!Num.equals("0"))Str = xStr + "พัน";break;
						case 4: if(!Num.equals("0"))Str = xStr + "ร้อย";break;
						case 5: 
							if( Num.equals("2") )
								Str = "ยี่สิบ";
							else if(!Num.equals("0") ) 
								Str = xStr + "สิบ"; 
							break;
						case 6:  
							if(Num.equals("1") && !nNum.equals("0"))
								Str = "เอ็ดบาท";
							else
								Str = xStr + "บาท";
							break;
					}
					break;
			}
		return Str;
	}
	
	private static String CheckPoint2(int Point,int cPoint,String Num,String xStr,String oNum){
		String nNum = oNum.substring(oNum.length()-2, oNum.length()-1);
		String Str = "";
			switch( cPoint ){
				case 1://หน่วย
					Str = xStr + "สตางค์";
					break;
				case 2://สิบ
					switch( Point ){
						case 0:	
							if( Num.equals("2") )
								Str = "ยี่สิบ";
							else if(!Num.equals("0") ) 
								Str = xStr + "สิบ"; 
							
							break;
						case 1: 
							if(Num.equals("1") && !nNum.equals("0"))
								Str = "เอ็ดสตางค์";
							else
								Str = xStr + "สตางค์";
						break;
					}
					break;
			}
		return Str;
	}

	public static String UnComma2d(String StrNumber){
		try{
			double DoubleNo = Double.valueOf(StrNumber).doubleValue(); 
			DecimalFormat df = new DecimalFormat("#00.00");
			return df.format(DoubleNo).toString();
		}catch(Exception e){
			return "0";
		}
	}

	public static String UnComma3d(String StrNumber){
		try{
			double DoubleNo = Double.valueOf(StrNumber).doubleValue(); 
			DecimalFormat df = new DecimalFormat("#00.000");		
			return df.format(DoubleNo).toString();
		}catch(Exception e){
			return "0";
		}
	}
	
	public static String UnComma4d(String StrNumber){
		try{
			double DoubleNo = Double.valueOf(StrNumber).doubleValue(); 
			DecimalFormat df = new DecimalFormat("#00.0000");		
			return df.format(DoubleNo).toString();
		}catch(Exception e){
			return "0";
		}
	}
	
	public static double UnComma5to2d(String StrNumber){
		try{
			double DoubleNo = Double.valueOf(StrNumber).doubleValue(); 
			DecimalFormat df = new DecimalFormat("#00.00000");	
			String ds = df.format(DoubleNo).toString();
			double Total = 0.00;
			int i = (Integer)ds.trim().indexOf( '.' );
			
			String pnt = ds.substring(i+1, ds.length());
			int chk = Integer.parseInt( pnt.substring( pnt.length()-1, pnt.length() ) );
			ds = UpNum(chk,5,ds)+"";

			pnt = ds.substring(i+1, ds.length());
			chk = Integer.parseInt( pnt.substring( pnt.length()-1, pnt.length() ) );
			ds = UpNum(chk,4,ds)+"";
			
			pnt = ds.substring(i+1, ds.length());
			chk = Integer.parseInt( pnt.substring( pnt.length()-1, pnt.length() ) );
			ds = UpNum(chk,3,ds)+"";

			Total = Double.parseDouble(ds);
			//String getL = 
					
			return Total;
		}catch(Exception e){
			return 0;
		}
	}
	
	private static String UpNum(int Chk,int Len,String Value){
		String Total = "";
		if( Chk >= 5 ){
			if( Len == 5 ) {
				Total =  (Double.parseDouble( Value.substring(0, Value.length()-1) ) + 0.0001)+"";
				DecimalFormat df = new DecimalFormat("#00.0000");	
				String dsx = df.format( Double.valueOf(Total).doubleValue() ).toString();
				Total = dsx;
			}else if( Len == 4 ){
				Total =  (Double.parseDouble( Value.substring(0, Value.length()-1) ) + 0.001)+"";
				DecimalFormat df = new DecimalFormat("#00.000");	
				String dsx = df.format( Double.valueOf(Total).doubleValue() ).toString();
				Total = dsx;
			}else if( Len == 3 ){
				Total =  (Double.parseDouble( Value.substring(0, Value.length()-1) ) + 0.01)+"";
				DecimalFormat df = new DecimalFormat("#00.00");	
				String dsx = df.format( Double.valueOf(Total).doubleValue() ).toString();
				Total = dsx;
			}
		}else{
			if( Len == 5 ) {
				Total =  (Double.parseDouble( Value.substring(0, Value.length()-1) ))+"";
				DecimalFormat df = new DecimalFormat("#00.0000");	
				String dsx = df.format( Double.valueOf(Total).doubleValue() ).toString();
				Total = dsx;
			}else if( Len == 4 ){
				Total =  (Double.parseDouble( Value.substring(0, Value.length()-1) ))+"";
				DecimalFormat df = new DecimalFormat("#00.000");	
				String dsx = df.format( Double.valueOf(Total).doubleValue() ).toString();
				Total = dsx;
			}else if( Len == 3 ){
				Total =  (Double.parseDouble( Value.substring(0, Value.length()-1) ))+"";
				DecimalFormat df = new DecimalFormat("#00.00");	
				String dsx = df.format( Double.valueOf(Total).doubleValue() ).toString();
				Total = dsx;
			}
		}
		return Total;
	}
	
	public static int Vat(String xUsername,String xPassword) throws Exception{
		ResultSet rs = null;
	    SqlSelection sqlsel = new SqlSelection();
	    sqlsel.uName = xUsername;
	    sqlsel.Pwd = xPassword;
	    int xVat = 0;
		rs = sqlsel.getReSultSQL("SELECT * FROM vat");
		while(rs.next()){
			xVat = rs.getInt("Vat");
		}
		rs.close();
		return xVat;
	}
	
	public static int Vat711(String xUsername,String xPassword) throws Exception{
		ResultSet rs = null;
	    SqlSelection sqlsel = new SqlSelection();
	    sqlsel.uName = xUsername;
	    sqlsel.Pwd = xPassword;
	    int xVat = 0;
		rs = sqlsel.getReSultSQL("SELECT * FROM vat711");
		while(rs.next()){
			xVat = rs.getInt("Vat");
		}
		rs.close();
		return xVat;
	}
	
	public static int BitToNum(boolean b ){
		if(b==false)
			return 0;
		else	
			return 1;
	}
	
	public static boolean NumToBit(int b ){
		if(b==0)
			return false;
		else	
			return true;
	}	
	
	public static String addZero(String StrNumber){
		try{
			double DoubleNo = Double.valueOf(StrNumber).doubleValue(); 
			DecimalFormat df = new DecimalFormat("#00");		
			return df.format(DoubleNo).toString();
		}catch(Exception e){
			return "00";
		}
	}

	public static String addZero(String StrNumber,String Format){
		try{
			double DoubleNo = Double.valueOf(StrNumber).doubleValue(); 
			DecimalFormat df = new DecimalFormat(Format);		
			return df.format(DoubleNo).toString();
		}catch(Exception e){
			return Format;
		}
	}
	
	public static String ChkComm(String StrNumber){
		String cString = "";
		int cOver = 0;
		int g = ChkCommUp(StrNumber);
		if(g > 0){
			cOver = Integer.parseInt( StrNumber.substring(g, g+1) );
			if(cOver > 5 )
				cString = (Integer.parseInt( StrNumber.substring(0, g-1) ) + 1) + "";
			else
				cString = (Integer.parseInt( StrNumber.substring(0, g-1) ) ) + "";
		}
		return cString + ",";
	}		
	
	private static int ChkCommUp(String StrNumber){
		int j=1;
		for(int i=0;i<StrNumber.length();i++){
			if( !StrNumber.substring(i, i+1).equals(",") ){
				j++;
			}else{
				return j;
			}
		}
		return j;
	}	
	
}
