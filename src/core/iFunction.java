package core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class iFunction {

	  public String getLastDay(String year, String month) {
		  
		    // get a calendar object
		    GregorianCalendar calendar = new GregorianCalendar();
		    
		    // convert the year and month to integers
		    int yearInt = Integer.parseInt(year);
		    int monthInt = Integer.parseInt(month);
		    
		    // adjust the month for a zero based index
		    monthInt = monthInt - 1;
		    
		    // set the date of the calendar to the date provided
		    calendar.set(yearInt, monthInt, 1);
		    
		    int dayInt = calendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		    
		    return Integer.toString(dayInt);
		  } // end getLastDay method
	  
	  public String getMonth(){
		  String pattern = "MM";
		  SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

		  String formatted = simpleDateFormat.format(new Date());
		  System.out.println(formatted);
		  return formatted;
	  }
	  
	  public String getYear(){
		  String pattern = "yyyy";
		  SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

		  String formatted = simpleDateFormat.format(new Date());
		  System.out.println(formatted);
		  return formatted;
	  }
}
