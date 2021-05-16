package com.dc.commonlib.utils;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MoneyUtils {

    public static double yuanToHundredMillion(String yuan) {
        BigDecimal aa = new BigDecimal(yuan);
        BigDecimal bb = new BigDecimal(100000000 + "");
        return aa.divide(bb).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

    }

    public static String subtract(String a1, String b1) {
        BigDecimal aa = new BigDecimal(a1);
        BigDecimal bb = new BigDecimal(b1);
        return aa.subtract(bb).toString();

    }

    public static float percentagefor2(float prc) {
        BigDecimal aa = new BigDecimal(prc);
        BigDecimal bb = new BigDecimal(100);
        float f1 = aa.divide(bb, 2, BigDecimal.ROUND_HALF_EVEN).floatValue();
        return f1;
    }

    public static Float percentage(float base, float prc) {
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
// 设置精确到小数点后4位
        numberFormat.setMaximumFractionDigits(4);
        String result = numberFormat.format((float) prc / (float) base);//所占百分比
        return Float.valueOf(result);
    }

    public static double percentage(String base, String pct) {
        BigDecimal aa = new BigDecimal(base);
        BigDecimal bb = new BigDecimal(pct);
        double sd = bb.divide(aa, 2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        return bb.divide(aa, 2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
    }

    public static int percentageInt(float base, float pct) {
        float real = pct / base * 100;
        if (real > 0 && real < 1) {
            return 1;
        }
        return (int) real;
    }
}
