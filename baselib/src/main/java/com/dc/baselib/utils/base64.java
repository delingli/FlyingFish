package com.dc.baselib.utils;

import android.text.TextUtils;
import android.util.Base64;

public class base64 {
    /**
     * BASE64解密
     *
     * @param key the String to be decrypted
     * @return byte[] the data which is decrypted
     * @throws
     */
    public static String decryptBASE64(String key) throws Exception {
        return new String(Base64.decode(key, Base64.DEFAULT), "UTF-8");
    }

    /**
     * BASE64加密
     *

     * @return String the data which is encrypted
     * @throws
     */
    public static String encryptBASE64(String data) throws Exception {
        if(TextUtils.isEmpty(data)){
            return null;
        }
        return Base64.encodeToString(data.getBytes(), Base64.DEFAULT);
    }
}
