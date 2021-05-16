package com.dc.baselib.http;

import java.lang.reflect.Type;

public class TokenUtil {
    public static <E> Type getType(){
        return new CustomTypeToken<E>() {}.getType();
    }
}
