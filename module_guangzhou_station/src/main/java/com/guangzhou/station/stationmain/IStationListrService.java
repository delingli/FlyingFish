package com.guangzhou.station.stationmain;


import com.dc.baselib.http.response.HttpResponse;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IStationListrService {

    @GET(StationListService.stationlistdetsil)
    Flowable<HttpResponse<ProjectListBean>> fetchStationList(@Query("serial_no") String serial_no);


    @GET(StationListService.keyWordList)
    Flowable<HttpResponse<KeywordListBean>> fetchKeywordList(@Query("serial_no") String serial_no,@Query("search") String search);

    @GET(StationListService.searchDetails)
    Flowable<HttpResponse<ProjectListBean>> fetchSearchDetailsList(@Query("serial_no") String serial_no,@Query("id") int id,@Query("search") String search);

}
