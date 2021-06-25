package com.dc.baselib.mvvm;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.blankj.utilcode.util.AdaptScreenUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class AbsLifecycleActivity<T extends AbsViewModel> extends BaseActivity {
    protected ViewModel mViewModel;
    private List<Object> eventKeys = new ArrayList<>();

    @Override
    protected void initView(Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(getViewModel());
    }

    protected abstract Class<ViewModel> getViewModel();

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
