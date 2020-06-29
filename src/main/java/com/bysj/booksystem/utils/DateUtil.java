package com.bysj.booksystem.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {


    //日期格式化


    //sql_date转java_date
//    public static getJavaDate(){
//
//    }

    //java_date转sql_date
//    public static String getSqlDate(Date date){
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        java.sql.Date date1 = new java.sql.Date(date.getTime());
//        String format = simpleDateFormat.format(date1);
//
//        return format;
//    }

    public static String getFormatDate(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date_format = simpleDateFormat.format(date);
        return date_format;
    }


    //计算某一日期30天后是什么日期
    public static Date getCalendar(Date date,Integer count){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(calendar.DATE,30);
        Date time = calendar.getTime();
        return time;
    }

    //获取当前日期并转换格式
    public static Date getNowDate() throws ParseException {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String format = sdf.format(new Date());
        Date nowTime = sdf.parse(format);
        return nowTime;
    }

}
