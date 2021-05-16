package com.dc.baselib.http;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class CustomTypeToken <T>{
    private final Type type;

    public CustomTypeToken() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        if (genericSuperclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        Type[] typeArguments = parameterizedType.getActualTypeArguments();
        type = typeArguments[0];
    }

    public Type getType() {
        return type;
    }
}
