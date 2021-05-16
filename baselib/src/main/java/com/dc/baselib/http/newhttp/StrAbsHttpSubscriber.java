package com.dc.baselib.http.newhttp;

import android.content.Context;

import com.dc.baselib.BaseApplication;
import com.dc.baselib.http.exception.ApiException;
import com.dc.baselib.http.exception.CustomException;
import com.dc.baselib.http.response.HttpResponse;
import com.dc.baselib.utils.ToastUtils;
import com.dc.baselib.utils.UserManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.reactivex.subscribers.DisposableSubscriber;
import okhttp3.ResponseBody;

//统一处理返回数据
public abstract class StrAbsHttpSubscriber extends DisposableSubscriber<ResponseBody> implements AbsSubscriberListener<String> {
    private Context mContext;

    public StrAbsHttpSubscriber(Context context) {
        this.mContext = context;
    }

    public StrAbsHttpSubscriber() {
    }

    private boolean forReal = false;

    public StrAbsHttpSubscriber(boolean forReal) {
        this.forReal = forReal;
    }

    @Override
    public void onNext(ResponseBody responsebody) {
        String str = null;
        try {
            str = responsebody.string();
            onSuccess(str);
//            JSONObject obj = new JSONObject(str);
//            int code = obj.optInt("code");
//            String msg = obj.optString("msg");
//            if (code == 0) {
//                if (forReal) {
//                    onSuccess(str);
//                } else {
//                    onSuccess(msg);
//
//                }
//            } else {
//                if (code == -50) {
//                    UserManager.getInstance().clearUser(BaseApplication.getsInstance());
//                }
//                onFailure(msg, code + "");
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        showLoading();

    }

    @Override
    public void onComplete() {
        hideLoading();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void onError(Throwable t) {
        hideLoading();
        ApiException e = CustomException.handlerServerException(t);
        onFailure(e.getMes(), e.getCode() + "");
    }

    @Override
    public void hideLoading() {

    }


}
