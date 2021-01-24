package com.data.util;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Date;

import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previousOrSame;

public class DateTimeUtil {
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String SIMPLE_DATE_FORMAT = "yyyyMMdd";
    public static final String DAY_FORMAT = "yyyy-MM-dd";
    public static final String HOUR_FORMAT = "HH";
    public static final String BIRTHDAY_FORMAT = "MM-dd";
    public static final String DNET_FORMAT = "yyyy.MM.dd";

    public static String getLineBreakOfSystem() {
        return System.getProperty("line.separator", "/n");
    }

    public static String getFormatedDate(Date date, String dateFormat) {
        SimpleDateFormat dft = new SimpleDateFormat(dateFormat);
        return dft.format(date.getTime());
    }

    public static String getFormatedDateFromTomeStamp(Timestamp timestamp, String format) {
        SimpleDateFormat dft = new SimpleDateFormat(format);
        return dft.format(timestamp.getTime());
    }

    public static  Date parse( String dateFormat, String date) throws ParseException
    {
        SimpleDateFormat dft = new SimpleDateFormat(dateFormat);
        return dft.parse(date);
    }


//    public static void main(String[] args) {
//        System.out.println(System.currentTimeMillis());
//
//        System.out.println(new BigDecimal(588).divide(new BigDecimal(588), 2, RoundingMode.HALF_UP).intValue());

//    for(int i=1;i<1000;i++){
//        System.out.println(System.currentTimeMillis() + "" + new Random().nextInt(10000));
//    }
//
//        Arrays.asList("1","2","3","4","5").subList(0,4).forEach(v->System.out.println(v));
//    }


    public static String getCurrentYear() {
        return String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
    }

    public static String getCurrentMonth() {
        final int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        if (currentMonth >= 10) {
            return String.valueOf(currentMonth);
        }
        return "0" + currentMonth;
    }

    public static Date getDateAfter(Date date, int after) {
        if (null != date) {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE, after);
            return c.getTime();
        }
        return null;
    }

    //根据日期取得星期几
    public static String getWeek(Date date){
        String[] weeks = {"周日","周一","周二","周三","周四","周五","周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if(week_index<0){
            week_index = 0;
        }
        return weeks[week_index];
    }


    /**
     * 获得某天的零点时刻0:0:0
     *
     * @param date 日期
     * @return
     */
    public static Date getDayBegin(Date date) {

        if (date == null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获得某天的截至时刻23:59:59
     *
     * @param date
     * @return
     */
    public static Date getDayEnd(Date date) {

        if (date == null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }


    public static String generateIndex(long index) {
        if (index > 99999) {
            return String.valueOf(index);
        }
        //return org.apache.commons.lang.StringUtils.right("0000" + index, 5);
        return org.apache.commons.lang3.StringUtils.right("0000" + index, 5);
    }

    public static Date getStartDateTimeOfThisWeek() {
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(previousOrSame(DayOfWeek.MONDAY));
        LocalTime startTime = LocalTime.MIN;
        LocalDateTime mondayStart = LocalDateTime.of(monday, startTime);
        return Date.from(mondayStart.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date getEndDateTimeOfThisWeek() {
        LocalDate today = LocalDate.now();
        LocalDate sunday = today.with(nextOrSame(DayOfWeek.SUNDAY));
        LocalTime endTime = LocalTime.MAX;
        LocalDateTime sundayEnd = LocalDateTime.of(sunday, endTime);
        return Date.from(sundayEnd.atZone(ZoneId.systemDefault()).toInstant());
    }


    public static int getBetweenDay(Long startTime, Long endTime) {
        if (endTime <= startTime) {
            return 0;
        }

        if (Long.valueOf(endTime - startTime).compareTo(Long.valueOf(24 * 3600 * 1000)) < 0) {
            return 1;
        }

        final BigDecimal dayDecimal = (new BigDecimal(endTime).subtract(new BigDecimal(startTime))).divide(new BigDecimal(24 * 3600 * 1000), 0);
        return dayDecimal.intValue();
    }

    public static int getBetweenMinutes(Long startTime, Long endTime) {

        if (endTime <= startTime) {
            return -1;
        }

        if (Long.valueOf(endTime - startTime).compareTo(Long.valueOf(60 * 1000)) < 0) {
            return 1;
        }

        final BigDecimal minutesDecimal = (new BigDecimal(endTime).subtract(new BigDecimal(startTime))).divide(new BigDecimal(60 * 1000), 0);

        return minutesDecimal.intValue();
    }

    // 通过时间格式格式化时间
    public static String formatDateTime(String pattern, Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);

        return formatter.format(date);
    }

    public static int getAgeByBirth(Date birthday) {
        int age = 0;
        try {
            Calendar now = Calendar.getInstance();
            now.setTime(new Date());// 当前时间

            Calendar birth = Calendar.getInstance();
            birth.setTime(birthday);

            if (birth.after(now)) {//如果传入的时间，在当前时间的后面，返回0岁
                age = 0;
            } else {
                age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
                if (now.get(Calendar.DAY_OF_YEAR) > birth.get(Calendar.DAY_OF_YEAR)) {
                    age += 1;
                }
            }
            return age;
        } catch (Exception e) {//兼容性更强,异常后返回数据
            return 0;
        }
    }

//    public static void main(String[] args)
//    {
//        System.out.println(Integer.valueOf("0001"));
//    }
}
