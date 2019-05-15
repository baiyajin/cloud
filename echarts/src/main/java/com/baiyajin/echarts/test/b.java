package com.baiyajin.echarts.test;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class b {
    public static void main(String[] args){



        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
            java.util.Date date3 = format.parse("2019-1");
            java.sql.Date time = new java.sql.Date(date3.getTime());


            java.util.Date date4 = format.parse("2019-5");
            java.sql.Date time2 = new java.sql.Date(date3.getTime());

            List<Date> a = findDates(time,time2);
            System.out.println(a);
        } catch (ParseException e) {
            e.printStackTrace();
        }




    }
    public static List<Date> findDates(Date dBegin, Date dEnd)  {
        List lDate = new ArrayList();
            lDate.add(dBegin);
            Calendar calBegin = Calendar.getInstance();
            // 使用给定的 Date 设置此 Calendar 的时间  
            calBegin.setTime(dBegin);
            Calendar calEnd = Calendar.getInstance();
            // 使用给定的 Date 设置此 Calendar 的时间  
            calEnd.setTime(dEnd);
            // 测试此日期是否在指定日期之后  
            while (dEnd.after(calBegin.getTime()))
            {
             // 根据日历的规则，为给定的日历字段添加或减去指定的时间量  
             calBegin.add(Calendar.DAY_OF_MONTH, 1);
             lDate.add(calBegin.getTime());
            }
        return lDate;
    }

    }
