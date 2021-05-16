package com.dc.baselib.mvvm;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;


import com.dc.baselib.livedata.LiveBus;

import java.util.ArrayList;
import java.util.List;

public abstract class AbsLifecycleActivity<T extends AbsViewModel> extends BaseActivity {
    protected T mViewModel;
    private List<Object> eventKeys = new ArrayList<>();

    @Override
    protected void initView(Bundle savedInstanceState) {
        mViewModel = VMProviders(this, (Class<T>) TUtil.getInstance(this, 0));
        dataObserver();
    }

    protected void dataObserver() {
//        observer
        if (mViewModel != null) {
            mViewModel.mRepository.loadState.observe(this, observer);
        }
    }


    protected <T extends ViewModel> T VMProviders(AppCompatActivity fragment, @NonNull Class modelClass) {
        return (T) ViewModelProviders.of(fragment).get(modelClass);

    }

    //默认实现用于监听网络请求响应的状态，提供给下层调用处理
    protected Observer observer = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String state) {
            if (!TextUtils.isEmpty(state)) {
                switch (state) {
                    case ContextStates.ERROR_STATE:
                        showErrorState();
                        break;
                    case ContextStates.NET_WORK_STATE:
                        showNetWorkError();
                        break;
                    case ContextStates.LOADING_STATE:
                        showloading();
                        break;
                    case ContextStates.SUCCESS_STATE:
                        showSucess();
                        break;
                    case ContextStates.NOT_DATA_STATE:
                        showNoDataLayout();
                        break;

                }

            }

        }
    };

    private void clearEvent() {
        if (eventKeys != null && eventKeys.size() > 0) {
            for (int i = 0; i < eventKeys.size(); i++) {
                LiveBus.getDefault().clear(eventKeys.get(i));
            }
        }
    }

    protected <T> MutableLiveData<T> registerSubscriber(Object eventKey, Class<T> tClass) {

        return registerSubscriber(eventKey, null, tClass);
    }

    protected <T> MutableLiveData<T> registerSubscriber(Object eventKey, String tag, Class<T> tClass) {
        String event;
        if (TextUtils.isEmpty(tag)) {
            event = (String) eventKey;
        } else {
            event = eventKey + tag;
        }
        eventKeys.add(event);
        return LiveBus.getDefault().subscribe(eventKey, tag, tClass);
    }

    protected void showErrorState() {
    }

    protected void showNoDataLayout() {
    }

    protected void showSucess() {
    }

    protected void showloading() {
    }

    protected void showNetWorkError() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearEvent();
    }
}
