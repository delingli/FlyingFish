package com.dc.baselib.mvvm;

import java.util.UUID;

public class EventUtils {
    public static String getEventKey() {
        return UUID.randomUUID().toString();
    }
}
