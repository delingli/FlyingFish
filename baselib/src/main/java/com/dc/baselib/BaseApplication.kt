package com.dc.baselib

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter

open class BaseApplication : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        registerActivityLifecycleCallbacks(callbacks);
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            //开启InstantRun之后，一定要在ARouter.init之前调用openDebug
            ARouter.openDebug()
            ARouter.openLog()
        }
        mInstance = this.applicationContext
        ARouter.init(this)
    }

    companion object {
        private lateinit var mInstance: Context;
        fun getInstances(): Context? {
            return mInstance
        }

    }

    private val callbacks: ActivityLifecycleCallbacks = object : ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            CurrentContentManager.getmInstance().setActivity(activity)
        }

        override fun onActivityStarted(activity: Activity) {
            TODO("Not yet implemented")
        }

        override fun onActivityResumed(activity: Activity) {
            TODO("Not yet implemented")
        }

        override fun onActivityPaused(activity: Activity) {
            TODO("Not yet implemented")
        }

        override fun onActivityStopped(activity: Activity) {
            TODO("Not yet implemented")
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            TODO("Not yet implemented")
        }

        override fun onActivityDestroyed(activity: Activity) {
            CurrentContentManager.getmInstance().setActivity(null)
        }
    }


}