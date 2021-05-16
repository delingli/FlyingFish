package com.dc.baselib.mvvm;

import java.util.UUID;

public class EventUtils {
    public static String getEventKey() {
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }
}
