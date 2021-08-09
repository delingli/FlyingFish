package com.zld.monitor.proj

import com.blankj.utilcode.util.LogUtils
import com.dc.baselib.BaseApplication
import com.dc.baselib.utils.SPUtils
import com.dc.commonlib.rabbit.RabbitService
import com.itc.screen_saver.service.ScreenSaverService
import com.tencent.mmkv.MMKV

class ElectricNestApplication : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        ScreenSaverService.startScreenSaverService(this)
        RabbitService.startRabbitService(this)
        SPUtils.initSpUtils(this)
        var rootDir = MMKV.initialize(this);
        LogUtils.dTag("ElectricNestApplication$rootDir")
        System.out.println("mmkv root: " + rootDir);
    }
}