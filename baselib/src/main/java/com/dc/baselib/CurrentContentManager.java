package com.dc.baselib;

import android.app.Activity;

public class CurrentContentManager {
    private volatile static CurrentContentManager mInstance;

    private CurrentContentManager() {
    }

    public static CurrentContentManager getmInstance() {
        if (mInstance == null) {
            synchronized (CurrentContentManager.class) {
                if (mInstance == null) {
                    mInstance = new CurrentContentManager();
                }
            }
        }
        return mInstance;
    }

    private Activity activity;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Activity getActivity() {
        return activity;
    }
}
