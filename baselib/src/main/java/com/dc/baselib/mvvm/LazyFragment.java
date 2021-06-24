package com.dc.baselib.mvvm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class LazyFragment<S extends AbsViewModel> extends AbsLifecycleFragment<S> {
    private boolean isViewCreated;
    private boolean isLoadDataComplete;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isLoadDataComplete && isViewCreated) {
            isLoadDataComplete = true;
            loadData();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isViewCreated = true;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView(View view) {
        super.initView(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint() && !isLoadDataComplete) {
            isLoadDataComplete = true;
            loadData();
        }
    }

    protected abstract void loadData();
}
