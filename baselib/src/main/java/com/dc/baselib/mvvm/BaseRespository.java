package com.dc.baselib.mvvm;


import androidx.lifecycle.MutableLiveData;

import com.dc.baselib.http.newhttp.RetrofitClient;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import retrofit2.Retrofit;

public abstract class BaseRespository {
    private CompositeDisposable mCompositeDisposable;
    protected Retrofit mRetrofit;

    public BaseRespository() {
        mRetrofit = RetrofitClient.getInstance().getRetrofit();
    }

    //添加
    protected void addDisposable(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    //移除
    public void unDisposable() {
        if (mCompositeDisposable != null && mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.clear();
        }
    }
}
