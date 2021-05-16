package com.dc.baselib.http.newhttp;

import android.content.Context;
import android.util.Log;

import com.dc.baselib.BaseApplication;
import com.dc.baselib.http.exception.ApiException;
import com.dc.baselib.http.exception.CustomException;
import com.dc.baselib.http.response.HttpResponse;
import com.dc.baselib.utils.ToastUtils;
import com.dc.baselib.utils.UserManager;

import io.reactivex.subscribers.DisposableSubscriber;

//统一处理返回数据
public abstract class AbsHttpSubscriber<T> extends DisposableSubscriber<HttpResponse<T>> implements AbsSubscriberListener<T> {
    private Context mContext;

    public AbsHttpSubscriber(Context context) {
        this.mContext = context;
    }

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
        if (tHttpResponse.getCode() == 0) {//正确
            onSuccess(tHttpResponse.getData());
        } else {
            ApiException apiException = new ApiException(tHttpResponse.getCode(), tHttpResponse.getMsg());
            ApiException e = CustomException.handlerCustomException(apiException);
            if (tHttpResponse.getCode() == -50) {
                UserManager.getInstance().clearUser(BaseApplication.getsInstance());
            }
            onFailure(e.getMes(), e.getCode() + "");
        }
    }

    @Override
    public void onError(Throwable t) {
        hideLoading();
        ApiException e = CustomException.handlerServerException(t);
        onFailure(e.getMes(), e.getCode() + "");
    }

    @Override
    public void onComplete() {
        hideLoading();
    }

//    private LoadingDialog mLoadingDialog;

    @Override
    public void showLoading() {
/*        if (null != mContext) {
            mLoadingDialog = new LoadingDialog.Builder(mContext).setMessage("加载中...").createDialog();
            if (!mLoadingDialog.isShowing()) {
                mLoadingDialog.show();
            }
        }*/

    }

    @Override
    public void hideLoading() {
//        if (mLoadingDialog != null) {
//            mLoadingDialog.dismiss();
//            mLoadingDialog = null;
//        }
    }
}
