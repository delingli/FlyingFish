package com.dc.baselib.utils;

import com.blankj.utilcode.util.LogUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {

    /*时间戳转换成字符窜*/
    public static String getDateToString(long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sf.format(d);
    }

    public static int getGapMinutes(String startDate, String endDate) {
        long start = 0;
        long end = 0;
        LogUtils.e("开始结束时间" + startDate + "  " + endDate);
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            start = df.parse(startDate).getTime();
            end = df.parse(endDate).getTime();
        } catch (Exception e) {
            e.printStackTrace();

        }
        int minutes = (int) ((end - start) / (1000 * 60));
        LogUtils.e("开始结束时间" + minutes + "");

        return minutes;

    }
}
