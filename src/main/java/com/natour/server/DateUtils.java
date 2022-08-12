package com.natour.server;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static final String DATE_SIMPLE_PATTERN = "dd/MM/yyyy";
    public static final String DATE_FULL_PATTERN = "yyyy-MM-dd hh:mm:ss";

    public static Calendar toCalendar(String string) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_SIMPLE_PATTERN);
        Calendar calendar = Calendar.getInstance();

        Date date = null;
        try {
            date = simpleDateFormat.parse(string);
        }
        catch (ParseException e) {
            e.printStackTrace();

            simpleDateFormat = new SimpleDateFormat(DATE_FULL_PATTERN);
            date = simpleDateFormat.parse(string);

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
}
