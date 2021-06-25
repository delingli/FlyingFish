package com.itc.screen_saver.screensaver;

import com.dc.baselib.http.response.HttpResponse;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IScreenSaverService {

    @GET(ScreenSaverService.SCREENDETAIL)
    Flowable<HttpResponse<ScreenSaverEntiry>> fetchScreenSaver(@Query("serial_no") String serial_no);

}
