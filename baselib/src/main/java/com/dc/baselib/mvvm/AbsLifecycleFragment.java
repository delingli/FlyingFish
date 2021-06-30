package com.dc.baselib.mvvm;


import android.text.TextUtils;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;

public abstract class AbsLifecycleFragment<T extends AbsViewModel> extends BaseFragment {

    protected T mViewModel;
    private List<Object> eventKeys = new ArrayList<>();

    @Override
    public void initView(View view) {
        ViewModelProvider viewModelProvider = new ViewModelProvider(this);
        mViewModel = viewModelProvider.get((Class<T>) getViewModel());

    }

    protected abstract Class<T> getViewModel();

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

    public abstract void dataObserver();

    protected <T extends ViewModel> T VMProviders(BaseFragment fragment, @NonNull Class<T> modelClass) {
        return ViewModelProviders.of(fragment).get(modelClass);

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        clearEvent();
    }

    private void clearEvent() {
        if (eventKeys != null && eventKeys.size() > 0) {
            for (int i = 0; i < eventKeys.size(); i++) {
                LiveBus.getDefault().clear(eventKeys.get(i));
            }
        }
    }
}
