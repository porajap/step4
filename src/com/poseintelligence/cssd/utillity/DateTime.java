package com.poseintelligence.cssd.utillity;

/*	
 *  -- ************************************************************
	-- Author		:	PARADOX
	-- Create date	:	18-07-2011
	-- Update date	:	PARADOX
	-- Update By	:   14-09-2011
	-- Description	:	Get Date [Version 1.0]
	-- ************************************************************
 */

/* 
 * Create By Paradox
 * 05-09-2010
 * Update By Paradox
 * 03-04-2014
 */

import java.util.Calendar;
import java.util.Date;
import java.text.*;

public class DateTime {
	public static String getDateNow() {
		Date now = new Date();
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String dmy = df.format(now);
		int year = Integer.valueOf(dmy.substring(6, 10)).intValue();
		return (dmy.substring(0, 2) + "-" + dmy.substring(3, 5) + "-" + year);
	}
	
	public static String getDateNow(String StrFormat) {
		Date now = new Date();
		SimpleDateFormat df = new SimpleDateFormat(StrFormat);
		String dmy = df.format(now);
		//int year = Integer.valueOf(dmy.substring(6, 10)).intValue();
		return dmy;
	}
	
	public static String getDateNowTH() {
		Date now = new Date();
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String dmy = df.format(now);
		int year = Integer.valueOf(dmy.substring(6, 10)).intValue() + 543;
		return (dmy.substring(0, 2) + "-" + dmy.substring(3, 5) + "-" + year);
	}
	
	public static String convert_YYYYMMDD(String StrDate) { //dd-MM-YYYY To DD-MM-YYYY
		//int IntYear = Integer.valueOf(StrDate.substring(6,10));
		return (StrDate.substring(8,10)+ "-" + StrDate.substring(5,7) + "-" + StrDate.substring(0,4));
	}
	
	public static String convertYYYY(String StrDate) { //dd-MM-YYYY To YYYY
		//int IntYear = Integer.valueOf(StrDate.substring(6,10));
		return StrDate.substring(6,10);
	}
	
	public static String convertDDMMYY(String StrDate) { //dd-MM-YYYY To DDMMYY
		//int IntYear = Integer.valueOf(StrDate.substring(6,10));
		return (StrDate.substring(0,2) + StrDate.substring(3,5) + StrDate.substring(8,10));
	}
	
	public static String convertThai(String StrDate) { //dd-MM-YYYY
		return (StrDate.substring(8,10) +"-"+ StrDate.substring(5,7) +"-"+ StrDate.substring(0,4));
	}
	
	public static String getDateNowEN() {
		Date now = new Date();
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String dmy = df.format(now);
		int year = Integer.valueOf(dmy.substring(6, 10)).intValue();
		return (year + "-" + dmy.substring(3, 5) + "-" + dmy.substring(0, 2));
	}
	
	public static String getAddDateNow(String xDate,String toDate) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
		String dateInString = toDate;
		Date date = sdf.parse(dateInString);
		
		DateFormat dateFormat= new SimpleDateFormat("dd-MM-yyyy");
		
		Calendar c = Calendar.getInstance(); 

		c.setTime(date);
		c.add(Calendar.DATE, Integer.parseInt( xDate ) );

