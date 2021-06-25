package com.dc.baselib.http.newhttp;

import android.content.Context;
import android.util.Log;

import com.dc.baselib.BaseApplication;
import com.dc.baselib.http.exception.ApiException;
import com.dc.baselib.http.exception.CustomException;
import com.dc.baselib.http.response.HttpResponse;
import com.dc.baselib.utils.ToastUtils;

import io.reactivex.subscribers.DisposableSubscriber;


//统一处理返回数据
public abstract class AbsHttpSubscriber<T> extends DisposableSubscriber<HttpResponse<T>> implements AbsSubscriberListener<T> {


    public AbsHttpSubscriber() {
    }

    @Override
    protected void onStart() {
        super.onStart();
        showLoading();
//        if (!NetWorkUtils.isNetworkAvailable(BaseApplication.getsInstance())) {
//            onNoNetWork();
//            cancel();
//        }
    }

    private void onNoNetWork() {
        ToastUtils.showToast("网络状态异常");
    }


    @Override
    public void onNext(HttpResponse<T> tHttpResponse) {
        if (null == tHttpResponse) {
            ToastUtils.showToast("tHttpResponse为null");
            return;
        }
        if (tHttpResponse.getCode()==0){
            Log.d("AbsHttpSubscriber", "#原生数据#" + tHttpResponse.getData().toString());
            onSuccess(tHttpResponse.getData());
        } else {
            ApiException e = CustomException.handlerCustomException(new ApiException(tHttpResponse.getCode(), tHttpResponse.getMessage()));
            Log.e("AbsHttpSubscriber", "code:" + tHttpResponse.getCode() + "message:" + tHttpResponse.getMessage());
            onFailure(e.getMes(), e.getCode());
            // 多重显示错误的问题，应该在这里判断一下
        }

    }

    @Override
    public void onError(Throwable t) {
        hideLoading();
        ApiException e = CustomException.handlerServerException(t);
        onFailure(e.getMes(), e.getCode());
    }

    @Override
    public void onComplete() {
        hideLoading();
    }



    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {

    }
}
