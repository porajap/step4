package com.poseintelligence.cssd.utillity;

import java.text.DecimalFormat;

public class Number {
	public static String addComma2d(String S_Number){
		try{
			double D_No = Double.valueOf(S_Number).doubleValue(); 
			DecimalFormat df = new DecimalFormat("###,##0.00");		
			return df.format(D_No).toString();
		}catch(Exception e){
			return "0";
		}
	}
	
	public static String addComma0d(String S_Number){
		try{
			double D_No = Double.valueOf(S_Number).doubleValue(); 
			DecimalFormat df = new DecimalFormat("###,##0");		
			return df.format(D_No).toString();
		}catch(Exception e){
			return "0";
		}
	}
	
	public static String addComma0d(int S_Number){
		try{
			double D_No = Double.valueOf(S_Number).doubleValue(); 
			DecimalFormat df = new DecimalFormat("###,##0");		
			return df.format(D_No).toString();
		}catch(Exception e){
			return "0";
		}
	}
}
