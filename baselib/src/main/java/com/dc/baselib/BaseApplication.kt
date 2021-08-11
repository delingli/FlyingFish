package com.dc.baselib

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.danikula.videocache.HttpProxyCacheServer

open class BaseApplication : Application() {


    private var proxy: HttpProxyCacheServer? = null



    private fun newProxy(): HttpProxyCacheServer? {
        return HttpProxyCacheServer.Builder(this).maxCacheSize(1024 * 1024 * 1024).build()
    }
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
        fun getProxy(context: Context): HttpProxyCacheServer? {
            val app: BaseApplication =
                context.getApplicationContext() as BaseApplication
            return if (app.proxy == null) app.newProxy().also {
                app.proxy = it
            } else app.proxy
        }
    }

    private val callbacks: ActivityLifecycleCallbacks = object : ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            CurrentContentManager.getmInstance().setActivity(activity)
        }

        override fun onActivityStarted(activity: Activity) {
        }

        override fun onActivityResumed(activity: Activity) {
        }

        override fun onActivityPaused(activity: Activity) {
        }

        override fun onActivityStopped(activity: Activity) {
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        }

        override fun onActivityDestroyed(activity: Activity) {
            CurrentContentManager.getmInstance().setActivity(null)
        }
    }


}