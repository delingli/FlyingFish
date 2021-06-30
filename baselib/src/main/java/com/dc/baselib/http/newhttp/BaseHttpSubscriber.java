package com.dc.baselib.http.newhttp;


import androidx.lifecycle.MutableLiveData;

import com.dc.baselib.BaseApplication;
import com.dc.baselib.http.exception.ApiException;
import com.dc.baselib.http.exception.CustomException;
import com.dc.baselib.http.response.HttpResponse;
import com.dc.baselib.utils.NetWorkUtils;

import io.reactivex.subscribers.DisposableSubscriber;

//统一处理返回数据 暂时不用
public class BaseHttpSubscriber<T> extends DisposableSubscriber<HttpResponse<T>> {
    private MutableLiveData<HttpResponse<T>> data;

    public BaseHttpSubscriber() {
        this.data = new MutableLiveData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!NetWorkUtils.isNetworkAvailable(BaseApplication.Companion.getInstances())) {
            onNoNetWork();
            cancel();

            return;
        }
    }

    private void onNoNetWork() {
    }

    public MutableLiveData<HttpResponse<T>> getData() {
        return data;
    }

    @Override
    public void onNext(HttpResponse<T> tHttpResponse) {
        if (tHttpResponse.getCode() ==(StatusCode.SUCESSCODE)) {
            onFinish(tHttpResponse);
        } else {
            getErrorDto(CustomException.handlerCustomException(new ApiException(tHttpResponse.getCode(), tHttpResponse.getMessage())));
        }
    }

    public void set(HttpResponse<T> t) {
        this.data.postValue(t);
    }

    public void onFinish(HttpResponse<T> t) {
        set(t);
    }


    @Override
    public void onError(Throwable t) {
        getErrorDto(CustomException.handlerServerException(t));
    }

    //通知
    private void getErrorDto(ApiException ex) {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setCode(ex.getCode());
        httpResponse.setMessage(ex.getMes());
        onFinish(httpResponse);
    }

    @Override
    public void onComplete() {

    }
}
