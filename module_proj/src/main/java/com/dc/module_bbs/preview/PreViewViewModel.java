package com.dc.module_bbs.preview;

import android.app.Application;
import android.support.annotation.NonNull;

import com.dc.baselib.mvvm.AbsViewModel;

public class PreViewViewModel extends AbsViewModel<PreViewRepostory> {
    public PreViewViewModel(@NonNull Application application) {
        super(application);
    }
}
