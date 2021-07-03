package com.zld.monitor.proj

import com.dc.baselib.BaseApplication
import com.dc.baselib.utils.SPUtils
import com.dc.commonlib.rabbit.RabbitService
import com.itc.screen_saver.service.ScreenSaverService

class ElectricNestApplication : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        ScreenSaverService.startScreenSaverService(this)
        RabbitService.startRabbitService(this)
        SPUtils.initSpUtils(this)
    }
}