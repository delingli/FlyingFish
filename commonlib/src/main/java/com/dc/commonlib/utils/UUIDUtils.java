package com.dc.commonlib.utils;

import java.util.UUID;

public class UUIDUtils {
    public static String createUUid() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid;
    }
}
