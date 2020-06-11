package com.amitrei.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyDateUtil {


    public java.sql.Date expiredDate(int daysFromStart) {
        Date date = new java.util.Date();
        return new java.sql.Date(date.getTime() + (1000 * 60 * 60 * (daysFromStart * 24)));
    }

    public java.sql.Date currentDate() {
        Date date = new java.util.Date();
        return new java.sql.Date(date.getTime());
    }

    public Boolean isDatePassed(java.sql.Date expiredDate) {
        return currentDate().after(expiredDate);
    }
}
