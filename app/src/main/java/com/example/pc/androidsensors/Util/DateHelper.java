package com.example.pc.androidsensors.Util;

import java.util.Calendar;

/**
 * Created by pc on 15/05/2020.
 */

public class DateHelper {

    public static long getToday() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis( System.currentTimeMillis() );
        c.set( Calendar.HOUR_OF_DAY, 0 );
        c.set( Calendar.MINUTE, 0 );
        c.set( Calendar.SECOND, 0 );
        c.set( Calendar.MILLISECOND, 0 );
        return c.getTimeInMillis();
    }
}

