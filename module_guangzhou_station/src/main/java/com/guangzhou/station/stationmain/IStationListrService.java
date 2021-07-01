package com.guangzhou.station.stationmain;


import com.dc.baselib.http.response.HttpResponse;
import com.guangzhou.station.playinfo.ProjectListBean;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IStationListrService {

    @GET(StationListService.stationlistdetsil)
    Flowable<HttpResponse<List<ProjectListBean>>> fetchStationList(@Query("serial_no") String serial_no);

}
