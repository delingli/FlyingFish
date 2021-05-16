package com.dc.module_main.ui.project;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class ProjectViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public ProjectViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("论坛");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
