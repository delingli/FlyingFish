package com.dc.commonlib.utils;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.view.View;

import com.dc.commonlib.R;

/**
 * 用于解决按钮防抖问题的工具类
 * <p>
 * Created by ZhangKe on 2019/5/29.
 */
public class AntiShakeUtils {

    private final static long INTERNAL_TIME = 1000;

    /**
     * Whether this click event is invalid.
     *
     * @param target target view
     * @return true, invalid click event.
     * @see #isInvalidClick(View, long)
     */
    public static boolean isInvalidClick(@NonNull View target) {
        return isInvalidClick(target, INTERNAL_TIME);
    }

    /**
     * Whether this click event is invalid.
     *
     * @param target       target view
     * @param internalTime the internal time. The unit is millisecond.
     * @return true, invalid click event.
     */
    public static boolean isInvalidClick(@NonNull View target, @IntRange(from = 0) long internalTime) {
        long curTimeStamp = System.currentTimeMillis();
        long lastClickTimeStamp = 0;
        Object o = target.getTag(R.id.anti_shake_last_click_time);
        if (o == null) {
            target.setTag(R.id.anti_shake_last_click_time, curTimeStamp);
            return false;
        }
        if (o instanceof Long) {
            lastClickTimeStamp = (Long) o;
            boolean isInvalid = curTimeStamp - lastClickTimeStamp < internalTime;
            if (!isInvalid)
                target.setTag(R.id.anti_shake_last_click_time, curTimeStamp);
            return isInvalid;
        } else {
            return false;
        }
    }
}
