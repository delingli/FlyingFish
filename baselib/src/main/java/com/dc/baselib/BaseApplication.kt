package com.dc.baselib

import android.app.Application
import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter

open class BaseApplication : Application() {
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

}