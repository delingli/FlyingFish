package com.dc.baselib.http.newhttp;


import android.arch.lifecycle.Observer;


import com.dc.baselib.http.response.HttpResponse;

public interface ObserverListener<T> extends Observer<HttpResponse<T>> {
    void onSucess(T data);
    void onFial(Throwable e);
}