		return dateFormat.format(c.getTime()); 
	}
	
	public static String getSubDateNow(String xDate,int toDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
		String dateInString = xDate;
		Date date = sdf.parse(dateInString);
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar c = Calendar.getInstance(); 

		c.setTime(date);
		c.add(Calendar.DATE, -toDate );
		
		return dateFormat.format(c.getTime()); 
	}
	

	
	public static String getDate(String StrFormat) {
		Date now = new Date();
		SimpleDateFormat df = new SimpleDateFormat(StrFormat);
		return df.format(now);
	}
	
	public static String getYearNow() {
		Date now = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy");
		return df.format(now);
	}
	
	public static String getTimeNow() {
		Date now = new Date();
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");	
		return df.format(now);
	}
	
	public static String getTimeMilliSecondNow() {
		Date now = new Date();
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss:SSS a,z");	
		return df.format(now);
	}
	
	//not sure
	public static String getDateTimeNow() {
		Date now = new Date();
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String dmy = df.format(now);
		int year = Integer.valueOf(dmy.substring(6, 10)).intValue();
		return (dmy.substring(0, 2) + "-" + dmy.substring(3, 5) + "-" + year + dmy.substring(10, 19));
	}
	
	public static String getDate_Format(String StrFormat) {
		Date now = new Date();
		SimpleDateFormat df = new SimpleDateFormat(StrFormat);
		return df.format(now);
	}
	
	public static String convertMMYYYY(String StrDate) { //dd-MM-YYYY To YYYYMM
		//int IntYear = Integer.valueOf(StrDate.substring(6,10));
		return (StrDate.substring(3,5)+"/"+StrDate.substring(6,10));
	}
	
	public static String convertDate(String StrDate) { //dd-MM-YYYY To YYYY-MM-dd
		//int IntYear = Integer.valueOf(StrDate.substring(6,10));
		return (StrDate.substring(6,10) +"-"+StrDate.substring(3,5)+"-"+StrDate.substring(0,2));
	}
	
	public static String convertDateTH(String StrDate) { //dd-MM-YYYY To YYYY-MM-dd
		int IntYear = Integer.valueOf(StrDate.substring(0,4))+ 543;
		//System.out.println(StrDate.substring(8,10) + "-" + StrDate.substring(5,7) + "-" + IntYear);
		return ( StrDate.substring(8,10) + "-" + StrDate.substring(5,7) + "-" + IntYear);
	}
	
	public static String convertDateTHyENG(String StrDate) { //dd-MM-YYYY To YYYY-MM-dd
		int IntYear = Integer.valueOf(StrDate.substring(0,4));
		//System.out.println(StrDate.substring(8,10) + "-" + StrDate.substring(5,7) + "-" + IntYear);
		return ( StrDate.substring(8,10) + "-" + StrDate.substring(5,7) + "-" + IntYear);
	}

	public static String convertDateEN(String StrDate) { //dd-MM-YYYY To YYYY-MM-dd
		int IntYear = Integer.valueOf(StrDate.substring(6,10))- 543;
		return ( IntYear +"-"+StrDate.substring(3,5)+"-"+StrDate.substring(0,2));
	}
	
	public static String convertDateTime(String StrDate) { //dd-MM-YYYY HH:mm:ss To YYYY-MM-dd HH:mm:ss
		//int IntYear = Integer.valueOf(StrDate.substring(6,10));
		return (StrDate.substring(6,10) +"-"+StrDate.substring(3,5)+"-"+StrDate.substring(0,2) + " " + StrDate.substring(11,19) );
	}
	
	public static String convertYYYYMM(String StrDate) { //dd-MM-YYYY To YYYYMM
		//int IntYear = Integer.valueOf(StrDate.substring(6,10));
		return (StrDate.substring(6,10) + StrDate.substring(3,5));
	}
	
	public static float convertTimeToFloat(String strTime) {
		if (strTime.length() == 5) { // Format HH:MM
			int IntHH = Integer.valueOf(strTime.substring(0, 2)).intValue();
			int IntMM = Integer.valueOf(strTime.substring(3, 5)).intValue();
			return (float) (((IntHH * 60) + IntMM) * 0.000694443333);
		} else {
			return 0f;
		}
	}

	public static String nextDate(String date, int d, String Format) {
		if(d < 2)
			return convertDate(date); 
		else
			d-=1;
		
		for (int i = 0; i < d; i++)
			date = next(date);
		
		if(Format.equals("yyyy-mm-dd"))
			return convertDate(date);
		else
			return date;
		
	}

	static String next(String Date) {
		int d = Integer.valueOf(Date.substring(0, 2)).intValue();
		int m = Integer.valueOf(Date.substring(3, 5)).intValue();
		int y = Integer.valueOf(Date.substring(6, 10)).intValue();

		int dt = 0;

		if (m == 4 || m == 6 || m == 9 || m == 11) {
			dt = 30;
		} else if (m == 2) {
			dt = leapYear(y);
		} else {
			dt = 31;
		}

		if (d < dt) {
			d++;
		} else {
			if (d == dt) {
				d = 1;
				m++;
				if (m > 12) {
					m = 1;
					y++;
				}
			}
		}

		if (d < 10 && m < 10)
			return ("0" + d + "-0" + m + "-" + y);
		else if (d < 10 && m > 9)
			return ("0" + d + "-" + m + "-" + y);
		else if (d > 9 && m < 10)
			return (d + "-0" + m + "-" + y);
		
		
		return (d + "-" + m + "-" + y);
	}

	
	public static String getLastDay(int xMonth,int xYear,String StrFormat){
		int d = 1;
		String xDD = "";
		switch(xMonth){
			case 2:	if ((xYear % 4 == 0) && (xYear % 100 != 0) ){
						d = 29;
					}else{
						d = 28;
					}

		}
		//d = 1;
		//Date now = new Date();
		//SimpleDateFormat df = new SimpleDateFormat(StrFormat);
		if( d  < 10) 
			xDD = "0" + d;
		else
			xDD = "" + d;
		return  xDD; //df.format(now);
	}
	
	static int leapYear(int theYear) {
		if (theYear < 100) {
			if (theYear > 40) {
				theYear += 1900;
			} else {
				theYear += 2000;
			}
		}

		if (theYear % 4 == 0) {
			if (theYear % 100 != 0) {
				return 29;
			} else if (theYear % 400 == 0) {
				return 29;
			} else {
				return 28;
			}
		} else {
			return 28;
		}
	}
	
	public static String getDateLastMonth(){
		try{
			String StrDate = getDateNow();
			int y = Integer.valueOf(StrDate.substring(6, 10)).intValue();
			int m = Integer.valueOf(StrDate.substring(3, 5)).intValue();
			int d = Integer.valueOf(StrDate.substring(0, 2)).intValue();
			int dt = 0;
			
			if(m == 1){
				m = 12;
				y -= 1;
			}else{
				m -= 1;
			}
			
			if (m == 4 || m == 6 || m == 9 || m == 11) {
				dt = 30;
			} else if (m == 2) {
				dt = leapYear(y);
			} else {
				dt = 31;
			}
			
			if(d > dt){
				d = 1;
				m += 1;
			}
			
			return (d > 9 ? d : ("0"+d)) + "-" + (m > 9 ? m : ("0"+m)) + "-" + y; 
		}catch(Exception e){ 
			return "";
		}
	}
	
	public static String getDateLastthreeMonth(){
		try{
			String StrDate = getDateNow();
			int y = Integer.valueOf(StrDate.substring(6, 10)).intValue();
			int m = Integer.valueOf(StrDate.substring(3, 5)).intValue();
			int d = Integer.valueOf(StrDate.substring(0, 2)).intValue();
			int dt = 0;
			
			if(m == 1){
				d -= 1;
				m = 10;
				y -= 1;
			}else{
				d -= 1;
				m -= 3;
			}
			
			if (m == 4 || m == 6 || m == 9 || m == 11) {
				dt = 30;
			} else if (m == 2) {
				dt = leapYear(y);
			} else {
				dt = 31;
			}
			
			if(d > dt){
				d = 1;
				m += 3;
			}
			
			return (d > 9 ? d : ("0"+d)) + "-" + (m > 9 ? m : ("0"+m)) + "-" + y; 
		}catch(Exception e){ 
			return "";
		}
	}
	
	public static String getDateLastdate(){
		try{
			String StrDate = getDateNow();
			int y = Integer.valueOf(StrDate.substring(6, 10)).intValue();
			int m = Integer.valueOf(StrDate.substring(3, 5)).intValue();
			int d = Integer.valueOf(StrDate.substring(0, 2)).intValue();
			int dt = 0;
			
			if(m == 1){
				d -= 1;
				m = 10;
				y -= 1;
			}else{
				d -= 1;
			}
			
			if (m == 4 || m == 6 || m == 9 || m == 11) {
				dt = 30;
			} else if (m == 2) {
				dt = leapYear(y);
			} else {
				dt = 31;
			}
			

			
			return (d > 9 ? d : ("0"+d)) + "-" + (m > 9 ? m : ("0"+m)) + "-" + y; 
		}catch(Exception e){ 
			return "";
		}
	}
	
	public static String getMonth() { //MM
		int IntMonth = 0;
		Date now = new Date();
		SimpleDateFormat df = new SimpleDateFormat("MM");
		String dmy = df.format(now);
		IntMonth = Integer.parseInt( dmy );
		return IntMonth+"";
	}
	
	public static String getMonthTHSub(int IntMonth) {
		String MonthTH = "";
		switch(IntMonth){
			case 1: MonthTH = "ม.ค.";break;
			case 2: MonthTH = "ก.พ.";break;
			case 3: MonthTH = "มี.ค.";break;
			case 4: MonthTH = "เม.ย.";break;
			case 5: MonthTH = "พ.ค.";break;
			case 6: MonthTH = "มิ.ย.";break;
			case 7: MonthTH = "ก.ค.";break;
			case 8: MonthTH = "ส.ค.";break;
			case 9: MonthTH = "ก.ย.";break;
			case 10: MonthTH = "ต.ค.";break;
			case 11: MonthTH = "พ.ย.";break;
			case 12: MonthTH = "ธ.ค.";break;
		}
		return MonthTH;
	}
	
	public static String getMonthTH(String StrDate) { //MM
		int IntMonth = Integer.valueOf(StrDate.substring(3,5));
		String MonthTH = "";
		switch(IntMonth){
			case 1: MonthTH = "มกราคม";break;
			case 2: MonthTH = "กุมภาพันธ์";break;
			case 3: MonthTH = "มีนาคม";break;
			case 4: MonthTH = "เมษายน";break;
			case 5: MonthTH = "พฤษภาคม";break;
			case 6: MonthTH = "มิถุนายน";break;
			case 7: MonthTH = "กรกฎาคม";break;
			case 8: MonthTH = "สิงหาคม";break;
			case 9: MonthTH = "กันยายน";break;
			case 10: MonthTH = "ตุลาคม";break;
			case 11: MonthTH = "พฤศจิกายน";break;
			case 12: MonthTH = "ธันวาคม";break;
		}
		return MonthTH + " " + StrDate.substring(6,10);
	}
	
	public static String getMonthTH(int IntMonth) { //MM
		String MonthTH = "";
		switch(IntMonth){
			case 1: MonthTH = "มกราคม";break;
			case 2: MonthTH = "กุมภาพันธ์";break;
			case 3: MonthTH = "มีนาคม";break;
			case 4: MonthTH = "เมษายน";break;
			case 5: MonthTH = "พฤษภาคม";break;
			case 6: MonthTH = "มิถุนายน";break;
			case 7: MonthTH = "กรกฎาคม";break;
			case 8: MonthTH = "สิงหาคม";break;
			case 9: MonthTH = "กันยายน";break;
			case 10: MonthTH = "ตุลาคม";break;
			case 11: MonthTH = "พฤศจิกายน";break;
			case 12: MonthTH = "ธันวาคม";break;
		}
		return MonthTH;
	}
	
	public static String FormatThai(String StrDate) { 
		int IntYear = Integer.valueOf(StrDate.substring(6,10))+543;
		int Month = Integer.valueOf(StrDate.substring(3,5));
		String sMonth = null;
		switch( Month ){
			case 1:sMonth = "มกราคม";
				break;
			case 2:sMonth = "กุมภาพันธ์";
				break;
			case 3:sMonth = "มีนาคม";
				break;
			case 4:sMonth = "เมษายน";
			break;
			case 5:sMonth = "พฤษภาคม";
			break;
			case 6:sMonth = "มิถุนายน";
			break;
			case 7:sMonth = "กรกฎาคม";
			break;
			case 8:sMonth = "สิงหาคม";
			break;
			case 9:sMonth = "กันยายน";
			break;
			case 10:sMonth = "ตุลาคม";
			break;
			case 11:sMonth = "พฤศจิกายน";
			break;
			case 12:sMonth = "ธันวาคม";
			break;
		}
		return (StrDate.substring(0,2) +"       "+sMonth+"       "+IntYear );
	}
	
}
