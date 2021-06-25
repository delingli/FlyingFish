package com.dc.baselib.http.newhttp;

public interface AbsSubscriberListener<T> {
    void showLoading();

    void hideLoading();

    void onSuccess(T t);

    void onFailure(String msg, int code);
}
