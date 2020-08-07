package com.spring.dev2chuc.nutritious_food.helper;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;

public class DateTimeHelper {
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public static String formatDateFromLong(Instant instant) {
        long time = instant.toEpochMilli();
        if (time == 0) {
            return "";
        }
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.setTimeInMillis(time);
        return simpleDateFormat.format(tempCalendar.getTime());
    }
}
