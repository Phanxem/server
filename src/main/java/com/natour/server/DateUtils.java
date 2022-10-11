package com.natour.server;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static final String DATE_SIMPLE_PATTERN = "dd/MM/yyyy";
    public static final String DATE_FULL_PATTERN = "yyyy-MM-dd hh:mm:ss";

    public static Calendar toCalendar(String string) throws ParseException {
        
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat simpleDateFormat;
        Date date = null;
        try {
        	simpleDateFormat = new SimpleDateFormat(DATE_SIMPLE_PATTERN);
            date = simpleDateFormat.parse(string);
        }
        catch (ParseException e) { }
        
        try {
        	simpleDateFormat = new SimpleDateFormat(DATE_FULL_PATTERN);
            date = simpleDateFormat.parse(string);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        calendar.setTime(date);

        return calendar;
    }


    public static String toSimpleString(Calendar calendar){
        Date date = new Date(calendar.getTimeInMillis());
        DateFormat dateFormat = new SimpleDateFormat(DATE_SIMPLE_PATTERN);
        String string = dateFormat.format(date);

        return string;
    }

    public static String toFullString(Calendar calendar){
        Date date = new Date(calendar.getTimeInMillis());
        DateFormat dateFormat = new SimpleDateFormat(DATE_FULL_PATTERN);
        String string = dateFormat.format(date);

        return string;
    }
    
    
    public static String toSimpleString(Timestamp timestamp){
        DateFormat dateFormat = new SimpleDateFormat(DATE_SIMPLE_PATTERN);
        String string = dateFormat.format(timestamp);

        return string;
    }

    public static String toFullString(Timestamp timestamp){
        DateFormat dateFormat = new SimpleDateFormat(DATE_FULL_PATTERN);
        String string = dateFormat.format(timestamp);

        return string;
    }
    
    
    public static Timestamp toTimestamp(String string) throws ParseException {
    	SimpleDateFormat simpleDateFormat;
        
        Date date = null;
        try {
        	simpleDateFormat = new SimpleDateFormat(DATE_SIMPLE_PATTERN);
        	date = simpleDateFormat.parse(string);
        }
        catch (ParseException e) { }
        
        try {
        	simpleDateFormat = new SimpleDateFormat(DATE_FULL_PATTERN);
            date = simpleDateFormat.parse(string);
        }
        catch (ParseException e) {
        	//TODO	
        	e.printStackTrace();
        }
       
    	Timestamp timestamp = new Timestamp(date.getTime());
    	
		return timestamp;
    }
}
