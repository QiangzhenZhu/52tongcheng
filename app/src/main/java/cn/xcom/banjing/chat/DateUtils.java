package cn.xcom.banjing.chat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by mac on 2017/10/3.
 */

public class   DateUtils {
    /**
     * 定义常量
     **/
    public static final String DATE_JFP_STR = "yyyyMM";
    public static final String DATE_FULL_STR = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_SMALL_STR = "yyyy-MM-dd";
    public static final String DATE_KEY_STR = "yyMMddHHmmss";
    /*
       * 将时间转换为时间戳//haha
       */
    public static String dateToStamp(String time) throws ParseException {
        String date = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date(Long.parseLong(time) * 1000));
        return date;
    }
    // 将时间戳转为字符串
    public static String getStrTime(String cc_time,String type) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat(type);
//        // 例如：
//        cc_time="1291778220" ;
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;
    }

    /**
     * 使用预设格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @return
     */
    public static Date parse(String strDate) {
        return parse(strDate, DATE_FULL_STR);
    }

    /**
     * 使用用户格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @param pattern 日期格式
     * @return
     */
    public static Date parse(String strDate, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 两个时间比较
     *
     * @param date1
     * @return
     */
    public static int compareDateWithNow(Date date1) {
        Date date2 = new Date();
        int rnum = date1.compareTo(date2);
        return rnum;
    }

    /**
     * 两个时间比较(时间戳比较)
     *
     * @param date1
     * @return
     */
    public static int compareDateWithNow(long date1) {
        long date2 = dateToUnixTimestamp();
        if (date1 > date2) {
            return 1;
        } else if (date1 < date2) {
            return -1;
        } else {
            return 0;
        }
    }


    /**
     * 获取系统当前时间
     *
     * @return
     */
    public static String getNowTime() {
        SimpleDateFormat df = new SimpleDateFormat(DATE_FULL_STR);
        return df.format(new Date());
    }

    /**
     * 获取系统当前时间
     *
     * @return
     */
    public static String getNowTime(String type) {
        SimpleDateFormat df = new SimpleDateFormat(type);
        return df.format(new Date());
    }

    /**
     * 获取系统当前计费期
     *
     * @return
     */
    public static String getJFPTime() {
        SimpleDateFormat df = new SimpleDateFormat(DATE_JFP_STR);
        return df.format(new Date());
    }

    /**
     * 将指定的日期转换成Unix时间戳
     * 需要/1000
     *
     * @param date date 需要转换的日期 yyyy-MM-dd HH:mm:ss
     * @return long 时间戳
     */
    public static long dateToUnixTimestamp(String date) {
        long timestamp = 0;
        try {
            timestamp = new SimpleDateFormat(DATE_FULL_STR).parse(date).getTime();
            timestamp = timestamp / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }

    public static long dateToUnixTimestampSmall(String date) {
        long timestamp = 0;
        try {
            timestamp = new SimpleDateFormat(DATE_SMALL_STR).parse(date).getTime();
            timestamp = timestamp / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }

    /**
     * 将指定的日期转换成Unix时间戳
     *
     * @param date date 需要转换的日期 yyyy-MM-dd
     * @return long 时间戳
     */
    public static long dateToUnixTimestamp(String date, String dateFormat) {
        long timestamp = 0;
        try {
            timestamp = new SimpleDateFormat(dateFormat).parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }

    /**
     * 将当前日期转换成Unix时间戳
     *
     * @return long 时间戳
     */
    public static long dateToUnixTimestamp() {
        long timestamp = new Date().getTime();
        return timestamp;
    }


    /*Date转换成字符窜*/
    public static String DateToString(Date time) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FULL_STR);
        java.util.Date date = new java.util.Date();
        return sdf.format(date);
    }


    public static String getWeekStr(int year, int month, int day) {
        String weekStr = "";
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        switch (week) {
            case 1:
                weekStr = "周日";
                break;
            case 2:
                weekStr = "周一";
                break;
            case 3:
                weekStr = "周二";
                break;
            case 4:
                weekStr = "周三";
                break;
            case 5:
                weekStr = "周四";
                break;
            case 6:
                weekStr = "周五";
                break;
            case 7:
                weekStr = "周六";
                break;
        }
        return weekStr;
    }
}
