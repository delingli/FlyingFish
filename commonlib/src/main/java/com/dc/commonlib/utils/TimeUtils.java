package com.dc.commonlib.utils;


import android.util.Log;

import com.hikvision.sdk.utils.SDKUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.util.Locale.SIMPLIFIED_CHINESE;

public class TimeUtils {

    private static final String TAG = "TimeUtils";

    /**
     * 字符串转换为Calendar
     *
     * @param str
     * @param dateFormat
     * @return
     */
    public static Calendar str2Calendar(String str, SimpleDateFormat dateFormat) {
        Date date = null;
        try {
            date = dateFormat.parse(str);
            Calendar instance = Calendar.getInstance();
            instance.setTime(date);
            return instance;
        } catch (ParseException e) {
            Log.e(TAG, "timeUtils", e);
        }
        return null;
    }

    /**
     * Calendar 转换为字符串
     *
     * @param calendar
     * @param dateFormat
     * @return
     */
    public static String calendar2Str(Calendar calendar, SimpleDateFormat dateFormat) {
        if (calendar == null) {
            throw new IllegalArgumentException("calendar not allow null");
        }
        if (dateFormat == null) {
            throw new IllegalArgumentException("dateFormat not allow null");
        }
        return dateFormat.format(calendar.getTime());
    }

    private static String[] time = new String[3];

    public static String[] getVideoTime(int position) {
        int minute = position * 60 % 60;
        int hour = minute * 60 % 60;
        position = position > 60 ? 0 : position;
        String s = position < 10 ? "0" + position : position + "";
        String m = minute < 10 ? "0" + minute : minute + "";
        String h = hour < 10 ? "0" + hour : hour + "";
        time[0] = h;
        time[1] = m;
        time[2] = s;
        return time;
    }


    /**
     * 格式化毫秒数为 xx:xx:xx这样的时间格式。
     *
     * @param ms 毫秒数
     * @return 格式化后的字符串
     */
    public static String formatMs(long ms) {
        int seconds = (int) (ms / 1000);
        int finalSec = seconds % 60;
        int finalMin = seconds / 60 % 60;
        int finalHour = seconds / 3600;

        StringBuilder msBuilder = new StringBuilder("");
        if (finalHour > 9) {
            msBuilder.append(finalHour).append(":");
        } else if (finalHour > 0) {
            msBuilder.append("0").append(finalHour).append(":");
        }

        if (finalMin > 9) {
            msBuilder.append(finalMin).append(":");
        } else if (finalMin > 0) {
            msBuilder.append("0").append(finalMin).append(":");
        } else {
            msBuilder.append("00").append(":");
        }

        if (finalSec > 9) {
            msBuilder.append(finalSec);
        } else if (finalSec > 0) {
            msBuilder.append("0").append(finalSec);
        } else {
            msBuilder.append("00");
        }

        return msBuilder.toString();
    }

    /**
     * 获取一天开始时间
     */
    public static Calendar getStartTimeOfDay() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart;
    }

    /**
     * 获取一天的结束时间
     */
    public static Calendar getEndTimeOfDay() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd;
    }


    public static String strangeFormatConvert(String time, SimpleDateFormat resultFormat) throws ParseException {
        SimpleDateFormat strangeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z", SIMPLIFIED_CHINESE);
        time = time.replace("Z", " UTC");//注意是空格+UTC
        Date beginDate = strangeFormat.parse(time);
        // 减去8小时时差
        long t = beginDate.getTime() - 28800000;
        beginDate.setTime(t);
        return resultFormat.format(beginDate);
    }

    public static Calendar getEndTimeOfDay(Date selectDate) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(selectDate.getTime());
        instance.set(Calendar.HOUR_OF_DAY, 23);
        instance.set(Calendar.MINUTE, 59);
        instance.set(Calendar.SECOND, 59);
        instance.set(Calendar.MILLISECOND, 999);
        return instance;
    }

    public static Calendar getStartTimeOfDay(Date selectDate) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(selectDate.getTime());
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        instance.set(Calendar.MILLISECOND, 0);
        return instance;
    }

    public static Calendar str2Calendar(String time) {
        Calendar instance = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:mm:dd hh:mm:ss");
        try {
            Date parse = simpleDateFormat.parse(time);
            instance.setTime(parse);
        } catch (ParseException e) {
            Log.e(TAG, "timeUtils", e);
        }
        return instance;
    }

    public static Calendar convertTimeString(String time) {
        Date date = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (SDKUtil.isEmptyString(time)) {
            return calendar;
        }

        String aString = time.replace("T", " ");
        String bString = aString.replace("Z", "");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", SIMPLIFIED_CHINESE);
        try {
            date = sdf1.parse(bString);
            calendar.setTime(date);
            return calendar;
        } catch (ParseException e) {
            return calendar;
        }
    }

    public static String convertTime2String(String time) {
        Date date = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        String aString = time.replace("T", " ");
        String bString = aString.replace("Z", "");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", SIMPLIFIED_CHINESE);
        try {
            Date parse = sdf1.parse(bString);
            return sdf1.format(parse);
        } catch (ParseException e) {
            Log.e(TAG, "timeUtils", e);
            return "";
        }
    }


    /**
     * 时间戳转换成日期格式字符串
     * @param seconds 精确到秒的字符串
     * @param formatStr
     * @return
     */
    public static String timeStamp2Date(String seconds,String format) {
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
            return "";
        }
        if(format == null || format.isEmpty()){
            format = "HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds+"000")));
    }

}
