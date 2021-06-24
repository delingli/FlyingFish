package com.dc.baselib.mvvm;


import androidx.lifecycle.MutableLiveData;

import com.dc.baselib.http.newhttp.RetrofitClient;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import retrofit2.Retrofit;

public abstract class BaseRespository {
    private CompositeDisposable mCompositeDisposable;
    public MutableLiveData<String> loadState;
    protected Retrofit mRetrofit;

    public BaseRespository() {
        loadState = new MutableLiveData<>();
        mRetrofit = RetrofitClient.getInstance().getRetrofit();
    }

    //添加
    protected void addDisposable(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    public MutableLiveData<Object> mDataMutableLiveData;

    protected void postData(Object eventKey, String tag, Object t) {
        LiveBus.getDefault().postEvent(eventKey, tag, t);
    }

    protected void postData(Object eventKey, Object t) {
        postData(eventKey, null, t);
    }

    //通知
    protected void postState(String state) {
        if (loadState != null) {
            loadState.postValue(state);
        }

    }

    //移除
    public void unDisposable() {
        if (mCompositeDisposable != null && mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.clear();
        }
    }
}
